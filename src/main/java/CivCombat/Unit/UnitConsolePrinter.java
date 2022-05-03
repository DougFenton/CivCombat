package CivCombat.Unit;

/**
 * Displays information about units to the console.
 */
public class UnitConsolePrinter {

  public static void print(Unit unit, int indent) {
    System.out.print("  ".repeat(indent) + unitInfo(unit));
  }

  public static void print(Unit unit) {
    print(unit, 0);
  }

  public static String unitInfo(Unit unit) {
    return String.format("Unit { type = %s, level = %s, attack = %s, health = %s, wounds = %s }%n", unit.getUnitType(), unit.getLevel(), unit.getAttack(), unit.getHealth(), unit.getWounds());
  }
}
