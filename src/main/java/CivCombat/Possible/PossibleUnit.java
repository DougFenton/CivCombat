/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CivCombat.Possible;

import CivCombat.Unit.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static CivCombat.Unit.UnitType.AIRCRAFT;

/**
 * The possible units for a given type and level.
 */
public class PossibleUnit implements Comparable<PossibleUnit> {

  private final UnitType type;
  private final int level;

  private List<Unit> possibleUnits;

  public PossibleUnit(UnitType type, int level) {
    if (type == AIRCRAFT && level != 4) {
      throw new IllegalArgumentException("Aircraft must be level 4");
    }
    this.type = type;
    this.level = level;
    possibleUnits = null;
  }

  public PossibleUnit(PossibleUnit p) {
    type = p.type;
    level = p.level;
    possibleUnits = null;
  }

  private static List<Unit> generateUnits(UnitType type, int level) {
    List<Unit> possibleUnits = new ArrayList<>();
    switch (type) {
      case INFANTRY -> {
        for (int i = 0; i < 3; i++) {
          possibleUnits.add(new InfantryUnit(level, level + i, level + 2 - i));
        }
      }
      case MOUNTED -> {
        for (int i = 0; i < 3; i++) {
          possibleUnits.add(new MountedUnit(level, level + i, level + 2 - i));
        }
      }
      case ARTILLERY -> {
        for (int i = 0; i < 3; i++) {
          possibleUnits.add(new ArtilleryUnit(level, level + i, level + 2 - i));
        }
      }
      case AIRCRAFT -> {
        for (int i = 0; i < 3; i++) {
          // Aircraft are one of each stat stronger.
          possibleUnits.add(new AircraftUnit(level + i + 1, level + 2 - i + 1));
        }
      }
    }
    return possibleUnits;
  }

  private static int orderValue(PossibleUnit possibleUnit) {
    int result = 1;
    result = 31 * result + possibleUnit.type.ordinal();
    result = 31 * result + possibleUnit.level;
    return result;
  }

  public PossibleUnit copy() {
    return new PossibleUnit(this);
  }

  public UnitType getType() {
    return type;
  }

  public List<Unit> getPossibleUnits() {
    if (possibleUnits == null) {
      possibleUnits = generateUnits(type, level);
    }
    return possibleUnits;
  }

  public int getNumberPossible() {
    return possibleUnits.size();
  }

  // Equals and hashcode ignore possibleUnits, as that should be determined by the level and type.

  /**
   * @return true if the given unit could be one of these represented by this PossibleUnit.
   */
  public boolean matches(Unit unit) {
    return type == unit.getUnitType() && level == unit.getLevel();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PossibleUnit that = (PossibleUnit) o;
    return level == that.level && type == that.type;
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, level);
  }

  @Override
  public int compareTo(PossibleUnit other) {
    return Integer.compare(orderValue(this), orderValue(other));
  }
}
