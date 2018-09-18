#include "Casino.h"


/************************************************************
* Name:  Joseph Allen   jallen6@ramapo.edu                 *
* Project : Casino Game c++ Submission                     *
* Class : OPL Section-01                                   *
* Date : 2018-10-02                                        *
************************************************************/

/* *********************************************************************
Function Name: main
Purpose: The main accesspoint to the program
Parameters:
			argc, int size of argv
			argv, Array of charcter arrays for eaach command line argument
Return Value: The exit code of the program, an int
Local Variables:
			Tournament* tour, which contains the game to be run
Algorithm:
			1) Create the tournament object
			2) Tell the object to start running
Assistance Received: none
********************************************************************* */
int main(int argc, char* argv[]) {

	Tournament* tour = new Tournament;
	tour->RunTournament();

	delete tour;
	return 0;
}