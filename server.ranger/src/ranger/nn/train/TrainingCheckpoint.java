package ranger.nn.train;

import ox.Json;

public class TrainingCheckpoint {

  public final long sessionId;

  public final long trainingStep;

  public final long checkpointNumber;

  public TrainingCheckpoint(long sessionId, long trainingStep, long checkpointNumber) {
    this.sessionId = sessionId;
    this.trainingStep = trainingStep;
    this.checkpointNumber = checkpointNumber;
  }

  public Json toJson() {
    return Json.object();
  }
}
