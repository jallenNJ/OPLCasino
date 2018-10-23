;Joseph Allen | jallen6@ramapo.edu
;	CMPS 366-01 Organization of Programming Languages Fa 18
;	Lisp submission for Casino

;====================
; functions for loading or saving to a file (serialization)
;====================


;This function prompts the user and returns the string they enter
(defun getFilePath ()
	(print "Please enter the fileName")
	
	(let
		(
			(input (read))
		)
		(cond 
			((numberp input) (print "Please enter a string") (getFilePath))
			(t input)
		)
	)	
)



;This function opens, reads the first list in a file, and returns its
(defun loadInFile ()
	(let*
		(
			;Get the file path
			(filePath (getFilePath))
			;Open the file, and if it doesn't exist, return nil instead of exception
			(saveFile (open filePath :if-does-not-exist nil))
		)
		
		(cond 
			;If failed to open, prompt for new path
			((null saveFile) (print "File failed to open")(loadInFile))
			;Else, return the list
			(t  (read saveFile))
		)
	)
)

;This functions outputs the closing message, and 
; exits the application with inputted status num
(defun closeApplication (statusNum)
	(print "Thanks for playing Casino in Lisp! :D")
	(exit statusNum)

)

;This function displays the prompt for loading a file, then calls yes no input
(defun promptForFileLoadIn ()
	(print "Would you like to load in a save file(y/n)")
	(getYesNoInput (read))
)			

