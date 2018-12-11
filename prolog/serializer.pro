%=======================
%Functions to load in serliazed data
%=======================	
load(LastCap, RoundScores) :-
	prompt1("What file would you like to load?"),
	read(File),
	exists_file(File),
	readFile(File, Data),
	parseSaveToRound(Data, LastCap, RoundScores).

load(LastCap, RoundScores) :-
	writeln("Invalid file name"),
	load(LastCap, RoundScores).	

readFile(FileName, FileData) :-
	open(FileName, read, FileStream),
	read(FileStream, FileData),
	close(FileStream).

parseSaveToRound(RawData, LastCapFromRound, RoundScores) :-

	nth0(1, RawData, CompScore),
	nth0(2, RawData, CompHand),

	nth0(3, RawData, CompPile),

	nth0(4, RawData, HumanScore),
	nth0(5, RawData, HumanHand),
	nth0(6, RawData,HumanPile),

	nth0(7, RawData, Table),


	nth0(8, RawData, CompReserved),
	nth0(9, RawData, HumanReserved),

	nth0(10, RawData, LastCapAtom),


	nth0(11, RawData, Deck),
	nth0(12, RawData, StartingPlayerAtom),
	enumerateAtomId(StartingPlayerAtom, StartingPlayer),
	enumerateAtomId(LastCapAtom, LastCap),

	%parseBuildOwnersFromRaw(RawData, Terms, HumanReserved, CompReserved),


	createPlayer(0, HumanHand, HumanPile, HumanReserved, HumanScore, Human),
	createPlayer(1, CompHand, CompPile, CompReserved, CompScore, Comp),


	startRoundFromLoad(StartingPlayer, Deck, Table, Human, Comp, LastCap, LastCapFromRound, RoundScores).


startRoundFromLoad(StartingPlayer, Deck, Table, Human, Comp, LastCap, LastCapFromRound, RoundScores) :-
	StartingPlayer = 0,
	playRound(StartingPlayer, Deck, Table, Human, Comp, LastCap, LastCapFromRound, RoundScores).

startRoundFromLoad(StartingPlayer, Deck, Table, Human, Comp, LastCap, LastCapFromRound, RoundScores) :-
	StartingPlayer = 1,
	playRound(StartingPlayer, Deck, Table, Comp, Human, LastCap, LastCapFromRound, RoundScores).

%parseBuildOwnersFromRaw(_, Terms, [], []) :-
%Terms =< 11.

%parseBuildOwnersFromRaw(RawData, Terms, HumanBuilds, CompBuilds) :-
%	Max is Terms-4,
%	getSlice(RawData, 7, Max, Slice),
%	parseOwnersFromSlice(Slice, HumanBuildsRaw, CompBuildsRaw),
%	sort(0, @<, HumanBuildsRaw, HumanBuilds),
%	sort(0, @<, CompBuildsRaw, CompBuilds).


%getSlice(_, Current, Max, []) :-
%	Current > Max.

%getSlice(RawData, Current, Max, Slice) :-
%	Next is Current+1,
%	getSlice(RawData, Next, Max, RestSlice),
%	nth0(Current, RawData, CurrentElement),
%	Slice = [CurrentElement | RestSlice].

%parseOwnersFromSlice([], [], []).

%parseOwnersFromSlice([Current| Rest], Human, Comp) :-
%	Current =[Build | Owner],
%	enumerateAtomId(Owner, Id),
%	Id = 0,
%	parseOwnersFromSlice(Rest, RestHuman, Comp),
%	sumCardList(Build, Val),
%	Human = [Val | RestHuman].

%parseOwnersFromSlice([Current| Rest], Human, Comp) :-
%	Current =[Build | Owner],
%	enumerateAtomId(Owner, Id),
%	Id = 1,
%	parseOwnersFromSlice(Rest, Human, RestComp),
%	sumCardList(Build, Val),
%	Comp = [Val | RestComp].


enumerateAtomId(AId, 0) :-
	AId = human.

enumerateAtomId(_, 1).

denumerateId(0, human).
denumerateId(_, computer).


formatSaveData(0, CompScore, CompHand, CompPile, HumanScore, HumanHand, HumanPile, Table,  CompReserved, HumanReserved, LastCapAtom, Deck, FirstIdAtom, Formatted) :-
	mergeLists([0], [CompScore], Merge1),
	mergeLists(Merge1, [CompHand], Merge2),
	mergeLists(Merge2, [CompPile], Merge3),
	mergeLists(Merge3, [HumanScore], Merge4),
	mergeLists(Merge4, [HumanHand], Merge5),
	mergeLists(Merge5, [HumanPile], Merge6),
	mergeLists(Merge6, [Table], Merge7),
	mergeLists(Merge7, [CompReserved], Merge8),
	mergeLists(Merge8, [HumanReserved], Merge9),
	mergeLists(Merge9, [LastCapAtom], Merge10),
	mergeLists(Merge10, [Deck] , Merge11),
	mergeLists(Merge11, [FirstIdAtom], Formatted).

saveFile(FileName, SaveData):-
	open(FileName, write, File),
	write(File, "["),
	writeSaveData(File, SaveData),
	write(File,"]."),
	close(File).


writeSaveData(_, []).
writeSaveData(File, [Line| []]) :-
	write(File, " "),
	writeq(File, Line).
	%writeSaveData(File, Rest).

writeSaveData(File, [Line | Rest]) :-
	write(File, " "),
	writeq(File, Line),
	writeln(File, ","),
	writeln(File, ""),
	%This space is seperate from the beginning so first line only gets one
	write(File, " "),
	writeSaveData(File, Rest).