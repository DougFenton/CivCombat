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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A random player representing a barbarian village. Has a level 1 unit of each non-aircraft type.
 */
public class VillagePlayer extends Player {

  public VillagePlayer() {
    super(generateUnits());
  }

  private static List<Unit> generateUnits() {
    List<Unit> units = new ArrayList<>();
    Random randomNumber = new Random();
    int n = randomNumber.nextInt(3) + 1;
    units.add(new InfantryUnit(1, n, 4 - n));

    n = randomNumber.nextInt(3) + 1;
    units.add(new MountedUnit(1, n, 4 - n));

    n = randomNumber.nextInt(3) + 1;
    units.add(new ArtilleryUnit(1, n, 4 - n));
    return units;
  }
}
