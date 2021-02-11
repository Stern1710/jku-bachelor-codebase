# PURPOSE:	This program converts an input file to an output file with input fields checked for validity
#			and converting them into correct output files
# PROCESSING: 1) Open the input file
#             2) Open the output file
#             3) While we're not at the end of the input file
#               a) read part of the file into our piece of memory
#               b) Calclulate checksum and look it it is viable
#                  if last byte is zero, proceed, otherwise print and error into output
#               c) Check values for validity, else write error message
#				d) Write outout line to the outout fule

.section .data

### Constants ###
    # System call numbers
	.equ SYS_OPEN, 2
	.equ SYS_READ, 0
	.equ SYS_WRITE, 1
	.equ SYS_CLOSE, 3
	.equ SYS_EXIT, 60

    # Options for file opening open 
	.equ O_RDONLY, 0                  # Open file options - read-only
	.equ O_CREAT_WRONLY_TRUNC, 03101  # Open file options for create, write and trunc
	.equ O_PERMS, 0666                # Read & Write permissions for everyone

	# End-of-file result status
	.equ END_OF_FILE, 0  # Value for EOF in read method

	# Header for output file
    header:		.ascii "serial-target -type    -count -height-length-width -weight-chcksm\n\0"
    .equ HEADER_LEN, 66	# Header length

	# Error message
    errormsg:	.ascii "ERROR\n\0"
    .equ ERROR_LEN, 6	# Error message length

	# ASCII DEC numbers for symbols
	.equ ZN_BASE, 48		# Base of numbers 0...9 in ASCII (48 = 0)
    .equ AF_BASE, 65		# Base for A...F in ASCII (65 = A)
    .equ AF_ADD, 10			# Value to add when converting numbers for A...F

	.equ LAST_BITS, 0x000000FF	# Mask for AND-operation to only allow last 8 bits to stay in register
	.equ CHECKSUM_MASK, 0x0000FFFF	# Mask for Checksum to check if last two bits are all zero

    # serial package codes
    ae: .ascii "AEX317-"
    f9: .ascii "BUZ439-"
    b8: .ascii "DTO380-"
    AE: .ascii "AE"
    F9: .ascii "9F"
    B8: .ascii "B8"

    # destination target codes
    c3: .ascii "GERMANY-"
    C3: .ascii "3C"
    ed: .ascii "ITALY  -"
    ED: .ascii "ED"
    tf: .ascii "SPAIN  -"
    TF: .ascii "53"
    a0: .ascii "JAPAN  -"
    A0: .ascii "0A"
    f1: .ascii "AUSTRIA-"
    F1: .ascii "1F"

    # package type codes
    e7: .ascii "FOOD    "
    E7: .ascii "7E"
    d1: .ascii "CHEMICAL"
    D1: .ascii "D1"
    va: .ascii "MEDICINE"
    VA: .ascii "84"
    f8: .ascii "COMMON  "
    F8: .ascii "F8"

	# general string contants
	dash: .ascii "-"		# Constant for dash-symbol
	wd: .ascii "    -"		# four whitespaces and single dash constant for output formatting
	nl: .ascii "\n"			# Newline for linebreak in output

### Buffers ###
    # Input buffer
    .equ BUFFER_SIZE_IN, 21					# Input buffer size
    .lcomm BUFFER_DATA_IN, BUFFER_SIZE_IN	# Input buffer with size 21 for reading input file line by line

    # Output buffer
    .equ BUFFER_SIZE_OUT, 64				# Output buffer size
    .lcomm BUFFER_DATA_OUT, BUFFER_SIZE_OUT	# Output buffer with size 64 for writing to output file

### Programm Code ###
    .section .text
    # Important positions on the stack
    .equ ST_SIZE_RESERVE, 16 # Space for local variables
	# Note: Offsets are RBP-based, which is set immediately at program start
	.equ ST_FD_IN, -16       # Local variable for input file descriptor
	.equ ST_FD_OUT, -8       # Local variable for output file descriptor
	.equ ST_ARGC, 0          # Number of arguments
	.equ ST_ARGV_0, 8        # Name of program
	.equ ST_ARGV_1, 16       # Input file name
	.equ ST_ARGV_2, 24       # Output file name

    .globl _start
