/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CivCombat.Player;

/**
 * Represents playing a unit from a given hand position to a given board position.
 */
public class PlayerAction {

  private final int handPosition;
  private final int battlePosition;

  public PlayerAction(int handPosition, int battlePosition) {
    this.handPosition = handPosition;
    this.battlePosition = battlePosition;
  }

  public int getHandPosition() {
    return handPosition;
  }

  public int getBattlePosition() {
    return battlePosition;
  }

  public void printPlayerAction() {
    System.out.println("Play unit " + handPosition + " at position " + battlePosition);
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 17 * hash + this.handPosition;
    hash = 17 * hash + this.battlePosition;
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final PlayerAction other = (PlayerAction) obj;
    if (this.handPosition != other.handPosition) {
      return false;
    }
    return this.battlePosition == other.battlePosition;
  }


}
