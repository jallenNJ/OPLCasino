%=======================
%Functions to create and manipulate the player data structure
%=======================
%Wrapper to createPlayer
createNewPlayer(Id, StartingCards, CreatedPlayer) :-
	createPlayer(Id, StartingCards, [], [], 0, CreatedPlayer).

createNewPlayer(Id, StartingCards, StartingScore, CreatedPlayer) :-
	createPlayer(Id, StartingCards, [], [], StartingScore, CreatedPlayer).	

%Creates a playerList from the given parameters
createPlayer(Id, StartingCards, StartingPile, StartingReserved, Score, CreatedPlayer) :-
	mergeLists([Id], [StartingCards], MergeOne),
	mergeLists(MergeOne, [StartingPile],MergeTwo),
	mergeLists(MergeTwo, [StartingReserved], MergeThree),
	mergeLists(MergeThree, [Score], CreatedPlayer).


getId(PlayerList, PlayerId) :-
	nth0(0, PlayerList, PlayerId).

getHand(PlayerList, PlayerHand) :-
	nth0(1, PlayerList, PlayerHand).

getPile(PlayerList, PlayerPile) :-
	nth0(2, PlayerList, PlayerPile).

getReserved(PlayerList, PlayerReserved) :-
	nth0(3, PlayerList, PlayerReserved).

getScore(PlayerList, PlayerScore) :-
	nth0(4, PlayerList, PlayerScore).

getPlayerComponents(PlayerList, Id, Hand, Pile, Reserved, Score) :-
	getId(PlayerList, Id),
	getHand(PlayerList, Hand),
	getPile(PlayerList, Pile),
	getReserved(PlayerList, Reserved),
	getScore(PlayerList, Score).



isHuman(PlayerList) :-
	getId(PlayerList, Id),
	Id = 0.

getPlayerName(PlayerList, Name) :-
	isHuman(PlayerList),
	Name = "Human".

getPlayerName(_, "Computer").

listContainsValue([], _, _, -1).

listContainsValue([CurrentCard|_], TargetVal, Index, FoundIndex) :-
	getCardVal(CurrentCard, CardVal),
	CardVal = TargetVal,
	FoundIndex = Index.

listContainsValue([_|Rest], TargetVal, Index, FoundIndex) :-
	NextIndex is Index+1,
	listContainsValue(Rest, TargetVal, NextIndex, FoundIndex).


addToPile(NewCards, StartingPile, PileWithAddedCards) :-
	getBuildCards(NewCards, ListOfCards),
	mergeLists(StartingPile, ListOfCards, PileWithAddedCards).

    
%=======================
%Functions for round to call, resolves to human or computer
%=======================

%Human Player
doPlayerMove(PlayerList, Table, LastCap, PlayerAfterMove, TableAfterMove, LastCapAfterMove) :-
	isHuman(PlayerList),
	getActionChoice(MoveChoice),
	integer(MoveChoice),
	doHumanMove(MoveChoice, PlayerList, Table, LastCap, PlayerAfterMove, TableAfterMove, LastCapAfterMove).

%Comp
doPlayerMove(PlayerList, Table, LastCap, PlayerAfterMove, TableAfterMove, LastCapAfterMove) :-
	doComputerMove( PlayerList, Table, LastCap, PlayerAfterMove, TableAfterMove, LastCapAfterMove).


%=======================
%Functions for human to make a move
%=======================
doHumanMove(0, PlayerList, Table, _, PlayerAfterMove, TableAfterMove, LastCapAfterMove) :-
	getHand(PlayerList, Hand),
	prompt1("Which card would you like to Capture with?"),
	length(Hand, CardsInHandPlusOne),
	CardsInHand is CardsInHandPlusOne-1,
	getNumericInput(0, CardsInHand, CaptureCardIndex),
	length(Table, TableCardAmount),
	TableAllowedIndices is TableCardAmount-1,
	getMultipleNumericInput(TableAllowedIndices, SelectedCardIndices),
	doCapture(PlayerList, Table, CaptureCardIndex, SelectedCardIndices, PlayerAfterMove, TableAfterMove, LastCapAfterMove).

