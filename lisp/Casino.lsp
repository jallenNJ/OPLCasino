(defun loadInFile ()
	(print "Would you like to load in a save file")
	(cond ( (string-equal (read) "Y")  ; If yes
				'(Y))
			(t 
				'(N)
			)))


;(print(loadInFile))
;;Right here is how to handle loading in


