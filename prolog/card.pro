
%=======================
%Functions to generate internal data
%=======================

/* *********************************************************************
Function Name: listall
Purpose: Toget a list in descending order from N to 0
Parameters:
	Num -- The highest number in the list

Return Value: 
	Result, The list of nums from N to 1
Algorithm:
            1) Recursivly call with Num-1
			2) Prepend Current Num to returned list
Assistance Received: Given in Class by Professor
********************************************************************* */ 


%Get all numbers from 0 to Num
listAll(Num, []) :- 
	Num<0.

listAll(Num, Result):- 
	NewNum is Num-1, 
	listAll(NewNum, NewResult),
	Result=[Num|NewResult].

%=======================
%Functions to create and manipulate cards
%=======================

/* *********************************************************************
Function Name: getSuits
Purpose: To Generate all suits (C,H,D,S)
Parameters:
     Val, Start at 0 for all Suits
Return Value: 
	A list of the suits
Algorithm:
            1) Generate a list of 0,1,2,3
			2) Map to the symbol
Assistance Received: none
********************************************************************* */ 

%Get all Suits	
getSuits(Val, []) :-
		Val >=4.
		
getSuits(Val, Result) :-
	NewVal is Val+1, 
	getSuits(NewVal, NewResult),
	numToSuit(Val, Suit),
	Result=[Suit|NewResult].
	
	
/* *********************************************************************
Function Name: numToSuit
Purpose: To Map a num to the suit
Parameters:
            Num being mapped
Return Value: The symbol it maps to

Algorithm:
            1) A rule
Assistance Received: none
********************************************************************* */ 	
%Suit to nums	
numToSuit(0, Output) :- Output = "s".	
numToSuit(1, Output) :- Output = "c".	
numToSuit(2, Output) :- Output = "d".	
numToSuit(3, Output) :- Output = "h".	



/* *********************************************************************
Function Name: createCard
Purpose: Wrapper function to make a card
Parameters:
            Suit -- The Card Suit
			Symbol -- The Card Symbol
Return Value: The card as a list [SUIT | Symbol]
Algorithm:
            1) Format list
Assistance Received: none
********************************************************************* */ 
%Creates card in form [SUIT SYMBOL]
createCard(Suit, Val, [Suit | Val]).


getSuit([Suit|_], Suit).




/* *********************************************************************
Function Name: isBuild
Purpose: To check if a card list is a build
Parameters:
            Card to check
Return Value: 
	None
	Fails if false

Algorithm:
            1) Split the list
			2) Make sure both are not null
Assistance Received: none
********************************************************************* */ 
isBuild([FirstCard|Rest]) :-
	FirstCard = [Suit | Symbol],
	%Make sure none are empty
	not(Suit=[]),
	not(Symbol=[]),
	not(Rest=[]).
%Displays card to the screen 




/* *********************************************************************
Function Name: displayBuild
Purpose: Helper functions to display a card when its a build
Parameters:
            The build to display 
Algorithm:
            1) Print build
			2) Recursivily call for next card
Assistance Received: none
********************************************************************* */ 
%End Build Recursion
displayBuild([]).

displayBuild(Build) :-
	Build = [BuildCard | RestOfBuild],
	displayCard(BuildCard),
	write(" "),
	displayBuild(RestOfBuild).


/* *********************************************************************
Function Name: displayCard
Purpose: To display a card or build
Parameters:
            card to be printed out
Algorithm:
            1) Print parameters
Assistance Received: none
********************************************************************* */ 


%If inputted card is a build, display every card enclosed in []
displayCard(Build) :- 
	
	isBuild(Build),
	write("[ "),
	displayBuild(Build),
	write("]").

%Display a single card nested in a list
displayCard([Card | []]) :-
	displayCard(Card).

%Display a single card
displayCard([Suit|Card]) :-
	write(Suit), 
	write(Card).	



/* *********************************************************************
Function Name: getBuildCards
Purpose: To flatten a build to a list of cards
Parameters:
            Build, The build to flatten
Return Value: A list of cards
Algorithm:
            1) Flatten to atom
			2) Rebuild into cards
Assistance Received: none
********************************************************************* */ 

getBuildCards(Build, CardOutput) :-
	flatten(Build, RawAtoms),
	rebuildAtomListToCards(RawAtoms, CardOutput).
	


