removeVal([], _, []).

removeVal([Target | Rest], Target, Result) :-
    removeVal(Rest, Target, Result).

removeVal([First | Rest], Target, Result) :-
    removeVal(Rest, Target, NewResult),
    Result = [First|NewResult].



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
doPlayerMove(PlayerList, OtherPlayer, Table, LastCap, PlayerAfterMove, OtherPlayerAfterMove, TableAfterMove, LastCapAfterMove) :-
	isHuman(PlayerList),
	getActionChoice(MoveChoice),
	integer(MoveChoice),
	doHumanMove(MoveChoice, PlayerList, OtherPlayer, Table, LastCap, PlayerAfterMove, OtherPlayerAfterMove, TableAfterMove, LastCapAfterMove).

%Comp
doPlayerMove(PlayerList, OtherPlayer, Table, LastCap, PlayerAfterMove,OtherPlayerAfterMove, TableAfterMove, LastCapAfterMove) :-
	doComputerMove( PlayerList, OtherPlayer, Table, LastCap, "Playing", PlayerAfterMove, OtherPlayerAfterMove, TableAfterMove, LastCapAfterMove).


%=======================
%Functions for human to make a move
%=======================
doHumanMove(0, PlayerList, OtherPlayer, Table, _, PlayerAfterMove, OtherPlayerAfterMove, TableAfterMove, LastCapAfterMove) :-
	getHand(PlayerList, Hand),
	prompt1("Which card would you like to Capture with?"),
	length(Hand, CardsInHandPlusOne),
	CardsInHand is CardsInHandPlusOne-1,
	getNumericInput(0, CardsInHand, CaptureCardIndex),
	length(Table, TableCardAmount),
	TableAllowedIndices is TableCardAmount-1,
	getMultipleNumericInput(TableAllowedIndices, SelectedCardIndices),
	doCapture(PlayerList, OtherPlayer, Table, CaptureCardIndex, SelectedCardIndices, PlayerAfterMove, OtherPlayerAfterMove, TableAfterMove, LastCapAfterMove).

doHumanMove(1, PlayerList, OtherPlayer, Table, LastCap, PlayerAfterMove, OtherPlayerAfterMove, TableAfterMove, LastCapAfterMove) :-
	getHand(PlayerList, Hand),
	prompt1("Which card would you like to Build with?"),
	length(Hand, CardsInHandPlusOne),
	CardsInHand is CardsInHandPlusOne-1,
	getNumericInput(0, CardsInHand, PlayedCardIndex),
	length(Table, TableCardAmount),
	TableAllowedIndices is TableCardAmount-1,
	getMultipleNumericInput(TableAllowedIndices, SelectedCardIndices),
	doBuild(PlayerList, OtherPlayer, Table, PlayedCardIndex, SelectedCardIndices, LastCap,PlayerAfterMove, OtherPlayerAfterMove, TableAfterMove, LastCapAfterMove).

doHumanMove(2, PlayerList, OtherPlayer, Table, LastCap, PlayerAfterMove, OtherPlayer, TableAfterMove, LastCapAfterMove) :-
	getHand(PlayerList, Hand),
	prompt1("Which card would you like to trail?"),
	length(Hand, CardsInHandPlusOne),
	CardsInHand is CardsInHandPlusOne-1,
	getNumericInput(0, CardsInHand, TrailedCardIndex),
	doTrail(PlayerList, Table, TrailedCardIndex, LastCap, PlayerAfterMove, TableAfterMove, LastCapAfterMove).


%=======================
%Functions for computer to make a move
%=======================


checkBuild([SumCard | _], Hand, Table, Action, HandAfterMove, TableAfterMove, BuildVal) :-
	checkAsSumCard(SumCard, Hand,Table, Action, HandAfterMove, TableAfterMove, BuildVal).

checkBuild([_|RestHand], Hand, Table, Action, HandAfterMove, TableAfterMove, BuildVal) :-
	checkBuild(RestHand, Hand, Table, Action, HandAfterMove, TableAfterMove, BuildVal ).

checkAsSumCard(SumCard, Hand, Table, Action, HandAfterMove, TableAfterMove, BuildVal) :-
	getCardVal(SumCard, SumVal),
	findPlayCard(SumVal, Hand, PlayCard, HandAfterMove),
	getCardVal(PlayCard, PlayVal),
	TargetVal is SumVal-PlayVal,
	findSet(Table, TargetVal, RemainingTableCards, NewBuildCards),
	Build = [PlayCard | NewBuildCards],
    writeln("DEBUG"),
    writeln(NewBuildCards),
    displayComputerMove(PlayCard, NewBuildCards, Action, "Build", "Saw an oppertunity to make a build"),
	%TableAfterAdding = [Build | RemainingTableCards],
	
	checkForMulti(Build, SumVal, RemainingTableCards, TableAfterMove),
	BuildVal = SumVal.





findPlayCard(SumVal, [First | Rest], First, Rest) :-
	getCardVal(First, FirstVal),
	FirstVal < SumVal.

findPlayCard(SumVal, [Skipped | Rest], SumCard, HandWithoutPlay) :-
	findPlayCard(SumVal, Rest, SumCard, RestHand),
	HandWithoutPlay=[Skipped | RestHand].


%TODO: Check for Ai removing val
doComputerMove(PlayerList, OtherPlayer, Table, LastCap, Action, PlayerAfterMove, OtherPlayerAfterMove, TableAfterMove, LastCap) :-
	getHand(PlayerList, Hand),
	checkBuild(Hand, Hand, Table, Action, HandAfterMove, TableAfterMove, BuildVal),
	getPlayerComponents(PlayerList, Id, _, Pile, OldReserved, Score),
	ReservedUnsorted = [BuildVal | OldReserved],
	sort(0, @<, ReservedUnsorted, Reserved),
    OtherPlayer = OtherPlayerAfterMove,
	createPlayer(Id, HandAfterMove, Pile, Reserved, Score, PlayerAfterMove).


