/* *********************************************************************
Function Name: removeVal
Purpose: To remove the specified value from the list
Parameters:
            The list of values
            The value to remove
Return Value: The list with values removed
Algorithm:
            1) If value at head matches, skip over it
Assistance Received: none
********************************************************************* */ 

removeVal([], _, []).

removeVal([Target | Rest], Target, Result) :-
    removeVal(Rest, Target, Result).

removeVal([First | Rest], Target, Result) :-
    removeVal(Rest, Target, NewResult),
    Result = [First|NewResult].



%=======================
%Functions to create and manipulate the player data structure
%=======================


/* *********************************************************************
Function Name: createNewPlayer
Purpose: Wrapper function to make a new player using less parameters
Parameters:
            Id of the player,
            The Starting hand
            The Starting Score (createNewPlayer\4 only)
Return Value: The created player
Algorithm:
            1) Call the createPlayer function with missing parameters as their default value
Assistance Received: none
********************************************************************* */ 
%Wrapper to createPlayer
createNewPlayer(Id, StartingCards, CreatedPlayer) :-
	createPlayer(Id, StartingCards, [], [], 0, CreatedPlayer).

createNewPlayer(Id, StartingCards, StartingScore, CreatedPlayer) :-
	createPlayer(Id, StartingCards, [], [], StartingScore, CreatedPlayer).	

/* *********************************************************************
Function Name: createPlayer
Purpose: Create a new PlayerList
Parameters:
            Id, The int to represent the id (0 for human, 1 for comp)
            StartingCards, List of Cards to be the hand
            StartingPile, The cards in the pile
            StartingReserved, The Reserved Cards,
            Score, The Player's score
Return Value: The formatted list
Algorithm:
            1) Merge the lists together
Assistance Received: none
********************************************************************* */ 
createPlayer(Id, StartingCards, StartingPile, StartingReserved, Score, CreatedPlayer) :-
	mergeLists([Id], [StartingCards], MergeOne),
	mergeLists(MergeOne, [StartingPile],MergeTwo),
	mergeLists(MergeTwo, [StartingReserved], MergeThree),
	mergeLists(MergeThree, [Score], CreatedPlayer).


/* *********************************************************************
Function Name:get(Field of Player)
Purpose: Gets specifed field of player
Parameters:
           The player list
Return Value: The targetted field
Algorithm:
            1) Add all the grades
            2) Divide the sum by the number of students in class to calculate the average
Assistance Received: none
********************************************************************* */ 
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


/* *********************************************************************
Function Name: getPlayerComponents
Purpose: To get all the fields of a player
Parameters:
           The Player List to split up
Return Value: All fields in order they are created
Assistance Received: none
********************************************************************* */ 
getPlayerComponents(PlayerList, Id, Hand, Pile, Reserved, Score) :-
	getId(PlayerList, Id),
	getHand(PlayerList, Hand),
	getPile(PlayerList, Pile),
	getReserved(PlayerList, Reserved),
	getScore(PlayerList, Score).


/* *********************************************************************
Function Name: isHuman
Purpose: Check if the player is for the Human
Parameters:
            The Player list to check
Return Value: Fails if not
Algorithm:
            1) See if Id = 0
Assistance Received: none
********************************************************************* */ 
isHuman(PlayerList) :-
	getId(PlayerList, Id),
	Id = 0.


%Convert Id to name
getPlayerName(PlayerList, Name) :-
	isHuman(PlayerList),
	Name = "Human".

getPlayerName(_, "Computer").


/* *********************************************************************
Function Name: listContainsValue
Purpose: Get the index of an element in a list if it exists
Parameters:
           List to check
           The Value to check
           The index of the current location
Return Value: The found index, -1 if not in list
Algorithm:
            1) If found, store index
Assistance Received: none
********************************************************************* */ 
listContainsValue([], _, _, -1).

listContainsValue([CurrentCard|_], TargetVal, Index, FoundIndex) :-
	getCardVal(CurrentCard, CardVal),
	CardVal = TargetVal,
	FoundIndex = Index.

listContainsValue([_|Rest], TargetVal, Index, FoundIndex) :-
	NextIndex is Index+1,
	listContainsValue(Rest, TargetVal, NextIndex, FoundIndex).

