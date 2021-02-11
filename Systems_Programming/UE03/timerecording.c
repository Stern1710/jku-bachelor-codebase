/* timerecording.c
 * Simple working time recording for students with user-selectable statistics
 */

#include <stdio.h>
#include <stdlib.h>
#include "timerecording.h"

/**
 * Print all days of the week, starting with day 1 (index 0) and counting up to DAYS
 * Enables the user input for work hours for every student on each day.
 * Uses the account-array declared in timerrecording.h
 * Uses the values of the constants declared in timerecording.h
 * The function does not have a return value as all work is directly written to the standard output
 */
void getStudentHours(void) {
    int i=0;
    while (i < DAYS) {
        printf("\nStart input for timerecording\n"
               "Day %d of week\n"
               "-------------------\n", i+1);
        int j=0;
        while (j < STUDENTS) {
            int hours, acceptHours = 0;
            do {
                printf("\nHours worked by student no. %i: ", j+1);
                scanf("%d", &hours);
                printf("%d", hours);

                if (hours < 0){
                    printf("\nNo negative hours!");
                } else if (hours > 24)  {
                    printf("\nA day has only 24 hours!");
                } else {
                    account[j][i] = hours;
                    acceptHours = 1;
                }
            } while (!acceptHours);

            j++;
        }
        printf("\n");
        i++;
    }
}

/**
 * Prints the menu with its six choices.
 * Reads a single char (1 byte) from the input and evaluates if it is a plausible input.
 * If yes, return that character, else prompt the user to enter a new number.
 * @return The valid choice in the range of 1 to 6 that is used further on
 */
int menuChoice(void) {
    int choice = 0;
    int choiceAccept = 0;
    do {
        printf("\n\nChoice -------------------------\n"
           "  1 Sum of hour per student/week\n"
           "  2 Average hours/day/student\n"
           "  3 Average hours of all students/day\n"
           "  4 Hours of all students/week\n"
           "  5 Select a student\n"
           "  6 END of PGM\n");

        printf("\nYour choice : ");
        scanf("%d", &choice);
        printf("%d", choice);

        if (0 < choice && choice <= 6) {
            choiceAccept = 1;
        } else {
            printf("\nUnknown selection!\n");
        }
    } while (!choiceAccept);

    return choice;
}

/**
 * Calculates the worked time over a week for each student over all weeks
 * Prints the weekly hours for a single student after each other before
 * moving on to the next student.
 * Uses the account-array declared in timerrecording.h
 * Uses the values of the constants declared in timerecording.h
 * The function does not have a return value as all work is directly written to the standard output
*/
void WorkingTime (void) {
    int i=0;
    while (i < STUDENTS) {
        printf("\nWorking time per week - Student No. %d\n"
               "-------------------------------------\n", i+1);

        int j=0, dummy=0;
        while (j < DAYS) {
            printf("|%d hours", account[i][j]);
            dummy = dummy + account[i][j];
            j++;
        }
        printf("| = Cpl. %d hours\n", dummy);

        i++;
    }
    printf("\n"); /*For exact output matching */
}

/**
 * Calculates the average working hours for a student over a week.
 * The average is calculated by summing up all daily working hours and dividing them by the number of days in a week.
 * Uses the account-array declared in timerrecording.h
 * Uses the values of the constants declared in timerecording.h
 * The function does not have a return value as all work is directly written to the standard output.
 */
void StudentDayAverage (void) {
    int i=0;
    while (i < STUDENTS) {
        printf("\nAverage time per day/week per student: %d\n"
               "-------------------------------------------\n", i+1);

        double dummy=0;
        int j=0;
        while (j < DAYS) {
            dummy = dummy + account[i][j];
            j++;
        }

        printf("Average of student %d per day: %.2lf hours/day\n", i+1, dummy / DAYS);
        i++;
    }
    printf("\n");
}

/**
 * Calculates the average work hours for each day of the week over all students.
 * Sums up the students work hours for a day and prints it.
 * Uses the account-array declared in timerrecording.h
 * Uses the values of the constants declared in timerecording.h
 * The function does not have a return value as all work is directly written to the standard output.
 */
void TeamDayAverage (void) {
    int i=0;
    while (i < DAYS) {
        double dummy=0;
        int j=0;
        while(j < STUDENTS) {
            dummy = dummy + account[j][i];
            j++;
        }
        i++;
        printf("\nAverage working time of all students per day on day %d = %.2lf hours", i, dummy / STUDENTS);
    }
    printf("\n");
}

/**
 * Calculates how many hours in total the students have worked in a week.
 * Summs up all hours of a week by all students and prints the value.
 * Uses the account-array declared in timerrecording.h
 * Uses the values of the constants declared in timerecording.h
 * The function does not have a return value as all work is directly written to the standard output.
 */
void TeamWeekHour(void) {
    printf("\nTotal hours of all students per week\n"
           "-----------------------------------------\n");

    int dummy = 0;
    int i=0;
    while (i < DAYS) {
        int j=0;
        while (j < STUDENTS) {
            dummy = dummy + account[j][i];
            j++;
        }
        i++;
    }

    printf("Total hours of all students this week:  %d hours.\n", dummy);
}

/**
 * Prints the working hours for a specific student on a specific day.
 * Reads a student number and the day number from the input and checks if it is a valid input
 * If yes, print the requested values, else print a error and quit.
 * Uses the account-array declared in timerrecording.h
 * Uses the values of the constants declared in timerecording.h
 * The function does not have a return value as all work is directly written to the standard output.
 */
void StudentHourOverview(void) {
    int stud=0, day=0;

    printf("\nWhich Student: ");
    scanf("%d", &stud);
    printf("%d", stud);
    if (stud <= 0) {
        printf("\nOnly positive student numbers allowed");
    } else if (stud > STUDENTS) {
        printf("\nThis university has only %d students", STUDENTS);
    } else {
        printf("\nWhich day: ");
        scanf("%d", &day);
        printf("%d", day);
        if (day <= 0) {
            printf("\nOnly positive day numbers allowed");
        } else if (day > DAYS) {
            printf("\nOnly %d days have been recorded", DAYS);
        } else {
            printf("\nStudent no. %d has worked on day %d: %d hours!\n", stud, day, account[stud - 1][day - 1]);
        }
    }

    printf("\n");
}

/**
 * Main function of the program.
 * Calls all auxilery functions needed for data input and printing.
 * Contains only minimal logic aside from the core switch-statement as all other functionality
 * is divided into several other functions.
 * Contains the main do-while loop running until a 6 was read from they standard input
 * @return 0 after the user input is a 6
 */
int main (void) {
    int keepGoing = 1;

    getStudentHours();
    do {
        switch (menuChoice()) {
            case 1:
                WorkingTime();
                break;
            case 2:
                StudentDayAverage();
                break;
            case 3:
                TeamDayAverage();
                break;
            case 4:
                TeamWeekHour();
                break;
            case 5:
                StudentHourOverview();
                break;
            case 6:
                keepGoing = 0;
                printf("\n");
                break;
        }
    } while(keepGoing);

    return 0;
}
