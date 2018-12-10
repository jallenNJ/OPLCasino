
%=======================
%Functions to run a round
%=======================
startNewRound(LastCap, EndScores) :- playRound(0, [],[],[],[], 0, LastCap,EndScores).
startNewRound(Starting, LastCap, EndScores) :- playRound(Starting, [],[],[],[], 0, LastCap, EndScores).
startNewRound(Starting, HumanScore, CompScore, LastCap, EndScores) :- playRound(Starting, HumanScore, CompScore, Starting, LastCap, EndScores).



playRound(FirstId, HumanScore, CompScore, LastCap, LastCapEnd, EndScores) :-
		FirstId = 0,
		createDeck(RawDeck),
		drawFourCards(RawDeck, AfterOneDraw, HumanCards),
		drawFourCards(AfterOneDraw, AfterTwoDraws, CompCards),
		drawFourCards(AfterTwoDraws, Deck, TableCards),
		createNewPlayer(0, HumanCards, HumanScore, HumanPlayer),
		createNewPlayer(1, CompCards, CompScore, ComputerPlayer),
		playRound(FirstId, Deck, TableCards, HumanPlayer, ComputerPlayer, LastCap, LastCapEnd, EndScores).

playRound(FirstId, HumanScore, CompScore, LastCap, LastCapEnd, EndScores) :-
		createDeck(RawDeck),
		drawFourCards(RawDeck, AfterOneDraw, HumanCards),
		drawFourCards(AfterOneDraw, AfterTwoDraws, CompCards),
		drawFourCards(AfterTwoDraws, Deck, TableCards),
		createNewPlayer(0, HumanCards, HumanScore, HumanPlayer),
		createNewPlayer(1, CompCards, CompScore, ComputerPlayer),
		playRound(FirstId, Deck, TableCards, ComputerPlayer, HumanPlayer, LastCap, LastCapEnd, EndScores).

playRound(FirstId,[],[],[],[],LastCap, LastCapEnd, EndScores) :- 
		createDeck(RawDeck),
		drawFourCards(RawDeck, AfterOneDraw, HumanCards),
		drawFourCards(AfterOneDraw, AfterTwoDraws, CompCards),
		drawFourCards(AfterTwoDraws, Deck, TableCards),
		createNewPlayer(0, HumanCards, HumanPlayer),
		createNewPlayer(1, CompCards, ComputerPlayer),
		playRound(FirstId, Deck, TableCards, HumanPlayer, ComputerPlayer, LastCap, LastCapEnd, EndScores).


%id, deck, table, p0Info, p1Info, retVal
playRound(FirstId,[],[],[],[],LastCap, LastCapEnd, EndScores) :- 
		createDeck(RawDeck),
		drawFourCards(RawDeck, AfterOneDraw, HumanCards),
		drawFourCards(AfterOneDraw, AfterTwoDraws, CompCards),
		drawFourCards(AfterTwoDraws, Deck, TableCards),
		createNewPlayer(0, HumanCards, HumanPlayer),
		createNewPlayer(1, CompCards, ComputerPlayer),
		playRound(FirstId, Deck, TableCards, HumanPlayer, ComputerPlayer, LastCap, LastCapEnd,EndScores).

%Deal new hands when hands are empty
playRound(FirstId, Deck, Table, P0Info, P1Info, LastCap, LastCapEnd, EndScores) :-
	getHand(P0Info, P0Hand),
	length(P0Hand, 0),
	getHand(P1Info, P1Hand),
	length(P1Hand, 0),
	length(Deck, CardsInDeck),
	CardsInDeck >= 8,
	drawFourCards(Deck, AfterOneDraw, P0Cards),
	drawFourCards(AfterOneDraw, AfterTwoDraws, P1Cards),
	getPlayerComponents(P0Info, P0Id, _, P0Pile, P0Reserved, P0Score),
	createPlayer(P0Id, P0Cards, P0Pile, P0Reserved, P0Score, NewP0),
	getPlayerComponents(P1Info, P1Id, _, P1Pile, P1Reserved, P1Score),
	createPlayer(P1Id, P1Cards, P1Pile, P1Reserved, P1Score, NewP1),
	playRound(FirstId, AfterTwoDraws, Table, NewP0, NewP1, LastCap, LastCapEnd, EndScores).
	

