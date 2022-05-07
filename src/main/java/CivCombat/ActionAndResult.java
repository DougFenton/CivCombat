/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CivCombat;

import CivCombat.Player.PlayerAction;

/**
 * Data type storing an action and the game result.
 *
 * @param action The action, or null if there are no possible actions
 * @param result true if the defender wins with optimal play, false otherwise.
 */
public record ActionAndResult(PlayerAction action, boolean result) {
}