/* *********************************************************************
Function Name: rebuildAtomListToCards
Purpose: To recombine a list of atoms into a list of cards
Parameters:
            The list of atoms
Return Value: The average grade in the class, a real value
Local Variables:
            The list of cards built from the list
Algorithm:
            1) Combine the 0th and 1st atom into a card,
			2) Recursively call the next list
Assistance Received: none
********************************************************************* */ 

rebuildAtomListToCards([],[]).
rebuildAtomListToCards(AtomList, RebuiltCards) :-
	AtomList = [Suit | Rest],
	Rest = [Sym | RestAtoms],

	rebuildAtomListToCards(RestAtoms, RestCards),
	createCard(Suit, Sym, ThisCard),
	RebuiltCards=[ThisCard | RestCards].

/* *********************************************************************
Function Name: getCardSymbol
Purpose: To get the symbol of a card or build
Parameters:
           The card to get the symbol of
Return Value: The symbol of the card
Algorithm:
            1) Get value
            2) If not a symbol, convert to num
Assistance Received: none
********************************************************************* */ 
getCardSymbol(Build, Sym) :-
	isBuild(Build),
	sumCardList(Build, Val),
	allNumsToFaceLetters([Val], Sym).

getCardSymbol([_|Sym], Sym).

/* *********************************************************************
Function Name: getCardVal
Purpose: To  get the value of a card list
Parameters:
           Card to get the value of
Return Value: The value of the card
Algorithm:
            1) Sum based on tyupe of card
Assistance Received: none
********************************************************************* */ 

%If a multi build, sum only the first one
getCardVal(MultiBuild , Val):-
	nth0(0, MultiBuild, Build1),
	isBuild(Build1),
	nth0(1, MultiBuild, Build2),
	isBuild(Build2),
	getCardVal(Build1, Val).

%If a build, sum the entire list
getCardVal(Build, Val):-
	isBuild(Build),
	sumCardList(Build, Val).
%Otherwise, get the symbol and convert to the val
getCardVal(Card, Val) :-
	getCardSymbol(Card, Sym),
	convertSymbolToVal(Sym, Val).


%Rules to map symbols to num
convertSymbolToVal("K", 13).
convertSymbolToVal("Q", 12).
convertSymbolToVal("J", 11).
convertSymbolToVal("X", 10).
convertSymbolToVal("A", 1).
%Make sure val is a number
convertSymbolToVal(Val, Val) :-
	integer(Val).	


/* *********************************************************************
Function Name: createCardsForSuit
Purpose: To generate all 13 cards in a suit
Parameters:
           The Suit to generate
		   The values to make cards for (Use listall to generate)
Return Value: The list of generated cards
Algorithm:
            1) Create a card for reach value
Assistance Received: none
********************************************************************* */ 
%Creates all cards for a suit, from K to A in a list
createCardsForSuit(_, [_|CurrentVal], []) :-	CurrentVal = [].	

createCardsForSuit(Suit, [Current|Rest], SuitCards) :- 
	createCard(Suit, Current, CreatedCard), 
	%displayCard(CreatedCard), 
	createCardsForSuit(Suit, Rest, NewSuitCards), 	
	SuitCards = [CreatedCard | NewSuitCards].
	
/* *********************************************************************
Function Name: mergeLists
Purpose: Wrapper for append... which doesn't need to exist anymore
Parameters:
            The lists to merge together
Return Value: The merged lists
Algorithm:
            1) Call append
Assistance Received: none
********************************************************************* */ 	
%Merges list in order [List one List two]
mergeLists(ListOne, ListTwo, Result) :- append(ListOne, ListTwo, Result).


/* *********************************************************************
Function Name: allNumsToFaceLetters
Purpose: To replace the numeric representations with the face letters
Parameters:
           The list of all numbers
Return Value: The substituted list
Algorithm:
            1) For each value, replace with the correct val
Assistance Received: none
********************************************************************* */ 
allNumsToFaceLetters(Input, Output) :-
	replace(Input, 13, "K", KAdded),
	replace(KAdded, 12, "Q", QAdded),
	replace(QAdded, 11, "J", JAdded),
	replace(JAdded, 10, "X", XAdded),
	replace(XAdded, 1, "A", Output).


