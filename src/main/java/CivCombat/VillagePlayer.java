/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CivCombat;

import CivCombat.Unit.ArtilleryUnit;
import CivCombat.Unit.InfantryUnit;
import CivCombat.Unit.MountedUnit;
import CivCombat.Unit.Unit;

import java.util.Random;

/**
 * @author Doug
 */
public class VillagePlayer extends Player {

  public VillagePlayer() {
    units = new Unit[3];
    Random randomNumber = new Random();
    int n = randomNumber.nextInt(3) + 1;
    units[0] = new InfantryUnit(n, 4 - n);

    n = randomNumber.nextInt(3) + 1;
    units[1] = new MountedUnit(n, 4 - n);

    n = randomNumber.nextInt(3) + 1;
    units[2] = new ArtilleryUnit(n, 4 - n);

  }
}