_start:
    ### Initalize the programm ###
    movq %rsp, %rbp
	subq $ST_SIZE_RESERVE, %rsp # Allocate space for our file descriptors on the stack
	###Check if parameter count is correct, otherwise quit program ###
	cmpq $3, ST_ARGC(%rbp)		# If too few or too many arguments (<> 3), quit
	je open_files
	movq $1, %rdi              # Our return value for parameter problems
	movq $SYS_EXIT, %rax
	syscall

open_files:
open_fd_in:
    ### Open all input files ###
    movq ST_ARGV_1(%rbp), %rdi      # Input filename into rdi
    movq $O_RDONLY, %rsi            # Open file read only
    movq $O_PERMS, %rdx             # Set permissions
    movq $SYS_OPEN, %rax        # Specify "open"
	syscall 	                # Call Linux
	movq $2, %rdi				# Move exit code 2 in case open input failed
	cmpq $0, %rax               # Check success
	jl exit                     # In case of error simply terminate
	movq  %rax, ST_FD_IN(%rbp)  # Else: Save the returned file descriptor

open_fd_out:
	###OPEN OUTPUT FILE###
	movq ST_ARGV_2(%rbp), %rdi        # Output filename into %rdi
	movq $O_CREAT_WRONLY_TRUNC, %rsi  # Flags for writing to the file
	movq $O_PERMS, %rdx               # Permission set for new file (if it's created)
	movq $SYS_OPEN, %rax              # Open the file
	syscall                           # Call Linux
	movq $3, %rdi					  # Move exit code 3 in case open output failed
	cmpq $0, %rax                     # Check success
	jl close_input                    # In case of error close input file (already open!)
	movq %rax, ST_FD_OUT(%rbp)        # Store the file descriptor

write_header:
	# Write header into file
	movq ST_FD_OUT(%rbp), %rdi	# Get output file location
	movq $header, %rsi			# place header location in rsi
	movq $HEADER_LEN, %rdx		# Write header length in rdx
	movq $SYS_WRITE, %rax		# Pass SysWrite number
	syscall						# Syscall to write header
	movq $HEADER_LEN, %r12		
	cmpq %rax, %r12				# CHeck if header was written correctly
	jne end_loop   				# If not, end program

read_loop_begin:
	### 1) Read first line and store into buffer ###
	movq ST_FD_IN(%rbp), %rdi		# Get input file descriptor
	movq $BUFFER_DATA_IN, %rsi		# Buffer data location to write read result into
	movq $BUFFER_SIZE_IN, %rdx		# Size of the input buffer
	movq $SYS_READ, %rax
	syscall
	# Exit if EOF
	movq $0, %rdi
	cmpq $END_OF_FILE, %rax			# Compare EOF static to return of syscall
	jle end_loop

check_checksum:
	### 2) Check the checksum if this is actually legit and quit if the line has a error ###
	# Call function and jump to error printing if there is a problem

	movq $0, %r10		# Storage for 4 Bytes from Input Buffer
	movq $0, %r11		# Addition sum of values
	movq $0, %r15		# Loop counter
	movq $BUFFER_DATA_IN, %rdi	# Load base pointer of buffer into rdi

checksum_loop:
	cmpq $4, %r15				# Check if 4 byte pairs have already been read
	jae add_checksum_value		# If yes, add checksum value and calculare

	movq (, %rdi, 1), %r10		# Read four byte from inout buffer

	movq %r10, %r8				# Prepare argument regs with values
	movq %r10, %r9				
	sar $8, %r9					# Shift second value to the right into last 8 bits
	and $LAST_BITS, %r8			# Make only relevant last 8 bits remain the value, set all others to 0
	and $LAST_BITS, %r9

	call from_ascii_to_number	# Call function to translate two Bytes into a number
	addq %rax, %r11				# Add the returned numbers together

	movq %r10, %r8				# Prepare second two bytes by copying into two separate registers
	movq %r10, %r9 
	sar $16, %r8				# Shift into last 8 bits
	sar $24, %r9
	and $LAST_BITS, %r8			# Make only relevant last 8 bits remain the value, set all others to 0
	and $LAST_BITS, %r9	

	call from_ascii_to_number	# Call function to translate inout bytes into value
	addq %rax, %r11				# Add returned number together

	inc %r15					# Increase loop counter by 1
	addq $4, %rdi				# Add four to location in put buffer to read next four bytes
	jmp checksum_loop	
	
