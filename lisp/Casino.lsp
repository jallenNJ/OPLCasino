(defun getYesNoInput (input)
		(cond( (string-equal input "Y") 
					"Y")
			 ( (string-equal input "N") 
					"N")
			( t	(print "Try again")	(getYesNoInput (read)))
		)
)
			
(defun promptForFileLoadIn ()
	(print "Would you like to load in a save file(y/n)")
	(getYesNoInput (read))
)


			


; Check scores in form (HUMANSCORE, COMPSCORE)
(defun checkScores (scores)
	(let ( ( human (first scores)) ;Get the human score
			(comp (first(rest scores))) ;Get the comp score
	)	
		(cond 
			((and (> human 20)(> human comp)) 0) ;If Human > 20 && human > comp
			((and (> comp 20)(< human comp)) 1) ; If comp > 20 && human < comp
			((and (> human 20)(= human comp)) 2) 
			(t 3)
		
		)	
	)
)


(defun getHeadTailChoice ()
	(print "(H)eads or (T)ails")
	(Let(
		(input  (read))	
		)
		(cond 
			((string-equal input "H") 'H)
			((string-equal input "T") 'T)
			(t (getHeadTailChoice))		
		)	
	)
)

(defun flipCoin ()
	(Let (
			(input (getHeadTailChoice))
			(randNum (random 2))
		)
		
		(cond 	((and (eq input 'h) (= randNum 0)) (print "Heads! Human goes first!") '0)
				((and (eq input 'h) (= randNum 1)) (print "Tails! Computer goes first!") '1)
				((and (eq input 't) (= randNum 0)) (print "Heads! Computer goes first!") '1)
				(t (print "Tails! Human goes first!") '0)
		)
	)
)

(defun removeNCard (n vector)
	(let 
		(
			(remainingPrev (nthcdr (- (list-length vector) n)(reverse vector)))
			(remainingAfter (nthcdr (+ n 1) vector))
		)
		(append remainingPrev remainingAfter)
	
	)



)


(defun getCardSymbol (card)
	(first (rest card))
)

(defun findAndRemoveSymbol (target vector result)
	(let
		(
			(removedCards (first result))
			(nonRemovedCards (first(rest result)))
			
		)
		
		(cond 
			((> (list-length vector) 0)
				(cond
					((eq (getCardSymbol (first vector)) target) (findAndRemoveSymbol target (rest vector) (list (list removedCards (first vector)) nonRemovedCards))) 
					(t (findAndRemoveSymbol target (rest vector) (list removedCards ( list nonRemovedCards (first vector)))))
				)
			)
			(t result)
		
		)
	)

)

; Worked w/ JL NC
(defun getFullDeck ()

	'(	(S A) (S 2) (S 3) (S 4) (S 5) (S 6) (S 7) (S 8) (S 9) (S X) (S J) (S Q) (S K) 
		(C A) (C 2) (C 3) (C 4) (C 5) (C 6) (C 7) (C 8) (C 9) (C X) (C J) (C Q) (C K)
		(D A) (D 2) (D 3) (D 4) (D 5) (D 6) (D 7) (D 8) (D 9) (D X) (D J) (D Q) (D K)
		(H A) (H 2) (H 3) (H 4) (H 5) (H 6) (H 7) (H 8) (H 9) (H X) (H J) (H Q) (H K)	
	)
)

(defun shuffleDeck (deck shuffled)
	(cond	
		(
			(> (list-length deck) 0) 
				(let* 
					(
						(randomNum (random (list-length deck) (make-random-state)))
						(choosen (nth randomNum deck))
						(newShuffle (cond ((null shuffled) (cons choosen ())) (t (append shuffled (cons choosen ())))))
					;	(remainingPrev (nthcdr (- (list-length deck) randomNum)(reverse deck)))
					;	(remainingAfter (nthcdr (+ randomNum 1) deck))			
					
					)
					
					(shuffleDeck (removeNCard randomNum deck) newShuffle)
				)
		)
		(t shuffled)
	)
	
)

(defun dealFourCards (deck)
 
	
	;Get the first four cards off the deck as a list of lists 
	(list (nth 0 deck) (nth 1 deck) (nth 2 deck)(nth 3 deck))
)

(defun dealFourCardsIfEmpty (hand deck)


	(cond 
		((null hand) (list (dealFourCards deck) (nthcdr 4 deck))) 
		(t (list hand deck))
	
	
	)

	

)

(defun printAll (hHand cHand table deck nextPlayer)
		(print 'Human )
		(print 	hHand)
		
		(print '-----------)
		(print 'Table )
		(print table)
		(print '-----------)
		
		(print 'Comp )
		(print cHand)

		(print '==================)
		(print 'Deck)
		(print deck)
		
		(cond ((= nextPlayer 0) (print "Human is next")) (t (print "Computer is next")))
)


(defun printBoard (parse) ; Parse round params
	(printAll (nth 3 parse) (nth 5 parse) (nth 2 parse) (nth 1 parse) (first parse)) 

)

(defun promptForAction ()
	(print "Would you like to Capture(1), Build(2), Trail(3)")
	( Let ((userInput (read)))
		(cond  ((equal userInput '1) '1)
				((equal userInput '2) '2)
				((equal userInput '3) '3)
				(t (print "Invalid input") (promptForAction))
		) 	
	)
)


(defun getNumericInput(lowerBound upperBound)
	;(print ('"Enter a number between" lowerBound '" and " upperBound))
	(print "Test enter a num")
	(let 
		( 
			(userInput (read))
			;numberp to check 
		)
		
		
		( cond
				((and (numberp userInput)(and (>= userInput lowerBound) (< userInput upperBound))) userInput)
				(t (print "Invalid input") (getNumericInput lowerBound upperBound))		
		)	
	)
)

(defun doCapture (hand table)
	(let*
		(
			(playedCardInput (getNumericInput 0 (list-length hand)))
			(selectedHandCard (nth playedCardInput hand))
			(remainingHandCards (removeNCard playedCardInput hand))
			;(selectedTableInput (getNumericInput 0 (list-length table)))
			;(selectedTableCard (nth selectedTableInput table))
			;(remainingTableCards (removeNCard selectedTableInput table))
			(resultTuple (findAndRemoveSymbol (getCardSymbol selectedHandCard) table ()))
			(selectedTableCards (first resultTuple))
			(remainingTableCards (first (rest resultTuple)))
		)
		(cond
				((> (list-length selectedTableCards) 0) (list remainingHandCards remainingTableCards))
				(t (list hand table))			
		)
	)
)

(defun doTrail (hand table)
	(let*
		(
			(input (getNumericInput 0 (list-length hand)))
			(selectedCard (nth input hand))
			(remainingCards (removeNCard input hand))		
		)
		(list remainingCards (append table (list selectedCard)))
	)
)

;return (hand table)
(defun takeAction (hand table)
	(Let ((actionToTake (promptForAction)))
	
		(cond ((equal actionToTake '1) "Capture here" (doCapture hand table))
				((equal actionToTake '2) "Build here")
				((equal actionToTake '3) "Trail here" (doTrail hand table))
				(t (takeAction hand table))
				
		)	
	)
)


(defun doPlayerMove (hand pile table)
	(cond 
		((null hand) (list hand pile table))
		(t
		
		(let
			(
				(resultTuple (takeAction hand table))
			)
			(list (first resultTuple) pile (nth 1 resultTuple))
		)
		
		)
	
	)

)


;This functions does a cycle where both players play one card
(defun doCycle (players table playerGoing deck)
	(Let* 
		(
			(humanHand (cond ((= playerGoing 0) 	(first players)) (t (nth 2 players))))
			(humanPile (cond ((= playerGoing 0) 	(nth 1 players)) (t (nth 3 players))))
			(compHand (cond ((= playerGoing 0) 	(nth 2 players)) (t (nth 0 players))))
			(compPile (cond ((= playerGoing 0) 	(nth 3 players)) (t (nth 1 players))))
			
			(firstMove (cond ((= playerGoing 0 ) (doPlayerMove humanHand humanPile table)) (t (doPlayerMove compHand compPile table))))
			(tableAfterFirst (nth 2 firstMove))
			(boardPlayerPrint (cond ((= playerGoing 0 ) (list (first firstMove) (nth 1 firstMove) compHand compPile)) (t (list humanHand humanPile (first firstMove) (nth 1 firstMove)))))
			(boardPrintState (printBoard (append(list 0 deck tableAfterFirst )boardPlayerPrint))) ;Var unused, just to print board
			
			(secondMove (cond ((= playerGoing 0 ) (doPlayerMove compHand compPile tableAfterFirst)) (t (doPlayerMove humanHand humanPile tableAfterFirst))))
			(tableAfterBoth (nth 2 secondMove))
			(humanResult (cond  ((= playerGoing 0 ) (list (first firstMove) (nth 1 firstMove))) (t (list (first secondMove) (nth 1 secondMove)))))
			(compResult (cond  ((= playerGoing 1 ) (list (first firstMove) (nth 1 firstMove))) (t (list (first secondMove) (nth 1 secondMove)))))
		)
		;Make: firstPlayer deck tableCards humanHand humanPile compHand compPile
		(list playerGoing deck tableAfterBoth (first humanResult) (nth 1 humanResult) (first compResult) (nth 1 compResult) )
	)


)




(defun newRoundParams (roundNum)
	(Let* (
			(firstPlayer (cond ((= roundNum 0) (flipCoin))  (t (print "Set up player for follow up round") '0)))
			(startingDeck (shuffleDeck (getFullDeck) ()))
			(humanHand (dealFourCards startingDeck))
			(humanPile ())
			(compHand  (dealFourCards (nthcdr 4 startingDeck)))
			(compPile ())
			(tableCards (dealFourCards (nthcdr 8 startingDeck)))
			(deck (nthcdr 12 startingDeck))
		)
	
		(list firstPlayer deck tableCards humanHand humanPile compHand compPile)
	)

)

(defun playRound (roundNum roundParams)
	(cond
		((null roundParams) (playRound roundNum (newRoundParams roundNum)))
		((< (list-length roundParams) 7) 
			;(print roundParams)
			(printBoard roundParams)
			(print "THIS IS TERMINATING CASE, ADD PROPER HANDLING")
		)
		(t 
			(let* 
				(
					(humanHandCheck (dealFourCardsIfEmpty (nth 3 roundParams) (nth 1 roundParams)))
					(deckAfterHuman (nth 1 humanHandCheck))
					(compHandCheck (dealFourCardsIfEmpty (nth 5 roundParams) deckAfterHuman))
					(deckAfterBoth (nth 1 compHandCheck))
					(updatedPlayers (list (first humanHandCheck) (nth 4 roundParams) (first compHandCheck) (nth 6 roundParams)))
					
				)
				(printBoard (append (list (first roundParams) deckAfterBoth (nth 2 roundParams)) updatedPlayers))
				(playRound roundNum (doCycle updatedPlayers (nth 2 roundParams) (first roundParams) deckAfterBoth))
			
				)
			)
		
		
			;(printBoard roundParams)
			;(playRound roundNum (doCycle (nthcdr 3 roundParams) (nth 2 roundParams) (first roundParams) (nth 1 roundParams)))	
		
	)
	
)

			
(defun runTournament (scores round)
	
	(Let ((result (checkScores scores))) ;Get the result and store it 
		(cond    
			((= result 0) (print "Human has won!"))
			((= result 1) (print "Computer has won!"))
			((= result 2) (print "Both players tied!"))
			(t (print (playRound 0 ())))
		
		
		
		)
	
	
	)
	


)			
			
			

;(getYesNoInput (read))

(cond ((string-equal (promptForFileLoadIn) "Y") (print "do the load"))

		(t (runTournament '(0 0) 0)))


