# Introduction #

All possible scenarios that can occur in a poker game and should be tested. Please add any if you feel a scenario is missing.

## Terminology ##
  * Active player: a player who can perform an action at the table.

# Texas Hold'Em Poker Scenarios #

## Multiple Players ##
  * **One active player left:** The active player collects all the chips. _tested_
  * **One all-in player left:** The all-in player collects all the chips. _tested_
  * **Only all-in players and at most one active player**: The rounds have to be changed automatically, without betting possibility until showdown.
  * **Small Blind All-in:** The next player must bet the big blind, which everyone else calls/raises. [link](http://www.learn-texas-holdem.com/questions/small-blind-not-enough-chips.htm)
  * **Big Blind All-in:** The other players must call the all-in amount, not the big blind amount. [link](http://www.learn-texas-holdem.com/questions/big-blind-all-in.htm)
  * **Both Blinds All-in**

## Two Players ##
  * **Blinds are inversed and dealer starts to act**
### All-in ###
If there is at least one all-in player, the rounds have to be switched automatically.
  * **Small Blind All-in:** The other player must call the all-in amount and not pay the big blind.
  * **Big Blind All-in:** The small blind player must call the all-in amount.
  * **Both Blinds All-in**