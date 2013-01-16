.data
failed: .asciiz "Failed!\n"
.data	
passed: .asciiz "My Tests Passed!\n"
.data
str: .asciiz "Hello, world!\n"
srl: .asciiz "srl "
sll: .asciiz "sll "
sra: .asciiz "sra "

.text

# _start is the entry point into any program.
.global _start
.ent    _start 
_start:

#
#  The header ends here, and code goes below
#

# print a string.  (note the %lo syntax only works if the string is
# in the lower 32KB of memory, which it always will be for us)
	ori   $a0, $zero, %lo(str)
	ori   $v0, $zero, 4
	syscall
	
# <MY TEST CODE>
	ori  $a0, $zero, %lo(srl)
	addiu  $t0, $zero, -1
	ori  $t1, $zero, 65535
	addiu  $t2, $zero, -1
	srl  $a1, $t0, 16
	bne  $a1, $t1, print_fail
	
	ori  $a0, $zero, %lo(sll)
	sll  $a2, $a1, 16
	lui  $a3, 0xffff
	bne  $a2, $a3, print_fail
	
	ori  $a0, $zero, %lo(sra)
	sra  $a3, $a2, 16
	bne  $a3, $t2, print_fail	

	
	ori   $a0, $0, %lo(passed)
	ori   $v0, $zero, 4
	syscall
	j	exit
	
print_fail:
	ori   $v0, $zero, 4
	syscall		#print test name
	ori   $a0, $zero, %lo(failed)
	ori   $v0, $zero, 4
	syscall		#print "Failed!"	
	
# exit the simulation (v0 = 10, syscall)
exit:
	ori   $v0, $zero, 10
	syscall


	
.end _start


