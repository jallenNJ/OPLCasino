
%=======================
%Functions to run a round
%=======================

/* *********************************************************************
Function Name: startNewRound
Purpose: Wrapper function to start the round cycle
Parameters:
        RoundNum(StartNewRound\4 & 6) The current roun num
        Starting(StartNewRound\4 & 6) The Id of the starting player
        HumanScore(StartNewRound\6) The Score of the human player
        CompScore(StartNewRound\6) The Score of the comp player
Return Value: 
            Last Cap, The Id of the player who captured last
            EndScores, The list of scores, [Human | Comp]
Algorithm:
            1)Wrapper
Assistance Received: none
********************************************************************* */

startNewRound(LastCap, EndScores) :- 
    writeln("New round started!"),
    playRound(0, 0, [],[],[],[], 0, LastCap,EndScores).
startNewRound(RoundNum, Starting, LastCap, EndScores) :-
    write("Round "),
    writeln(RoundNum),
    writeln(" is starting!"),
    playRound(RoundNum, Starting, [],[],[],[], 0, LastCap, EndScores).
startNewRound(RoundNum, Starting, HumanScore, CompScore, LastCap, EndScores) :- 
    write("Round "),
    writeln(RoundNum),
    writeln(" is starting!"),
    playRound(RoundNum, Starting, HumanScore, CompScore, Starting, LastCap, EndScores).


/* *********************************************************************
Function Name: PlayRound
Purpose: Main Loop to play the round to completion
Parameters:
            Round The current round num
            FIrstId  The id of the starting player
            HumanScore The Score of the human
                Or P0Info, the info of the 0th Player
            CompScore The Score of the computer
                Or P1Info the info of the 1st Player
            Last Cap The id of who captured last

Return Value: 
            LastCapEnd -- The last cap at the end of the clause
            EndScores -- The score list at the end of the round
Algorithm:
            1) Make sure Game State is valid
            2) If valid, Have p0 do turn
            3) Have p1 Do turn
Assistance Received: none
********************************************************************* */

%New round (Human Start)
playRound(Round, FirstId, HumanScore, CompScore, LastCap, LastCapEnd, EndScores) :-
		FirstId = 0,
        %Create the deck
		createDeck(RawDeck),
        %Deal the starting cards
		drawFourCards(RawDeck, AfterOneDraw, HumanCards),
		drawFourCards(AfterOneDraw, AfterTwoDraws, CompCards),
		drawFourCards(AfterTwoDraws, Deck, TableCards),
        %Create new players
		createNewPlayer(0, HumanCards, HumanScore, HumanPlayer),
		createNewPlayer(1, CompCards, CompScore, ComputerPlayer),
        %Call next round
		playRound(Round, FirstId, Deck, TableCards, HumanPlayer, ComputerPlayer, LastCap, LastCapEnd, EndScores).
%New Round (Computer Start)
playRound(Round, FirstId, HumanScore, CompScore, LastCap, LastCapEnd, EndScores) :-
        %Create the deck
    	createDeck(RawDeck),
        %draw four cards to each pile
		drawFourCards(RawDeck, AfterOneDraw, HumanCards),
		drawFourCards(AfterOneDraw, AfterTwoDraws, CompCards),
		drawFourCards(AfterTwoDraws, Deck, TableCards),
        %Create new players and recursive function
		createNewPlayer(0, HumanCards, HumanScore, HumanPlayer),
		createNewPlayer(1, CompCards, CompScore, ComputerPlayer),
		playRound(Round, FirstId, Deck, TableCards, ComputerPlayer, HumanPlayer, LastCap, LastCapEnd, EndScores).

%New Round with no scores
playRound(Round, FirstId,[],[],[],[],LastCap, LastCapEnd, EndScores) :- 
        %Create deck
		createDeck(RawDeck),
        %Deal Cards
		drawFourCards(RawDeck, AfterOneDraw, HumanCards),
		drawFourCards(AfterOneDraw, AfterTwoDraws, CompCards),
		drawFourCards(AfterTwoDraws, Deck, TableCards),
        %Create the players and recursive call
		createNewPlayer(0, HumanCards, HumanPlayer),
		createNewPlayer(1, CompCards, ComputerPlayer),
		playRound(Round, FirstId, Deck, TableCards, HumanPlayer, ComputerPlayer, LastCap, LastCapEnd, EndScores).


