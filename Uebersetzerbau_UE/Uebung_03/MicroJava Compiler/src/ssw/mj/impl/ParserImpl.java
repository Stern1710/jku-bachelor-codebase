package ssw.mj.impl;

import ssw.mj.Parser;
import ssw.mj.Scanner;
import ssw.mj.Token;

import java.util.EnumSet;

import static ssw.mj.Errors.Message.*;
import static ssw.mj.Token.Kind.*;

public final class ParserImpl extends Parser {

    private static final EnumSet<Token.Kind> firstOfStatement = EnumSet.of(ident, if_, loop_, while_, break_, return_, read,
            print, lbrace, semicolon);
    private static final EnumSet<Token.Kind> firstOfAssignOp = EnumSet.of(assign, plusas, minusas, timesas, slashas, remas);
    private static final EnumSet<Token.Kind> firstOfRelop = EnumSet.of(eql, neq, gtr, geq, lss, leq);
    private static final EnumSet<Token.Kind> firstOfExpr = EnumSet.of(minus, ident, number, charConst, new_, lpar);
    private static final EnumSet<Token.Kind> firstOfMulOp = EnumSet.of(times, slash, rem);


    // TODO Exercise 3 - 6: implementation of parser
    public ParserImpl(Scanner scanner) {
        super(scanner);
    }

    /**
     * Starts the analysis.
     */
    @Override
    public void parse() {
        scan();
        program();
        check(eof);
    }

    private void scan() {
        t = la;
        la = scanner.next();
        sym = la.kind;
    }

    private void check(Token.Kind expected) {
        if (sym == expected) {
            scan();
        } else {
            error(TOKEN_EXPECTED, expected);
        }
    }

    private void program() {
        check(program);
        check(ident);

        while (true) {
            if (sym == final_) { //First(ConstDecl) = "final"
                constDecl();
            } else if (sym == ident) { //First(VarDecl) = First(Type) = ident
                varDecl();
            } else if (sym == class_) { //First(ClassDecl) = "class"
                classDecl();
            } else {
                break; //Exit the loop
            }
        }

        check(lbrace);
        while (sym == ident || sym == void_) { //First(MethodDecl) = First(Type), "void" = ident, void
            methodDecl();
        }
        check(rbrace);
    }

    private void constDecl() {
        check(final_);
        type();
        check(ident);
        check(assign);

        if (sym == number || sym == charConst) {
            scan();
        } else {
            error(CONST_DECL);
        }

        check(semicolon);
    }

    private void varDecl() {
        type();
        check(ident);

        while (sym == comma) { //Check for ",", ident
            scan();
            check(ident);
        }

        check(semicolon);
    }

    private void classDecl() {
        check(class_);
        check(ident);
        check(lbrace);

        while (sym == ident) { //First(VarDecl) = ident
            varDecl();
        }

        check(rbrace);
    }

    private void methodDecl() {
        if (sym == ident) { //First(Type) = ident
            type();
        } else if (sym == void_) {
            scan();
        } else {
            error(METH_DECL);
        }

        check(ident);
        check(lpar);
        if (sym == ident) { //First(FormPars) = First(Type) = ident
            formPars();
        }
        check(rpar);

        //First(VarDecl) = ident
        while (sym == ident) {
            varDecl();
        }
        block();
    }

    private void formPars() {
        type();
        check(ident);

        while (sym == comma) {
            scan();
            type();
            check(ident);
        }
    }

    private void type() {
        check(ident);
        if (sym == lbrack) { //Optional [] after ident
            scan();
            check(rbrack);
        }
    }

    private void block() {
        check(lbrace);
        while (firstOfStatement.contains(sym)) { //Check for first symbol of statement
            statement();
        }
        check(rbrace);
    }

