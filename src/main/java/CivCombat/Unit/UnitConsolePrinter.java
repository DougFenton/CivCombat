package CivCombat.Unit;

/**
 * Displays information about units to the console.
 */
public class UnitConsolePrinter {

  public static void print(Unit unit, int indent) {
    System.out.printf("  ".repeat(indent) + "Unit { type = %s, level = %s, attack = %s, health = %s, wounds = %s }%n", unit.getUnitType(), unit.getLevel(), unit.getAttack(), unit.getHealth(), unit.getWounds());
  }

  public static void print(Unit unit) {
    print(unit, 0);
  }
}