%duplicate with above?
playRound(Round, FirstId,[],[],[],[],LastCap, LastCapEnd, EndScores) :- 
        %Create deck
		createDeck(RawDeck),
        %Deal cards
		drawFourCards(RawDeck, AfterOneDraw, HumanCards),
		drawFourCards(AfterOneDraw, AfterTwoDraws, CompCards),
		drawFourCards(AfterTwoDraws, Deck, TableCards),
        %Create the players and call round
		createNewPlayer(0, HumanCards, HumanPlayer),
		createNewPlayer(1, CompCards, ComputerPlayer),
		playRound(Round, FirstId, Deck, TableCards, HumanPlayer, ComputerPlayer, LastCap, LastCapEnd,EndScores).

%Deal new hands when hands are empty
playRound(Round, FirstId, Deck, Table, P0Info, P1Info, LastCap, LastCapEnd, EndScores) :-
    %Make sure hands are empty and there are eight cards in the deck
	getHand(P0Info, P0Hand),
	length(P0Hand, 0),
	getHand(P1Info, P1Hand),
	length(P1Hand, 0),
	length(Deck, CardsInDeck),
	CardsInDeck >= 8,
    %Deal the cards
	drawFourCards(Deck, AfterOneDraw, P0Cards),
	drawFourCards(AfterOneDraw, AfterTwoDraws, P1Cards),
    %Add tk tbe l,ayer
	getPlayerComponents(P0Info, P0Id, _, P0Pile, P0Reserved, P0Score),
	createPlayer(P0Id, P0Cards, P0Pile, P0Reserved, P0Score, NewP0),
	getPlayerComponents(P1Info, P1Id, _, P1Pile, P1Reserved, P1Score),
	createPlayer(P1Id, P1Cards, P1Pile, P1Reserved, P1Score, NewP1),
	playRound(Round, FirstId, AfterTwoDraws, Table, NewP0, NewP1, LastCap, LastCapEnd, EndScores).
	

%End round to score
playRound(Round, _, Deck, Table, P0Info, P1Info, LastCap,LastCapEnd, EndScores)	:-
    %Make sure hands are empty and deck doesn't have enough cards to deal
	getHand(P0Info, P0Hand),
	length(P0Hand, 0),
	getHand(P1Info, P1Hand),
	length(P1Hand, 0),
	length(Deck, CardsInDeck),
	CardsInDeck < 8,
	LastCapEnd = LastCap,
    %Score :D
	scoreRound(Round, Table, P0Info, P1Info, LastCap, EndScores).

%Main loop (Both players play)
playRound(Round, FirstId, Deck, Table, P0Info, P1Info, LastCap, LastCapEnd, EndScores) :-
    getHand(P0Info, P0Hand),
    %Ensure both players have cards to do the move
	length(P0Hand, P0HandLength),
    P0HandLength > 0,
	getHand(P1Info, P1Hand),
	length(P1Hand, P1HandLength),
    P1HandLength > 0,
    %Print the table and get the menu choice
	printFullTable(P0Info, Table, P1Info, Deck),
	getActionMenuChoice(P0Info, MenuChoice),
	handleMenuChoice(MenuChoice, Round, FirstId, Deck, Table, P0Info, P1Info, LastCap),
    %Make the move
	doPlayerMove(P0Info, P1Info, Table, LastCap, P0AfterMove, P1AfterP0, TableAfterP0, LastCapAfterP0),
    %Draw table and have the other player do their move
	printFullTable(P0AfterMove, TableAfterP0, P1AfterP0, Deck),
	getActionMenuChoice(P1Info, MenuChoice2),
    findOtherId(FirstId, OtherId),
	handleMenuChoice(MenuChoice2, Round, OtherId, Deck, TableAfterP0, P0AfterMove, P1AfterP0, LastCapAfterP0),
	doPlayerMove(P1AfterP0, P0AfterMove, TableAfterP0, LastCapAfterP0, P1AfterMove, P0AfterP1, TableAfterP1, LastCapAfterP1),
	playRound(Round, FirstId, Deck, TableAfterP1, P0AfterP1, P1AfterMove, LastCapAfterP1, LastCapEnd, EndScores).