doHumanMove(1, PlayerList, Table, LastCap, PlayerAfterMove, TableAfterMove, LastCapAfterMove) :-
	getHand(PlayerList, Hand),
	prompt1("Which card would you like to Build with?"),
	length(Hand, CardsInHandPlusOne),
	CardsInHand is CardsInHandPlusOne-1,
	getNumericInput(0, CardsInHand, PlayedCardIndex),
	length(Table, TableCardAmount),
	TableAllowedIndices is TableCardAmount-1,
	getMultipleNumericInput(TableAllowedIndices, SelectedCardIndices),
	doBuild(PlayerList, Table, PlayedCardIndex, SelectedCardIndices, LastCap,PlayerAfterMove, TableAfterMove, LastCapAfterMove).

doHumanMove(2, PlayerList, Table, LastCap, PlayerAfterMove, TableAfterMove, LastCapAfterMove) :-
	getHand(PlayerList, Hand),
	prompt1("Which card would you like to trail?"),
	length(Hand, CardsInHandPlusOne),
	CardsInHand is CardsInHandPlusOne-1,
	getNumericInput(0, CardsInHand, TrailedCardIndex),
	doTrail(PlayerList, Table, TrailedCardIndex, LastCap, PlayerAfterMove, TableAfterMove, LastCapAfterMove).


%=======================
%Functions for computer to make a move
%=======================


checkBuild([SumCard | _], Hand, Table, HandAfterMove, TableAfterMove, BuildVal) :-
	checkAsSumCard(SumCard, Hand,Table, HandAfterMove, TableAfterMove, BuildVal).

checkBuild([_|RestHand], Hand, Table,HandAfterMove, TableAfterMove, BuildVal) :-
	checkBuild(RestHand, Hand, Table,HandAfterMove, TableAfterMove, BuildVal ).

checkAsSumCard(SumCard, Hand, Table, HandAfterMove, TableAfterMove, BuildVal) :-
	getCardVal(SumCard, SumVal),
	findPlayCard(SumVal, Hand, PlayCard, HandAfterMove),
	getCardVal(PlayCard, PlayVal),
	TargetVal is SumVal-PlayVal,
	findSet(Table, TargetVal, RemainingTableCards, NewBuildCards),
	Build = [PlayCard | NewBuildCards],
	%TableAfterAdding = [Build | RemainingTableCards],
	
	checkForMulti(Build, SumVal, RemainingTableCards, TableAfterMove),
	BuildVal = SumVal.





findPlayCard(SumVal, [First | Rest], First, Rest) :-
	getCardVal(First, FirstVal),
	FirstVal < SumVal.

findPlayCard(SumVal, [Skipped | Rest], SumCard, HandWithoutPlay) :-
	findPlayCard(SumVal, Rest, SumCard, RestHand),
	HandWithoutPlay=[Skipped | RestHand].



doComputerMove(PlayerList, Table, LastCap, PlayerAfterMove, TableAfterMove, LastCap) :-
	getHand(PlayerList, Hand),
	checkBuild(Hand, Hand, Table, HandAfterMove, TableAfterMove, BuildVal),
	getPlayerComponents(PlayerList, Id, _, Pile, OldReserved, Score),
	ReservedUnsorted = [BuildVal | OldReserved],
	sort(0, @<, ReservedUnsorted, Reserved),
	createPlayer(Id, HandAfterMove, Pile, Reserved, Score, PlayerAfterMove).


doComputerMove(PlayerList, Table, _, PlayerAfterMove, TableAfterMove, LastCapAfterMove):-
	getHand(PlayerList, Hand),
	checkSetCapture(Hand, Table, HandAfterMove, TableAfterMove,CapturedCards),
	length(CapturedCards, AmountCapped),
	AmountCapped > 1,
	getPlayerComponents(PlayerList, Id, _, StartingPile, Reserved, Score),
	printCards(CapturedCards),
	%addToPile(StartingPile, CapturedCards, NewPile),
	addToPile(CapturedCards, StartingPile, NewPile),
	LastCapAfterMove = Id,
	createPlayer(Id, HandAfterMove, NewPile, Reserved, Score, PlayerAfterMove).


