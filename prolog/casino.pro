%************************************************************
%* Name: Joseph Allen                                       *
%* Project:  Casino in Prolog  Fall 2018                    *
%* Class:  CMPS366-01 Fall 2018                             *
%* Date:  11-Dec-2018                                        *
%************************************************************

/* *********************************************************************
Function Name:main
Purpose: To consult all includes and be the entry point
Assistance Received: none
********************************************************************* */ 
main() :-
	consult("card.pro"),
	consult("player.pro"),
	consult("round.pro"),
	consult("inputAndOutput.pro"),
	consult("serializer.pro"),

	runTournament().		
