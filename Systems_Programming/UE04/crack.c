/**
 * Password cracking program
 *
 * System oriented programming, WS 2019
 */

/* You can use the GNU extensions */
#define _GNU_SOURCE

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <time.h>
/* Speical import: Hashing functions */
#include <openssl/sha.h>

/* Default size of PW array */
#define PW_ARRAY_DEF_SIZE 4

/* Return values of the program */
#define SUCCESS				0
#define INVALID_PARAM_COUNT		1
#define INVALID_PARAM			2
#define PASSWORD_FILE_NOT_FOUND		3
#define PASSWORD_FILE_READ_ERROR	4
#define MEMORY_ERROR			5
#define INVALID_FILE_FORMAT		6

/* Limits on the minimum and maximum values of the lower/upper bound of password length.
Obvioulsy the upper bound must be equal or larger than the lower bound as well! */
#define MINIMUM_LOWER	1 /* Lower bound must be between 1 and 4 */
#define MINIMUM_UPPER	4
#define MAXIMUM_LOWER	1 /* Upper bound must be between 1 and 5 */
#define MAXIMUM_UPPER	5

/* Helper function to automatically calculate the maximum of two values */
#define MAX(a,b) (((a)>(b))?(a):(b))

/* Internal encoding of the hash algorithms we support */
#define HASH_ALG_SHA1	1
#define HASH_ALG_SHA256	2
/* Maximum length for the hash value. This allows us to create a buffer on the stack and avoid having to malloc it.
Note: In "modern" C you could easily create an array of dynamic (=runtime-calculated) length on the stack.
But ANSI-C requires arrays to have a static (compile-time) length! */
#define MAX_HASH_LEN MAX(SHA_DIGEST_LENGTH,SHA256_DIGEST_LENGTH)

/* After how many passwords we print the current state */
#define DUMP_COUNT 1000

/* Length of your salt in bytes */
#define SALT_LEN 10
/* A new datatype for our salt */
typedef unsigned char Salt[SALT_LEN];

/* How to store the passwords in memory: Username and password are strings (zero-terminated), hash and salt are arrays of
"fixed" (salt: SALT_LEN, hash: depends on algorithm) length and NOT zero-terminated! algorithm is the internal representation
(see above) of the algorithm used */
typedef struct {
    char *username;	/* Username; zero-terminated. Used only for displaying */
    unsigned char *hash;	/* NOT \0-terminated (already binary)! Length depends on algorithm */
    char *password;	/* Cracked passsword or NULL if not yet discovered */
    int algorithm;	/* Internal numbering; see constants above */
    Salt salt;	/* NOT \0-terminated! */
} PasswordEntry;

/* The character set we use for generating passwords. Note that this is not "define" but a global variable. */
static const char passwordCharacterSet[]="0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";


/* Prototypes for the functions below. Documentation is at the implementation. */
int checkArgumentCount (int argc);
int checkArgumentValidity (int argc, char *argv[]);
int createPassword (int argc, char *argv[]);
int breakPassword (int argc, char *argv[]);
void convertToSalt(char *userSalt, Salt *salt);
void convertToHex(unsigned char *hash, int hashLen, unsigned char *hex);
void sortPasswords(PasswordEntry *entries, int entriesLen);
void swap (PasswordEntry *one, PasswordEntry *two);
int compareHashes (unsigned char *hash1, unsigned char *hash2, int len);
void bruteForce (PasswordEntry *entries, int entriesLen, int minPwLen, int maxPwLen) ;
int checkBruteAgainstEntry (PasswordEntry *entry, char *brute, int bruteLen);
int increaseCounter (int *charCounterSet, int incPos, int maxNum);
void printSHAOutput(char *method, char *salt, unsigned char *hash, char *user, int hashLength);
void printAllOutput(PasswordEntry *entries, int entriesLen);
void printSingleOutput(PasswordEntry entry);


