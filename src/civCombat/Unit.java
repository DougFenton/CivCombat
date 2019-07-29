/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package civCombat;

/**
 *
 * @author Doug
 */
public abstract class Unit {
    private int baseAttack;
    private int baseHealth;
    private int wounds;    
    
    public Unit(int baseAttack, int baseHealth) {
        this.baseAttack = baseAttack;
        this.baseHealth = baseHealth;
        this.wounds = 0;
    }
    
    public Unit (Unit original) {
        this.baseAttack = original.baseAttack;
        this.baseHealth = original.baseHealth;
        this.wounds = original.wounds;
    }
    
    public abstract Unit copyUnit();
    
    public abstract boolean trumps(Unit unit);
    
    public abstract String getUnitType();
    

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
    
    public void printUnit() {
        System.out.print("Attack: " + this.baseAttack + " ");
        System.out.print("Health: " + this.baseHealth + " ");
    }
   
    
}