/* *********************************************************************
Function Name: createDeck
Purpose: To create a deck of 52 cards and shuffle it
Return Value: The shuffled deck
Algorithm:
            1) Generate 13 to 1
			2) Swap with face values
			3) Make cards for each suit
			4) Merge together
			5) Shuffle
Assistance Received: none
********************************************************************* */ 
createDeck(Result) :-
		listAll(13, RawNums),
		allNumsToFaceLetters(RawNums, Nums),
		createCardsForSuit("S", Nums, Spades),
		createCardsForSuit("C", Nums, Clubs),
		createCardsForSuit("D", Nums, Diamonds),
		createCardsForSuit("H", Nums, Hearts),
		mergeLists(Spades, Clubs, MergeOne),
		mergeLists(MergeOne, Diamonds, MergeTwo),
		mergeLists(MergeTwo, Hearts, RawDeck),
		random_permutation(RawDeck, Result).


/* *********************************************************************
Function Name: replace
Purpose: Replace first occurance of key with value
Parameters:
            List to check
			Key to find
			Value to replace it with
Return Value: List with subsitutions
Algorithm:
            1) Given in class
Assistance Received: Given in class
********************************************************************* */ 
replace([], _, _, []).	
replace([Key | Rest], Key, Value, [Value | Rest]).
replace([First|Rest], Key, Value, [First | NewRest]) :- replace(Rest, Key,Value,NewRest).	


/* *********************************************************************
Function Name: removeAtIndex
Purpose: To remove element at ith location and return it
Parameters:
           List to remove from
		   The index to remove at
Return Value: The list after it was removed, and the element removed
Algorithm:
            1) Go the the ith location
			2) Remove element
Assistance Received: none
********************************************************************* */ 
% End of list before finding target index
removeAtIndex([], _, [], _).
%Current element is the one to be removed, end recursion
removeAtIndex([Current | Rest], 0, ResultingList, RemovedElement) :-	
	ResultingList = Rest,
	RemovedElement = Current.

%Recursive case
removeAtIndex([Current | Rest], Index, ResultingList, RemovedElement) :-
	Index > 0,
	NewIndex is Index-1,
	removeAtIndex(Rest, NewIndex, NewResultingList, RemovedElement),
	ResultingList = [Current | NewResultingList].



/* *********************************************************************
Function Name: removeAllIndices
Purpose: To call removeAtIndex on a list of indicies
Parameters:
            List of indices to remove
			List of cards
Return Value: 
		The cards left,
		The removed cards 
Algorithm:
            1) For each index, call remove at index
Assistance Received: none
********************************************************************* */ 
%base case, remaining list is what is the resulting list
removeAllIndices([], RemaingCards, RemaingCards, []).
%Recursive Case
removeAllIndices([CurrentIndex | Rest], InputList, ResultingList, RemovedElements) :-
	removeAtIndex(InputList, CurrentIndex, RemainingCards, RemovedCard),
	removeAllIndices(Rest, RemainingCards, ResultingList, RestRemovedCards),
	RemovedElements = [RemovedCard | RestRemovedCards].

/* *********************************************************************
Function Name: drawFourCards
Purpose: Draw four cards from the deck
Parameters:
           The deck to draw cards from
Return Value: 
	The deck after the cards removed
	The Cards removed
Algorithm:
            1) Get the four cards
			2) Remove those cards from the deck
			3) Merge the cards togeth34
Assistance Received: none
********************************************************************* */ 
drawFourCards(InputDeck, OutputDeck, DrawnCards):-
	nth0(0, InputDeck, FirstCard),
	nth0(1, InputDeck, SecondCard),
	nth0(2, InputDeck, ThridCard),
	nth0(3, InputDeck, FourthCard),
	removeNCards(4, InputDeck, OutputDeck),
	mergeLists([FirstCard], [SecondCard], TwoMerged),
	mergeLists(TwoMerged, [ThridCard], ThreeMerged),
	mergeLists(ThreeMerged,[FourthCard], DrawnCards).

/* *********************************************************************
Function Name: removeNCards
Purpose: Remove the first N Cards from the list
Parameters:
            The Amount of cards to remove
			The list to remove from
Return Value: The List with the elements removed
Algorithm:
            1) Go through list
			2) Dropping N cards until N is zero
Assistance Received: none
********************************************************************* */ 
removeNCards(Amount, Rem, Out) :- 
	Amount =< 0,
	Out = Rem.

