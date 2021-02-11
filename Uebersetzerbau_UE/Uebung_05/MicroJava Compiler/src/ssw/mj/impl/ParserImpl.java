package ssw.mj.impl;

import ssw.mj.Errors;
import ssw.mj.Parser;
import ssw.mj.Scanner;
import ssw.mj.Token;
import ssw.mj.codegen.Operand;
import ssw.mj.symtab.Obj;
import ssw.mj.symtab.Struct;
import ssw.mj.symtab.Tab;

import java.util.EnumSet;

import static ssw.mj.Errors.Message.*;
import static ssw.mj.Token.Kind.*;
import static ssw.mj.codegen.Code.OpCode;

public final class ParserImpl extends Parser {

    private static final int MIN_ERRORS = 3; //constant for distance checking to throw errors
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

        if (tab.curScope.nVars() > MAX_GLOBALS) {
            error(TOO_MANY_GLOBALS);
        }
        code.dataSize = tab.curScope.nVars(); //size of global variables

        check(lbrace);
        while (sym != rbrace && sym != eof) { //First(MethodDecl) = First(Type), "void" = ident, void
            if (sym == ident || sym == void_) {
                methodDecl();
            } else {
                recoverMethodDecl();
            }

        }
        check(rbrace);

        if (code.mainpc == -1) { //If no main was declared (i.e mainpc never set), throw an error
            error(METH_NOT_FOUND, "main");
        }

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
            if ((sym == number && type.kind != Struct.Kind.Int)
                    || (sym == charConst && type.kind != Struct.Kind.Char)) {
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
        StructImpl type = Tab.noType;
        boolean mainRetIdent = false, mainParams = false;

        if (nextTokenIsType()) { //First(Type) = ident
            type = type();
            mainRetIdent = true;
        } else if (sym == void_) {
            scan();
        }
        check(ident);

        String methodName = t.str;
        Obj meth = tab.insert(Obj.Kind.Meth, t.str, type);
        check(lpar);

        tab.openScope();

        if (nextTokenIsType()) { //First(FormPars) = First(Type) = ident
            formPars();
            mainParams = true;
        }
        check(rpar);

        meth.nPars = tab.curScope.nVars(); //Only form params in here --> Write to
        if ("main".equals(methodName)) {
            code.mainpc = code.pc;
            if (mainRetIdent) {
                error(MAIN_NOT_VOID);
            }
            if (mainParams) {
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

        //Set the correct enter parameters for a method
        code.put(OpCode.enter);
        code.put(meth.nPars);
        code.put(tab.curScope.nVars());

        block(); //all statements of the method are in here

        //set the correct return params for 
        if (meth.type == Tab.noType) {
            code.put(OpCode.exit);
            code.put(OpCode.return_);
        } else {
            code.put(OpCode.trap);
            code.put(1);
        }

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
                Operand x = designator();

                if (sym == assign) { //Simple assign with "="
                    assignOp();
                    Operand y = expr();

                    if (x.obj != null && x.obj.kind != Obj.Kind.Var) {
                        error(NO_VAR);
                    }

                    if (y.type.assignableTo(x.type)) {
                        code.assign(x, y);
                    } else {
                        error(INCOMP_TYPES);
                    }
                } else if (firstOfAssignOp.contains(sym)) { //Go into for (+ | - | * | / | %) =; "=" not possible
                    OpCode asOp = OpCode.nop;
                    switch (sym) { //Map the assignOp code to the bytecode
                        case plusas:
                            asOp = OpCode.add;
                            break;
                        case minusas:
                            asOp = OpCode.sub;
                            break;
                        case timesas:
                            asOp = OpCode.mul;
                            break;
                        case slashas:
                            asOp = OpCode.div;
                            break;
                        case remas:
                            asOp = OpCode.rem;
                            break;
                    }

                    assignOp();

                    if (x.obj != null && x.obj.kind != Obj.Kind.Var) {
                        error(NO_VAR);
                    }

                    if (x.kind == Operand.Kind.Fld) {
                        code.put(OpCode.dup);
                    } else if (x.kind == Operand.Kind.Elem) {
                        code.put(OpCode.dup2);
                    }

                    Operand.Kind xKind = x.kind; //remember value of kind
                    code.load(x); //Load the Operand

                    Operand y = expr();

                    if ((x.type != Tab.intType || y.type != Tab.intType)) {
                        error(NO_INT_OP);
                    }
                    if (!y.type.assignableTo(x.type)) {
                        error(INCOMP_TYPES);
                    }

                    code.load(y);
                    code.put(asOp);
                    x.kind = xKind; //Reset the kind as now after the asOp the value is not on the stack anymore
                    code.assign(x);

                } else { //Go to faster switch if it wasn't in AssignOp
                    switch (sym) {
                        case lpar:
                            actPars();
                            break;

                        case pplus:
                        case mminus: //combined case and decide in two places for add/sub, saves duplicated code
                            if (x.type != Tab.intType) {
                                error(NO_INT);
                            }
                            if (x.obj != null && x.obj.kind != Obj.Kind.Var) {
                                error(NO_VAR);
                            }

                            if (x.kind == Operand.Kind.Local) {
                                code.put(OpCode.inc);
                                code.put(x.adr);

                                if (sym == pplus) { //add 1
                                    code.put(1);
                                } else { //sub case, substract 1
                                    code.put(255);
                                }

                            } else {
                                if (x.kind == Operand.Kind.Fld) {
                                    code.put(OpCode.dup);
                                } else if (x.kind == Operand.Kind.Elem) {
                                    code.put(OpCode.dup2);
                                }

                                Operand.Kind xKind = x.kind;
                                code.load(x);
                                code.load(new Operand(1));

                                if (sym == pplus) {
                                    code.put(OpCode.add);
                                } else {
                                    code.put(OpCode.sub);
                                }

                                x.kind = xKind; //reset kind as now it isn't on stack anymore
                                code.assign(x);
                            }
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
                    code.load(expr());
                }
                //Return/Exit/Trap get set in Program accordingly, not being done here
                check(semicolon);
                break;

            case read:
                scan();
                check(lpar);

                x = designator();
                if (x.type == Tab.intType) { //determine for reading int or byte
                    code.put(OpCode.read);
                    code.assign(x, new Operand(Tab.intType)); //Load our designator as and int
                } else if (x.type == Tab.charType) {
                    code.put(OpCode.bread);
                    code.assign(x, new Operand(Tab.charType)); //Load here as char
                } else {
                    error(READ_VALUE);
                }

                check(rpar);
                check(semicolon);
                break;

            case print:
                scan();
                check(lpar);
                x = expr();

                int afterDistance = 0;
                if (sym == comma) {
                    scan();
                    check(number);
                    afterDistance = t.val;
                }

                code.load(x); //load the operand
                code.load(new Operand(afterDistance)); //put number of whitespaces after print on stack

                if (x.type == Tab.charType) { //determine for writing int or byte
                    code.put(OpCode.bprint);
                } else if (x.type == Tab.intType) {
                    code.put(OpCode.print);
                } else {
                    error(PRINT_VALUE);
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

    private Operand expr() {
        boolean wasMinus = false;

        if (sym == minus) {
            scan(); //Just go to the next symbol
            wasMinus = true;
        }

        Operand x = term();

        if (wasMinus) {
            if (x.type != Tab.intType) {
                error(NO_INT_OP);
            }
            if (x.kind == Operand.Kind.Con) {
                x.val = -x.val;
            } else {
                code.load(x);
                code.put(OpCode.neg);
            }
        }

        while (sym == plus || sym == minus) { //First(AddOp) = +, -
            Token.Kind op = sym;
            addOp();

            code.load(x);
            Operand y = term();
            code.load(y);

            if (x.type != Tab.intType || y.type != Tab.intType) {
                error(NO_INT_OP); //Both terms need to be int for add/sub-operation
            }

            switch (op) {
                case plus:
                    code.put(OpCode.add);
                    break;
                case minus:
                    code.put(OpCode.sub);
                    break;
            }
        }

        return x;
    }

    private Operand term() {
        Operand x = factor();

        while (firstOfMulOp.contains(sym)) { //First(MulOp) = *, /, %
            Token.Kind op = sym;
            mulOp();

            code.load(x);
            Operand y = factor();
            code.load(y);

            if (x.type != Tab.intType || y.type != Tab.intType) {
                error(NO_INT_OP); //Both factors need to be int for mult-operation
            }

            switch (op) {
                case times:
                    code.put(OpCode.mul);
                    break;
                case slash:
                    code.put(OpCode.div);
                    break;
                case rem:
                    code.put(OpCode.rem);
                    break;
            }
        }

        return x;
    }

    private Operand factor() {
        Operand x;

        switch (sym) {
            case ident:
                x = designator();

                if (sym == lpar) { //First(ActPars) = (
                    if (x.kind != Operand.Kind.Meth) {
                        error(NO_METH);
                    }
                    if (x.obj.type == Tab.noType) {
                        error(INVALID_CALL);
                    }

                    actPars();
                    x.kind = Operand.Kind.Stack; //Manually set to stack because of being a methhod designator
                }
                break;

            case number:
                scan();
                x = new Operand(t.val);
                break;

            case charConst:
                scan();
                x = new Operand(t.val);
                x.type = Tab.charType; //Need to manually set the char constant
                break;

            case new_:
                scan();
                check(ident);

                Obj obj = tab.find(t.str);
                if (obj.kind != Obj.Kind.Type) {
                    error(NO_TYPE);
                }

                StructImpl type = obj.type;

                if (sym == lbrack) {
                    scan();
                    x = expr();

                    if (x.type != Tab.intType) {
                        error(ARRAY_SIZE);
                    }

                    code.load(x);
                    code.put(OpCode.newarray);
                    if (type == Tab.charType) {
                        code.put(0);
                    } else {
                        code.put(1);
                    }
                    type = new StructImpl(type);

                    check(rbrack);
                } else {
                    if (obj.kind != Obj.Kind.Type || obj.type.kind != Struct.Kind.Class) {
                        error(NO_CLASS_TYPE);
                    } else {
                        code.put(OpCode.new_);
                        code.put2(type.nrFields());
                    }

                }
                x = new Operand(type);
                break;

            case lpar:
                scan();
                x = expr();
                check(rpar);
                break;

            default:
                x = new Operand(1); //Idea is for multiplication 1 does not ruin anything
                error(INVALID_FACT);
                break;
        }

        return x;
    }

    private Operand designator() {
        check(ident);
        Operand x = new Operand(tab.find(t.str), this); //Build operand with the ident

        while (true) {
            if (sym == period) { //ff period, x must be a class and the next symbol is a field of that class
                if (x.type.kind != Struct.Kind.Class) {
                    error(NO_CLASS);
                }
                scan();
                code.load(x); //Load field adress on stack
                check(ident);

                Obj obj = tab.findField(t.str, x.type);

                x.kind = Operand.Kind.Fld;
                x.type = obj.type;
                x.adr = obj.adr;
            } else if (sym == lbrack) { //If bracket, the value is in the array of x
                if (x.obj != null && x.obj.kind != Obj.Kind.Var) {
                    error(NO_VAL);
                }

                scan();
                code.load(x); //Load elem adress on stack
                Operand y = expr();

                if (x.type.kind != Struct.Kind.Arr) {
                    error(NO_ARRAY);
                }
                if (y.type != Tab.intType) {
                    error(ARRAY_INDEX);
                }
                code.load(y); //Load index on stack

                x.kind = Operand.Kind.Elem;
                x.type = x.type.elemType;

                check(rbrack);
            } else {
                break; //Break the loop as no correct symbol was found
            }
        }

        return x;
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

    private void recoverMethodDecl() {
        error(METH_DECL);
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
        if (errorDist >= MIN_ERRORS) {
            scanner.errors.error(la.line, la.col, msg, msgParams);
        }
        errorDist = 0;
    }

    /*
    ------------------------------------------------
    ### Additional methods
    ------------------------------------------------
     */

    //Method copied from VL-script
    private boolean nextTokenIsType() {
        if (sym != ident) return false;
        Obj obj = tab.find(la.str);
        return obj.kind == Obj.Kind.Type;
    }
}
