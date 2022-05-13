/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CivCombat.Possible;

import CivCombat.Player.Player;
import CivCombat.Unit.Unit;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a set of possible configurations of opponent units.
 */
public class PossiblePlayers {

  // List of all possible Players given known information
  private final List<Player> possiblePlayers;

  public PossiblePlayers(List<List<PossibleUnit>> possibleUnits) {
    List<List<Unit>> finalUnits = new ArrayList<>();

    for (List<PossibleUnit> possibleUnitSet : possibleUnits) {
      List<List<Unit>> lists = possibleUnitSet.stream().map(PossibleUnit::getPossibleUnits).toList();
      List<List<Unit>> units = new ArrayList<>();
      for (List<Unit> unitList : lists) {
        units = combine(units, unitList);
      }
      finalUnits.addAll(units);
    }

    possiblePlayers = finalUnits.stream().map(Player::new).toList();
  }

  /**
   * Returns a new list consisting of each of the source lists, with each item from options appended.
   */
  private <T> List<List<T>> combine(List<List<T>> existing, List<T> options) {
    if (options.isEmpty()) {
      throw new IllegalArgumentException("No options to combine");
    }

    if (existing.isEmpty()) {
      return options.stream()
          .map(t -> (List<T>) new ArrayList<>(List.of(t)))
          .toList();
    }

    List<List<T>> resultLists = new ArrayList<>();
    for (List<T> sourceList : existing) {
      options.stream()
          .map(option -> {
            List<T> appended = new ArrayList<>(sourceList);
            appended.add(option);
            return appended;
          })
          .forEach(resultLists::add);
    }
    return resultLists;
  }

  public List<Player> getPlayers() {
    return possiblePlayers;
  }

  public int getNumberOfPlayers() {
    return possiblePlayers.size();
  }

}
