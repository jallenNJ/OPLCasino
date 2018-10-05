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

;(print(promptForFileLoadIn))
;;Right here is how to handle loading in
(print "Please enter Y/N")
(getYesNoInput (read))



