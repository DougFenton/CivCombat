/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Minimax;

/**
 *
 * @author Doug
 */

/**
function Minimax-Decision(s) returns an action
    return a in Actions(s) that maximises Min-Value(Result(s, a))

function Max-Value(s) returns a utility value
    if Terminal-Test(s) then return Utility(s)
    v← −∞
    for each a in Actions(s) do
        v← max(v,Min-Value(Result(s, a)))
    return v
    * 
function Min-Value(s) returns a utility value
    if Terminal-Test(s) then return Utility(s)
    v← +∞
    for each a in Actions(s) do
        v← min(v,Max-Value(Result(s, a)))
    return v
*/

public class MinimaxDecision {
    
}