removeNCards(Amount, InputList, Output) :-
	NewAmount is Amount-1,
	InputList = [_ | Rest],
	removeNCards(NewAmount, Rest, NewOutput),
	Output = NewOutput.

/* *********************************************************************
Function Name: removeMatchingSymbols
Purpose: To Remove matching symbols from list
Parameters:
           The list of cards,
		   The value to remove
Return Value: The remaining cards
	The removed cards
Algorithm:
            1) Go through list
			2) If matching, add to removed
			3) Otherwise, add to remaining
Assistance Received: none
********************************************************************* */ 
removeMatchingSymbols([], _, [],[]).

removeMatchingSymbols([Currentcard | Rest], Value, RemainingCards, RemovedCards) :-
	getCardSymbol(Currentcard, CurrentcardVal),
	CurrentcardVal = Value,
	removeMatchingSymbols(Rest, Value, RemainingCards, NewRemoved),
	RemovedCards = [Currentcard | NewRemoved].

removeMatchingSymbols([Currentcard | Rest], Value, RemainingCards, RemovedCards) :-
	removeMatchingSymbols(Rest, Value, NewRemainingCards, RemovedCards),
	RemainingCards = [Currentcard | NewRemainingCards].


/* *********************************************************************
Function Name: findIndicesOfMatchingVals
Purpose: To get the indicies of anything that matches
Parameters:
            List of cards,
			Value to match,
			Index to search at
Return Value: The list of indices
Algorithm:
            1) If value matches, add to matching index lists
Assistance Received: none
********************************************************************* */ 
findMatchingValsIndices([], _, _,[]).

findMatchingValsIndices([Currentcard | Rest], Value, Index, Indices) :-
	NextIndex is Index+1,
	getCardVal(Currentcard, CurrentcardVal),
	CurrentcardVal = Value,
	findMatchingValsIndices(Rest, Value, NextIndex,  NewIndices),
	Indices = [Index | NewIndices].

findMatchingValsIndices([ _ | Rest], Value, Index, Indices) :-
	NextIndex is Index+1,
	findMatchingValsIndices(Rest, Value, NextIndex, Indices).



/* *********************************************************************
Function Name: sumSelectedCards
Purpose: To sum selected cards (by index) in the card list
Parameters:
            List of indices
			The list to select from
Return Value: The sum of the cards
Algorithm:
            1) Get card at index
			2) Add value
			3)Repeate until list is emty
Assistance Received: none
********************************************************************* */ 
sumSelectedCards([], _, 0).


sumSelectedCards([CurrentIndex | RestIndicies], Table, Sum) :-
	nth0(CurrentIndex, Table, SelectedCard),
	sumSelectedCards(RestIndicies, Table, RemainingSum),
	getCardVal(SelectedCard, Val),
	Sum is RemainingSum+Val.

/* *********************************************************************
Function Name: sumCardList
Purpose: The card list to sum
Parameters:
           The list of cards
Return Value: The sum of the list
Algorithm:
            1) Get the card
			2) Get the val
			3) Increment the pending sum
Assistance Received: none
********************************************************************* */ 
sumCardList([], 0).
sumCardList([Current | Rest], Sum) :-
	getCardVal(Current, ThisVal),
	sumCardList(Rest, RestVal),
	Sum is ThisVal+RestVal.
	
/* *********************************************************************
Function Name: makeBuild
Purpose: To format a (multi) build from a list of cards
Parameters:
            The list to add
			The card played
Return Value: The formatted build
Algorithm:
            1) Add cards one by one into build
Assistance Received: none
********************************************************************* */ 
makeBuild([], BuildCard, [BuildCard]).

makeBuild([CurrentCard | Rest], BuildCard, Build) :-
	makeBuild(Rest, BuildCard, NewBuild),
	Build = [CurrentCard | NewBuild].


/* *********************************************************************
Function Name: getCardsAtIndicies
Purpose: To get the cards at the indicies
Parameters:
            The list of cards
			The list of indices
Return Value: The list of cards of the indides
Algorithm:
            1) For each index, get the card there and append to the list
Assistance Received: none
********************************************************************* */ 
getCardsAtIndicies(_, [], []).

getCardsAtIndicies(CardList, [First|Rest], Result) :-
	getCardsAtIndicies(CardList, Rest, NewResult),
	nth0(First, CardList, Current),
	Result = [Current|NewResult].