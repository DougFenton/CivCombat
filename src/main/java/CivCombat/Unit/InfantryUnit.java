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

  public InfantryUnit(int level, int baseAttack, int baseHealth) {
    super(UnitType.INFANTRY, level, baseAttack, baseHealth);
  }

  public InfantryUnit(InfantryUnit unit) {
    super(unit);
  }

  @Override
  public boolean trumps(Unit unit) {
    return unit.getUnitType() == UnitType.MOUNTED;
  }

  @Override
  public Unit copy() {
    return new InfantryUnit(this);
  }

  @Override
  public UnitType getUnitType() {
    return UnitType.INFANTRY;
  }

}
