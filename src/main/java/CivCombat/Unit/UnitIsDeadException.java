package CivCombat.Unit;

/**
 * Thrown when a dead unit is interacted with.
 */
public class UnitIsDeadException extends RuntimeException {

  public UnitIsDeadException() {
    super("Unit is dead, cannot be changed.");
  }
}
