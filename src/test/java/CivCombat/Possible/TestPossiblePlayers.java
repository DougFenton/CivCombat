package CivCombat.Possible;

import CivCombat.Player.Player;
import CivCombat.Unit.ArtilleryUnit;
import CivCombat.Unit.InfantryUnit;
import CivCombat.Unit.MountedUnit;
import CivCombat.Unit.Unit;
import org.junit.jupiter.api.Test;

import java.util.List;

import static CivCombat.Unit.UnitType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Functional tests for {@link PossiblePlayers}.
 */
public class TestPossiblePlayers {

  @Test
  public void testVillagePlayer() {
    PossiblePlayers possiblePlayers = new PossiblePlayers(List.of(village()));
    assertEquals(3 * 3 * 3, possiblePlayers.getNumberOfPlayers());
    for (int i = 0; i < possiblePlayers.getPlayers().size(); i++) {
      List<Unit> units = possiblePlayers.getPlayers().get(i).getUnitsList();
      assertEquals(INFANTRY, units.get(0).getUnitType());
      assertEquals(MOUNTED, units.get(1).getUnitType());
      assertEquals(ARTILLERY, units.get(2).getUnitType());
    }

    for (int infantryAttack = 1; infantryAttack < 4; infantryAttack++) {
      for (int mountedAttack = 1; mountedAttack < 4; mountedAttack++) {
        for (int artilleryAttack = 1; artilleryAttack < 4; artilleryAttack++) {
          assertTrue(possiblePlayers.getPlayers().contains(new Player(List.of(
              new InfantryUnit(1, infantryAttack, 4 - infantryAttack),
              new MountedUnit(1, mountedAttack, 4 - mountedAttack),
              new ArtilleryUnit(1, artilleryAttack, 4 - artilleryAttack)
          ))));
        }
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
