/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Minimax;

import java.util.Set;

/**
 *
 * @author Doug
 */
public interface State {

    Set<? extends Action> getApplicableActions();

    State getActionResult(Action action);
}