%	not(MatchedCardsIndices=[]),
%	write("DEBEEEEEBUG AI CAPTURE"),
	%doCapture(PlayerList, Table, PlayedCardIndex, MatchedCardsIndices, PlayerAfterMove, TableAfterMove).



doComputerMove(PlayerList, Table, _, PlayerAfterMove, TableAfterMove, LastCapAfterMove):-
	getHand(PlayerList, Hand),
	checkForMatchingCaptures(Hand, Table, 0, PlayedCardIndex,MatchedCardsIndices),
	not(MatchedCardsIndices=[]),
	write("DEBEEEEEBUG AI CAPTURE"),
	doCapture(PlayerList, Table, PlayedCardIndex, MatchedCardsIndices, PlayerAfterMove, TableAfterMove, LastCapAfterMove).

	


%doComputerMove(1, PlayerList, Table, PlayerAfterMove, TableAfterMove)
doComputerMove(PlayerList, Table, LastCap, PlayerAfterMove, TableAfterMove, LastCapAfterMove) :-
	doTrail(PlayerList, Table, 0, LastCap, PlayerAfterMove, TableAfterMove, LastCapAfterMove),
	displayComputerMove(PlayerList, 0).
	


checkSetCapture([], _, [], [],[]).

checkSetCapture([Card|HandAfterMove], Table, HandAfterMove, TableAfterMove, CapturedCards) :-
	getCardVal(Card, Target),
	findSet(Table, Target, TableAfterMove, CapturedCardsFromTable),
	length(CapturedCardsFromTable, AmountCapped),
	AmountCapped > 1,
	CapturedCards = [Card|CapturedCardsFromTable].

checkSetCapture([Skipped| Rest], Table, HandAfterMove, TableAfterMove, CapturedCards):-
	checkSetCapture(Rest, Table, HandAfterCap, TableAfterMove, CapturedCards),
	HandAfterMove=[Skipped | HandAfterCap].

	
findSet([FirstCard| Rest], Target, TableAfterMove, Captured) :-
	getCardVal(FirstCard, ThisVal),
	NextTarget is Target-ThisVal,
	NextTarget >=0,
	findSet(Rest, NextTarget, TableAfterMove, ThatCaptured),
	Captured = [FirstCard | ThatCaptured].

findSet(Table, 0, Table, []).


findSet([First | Rest], Target, TableAfterMove, Captured) :-
	findSet(Rest, Target, Table, Captured),
	TableAfterMove=[First|Table].

checkForMatchingCaptures([], _, _, [], []).

checkForMatchingCaptures([CurrentCard | _], Table, Index, CardWithMatches, MatchedCards):-
	getCardVal(CurrentCard, CardVal),
	findMatchingValsIndices(Table, CardVal, 0, MatchingCards),
	length(MatchingCards, CardsThatMatched),
	CardsThatMatched > 0,
	CardWithMatches = Index,
	MatchedCards = MatchingCards.


checkForMatchingCaptures([_ | RestHand], Table, Index, CardWithMatches, MatchedCards) :-
	NextIndex is Index+1,
	checkForMatchingCaptures(RestHand, Table, NextIndex, CardWithMatches, MatchedCards).


