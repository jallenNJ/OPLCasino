%=======================
%Functions to load in serialized data
%=======================	

/* *********************************************************************
Function Name: load
Purpose: To load in a file from save data
Parameters:
            
Return Value: 
            Last Cap -- who captured last
			RoundScores The scores of the players at the end
			RoundNum The number of the round at the end of the round
Algorithm:
            1) Get the save plath
			2) Make sure it exists
			3) Read the file
			4) Parse the file
Assistance Received: none
********************************************************************* */
load(LastCap, RoundScores, RoundNum) :-
	prompt1("What file would you like to load?"),
	read(File),
	exists_file(File),
	readFile(File, Data),
	parseSaveToRound(Data, LastCap, RoundScores, RoundNum).
%In case of invalid file, prompt again
load(LastCap, RoundScores, RoundNum) :-
	writeln("Invalid file name"),
	load(LastCap, RoundScores, RoundNum).	

/* *********************************************************************
Function Name: readFile
Purpose: Opens a file, reads in the first statement, and close it
Parameters:
            The file name to read
Return Value: 
           The first list in the file
Algorithm:
            1) Open
			2) Read
			3) Close
Assistance Received: none
********************************************************************* */
readFile(FileName, FileData) :-
	open(FileName, read, FileStream),
	read(FileStream, FileData),
	close(FileStream).

/* *********************************************************************
Function Name: parseSaveToRound
Purpose: Parse the loaded save data and resume the round
Parameters:
            Raw Data is the data from the save file
Return Value: 
            LastCapFromRound  The id of who captured last
			RoundScores The Scores of who captured last
			RounNum  The round num at the end of the round
Algorithm:
            1) Parse each field into own var
			2) enumerate atoms
			3) start round
Assistance Received: none
********************************************************************* */
parseSaveToRound(RawData, LastCapFromRound, RoundScores, RoundNum) :-

	nth0(0, RawData, RoundNum),
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


	%Create the player list from their data
	createPlayer(0, HumanHand, HumanPile, HumanReserved, HumanScore, Human),
	createPlayer(1, CompHand, CompPile, CompReserved, CompScore, Comp),


	startRoundFromLoad(RoundNum, StartingPlayer, Deck, Table, Human, Comp, LastCap, LastCapFromRound, RoundScores).


/* *********************************************************************
Function Name: startRoundFromLoad
Purpose: Start the round from the savde data
Parameters:
           RoundNum The roundNum to start at
		   StartingPlayer The ID of who is going first
		   Deck The deck to start with
		   Table The table to start with
		   Human  The human player
		   Comp The Comp player
		   Last Cap Id of who captured last
Return Value: 
            LastCapFromRound The player who capped last in the round
			RoundScores The scores of who capped last
Algorithm:
            1)Start the round based on the player
Assistance Received: none
********************************************************************* */
%Human first
startRoundFromLoad(RoundNum, StartingPlayer, Deck, Table, Human, Comp, LastCap, LastCapFromRound, RoundScores) :-
	StartingPlayer = 0,
	playRound(RoundNum, StartingPlayer, Deck, Table, Human, Comp, LastCap, LastCapFromRound, RoundScores).
%ComputerFirst
startRoundFromLoad(RoundNum, StartingPlayer, Deck, Table, Human, Comp, LastCap, LastCapFromRound, RoundScores) :-
	StartingPlayer = 1,
	playRound(RoundNum, StartingPlayer, Deck, Table, Comp, Human, LastCap, LastCapFromRound, RoundScores).


%Change a symbol (human/computer) to its id(0/1)
enumerateAtomId(AId, 0) :-
	AId = human.

enumerateAtomId(_, 1).

%Change an id(0/1) to (human/computer).
denumerateId(0, human).
denumerateId(_, computer).


/* *********************************************************************
Function Name: formatSaveData
Purpose: Take the raw game data and format into a single list to save
Parameters:
            RoundNum  The current round num
			CompScore The current Computer Score
			CompHand  The current comp hand
			Comppile  The current comp Pile
			HumanScore The current HumanScore
			HumanHand The current human hand
			HumanPile The current human pile
			Table The current table state
			CompReserve The builds that comp has
			HumanReserved The builds the the human has
			LasCapAtom   Last cap as an atom
			Deck		The current state of the deck
			FirstIdAtom  The id of the first palyer
Return Value: 
            Formatted, the formatted data
Algorithm:
            1) Check if selected cards are valid
            2) If it is, do the move, otherwise reprompt
Assistance Received: none
********************************************************************* */
formatSaveData(RoundNum, CompScore, CompHand, CompPile, HumanScore, HumanHand, HumanPile, Table,  CompReserved, HumanReserved, LastCapAtom, Deck, FirstIdAtom, Formatted) :-
	%Round and comp score
	mergeLists([RoundNum], [CompScore], Merge1),
	mergeLists(Merge1, [CompHand], Merge2),
	mergeLists(Merge2, [CompPile], Merge3),
	%Human
	mergeLists(Merge3, [HumanScore], Merge4),
	mergeLists(Merge4, [HumanHand], Merge5),
	mergeLists(Merge5, [HumanPile], Merge6),
	%Table and builds
	mergeLists(Merge6, [Table], Merge7),
	mergeLists(Merge7, [CompReserved], Merge8),
	mergeLists(Merge8, [HumanReserved], Merge9),
	%Misc info and deck
	mergeLists(Merge9, [LastCapAtom], Merge10),
	mergeLists(Merge10, [Deck] , Merge11),
	mergeLists(Merge11, [FirstIdAtom], Formatted).

/* *********************************************************************
Function Name: saveFile
Purpose: write the save data to the fill
Parameters:
           File Name  The name of the file to open
		   Save Data, The data to write
Return Value: 
        None -- Data saved to file
Algorithm:
			1)Open file
			2) Add brackets
			3)Write data
Assistance Received: none
********************************************************************* */
saveFile(FileName, SaveData):-
	%Open and write the open bracket
	open(FileName, write, File),
	write(File, "["),
	%write the data and close the list then file
	writeSaveData(File, SaveData),
	write(File,"]."),
	close(File).

/* *********************************************************************
Function Name: writeSaveData
Purpose: Write the save data to the file in correct formatting
Parameters:
            File The file stream to write data to
			The data to write 
Return Value: 
           none
Algorithm:
            1) Write the data,
			2) Added correct white space
Assistance Received: none
********************************************************************* */
%No data left, end recursion
writeSaveData(_, []).

%Last line, don't write the comma
writeSaveData(File, [Line| []]) :-
	write(File, " "),
	writeq(File, Line).
	%writeSaveData(File, Rest).

%Normal data
writeSaveData(File, [Line | Rest]) :-
	%Write only a single space as there is already one there
	write(File, " "),
	%The line and the comma, then a nl line to seperate data
	writeq(File, Line),
	writeln(File, ","),
	writeln(File, ""),
	%This space is seperate from the beginning so first line only gets one
	write(File, " "),
	writeSaveData(File, Rest).