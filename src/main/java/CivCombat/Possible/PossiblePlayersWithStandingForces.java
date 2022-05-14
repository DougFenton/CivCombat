package CivCombat.Possible;

import java.util.ArrayList;
import java.util.List;

/**
 * Selects the required number of units from the standing forces to generate the possible players.
 */
public class PossiblePlayersWithStandingForces extends PossiblePlayers {
  public PossiblePlayersWithStandingForces(List<PossibleUnit> standingForces, int battleHandSize) {
    super(calculatePossibleUnits(standingForces, battleHandSize));
  }

  private static List<List<PossibleUnit>> calculatePossibleUnits(List<PossibleUnit> standingForces, int battleHandSize) {
    if (standingForces.size() < battleHandSize) {
      throw new IllegalStateException("Insufficient number of remaining units");
    }

    // Sets of $battleHandSize possible units from those remaining.
    List<List<PossibleUnit>> sublistsOfSize = sublistsOfSize(standingForces, battleHandSize);

    // Order units and remove duplicate players (same combination of possible units).
    return sublistsOfSize.stream()
        .map(possibleUnits -> possibleUnits.stream().sorted().toList())
        .distinct()
        .toList();
  }

  /**
   * Returns all sub-lists of a given size of the given list.
   */
  private static <T> List<List<T>> sublistsOfSize(List<T> items, int elementsPerSubset) {
    int size = items.size();

    List<boolean[]> allIncludes = new ArrayList<>();
    // Whether the subset should include each element of the input, by index
    boolean[] include = new boolean[size];
    for (int i = 0; i < size; i++) {
      include[i] = false;
    }

    // Add starting value
    allIncludes.add(include.clone());
    // Add all other values
    int configurations = (int) Math.pow(2, size);
    for (int i = 0; i < configurations - 1; i++) {
      increment(include);
      allIncludes.add(include.clone());
    }

    // List of lists which elements from the original list to include, by index
    List<List<Boolean>> subsets = allIncludes.stream()
        .filter(includes -> numberTrue(includes) == elementsPerSubset)
        .map(PossiblePlayersWithStandingForces::toList)
        .toList();

    return subsets.stream()
        .map(booleans -> itemsFromList(items, booleans))
        .toList();
  }

  /**
   * Increments the boolean array to the next configuration.
   */
  private static void increment(boolean[] booleans) {
    int current = booleans.length - 1;
    while (booleans[current]) {
      booleans[current] = false;
      current -= 1;
    }

    //noinspection ConstantConditions
    if (current >= 0) {
      booleans[current] = true;
    }
  }

  /**
   * Returns the count of {@link Boolean#TRUE} elements of the array.
   */
  private static int numberTrue(boolean[] includes) {
    int total = 0;
    for (boolean include : includes) {
      if (include) {
        total += 1;
      }
    }
    return total;
  }

  private static List<Boolean> toList(boolean[] booleans) {
    ArrayList<Boolean> list = new ArrayList<>();
    for (boolean b : booleans) {
      list.add(b);
    }
    return list;
  }

  /**
   * Returns a sub-list of the given list, consisting of all items at indexes marked for inclusion.
   * e.g. { items[i] | 0 < i < items.size() && indexesToInclude[i] == true }.
   */
  private static <T> List<T> itemsFromList(List<T> items, List<Boolean> indexesToInclude) {
    if (items.size() != indexesToInclude.size()) {
      throw new IllegalArgumentException();
    }

    List<T> toInclude = new ArrayList<>();
    for (int i = 0; i < items.size(); i++) {
      if (indexesToInclude.get(i)) {
        toInclude.add(items.get(i));
      }
    }
    return toInclude;
  }
}
