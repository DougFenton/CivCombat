/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CivCombat.Player;

import CivCombat.Unit.ArtilleryUnit;
import CivCombat.Unit.InfantryUnit;
import CivCombat.Unit.MountedUnit;
import CivCombat.Unit.Unit;

import java.util.Random;

/**
 * A random player representing a barbarian village. Has a level 1 unit of each non-aircraft type.
 */
public class VillagePlayer extends Player {

  public VillagePlayer() {
    units = new Unit[3];
    Random randomNumber = new Random();
    int n = randomNumber.nextInt(3) + 1;
    units[0] = new InfantryUnit(1, n, 4 - n);

    n = randomNumber.nextInt(3) + 1;
    units[1] = new MountedUnit(1, n, 4 - n);

    n = randomNumber.nextInt(3) + 1;
    units[2] = new ArtilleryUnit(1, n, 4 - n);

  }
}
