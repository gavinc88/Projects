Project Title: Pig Dice Game

Author: Gavin Chu & Eric O'neil

Description: 
	Pig is a simple dice rolling game. The game involves 2 players.
The players take turn rolling a dice. The player's score from each round is 
the sum of all the dice rolled so far in that round. The player can roll the 
dice as many times as he or she wants as long as he or she doesn't roll a 1, 
which in that case, the total score for that round becomes 1. The game also 
considers specific rules such as if the sum of both players' score is divisible 
by 7, the other player must use a 4 sided dice as opposed to a regular 6 sided 
dice for his or her next turn.
 
	I implemented the framework that satisfies all the rules in Pig. I also 
implemented different strategies for a computer player to follow. My final task 
was to come up with a strategy for playing against a naive computer who keeps 
rolling to a certain value each round. My final strategy was capable of beating 
naive computer over 50% of the time.

Instruction:
- run pig.py with Python 3 to see win percentages using different strategies
