/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CivCombat;

/**
 * @author Doug
 */
public class Decision {
  public final boolean winner;
  public final PlayerAction action;

  public Decision(boolean result, PlayerAction action) {
    this.winner = result;
    this.action = action;
  }

}