%Only one player has enough cards to make a move
playRound(Round, FirstId, Deck, Table, P0Info, P1Info, LastCap, LastCapEnd, EndScores) :-
    %make sure only one player has enough cards
    getHand(P0Info, P0Hand),
	length(P0Hand, P0HandLength),
    P0HandLength > 0,
	getHand(P1Info, P1Hand),
	length(P1Hand, 0),

    %Print table, and do the move the user enters
	printFullTable(P0Info, Table, P1Info, Deck),
	getActionMenuChoice(P0Info, MenuChoice),
	handleMenuChoice(MenuChoice, Round, FirstId, Deck, Table, P0Info, P1Info, LastCap),
	doPlayerMove(P0Info, P1Info, Table, LastCap, P0AfterMove, P1AfterP0, TableAfterP0, LastCapAfterP0),
	printFullTable(P0AfterMove, TableAfterP0, P1AfterP0, Deck),
	playRound(Round, FirstId, Deck, TableAfterP0, P0AfterMove, P1AfterP0, LastCapAfterP0, LastCapEnd, EndScores).


% Only one player has cards, however its p1 and p0
playRound(Round, FirstId, Deck, Table, P0Info, P1Info, LastCap, LastCapEnd, EndScores) :-
    %Ensure the above is true
	getHand(P0Info, P0Hand),
	length(P0Hand, 0),
    getHand(P1Info, P1Hand),
	length(P1Hand, P1HandLength),
    P1HandLength > 0,

    %Swap
	playRound(Round, FirstId, Deck, Table, P1Info, P0Info, LastCap, LastCapEnd, EndScores).


%Rules to swap the ids
findOtherId(0,1).
findOtherId(1,0).


/* *********************************************************************
Function Name: scoreRound
Purpose: To score the current round
Parameters:
           Round The round num
           Table The table at the end of the round
           HumanStart The human that is passed in
           CompStart The Comp that is passed in
           %Last Cap Who captured last
Return Value: 
            EndScores The scores of both players [Human | Comp]
Algorithm:
            1) Check if selected cards are valid
            2) If it is, do the move, otherwise reprompt
Assistance Received: none
********************************************************************* */
scoreRound(Round, Table, HumanStart, CompStart, LastCap, EndScores) :-
    %Make sure human is human
	isHuman(HumanStart),
    %Give the cards to who captured last per the scoreing rules
	addCardsToLastCap(Table, HumanStart, CompStart, LastCap, Human, Comp),

    %Get both piles and print them
	getPile(Human, HumanPile),
	getPile(Comp, CompPile),
    write("Comp Pile: "),
    printCards(CompPile),
    nl,
    write("Human Pile: "),
    printCards(HumanPile),
    nl,
    %Get the starting score
	getScore(Human, HumanStartScore),
	getScore(Comp, CompStartScore),
    %Get the sizes and print them
	length(HumanPile, HumanPileSize),
	length(CompPile, CompPileSize),
	write("Human Pile Size: "),
	writeln(HumanPileSize),
	write("Computer Pile Size: "),
	writeln(CompPileSize),
    %Score who got the most points
	scoreBySize(3, HumanPileSize, CompPileSize, HumanSizeScore, CompSizeScore),

    %Count spades and output them
	countSpades(HumanPile, HumanSpades),
	countSpades(CompPile, CompSpades),
	write("Human Spades: "),
	writeln(HumanSpades),
	write("Computer Spades: "),
	writeln(CompSpades),

    %Give points for each card
	scoreBySize(1, HumanSpades, CompSpades, HumanSpadeScore, CompSpadeScore),
	containsCardScore(2, "D", "X", HumanPile, HumanDXScore),
	containsCardScore(2, "D", "X", CompPile, CompDXScore),
	containsCardScore(1, "S", 2, HumanPile, HumanS2Score),
	containsCardScore(1, "S", 2, CompPile, CompS2Score),
    
    %Count the ace score
	countAceScore(HumanPile, HumanAceScore),
	countAceScore(CompPile, CompAceScore),

    %Get and output the round score
	HumanRoundScore is HumanSizeScore+HumanSpadeScore+HumanDXScore+HumanS2Score + HumanAceScore,
	CompRoundScore is CompSizeScore+CompSpadeScore+CompDXScore+CompS2Score+CompAceScore,
	write("Round Scores for Round"),
    write(Round),
    writeln(": "),
    write("Human: "),
	writeln(HumanRoundScore),
    write("Computer: "),
	writeln(CompRoundScore),
	nl,
    %Get and output the tour scores
	writeln("Tour Scores"),
	HumanTourScore is HumanStartScore+HumanRoundScore,
	CompTourScore is CompStartScore+CompRoundScore,
    write("Human: "),
	writeln(HumanTourScore),
    write("Computer: "),
	writeln(CompTourScore),

    %Combine the scores
	mergeLists([HumanTourScore], [CompTourScore], EndScores).