/* Main program */
int main (int argc, char *argv[]) {
    if (checkArgumentCount(argc)) {
        if (checkArgumentValidity(argc, argv)) {
            /*Command line arguments are okay so far*/
            if (!strcmp(argv[1], "-c")) {
                /* Perform actions for creating */
                return createPassword(argc, argv);
            } else {
                /* Perform actions for breaking */
                return breakPassword(argc, argv);
            }
        } else {
            /* Error with the arguments */
            return INVALID_PARAM;
        }
    } else {
        /* Too many or too few arguments */
        return INVALID_PARAM_COUNT;
    }
}

/*
    Checks if the correct number of command line arguments was passed to the program.
*/
int checkArgumentCount (int argc) {
    if (argc < 5 || argc > 6) {
        return 0;
    }
    return 1;
}

/*
 Checks all command line arguments if they are valid and in the correct position
*/
int checkArgumentValidity (int argc, char *argv[]) {
    if (((int) strlen(argv[1])) != 2) {
        return 0;
    }
    if (strcmp(argv[1], "-b") && strcmp(argv[1], "-c")) {
        return 0;
    }

    /* Check all params for create option*/
    if (!strcmp(argv[1], "-c")) {
        if (((int) strlen(argv[2])) < 1) {
            return 0;
        }
        if (((int) strlen(argv[3])) <  MINIMUM_LOWER || ((int) strlen(argv[3])) > MAXIMUM_UPPER) {
            return 0;
        }
        if (strcmp(argv[4], "SHA1") && strcmp(argv[4], "SHA2")) {
            return 0;
        }

        if (argc > 5 && ((int) strlen(argv[5])) > 20) {
            return 0;
        } else if (argc > 5 && ((int) strlen(argv[5])) % 2 != 0) {
            return 0;
        }
    }
    /* Check all params for break option*/
    else if (!strcmp(argv[1], "-b")) {
        int minPos = 0, maxPos = 0;
        int min, max;

        if (!strcmp(argv[2], "-t")) {
            minPos = 4;
            maxPos = 5;
        } else {
            minPos = 3;
            maxPos = 4;
        }

        if ((int) strlen(argv[minPos]) > 1 || (int) strlen(argv[maxPos]) > 1) {
            return 0;
        }

        /* Read min and max as integer */
        sscanf(argv[minPos], "%d", &min);
        sscanf(argv[maxPos], "%d", &max);

        if (min < MINIMUM_LOWER || MINIMUM_UPPER < min) {
            return 0;
        }
        if (max < MAXIMUM_LOWER || MAXIMUM_UPPER < max) {
            return 0;
        }
        if (min > max) {
            return 0;
        }
    }

    return 1;
}

/*
 Creates a password in relation to the
*/
int createPassword (int argc, char *argv[]) {
    /* Setup the hash code */
    char *saltCode = (char *) malloc(SALT_LEN*2*sizeof(char));
    unsigned char *toHash;
    unsigned char *pointerToHash;
    unsigned char md[MAX_HASH_LEN];
    char c;
    int hashCounter = 0;
    int i=0;
    int j=0;
    int toHashLen;
    Salt salt;

    if (saltCode == NULL) {
        return MEMORY_ERROR;
    }

    /* Prepare user input salt */
    if (argc > 5) {
        int lenUserHash = ((int) strlen(argv[5]));
        while (hashCounter < lenUserHash) {
            c = argv[5][hashCounter];
            if (c >= 97 && c <= 122) {
                c = c - 32;
            }
            saltCode[hashCounter] = c;
            hashCounter++;
        }
    }
    while (hashCounter < SALT_LEN*2) {
        saltCode[hashCounter] = '0';
        hashCounter++;
    }
    /* Converts the up to 20 digit user input salt into a 10 character salt */
    convertToSalt (saltCode, &salt);

    toHashLen = SALT_LEN + 1 + ((int)strlen(argv[3]));
    toHash = (unsigned char *) malloc(toHashLen*sizeof(unsigned char));

    if (toHash == NULL) {
        return MEMORY_ERROR;
    }

    /* Prepare data for hashing */
    while (i < SALT_LEN) {
        toHash[i] = salt[i];
        i++;
    }

    toHash[i] = '|';
    i++;

    while (i < toHashLen) {
        toHash[i] = argv[3][j];
        i++;
        j++;
    }

    /* Decide which algorithm to use based on already checked user input */
    if (!strcmp(argv[4], "SHA1")) {
        /* Use SHA1 algorithm */
        pointerToHash = SHA1(toHash, toHashLen, md);
        printSHAOutput(argv[4], saltCode, pointerToHash, argv[2], SHA_DIGEST_LENGTH);

    } else {
        /* Use SHA256 algorithm */
        pointerToHash = SHA256(toHash, toHashLen, md);
        printSHAOutput(argv[4], saltCode, pointerToHash, argv[2], SHA256_DIGEST_LENGTH);
    }

    /* Free dynamically allocated space to avoid memory violations and security problems */
    free(toHash);
    free(saltCode);

    return 0;
}

