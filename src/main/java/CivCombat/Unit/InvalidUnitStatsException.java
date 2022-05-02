package CivCombat.Unit;

/**
 * Thrown when a unit's attack and health are not valid for its level.
 */
public class InvalidUnitStatsException extends RuntimeException {

  public InvalidUnitStatsException(UnitType type, int level, int attack, int health) {
    super(String.format("Invalid stats given for unit of type %s: Level %s, attack %s, health %s", type, level, attack, health));
  }
}
