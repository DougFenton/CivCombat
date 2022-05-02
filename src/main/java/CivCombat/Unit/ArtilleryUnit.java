/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CivCombat.Unit;

/**
 * An artillery unit.
 */
public class ArtilleryUnit extends Unit {

  public ArtilleryUnit(int level, int baseAttack, int baseHealth) {
    super(UnitType.ARTILLERY, level, baseAttack, baseHealth);
  }

  public ArtilleryUnit(ArtilleryUnit unit) {
    super(unit);
  }

  @Override
  public boolean trumps(Unit unit) {
    return unit.getUnitType() == UnitType.INFANTRY;
  }

  @Override
  public Unit copyUnit() {
    return new ArtilleryUnit(this);
  }

  @Override
  public void printUnit() {
    System.out.print("      Unit Type: Artillery ");
    super.printUnit();
  }

  @Override
  public UnitType getUnitType() {
    return UnitType.ARTILLERY;
  }

  @Override
  public String toString() {
    return "Artillery" + super.toString();
  }
}