/*
 Breaks password(s) from given file
*/
int breakPassword (int argc, char *argv[]) {
    FILE *inStream;
    char *line;
    /*char *helper;*/
    char shaBuf[4], saltBuf[20];
    int i, minPWLen, maxPWLen;
    int readLen, hashLength, userLength;
    int pwArraySize, curPwCount;
    int doTimeRec;
    size_t len = 0;
    time_t timeStart, timeEnd;
    PasswordEntry *entries = (PasswordEntry *) malloc(PW_ARRAY_DEF_SIZE*sizeof(PasswordEntry));

    if (entries == NULL) {
        return MEMORY_ERROR;
    }

    pwArraySize = PW_ARRAY_DEF_SIZE;

    /* 1) Open file for reading and set min/max PW length*/
    if (!strcmp(argv[2], "-t")) {
        /* second parameter is -t, therefore use 3rd */
        inStream = fopen(argv[3], "r");
        sscanf(argv[4], "%d", &minPWLen);
        sscanf(argv[5], "%d", &maxPWLen);
        doTimeRec = 1;
    } else {
        /* Second parameter is the file name */
        inStream = fopen(argv[2], "r");
        sscanf(argv[3], "%d", &minPWLen);
        sscanf(argv[4], "%d", &maxPWLen);
        doTimeRec = 0;
    }

    if (inStream == NULL) {
        return PASSWORD_FILE_NOT_FOUND;
    }

    /* Read all lines from input and put them into the predefined structure */
    curPwCount = 0;
    while ((readLen = getline(&line, &len, inStream)) != -1) {
        if (curPwCount >= pwArraySize) {
            pwArraySize = 2*pwArraySize;
            entries = realloc(entries, pwArraySize*sizeof(PasswordEntry));
            if (entries == NULL) {
                return MEMORY_ERROR;
            }
        }

        memcpy(shaBuf, &line[0], 4);
        memcpy(saltBuf, &line[5], 20);
        /* Write the data into the data structure*/
        /* Write the new salt */
        convertToSalt(saltBuf, &(entries[curPwCount].salt));

        /* Determine used algorithm and read the hash*/
        if (!strcmp(shaBuf, "SHA1")) {
            entries[curPwCount].algorithm = HASH_ALG_SHA1;
            hashLength = 2*SHA_DIGEST_LENGTH;
        } else if (!strcmp(shaBuf, "SHA2")) {
            entries[curPwCount].algorithm = HASH_ALG_SHA256;
            hashLength = 2*SHA256_DIGEST_LENGTH;
        } else {
            return INVALID_FILE_FORMAT;
        }

        entries[curPwCount].hash = (unsigned char *) malloc(hashLength*sizeof(unsigned char));
        if (entries[curPwCount].hash == NULL) {
            return MEMORY_ERROR;
        }
        memcpy(entries[curPwCount].hash, &line[26], hashLength);

        userLength = (int)strlen(line) - 27 - hashLength;
        entries[curPwCount].username = (char *) malloc(userLength * sizeof(char));
        if (entries[curPwCount].username == NULL) {
            return MEMORY_ERROR;
        }
        memcpy(entries[curPwCount].username, &line[27+hashLength], userLength);
        entries[curPwCount].username[userLength-1] = '\0';

        /* Make sure PW is set to NULL with the little help of my helper-friend */
        entries[curPwCount].password = NULL;
        /*helper = entries[curPwCount].password;
        helper = NULL;
        entries[curPwCount].password = helper;
        */
        curPwCount++;
    }

    /* Closing opened input file */
    fclose(inStream);
    if (line) {
        free(line);
    }

    /* 2) Sort the input */
    sortPasswords(entries, curPwCount);

    /* 3) Do password hashes */
    /* TBH do not know what to do here :(*/

    printf("Hashes to crack:\n");
    printAllOutput(entries, curPwCount);

    /* 4) Optional: Start time recording in case it is needed */
    if (doTimeRec) {
        timeStart = time(NULL);
    }

    /* 5) Cracking Operation */
    /* Try every combination from minPW length to maxPW length against all given hashes */
    bruteForce (entries, curPwCount, minPWLen, maxPWLen);


    /* 6) Optional: Print elapsed  time */
    if (doTimeRec) {
        timeEnd = time(NULL);
        printf("Time elapsed: %ld second(s)\n\n", timeEnd-timeStart);
    }


    /* 7) Print end result that has been calculated */
    printf("End result:\n");
    printAllOutput(entries, curPwCount);

    /* 8) Free dynamically allocated address spaces */

    for (i=0; i < curPwCount; i++) {
        free(entries[i].username);
        free(entries[i].hash);

        if (entries[i].password != NULL && strcmp(entries[i].password, "")) {
            free(entries[i].password);
        }
    }

    free(entries);
    return 0;
}