/* *********************************************************************
Function Name: addToPile
Purpose: Adds cards to pile, flattens builds if passed into it
Parameters:
            Cards to add
            The cards in the pile
Return Value: Merged list
Algorithm:
            1) Flatten input
            2) Add to list and reutrn
Assistance Received: none
********************************************************************* */ 
addToPile(NewCards, StartingPile, PileWithAddedCards) :-
	getBuildCards(NewCards, ListOfCards),
	mergeLists(StartingPile, ListOfCards, PileWithAddedCards).

    
%=======================
%Functions for round to call, resolves to human or computer
%=======================


/* *********************************************************************
Function Name: doPlayerMove
Purpose: To resolve which player needs to make the move
Parameters:
            The Player making the move
            The other player not making a move
            The table of cards
            The id of the lasp Cap
Return Value: 
            The First Player after the move
            The other player after the move
            The Table after the move
            Last Cap after the move
Algorithm:
            1) Figure out if human or not.
            2) Get any input
            3)Call appropriate function
Assistance Received: none
********************************************************************* */ 

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



/* *********************************************************************
Function Name: doHumanMove
Purpose: To have the human make a move
Parameters:
            The Player making the move
            The other player not making a move
            The table of cards
            The id of the lasp Cap
Return Value: 
            The First Player after the move
            The other player after the move
            The Table after the move
            Last Cap after the move
Algorithm:
            1) Get input
            2) Pass to move validation
Assistance Received: none
********************************************************************* */ 
%For Capture
doHumanMove(0, PlayerList, OtherPlayer, Table, LastCap, PlayerAfterMove, OtherPlayerAfterMove, TableAfterMove, LastCapAfterMove) :-
	getHand(PlayerList, Hand),
    %Get input for card to play
	writeln("Which card would you like to Capture with?"),
	length(Hand, CardsInHandPlusOne),
	CardsInHand is CardsInHandPlusOne-1,
	getNumericInput(0, CardsInHand, CaptureCardIndex),
    %Get selected cards from table
	length(Table, TableCardAmount),
	TableAllowedIndices is TableCardAmount-1,
	getMultipleNumericInput(TableAllowedIndices, SelectedCardIndices),
    %Issue the capture move
	doCapture(PlayerList, OtherPlayer, Table, LastCap, CaptureCardIndex, SelectedCardIndices, PlayerAfterMove, OtherPlayerAfterMove, TableAfterMove, LastCapAfterMove).  

%Builds
doHumanMove(1, PlayerList, OtherPlayer, Table, LastCap, PlayerAfterMove, OtherPlayerAfterMove, TableAfterMove, LastCapAfterMove) :-
	getHand(PlayerList, Hand),
    %Get the card to build with
	writeln("Which card would you like to Build with?"),
	length(Hand, CardsInHandPlusOne),
	CardsInHand is CardsInHandPlusOne-1,
	getNumericInput(0, CardsInHand, PlayedCardIndex),
    %Get selected cards from the table
	length(Table, TableCardAmount),
	TableAllowedIndices is TableCardAmount-1,
	getMultipleNumericInput(TableAllowedIndices, SelectedCardIndices),
    %Issue the build
	doBuild(PlayerList, OtherPlayer, Table, PlayedCardIndex, SelectedCardIndices, LastCap,PlayerAfterMove, OtherPlayerAfterMove, TableAfterMove, LastCapAfterMove).

%Trail
doHumanMove(2, PlayerList, OtherPlayer, Table, LastCap, PlayerAfterMove, OtherPlayerAfterMove, TableAfterMove, LastCapAfterMove) :-
	getHand(PlayerList, Hand),
    %Get the card to trail
	writeln("Which card would you like to trail?"),
	length(Hand, CardsInHandPlusOne),
	CardsInHand is CardsInHandPlusOne-1,
	getNumericInput(0, CardsInHand, TrailedCardIndex),
    %Issue Trail
	doTrail(PlayerList, OtherPlayer, Table, TrailedCardIndex, LastCap, PlayerAfterMove, OtherPlayerAfterMove, TableAfterMove, LastCapAfterMove).


%=======================
%Functions for computer to make a move
%=======================





