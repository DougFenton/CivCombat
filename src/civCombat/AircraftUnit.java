/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package civCombat;

/**
 * @author Doug
 */
public class AircraftUnit extends Unit {

  public AircraftUnit(int baseAttack, int baseHealth) {
    super(baseAttack, baseHealth);
  }

  public AircraftUnit(AircraftUnit unit) {
    super(unit);
  }

  @Override
  public boolean trumps(Unit unit) {
    return false;
  }

  @Override
  public Unit copyUnit() {
    return new AircraftUnit(this);
  }

  @Override
  public void printUnit() {
    System.out.print("      Unit Type: Aircraft ");
    super.printUnit();
  }

  @Override
  public String getUnitType() {
    return "Aircraft";
  }

  @Override
  public String toString() {
    return "Aircraft" + super.toString();
  }
}