/*
 Converts the  20 input bytes into the 10 salt bytes.
 Merges two characters (example E1 as 0xE1 to 225) and saves into a char of the Salt
*/
void convertToSalt(char *userSalt, Salt *salt) {
    int hc, sc;
    char c;
    hc = 0;
    sc = 0;

    while (hc < SALT_LEN*2) {
        c = 0;

        if (userSalt[hc] >= 48 && userSalt[hc] <= 57) {
            c = (userSalt[hc]-48) << 4;
        } else {
            c = (userSalt[hc]-55) << 4;
        }
        hc = hc+1;

        if (userSalt[hc] >= 48 && userSalt[hc] <= 57) {
            c = c + (userSalt[hc]-48);
        } else {
            c = c + (userSalt[hc]-55);
        }
        hc = hc+1;

        salt[0][sc] = c;
        sc = sc+1;
    }
}

/*
 Converts two character inputs into a single char representing a value from 0..255
 Every two input characters X,Y are interpreted as 0xXY and therefore converted into
 a decimal number from 0 to 255 and saved into a char.
 hashLen is the length of the hash and hash has double the size of hex.
*/
void convertToHex(unsigned char *hash, int hashLen, unsigned char *hex) {
    char c;
    int hashC, hexC;
    hashC=0;
    hexC=0;

    while (hashC < hashLen) {
        c=0;

        if (hash[hashC] >= 48 && hash[hashC] <= 57) {
            c = (hash[hashC]-48) << 4;
        } else {
            c = (hash[hashC]-55) << 4;
        }
        hashC++;

        if (hash[hashC] >= 48 && hash[hashC] <= 57) {
            c = c + (hash[hashC]-48);
        } else {
            c = c + (hash[hashC]-55);
        }
        hashC++;

        hex[hexC] = c;
        hexC++;
    }

}

