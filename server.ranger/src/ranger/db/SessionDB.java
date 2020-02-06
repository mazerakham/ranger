package ranger.db;

import ez.Table;
import ranger.db.model.Session;

public class SessionDB extends RangerDB<Session> {

  @Override
  protected Table getTable() {
    return new Table("session")
        .idColumn()
        .column("datasetHandleId", Long.class);
  }

}
