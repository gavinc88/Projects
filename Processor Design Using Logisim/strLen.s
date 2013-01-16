StringLen:
	andi $r0 $r0 0		#set $r0 to 0 so we can access address 0 of memory
	
loop:
	lw $r1 0($r0) 		#load counter ($r0), which equals memory location, into $r1
	zb $r2 $r1 			#check for null; 1 if null exists
	andi $r1 $r1 0		#set $r1 to 0 for comparing it with $r2
	addi $r0 $r0 1		#increment counter
	beq $r1 $r2 loop	#if null not reached, keep looping through memory

cleanup:
	andi $r2 $r2 0 		#reset $r2
	addi $r0 $r0 -1		#go back to the last memory to check if null is in upper or lower byte
	lw $r1 0($r0) 		#load char at current memory
	ori $r1 $r1 0x00FF 	#set lower 8 byte to 0xff so we can check if null is in upper byte
	zb $r1 $r1 			#check for null in upper byte; 1 if null is there
	add $r0 $r0 $r0 	#double the counter because each memory stores 2 characters
	beq $r1 $r2 bottom	#if null is in bottom byte

top: 					#null in upper byte
	disp $r0 0 			#display to d0
	jr $r3				#return to wherever the program left off

bottom: 				#null in lower byte
	addi $r0 $r0 1		#add 1 to result
	disp $r0 0			#display to d0
	jr $r3				#return to wherever the program left off

