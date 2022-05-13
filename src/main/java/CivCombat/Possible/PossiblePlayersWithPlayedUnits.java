package CivCombat.Possible;

import CivCombat.Unit.Unit;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Selects the required units from the standing forces, given knowledge about played units.
 */
public class PossiblePlayersWithPlayedUnits extends PossiblePlayersWithStandingForces {
  public PossiblePlayersWithPlayedUnits(List<Unit> playedUnits, List<PossibleUnit> standingForces, int battleHandSize) {
    super(calculatePossibleUnits(playedUnits, standingForces, battleHandSize), battleHandSize - playedUnits.size());
  }

  private static List<PossibleUnit> calculatePossibleUnits(List<Unit> playedUnits, List<PossibleUnit> standingForces, int battleHandSize) {
    if (playedUnits.size() > battleHandSize) {
      throw new IllegalStateException("More units played than hand size");
    }

    // Remove the played units from the standing forces.
    List<PossibleUnit> remainingStandingForces = standingForces.stream().map(PossibleUnit::copy).collect(Collectors.toList());
    playedUnits.forEach(unit -> {
          PossibleUnit matching = remainingStandingForces.stream()
              .filter(possibleUnit -> possibleUnit.matches(unit))
              .findFirst().orElseThrow(() -> new IllegalStateException("Matching unit not found in standing forces"));
          remainingStandingForces.remove(matching);
        }
    );
    return remainingStandingForces;
  }
}
