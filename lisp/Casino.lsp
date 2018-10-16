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

(defun getFilePath ()
	(print "Please enter the fileName")
	(read)
)


(defun loadInFile ()
	(let*
		(
			(filePath (getFilePath))
			(saveFile (open filePath :if-does-not-exist nil))
		)
		
		(cond 
			((null saveFile) (print "File failed to open")(loadInFile))
			(t  (read saveFile))
		)
	)
)			

;Returns the round number in a list
(defun getRoundNumFromFile(data)
	(list (first data))
)
;Returns the score (Human, comp)
(defun getScoresFromFile (data)
	(list (nth 4 data) (nth 1 data))
)

;Gets the comp player form (hand, pile)
(defun getCompFromFile (data)
	(list (nth 2 data) (nth 3 data))
)
;Gets the human player form (hand, pile)
(defun getHumanFromFile (data)
	(list (nth 5 data) (nth 6 data))
)
;Gets the table in a list
(defun getTableFromFile (data)
	(nth 7 data)
)
;Gets the next player, 0 human 1 comp
(defun getNextPlayerFromFile (data)
	(cond ((string-equal(nth (-(list-length data)1) data) "Human")0)(t 1) )
)
;Gets the deck 
(defun getDeckFromFile (data)
	(nth (-(list-length data)2) data )
)
;Gets the last capturer, 0 human 1 comp
(defun getLastCapturer  (data)
(cond ((string-equal(nth (-(list-length data)3) data) "Human")0)(t 1) )
)
;Get the build owners, non implemented
(defun getBuildOwners (data)
	(print "Implement getBuildOwners")

)

;(defun formatSaveFileToRoundParams (params)

;(list (list(first params))())
	

;)

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
	(char (string card) 1)
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
					((eq (getCardSymbol (first vector)) target) (findAndRemoveSymbol target (rest vector) (list (list removedCards (first Vector)) nonRemovedCards))) ;(list (cons removedCards  (list (first vector))) nonRemovedCards)
					(t (findAndRemoveSymbol target (rest vector) (list removedCards ( append nonRemovedCards  (list (first vector))))))
				)
			)
			(t result)
		
		)
	)

)

; Worked w/ JL NC
(defun getFullDeck ()
	'(	SA S2 S3 S4 S5 S6 S7 S8 S9 SX SJ SQ SK
		CA C2 C3 C4 C5 C6 C7 C8 C9 CX CJ CQ CK
		DA D2 D3 D4 D5 D6 D7 D8 D9 DX DJ DQ DK
		HA H2 H3 H4 H5 H6 H7 H8 H9 HX HJ HQ HK
	)
	;'(	(S A) (S 2) (S 3) (S 4) (S 5) (S 6) (S 7) (S 8) (S 9) (S X) (S J) (S Q) (S K) 
	;	(C A) (C 2) (C 3) (C 4) (C 5) (C 6) (C 7) (C 8) (C 9) (C X) (C J) (C Q) (C K)
	;	(D A) (D 2) (D 3) (D 4) (D 5) (D 6) (D 7) (D 8) (D 9) (D X) (D J) (D Q) (D K)
	;	(H A) (H 2) (H 3) (H 4) (H 5) (H 6) (H 7) (H 8) (H 9) (H X) (H J) (H Q) (H K)	
	;)
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
			(print "Result")
			(print resultTuple)
			
			(cond 
				;If move was invalid
				((equal resultTuple (list hand table)) (print "Invalid move") (doPlayerMove hand pile table))
				;Move was valid
				(t (list (first resultTuple) pile (nth 1 resultTuple)))
			
			)
			
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

			
(defun runTournament (scores round intialParams)
	
	(Let ((result (checkScores scores))) ;Get the result and store it 
		(cond    
			((= result 0) (print "Human has won!"))
			((= result 1) (print "Computer has won!"))
			((= result 2) (print "Both players tied!"))
			(t (print (playRound round intialParams)))
		)
	)
)			
			
	

;"Main"	
(cond 
	((string-equal (promptForFileLoadIn) "Y") 
		(let* 
			(
				(fileData (loadInFile))
				(roundNumber (getRoundNumFromFile fileData))
				(scores (getScoresFromFile fileData))
				(compPlayer (getCompFromFile fileData))
				(humanPlayer (getHumanFromFile fileData))
				(table (getTableFromFile fileData))
				(deck (getDeckFromFile fileData))
				(nextPlayer (getNextPlayerFromFile fileData))
				(lastCapturer (getLastCapturer fileData))
			)
			
			;(print roundNumber)
			;(print scores)
			;(print compPlayer)
			;(print humanPlayer)
			;(print table)
			;(print deck)
			;(print nextPlayer)
			;(print lastCapturer)
			;Make: firstPlayer deck tableCards humanHand humanPile compHand compPile
			(runTournament scores  roundNumber (list nextPlayer deck table (first humanPlayer) (nth 1 humanPlayer) (first compPlayer) (nth 1 compPlayer)))
		)
	
	)

	(t (runTournament '(0 0) 0 ()))
)




