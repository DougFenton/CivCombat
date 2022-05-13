package CivCombat.Possible;

import CivCombat.Player.Player;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static CivCombat.Unit.UnitType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for {@link PossiblePlayersWithStandingForces}.
 */
public class TestPossiblePlayersWithStandingForces {

  private static final int SIX_CHOOSE_FOUR = 15;

  @Test
  public void testPicksRightNumberOfUnits() {
    PossiblePlayersWithStandingForces possiblePlayersWithStandingForces = new PossiblePlayersWithStandingForces(sixUnits(), 4);
    for (Player player : possiblePlayersWithStandingForces.getPlayers()) {
      assertEquals(4, player.getUnitsList().size());
    }
    final int unitPossibilities = (int) Math.pow(3, 4);
    assertEquals(SIX_CHOOSE_FOUR * unitPossibilities, possiblePlayersWithStandingForces.getNumberOfPlayers());
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

  @Test
  @Disabled // runs out of heap space
  public void testPerformance() {
    PossiblePlayersWithStandingForces possiblePlayersWithStandingForces = new PossiblePlayersWithStandingForces(sixteenUnits(), 8);
    for (Player player : possiblePlayersWithStandingForces.getPlayers()) {
      assertEquals(8, player.getUnitsList().size());
    }
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
