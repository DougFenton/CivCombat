/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package civCombat;

/**
 * @author Doug
 */
public class ArtilleryUnit extends Unit {

  public ArtilleryUnit(int baseAttack, int baseHealth) {
    super(baseAttack, baseHealth);
  }

  public ArtilleryUnit(ArtilleryUnit unit) {
    super(unit);
  }

  @Override
  public boolean trumps(Unit unit) {
    //return ("Infantry".equals(unit.type));
    return unit instanceof InfantryUnit;
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
  public String getUnitType() {
    return "Artillery";
  }

  @Override
  public String toString() {
    return "Artillery" + super.toString();
  }
}
