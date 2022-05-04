/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CivCombat.Possible;

import CivCombat.Player.Player;
import CivCombat.Unit.Unit;

import java.util.*;

/**
 * Represents a set of possible configurations of opponent units.
 */
public class PossiblePlayers {

  //List of all possible Players given known information
  private final List<Player> possiblePlayers;

  //To be changed, want to implement battleHandSize
  public PossiblePlayers(List<Unit> playedUnits, List<PossibleUnit> standingForces, int battleHandSize) {
    //List of players, each corresponding to a different combination of units
    possiblePlayers = new ArrayList<>();

    if (playedUnits.size() > battleHandSize) {
      throw new IllegalStateException("More units played than hand size");
    }
    /*Number of units given to each player*/
    int unitsPerPlayer = battleHandSize - playedUnits.size();

    /* Make copies of each PossibleUnit from standingForces */
    List<PossibleUnit> remainingStandingForces = new ArrayList<>();
    for (PossibleUnit p : standingForces) {
      remainingStandingForces.add(p.copy());
    }
    /* Remove PossibleUnits from remainingStandingForces matching the unit type of each played unit */
    int index;
    for (Unit u : playedUnits) {
      index = 0;
      while (!remainingStandingForces.get(index).getUnitType().equals(u.getUnitType())) {
        index++;
        if (index > remainingStandingForces.size() - 1) {
          throw new IllegalArgumentException("Unit matching played unit type not found");
        }
      }
      remainingStandingForces.remove(index);
    }
    if (remainingStandingForces.size() < unitsPerPlayer) {
      throw new IllegalStateException("Insufficient number of remaining units");
    }

    /* Select unitsPerPlayer from remainingStandingForces to create a PossiblePlayer */
    int unitsAvailable = remainingStandingForces.size();
    if (unitsAvailable != standingForces.size() - playedUnits.size()) {
      throw new IllegalStateException("Incorrect number of units available");
    }
    Set<int[]> selections = generateSelections(unitsAvailable, unitsPerPlayer);

        /*Iterate through all possible combinations of the selected units. 
        Pointers shows the current selection of units; the pointer[i]'th unit
        is selected from remainingStandingForces(selection[i]) for each
        selection in selections */
    int[] pointers = new int[unitsPerPlayer];
    Arrays.fill(pointers, 0);

    //Set to hold Units for our next player
    Set<Unit> units = new LinkedHashSet<>();;
    for (int[] sel : selections) {

      //Finished when we add a player with the last choice from each PossibleUnit
      boolean done = false;
      while (!done) {
        //Change the first unit
        pointers[0] += 1;
        //If we move past the end of our options for a unit, reset to the first option and increment the next one instead
        for (int i = 0; i < pointers.length; i++) {
          if (pointers[i] > remainingStandingForces.get(sel[i]).numberPossible() - 1) {
            pointers[i] = 0;
            if (i != pointers.length - 1) {
              pointers[i + 1] = pointers[i + 1] + 1;
            } else {
              done = true;
            }
          }
        }

        //Create copies of required units
        for (int i = 0; i < unitsPerPlayer; i++) {
          units.add(remainingStandingForces.get(sel[i]).getUnit(pointers[i]).copyUnit());
        }
        possiblePlayers.add(new Player(units));

      }
    }
  }

  private Set<int[]> generateSelections(int unitsAvailable, int unitsPerPlayer) {
    Set<int[]> selections = new HashSet<>();


    if (unitsPerPlayer > 0) {
      int[] selection = new int[unitsPerPlayer];
      for (int n = 0; n < unitsPerPlayer; n++) {
        selection[n] = n;
      }
      selections.add(selection.clone());

      boolean done = false;
      while (!done) {
        selection[unitsPerPlayer - 1]++;
        for (int i = unitsPerPlayer - 1; i > 0; i--) {
          if (selection[i] > unitsAvailable - unitsPerPlayer + i) {
            selection[i - 1]++;
            for (int j = i; j < unitsPerPlayer; j++) {
              selection[j] = selection[j - 1] + 1;
            }
          }
        }
        if (selection[0] == unitsAvailable - unitsPerPlayer + 1) {
          done = true;
        } else {
          selections.add(selection.clone());
        }
      }
    }
    return selections;
  }

  public List<Player> getPlayers() {
    return possiblePlayers;
  }

  public int getNumberOfPlayers() {
    return possiblePlayers.size();
  }

}
