/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CivCombat.Unit;


/**
 * A mounted unit.
 */
public class MountedUnit extends Unit {

  public MountedUnit(int baseAttack, int baseHealth) {
    super(baseAttack, baseHealth);
  }

  public MountedUnit(MountedUnit unit) {
    super(unit);
  }

  @Override
  public boolean trumps(Unit unit) {
    //return ("Artillery".equals(unit.type));
    return unit instanceof ArtilleryUnit;
  }

  @Override
  public Unit copyUnit() {
    return new MountedUnit(this);
  }

  @Override
  public void printUnit() {
    System.out.print("      Unit Type: Mounted ");
    super.printUnit();
  }

  @Override
  public String getUnitType() {
    return "Mounted";
  }

  @Override
  public String toString() {
    return "Mounted" + super.toString();
  }
}
