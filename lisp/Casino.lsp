(defun promptForFileLoadIn ()
	(print "Would you like to load in a save file")
	(cond ( (string-equal (read) "Y")  ; If yes
				'(Y))
			(t 
				'(N)
			)))

			
(defun getYesNoInput (input)
		(cond( (string-equal input "Y") 
					'(Y))
			 ( (string-equal input "N") 
					'(N))
			( t	(print "Try again")	(getYesNoInput (read)))
			))

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
						(randomNum (random (list-length deck) ))
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
;

)

(defun printAll (hHand cHand table deck)
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
)


(defun printBoard (parse) ; Parse round params
	(printAll (nth 3 parse) (nth 5 parse) (nth 2 parse) (nth 1 parse)) 

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
				((and (>= userInput lowerBound) (<= userInput upperBound)) userInput)
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
			(selectedCardInput (getNumericInput 0 (list-length table)))
			(selectedTableCard (nth selectedCardInput table))
			(remainingTableCards (removeNCard selectedTableCard table))
		
		)
		;(list remainingCards (append table (list selectedCard)))
		(cond
			(
				(equal (getCardSymbol selectedHandCard) (getCardSymbol selectedTableCard)) (list remainingCards remainingTableCards)
				(t (list hand table))
				
			)
			
		
		
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


	;( list (rest hand) (append table (list (first hand))))
)


(defun takeAction (hand table)
	(Let ((actionToTake (promptForAction)))
	
		(cond ((equal actionToTake '1) "Caputure here")
				((equal actionToTake '2) "Build here")
				((equal actionToTake '3) "Trail here" (doTrail hand table))
				(t (takeAction hand table))
				
		)	
	)

)


;;Pass in all the players, if remaining player, recursive call
(defun doCycle (players table playerGoing deck)
	(Let* 
		(
			(hand (nth 0 players))
			(pile (nth 1 players))
			(resultTuple (takeAction hand table))
			(nextPlayer (cond ((= playerGoing 0 ) 1) (t 0)))
		)
	
		;Contains new hand, new table
		;Make: firstPlayer deck tableCards humanHand humanPile compHand compPile
		(cond
			(t (list nextPlayer deck (nth 1 resultTuple) (first resultTuple) () (nth 2 players) () )) ;Left piles () until implemented
			;(t (append () (list 'TestCase)))		
		)
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
			(printBoard roundParams)
			(playRound roundNum (doCycle (nthcdr 3 roundParams) (nth 2 roundParams) (first roundParams) (nth 1 roundParams)))	
		)
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
			
			
;(print(promptForFileLoadIn))
;;Right here is how to handle loading in
;(print "Please enter Y/N")
;(getYesNoInput (read))

(runTournament '(0 0) 0)

