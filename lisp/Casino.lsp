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

(defun closeApplication (statusNum)
	(print "Thanks for playing! :D")
	(exit statusNum)

)

(defun saveGame ()
					 
	(let
		(
			(file (open (getFilePath)
                     :direction :output
                     :if-exists :supersede
                     :if-does-not-exist :create
					 ))
		)
		
		(format file "Test String :D")
		(close file)
		
	)

	(closeApplication 0)
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
	(print "Please enter a number: ")
	(let 
		( 
			(userInput (read))
		)
		
		( cond
				((and (numberp userInput)(and (> userInput lowerBound) (<= userInput upperBound))) (- userInput 1))
				(t (print "Invalid input") (getNumericInput lowerBound upperBound))		
		)	
	)
)

(defun symbolToNumericValue (input aceHigh)
	(cond
		( (eq input #\A) (cond (( null aceHigh) 1) ( t 14) ))
		( (eq input #\2) 2)
		( (eq input #\3) 3)
		( (eq input #\4) 4)
		( (eq input #\5) 5)
		( (eq input #\6) 6)
		( (eq input #\7) 7)
		( (eq input #\8) 8)
		( (eq input #\9) 9)
		( (eq input #\X) 10)
		( (eq input #\J) 11)
		( (eq input #\Q) 12)
		(t 13)
	)
)

(defun numericValueToSymbol (input)
	(cond
		((= input 2) '2)
		((= input 3) '3)
		((= input 4) '4)
		((= input 5) '5)
		((= input 6) '6)
		((= input 7) '7)
		((= input 8) '8)
		((= input 9) '9)
		((= input 10) 'X)
		((= input 11) 'J)
		((= input 12) 'Q)
		((= input 13) 'K)
		(t 'A)
	)
)

(defun checkSetSum (check target checked)

	(cond ((null check) (cond ((= target 0 ) target) (t ())))
		(t (checkSetSum (rest check) (- target (symbolToNumericValue (getCardSymbol(first check)) ())) (append checked (list (first check)))))
	)
)

(defun findSetsThatSum (toProcess target currentResult currentSum)

	(cond 	((or
				(or (< target 1) (> target 14)) 
				( = (list-length toProcess) 0)
			  ) 
			  (checkSetSum currentResult target ()))
			(t 
				(cond (( >(+ (symbolToNumericValue (getCardSymbol(first toProcess)) ()) currentSum) target)
							(findSetsThatSum (rest toProcess) target currentResult currentSum)
						)
						(t (findSetsThatSum (rest toProcess) target (append currentResult (list (first toProcess))) (+ currentSum (symbolToNumericValue (getCardSymbol(first toProcess)) ()))))					
				)
			)
	)
)

(defun findAllSetsThatSumForStarting (starting toProcess target found)
	(let*
		(
			(result (findSetsThatSum toProcess target () 0))
			(appendedFound (cond ((null result) found) (t (cond ((null found) result)(t(append (cons found () )(list result)))))))
		)
		
		(cond ((null toProcess) appendedFound)
			(t (findAllSetsThatSumForStarting starting (rest toProcess) target appendedFound))		
		)	
	)
)

(defun findAllSetsThatSum (toProcess target found)
	(let*
		(
			(result (findAllSetsThatSumForStarting (first toProcess) (rest toProcess) target found))
			(appendedFound (cond ((null result) found) (t (cond ((null found) result)(t(append (cons found () )(list result)))))))
		
		)
		(cond 
			((null toProcess) appendedFound)
			(t (findAllSetsThatSum (rest toProcess) target appendedFound))
		
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



(defun getIndices (maxValue amount recieved)
	(let* 
		(
			(input (getNumericInput 0 maxValue))
			(appended (cond ((null recieved) (list input)) (t (append recieved (list input)))))
		)
		
		(cond ((<= amount 1) appended)
			(t (getIndices maxValue (- amount 1) appended)))
	)

)

(defun getSelectedCards (vector indices retrieved)
	(cond ((null indices) retrieved)
		(t 
			(getSelectedCards vector (rest indices) 
				(cond 
					((null retrieved) 
						(list (nth (first indices) vector))) 
					(t 
						(append retrieved (list (nth (first indices) vector)))
					)
				)
			)
		)
	)
)

;Returns T if set matches sum, and NIL if it doesn't
(defun getSetInput (table targetSum)
	(print "Please enter how many cards in the set you want to choose")
	(let*
		(
			(rawAmount (getNumericInput 1 6))
			(amount (+ rawAmount 1))
			
			(output (print "Please enter the indicies on new lines"))
			(indices (getIndices (list-length table) amount ()))
			(selectedSet (getSelectedCards table indices ()))
			(selectedSum (checkSetSum selectedSet targetSum ()))
		)
		(cond ((null selectedSum) ()) (t t))
	)
)

(defun captureSets (handAndTable)

	(print "Are there any additionial sets you would like to capture?")
	(let
		(
			(hand (first handAndTable))
			(table (rest handAndTable))
			(yesNo (getYesNoInput (read)))
		)
	
		(cond
			((string-equal yesNo "N") handAndTable)
			(t handAndTable)
		)
	)
)


;(defun doBuild (hand table)
;	(let*
;		(
;			(playedCardInput (getNumericInput 0 (list-length hand)))
;			(selectedHandCard (nth playedCardInput hand))
;			(remainingHandCards (removeNCard playedCardInput hand))
		     
;			(getBuildCards  table () )
;		)	
;	)

;)

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
	
		(cond ((equal actionToTake '1) "Capture here" (captureSets(doCapture hand table)))
				((equal actionToTake '2) "Build here"); (doBuild hand table))
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

(defun displayMenu (current)

	(cond 
		((= current 0) 
			(print "1) Save the Game")
			(print "2) Make a Move")
			(print "3) Ask for help")
			(print "4) Quit the game")
		)
		
		(t 
		
			(print "1) Save the Game")
			(print "2) Make a Move")
			(print "3) Quit the game")	
		)
	)
	
	(let*
		(
			(rawInput (cond ((= current 0 )(getNumericInput 0 4) ) (t (getNumericInput 0 3))))
			(nonIndexForm (+ 1 rawInput))
			(input (cond ((and (= current 1) (= nonIndexForm 3)) 4) (t nonIndexForm)))
		)
	
		(cond
			((= input 1) (saveGame))
			((= input 2) (print "DO NOTHING in MENU") 2)
			((= input 3) (print "IMPLEMENT HELP" ) 3)
			((= input 4) (closeApplication 0))
			(t (print "Invalid menu option, assuming 2") 2)
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
			
			(otherPlayer (cond ((= playerGoing 0) 1) (t 0)))
			(menuOption (displayMenu playerGoing))
			
			(firstMove (cond ((= playerGoing 0 ) (doPlayerMove humanHand humanPile table)) (t (doPlayerMove compHand compPile table))))
			(tableAfterFirst (nth 2 firstMove))
			(boardPlayerPrint (cond ((= playerGoing 0 ) (list (first firstMove) (nth 1 firstMove) compHand compPile)) (t (list humanHand humanPile (first firstMove) (nth 1 firstMove)))))
			(boardPrintState (printBoard (append(list 0 deck tableAfterFirst )boardPlayerPrint))) ;Var unused, just to print board
			(menuOption2 (displayMenu otherPlayer))
			
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
			
(print (getSetInput (list 'H5 'D6 'S2 'C7) 9))		
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
			
			;Make: firstPlayer deck tableCards humanHand humanPile compHand compPile
			(runTournament scores  roundNumber (list nextPlayer deck table (first humanPlayer) (nth 1 humanPlayer) (first compPlayer) (nth 1 compPlayer)))
		)
	
	)
	(t (runTournament '(0 0) 0 ()))
)

