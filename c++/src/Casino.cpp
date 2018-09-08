#include "Casino.h"


int main(int argc, char* argv[]) {

	Tournament* tour = new Tournament;
	tour->RunTournament();

	delete tour;
	return 0;
}