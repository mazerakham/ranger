package ranger.db;

import java.util.UUID;

import ez.Table;
import ranger.db.model.TrainingCheckpointHandle;

public class TrainingCheckpointHandleDB extends RangerDB<TrainingCheckpointHandle> {

  @Override
  protected Table getTable() {
    return new Table("training_step")
        .idColumn()
        .column("sessionId", Long.class).index()
        .column("checkpoint", Long.class)
        .column("checkpointUUID", UUID.class);
  }

  public TrainingCheckpointHandle getTrainingStep(long sessionId, long trainingStep) {
    return this.table
        .fromRow(this.db.selectSingleRow("SELECT * FROM training_set WHERE `sessionId' = ? AND `trainingStep' = ?",
            sessionId, trainingStep), TrainingCheckpointHandle.class);
  }

}
