package ssw.mj.impl;

import ssw.mj.Errors;
import ssw.mj.Parser;
import ssw.mj.Scanner;
import ssw.mj.Token;
import ssw.mj.symtab.Obj;
import ssw.mj.symtab.Struct;
import ssw.mj.symtab.Tab;

import java.util.EnumSet;

import static ssw.mj.Errors.Message.*;
import static ssw.mj.Token.Kind.*;

public final class ParserImpl extends Parser {

    private static final int minErrors = 3; //constant for distance checking to throw errors
    private int errorDist = 3; //Init with a errorDistance >= minErrors

    private static final EnumSet<Token.Kind> followDecl = EnumSet.of(lbrace, final_, class_, eof);
    private static final EnumSet<Token.Kind> followStat = EnumSet.of(rbrace, if_, loop_, while_, break_, return_, read,
            print, semicolon, eof, else_);
    private static final EnumSet<Token.Kind> followMethDecl = EnumSet.of(rbrace, eof, void_);

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
        errorDist++;
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
        Obj insert = tab.insert(Obj.Kind.Prog, t.str, Tab.noType);

        tab.openScope(); //Open level 0 scope

        while (sym != lbrace && sym != eof) {
            if (sym == final_) { //First(ConstDecl) = "final"
                constDecl();
            } else if (sym == ident) { //First(VarDecl) = First(Type) = ident
                varDecl();
            } else if (sym == class_) { //First(ClassDecl) = "class"
                classDecl();
            } else {
                recoverDecl(INVALID_DECL);
            }
        }

        if (tab.curScope.locals().size() > MAX_GLOBALS) {
            error(TOO_MANY_GLOBALS);
        }

        check(lbrace);
        while (sym != rbrace && sym != eof) { //First(MethodDecl) = First(Type), "void" = ident, void
            methodDecl();
        }
        check(rbrace);
        insert.locals = tab.curScope.locals();

        tab.closeScope(); //Close level 0 scope --> Back to universe
    }

    private void constDecl() {
        check(final_);
        StructImpl type = type();
        check(ident);
        String str = t.str;
        check(assign);

        if (sym == number || sym == charConst) {
            if (sym == number && type.kind != Struct.Kind.Int) {
                recoverDecl(CONST_TYPE);
            } else if (sym == charConst && type.kind != Struct.Kind.Char) {
                recoverDecl(CONST_TYPE);
            } else {
                scan();
                Obj obj = tab.insert(Obj.Kind.Con, str, type);
                obj.val = t.val;
            }

        } else {
            error(CONST_DECL);
        }

        check(semicolon);
    }

    private void varDecl() {
        StructImpl type = type();
        check(ident);
        tab.insert(Obj.Kind.Var, t.str, type);

        while (sym == comma) { //Check for ",", ident
            scan();
            check(ident);
            tab.insert(Obj.Kind.Var, t.str, type);
        }

        check(semicolon);
    }

    private void classDecl() {
        check(class_);
        check(ident);
        Obj clazz = tab.insert(Obj.Kind.Type, t.str, new StructImpl(Struct.Kind.Class));

        check(lbrace);
        tab.openScope();

        while (sym == ident) { //First(VarDecl) = ident
            varDecl();
        }

        if (tab.curScope.nVars() > MAX_FIELDS) {
            error(TOO_MANY_FIELDS);
        }
        clazz.type.fields = tab.curScope.locals();
        tab.closeScope();

        check(rbrace);
    }

    private void methodDecl() {
        if (!(sym == ident || sym == void_)) {
            recoverMethodDecl(METH_DECL);
            return;
        }

        StructImpl type = Tab.noType;
        boolean mainRetIdent = false, mainParams = false;

        if (sym == ident) { //First(Type) = ident
            if (!nextTokenIsType()) {
                recoverMethodDecl(METH_DECL);
                return;
            }
            type = type();
            mainRetIdent = true;
        } else if (sym == void_) {
            scan();
        } else {
            recoverMethodDecl(METH_DECL);
            return;
        }
        check(ident);

        String methodName = t.str;

        Obj meth = tab.insert(Obj.Kind.Meth, t.str, type);
        check(lpar);

        tab.openScope();

        if (sym == ident) { //First(FormPars) = First(Type) = ident
            formPars();
            mainParams = true;
        }
        check(rpar);

        meth.nPars = tab.curScope.nVars(); //Only form params in here --> Write to
        if ("main".equals(methodName)) {
            if (mainRetIdent) {
                error(MAIN_NOT_VOID);
            } else if (mainParams) {
                error(MAIN_WITH_PARAMS);
            }
        }

        //First(VarDecl) = ident
        while (sym == ident) {
            varDecl();
        }
        meth.locals = tab.curScope.locals();

        if (tab.curScope.locals().size() > MAX_LOCALS) {
            error(TOO_MANY_LOCALS);
        }
        block();

        tab.closeScope();
    }

    private void formPars() {
        StructImpl type = type();
        check(ident);

        tab.insert(Obj.Kind.Var, t.str, type);
        while (sym == comma) {
            scan();
            type = type();
            check(ident);
            tab.insert(Obj.Kind.Var, t.str, type);
        }
    }

    private StructImpl type() {
        check(ident);

        Obj o = tab.find(t.str);
        if (o.kind != Obj.Kind.Type) {
            error(NO_TYPE);
        }
        StructImpl type = o.type;

        if (sym == lbrack) { //Optional [] after ident
            scan();
            check(rbrack);
            type = new StructImpl(type);
        }

        return type;
    }

    private void block() {
        check(lbrace);

        if (t.kind == lbrace) { //Check for lbrace if error was suppressed by error distance
            while (sym != rbrace && sym != eof) { //Check for first symbol of statement
                statement();
            }
        }
        check(rbrace);
    }

    private void statement() {
        if (!firstOfStatement.contains(sym)) {
            recoverStat(INVALID_STAT);
        }

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
                tab.openScope();
                tab.insert(Obj.Kind.Label, t.str, Tab.noType);
                check(colon);

                check(while_);
                check(lpar);
                condition();
                check(rpar);
                statement();

                tab.closeScope();
                break;

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

                    Obj obj = tab.find(t.str);
                    if (obj.kind != Obj.Kind.Label) {
                        error(NO_LABEL);
                    }
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
    
    /*
    ------------------------------------------------
    ### Recover methods
    ------------------------------------------------
     */

    private void recoverDecl(Errors.Message msg) {
        error(msg);
        do {
            scan();
        }
        while (!followDecl.contains(sym) && !(sym == ident && nextTokenIsType()));
        //Only recover on an ident if it is a token, otherwise keep looking
    }

    private void recoverMethodDecl(Errors.Message msg) {
        error(msg);
        do {
            scan();
        }
        while (!followMethDecl.contains(sym) && !(sym == ident && nextTokenIsType()));
        //Only recover on an ident if it is a token, otherwise keep looking
        if (sym == rbrace) {
            scan(); //Scan away an right brace as the scanner needs to stark after this symbol
        }
    }

    private void recoverStat(Errors.Message msg) {
        error(msg);
        do {
            scan();
        }
        while (!followStat.contains(sym));
    }

    //Overwritten error method to count in the minimum error distance to actually throw errors
    @Override
    public void error(Errors.Message msg, Object... msgParams) {
        if (errorDist >= minErrors) {
            scanner.errors.error(la.line, la.col, msg, msgParams);
        }
        errorDist = 0;
    }

    //Method copied from VL-script
    private boolean nextTokenIsType() {
        if (sym != ident) return false;
        Obj obj = tab.find(la.str);
        return obj.kind == Obj.Kind.Type;
    }
}
