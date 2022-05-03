/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CivCombat.Player;


import CivCombat.Unit.Unit;
import CivCombat.Unit.UnitConsolePrinter;

import java.util.Arrays;
import java.util.List;

/**
 * Represents either the attacker or defender in a single combat.
 */
public class Player {
  //Array of units the player has in hand
  protected Unit[] units;

  public Player(Unit... units) {
    this.units = units;
  }

  public List<Unit> getUnitsList() {
    return Arrays.asList(units);
  }

  public void printPlayer() {
    System.out.println("Player has " + units.length + " units");
    for (Unit u : units) {
      UnitConsolePrinter.print(u, 1);
    }
    System.out.println();
  }

}
