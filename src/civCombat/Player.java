/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package civCombat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Doug
 */
public class Player {
  //Array of units the player has in hand
  protected Unit[] units;

  public Player(Unit... units) {
    this.units = units;
  }

  //Return an ArrayList of player's units
  public List<Unit> getUnitsList() {
    return new ArrayList<>(Arrays.asList(units));
  }

  //Get n'th unit from hand, 0 for first unit
  public Unit getUnit(int n) {
    return units[n];
  }

  public int getNumberOfUnits() {
    return units.length;
  }

  public void printPlayer() {
    //System.out.println("Printing Player");
    System.out.println("Player has " + this.getNumberOfUnits() + " units");
    for (Unit u : units) {
      u.printUnit();
      System.out.println();
    }
    System.out.println();
  }

}
