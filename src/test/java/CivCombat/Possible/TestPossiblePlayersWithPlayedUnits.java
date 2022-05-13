package CivCombat.Possible;

import CivCombat.Player.Player;
import CivCombat.Unit.ArtilleryUnit;
import CivCombat.Unit.InfantryUnit;
import CivCombat.Unit.MountedUnit;
import org.junit.jupiter.api.Test;

import java.util.List;

import static CivCombat.Unit.UnitType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link PossiblePlayersWithPlayedUnits}.
 */
public class TestPossiblePlayersWithPlayedUnits {

  @Test
  public void testPlayedUnits() {
    PossiblePlayersWithPlayedUnits possiblePlayersWithPlayedUnits = new PossiblePlayersWithPlayedUnits(List.of(new InfantryUnit(1, 1, 3)), village(), 3);

    assertEquals(9, possiblePlayersWithPlayedUnits.getNumberOfPlayers());
    for (int mountedAttack = 1; mountedAttack < 4; mountedAttack++) {
      for (int artilleryAttack = 1; artilleryAttack < 4; artilleryAttack++) {
        assertTrue(possiblePlayersWithPlayedUnits.getPlayers().contains(new Player(List.of(
            new MountedUnit(1, mountedAttack, 4 - mountedAttack),
            new ArtilleryUnit(1, artilleryAttack, 4 - artilleryAttack)
        ))));
      }
    }
  }

  private List<PossibleUnit> village() {
    return List.of(
        new PossibleUnit(INFANTRY, 1),
        new PossibleUnit(MOUNTED, 1),
        new PossibleUnit(ARTILLERY, 1)
    );
  }
}
