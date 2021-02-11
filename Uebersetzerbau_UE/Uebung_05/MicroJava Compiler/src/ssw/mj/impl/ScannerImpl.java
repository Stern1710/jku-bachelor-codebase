package ssw.mj.impl;

import ssw.mj.Scanner;
import ssw.mj.Token;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import static ssw.mj.Errors.Message.*;
import static ssw.mj.Token.Kind.*;

public final class ScannerImpl extends Scanner {

    private static final Map<String, Token.Kind> keywords = new HashMap<>();

    static {
        keywords.put("break", break_);
        keywords.put("class", class_);
        keywords.put("else", else_);
        keywords.put("final", final_);
        keywords.put("if", if_);
        keywords.put("new", new_);
        keywords.put("print", print);
        keywords.put("program", program);
        keywords.put("read", read);
        keywords.put("return", return_);
        keywords.put("void", void_);
        keywords.put("while", while_);
        keywords.put("loop", loop_);
    }

    public ScannerImpl(Reader r) {
        super(r);
        line = 1;
        col = 0;
        nextCh();
    }

    /**
     * Returns next token. To be used by parser.
     */
    @Override
    public Token next() {
        while (Character.isWhitespace(ch)) {
            nextCh();
        }

        Token t = new Token(none, line, col);

        switch (ch) {
            case 'a':
            case 'b':
            case 'c':
            case 'd':
            case 'e':
            case 'f':
            case 'g':
            case 'h':
            case 'i':
            case 'j':
            case 'k':
            case 'l':
            case 'm':
            case 'n':
            case 'o':
            case 'p':
            case 'q':
            case 'r':
            case 's':
            case 't':
            case 'u':
            case 'v':
            case 'w':
            case 'x':
            case 'y':
            case 'z':
            case 'A':
            case 'B':
            case 'C':
            case 'D':
            case 'E':
            case 'F':
            case 'G':
            case 'H':
            case 'I':
            case 'J':
            case 'K':
            case 'L':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'S':
            case 'T':
            case 'U':
            case 'V':
            case 'W':
            case 'X':
            case 'Y':
            case 'Z':
                readName(t);
                break;

            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                readNumber(t);
                break;

            case '\'':
                t.kind = charConst;
                readCharConst(t);
                break;

            case EOF:
                t.kind = eof;
                break;

            case '/':
                nextCh();
                if (ch == '*') {
                    skipComment(t);
                    t = next();
                } else if (ch == '=') {
                    t.kind = slashas;
                    nextCh();
                } else {
                    t.kind = slash;
                }
                break;

            case '-':
                nextCh();
                if (ch == '-') {
                    t.kind = mminus;
                    nextCh();
                } else if (ch == '=') {
                    t.kind = minusas;
                    nextCh();
                } else {
                    t.kind = minus;
                }
                break;

            case '+':
                nextCh();
                if (ch == '=') {
                    t.kind = plusas;
                    nextCh();
                } else if (ch == '+') {
                    t.kind = pplus;
                    nextCh();
                } else {
                    t.kind = plus;
                }
                break;

            case '*':
                nextCh();
                if (ch == '=') {
                    t.kind = timesas;
                    nextCh();
                } else {
                    t.kind = times;
                }
                break;

            case '%':
                nextCh();
                if (ch == '=') {
                    t.kind = remas;
                    nextCh();
                } else {
                    t.kind = rem;
                }
                break;

            case '<':
                nextCh();
                if (ch == '=') {
                    t.kind = leq;
                    nextCh();
                } else {
                    t.kind = lss;
                }
                break;

            case '>':
                nextCh();
                if (ch == '=') {
                    t.kind = geq;
                    nextCh();
                } else {
                    t.kind = gtr;
                }
                break;

            case '=':
                nextCh();
                if (ch == '=') {
                    t.kind = eql;
                    nextCh();
                } else {
                    t.kind = assign;
                }
                break;

            case '&':
                nextCh();
                if (ch == '&') {
                    t.kind = and;
                    nextCh();
                } else {
                    error(t, INVALID_CHAR, '&');
                }
                break;

            case '|':
                nextCh();
                if (ch == '|') {
                    t.kind = or;
                    nextCh();
                } else {
                    error(t, INVALID_CHAR, '|');
                }
                break;

            case '!':
                nextCh();
                if (ch == '=') {
                    t.kind = neq;
                    nextCh();
                } else {
                    error(t, INVALID_CHAR, '!');
                }
                break;

            case ';':
                t.kind = semicolon;
                nextCh();
                break;

            case ':':
                t.kind = colon;
                nextCh();
                break;

            case ',':
                t.kind = comma;
                nextCh();
                break;

            case '.':
                t.kind = period;
                nextCh();
                break;

            case '(':
                t.kind = lpar;
                nextCh();
                break;

            case ')':
                t.kind = rpar;
                nextCh();
                break;

            case '[':
                t.kind = lbrack;
                nextCh();
                break;

            case ']':
                t.kind = rbrack;
                nextCh();
                break;

            case '{':
                t.kind = lbrace;
                nextCh();
                break;

            case '}':
                t.kind = rbrace;
                nextCh();
                break;

            default:
                error(t, INVALID_CHAR, ch);
                nextCh();
                break;
        }

        return t;
    }

