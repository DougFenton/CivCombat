package CivCombat.Unit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for units.
 */
public class TestUnit {

  @Test
  public void testTrumps() {
    InfantryUnit infantryUnit = new InfantryUnit(1, 1, 3);
    MountedUnit mountedUnit = new MountedUnit(1, 1, 3);
    ArtilleryUnit artilleryUnit = new ArtilleryUnit(1, 1, 3);
    AircraftUnit aircraftUnit = new AircraftUnit(5, 7); // Is 5/7 right?

    assertAll(
        () -> assertTrue(infantryUnit.trumps(mountedUnit)),
        () -> assertTrue(mountedUnit.trumps(artilleryUnit)),
        () -> assertTrue(artilleryUnit.trumps(infantryUnit))
    );

    assertAll(
        () -> assertFalse(infantryUnit.trumps(infantryUnit)),
        () -> assertFalse(infantryUnit.trumps(artilleryUnit)),
        () -> assertFalse(infantryUnit.trumps(aircraftUnit)),
        () -> assertFalse(mountedUnit.trumps(infantryUnit)),
        () -> assertFalse(mountedUnit.trumps(mountedUnit)),
        () -> assertFalse(mountedUnit.trumps(aircraftUnit)),
        () -> assertFalse(artilleryUnit.trumps(mountedUnit)),
        () -> assertFalse(artilleryUnit.trumps(artilleryUnit)),
        () -> assertFalse(artilleryUnit.trumps(aircraftUnit)),
        () -> assertFalse(aircraftUnit.trumps(infantryUnit)),
        () -> assertFalse(aircraftUnit.trumps(mountedUnit)),
        () -> assertFalse(aircraftUnit.trumps(artilleryUnit)),
        () -> assertFalse(aircraftUnit.trumps(aircraftUnit))
    );

  }

  @Test
  public void testStatsMatchLevel() {
    assertDoesNotThrow(() -> new InfantryUnit(1, 1, 3));
    assertDoesNotThrow(() -> new MountedUnit(2, 3, 3));
    assertDoesNotThrow(() -> new ArtilleryUnit(3, 5, 3));
    assertDoesNotThrow(() -> new AircraftUnit(6, 6));

    assertThrows(InvalidUnitStatsException.class, () -> new InfantryUnit(5, 1, 1));
    assertThrows(InvalidUnitStatsException.class, () -> new MountedUnit(1, 1, 4));
    assertThrows(InvalidUnitStatsException.class, () -> new ArtilleryUnit(4, 5, 6));
    assertThrows(InvalidUnitStatsException.class, () -> new AircraftUnit(4, 7));
  }

  @Test
  public void testWounds() {
    InfantryUnit unit = new InfantryUnit(1, 1, 3);
    assertEquals(0, unit.getWounds());
    assertFalse(unit.isDead());

    unit.applyWounds(1);
    assertEquals(1, unit.getWounds());
    assertFalse(unit.isDead());

    // Overheal sets wounds to 0.
    unit.removeWounds(3);
    assertEquals(0, unit.getWounds());

    unit.applyWounds(2);
    unit.removeWounds();
    assertEquals(0, unit.getWounds());

    unit.applyWounds(1);
    unit.applyWounds(2);
    assertEquals(3, unit.getWounds());
    assertTrue(unit.isDead());

    // Unit is already dead.
    assertThrows(UnitIsDeadException.class, () -> unit.applyWounds(1));
    assertEquals(3, unit.getWounds());
    assertTrue(unit.isDead());

    assertThrows(UnitIsDeadException.class, unit::removeWounds);
    assertThrows(UnitIsDeadException.class, () -> unit.removeWounds(2));
  }
}
