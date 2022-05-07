package CivCombat;

import CivCombat.Player.Player;
import CivCombat.Player.PlayerAction;
import CivCombat.Unit.Unit;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static java.util.Optional.empty;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link Battlefield}.
 */
public class TestBattlefield {

  @Test
  public void testCopy() {
    Player attacker = ConsoleCombat.getSampleAttacker();
    Player defender = ConsoleCombat.getSampleDefender();
    Battlefield battlefield = new Battlefield(attacker, defender);

    assertEquals(battlefield, battlefield.copy());
    assertNotSame(battlefield, battlefield.copy());
  }

  @Test
  public void testCopyAndModify() {
    Player attacker = ConsoleCombat.getSampleAttacker();
    Player defender = ConsoleCombat.getSampleDefender();
    Battlefield battlefield = new Battlefield(attacker, defender);
    Battlefield spy = spy(battlefield);

    Battlefield copy = spy.copy();
    copy.playDefenderUnit(copy.anyAction(true));
    copy.playAttackerUnit(copy.anyAction(false));
    copy.playDefenderUnit(copy.anyAction(true));
    copy.playAttackerUnit(copy.anyAction(false));
    verify(spy).copy();
    verifyNoMoreInteractions(spy);
  }

  @Test
  public void testAnyAction() {
    Player attacker = ConsoleCombat.getSampleAttacker();
    Player defender = ConsoleCombat.getSampleDefender();
    Battlefield battlefield = new Battlefield(attacker, defender);

    PlayerAction defenderAction = battlefield.anyAction(true);

    assertFalse(defenderAction.isAttacker());
    assertSame(defender.getUnitsList().get(0), defenderAction.unitPlayed());
    assertEquals(empty(), defenderAction.battlePosition());

    PlayerAction attackerAction = battlefield.anyAction(false);
    assertTrue(attackerAction.isAttacker());
    assertSame(attacker.getUnitsList().get(0), attackerAction.unitPlayed());
    assertEquals(empty(), attackerAction.battlePosition());
  }

  @Test
  public void testNewBattlefield() {
    Player attacker = ConsoleCombat.getSampleAttacker();
    Player defender = ConsoleCombat.getSampleDefender();
    Battlefield battlefield = new Battlefield(attacker, defender);

    assertEquals(attacker.getUnitsList(), battlefield.getAttackerUnits());
    assertEquals(defender.getUnitsList(), battlefield.getDefenderUnits());
    assertEquals(3, battlefield.getAttackerOptions().size());
    assertEquals(3, battlefield.getDefenderOptions().size());
    assertEquals(0, battlefield.getBattlePositions().size());
  }

  @Test
  public void testPlayAttackerUnit() {
    Player attacker = ConsoleCombat.getSampleAttacker();
    Player defender = ConsoleCombat.getSampleDefender();
    Battlefield battlefield = new Battlefield(attacker, defender);

    Unit unit = battlefield.playAttackerUnit(battlefield.anyAction(false));

    assertEquals(attacker.getUnitsList().get(0), unit);
    // Changed
    assertEquals(2, battlefield.getAttackerUnits().size());
    assertEquals(2, battlefield.getAttackerOptions().size());
    assertEquals(1, battlefield.getBattlePositions().size());
    assertEquals(Optional.of(unit), battlefield.getBattlePositions().get(0).getAttackerUnit());
    assertTrue(battlefield.getBattlePositions().get(0).getDefenderUnit().isEmpty());

    // 3 units, in each of 2 positions
    assertEquals(6, battlefield.getDefenderOptions().size());

    // Unchanged
    assertEquals(3, battlefield.getDefenderUnits().size());
  }

  @Test
  public void testPlayDefenderUnit() {
    Player attacker = ConsoleCombat.getSampleAttacker();
    Player defender = ConsoleCombat.getSampleDefender();
    Battlefield battlefield = new Battlefield(attacker, defender);

    Unit unit = battlefield.playDefenderUnit(battlefield.anyAction(true));

    assertEquals(defender.getUnitsList().get(0), unit);
    // Changed
    assertEquals(2, battlefield.getDefenderUnits().size());
    assertEquals(2, battlefield.getDefenderOptions().size());
    assertEquals(1, battlefield.getBattlePositions().size());
    assertEquals(Optional.of(unit), battlefield.getBattlePositions().get(0).getDefenderUnit());
    assertTrue(battlefield.getBattlePositions().get(0).getAttackerUnit().isEmpty());
    // 3 units, in each of 2 positions
    assertEquals(6, battlefield.getAttackerOptions().size());

    // Unchanged
    assertEquals(3, battlefield.getAttackerUnits().size());
  }

  @Test
  public void testFight() {
    Player attacker = ConsoleCombat.getSampleAttacker();
    Player defender = ConsoleCombat.getSampleDefender();
    Battlefield battlefield = new Battlefield(attacker, defender);

    // 2/2 infantry
    Unit attackerUnit = battlefield.playAttackerUnit(battlefield.getAttackerOptions().get(0));
    // 1/3 infantry
    Unit defenderUnit = battlefield.playDefenderUnit(battlefield.getDefenderOptions().get(0));

    BattlePosition battlePosition = battlefield.getBattlePositions().get(0);
    assertSame(attackerUnit, battlePosition.getAttackerUnit().orElseThrow());
    assertSame(defenderUnit, battlePosition.getDefenderUnit().orElseThrow());

    assertEquals(1, attackerUnit.getWounds());
    assertEquals(2, defenderUnit.getWounds());
    assertFalse(attackerUnit.isDead());
    assertFalse(defenderUnit.isDead());

    // 2/2 mounted
    attackerUnit = battlefield.playAttackerUnit(battlefield.getAttackerOptions().get(0));
    // 2/2 mounted
    defenderUnit = battlefield.playDefenderUnit(battlefield.getDefenderOptions().get(0));

    assertTrue(attackerUnit.isDead());
    assertTrue(defenderUnit.isDead());
    // Both dead, so position is cleared.
    assertEquals(1, battlefield.getBattlePositions().size());
  }

  @Test
  public void testResult() {
    Player attacker = ConsoleCombat.getSampleAttacker();
    Player defender = ConsoleCombat.getSampleDefender();
    Battlefield battlefield = new Battlefield(attacker, defender);

    // 2/2 infantry
    Unit attackerUnit = battlefield.playAttackerUnit(battlefield.getAttackerOptions().get(0));

    // 1/3 infantry
    Battlefield result = battlefield.result(battlefield.getDefenderOptions().get(0));

    // Assert the result is a copy
    BattlePosition battlePosition = result.getBattlePositions().get(0);
    assertNotSame(battlePosition, battlefield.getBattlePositions().get(0));
    assertNotSame(attackerUnit, battlePosition.getAttackerUnit().orElseThrow());

    assertNotEquals(battlePosition, battlefield.getBattlePositions().get(0));
    // Check the fight happened
    assertEquals(1, battlePosition.getAttackerUnit().orElseThrow().getWounds());
    assertEquals(2, battlePosition.getDefenderUnit().orElseThrow().getWounds());
  }
}
