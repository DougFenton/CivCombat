/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CivCombat.Player;


import CivCombat.Unit.Unit;
import CivCombat.Unit.UnitConsolePrinter;

import java.util.List;
import java.util.Objects;

/**
 * Represents either the attacker or defender in a single combat.
 */
public class Player {
  protected List<Unit> units;

  public Player(List<Unit> units) {
    this.units = units;
  }

  public List<Unit> getUnitsList() {
    return units.stream().toList();
  }

  public void printPlayer() {
    System.out.println("Player has " + units.size() + " units");
    for (Unit u : units) {
      UnitConsolePrinter.print(u, 1);
    }
    System.out.println();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Player player)) return false;
    return Objects.equals(units, player.units);
  }

  @Override
  public int hashCode() {
    return Objects.hash(units);
  }
}
