#include "PlayingCard.h"


PlayingCard::PlayingCard(char su, char sy) {
		suit = su;
		symbol = sy;
		symbolToNumericValue();

}