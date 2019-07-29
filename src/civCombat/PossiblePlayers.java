/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package civCombat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Doug
 */
public class PossiblePlayers {

    //List of all possible Players given known information
    private List<Player> possiblePlayers;

    //To be changed, want to implement battleHandSize
    public PossiblePlayers(List<Unit> playedUnits, List<PossibleUnit> standingForces, int battleHandSize) {
        //List of players, each corresponding to a different combination of units
        possiblePlayers = new ArrayList<>();

        if (playedUnits.size() > battleHandSize) {
            throw new IllegalStateException("More units played than hand size");
        }

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
                if (index > remainingStandingForces.size() - 1) throw new IllegalArgumentException("Unit matching played unit type not found");
            }
            remainingStandingForces.remove(index);            
        }

        /*Number of units given to each player*/
        int totalUnits = battleHandSize - playedUnits.size(); //=remainingStandingForces.size()

        /* Select totalUnits from remainingStandingForces to create a PossiblePlayer */
        int[] selection = new int[battleHandSize];
        for (int n = 0; n < battleHandSize; n++) {
            selection[n] = n;
        }        
        Set<int[]> selections = new HashSet<>();        
        selections.add(selection.clone());
        
        boolean done = false;
        while (!done) {
            selection[battleHandSize - 1]++;
            for (int i = battleHandSize - 1; i > 0; i--) {
                if (selection[i] > totalUnits - battleHandSize + i) {
                    selection[i - 1]++;
                    for (int j = i; j < battleHandSize; j++) {
                        selection[j] = selection[j - 1] + 1;
                    }
                }
            }
            if (selection[0] == totalUnits - battleHandSize + 1) done = true;
            else selections.add(selection.clone());
        }

        /*Iterate through all possible combinations of the selected units. 
        Pointers shows the current selection of units; the pointer[i]'th unit
        is selected from remainingStandingForces(selection[i]) for each
        selection in selections */
        int[] pointers = new int[totalUnits];
        Arrays.fill(pointers, 0);

        //Finished when we add a player with the last choice from each PossibleUnit
        done = false;
        while (!done) {
            //Change the first unit
            pointers[0] += 1;
            //If we move past the end of our options for a unit, reset to the first option and increment the next one instead
            for (int i = 0; i < pointers.length; i++) {
                if (pointers[i] > remainingStandingForces.get(i).numberPossible() - 1) {
                    pointers[i] = 0;
                    if (i != pointers.length - 1) {
                        pointers[i + 1] = pointers[i + 1] + 1;
                    } else {
                        done = true;
                    }
                }
            }
            //Create new units for our next players
            Unit[] units;
            for (int[] sel : selections) {
                units = new Unit[totalUnits];
                for (int i = 0; i < totalUnits; i++) {
                    units[i] = remainingStandingForces.get(sel[i]).getUnit(pointers[i]).copyUnit();
                }
                possiblePlayers.add(new Player(units));
            }
        }
    }

    public List<Player> getPlayers() {
        return possiblePlayers;
    }

    public int getNumberOfPlayers() {
        return possiblePlayers.size();
    }

}
