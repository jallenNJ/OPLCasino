%Get all numbers from 0 to Num
listAll(Num, []) :- 
	Num<0.

listAll(Num, Result):- 
	NewNum is Num-1, 
	listAll(NewNum, NewResult),
	Result=[Num|NewResult].
	
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

%Displays card to the screen 
displayCard([Suit|Card]) :- write(Suit), write(Card).

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
	replace(JAdded, 1, "A", Output).

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



drawFourCards(InputDeck, OutputDeck, DrawnCards):-
	nth0(0, InputDeck, FirstCard),
	nth0(1, InputDeck, SecondCard),
	nth0(2, InputDeck, ThridCard),
	nth0(3, InputDeck, FourthCard),
	removeNCards(4, InputDeck, OutputDeck),
	mergeLists([FirstCard], [SecondCard], TwoMerged),
	mergeLists(TwoMerged, [ThridCard], ThreeMerged),
	mergeLists(ThreeMerged,[FourthCard], DrawnCards).

removeNCards(Amount, Rem, Out) :- Amount =< 0,
									Out = Rem.

removeNCards(Amount, InputList, Output) :-
	NewAmount is Amount-1,
	InputList = [_ | Rest],
	removeNCards(NewAmount, Rest, NewOutput),
	Output = NewOutput.


%id, deck, table, p0Info, p1Info, retVal
%playRound(FirstId,[],[],[],[],_):- 
	%Call is params
%		createDeck(RawDeck).


%Round end rule, deck is empty, player infos have null hands
%playRound(_,[])	

%Main loop
%playRound(FirstId, Deck, Table, P0Info, P1Info, _) :-

test() :-
	createDeck(Deck),
	drawFourCards(Deck, NewDeck, DrawnCards),
	writeln(Deck),
	writeln("-----"),
	writeln(NewDeck),
	writeln("-----"),
	writeln(DrawnCards).