/* *********************************************************************
Function Name: doComputerMove
Purpose: To have the computer make a move
Parameters:
            The Player making the move
            The other player not making a move
            The table of cards
            The id of the lasp Cap
            The Action to print in the move
Return Value: 
            The First Player after the move
            The other player after the move
            The Table after the move
            Last Cap after the move
Algorithm:
            1) Check for valid move on priority table (Build, Set Capture, Symbols, Trail)
            2) Issue the move
Assistance Received: none
********************************************************************* */ 
%Build
doComputerMove(PlayerList, OtherPlayer, Table, LastCap, Action, PlayerAfterMove, OtherPlayerAfterMove, TableAfterMove, LastCap) :-
	getHand(PlayerList, Hand),
    %Check if a build is possible
	checkBuild(Hand, Hand, Table, Action, HandAfterMove, TableAfterMove, BuildVal),
    %If it is, update the player with the new reserved
	getPlayerComponents(PlayerList, Id, _, Pile, OldReserved, Score),
	ReservedUnsorted = [BuildVal | OldReserved],
	sort(0, @<, ReservedUnsorted, Reserved),
    OtherPlayer = OtherPlayerAfterMove,
	createPlayer(Id, HandAfterMove, Pile, Reserved, Score, PlayerAfterMove).

%Capture Sets
doComputerMove(PlayerList, OtherPlayer, Table, _, Action, PlayerAfterMove, OtherPlayerAfterMove, TableAfterMove, LastCapAfterMove):-
	getHand(PlayerList, Hand),
    %See if there are sets to capture
	checkSetCapture(Hand, Table, Action, HandAfterMove, TableAfterMove,CapturedCards, PlayedVal),
	length(CapturedCards, AmountCapped),
	AmountCapped > 1,
	getPlayerComponents(PlayerList, Id, _, StartingPile, RawReserved, Score),
    %Add cards to pile and update last cap
	addToPile(CapturedCards, StartingPile, NewPile),
	LastCapAfterMove = Id,

    %Remove the build value from both
    removeVal(RawReserved, PlayedVal, Reserved),
	createPlayer(Id, HandAfterMove, NewPile, Reserved, Score, PlayerAfterMove),
    getPlayerComponents(OtherPlayer, OId, OHand, OPile, ORawReserved, OScore),
    removeVal(ORawReserved, PlayedVal, OReserved),
    createPlayer(OId, OHand, OPile, OReserved, OScore, OtherPlayerAfterMove).

%Capture Identical
doComputerMove(PlayerList, OtherPlayer, Table, LastCap, Action, PlayerAfterMove, OtherPlayerAfterMove, TableAfterMove, LastCapAfterMove):-
	getHand(PlayerList, Hand),
    %Check for identical symbols
	checkForMatchingCaptures(Hand, Table, 0, PlayedCardIndex,MatchedCardsIndices),
	not(MatchedCardsIndices=[]),
    %Get the indicies of selected cards
    getCardsAtIndicies(Hand, [PlayedCardIndex], PlayedCard),
    getCardsAtIndicies(Table, MatchedCardsIndices, CapturedCards),
    displayComputerMove(PlayedCard, CapturedCards, Action, "Capture", "To capture identical symbols"),
    writeln(Action),
	%Issue the capture
	doCapture(PlayerList, OtherPlayer, Table, LastCap,PlayedCardIndex, MatchedCardsIndices, PlayerAfterMove, OtherPlayerAfterMove, TableAfterMove, LastCapAfterMove).

	


%Trail
doComputerMove(PlayerList, OtherPlayer, Table, LastCap, Action, PlayerAfterMove, OtherPlayerAfterMove, TableAfterMove, LastCapAfterMove) :-
	doTrail(PlayerList, OtherPlayer, Table, 0, LastCap, PlayerAfterMove, OtherPlayerAfterMove, TableAfterMove, LastCapAfterMove),
	displayComputerMove(PlayerList, Action, 0).
	


/* *********************************************************************
Function Name: CheckSetCapture
Purpose: To see if there is a set to capture
Parameters:
            The hand to check for cards
            The table
            The action for the move description
Return Value: 
            The Hand after the move
            The table after the move
            The captured cards
            The played val
Algorithm:
            1) Check every card in hand
            2) For a set to capture on the table
Assistance Received: none
********************************************************************* */ 
%Base case
checkSetCapture([], _, [], [],[], _).

checkSetCapture([Card|HandAfterMove], Table, Action, HandAfterMove, TableAfterMove, CapturedCards, PlayedVal) :-
	getCardVal(Card, Target),
    %Check for set for this card
	findSet(Table, Target, TableAfterMove, CapturedCardsFromTable),
	length(CapturedCardsFromTable, AmountCapped),
	AmountCapped > 1,
    %If a set found, accept the move
	CapturedCards = [Card|CapturedCardsFromTable],
    PlayedVal = Target,
    displayComputerMove(Card, CapturedCardsFromTable, Action, "Capture", "Saw an oppertunity to capture a set").

