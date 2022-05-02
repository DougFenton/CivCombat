/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CivCombat;

import CivCombat.Player.PlayerAction;

/**
 * Data type storing an action and the game result.
 */
public record Decision(boolean result, PlayerAction action) {}