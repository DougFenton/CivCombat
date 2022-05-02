/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CivCombat.Unit;

/**
 * An infantry unit.
 */
public class InfantryUnit extends Unit {

  public InfantryUnit(int baseAttack, int baseHealth) {
    super(baseAttack, baseHealth);
  }

  public InfantryUnit(InfantryUnit unit) {
    super(unit);
  }

  @Override
  public boolean trumps(Unit unit) {
    //return ("Mounted".equals(unit.type));
    return unit instanceof MountedUnit;
  }

  @Override
  public Unit copyUnit() {
    return new InfantryUnit(this);
  }

  @Override
  public void printUnit() {
    System.out.print("      Unit Type: Infantry ");
    super.printUnit();
  }

  @Override
  public String getUnitType() {
    return "Infantry";
  }

  @Override
  public String toString() {
    return "Infantry" + super.toString();
  }


}
