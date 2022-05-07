package CivCombat;

import CivCombat.Player.Player;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link MinimaxCombat}.
 */
public class TestMinimaxCombat {

  @Test
  public void testAllUnitsPlayed() {
    Player attacker = new Player(Collections.emptySet());
    Player defender = new Player(Collections.emptySet());
    Battlefield battlefield = new Battlefield(attacker, defender);
    MinimaxCombat minimaxCombat = new MinimaxCombat(battlefield);

    ActionAndResult actionAndResult = minimaxCombat.determineWinner(true);
    assertTrue(actionAndResult.result());
    // This is the only case the action will be null, when there are no possible actions.
    assertNull(actionAndResult.action());
  }

  @Test
  public void testOnlyAttackerUnits() {
    Player attacker = ConsoleCombat.getSampleAttacker();
    Player defender = new Player(Collections.emptySet());
    Battlefield battlefield = new Battlefield(attacker, defender);
    MinimaxCombat minimaxCombat = new MinimaxCombat(battlefield);

    ActionAndResult actionAndResult = minimaxCombat.determineWinner(true);
    assertFalse(actionAndResult.result());
    // Returns the winning action for the attacker, even though it's the defender turn.
    // The action knows the turn, so this can be checked if required.
    assertEquals(battlefield.getAttackerOptions().get(0), actionAndResult.action());
  }

  @Test
  public void testOnlyDefenderUnits() {
    Player attacker = new Player(Collections.emptySet());
    Player defender = ConsoleCombat.getSampleDefender();
    Battlefield battlefield = new Battlefield(attacker, defender);
    MinimaxCombat minimaxCombat = new MinimaxCombat(battlefield);

    ActionAndResult actionAndResult = minimaxCombat.determineWinner(true);
    assertTrue(actionAndResult.result());
    // The first action will be winning
    assertEquals(battlefield.getDefenderOptions().get(0), actionAndResult.action());
  }

  @Test
  public void testSampleCombat() {
    Player attacker = ConsoleCombat.getSampleAttacker();
    Player defender = ConsoleCombat.getSampleDefender();
    Battlefield battlefield = new Battlefield(attacker, defender);
    MinimaxCombat minimaxCombat = new MinimaxCombat(battlefield);

    ActionAndResult actionAndResult = minimaxCombat.determineWinner(true);

    assertNotNull(actionAndResult.action());
    assertFalse(actionAndResult.result()); //Probably.

  }

}