checkSetCapture([Skipped| Rest], Table, Action, HandAfterMove, TableAfterMove, CapturedCards, PlayedVal):-
    %Move to next hand card
	checkSetCapture(Rest, Table, Action, HandAfterCap, TableAfterMove, CapturedCards, PlayedVal),
	HandAfterMove=[Skipped | HandAfterCap].


/* *********************************************************************
Function Name: findSet
Purpose: To find a set on table that sums to target value
Parameters:
            The list of cards to check
            The target to sum to
Return Value: 
            The table after the move
            The Captured cards
Algorithm:
            1) Check if Current - cardVal  is <= to target
            2) If <, add to set
            3) If =, Set found
            4) Else reject
Assistance Received: none
********************************************************************* */ 	
findSet([FirstCard| Rest], Target, TableAfterMove, Captured) :-
	getCardVal(FirstCard, ThisVal),
    %If possibly valid
	NextTarget is Target-ThisVal,
	NextTarget >=0,
    %Try next card
	findSet(Rest, NextTarget, TableAfterMove, ThatCaptured),
	Captured = [FirstCard | ThatCaptured].

%If match, stop recurstion
findSet(Table, 0, Table, []).


findSet([First | Rest], Target, TableAfterMove, Captured) :-
	findSet(Rest, Target, Table, Captured),
	TableAfterMove=[First|Table].



/* *********************************************************************
Function Name: checkBuild
Purpose: To see if a build is possible
Parameters:
            The list to check
            The hand of being played
            The Table of cards
            The Action being done for output
Return Value: 
            The Hand after move
            The Table after move
            The Val of the build
Algorithm:
            1) The card to see if a build can be made for
Assistance Received: none
********************************************************************* */ 
%See if this card can make a build
checkBuild([SumCard | _], Hand, Table, Action, HandAfterMove, TableAfterMove, BuildVal) :-
	checkAsSumCard(SumCard, Hand,Table, Action, HandAfterMove, TableAfterMove, BuildVal).
%Else check next card
checkBuild([_|RestHand], Hand, Table, Action, HandAfterMove, TableAfterMove, BuildVal) :-
	checkBuild(RestHand, Hand, Table, Action, HandAfterMove, TableAfterMove, BuildVal ).


/* *********************************************************************
Function Name: checkAsSumCard
Purpose: To see if any sets of card sum to this card
Parameters:
            The card to sum to
            The Hand of the player
            The Table
            The action for output
Return Value: 
            The hand after the move
            The Table after the move
            The Build val
Algorithm:
            1) Get input
            2) Pass to move validation
Assistance Received: none
********************************************************************* */ 
checkAsSumCard(SumCard, Hand, Table, Action, HandAfterMove, TableAfterMove, BuildVal) :-
	getCardVal(SumCard, SumVal),
    %Find a card to play on the table
	findPlayCard(SumVal, Hand, PlayCard, HandAfterMove),
    %Get the value of the card
	getCardVal(PlayCard, PlayVal),
	TargetVal is SumVal-PlayVal,
    %Check for sets
	findSet(Table, TargetVal, RemainingTableCards, NewBuildCards),
    %If it exists, create a build
	Build = [PlayCard | NewBuildCards],
    displayComputerMove(PlayCard, NewBuildCards, Action, "Build", "Saw an oppertunity to make a build"),
	%TableAfterAdding = [Build | RemainingTableCards],
	checkForMulti(Build, SumVal, RemainingTableCards, TableAfterMove),
	BuildVal = SumVal.

/* *********************************************************************
Function Name: findPlayCard
Purpose: To find the card to play which is less than the sum card
Parameters:
            The Target Sum
            The cards to check
Return Value: 
           The card that can be played
           The hand without the card
Algorithm:
            1) Check each card in the hand for one that is less than the sum
Assistance Received: none
********************************************************************* */ 
findPlayCard(SumVal, [First | Rest], First, Rest) :-
	getCardVal(First, FirstVal),
	FirstVal < SumVal.

findPlayCard(SumVal, [Skipped | Rest], SumCard, HandWithoutPlay) :-
	findPlayCard(SumVal, Rest, SumCard, RestHand),
	HandWithoutPlay=[Skipped | RestHand].


/* *********************************************************************
Function Name: checkForMatchingCapture
Purpose: To find identical symbols to capture
Parameters:
            The hand of the player
            The table of cards
            The current index
Return Value: 
            The Card with matches
            The Cards that match the above card
Algorithm:
            1) For every card
            2) Check if any other card after it has the same value
Assistance Received: none
********************************************************************* */ 
checkForMatchingCaptures([], _, _, [], []).