%Round end rule, deck is empty, player infos have null hands
%TODO, make sure to print table
playRound(_, Deck, Table, P0Info, P1Info, LastCap,LastCapEnd, EndScores)	:-
	getHand(P0Info, P0Hand),
	length(P0Hand, 0),
	getHand(P1Info, P1Hand),
	length(P1Hand, 0),
	length(Deck, CardsInDeck),
	CardsInDeck < 8,
	LastCapEnd = LastCap,
	scoreRound(Table, P0Info, P1Info, LastCap, EndScores).

%Main loop
playRound(FirstId, Deck, Table, P0Info, P1Info, LastCap, LastCapEnd, EndScores) :-
	printFullTable(P0Info, Table, P1Info, Deck),
	getActionMenuChoice(P0Info, MenuChoice),
	handleMenuChoice(MenuChoice, FirstId, Deck, Table, P0Info, P1Info, LastCap),
	doPlayerMove(P0Info, P1Info, Table, LastCap, P0AfterMove, P1AfterP0, TableAfterP0, LastCapAfterP0),
	printFullTable(P0AfterMove, TableAfterP0, P1AfterP0, Deck),
	getActionMenuChoice(P1Info, MenuChoice2),
    findOtherId(FirstId, OtherId),
	handleMenuChoice(MenuChoice2, OtherId, Deck, TableAfterP0, P0AfterMove, P1AfterP0, LastCapAfterP0),
	doPlayerMove(P1AfterP0, P0AfterMove, TableAfterP0, LastCapAfterP0, P1AfterMove, P0AfterP1, TableAfterP1, LastCapAfterP1),
	playRound(FirstId, Deck, TableAfterP1, P0AfterP1, P1AfterMove, LastCapAfterP1, LastCapEnd, EndScores).


findOtherId(0,1).
findOtherId(1,0).

scoreRound(Table, HumanStart, CompStart, LastCap, EndScores) :-
	isHuman(HumanStart),
	addCardsToLastCap(Table, HumanStart, CompStart, LastCap, Human, Comp),
	getPile(Human, HumanPile),
	getPile(Comp, CompPile),
	getScore(Human, HumanStartScore),
	getScore(Comp, CompStartScore),
	length(HumanPile, HumanPileSize),
	length(CompPile, CompPileSize),
	write("Human Pile Size: "),
	writeln(HumanPileSize),
	write("Computer Pile Size: "),
	writeln(CompPileSize),
	scoreBySize(3, HumanPileSize, CompPileSize, HumanSizeScore, CompSizeScore),
	countSpades(HumanPile, HumanSpades),
	countSpades(CompPile, CompSpades),
	write("Human Spades: "),
	writeln(HumanSpades),
	write("Computer Spades: "),
	writeln(CompSpades),
	scoreBySize(1, HumanSpades, CompSpades, HumanSpadeScore, CompSpadeScore),
	containsCardScore(2, "H", "X", HumanPile, HumanDXScore),
	containsCardScore(2, "H", "X", CompPile, CompDXScore),
	containsCardScore(1, "S", 2, HumanPile, HumanS2Score),
	containsCardScore(1, "S", 2, CompPile, CompS2Score),
	countAceScore(HumanPile, HumanAceScore),
	countAceScore(CompPile, CompAceScore),

	HumanRoundScore is HumanSizeScore+HumanSpadeScore+HumanDXScore+HumanS2Score + HumanAceScore,
	CompRoundScore is CompSizeScore+CompSpadeScore+CompDXScore+CompS2Score+CompAceScore,
	writeln("Round Scores:"),
	writeln(HumanRoundScore),
	writeln(CompRoundScore),
	nl,
	writeln("Tour Scores"),
	HumanTourScore is HumanStartScore+HumanRoundScore,
	CompTourScore is CompStartScore+CompRoundScore,
	writeln(HumanTourScore),
	writeln(CompTourScore),
	mergeLists([HumanTourScore], [CompTourScore], EndScores).



