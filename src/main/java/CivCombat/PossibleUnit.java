/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CivCombat;

import CivCombat.Unit.*;

import java.util.ArrayList;
import java.util.List;

/**
 * The possible units for a given type and level.
 */
public class PossibleUnit {

  private final String unitType;
  private final int unitLevel;
  private List<Unit> possibleUnits;

  public PossibleUnit(String unitType, int unitLevel) {
    /* Type is one of: "Infantry", "Mounted", "Artillery", "Aircraft" */
    this.unitType = unitType;
    /* Level is one of: 1,2,3,4 */
    this.unitLevel = unitLevel;

    generateUnits();
  }

  public PossibleUnit(PossibleUnit p) {
    this.unitType = p.unitType;
    this.unitLevel = p.unitLevel;
    generateUnits();
  }

  public PossibleUnit copy() {
    return new PossibleUnit(this);
  }

  private void generateUnits() {
    if ("Aircraft".equals(unitType) && unitLevel != 4) {
      throw new IllegalArgumentException("Aircraft level is not 4");
    }
    possibleUnits = new ArrayList<>();
    if ("Infantry".equals(unitType)) {
      for (int i = 0; i < 3; i++) {
        possibleUnits.add(new InfantryUnit(unitLevel, unitLevel + i, unitLevel + 2 - i));
      }
    } else if ("Mounted".equals(unitType)) {
      for (int i = 0; i < 3; i++) {
        possibleUnits.add(new MountedUnit(unitLevel, unitLevel + i, unitLevel + 2 - i));
      }
    } else if ("Artillery".equals(unitType)) {
      for (int i = 0; i < 3; i++) {
        possibleUnits.add(new ArtilleryUnit(unitLevel, unitLevel + i, unitLevel + 2 - i));
      }
    } else {
      for (int i = 0; i < 4; i++) {
        possibleUnits.add(new AircraftUnit(unitLevel + i, unitLevel + 2 - i));
      }
    }
  }

  public String getUnitType() {
    return unitType;
  }

  public int numberPossible() {
    return possibleUnits.size();
  }

  public Unit getUnit(int i) {
    return possibleUnits.get(i);
  }
}