checkForMatchingCaptures([CurrentCard | _], Table, Index, CardWithMatches, MatchedCards):-
	getCardVal(CurrentCard, CardVal),
	findMatchingValsIndices(Table, CardVal, 0, MatchingCards),
	length(MatchingCards, CardsThatMatched),
    %If any cards matches
	CardsThatMatched > 0,
	CardWithMatches = Index,
	MatchedCards = MatchingCards.

%If it does not match, next index
checkForMatchingCaptures([_ | RestHand], Table, Index, CardWithMatches, MatchedCards) :-
	NextIndex is Index+1,
	checkForMatchingCaptures(RestHand, Table, NextIndex, CardWithMatches, MatchedCards).


%=======================
%Functions to validate and execute the choosen move (for both players)
%=======================


/* *********************************************************************
Function Name: doCapture
Purpose: To have a capture issued
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

doCapture(PlayerList, OtherPlayer, Table, _, PlayedCardIndex, SelectedCardIndices, PlayerAfterMove, OtherPlayerAfterMove, TableAfterMove, LastCapAfterMove) :-
	%Get the hand and ensure the Played index is instaniated
	getHand(PlayerList, Hand),
	integer(PlayedCardIndex),
	%Remove played card from the hand and get its value
	removeAtIndex(Hand, PlayedCardIndex, ResultingHand, CaptureCard),
	getCardVal(CaptureCard, CaptureVal),
	%Sum all selected cards to make sure the capture is valid
    validateCaptureSum(SelectedCardIndices, Table, CaptureVal),
	%Remove the cards
	removeAllIndices(SelectedCardIndices, Table, TableAfterMove, CaputuredCards),
	%Ensure atleast one card is selected
	length(CaputuredCards, CapturedAmounts),
	CapturedAmounts > 0,
	%Ensure all required cards are taken
	getCardSymbol(CaptureCard, CaptureSym),
	removeMatchingSymbols(TableAfterMove, CaptureSym, _, Matching),
	length(Matching, MatchingSize),
	MatchingSize = 0,
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
%If Human, reprompt
doCapture(PlayerList, OtherPlayer, Table, LastCap, _, _, PlayerAfterMove, OtherPlayerAfterMove, TableAfterMove, LastCapAfterMove) :-
    isHuman(PlayerList),
    writeln("Invalid Capture Selection, try again"),
    doPlayerMove(PlayerList, OtherPlayer, Table, LastCap, PlayerAfterMove, OtherPlayerAfterMove, TableAfterMove, LastCapAfterMove).

/* *********************************************************************
Function Name: doBuild
Purpose: To have a capture issued
Parameters:
            The Player making the move
            The other player not making a move
            The table of cards
            The index of the played card
            The list of selected indices
            The ID of the last cap
Return Value: 
            The First Player after the move
            The other player after the move
            The Table after the move
            Last Cap 
Algorithm:
            1) Check if selected cards are valid
            2) Check if the build sum is valid
            3) If it is, do the move, otherwise reprompt
Assistance Received: none
********************************************************************* */

doBuild(PlayerList, OtherPlayer, Table, PlayedCardIndex, SelectedCardIndices, LastCap, PlayerAfterMove, OtherPlayerAfterMove, TableAfterMove, LastCap) :-	
	getHand(PlayerList, Hand),
	integer(PlayedCardIndex),
	%Remove played card from the hand and get its value
	removeAtIndex(Hand, PlayedCardIndex, ResultingHand, PlayedCard),
	getCardVal(PlayedCard, PlayedVal),
    %Make sure sum of build is valid
	sumSelectedCards(SelectedCardIndices, Table, Sum),
	BuildValue is Sum+PlayedVal,
	BuildValue =< 14,
    %Make sure player contains the card
	listContainsValue(ResultingHand, BuildValue, 0, ReserveCardIndex),
	ReserveCardIndex >=0,
    %Added to reserved
	getReserved(PlayerList, StartingReserved),
	NewReservedUnsorted = [BuildValue | StartingReserved],
	sort(0 ,@<, NewReservedUnsorted, NewReserved),
    %Remove the selected cards
	removeAllIndices(SelectedCardIndices, Table, TableAfterCardsRemoved, TableBuildCards),
	makeBuild(TableBuildCards, PlayedCard, NewBuild),
	%Combine into a multi build if value matches
	checkForMulti(NewBuild, BuildValue, TableAfterCardsRemoved, TableAfterMove),
    %Update Player
	getPlayerComponents(PlayerList, Id, _, Pile, _, Score),
	createPlayer(Id, ResultingHand, Pile, NewReserved, Score, PlayerAfterMove),
    getPlayerComponents(OtherPlayer, OId, OHand, OPile, ORawReserved, OScore),
    removeVal(ORawReserved, Sum, OReserved),
    createPlayer(OId, OHand, OPile, OReserved, OScore, OtherPlayerAfterMove).