    private void statement() {
        switch (sym) {
            case ident: //First(Designator) = ident
                designator();

                if (firstOfAssignOp.contains(sym)) { //Use if first to check the first of AssignOp
                    assignOp();
                    expr();
                } else { //Go to faster switch if it wasn't in AssignOp
                    switch (sym) {
                        case lpar:
                            actPars();
                            break;

                        case pplus:
                        case mminus:
                            scan();
                            break;

                        default:
                            error(DESIGN_FOLLOW);
                            break;
                    }
                }
                check(semicolon);
                break;

            case if_:
                scan();
                check(lpar);
                condition();
                check(rpar);
                statement();

                if (sym == else_) {
                    scan();
                    statement();
                }
                break;

            case loop_:
                scan();
                check(ident);
                check(colon); //FallThrough Case to while (this was the optional start)

            case while_:
                scan();
                check(lpar);
                condition();
                check(rpar);
                statement();
                break;

            case break_:
                scan();
                if (sym == ident) {
                    scan();
                }
                check(semicolon);
                break;

            case return_:
                scan();
                if (firstOfExpr.contains(sym)) {//Check for First(Expr)
                    expr();
                }
                check(semicolon);
                break;

            case read:
                scan();
                check(lpar);
                designator();
                check(rpar);
                check(semicolon);
                break;

            case print:
                scan();
                check(lpar);
                expr();

                if (sym == comma) {
                    scan();
                    check(number);
                }

                check(rpar);
                check(semicolon);
                break;

            case lbrace: //First(Block) = {
                block();
                break;

            case semicolon:
                scan();
                break;

            default:
                error(INVALID_STAT);
                break;
        }
    }

    private void assignOp() {
        if (firstOfAssignOp.contains(sym)) {
            scan();
        } else {
            error(ASSIGN_OP);
        }
    }

    private void actPars() {
        check(lpar);
        if (firstOfExpr.contains(sym)) { //First(Expr) = -, First(Term) = {-, ident, number, charConst, "new", "("}
            expr();
            while (sym == comma) {
                scan();
                expr();
            }
        }
        check(rpar);
    }

    private void condition() {
        condTerm();
        while (sym == or) {
            scan();
            condTerm();
        }
    }

    private void condTerm() {
        condFact();
        while (sym == and) {
            scan();
            condFact();
        }
    }

    private void condFact() {
        expr();
        relop();
        expr();
    }

    private void relop() {
        if (firstOfRelop.contains(sym)) {
            scan();
        } else {
            error(REL_OP);
        }
    }

    private void expr() {
        if (sym == minus) {
            scan(); //Just go to the next symbol
        }
        term();

        while (sym == plus || sym == minus) { //First(AddOp) = +, -
            addOp(); //Theoretically a scan() would be enough, but maybe AddOp changes --> Would need more changes here
            term();
        }
    }

    private void term() {
        factor();
        while (firstOfMulOp.contains(sym)) { //First(MulOp) = *, /, %
            mulOp(); //Theoretically a scan() would be enough, but maybe MulOp changes --> Would need more changes here
            factor();
        }
    }

    private void factor() {
        switch (sym) {
            case ident:
                designator();
                if (sym == lpar) { //First(ActPars) = (
                    actPars();
                }
                break;

            case number:
            case charConst:
                scan(); //Recognize, just read the next one
                break;

            case new_:
                scan();
                check(ident);
                if (sym == lbrack) {
                    scan();
                    expr();
                    check(rbrack);
                }
                break;

            case lpar:
                scan();
                expr();
                check(rpar);
                break;

            default:
                error(INVALID_FACT);
                break;
        }
    }

    private void designator() {
        check(ident);

        while (true) {
            if (sym == period) {
                scan();
                check(ident);
            } else if (sym == lbrack) {
                scan();
                expr();
                check(rbrack);
            } else {
                break; //Break the loop as no correct symbol was found
            }
        }
    }

    private void addOp() {
        if (sym == plus || sym == minus) {
            scan();
        } else {
            error(ADD_OP);
        }
    }

    private void mulOp() {
        if (firstOfMulOp.contains(sym)) {
            scan();
        } else {
            error(MUL_OP);
        }
    }
}