; ******
; These functions retrieve data from the loaded in list
; ******
;Returns the round number in a list
(defun getRoundNumFromFile(data)
	 (first data)
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


;This function writes the data to the save file
; with each element on its own line, then two \n following it
(defun saveGame (data)
					 
	(let
		(
			;Open file to write
			; Override if it exits, create if it doesn't
			(file (open (getFilePath)
                     :direction :output
                     :if-exists :supersede
                     :if-does-not-exist :create
					 ))
		)
		;Specify directives, serializable (able to load back in)
		; and two new lines
		(format file "(~%~{~S~%~%~})" data)
		;Close the streams
		(close file)
		
	)
	;Quit the application
	(closeApplication 0)
)




; =================================
;These functions are for getting user input
; =================================

;This function is responsible for getting "Y" or "N" input
;Recursivily calls itself until it does
(defun getYesNoInput (input)
		(cond
		((numberp input ) (print "Try again")	(getYesNoInput (read)))
		( (string-equal input "Y") 
					"Y")
			 ( (string-equal input "N") 
					"N")
			( t	(print "Try again")	(getYesNoInput (read)))
		)
)

;This function is responsible for getting if the user wants heads or tails
;Recursivily calls itself until it works
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

;This function prompts the user for which move action they want to takeAction
;Recursivily calls itself until a valid move is input
;1 is Capture, 2 is Build, 3 is Trail
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


;This functions gets numeric input on the range [lowerBound upperBound)
; However it prompts the user to enter on (lowerBound upperBound] for useablity
; Therefore it takes what the user entered, subtracts one, and returns
;Recursivily calls itself until a valid number is entered
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
;========================================
;These functions are for internal logic
; This is mainly a misc category and used to format, retrieve or generate data as helper functions
;=======================================

;This function flips a coin, and says if the human won/lost based on user input
(defun flipCoin ()
	(Let (
			(input (getHeadTailChoice))
			(randNum (random 2))
		)
		
		(cond 	((and (eq input 'h) (= randNum 0)) (print "Heads! Human goes first!") 0)
				((and (eq input 'h) (= randNum 1)) (print "Tails! Computer goes first!") 1)
				((and (eq input 't) (= randNum 0)) (print "Heads! Computer goes first!") 1)
				(t (print "Tails! Human goes first!") 0)
		)
	)
)

;This function gets a card symbol (the base case if its a single card)
;The over arching logic is to get the "symbol" of a build
(defun getCardSymbol (card)
	(cond 
		;If its a build with more than one card left
		((and (listp card) (> (list-length card) 1) )
			(numericValueToSymbol 
				;Add this value, with the rest of the build (found recursivly)
				( + 
					(symbolToNumericValue (getCardSymbol (first card)) () ) 
					(symbolToNumericValue (getCardSymbol (rest card))  () )
				)
			)
		)
		;Recursive base case, the last card in a build
		((and (listp card) (= (list-length card) 1)) (getCardSymbol (first card)))
		
		;If a singleton card entered as a string
		((= (length (string card)) 1) (char (string card) 0))
		;If a singletone card entered as a symbol
		(t (char (string card) 1))
	)

)



; Worked w/ JL NC
;Create the full deck of cards as symbols
; Worked with Josh Long and Nick Cockcraft for intialize algoirthm discussion (what data type to use as cards)
(defun getFullDeck ()
	'(	SA S2 S3 S4 S5 S6 S7 S8 S9 SX SJ SQ SK
		CA C2 C3 C4 C5 C6 C7 C8 C9 CX CJ CQ CK
		DA D2 D3 D4 D5 D6 D7 D8 D9 DX DJ DQ DK
		HA H2 H3 H4 H5 H6 H7 H8 H9 HX HJ HQ HK
	)
)

;This function shuffles the inputted deck
; To start the shuffle, pass the deck to deck, and nil to shuffled
(defun shuffleDeck (deck shuffled)
	(cond	
		(
			;If cards are left to be shuffled
			(> (list-length deck) 0) 
				(let* 
					(
						;Get a random number between 0 and size of deck
						(randomNum (random (list-length deck) (make-random-state)))
						;Get that card
						(choosen (nth randomNum deck))
						;Append to the shuffled list, if its null create  list with this as first crd
						(newShuffle (cond ((null shuffled) (cons choosen ())) (t (append shuffled (cons choosen ())))))			
					)
					;Recursivily call this function
					(shuffleDeck (removeNCard randomNum deck) newShuffle)
				)
		)
		;Return the shuffled deck
		(t shuffled)
	)
	
)

;This functions deals four cards from the deck
;Does NOT return a modified deck, only a list of the four cards
(defun dealFourCards (deck)
 
	;Get the first four cards off the deck as a list of lists 
	(list (nth 0 deck) (nth 1 deck) (nth 2 deck)(nth 3 deck))
)

;This function checks if a deck is empty
;If it is, deal four cards, and return that as the hand
;Else, return the input
;This function DOES return a truncated deck in the list
(defun dealFourCardsIfEmpty (hand deck)

	(cond 
		;If hand is empty, cards need to be dealt
		((null hand) (list (dealFourCards deck) (nthcdr 4 deck))) 
		(t (list hand deck))
	)
)


(defun indexToString (index)
	(cond ((= index 0 ) "Human")
			(t "Computer")
	)
)

;This function takes the card symbol (as a char), and returns its numeric equivlant
;aceHigh controls if aces are 1 or 14. nil for 1, non-Nil (t) for 14
(defun symbolToNumericValue (input aceHigh)

	(cond
		;If its a number already, return that
		( (numberp input) input)
		;If Ace, return 1 or 14 based on aceHigh
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

;This function takes the numeric value and returns the symbol as a char
(defun numericValueToSymbol (input)
	(cond
		((= input 2) #\2)
		((= input 3) #\3)
		((= input 4) #\4)
		((= input 5) #\5)
		((= input 6) #\6)
		((= input 7) #\7)
		((= input 8) #\8)
		((= input 9) #\9)
		((= input 10) #\X)
		((= input 11) #\J)
		((= input 12) #\Q)
		((= input 13) #\K)
		(t #\A)
	)
)


;Remove the nth card from the list, and return the rest of the list
(defun removeNCard (n vector)
	(let 
		(
			;Get everything before the card in the list
			;This is done by reversing the list, and taking everything after from n-1
			(remainingPrev (nthcdr (- (list-length vector) n)(reverse vector)))
			;Get everything after n
			(remainingAfter (nthcdr (+ n 1) vector))
		)
		;Append these lists together and return
		(append remainingPrev remainingAfter)	
	)

)



;This function finds all specified symbols in the provided vector, and then returns the vector without those symbols
;To start off this function, pass nill to result
;Result is in the form (removed, nonRemoved), and is returned when done execution
(defun findAndRemoveSymbol (target vector result)
	(let
		(
			;For readability, seperate and gives names
			(removedCards (first result))
			(nonRemovedCards (first(rest result)))
		)

		(cond 
			;If cards left to process
			((> (list-length vector) 0)
				(cond
					
					;If the first card matches the symbol
					((eq (getCardSymbol (first vector)) (getCardSymbol target)) 
						(findAndRemoveSymbol target (rest vector)  
						(cond 
							;If vector is null, add list directly to correct index
							((null removedCards)  
								(list (list (first Vector)) nonRemovedCards)) 
							(t ;Else, add to the list
								(list (list removedCards (first Vector)) nonRemovedCards)))))
					;Card does not match symbol	
					(t (findAndRemoveSymbol target (rest vector) (list removedCards  
						(cond 
							;If current is nil,create list with this card
							((null nonRemovedCards) 
								(list (first vector))) 
								
							(t ;Else append to it
							( append nonRemovedCards  (list (first vector))))))))
				)
			)
			;No cards left, return what it found
			(t result)
		
		)
	)
)

; Returns T or nil
(defun checkIfIndexIsInList (number vector)

	(cond ((null vector) () )
			((= number (first vector)))
			(t (checkIfIndexIsInList number (rest vector)))
	)
)


;Returns list of non included indices in ascending order
(defun getIndicesNotInList (maxIndex vector result )
	(cond 
		((< maxIndex 0) (reverse result))
		((null (checkIfIndexIsInList maxIndex vector )) (getIndicesNotInList (- maxIndex 1) vector (append result (list maxIndex))))
		(t (getIndicesNotInList (- maxIndex 1) vector result))
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




;These functions are for scoring

; Check scores in form (HUMANSCORE, COMPSCORE)
(defun checkScores (scores)
	(let ( ( human (first scores)) ;Get the human score
			(comp (first(rest scores))) ;Get the comp score
	)	
		(cond 
			((and (> human 20)(> human comp)) 0) ;If Human > 20 && human > comp
			((and (> comp 20)(< human comp)) 1) ; If comp > 20 && human < comp
			((and (> human 20)(= human comp)) 2) ;If Human > 20 && human = comp
			(t 3) ;Game must continue
		
		)	
	)
)

(defun isSpade (card)
	(let
		(
			(suit (char (string card) 0))
		)
		
		(cond 
			((eq suit 'S) t)
			((eq suit #\S) t)
			(t nil)
		)
	)
)

(defun isAce (card)
	(cond
		((eq (getCardSymbol card) #\A) t)
		((eq (getCardSymbol card) 'A) t)
		(t nil)
	
	)

)

;Returns T or nil
(defun hasTenOfDiamonds (vector)

	(let
		(
			(suit (char (string (first vector)) 0))
			(symb (getCardSymbol (first vector)))
		)
		
		(cond 
			((null vector) nil)
			((and
				(or (eq suit 'D) (eq suit #\D))
				(or (eq symb 'X) (eq symb #\X))
			 )
				t
			)
			(t (hasTenOfDiamonds (rest vector)))	
		)
	)
)

;Returns T or nil
(defun hasTwoOfSpades (vector)

	(let
		(
			(suit (char (string (first vector)) 0))
			(symb (getCardSymbol (first vector)))
		)
		
		(cond 
			((null vector) nil)
			((and
				(or (eq suit 'S) (eq suit #\S))
				(or (eq symb '2) (eq symb #\2))
			 )
				t
			)
			(t (hasTwoOfSpades (rest vector)))	
		)
	)
)


(defun countSpades (vector amount)
	(cond
		((null vector) amount)
		((isSpade (first Vector)) (countSpades (rest vector) (+ amount 1)))
		(t (countSpades (rest vector) amount))
	
	
	)
)

(defun countAces (vector amount)
	(cond
		((null vector) amount)
		((isAce (first Vector)) (countAces (rest vector) (+ amount 1)))
		(t (countAces (rest vector) amount))
	)
)

 

(defun calculateScores (humanPile compPile scores)
	(print "Human pile: ")
	(print humanPile)
	(print (list-length humanPile))
	(print "Comp pile: ")
	(print compPile)
	(print (list-length compPile))


	(let* 
		(
		
			(humanStartScore (first scores))
			(compStartScore  (nth 1 scores))
			(humanMostCardsScore (cond ((> (list-length humanPile) (list-length compPile)) 3) (t 0)))
			(compMostCardsScore (cond ((< (list-length humanPile) (list-length compPile)) 3) (t 0)))
			
			(humanSpades (countSpades humanPile 0))
			(compSpades (countSpades compPile 0))
			
			(humanSpadesScore (cond ((> humanSpades compSpades) 1) (t 0)))
			(compSpadesScore (cond ((< humanSpades compSpades) 1) (t 0)))
		
			(humanTenDiamondsScore (cond ((
			hasTenOfDiamonds humanPile) 2) (t 0 )))
			(compTenDiamondsScore (cond ((hasTenOfDiamonds compPile) 2) (t 0 )))
			
			(humanTwoSpadesScore (cond ((hasTwoOfSpades humanPile) 1) (t 0 )))
			(compTwoSpadesScore (cond ((hasTwoOfSpades compPile) 1) (t 0 )))

			(humanAces (countAces humanPile 0))
			(compAces (countAces compPile 0))
		)
		
		(cond 
			((> humanMostCardsScore compMostCardsScore) (print "Human had the most cards"))
			((< humanMostCardsScore compMostCardsScore) (print "Comp had the most cards"))
			(t (print "Players had equal cards"))
		)
		(print "Human Spades")
		(print humanSpades)
		(print "Computer Spades")
		(print compSpades)
		
		(cond
			((> humanSpadesScore compSpadesScore) (print "Human had the most spades"))
			((< humanSpadesScore compSpadesScore) (print "Comp had the most spades"))
			(t (print "They tied in Spades!"))
		)
		
		(cond 
			((> humanTenDiamondsScore 0) (print "Human had the DX"))
			(t (print "Comp had the DX"))
		)
		(cond
			((> humanTwoSpadesScore 0) (print "Human had the S2"))
			(t (print "Comp had the S2"))
		)
		(print "Human had this many aces:")
		(print humanAces)
		(print "Comp had this many aces:")
		(print compAces)
		
		(print "Human round score: ")
		(print (+(+(+(+(+ humanMostCardsScore) humanSpadesScore) humanTenDiamondsScore) humanTwoSpadesScore) humanAces) )
		(print "Computer round score: ")
		(print (+(+(+(+(+ compMostCardsScore)compSpadesScore)compTenDiamondsScore)compTwoSpadesScore) compAces))
		
		(print "Human total round score: ")
		(print (+(+(+(+(+(+ humanStartScore)humanMostCardsScore) humanSpadesScore) humanTenDiamondsScore) humanTwoSpadesScore) humanAces) )
		(print "Computer total round score: ")
		(print (+(+(+(+(+(+ compStartScore compMostCardsScore)compSpadesScore)compTenDiamondsScore)compTwoSpadesScore) compAces)))
		(list 
			(+(+(+(+(+(+ humanStartScore)humanMostCardsScore) humanSpadesScore) humanTenDiamondsScore) humanTwoSpadesScore) humanAces) 
			(+(+(+(+(+(+ compStartScore compMostCardsScore)compSpadesScore)compTenDiamondsScore)compTwoSpadesScore) compAces))
		)
		
	)
)


; These functions are for output
(defun displayMenu (current saveData)

	(cond 
		((= current 0) 
			(print "1) Save the Game")
			(print "2) Make a Move (Human)")
			(print "3) Ask for help")
			(print "4) Quit the game")
		)
		
		(t 
		
			(print "1) Save the Game")
			(print "2) Make a Move (Computer)")
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
			((= input 1) (saveGame saveData))
			((= input 2)  2)
			((= input 3) (print "IMPLEMENT HELP" ) 3)
			((= input 4) (closeApplication 0))
			(t (print "Invalid menu option, assuming 2") 2)
		)	
	)
)


;Deals with outputting to console

(defun printAll (hHand cHand table deck nextPlayer humanPile compPile)
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
		
		(print 'HumanPile)
		(print humanPile)
		(print '-)
		(print 'ComputerPile)
		(print compPile)
		(print '---------------)
		
		(cond ((= nextPlayer 0) (print "Human is next")) (t (print "Computer is next")))
)

;Takes in next player deck table hHand hPile cHand cPile
(defun printBoard (parse) ; Parse round params
	(printAll (nth 3 parse) (nth 5 parse) (nth 2 parse) (nth 1 parse) (first parse) (nth 4 parse) (nth 6 parse)) 

)







;These functions deals with sets

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





;Returns (indices) if set matches sum, and NIL if it doesn't
(defun getSetInput (table targetSum)
	(print "Please enter how many cards in the set you want to choose")
	(let*
		(
			(rawAmount (getNumericInput 0 6))
			(amount (+ rawAmount 1))
			
			(output (print "Please enter the indicies on new lines"))
			(indices (getIndices (list-length table) amount ()))
			(selectedSet (getSelectedCards table indices ()))
			(selectedSum (checkSetSum selectedSet targetSum ()))
		)
		(cond ((null selectedSum) ()) (t indices))
	)
)

;====================================
;These functions are for player actions
;====================================
(defun captureSets (handAndTable)

	(print "Are there any additionial sets you would like to capture?")
	(let
		(
			(hand (first handAndTable))
			(pile (nth 1 handAndTable))
			(table (nth 2 handAndTable))
			(playedCard (nth 3 handAndTable))
			(playedCardIndex (nth 4 handAndTable))
			(addedPlayedCard (nth 5 handAndTable))
			(yesNo (getYesNoInput (read)))
		)
	
		(cond
			((string-equal yesNo "N") handAndTable)
			;(t (getSetInput table (symbolToNumericValue (getCardSymbol playedCard) t)))
			
			(t
				(let 
					(
						(result (getSetInput table (symbolToNumericValue (getCardSymbol playedCard ) t)))
					)
					(print result)
					(cond 
						((null result) handAndTable)
						
						( t
							(list 
								;Add Hand
								(removeNCard playedCardIndex hand) 
								
								;Add Pile
								(cond 
									;No cards in pile and card was not added already
									((and (null pile) (null addedPlayedCard))
										(append (getSelectedCards table result () ) (list playedCard)))
									;Cards in pile and card not added already
									((and (not(null pile)) (null addedPlayedCard))
										(append pile (append (getSelectedCards table (getIndicesNotInList (- (list-length table) 1) result () ) ()) (list playedCard))))  
									;No cards in pile but card was added already... shouldn't happen... but better to have an edge case then crash	
									((null pile) 
										(getSelectedCards table result () )) 
									;Pile exists and card added
									(t 
										(append pile result))
								) 
									
									;Add Table
									(getSelectedCards table (getIndicesNotInList (- (list-length table) 1) result () ) ()) 
							)
						)
					)
				
				)
			)
		)
	)
)

(defun doCapture (hand pile table)
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
				((> (list-length selectedTableCards) 0) (list remainingHandCards  (append (append pile (list selectedHandCard)) selectedTableCards) remainingTableCards selectedHandCard playedCardInput t))
				(t (list hand pile table selectedHandCard playedCardInput () ))			
		)
	)
)

(defun doBuild (hand pile table)
	(let*
		(
			(playedCardInput (getNumericInput 0 (list-length hand)))
			(selectedHandCard (nth playedCardInput hand))
			(remainingHandCards (removeNCard playedCardInput hand))
			(output (print "What card to sumto?"))
			(output2 (print remainingHandCards))
			(selectedTargetCard (nth (getNumericInput 0 (list-length remainingHandCards)) remainingHandCards))

			(buildCardIndices (getSetInput table (- (symbolToNumericValue(getCardSymbol selectedTargetCard ) t) (symbolToNumericValue(getCardSymbol selectedHandCard ) () ))))
			(buildCards (getSelectedCards table buildCardIndices ()))
			
			
		)
		(print "Do build needs to remove cards put into build")
		;(print selectedHandCard)
		(cond
			((null buildCardIndices) (list hand pile table))
			(t 
				(list 
					remainingHandCards 
					pile
					(append 
						(getSelectedCards table (getIndicesNotInList (- (list-length table) 1) buildCardIndices () ) ()) 
						(list (cons selectedHandCard buildCards))
					)
				)
			)
		)
	)

)

(defun doTrail (hand pile table)
	(let*
		(
			(input (getNumericInput 0 (list-length hand)))
			(selectedCard (nth input hand))
			(remainingCards (removeNCard input hand))		
		)
		(list remainingCards pile (append table (list selectedCard)))
	)
)

(defun computerTrail (hand table)

	(let*
		(
			(input (random (list-length hand)))
			(selectedCard (nth input hand))
			(remainingCards (removeNCard input hand))		
		)
		(list remainingCards (append table (list selectedCard)))
	)

)
;return (hand table)
(defun takeAction (hand pile table)
	(Let ((actionToTake (promptForAction)))
	
		(cond ((equal actionToTake '1) "Capture here" (captureSets(doCapture hand pile table)))
				((equal actionToTake '2) (doBuild hand pile table))
				((equal actionToTake '3) "Trail here" (doTrail hand pile table))
				(t (takeAction hand pile table))	
		)	
	)
)

(defun aiCheckForCapture (vector table index)
	(cond 
		((null vector) nil)
		(t
			(let*
				(
					(currentCard (first vector))
					(result (findAndRemoveSymbol (getCardSymbol currentCard) table nil))
				)

				(cond
					((null (first result)) 	(aiCheckForCapture (rest vector) table (+ index 1)))
					(t (list (first result) (nth 1 result) index))
				)
			)
		)
	)	
)


(defun doComputerMove (hand pile table)
	(let
		(
			(captureResult (aiCheckForCapture hand table 0))
			(trailTuple (computerTrail hand table))
		)
		
		(cond 
			;If move was invalid
			;((equal resultTuple (list hand table)) (print "Invalid move") (doPlayerMove hand pile table))
			;Move was valid
			((not (null (first captureResult))) (print "Ai is capturing") (list (removeNCard (nth 2 captureResult) hand)
			
				(append pile 
					(append 
						(cond 
							((listp (first captureResult)) 
								(cond 
									((listp (first (first captureResult))) 
										(first (first captureResult))) 
									(t (first captureResult)))) 
										
							(t (list (first captureResult)))
						) 
						(list (nth (nth 2 captureResult) hand )))) (nth 1 captureResult)))
			(t (print "AI is trailing")(list (first trailTuple) pile (nth 1 trailTuple)))
		
		)
		
	)
)


(defun doHumanMove (hand pile table)

	(let
		(
			(resultTuple (takeAction hand pile table))
		)
		(print "Result")
		(print resultTuple)
		
		(cond 
			;If move was invalid
			((equal resultTuple (list hand pile table)) (print "Invalid move") (doHumanMove hand pile table))
			;Move was valid
			(t (list (first resultTuple) (nth 1 resultTuple) (nth 2 resultTuple)))
		
		)
		
	)

)

(defun doPlayerMove (hand pile table playerId)
	
	(cond 
		((null hand) (list hand pile table))
		((= playerId 1) (doComputerMove hand pile table))
	
		(t (doHumanMove hand pile table))
	
	)
	
)



(defun checkIfCapture (startPile afterPile)
	(cond
		((null afterPile) nil)
		((equal startPile afterPile) nil)
		(t t)
	
	)


)


;This functions does a cycle where both players play one card
(defun doCycle (players table playerGoing deck saveFileParams lastCap)
	(Let* 
		(
			(humanHand (first players))
			(humanPile (nth 1 players))
			(compHand (nth 2 players))
			(compPile (nth 3 players))
			
			(otherPlayer (cond ((= playerGoing 0) 1) (t 0)))
			(menuOption (displayMenu playerGoing (list (first saveFileParams) (nth 2 saveFileParams)  compHand compPile (nth 1 saveFileParams) humanHand humanPile table (indexToString lastCap) deck (indexToString playerGoing))))
			
			(firstMove (cond ((= playerGoing 0 ) (doPlayerMove humanHand humanPile table 0)) (t (doPlayerMove compHand compPile table 1))))
			(firstCaptured (cond ((= playerGoing 0) (checkIfCapture humanPile (nth 1 firstMove))) (t (checkIfCapture compPile (nth 1 firstMove)))))
			(tableAfterFirst (nth 2 firstMove))
			(boardPlayerPrint (cond ((= playerGoing 0 ) (list (first firstMove) (nth 1 firstMove) compHand compPile)) (t (list humanHand humanPile (first firstMove) (nth 1 firstMove)))))
			;(boardPlayerPrint (cond ((= playerGoing 0 ) (list  compHand compPile (first firstMove) (nth 1 firstMove))) (t (list (first firstMove) (nth 1 firstMove) humanHand humanPile))))
			(boardPrintState (printBoard (append(list 0 deck tableAfterFirst )boardPlayerPrint))) ;Var unused, just to print board
			
			(menuOption2 (displayMenu otherPlayer () ))
			
			(secondMove (cond ((= playerGoing 0 ) (doPlayerMove compHand compPile tableAfterFirst 1)) (t (doPlayerMove humanHand humanPile tableAfterFirst 0))))
			(secondCaptured (cond ((= playerGoing 0) (checkIfCapture compPile (nth 1 firstMove))) (t (checkIfCapture humanPile (nth 1 firstMove)))))
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
			(firstPlayer (cond ((= roundNum 0) (flipCoin))  (t (print "Set up player for follow up round") 0)))
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


(defun playRound (roundNum roundParams scores)
	(cond
		((null roundParams) (playRound roundNum (newRoundParams roundNum) (list roundNum (first scores) (nth 1 scores))))
		((and (null (nth 1 roundParams)) (null (nth 3 roundParams))) 
			(printBoard roundParams)
			;(print "THIS IS TERMINATING CASE, ADD PROPER HANDLING")
			(print "HANDLE LAST CAPTURER and LOOSE CARDS GOING THERE")
			(print "PAST YOU RECOMMENDS RETURNING WHO DID AND HANDLING IN CALC SCORES")
			(print (calculateScores (nth 4 roundParams) (nth 6 roundParams) scores))
			(calculateScores (nth 4 roundParams) (nth 6 roundParams) scores)
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
				(print "ENSURE SAVE OF LAST CAPTURER BETWEEN CYCLES")
				(playRound roundNum (doCycle updatedPlayers (nth 2 roundParams) (first roundParams) deckAfterBoth (list roundNum (first scores) (nth 1 scores)) 0) scores)
			
				)
			)
	)
)

(defun outputTournamentResults (scores)
	(print "Human had this many tournament points: ")
	(print (first scores))
	(print "Computer had this many tournament points: ")
	(print (nth 1 scores))
	(cond
		((> (first scores) (nth 1 scores) )
			(print "Human won!")
		)
		((< (first scores) (nth 1 scores)) (print "Computer won!"))
		(t (print "The players tied!"))
	)

	

)
			
(defun runTournament (scores round intialParams)
	
	(Let ((result (checkScores scores))) ;Get the result and store it 
		(cond    
			((< result 3) scores)
			(t
			(runTournament (playRound round intialParams scores) (+ round 1) () )) 
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
			
			;Make: firstPlayer deck tableCards humanHand humanPile compHand compPile
			(outputTournamentResults(runTournament scores  roundNumber (list nextPlayer deck table (first humanPlayer) (nth 1 humanPlayer) (first compPlayer) (nth 1 compPlayer))))
		)
	
	)
	(t (outputTournamentResults(runTournament '(0 0) 0 ())))
)