add_checksum_value:
	movq (, %rdi, 1), %r10		# Load four bits of checksum

	movq %r10, %r8				# Prepare argument regs with values (similiar to loop)
	movq %r10, %r9 
	sar $16, %r8
	sar $24, %r9
	and $LAST_BITS, %r8
	and $LAST_BITS, %r9

	call from_ascii_to_number	# Call function
	addq %rax, %r11				# Add lower byte to checksum sum

	movq %r10, %r8				# Prepare arguments of higher byte for parsing
	movq %r10, %r9
	sar $8, %r9
	and $LAST_BITS, %r8			# Make only relevant last 8 bits remain the value, set all others to 0
	and $LAST_BITS, %r9	

	call from_ascii_to_number	# Call function
	sal $8, %rax				# Shift to the left for 8 bits
	addq %rax, %r11				# Add shifted number to checksum


	and $CHECKSUM_MASK, %r11	# Make only last 16 bits, hopefully all 0, remain
	cmpq $0, %r11			# Compare if value is zero
	jne error_message		# If not, print error as checksum was not okay

input_decoding:
	### 3) Checksum was okay, so calculate various codes and numbers ###
	movq $BUFFER_DATA_IN, %rdi	# Load buffer location into rdi
	movw (, %rdi, 1), %bx		# Load first two bytes into bx register

serial_codes:
	# Compare serial inputs
serial_input:
	cmpw AE, %bx		# Check if bytes are equal to AE
	je write_ae			# If  yes, write encoded AE value to file

	cmpw F9, %bx		# Check if bytes are equal to 9F
	je write_f9

	cmpw B8, %bx		# Check if bytes are equal to B8
	je write_b8

	jmp error_message	# Write ERROR into the current line as not match was found

write_ae:
	movq $BUFFER_DATA_OUT, %rdi		# Load output buffer location into rdi
	movq ae, %r12					# Write value from AE into register
	movq %r12, (, %rdi, 1)			# Write ae into output buffer
	jmp destination_input

write_f9:
	movq $BUFFER_DATA_OUT, %rdi		# See above in write_aem here for 9F encoding
	movq f9, %r12				
	movq %r12, (, %rdi, 1)
	jmp destination_input

write_b8:
	movq $BUFFER_DATA_OUT, %rdi		# See above in write_aem here for 9F encoding
	movq b8, %r12
	movq %r12, (, %rdi, 1)
	jmp destination_input

destination_input:
	# Read byte for destination target codes
	movq $BUFFER_DATA_IN, %rdi
	addq $2, %rdi				# Add 2 to current offset for next 2 bytes
	movw (, %rdi, 1), %bx		# Move two bytes into register bx

	cmpw C3, %bx				# Check which destination is in the input
	je write_c3

	cmpw ED, %bx
	je write_ed

	cmpw TF, %bx
	je write_tf

	cmpw A0, %bx
	je write_a0

	cmpw F1, %bx
	je write_f1

	jmp error_message			# No destination found -> write error

write_c3:
	movq $BUFFER_DATA_OUT, %rdi		# Write destination to output
	addq $7, %rdi
	movq c3, %r12
	movq %r12, (, %rdi, 1)
	jmp package_input

write_ed:							# Write destination to output
	movq $BUFFER_DATA_OUT, %rdi
	addq $7, %rdi
	movq ed, %r12
	movq %r12, (, %rdi, 1)
	jmp package_input

write_tf:							# Write destination to output
	movq $BUFFER_DATA_OUT, %rdi
	addq $7, %rdi
	movq tf, %r12
	movq %r12, (, %rdi, 1)
	jmp package_input

write_a0:							# Write destination to output
	movq $BUFFER_DATA_OUT, %rdi
	addq $7, %rdi
	movq a0, %r12
	movq %r12, (, %rdi, 1)
	jmp package_input

