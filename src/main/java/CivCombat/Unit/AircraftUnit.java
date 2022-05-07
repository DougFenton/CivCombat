/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CivCombat.Unit;

/**
 * An aircraft unit. Always level 4.
 */
public class AircraftUnit extends Unit {

  public AircraftUnit(int baseAttack, int baseHealth) {
    super(UnitType.AIRCRAFT, 4, baseAttack, baseHealth);
  }

  public AircraftUnit(AircraftUnit unit) {
    super(unit);
  }

  @Override
  public boolean trumps(Unit unit) {
    return false;
  }

  @Override
  public Unit copy() {
    return new AircraftUnit(this);
  }

  @Override
  public UnitType getUnitType() {
    return UnitType.AIRCRAFT;
  }

}
