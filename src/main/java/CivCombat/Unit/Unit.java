/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CivCombat.Unit;

import java.util.Objects;

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
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Unit unit = (Unit) o;
    return level == unit.level && baseAttack == unit.baseAttack && baseHealth == unit.baseHealth && wounds == unit.wounds && type == unit.type;
  }

  @Override
  public int hashCode() {
    return Objects.hash(level, type, baseAttack, baseHealth, wounds);
  }
}