write_f1:							# Write destination to output
	movq $BUFFER_DATA_OUT, %rdi
	addq $7, %rdi
	movq f1, %r12
	movq %r12, (, %rdi, 1)
	jmp package_input

package_input:
	# Read byte for package type codes
	movq $BUFFER_DATA_IN, %rdi
	addq $4, %rdi				# Add 2 to current offset for next 2 bytes
	movw (, %rdi, 1), %bx

	cmpw E7, %bx				# Check for package type codes
	je write_e7

	cmpw D1, %bx
	je write_d1

	cmpw VA, %bx
	je write_va

	cmpw F8, %bx
	je write_f8

	jmp error_message
write_e7:
	movq $BUFFER_DATA_OUT, %rdi
	addq $15, %rdi
	movq e7, %r12
	movq %r12, (, %rdi, 1)
	jmp count_input

write_d1:
	movq $BUFFER_DATA_OUT, %rdi
	addq $15, %rdi
	movq d1, %r12
	movq %r12, (, %rdi, 1)
	jmp count_input

write_va:
	movq $BUFFER_DATA_OUT, %rdi
	addq $15, %rdi
	movq va, %r12
	movq %r12, (, %rdi, 1)
	jmp count_input

write_f8:
	movq $BUFFER_DATA_OUT, %rdi
	addq $15, %rdi
	movq f8, %r12
	movq %r12, (, %rdi, 1)
	jmp count_input

count_input:
	movq $BUFFER_DATA_OUT, %rdi		# Move output buffer start into rdi
	addq $23, %rdi					# Add offset to place at end of already written data
	movq dash, %r12					# Load dash symbol
	movq %r12, (, %rdi, 1)			# Write dash symbol to data

	movq $BUFFER_DATA_IN, %rdi		
	addq $6, %rdi					# Increase offset to read correct bytes
	movw (, %rdi, 1), %bx			# Read two bytes from input

	movq $BUFFER_DATA_OUT, %rdi		# Write input to output and add whitespaces and dash
	addq $24, %rdi					# Add offset to write at correct position
	movw %bx, (, %rdi, 1)			# Write twi bytes into output buffer
	addq $2, %rdi					
	movq wd, %r12					# Write four whitespaces and text into output
	movq %r12, (, %rdi, 1)

height_input:
	movq $BUFFER_DATA_IN, %rdi		# Read from input
	addq $8, %rdi
	movw (, %rdi, 1), %bx

	movq $BUFFER_DATA_OUT, %rdi		# Write input to output and add whitespaces and dash
	addq $31, %rdi
	movw %bx, (, %rdi, 1)
	addq $2, %rdi
	movq wd, %r12
	movq %r12, (, %rdi, 1)

length_input:
	movq $BUFFER_DATA_IN, %rdi		# Read from input
	addq $10, %rdi
	movw (, %rdi, 1), %bx

	movq $BUFFER_DATA_OUT, %rdi		# Write input to output and add whitespaces and dash
	addq $38, %rdi
	movw %bx, (, %rdi, 1)
	addq $2, %rdi
	movq wd, %r12
	movq %r12, (, %rdi, 1)

width_input:
	movq $BUFFER_DATA_IN, %rdi		# Read from input
	addq $12, %rdi
	movw (, %rdi, 1), %bx

	movq $BUFFER_DATA_OUT, %rdi		# Write input to output and add whitespaces and dash
	addq $45, %rdi
	movw %bx, (, %rdi, 1)
	addq $2, %rdi
	movq wd, %r12
	movq %r12, (, %rdi, 1)

weight_input:
	movq $BUFFER_DATA_IN, %rdi		# Read from input
	addq $14, %rdi
	movw (, %rdi, 1), %bx

	movq $BUFFER_DATA_OUT, %rdi		# Write input to output and add whitespaces and dash
	addq $52, %rdi
	movw %bx, (, %rdi, 1)
	addq $2, %rdi
	movq wd, %r12
	movq %r12, (, %rdi, 1)


