package ranger.db.model;

import java.io.File;
import java.util.UUID;

import ox.IO;
import ox.OS;
import ranger.data.sets.Dataset;

public class DatasetHandle extends AbstractModel {

  public final UUID datasetFileId;

  private static final String datasetsPath = OS.getAppFolder("ranger").getPath() + "/datasets";

  public static DatasetHandle createDatasetHandle(Dataset dataset) {
    UUID uuid = UUID.randomUUID();
    DatasetHandle handle = new DatasetHandle(uuid);
    saveDatasetToFile(dataset, uuid);
    return handle;
  }

  private static String getDatasetPath(UUID uuid) {
    return datasetsPath + "/dataset_" + uuid + ".json";
  }

  private static void saveDatasetToFile(Dataset dataset, UUID uuid) {
    IO.from(dataset.toJson()).to(new File(getDatasetPath(uuid)));
  }

  private Dataset getDataset() {
    return Dataset.fromJson(null);
  }

  public DatasetHandle(UUID uuid) {
    datasetFileId = uuid;
  }
}