%Swap the players and call again
scoreRound(Round, Table, Human, Comp, LastCap, EndScores) :-
	scoreRound(Round, Table, Comp, Human, LastCap, EndScores).	


/* *********************************************************************
Function Name: scoreBySize
Purpose: Give points to larger size, none if tie
Parameters:
            Points to give
            HumanSize The size of Human
            CompSize The size of Comp
Return Value: 
            Points to give to Human
            Points to give to Comp
Algorithm:
            check which is greater
Assistance Received: none
********************************************************************* */
scoreBySize(Points, HumanSize, CompSize, Points, 0) :-
	HumanSize > CompSize.

scoreBySize(Points, HumanSize, CompSize, 0, Points) :-
	CompSize > HumanSize.

scoreBySize(_, _, _, 0, 0).



/* *********************************************************************
Function Name: countSpades
Purpose: CountSpadesInHand
Parameters:
           Hand to check
Return Value: 
            Amount in hand
Algorithm:
            1) If Spade, add one to counter
Assistance Received: none
********************************************************************* */
countSpades([], 0).
countSpades([Current | Rest], Count) :-
	getSuit(Current, Suit),
	Suit = "S",
	countSpades(Rest, RestCount),
	Count is RestCount+1.

countSpades([_ | Rest], Count) :-
	countSpades(Rest, Count).



/* *********************************************************************
Function Name: containCardScore
Purpose: To check if a list contains a card with suit and symbol
Parameters:
            Points  The points to give
            TargetSuit  The Suit to look for
            TargetSym   The Sym to look for
            List  The list to iterate throiugh
Return Value: 
            Points Given
Algorithm:
            1) Check if selected cards are valid
            2) If it is, do the move, otherwise reprompt
Assistance Received: none
********************************************************************* */
containsCardScore(_, _, _, [], 0).	
containsCardScore(Points, TargetSuit, TargetSym, [Current|_], Points)	:-
	getSuit(Current, Suit),
	Suit = TargetSuit,
	getCardSymbol(Current, Sym),
	Sym = TargetSym.

containsCardScore(Points, TargetSuit, TargetSym, [_|Rest], Score) :-
	containsCardScore(Points, TargetSuit, TargetSym, Rest, Score).	


/* *********************************************************************
Function Name: countAceScore
Purpose: ListTo have a capture issued
Parameters:
            The Player making the move
            The other player not making a move
            The table of cards
            Anonymous var
            The index of the played card
            The list of selected indices
Return Value: 
            The First Player after the move
            The other player after the move
            The Table after the move
            Last Cap after the move
Algorithm:
            1) Check if selected cards are valid
            2) If it is, do the move, otherwise reprompt
Assistance Received: none
********************************************************************* */
countAceScore([], 0).

countAceScore([Current| Rest], Score) :-

	getCardVal(Current, Val),
	Val =1,
	countAceScore(Rest, RestScore),
	Score is RestScore+1.

countAceScore([_|Rest],Score):-
	countAceScore(Rest, Score).		



/* *********************************************************************
Function Name: addCardsToLastCap
Purpose: To give the remaining cards to the player which captured last
Parameters:
            The table of cards
			The Human at the Start
			The Computer at the start
			The id of whi captured last
Return Value: 
            The human after giving cards (if any)
			The comp after giving cards (if any)
Algorithm:
            1) Check if Id of player matches last cap id
			2) If it does, all table cards go to them
Assistance Received: none
********************************************************************* */
addCardsToLastCap(Table, HumanStart, Comp, LastCap, Human, Comp) :-
	LastCap = 0,
	%Pass card to human as they were the last capturer
	getPlayerComponents(HumanStart, Id, _, BasePile, _, Score),
	addToPile(Table, BasePile, Pile),
	createPlayer(Id, [], Pile, [], Score, Human),
	writeln("Table cards went to Human").