scoreRound(Table, Human, Comp, LastCap, EndScores) :-
	scoreRound(Table, Comp, Human, LastCap, EndScores).	


scoreBySize(Points, HumanSize, CompSize, Points, 0) :-
	HumanSize > CompSize.

scoreBySize(Points, HumanSize, CompSize, 0, Points) :-
	CompSize > HumanSize.

scoreBySize(_, _, _, 0, 0).

countSpades([], 0).
countSpades([Current | Rest], Count) :-
	getSuit(Current, Suit),
	Suit = "S",
	countSpades(Rest, RestCount),
	Count is RestCount+1.

countSpades([_ | Rest], Count) :-
	countSpades(Rest, Count).

containsCardScore(_, _, _, [], 0).	
containsCardScore(Points, TargetSuit, TargetSym, [Current|_], Points)	:-
	getSuit(Current, Suit),
	Suit = TargetSuit,
	getCardSymbol(Current, Sym),
	Sym = TargetSym.

containsCardScore(Points, TargetSuit, TargetSym, [_|Rest], Score) :-
	containsCardScore(Points, TargetSuit, TargetSym, Rest, Score).	

countAceScore([], 0).

countAceScore([Current| Rest], Score) :-

	getCardVal(Current, Val),
	Val =1,
	countAceScore(Rest, RestScore),
	Score is RestScore+1.

countAceScore([_|Rest],Score):-
	countAceScore(Rest, Score).		



addCardsToLastCap(Table, HumanStart, Comp, LastCap, Human, Comp) :-
	LastCap = 0,
	getPlayerComponents(HumanStart, Id, _, BasePile, _, Score),
	addToPile(Table, BasePile, Pile),
	createPlayer(Id, [], Pile, [], Score, Human),
	writeln("Table cards went to Human").

addCardsToLastCap(Table, Human, CompStart, _, Human, Comp)	:-
	getPlayerComponents(CompStart, Id, _, BasePile, _, Score),
	addToPile(Table, BasePile, Pile),
	createPlayer(Id, [], Pile, [], Score, Comp),
	writeln("Table cards went to Comp").


coinFlip(LastCap, ReturnedScores) :-
	writeln("Heads(0) or Tails(1)"),
	getNumericInput(0, 1, Call),
	random(0,2, CoinVal),
	evalCoinToss(Call, CoinVal, Starting),
	startNewRound( Starting, LastCap,ReturnedScores).


evalCoinToss(0, 0, 0) :-
	writeln("Heads! Human goes first!").
evalCoinToss(0, 1, 1) :-
	writeln("Tails! Computer goes first!").	
evalCoinToss(1, 0, 1):-
	writeln("Heads! Computer goes first!").	
evalCoinToss(_, _, 1):-
	writeln("Tails! Human goes first!").		

runTournament() :-
	writeln("Would you like to start a new game(0), or load a save game(1)?"),
	getNumericInput(0, 1, GameChoice),
	handleTourChoice(GameChoice, LastCap, ResultingScores),
	nth0(0, ResultingScores, HumanScore),
	nth0(1, ResultingScores, CompScore),
	processRoundResults(HumanScore, CompScore, LastCap).

handleTourChoice(0,LastCap, Scores) :-
	coinFlip(LastCap, Scores).
handleTourChoice(1, LastCap, Scores) :-
	load(LastCap, Scores).

processRoundResults(HumanScore, CompScore, _ ) :-
	HumanScore >= 21,
	HumanScore > CompScore,
	writeln("Human won the tour!").

processRoundResults(HumanScore, CompScore, _ ) :-
	HumanScore >= 21,
	HumanScore = CompScore,
	writeln("The tournamnet was a tie!").

processRoundResults(_, CompScore, _ ) :-
	CompScore >= 21,
	writeln("Computer won the tour!").	

processRoundResults(HumanScore, CompScore, LastCap) :-
	startNewRound(LastCap, HumanScore, CompScore, LastCapRoundEnd, EndScores),
	nth0(0, EndScores, HumanScore),
	nth0(1, EndScores, CompScore),
	processRoundResults(HumanScore, CompScore, LastCapRoundEnd).

