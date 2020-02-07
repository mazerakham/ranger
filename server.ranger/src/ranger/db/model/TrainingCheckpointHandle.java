package ranger.db.model;

import java.io.File;
import java.util.UUID;

import ox.IO;
import ox.OS;
import ranger.nn.train.TrainingCheckpoint;

public class TrainingCheckpointHandle extends AbstractModel {

  private static final String basePath = OS.getAppFolder("ranger").getPath() + "/training-checkpoints";

  /**
   * 
   */
  public final long sessionId;

  /**
   * checkpoints for a given session will count up from zero.
   */
  public final long checkpoint;

  /**
   * The training step in which the checkpoint was created.
   */
  public final long trainingStep;

  /**
   * Identifies the file where checkpoint is saved.
   */
  public final UUID checkpointUUID;

  public static TrainingCheckpointHandle createHandle(TrainingCheckpoint checkpoint) {
    UUID checkpointUUID = UUID.randomUUID();
    saveCheckpointToFile(checkpoint, checkpointUUID);
    return new TrainingCheckpointHandle(checkpoint.sessionId, checkpoint.checkpointNumber, checkpoint.trainingStep,
        checkpointUUID);
  }

  private static void saveCheckpointToFile(TrainingCheckpoint checkpoint, UUID checkpointUUID) {
    IO.from(checkpoint.toJson()).to(new File(getPath(checkpointUUID)));
  }

  private static String getPath(UUID uuid) {
    return basePath + "/checkpoint_" + uuid + ".json";
  }

  private TrainingCheckpointHandle(long sessionId, long checkpoint, long trainingStep, UUID checkpointUUID) {
    this.sessionId = sessionId;
    this.checkpoint = checkpoint;
    this.trainingStep = trainingStep;
    this.checkpointUUID = checkpointUUID;
  }

}
