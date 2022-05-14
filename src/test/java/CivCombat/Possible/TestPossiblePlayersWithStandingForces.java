package CivCombat.Possible;

import CivCombat.Player.Player;
import org.junit.jupiter.api.Test;

import java.util.List;

import static CivCombat.Unit.UnitType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for {@link PossiblePlayersWithStandingForces}.
 */
public class TestPossiblePlayersWithStandingForces {

  @Test
  public void testPicksRightNumberOfUnits() {
    PossiblePlayersWithStandingForces possiblePlayersWithStandingForces = new PossiblePlayersWithStandingForces(sixUnits(), 4);
    for (Player player : possiblePlayersWithStandingForces.getPlayers()) {
      assertEquals(4, player.getUnitsList().size());
    }
    int distinctPlayers = 8; // Of the 6C4=15 players, only 8 turn out to have distinct units.
    assertEquals(distinctPlayers * unitPossibilities(4), possiblePlayersWithStandingForces.getNumberOfPlayers());
  }

  @Test
  public void testRemovesDuplicates() {
    PossiblePlayersWithStandingForces possiblePlayersWithStandingForces = new PossiblePlayersWithStandingForces(sixInfantry(), 3);
    assertEquals(unitPossibilities(3), possiblePlayersWithStandingForces.getNumberOfPlayers());
  }

  @Test
  public void testPerformance() {
    PossiblePlayersWithStandingForces possiblePlayersWithStandingForces = new PossiblePlayersWithStandingForces(sixteenUnits(), 8);
    for (Player player : possiblePlayersWithStandingForces.getPlayers()) {
      assertEquals(8, player.getUnitsList().size());
    }

    int distinctPlayers = 72; // Of the 16C8=12870 players, only 72 turn out to have distinct units.
    assertEquals(distinctPlayers * unitPossibilities(8), possiblePlayersWithStandingForces.getNumberOfPlayers());
  }

  private int unitPossibilities(int numberOfUnits) {
    return (int) Math.pow(3, numberOfUnits);
  }

  private List<PossibleUnit> sixUnits() {
    return List.of(
        new PossibleUnit(INFANTRY, 2),
        new PossibleUnit(MOUNTED, 2),
        new PossibleUnit(AIRCRAFT, 4),
        new PossibleUnit(ARTILLERY, 3),
        new PossibleUnit(ARTILLERY, 3),
        new PossibleUnit(AIRCRAFT, 4)
    );
  }

  private List<PossibleUnit> sixInfantry() {
    return List.of(
        new PossibleUnit(INFANTRY, 4),
        new PossibleUnit(INFANTRY, 4),
        new PossibleUnit(INFANTRY, 4),
        new PossibleUnit(INFANTRY, 4),
        new PossibleUnit(INFANTRY, 4),
        new PossibleUnit(INFANTRY, 4)
    );
  }

  private List<PossibleUnit> sixteenUnits() {
    return List.of(
        new PossibleUnit(INFANTRY, 2),
        new PossibleUnit(MOUNTED, 2),
        new PossibleUnit(AIRCRAFT, 4),
        new PossibleUnit(ARTILLERY, 3),
        new PossibleUnit(ARTILLERY, 3),
        new PossibleUnit(INFANTRY, 2),
        new PossibleUnit(MOUNTED, 2),
        new PossibleUnit(AIRCRAFT, 4),
        new PossibleUnit(ARTILLERY, 3),
        new PossibleUnit(ARTILLERY, 3),
        new PossibleUnit(INFANTRY, 2),
        new PossibleUnit(MOUNTED, 2),
        new PossibleUnit(AIRCRAFT, 4),
        new PossibleUnit(ARTILLERY, 3),
        new PossibleUnit(ARTILLERY, 3),
        new PossibleUnit(AIRCRAFT, 4)
    );
  }
}
