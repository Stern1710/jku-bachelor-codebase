	# PURPOSE:  This program calculates the (integer) average of a
	#           set of integer data items (long values).
	#

	# VARIABLES: The registers have the following uses:
	# %rbp Index of current items
	# %rax Current data item, divident in the division
	# %rdi Sum of data, return status code at the end

	# The following memory locations are used:
	#
	# data_items - contains the item data.  A 0 is used
	#              to terminate the data. Only positive values are allowed.
	#

	.section .data

data_items:                       # These are the data items
	.quad 3,67,34,333,45,75,54,34,44,33,22,11,66,0

	.section .text

	.globl _start
_start:
	movq $0, %rbp		# Set index to zero
	movq $0, %rdi		# Set sum to zero
	movq data_items(,%rbp,8), %rax	# Load first value
	addq %rax, %rdi		# Add first value to the sum
	incq %rbx			# Increase item counter by one

start_loop:
	cmpq $0, %rax		# Check if end of loop is there
	je loop_exit		# Jump to exit

	incq %rbp			# Increase index by one
	movq data_items(,%rbp,8), %rax	# Load next value
	addq %rax, %rdi		# Increase sum by value
	jmp start_loop		# Jump to loop beginning

loop_exit:
	movq $0, %rdx		# Move 0 into upper 32 bit of divide register
	movq %rdi, %rax		# Move sum into divident register rax
	divq %rbp			# Divide by number of items (equals the index)
	movq %rax, %rdi		# Write average into status register

	movq $60, %rax		# Set 60 as this is the exit call
	syscall
