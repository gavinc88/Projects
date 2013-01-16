OCTAL:
	andi $r2 $r2 0 		#clear $r2; used as the result
	andi $r0 $r0 0		#reset $r0 to 0 so we can access address 0 of memory
	lw $r1 0($r0)		#load the input hex value into $r1
	
	addi $r0 $r0 0x7	#set $r0 to 0x7, which is the position for the first 3 bits
	and $r1 $r1 $r0		#examine the first 3 bits
	add $r2 $r2 $r1		#set the first 3 bits of result
	
	andi $r0 $r0 0		#reset $r0 to 0 so we can access address 0 of memory
	lw $r1 0($r0)		#load the original hex value again since we modified it
	addi $r0 $r0 0x3	#set $r0 to 3; $r0 will be used for shift
	srlv $r1 $r1 $r0	#shift original hex value by 3 bits so we can examine the next 3 bits
	andi $r1 $r1 0x7	#examine the 2nd 3 bits; note that $r1 is changed
	addi $r0 $r0 0x1	#set $r0 to 4; previously 3; $r0 will be used for shift
	sllv $r1 $r1 $r0	#shift the 2nd 3 bits by 4
	add $r2 $r2 $r1		#update result with the 2nd 3 bits
	
	andi $r0 $r0 0		#reset $r0 to 0 so we can access address 0 of memory
	lw $r1 0($r0)		#load the original hex value again since we modified it
	addi $r0 $r0 0x6	#set $r0 to 6; previously 0; $r0 will be used for shift
	srlv $r1 $r1 $r0	#shift original hex value by 6 so we can examine the 3rd 3 bits
	andi $r1 $r1 0x7	#examine the 3rd 3 bits
	addi $r0 $r0 0x2	#set $r0 to 8; previously 6; $r0 will be used for shift
	sllv $r1 $r1 $r0	#shift the 3rd 3 bits by 8
	add $r2 $r2 $r1		#update result with the 3rd 3 bits
	
	#return
	disp $r2 0			#display result to d0
	jr $r3 				#return to wherever the program left off
	