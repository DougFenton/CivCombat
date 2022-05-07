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

  public MountedUnit(int level, int baseAttack, int baseHealth) {
    super(UnitType.MOUNTED, level, baseAttack, baseHealth);
  }

  public MountedUnit(MountedUnit unit) {
    super(unit);
  }

  @Override
  public boolean trumps(Unit unit) {
    return unit.getUnitType() == UnitType.ARTILLERY;
  }

  @Override
  public Unit copy() {
    return new MountedUnit(this);
  }

  @Override
  public UnitType getUnitType() {
    return UnitType.MOUNTED;
  }

}