doComputerMove(PlayerList, OtherPlayer, Table, _, Action, PlayerAfterMove, OtherPlayerAfterMove, TableAfterMove, LastCapAfterMove):-
	getHand(PlayerList, Hand),
	checkSetCapture(Hand, Table, Action, HandAfterMove, TableAfterMove,CapturedCards, PlayedVal),
	length(CapturedCards, AmountCapped),
	AmountCapped > 1,
	getPlayerComponents(PlayerList, Id, _, StartingPile, RawReserved, Score),
	addToPile(CapturedCards, StartingPile, NewPile),
	LastCapAfterMove = Id,
    removeVal(RawReserved, PlayedVal, Reserved),
	createPlayer(Id, HandAfterMove, NewPile, Reserved, Score, PlayerAfterMove),
    getPlayerComponents(OtherPlayer, OId, OHand, OPile, ORawReserved, OScore),
    removeVal(ORawReserved, PlayedVal, OReserved),
    createPlayer(OId, OHand, OPile, OReserved, OScore, OtherPlayerAfterMove).


doComputerMove(PlayerList, OtherPlayer, Table, _, Action, PlayerAfterMove, OtherPlayerAfterMove, TableAfterMove, LastCapAfterMove):-
	getHand(PlayerList, Hand),
	checkForMatchingCaptures(Hand, Table, 0, PlayedCardIndex,MatchedCardsIndices),
	not(MatchedCardsIndices=[]),
    getCardsAtIndicies(Hand, [PlayedCardIndex], PlayedCard),
    getCardsAtIndicies(Table, MatchedCardsIndices, CapturedCards),
    displayComputerMove(PlayedCard, CapturedCards, Action, "Capture", "To capture identical symbols"),
    writeln(Action),
	%displayComputerMove(PlayedCard, TableCards, Action, Reason)
	doCapture(PlayerList, OtherPlayer, Table, PlayedCardIndex, MatchedCardsIndices, PlayerAfterMove, OtherPlayerAfterMove, TableAfterMove, LastCapAfterMove).

	


%doComputerMove(1, PlayerList, Table, PlayerAfterMove, TableAfterMove)
doComputerMove(PlayerList, OtherPlayer, Table, LastCap, Action, PlayerAfterMove, OtherPlayer, TableAfterMove, LastCapAfterMove) :-
	doTrail(PlayerList, Table, 0, LastCap, PlayerAfterMove, TableAfterMove, LastCapAfterMove),
	displayComputerMove(PlayerList, Action, 0).
	


checkSetCapture([], _, [], [],[], _).

checkSetCapture([Card|HandAfterMove], Table, Action, HandAfterMove, TableAfterMove, CapturedCards, PlayedVal) :-
	getCardVal(Card, Target),
	findSet(Table, Target, TableAfterMove, CapturedCardsFromTable),
	length(CapturedCardsFromTable, AmountCapped),
	AmountCapped > 1,
	CapturedCards = [Card|CapturedCardsFromTable],
    PlayedVal = Target,
    displayComputerMove(Card, CapturedCardsFromTable, Action, "Capture", "Saw an oppertunity to capture a set").

checkSetCapture([Skipped| Rest], Table, Action, HandAfterMove, TableAfterMove, CapturedCards, PlayedVal):-
	checkSetCapture(Rest, Table, Action, HandAfterCap, TableAfterMove, CapturedCards, PlayedVal),
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
doCapture(PlayerList, OtherPlayer, Table, PlayedCardIndex, SelectedCardIndices, PlayerAfterMove, OtherPlayerAfterMove, TableAfterMove, LastCapAfterMove) :-
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
	getPlayerComponents(PlayerList, Id, _, StartingPile, RawReserved, Score),
	mergeLists(StartingPile, [CaptureCard], PilewithCapCard),
	addToPile( CaputuredCards, PilewithCapCard,AllPileCards),
	LastCapAfterMove = Id,
    removeVal(RawReserved, CaptureVal, Reserved),
	createPlayer(Id, ResultingHand, AllPileCards, Reserved, Score, PlayerAfterMove),
    getPlayerComponents(OtherPlayer, OId, OHand, OPile, ORawReserved, OScore),
    removeVal(ORawReserved, CaptureVal, OReserved),
    createPlayer(OId, OHand, OPile, OReserved, OScore, OtherPlayerAfterMove).


doBuild(PlayerList, OtherPlayer, Table, PlayedCardIndex, SelectedCardIndices, LastCap, PlayerAfterMove, OtherPlayerAfterMove, TableAfterMove, LastCap) :-	
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
	createPlayer(Id, ResultingHand, Pile, NewReserved, Score, PlayerAfterMove),
    getPlayerComponents(OtherPlayer, OId, OHand, OPile, ORawReserved, OScore),
    removeVal(ORawReserved, Sum, OReserved),
    createPlayer(OId, OHand, OPile, OReserved, OScore, OtherPlayerAfterMove).


doTrail(PlayerList, Table, PlayedCardIndex, LastCap, PlayerAfterMove, TableAfterMove, LastCap) :-
	getHand(PlayerList, Hand),
	integer(PlayedCardIndex),
	removeAtIndex(Hand, PlayedCardIndex, ResultingHand, TrailedCard),
	mergeLists(Table, [TrailedCard], TableAfterMove),
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
