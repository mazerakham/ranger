package ranger.db;

import java.util.UUID;

import ez.Table;
import ranger.db.model.DatasetHandle;

public class DatasetHandleDB extends RangerDB<DatasetHandle> {

  @Override
  protected Table getTable() {
    return new Table("dataset_handle")
        .idColumn()
        .column("fileUUID", UUID.class);
  }

}