/*
 Sorts passwords according the SHA - Salts - Username - Hash
*/
void sortPasswords(PasswordEntry *entries, int entriesLen) {
    int i, j;

    for (i=0; i < entriesLen-1; i++) {
        for (j=0; j < entriesLen-i-1; j++) {
            if (entries[j].algorithm == entries[j+1].algorithm) {
                if (strcmp((const char *)entries[j].salt, (const char *)entries[j+1].salt) == 0) {
                    if (strcmp(entries[j].username, entries[j+1].username) == 0) {
                        if (strcmp((const char *)entries[j].hash, (const char *)entries[j+1].hash) > 0) {
                            swap(&entries[j], &entries[j+1]);
                        }
                    } else if (strcmp(entries[j].username, entries[j+1].username) > 0) {
                        swap(&entries[j], &entries[j+1]);
                    }
                } else if (strcmp((const char *)entries[j].salt, (const char *)entries[j+1].salt) > 0) {
                    swap(&entries[j], &entries[j+1]);
                }
            } else if (entries[j].algorithm > entries[j+1].algorithm) {
                swap(&entries[j], &entries[j+1]);
            }
        }
    }
}

/*
 Swaps to passwords entries from the list
*/
void swap (PasswordEntry *one, PasswordEntry *two) {
    PasswordEntry temp;
    temp = *one;
    *one = *two;
    *two = temp;
}

/*
 Compares the two char arrays character by character
*/
int compareHashes (unsigned char *hash1, unsigned char *hash2, int len) {
    int i;
    for (i=0; i < len; i++) {
        if (hash1[i] != hash2[i]) {
            return 0;
        }
    }
    return 1;
}

/*
 Tries every possible combination of defined numbers and letters with their hash against the given hash
*/
void bruteForce (PasswordEntry *entries, int entriesLen, int minPwLen, int maxPwLen) {
    int triedPW, crackedPW;
    int curPos;
    int assembleCounter, pc, pwc;
    int *charCounterSet;
    char *charSet;

    triedPW = 0;
    crackedPW = 0;
    for (curPos = minPwLen; curPos <= maxPwLen && crackedPW < entriesLen; curPos++) {
        charCounterSet = (int *) malloc (curPos * sizeof(int));
        charSet = (char *) malloc(curPos * sizeof(char));

        if (charCounterSet == NULL || charSet == NULL) {
            return;
        }

        for (assembleCounter=0; assembleCounter < curPos; assembleCounter++) {
            charCounterSet[assembleCounter] = 0;
            charSet[assembleCounter] = ' ';
        }

        do {
            /* Assemble the password with salt to try */
            for (assembleCounter=0; assembleCounter < curPos; assembleCounter++) {
                charSet[assembleCounter] = passwordCharacterSet[charCounterSet[assembleCounter]];
            }
            /* Try Password against all non-cracked PWs*/
            for (pwc=0; pwc < entriesLen; pwc++) {
                if (entries[pwc].password == NULL || !strcmp(entries[pwc].password, "")) {
                    if (checkBruteAgainstEntry(&(entries[pwc]), charSet, curPos) != 0) {
                        crackedPW++;
                    }
                }
            }

            triedPW++;
            /* Print every 1000th try to the output */
            if (triedPW%DUMP_COUNT == 0) {
                printf("%d: ", triedPW);
                for (pc=curPos-1; pc >= 0; pc--) {
                    printf("%c", charSet[pc]);
                }
                printf(" (%d found)\n", crackedPW);
            }
        } while (increaseCounter(charCounterSet, 0, curPos) != 0 && crackedPW < entriesLen);

        free(charSet);
        free(charCounterSet);
    }

    printf("Tried %d passwords, %d cracked\n\n", triedPW, crackedPW);
}