%Reprompt if human
doBuild(PlayerList, OtherPlayer, Table, _, _, LastCap, PlayerAfterMove, OtherPlayerAfterMove, TableAfterMove, LastCapAfterMove) :-
    isHuman(PlayerList),
    writeln("Invalid Build Selection, try again"),
    doPlayerMove(PlayerList, OtherPlayer, Table, LastCap, PlayerAfterMove, OtherPlayerAfterMove, TableAfterMove, LastCapAfterMove).


/* *********************************************************************
Function Name: doTrail
Purpose: To have a trail issued
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
            1) Check if  no reserved values
Assistance Received: none
********************************************************************* */
doTrail(PlayerList, OtherPlayer, Table, PlayedCardIndex, LastCap, PlayerAfterMove, OtherPlayer, TableAfterMove, LastCap) :-
    %Make sure no builds on table
    getReserved(PlayerList, Reserved),
    length(Reserved, ReservedSize),
    ReservedSize=0,
    %Remove the card from hand
	getHand(PlayerList, Hand),
	integer(PlayedCardIndex),
	removeAtIndex(Hand, PlayedCardIndex, ResultingHand, TrailedCard),
    %Add to table
	mergeLists(Table, [TrailedCard], TableAfterMove),
    %Update Player
	getId(PlayerList, Id),
	getPile(PlayerList, Pile),
	getReserved(PlayerList, Reserved),
	getScore(PlayerList, Score),
	createPlayer(Id, ResultingHand, Pile, Reserved, Score,PlayerAfterMove).

doTrail(PlayerList, OtherPlayer, Table, _, LastCap, PlayerAfterMove, OtherPlayerAfterMove, TableAfterMove, LastCapAfterMove) :-
    isHuman(PlayerList),
    writeln("Invalid Trail, try again"),
    doPlayerMove(PlayerList, OtherPlayer, Table, LastCap, PlayerAfterMove, OtherPlayerAfterMove, TableAfterMove, LastCapAfterMove).

/* *********************************************************************
Function Name: validateCaptureSum
Purpose: To see if a capture is valid
Parameters:
            The selected cards indices
            The table of cards
            The val being played
Return Value: 
        Fails if false
Algorithm:
            1) Check if sum % played val == 0
Assistance Received: Same algorithm Andrew Wild and I had in Java/Android
********************************************************************* */
%If Ace low, check Ace High
validateCaptureSum(SelectedCardIndices, Table, 1) :-
	sumSelectedCards(SelectedCardIndices, Table, Sum),
	ModResult is mod(Sum, 14),
	ModResult = 0.

validateCaptureSum(SelectedCardIndices, Table, CaptureVal) :-
    sumSelectedCards(SelectedCardIndices, Table, Sum),
	ModResult is mod(Sum, CaptureVal),
	ModResult = 0.

validateCaptureSum(SelectedCardIndices, Table, 14) :-
    sumSelectedCards(SelectedCardIndices, Table, Sum),
    length(SelectedCardIndices, SelectedAmount),
    Sum = SelectedAmount.


/* *********************************************************************
Function Name: checkForMulti
Purpose: To combine builds of the same value into a multi build
Parameters:
            The new build
            Its Value
            The Table
Return Value: 
            The Table after the function runs
Algorithm:
            1) Look at every card
            2) If build and same value, combine.
Assistance Received: none
********************************************************************* */
checkForMulti(NewBuild, _, [], [NewBuild]).

checkForMulti(NewBuild, BuildValue, [Current | RestCard],  TableAfterMove) :-
	sumCardList(Current, CurrentVal),
	BuildValue = CurrentVal,
	NewMulti = [NewBuild | [Current]],
	TableAfterMove = [NewMulti | RestCard].

checkForMulti(NewBuild, BuildValue, [Current | RestCard],  TableAfterMove) :-
	checkForMulti(NewBuild, BuildValue, RestCard, RestTable),
	TableAfterMove = [Current | RestTable].