addCardsToLastCap(Table, Human, CompStart, _, Human, Comp)	:-
	%Pass to computer as they are the last capturer
	getPlayerComponents(CompStart, Id, _, BasePile, _, Score),
	addToPile(Table, BasePile, Pile),
	createPlayer(Id, [], Pile, [], Score, Comp),
	writeln("Table cards went to Comp").


/* *********************************************************************
Function Name: coinFlip
Purpose: To flip the coin at the start of the 0th Round
Parameters:
           None
Return Value: 
            The Id of who captured Last
			The scores which were returned
Algorithm:
            1) Call Toss
			2) See if it matches
			3) If true, go first, otherwise go second
Assistance Received: none
********************************************************************* */
coinFlip(LastCap, ReturnedScores) :-
	%Ask for the call
	writeln("Heads(0) or Tails(1)"),
	getNumericInput(0, 1, Call),
    integer(Call),
    Call =<1,
    Call >=0,
	random(0,2, CoinVal),
	%Find result and start the round
	evalCoinToss(Call, CoinVal, Starting),
	startNewRound(0,  Starting, LastCap,ReturnedScores).
%Invalid input bounce back
coinFlip(LastCap, ReturnedScores) :-
    writeln("Invalid coin toss"),
    coinFlip(LastCap, ReturnedScores).


%Rules to eval a coin toss. Equal have the human going first, mismatch has the comp
%4 seperate rules instead of two due to output strings
evalCoinToss(0, 0, 0) :-
	writeln("Heads! Human goes first!").
evalCoinToss(0, 1, 1) :-
	writeln("Tails! Computer goes first!").	
evalCoinToss(1, 0, 1):-
	writeln("Heads! Computer goes first!").	
evalCoinToss(_, _, 1):-
	writeln("Tails! Human goes first!").		

/* *********************************************************************
Function Name: runTournament
Purpose: To be the entry point into the tournament
Parameters:
            none
Return Value: 
       	none
Algorithm:
            1) Get if the user wants to load a save or new game
			2) Call appropriate clause to handle
			3) Once round completes, score round
Assistance Received: none
********************************************************************* */
runTournament() :-
	writeln("Would you like to start a new game(0), or load a save game(1)?"),
	getNumericInput(0, 1, GameChoice),
    integer(GameChoice),
    GameChoice>=0,
    GameChoice =<1,
	handleTourChoice(GameChoice, LastCap, ResultingScores, Round),
	nth0(0, ResultingScores, HumanScore),
	nth0(1, ResultingScores, CompScore),
	processRoundResults(HumanScore, CompScore, LastCap, Round).

%Bounce back for invalid input
runTournament() :-
    runTournament.


%If 0, call coinflip | If 1 call load
handleTourChoice(0,LastCap, Scores, 1) :- %1 hardcoded for round, as after the end of a first round is always 1
	coinFlip(LastCap, Scores).
handleTourChoice(1, LastCap, Scores, Round) :-
	load(LastCap, Scores, Round).


/* *********************************************************************
Function Name: processRoundResults
Purpose: To see if anyone has won afte a round has been scored
Parameters:
            Human score   The score of the human
			CompScore     The score of the comp
			LastCap		 Id of who captured last
			Round		The round number of the next round
Return Value: 
            None
Algorithm:
            1) See if either player won
			2) If neither has, start next round
Assistance Received: none
********************************************************************* */
%Human won
processRoundResults(HumanScore, CompScore, _ , _) :-
	HumanScore >= 21,
	HumanScore > CompScore,
	writeln("Human won the tour!").

%Comp Won
processRoundResults(HumanScore, CompScore, _ , _ ) :-
	HumanScore >= 21,
	HumanScore = CompScore,
	writeln("The tournamnet was a tie!").

%Tie
processRoundResults(_, CompScore, _ ,_) :-
	CompScore >= 21,
	writeln("Computer won the tour!").	
%No winner, start next round!
processRoundResults(HumanScore, CompScore, LastCap, Round) :-
    NextRound is Round+1,
	startNewRound(NextRound, LastCap, HumanScore, CompScore, LastCapRoundEnd, EndScores),
	nth0(0, EndScores, HumanScore),
	nth0(1, EndScores, CompScore),
	processRoundResults(HumanScore, CompScore, LastCapRoundEnd, NextRound).

