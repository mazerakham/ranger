package ranger.db;

import static com.google.common.base.Preconditions.checkState;
import static ox.util.Functions.index;
import static ox.util.Functions.map;
import static ox.util.Functions.splice;
import static ox.util.Utils.only;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import ez.DB;
import ez.Row;
import ez.Table;
import ox.Config;
import ox.Log;
import ox.Reflection;

public abstract class RangerDB<T> {

  private static final Config config = Config.load("ranger");

  public static DB db;
  public static Set<String> tables;

  public final Table table;

  @SuppressWarnings("unchecked")
  protected final Class<T> modelClass = (Class<T>) Reflection.getGenericClass(getClass());

  public RangerDB() {
    table = getTable();
    if (tables == null) {
      connectToDatabase();
    }
    if (!tables.contains(table.name.toLowerCase())) {
      Log.info("Creating table: " + table);
      db.addTable(table);
      tables.add(table.name.toLowerCase());
    }
  }

  public void truncate() {
    db.truncate(table.name);
    db.execute("ALTER TABLE `" + table.name + "` AUTO_INCREMENT = 1");
  }

  public T get(Long id) {
    if (id == null) {
      return null;
    }
    Row row = db.selectSingleRow("SELECT * FROM `" + table.name + "` WHERE id = ?", id);
    return table.fromRow(row, modelClass);
  }

  public List<T> get(Iterable<Long> ids) {
    Iterator<Long> iter = ids.iterator();
    if (!iter.hasNext()) {
      return ImmutableList.of();
    }
    return getResults("SELECT * FROM `" + table.name + "` WHERE id IN (" + Joiner.on(',').join(iter) + ")");
  }

  public List<T> getInOrder(Iterable<Long> ids) {
    Map<Long, T> idModels = index(get(ids), t -> Reflection.get(t, "id"));
    return map(ids, idModels::get);
  }

  public T getUnique(String indexName, Object key) {
    return only(getIndexed(indexName, key));
  }

  public List<T> getIndexed(String indexName, Object key) {
    return getResults("SELECT * FROM `" + table.name + "` WHERE " + indexName + " = ?", key);
  }

  public ResultsWindow<T> getIndexed(String indexName, Object key, int offset, int count) {
    return getIndexed(indexName, key, offset, count, null);
  }

  public ResultsWindow<T> getIndexed(String indexName, Object key, int offset, int count, String orderBy) {
    StringBuilder sb = new StringBuilder("SELECT * FROM `").append(table.name).append("` WHERE ").append(indexName)
        .append(" = ?");
    if (orderBy != null) {
      sb.append(" ORDER BY " + orderBy);
    }
    sb.append(" LIMIT ?,?");
    List<T> results = getResults(sb.toString(), key, offset, count);
    long total = db.getCount("SELECT COUNT(*) FROM `" + table.name + "` WHERE " + indexName + " = ?", key);
    return new ResultsWindow<>(results, total);
  }

  public List<T> getIndexed(String indexName, Collection<? extends Object> keys) {
    if (keys.isEmpty()) {
      return ImmutableList.of();
    }
    return getResults("SELECT * FROM `" + table.name +
        "` WHERE " + indexName + " IN (" + Joiner.on(',').join(Collections.nCopies(keys.size(), "?")) + ")",
        keys.toArray());
  }

  public List<T> getAll() {
    return getResults("SELECT * FROM `" + table.name + "`");
  }

  public void streamAll(Consumer<T> callback) {
    db.stream("SELECT * FROM `" + table.name + "`", row -> {
      T model = table.fromRow(row, modelClass);
      callback.accept(model);
    });
  }

  protected List<T> getResults(String query, Object... args) {
    List<T> ret = Lists.newArrayList();
    db.stream(query, row -> ret.add(table.fromRow(row, modelClass)), args);
    return ret;
  }

  public void insert(Collection<T> objects) {
    List<Row> rows = table.toRows(objects);
    db.insert(table.name, rows);

    splice(objects, rows, (o, row) -> {
      Reflection.set(o, "id", row.getId());
    });
  }

  public void insert(T o) {
    Row row = table.toRow(o);
    long id = db.insert(table.name, row);
    Reflection.set(o, "id", id);
  }

  public void replace(T o) {
    replace(ImmutableList.of(o));
  }

  public void replace(Collection<T> objects) {
    List<Row> rows = table.toRows(objects);
    db.replace(table.name, rows);

    splice(objects, rows, (o, row) -> {
      Reflection.set(o, "id", row.getId());
    });
  }

  public void update(T o) {
    db.update(table.name, table.toRow(o));
  }

  public void update(long id, String columnName, Object value) {
    db.update("UPDATE `" + table.name + "` SET `" + columnName + "` = ? WHERE id = ?", value, id);
  }

  public void delete(long id) {
    db.update("DELETE FROM `" + table.name + "` WHERE id = ?", id);
  }

  public void delete(Collection<Long> ids) {
    if (ids.isEmpty()) {
      return;
    }
    db.update("DELETE FROM `" + table.name + "` WHERE id IN(" + Joiner.on(',').join(ids) + ")");
  }

  public void transaction(Runnable r) {
    db.transaction(r);
  }

  protected String nQuestionMarks(int n) {
    return Joiner.on(',').join(Collections.nCopies(n, '?'));
  }

  public static void connectToDatabase() {
    String schema = config.get("mysql.schema", "ranger");

    connectToDatabase(schema);
  }

  public static synchronized void connectToDatabase(String schema) {
    checkState(db == null, "Already connected!");

    String ip = config.get("mysql.ip", "localhost");
    String user = config.get("mysql.user", "root");

    db = new DB(ip, user, config.get("mysql.password", ""), schema, false).ensureSchemaExists();
    tables = db.getTables();
  }

  public static class ResultsWindow<T> {
    public final List<T> results;
    public final long total;

    public ResultsWindow(List<T> results, long total) {
      this.results = results;
      this.total = total;
    }
  }

  protected abstract Table getTable();

  public DB rawQuery() {
    return db;
  }

}