    private void nextCh() {
        try {
            ch = (char) in.read();
            col++;

            if (ch == '\r') {
                nextCh();
            } else if (ch == LF) {
                col = 0;
                line++;
            }
            if (ch == EOF) {
                col--; //Sub one for EOF as +1 is added unnecasserily
            }
        } catch (IOException e) {
            ch = EOF;
        }
    }

    private void readName(Token t) {
        StringBuilder bd = new StringBuilder();

        do {
            bd.append(ch);
            nextCh();
        } while (isLetter(ch) || isDigit(ch) || ch == '_');

        String represent = bd.toString();
        if (keywords.containsKey(represent)) {
            t.kind = keywords.get(represent);
        } else {
            t.kind = ident;
            t.str = represent;
        }
    }

    private void readNumber(Token t) {
        StringBuilder bd = new StringBuilder();

        do {
            bd.append(ch);
            nextCh();
        } while (isDigit(ch));

        t.kind = number;
        String represent = bd.toString();
        t.str = represent;
        try {
            t.val = Integer.parseInt(represent);
        } catch (NumberFormatException ex) {
            error(t, BIG_NUM, represent);
        }

    }

    private void readCharConst(Token t) {
        nextCh(); //Fetch next character

        if (ch == '\'') {
            error(t, EMPTY_CHARCONST);
            nextCh();
        } else if (ch == LF) {
            error(t, ILLEGAL_LINE_END);
            nextCh();
        } else if (ch == '\\') {
            nextCh();
            if (!(ch == 'n' || ch == 'r' || ch == '\\' || ch == '\'')) {
                error(t, UNDEFINED_ESCAPE, ch);
            } else {
                if (ch == 'n') {
                    t.val = '\n';
                } else if (ch == 'r') {
                    t.val = '\r';
                } else if (ch == '\\') {
                    t.val = '\\';
                } else {
                    t.val = '\'';
                }
            }
            nextCh();
            if (ch != '\'') {
                error(t, MISSING_QUOTE);
            } else {
                nextCh();
            }
        } else {
            char tempCh = ch;
            nextCh();

            if (ch != '\'') {
                error(t, MISSING_QUOTE);
            } else {
                t.val = tempCh;
                nextCh();
            }
        }
    }

    private void skipComment(Token t) {
        int count = 1;
        char last = ' ';

        nextCh(); //Skip one character to avoid /*/ false recognition
        while (count > 0 && ch != EOF) {
            last = ch;
            nextCh();

            if (last == '/' && ch == '*') {
                count++;
                nextCh();
            } else if (last == '*' && ch == '/') {
                count--;
                nextCh();
            }
        }
        if (count > 0) {
            error(t, EOF_IN_COMMENT);
        }

    }

    //Probably implement own isLetter and isDigit here
    private boolean isLetter(char ch) {
        return (('a' <= ch) && (ch <= 'z')) | (('A' <= ch) && (ch <= 'Z'));
    }

    private boolean isDigit(char ch) {
        return ('0' <= ch) && (ch <= '9');
    }
}
