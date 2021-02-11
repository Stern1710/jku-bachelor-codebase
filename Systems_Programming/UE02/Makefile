all: clean decode

clean:
	rm -f exit decode toupper
	rm -f *.o

decode: decode.s
	as -o decode.o decode.s --gstabs+
	ld -o decode decode.o

toupper: toupper.s
	as -o toupper.o toupper.s --gstabs+
	ld -o toupper toupper.o
 