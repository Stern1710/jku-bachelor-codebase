all: clean average

clean:
	rm -f exit average
	rm -f *.o

average: average.s
	as -o average.o average.s --gstabs+
	ld -o average average.o
 