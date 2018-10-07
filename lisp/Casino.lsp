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


(defun getFullDeck ()

	'(	(S A) (S 2) (S 3) (S 4) (S 5) (S 6) (S 7) (S 8) (S 9) (S X) (S J) (S Q) (S K) 
		(C A) (C 2) (C 3) (C 4) (C 5) (C 6) (C 7) (C 8) (C 9) (C X) (C J) (C Q) (C K)
		(D A) (D 2) (D 3) (D 4) (D 5) (D 6) (D 7) (D 8) (D 9) (D X) (D J) (D Q) (D K)
		(H A) (H 2) (H 3) (H 4) (H 5) (H 6) (H 7) (H 8) (H 9) (H X) (H J) (H Q) (H K)
	
	
	)


)

(defun playRound (roundNum)
	(Let (
			(firstPlayer (cond ((= roundNum 0) (flipCoin))  (t (print "Set up player for follow up round") '0)))
	
		)
	
		
	
	
	
	)





)
			
(defun runTournament (scores round)
	;Check scores here
	
	; run round as base case
	
	(Let ((result (checkScores scores))) ;Get the result and store it 
		(cond    
			((= result 0) (print "Human has won!"))
			((= result 1) (print "Computer has won!"))
			((= result 2) (print "Both players tied!"))
			(t (print (playRound 0)))
		
		
		
		)
	
	
	)
	
	
	;(cond ((< (checkScores scores) 3) (print "Someone has won!"))
	;		(t (checkScores score)))


		)			
			
			
;(print(promptForFileLoadIn))
;;Right here is how to handle loading in
;(print "Please enter Y/N")
;(getYesNoInput (read))

(runTournament '(0 0) 0)