/*
 Checks a brute password against a password entry and returns 1 in case of success
 but 0 in case of a missmatch.
*/
int checkBruteAgainstEntry (PasswordEntry *entry, char *brute, int bruteLen) {
    unsigned char *pointerToHash;
    unsigned char *toHash;
    unsigned char *convHash;
    unsigned char md[MAX_HASH_LEN];
    int toHashLen, hashLen, i, j;

    toHashLen = SALT_LEN + 1 + bruteLen;
    toHash = (unsigned char *) malloc(toHashLen*sizeof(unsigned char));
    if (toHash == NULL) {
        return MEMORY_ERROR;
    }

    /* Prepare with Salt*/
    for(i=0; i < SALT_LEN; i++) {
        toHash[i] = entry->salt[i];
    }
    toHash[i] = '|';
    i++;
    for(j=0; i < toHashLen; i++, j++) {
        toHash[i] = brute[j];
    }


    /* Choose algorithmn and hash */
    if (entry->algorithm == HASH_ALG_SHA1) {
        pointerToHash = SHA1(toHash, toHashLen, md);
        hashLen = SHA_DIGEST_LENGTH;
        convHash = (unsigned char*) malloc (sizeof(unsigned char) * SHA_DIGEST_LENGTH);
        if (convHash == NULL) {
            return MEMORY_ERROR;
        }
        convertToHex(entry->hash, SHA_DIGEST_LENGTH*2, convHash);
    } else {
        pointerToHash = SHA256(toHash, toHashLen, md);
        hashLen = SHA256_DIGEST_LENGTH;
        convHash = (unsigned char*) malloc (sizeof(unsigned char) * SHA256_DIGEST_LENGTH);
        if (convHash == NULL) {
            return MEMORY_ERROR;
        }
        convertToHex(entry->hash, SHA256_DIGEST_LENGTH*2, convHash);
    }

    /* Free used items */
    free(toHash);
    /* Compare the two hases */
    if (compareHashes(pointerToHash, convHash, hashLen)) {
        entry->password = (char *) malloc((bruteLen+1)*sizeof(char));
        if (entry->password == NULL) {
            return MEMORY_ERROR;
        }
        for(i=0; i < bruteLen; i++) {
            entry->password[i] = brute[i];
        }
        entry->password[i] = '\0';
        free(convHash);
        return 1;
    }
    free(convHash);
    return 0;
}

/*
 Increases the counter for array that remembers which character combination to try next.
 Returns 0 if increasing to next position failed (already all combinations tried)
 Returns 1 if a next combination is possible and sets the according indices.
*/
int increaseCounter (int *charCounterSet, int incPos, int maxNum) {
    int maxCharLen;
    maxCharLen = (int)strlen(passwordCharacterSet)-1;

    if (charCounterSet[incPos] >= maxCharLen) {
        if (incPos < maxNum-1) {
            charCounterSet[incPos] = 0;
            return increaseCounter(charCounterSet, ++incPos, maxNum);
        } else {
            return 0;
        }
    } else {
        charCounterSet[incPos]++;
    }

    return 1;
}

/*
 Prints out the values from the hashing function depending on how long the hash is
*/
void printSHAOutput(char *method, char *salt, unsigned char *hash, char *user, int hashLength) {
    int i;

    printf("%s;", method);

    for (i=0; i < strlen(salt); i++) {
        printf("%c", salt[i]);
    }
    printf(";");


    for(i=0; i < hashLength; i++) {
        printf("%02X", hash[i]);
    }
    printf(";%s\n", user);
}

/*
 Prints all outputs for a (cracked) password in the according style.
 Uses the printSingleOutput function for printing a single line
*/
void printAllOutput(PasswordEntry *entries, int entriesLen) {
    int i;
    for (i=0; i < entriesLen; i++) {
        printSingleOutput(entries[i]);
    }
    printf("\n");
}

/*
 Prints a single output line for a (cracked) password in the according style
*/
void printSingleOutput(PasswordEntry entry) {
    int hashLength, i;

    if (entry.algorithm == HASH_ALG_SHA1) {
        hashLength = 2*SHA_DIGEST_LENGTH;
    } else {
        hashLength = 2*SHA256_DIGEST_LENGTH;
    }

    printf("%s: ", entry.username);
    for (i=0; i<hashLength; i++) {
        printf("%c", entry.hash[i]);
    }
    printf(" = ");
    if (entry.password == NULL) {
        printf("???");
    } else {
        printf("%s", entry.password);
    }
    printf(" (");
    if (entry.algorithm == HASH_ALG_SHA1) {
        printf("SHA1/");
    } else {
        printf("SHA2/");
    }
    for(i=0; i<SALT_LEN; i++) {
        printf("%02X", entry.salt[i]);
    }

    printf(")\n");
}