%=======================
%Functions to validate and execute the choosen move (for both players)
%=======================
doCapture(PlayerList, Table, PlayedCardIndex, SelectedCardIndices, PlayerAfterMove, TableAfterMove, LastCapAfterMove) :-
	%Get the hand and ensure the Played index is instaniated
	getHand(PlayerList, Hand),
	integer(PlayedCardIndex),
	%Remove played card from the hand and get its value
	removeAtIndex(Hand, PlayedCardIndex, ResultingHand, CaptureCard),
	getCardVal(CaptureCard, CaptureVal),
	%Sum all selected cards to make sure the capture is valid
	sumSelectedCards(SelectedCardIndices, Table, Sum),
	ModResult is mod(Sum, CaptureVal),
	ModResult = 0,
	%Remove the cards
	removeAllIndices(SelectedCardIndices, Table, TableAfterMove, CaputuredCards),
	%Ensure atleast one card is selected
	length(CaputuredCards, CapturedAmounts),
	CapturedAmounts > 0,
	%Ensure all required cards are taken
	getCardSymbol(CaptureCard, CaptureSym),
	removeMatchingSymbols(TableAfterMove, CaptureSym, _, Matching),
	length(Matching, MatchingSize),
	MatchingSize =0,
	%Make the updated player
	getPlayerComponents(PlayerList, Id, _, StartingPile, Reserved, Score),
	mergeLists(StartingPile, [CaptureCard], PilewithCapCard),
	addToPile( CaputuredCards, PilewithCapCard,AllPileCards),
	LastCapAfterMove = Id,
	createPlayer(Id, ResultingHand, AllPileCards, Reserved, Score, PlayerAfterMove).


doBuild(PlayerList, Table, PlayedCardIndex, SelectedCardIndices, LastCap, PlayerAfterMove, TableAfterMove, LastCap) :-	
	getHand(PlayerList, Hand),
	integer(PlayedCardIndex),
	%Remove played card from the hand and get its value
	removeAtIndex(Hand, PlayedCardIndex, ResultingHand, PlayedCard),
	getCardVal(PlayedCard, PlayedVal),
	sumSelectedCards(SelectedCardIndices, Table, Sum),
	BuildValue is Sum+PlayedVal,
	BuildValue =< 14,
	listContainsValue(ResultingHand, BuildValue, 0, ReserveCardIndex),
	ReserveCardIndex >=0,
	getReserved(PlayerList, StartingReserved),
	NewReservedUnsorted = [BuildValue | StartingReserved],
	sort(0 ,@<, NewReservedUnsorted, NewReserved),
	removeAllIndices(SelectedCardIndices, Table, TableAfterCardsRemoved, TableBuildCards),
	makeBuild(TableBuildCards, PlayedCard, NewBuild),
	%TableAfterNewBuild = [NewBuild | TableAfterCardsRemoved],
	checkForMulti(NewBuild, BuildValue, TableAfterCardsRemoved, TableAfterMove),
	getPlayerComponents(PlayerList, Id, _, Pile, _, Score),
	createPlayer(Id, ResultingHand, Pile, NewReserved, Score, PlayerAfterMove).

doTrail(PlayerList, Table, PlayedCardIndex, LastCap, PlayerAfterMove, TableAfterMove, LastCap) :-
	writeln("InTrail"),
	writeln(LastCap),
	writeln(PlayedCardIndex),
	getHand(PlayerList, Hand),
	integer(PlayedCardIndex),
		writeln("Passed int"),
	removeAtIndex(Hand, PlayedCardIndex, ResultingHand, TrailedCard),
	mergeLists(Table, [TrailedCard], TableAfterMove),
		writeln("Did Move"),
	getId(PlayerList, Id),
	getPile(PlayerList, Pile),
	getReserved(PlayerList, Reserved),
	getScore(PlayerList, Score),
	createPlayer(Id, ResultingHand, Pile, Reserved, Score,PlayerAfterMove).



checkForMulti(NewBuild, _, [], [NewBuild]).

checkForMulti(NewBuild, BuildValue, [Current | RestCard],  TableAfterMove) :-
	sumCardList(Current, CurrentVal),
	BuildValue = CurrentVal,
	NewMulti = [NewBuild | [Current]],
	TableAfterMove = [NewMulti | RestCard].

checkForMulti(NewBuild, BuildValue, [Current | RestCard],  TableAfterMove) :-
	checkForMulti(NewBuild, BuildValue, RestCard, RestTable),
	TableAfterMove = [Current | RestTable].
