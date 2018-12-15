# OPLCasino
Repo for the Casino game in all languages

## Description

This repo is dedicated to the semester long project of fully designing, and implementing a modified version of the royal casino card game in four programming languages: C++, Common Lisp, Java/Android, and Prolog. All versions of the game do follow the same rules, with Java/Android being the most developed and  feature complete while the Common Lisp version is the least feature complete.

## Rules of the Game

A player is allowed to take three actions: Capture, Build, and Trail, and can only play one card per turn. After the player makes their move, it alternates to the other player. 

The players are initially dealt a hand of four cards, with four cards being played on the table. Once both player hands are empty, they are dealt another four cards. This process repeats until the deck is empty and both players are out of cards. Once that happens the round is scored. The player with the highest score over 21 wins! 

### Scoring
Once both players are out of cards and there are no more cards in the deck, round scoring beings. The first step is to give all remaining table cards to the player who captured last, then awarding round points based on the following criteria: 

* 3 Points are awarded for having the most cards in the pile
	* No points in a tie

* 1 Point is awarded for having the most Spades in the pile
	* No points in a tie

* 2 points to the player with the ten of diamonds
* 1 point to the player with the two of spades
* 1 point per ace in the pile

The most points a player can get in a single round is  11, and the minimum points to be given are 7 between both players.

The tournament ends when a player has more than 21 points, otherwise a new round starts with the player who captured last going first. 

### Card Values

* Kings are 13
* Queens are 12
* Jacks are 11
* Tens are 10 (Represented as an X in strings)

* Ace can be both 1 and 14, including in the same action

### Capture
When a user plays a card as a capture, they are required to capture any matching symbols on the table. In addition, a user may capture any set of cards which up to that cards. 

### Build
A user may play a card onto one or more card onto the table to create a build of cards. This will prevent the player from trailing any cards and they must contain the card which the build sums to in their hand. This prevents any cards inside the build from being captured, as only the build as a whole can be taken. 

If the other play can either capture the opponent's build if they have the correct card, or they can play a card to take the build and extend it if they have the appropriate cards. As an example, if there is a build of seven on the table, and they have a two and a nine in their hand, they can extend the build by playing a two on it, therefore taking it from the other player. As a side note, a player is unable to extend their own build

If a player has two builds of the same sum on the table, they will be combined into a multiple build (multi-build). These work exactly the same way as a normal build except it cannot be extended by the other player.

Lastly, a build cannot be part of a set capture unless it matches the played card exactly. 

### Trail
If the user does not have any builds on the table, and there are no cards on the table which match a symbol of a card in the player's hand, the player is allowed to trail. This action is simply placing a card on the table. 
