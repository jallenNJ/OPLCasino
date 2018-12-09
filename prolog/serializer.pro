
%=======================
%Functions to load in serliazed data
%=======================	

load(LastCap, RoundScores) :-
	prompt1("What file would you like to load?"),
	read(File),
	readFile(File, Data),
	parseSaveToRound(Data, LastCap, RoundScores).

readFile(FileName, FileData) :-
	open(FileName, read, FileStream),
	read(FileStream, FileData).

parseSaveToRound(RawData, LastCapFromRound, RoundScores) :-

	nth0(1, RawData, CompScore),
	nth0(2, RawData, CompHand),

	nth0(3, RawData, CompPile),

	nth0(4, RawData, HumanScore),
	nth0(5, RawData, HumanHand),
	nth0(6, RawData,HumanPile),

	nth0(7, RawData, Table),

	length(RawData, Terms),
	LastCapIndex is Terms-3,
	DeckIndex is Terms-2,
	StartingIndex is Terms-1,
	nth0(DeckIndex, RawData, Deck),
	nth0(LastCapIndex, RawData, LastCapAtom),
	nth0(StartingIndex, RawData, StartingPlayerAtom),
	enumerateAtomId(StartingPlayerAtom, StartingPlayer),
	enumerateAtomId(LastCapAtom, LastCap),

	parseBuildOwnersFromRaw(RawData, Terms, HumanReserved, CompReserved),


	createPlayer(0, HumanHand, HumanPile, HumanReserved, HumanScore, Human),
	createPlayer(1, CompHand, CompPile, CompReserved, CompScore, Comp),


	startRoundFromLoad(StartingPlayer, Deck, Table, Human, Comp, LastCap, LastCapFromRound, RoundScores).


startRoundFromLoad(StartingPlayer, Deck, Table, Human, Comp, LastCap, LastCapFromRound, RoundScores) :-
	StartingPlayer = 0,
	playRound(StartingPlayer, Deck, Table, Human, Comp, LastCap, LastCapFromRound, RoundScores).

startRoundFromLoad(StartingPlayer, Deck, Table, Human, Comp, LastCap, LastCapFromRound, RoundScores) :-
	StartingPlayer = 1,
	playRound(StartingPlayer, Deck, Table, Comp, Human, LastCap, LastCapFromRound, RoundScores).

parseBuildOwnersFromRaw(_, Terms, [], []) :-
Terms =< 11.

parseBuildOwnersFromRaw(RawData, Terms, HumanBuilds, CompBuilds) :-
	Max is Terms-4,
	getSlice(RawData, 7, Max, Slice),
	parseOwnersFromSlice(Slice, HumanBuildsRaw, CompBuildsRaw),
	sort(0, @<, HumanBuildsRaw, HumanBuilds),
	sort(0, @<, CompBuildsRaw, CompBuilds).


getSlice(_, Current, Max, []) :-
	Current > Max.

getSlice(RawData, Current, Max, Slice) :-
	Next is Current+1,
	getSlice(RawData, Next, Max, RestSlice),
	nth0(Current, RawData, CurrentElement),
	Slice = [CurrentElement | RestSlice].

parseOwnersFromSlice([], [], []).

parseOwnersFromSlice([Current| Rest], Human, Comp) :-
	Current =[Build | Owner],
	enumerateAtomId(Owner, Id),
	Id = 0,
	parseOwnersFromSlice(Rest, RestHuman, Comp),
	sumCardList(Build, Val),
	Human = [Val | RestHuman].

parseOwnersFromSlice([Current| Rest], Human, Comp) :-
	Current =[Build | Owner],
	enumerateAtomId(Owner, Id),
	Id = 1,
	parseOwnersFromSlice(Rest, Human, RestComp),
	sumCardList(Build, Val),
	Comp = [Val | RestComp].


enumerateAtomId(AId, 0) :-
	AId = human.

enumerateAtomId(_, 1).