chcksm_input:

	movq $BUFFER_DATA_IN, %rdi		# Read from input
	addq $16, %rdi
	movq (, %rdi, 1), %r12

	movq $BUFFER_DATA_OUT, %rdi		# Write input to output and add whitespaces and dash
	addq $59, %rdi
	movq %r12, (, %rdi, 1)

write_newline:
	addq $4, %rdi				# Increment output buffer pointer  to not overwrite checksum
	movq nl, %r12				# Load newline symbol into register
	movq %r12, (, %rdi, 1)		# Write newline in output buffer

write_line:	
	### 4) All things are  converted, write line to output and read next line###
	movq ST_FD_OUT(%rbp), %rdi		# Use output file
	movq $BUFFER_DATA_OUT, %rsi		# Location of output data buffer
	movq $BUFFER_SIZE_OUT, %rdx		# Output buffer size, number of bytes to write
	movq $SYS_WRITE, %rax
	syscall
	cmpq %rax, %rdx					# Compare number written to buffer size that should be written
	jne end_loop					# Quit programm if writing was not successfull
	jmp read_loop_begin				# Jump to read new line if writing was okay

	### 5) In case there was a failure, print error for the line into output and read next line ###
error_message:
	movq ST_FD_OUT(%rbp), %rdi	# Get output file 
	movq $errormsg, %rsi		# Load error message location
	movq $ERROR_LEN, %rdx		# Load error message length
	movq $SYS_WRITE, %rax		# Load syswrite number
	syscall						# Write
	###CHECK WRITE SUCCESS###
	cmpq %rax, %rdx             # Compare number read to written
	jne end_loop                # If not the same, terminate program

	jmp read_loop_begin			# Jump to read a new line


end_loop:
	# Close all opened files # 
	movq ST_FD_OUT(%rbp), %rdi
	movq $SYS_CLOSE, %rax
	syscall
close_input:
	movq ST_FD_IN(%rbp), %rdi
	movq $SYS_CLOSE, %rax
	syscall

exit:
	###EXIT###
	movq $SYS_EXIT, %rax
	syscall

##### FUNCTION from_ascii_to_number #####
#
#PURPOSE:   Converts the input of two bytes into a single 8 bit number
#
#INPUT:     The first parameter (r8) is the higher value byte for the number
##          The second parameter (r9) is the lower value byte for the number
#
#OUTPUT:    Writes into rax the converted input in the form of a 8 bit number (0...255 in decimal system)
#
#VARIABLES:
#           %r8			First passed register
#			%r9			Second passed register
#			%r13		Used for converting the upper value
#			%r14		Used for converting the lower value
#			%rax		Return value and sum register for upper and lower value

from_ascii_to_number:
	pushq %rbp			# Save rbp on stack
	movq %rsp, %rbp
	pushq %rbx			# Save rbx on stack
	pushq %r13			# Save used register r13 on stack
	pushq %r14			# Save used register r14 on stack

	movq %r8, %r13			# Load higher value number into register
	subq $ZN_BASE, %r13		# Substrat 0...9 base
	# If A-F >= 10
    # IF 0-9 < 10
	cmpq $10, %r13			# If register is now < 10, it was a number from 0...9
	jb upper_conv_shift		# If it is greater than 10, it was A..F,

upper_conv_af:
	movq %r8, %r13			# Reload value is it is not correct by 0...9 substracted
	subq $AF_BASE, %r13		# Sbstract A...F base
	addq $AF_ADD, %r13		# Add 10 for correct DEC number representation

upper_conv_shift:		
	sal $4, %r13			# Shift bits into place as they (four leading bits) --> xyza 0000 instead of ???? xyza

conv_lower:
	movq %r9, %r14			# Do the same procedure for lower value, but to not shift it, just add
	subq $ZN_BASE, %r14

	cmpq $10, %r14
	jb conv_lower_add

conv_lower_hex:
	movq %r9, %r14
	sub $AF_BASE, %r14
	addq $AF_ADD, %r14

conv_lower_add:
	addq %r14, %r13
	movq $0, %rax
	movq %r13, %rax

end_convert_function:
	popq %r14				# Restore previous register values
	popq %r13
	popq %rbx
	movq %rbp, %rsp
	popq %rbp
	ret
