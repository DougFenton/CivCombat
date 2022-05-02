/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CivCombat.Unit;

/**
 * Units have an attack value, a health value, a level, a type, and a number of wounds.
 * They can exist in a player's hand, or at a position on the battlefield.
 */
public abstract class Unit {
  private final int level;
  private final UnitType type;
  private final int baseAttack;
  private final int baseHealth;
  private int wounds;

  public Unit(UnitType type, int level, int baseAttack, int baseHealth) {
    this.level = level;
    this.type = type;
    this.baseAttack = baseAttack;
    this.baseHealth = baseHealth;
    this.wounds = 0;
  }

  public Unit(Unit original) {
    this.type = original.type;
    this.level = original.level;
    this.baseAttack = original.baseAttack;
    this.baseHealth = original.baseHealth;
    this.wounds = original.wounds;
  }

  public abstract Unit copyUnit();

  public abstract boolean trumps(Unit unit);

  public abstract UnitType getUnitType();

  public int getLevel() {
    return level;
  }

  public int getAttack() {
    return baseAttack;
  }

  public int getHealth() {
    return baseHealth;
  }

  public void applyWounds(int wounds) {
    this.wounds += wounds;
  }

  public void removeWounds(int wounds) {
    this.wounds -= wounds;
  }

  public void removeWounds() {
    removeWounds(wounds);
  }

  public int getWounds() {
    return wounds;
  }

  public boolean isDead() {
    return wounds >= baseHealth;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 41 * hash + this.baseAttack;
    hash = 41 * hash + this.baseHealth;
    hash = 41 * hash + this.wounds;
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Unit other = (Unit) obj;
    if (this.baseAttack != other.baseAttack) {
      return false;
    }
    if (this.baseHealth != other.baseHealth) {
      return false;
    }
    return this.wounds == other.wounds;
  }


}
