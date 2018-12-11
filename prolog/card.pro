
%=======================
%Functions to generate internal data
%=======================

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

	
%Get all Suits	
getSuits(Val, []) :-
		Val >=4.
		
getSuits(Val, Result) :-
	NewVal is Val+1, 
	getSuits(NewVal, NewResult),
	numToSuit(Val, Suit),
	Result=[Suit|NewResult].
	
	
%Suit to nums	
numToSuit(0, Output) :- Output = "s".	
numToSuit(1, Output) :- Output = "c".	
numToSuit(2, Output) :- Output = "d".	
numToSuit(3, Output) :- Output = "h".	

%Creates card in form [SUIT SYMBOL]
createCard(Suit, Val, [Suit | Val]).


getSuit([Suit|_], Suit).

isBuild([FirstCard|Rest]) :-
	FirstCard = [Suit | Symbol],
	not(Suit=[]),
	not(Symbol=[]),
	not(Rest=[]).
%Displays card to the screen 

%End Build Recursion
displayBuild([]).

displayBuild(Build) :-
	Build = [BuildCard | RestOfBuild],
	displayCard(BuildCard),
	write(" "),
	displayBuild(RestOfBuild).

%If inputted card is a build, display every card enclosed in []
displayCard(Build) :- 
	
	isBuild(Build),
	write("[ "),
	displayBuild(Build),
	write("]").

displayCard([Card | []]) :-
	displayCard(Card).

%Display a single card
displayCard([Suit|Card]) :-
	write(Suit), 
	write(Card).	




getBuildCards(Build, CardOutput) :-
	flatten(Build, RawAtoms),
	rebuildAtomListToCards(RawAtoms, CardOutput).
	


%getBuildCards(Build, Build).

rebuildAtomListToCards([],[]).
rebuildAtomListToCards(AtomList, RebuiltCards) :-
	AtomList = [Suit | Rest],
	Rest = [Sym | RestAtoms],

	rebuildAtomListToCards(RestAtoms, RestCards),
	createCard(Suit, Sym, ThisCard),
	RebuiltCards=[ThisCard | RestCards].


getCardSymbol(Build, Sym) :-
	isBuild(Build),
	sumCardList(Build, Val),
	allNumsToFaceLetters([Val], Sym).

getCardSymbol([_|Sym], Sym).



getCardVal(MultiBuild , Val):-
	nth0(0, MultiBuild, Build1),
	isBuild(Build1),
	nth0(1, MultiBuild, Build2),
	isBuild(Build2),
	getCardVal(Build1, Val).

getCardVal(Build, Val):-
	isBuild(Build),
	sumCardList(Build, Val).

getCardVal(Card, Val) :-
	getCardSymbol(Card, Sym),
	convertSymbolToVal(Sym, Val).

convertSymbolToVal("K", 13).
convertSymbolToVal("Q", 12).
convertSymbolToVal("J", 11).
convertSymbolToVal("X", 10).
convertSymbolToVal("A", 1).
convertSymbolToVal(Val, Val) :-
	integer(Val).	

%Creates all cards for a suit, from K to A in a list
createCardsForSuit(_, [_|CurrentVal], []) :-	CurrentVal = [].	

createCardsForSuit(Suit, [Current|Rest], SuitCards) :- 
	createCard(Suit, Current, CreatedCard), 
	%displayCard(CreatedCard), 
	createCardsForSuit(Suit, Rest, NewSuitCards), 	
	SuitCards = [CreatedCard | NewSuitCards].
	
%Megers list in order [List one List two]
mergeLists(ListOne, ListTwo, Result) :- append(ListOne, ListTwo, Result).

%Converts all numeric values to symbol represntation
allNumsToFaceLetters(Input, Output) :-
	replace(Input, 13, "K", KAdded),
	replace(KAdded, 12, "Q", QAdded),
	replace(QAdded, 11, "J", JAdded),
	replace(JAdded, 10, "X", XAdded),
	replace(XAdded, 1, "A", Output).

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


%Replaces first occurance of key with value		
replace([], _, _, []).	
replace([Key | Rest], Key, Value, [Value | Rest]).
replace([First|Rest], Key, Value, [First | NewRest]) :- replace(Rest, Key,Value,NewRest).	

%removeAtIndex(InputList, Index, ResultingList) :-
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

%base case, remaining list is what is the resulting list
removeAllIndices([], RemaingCards, RemaingCards, []).
%Recursive Case
removeAllIndices([CurrentIndex | Rest], InputList, ResultingList, RemovedElements) :-
	removeAtIndex(InputList, CurrentIndex, RemainingCards, RemovedCard),
	removeAllIndices(Rest, RemainingCards, ResultingList, RestRemovedCards),
	RemovedElements = [RemovedCard | RestRemovedCards].

drawFourCards(InputDeck, OutputDeck, DrawnCards):-
	nth0(0, InputDeck, FirstCard),
	nth0(1, InputDeck, SecondCard),
	nth0(2, InputDeck, ThridCard),
	nth0(3, InputDeck, FourthCard),
	removeNCards(4, InputDeck, OutputDeck),
	mergeLists([FirstCard], [SecondCard], TwoMerged),
	mergeLists(TwoMerged, [ThridCard], ThreeMerged),
	mergeLists(ThreeMerged,[FourthCard], DrawnCards).

removeNCards(Amount, Rem, Out) :- 
	Amount =< 0,
	Out = Rem.

removeNCards(Amount, InputList, Output) :-
	NewAmount is Amount-1,
	InputList = [_ | Rest],
	removeNCards(NewAmount, Rest, NewOutput),
	Output = NewOutput.

removeMatchingSymbols([], _, [],[]).

removeMatchingSymbols([Currentcard | Rest], Value, RemainingCards, RemovedCards) :-
	getCardSymbol(Currentcard, CurrentcardVal),
	CurrentcardVal = Value,
	removeMatchingSymbols(Rest, Value, RemainingCards, NewRemoved),
	RemovedCards = [Currentcard | NewRemoved].

removeMatchingSymbols([Currentcard | Rest], Value, RemainingCards, RemovedCards) :-
	removeMatchingSymbols(Rest, Value, NewRemainingCards, RemovedCards),
	RemainingCards = [Currentcard | NewRemainingCards].


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



sumSelectedCards([], _, 0).


sumSelectedCards([CurrentIndex | RestIndicies], Table, Sum) :-
	nth0(CurrentIndex, Table, SelectedCard),
	sumSelectedCards(RestIndicies, Table, RemainingSum),
	getCardVal(SelectedCard, Val),
	Sum is RemainingSum+Val.


sumCardList([], 0).
sumCardList([Current | Rest], Sum) :-
	getCardVal(Current, ThisVal),
	sumCardList(Rest, RestVal),
	Sum is ThisVal+RestVal.
	

makeBuild([], BuildCard, [BuildCard]).

makeBuild([CurrentCard | Rest], BuildCard, Build) :-
	makeBuild(Rest, BuildCard, NewBuild),
	Build = [CurrentCard | NewBuild].


getCardsAtIndicies(_, [], []).

getCardsAtIndicies(CardList, [First|Rest], Result) :-
	getCardsAtIndicies(CardList, Rest, NewResult),
	nth0(First, CardList, Current),
	Result = [Current|NewResult].