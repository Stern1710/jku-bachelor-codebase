package ssw.mj.impl;

import ssw.mj.Errors;
import ssw.mj.Parser;
import ssw.mj.Scanner;
import ssw.mj.Token;
import ssw.mj.codegen.Code;
import ssw.mj.codegen.Label;
import ssw.mj.codegen.Operand;
import ssw.mj.symtab.Obj;
import ssw.mj.symtab.Struct;
import ssw.mj.symtab.Tab;

import java.util.*;

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

    private Obj currentMeth;

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
        currentMeth = tab.insert(Obj.Kind.Meth, t.str, type);
        check(lpar);

        tab.openScope();

        if (nextTokenIsType()) { //First(FormPars) = First(Type) = ident
            formPars();
            mainParams = true;
        }
        check(rpar);

        currentMeth.nPars = tab.curScope.nVars(); //Only form params in here --> Write to
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
        currentMeth.locals = tab.curScope.locals();

        if (tab.curScope.locals().size() > MAX_LOCALS) {
            error(TOO_MANY_LOCALS);
        }

        currentMeth.adr = code.pc;
        //Set the correct enter parameters for a method
        code.put(OpCode.enter);
        code.put(currentMeth.nPars);
        code.put(tab.curScope.nVars());

        block(null); //all statements of the method are in here

        //set the correct return params for 
        if (currentMeth.type == Tab.noType) {
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

    private void block(Label breakLabel) {
        check(lbrace);

        if (t.kind == lbrace) { //Check for lbrace if error was suppressed by error distance
            while (sym != rbrace && sym != eof) { //Check for first symbol of statement
                statement(breakLabel);
            }
        }
        check(rbrace);
    }

    private void statement(Label breakLabel) {
        if (!firstOfStatement.contains(sym)) {
            recoverStat();
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
                    OpCode asOp = assignOp();

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

                            actPars(x);
                            code.put(OpCode.call);
                            code.put2(x.adr - (code.pc - 1));
                            if (x.type != Tab.noType) {
                                code.put(OpCode.pop);
                            }
                            break;

                        case pplus:
                            code.inc(x, 1);
                            scan();
                            break;
                        case mminus:
                            code.inc(x, -1);
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

                Operand o = condition();
                code.fJump(o);
                o.tLabel.here();

                check(rpar);
                statement(breakLabel);


                if (sym == else_) {
                    Label end = new LabelImpl(code);
                    code.jump(end); //Jump to end to avoid else-case when already been in if-case

                    o.fLabel.here();
                    scan();
                    statement(breakLabel);

                    end.here();
                } else {
                    o.fLabel.here();
                }
                break;

            case loop_:
                scan();
                check(ident);

                tab.openScope();
                Obj insertedLoop = tab.insert(Obj.Kind.Label, t.str, Tab.noType);
                check(colon);

                check(while_);
                check(lpar);

                Label top = new LabelImpl(code);
                top.here();

                o = condition();
                insertedLoop.label = o.fLabel;

                code.fJump(o);
                o.tLabel.here();
                check(rpar);
                statement(o.fLabel);

                code.jump(top);
                o.fLabel.here();

                tab.closeScope();
                break;

            case while_:
                scan();
                check(lpar);

                top = new LabelImpl(code);
                top.here();

                o = condition();
                code.fJump(o);
                o.tLabel.here();

                check(rpar);
                statement(o.fLabel);

                code.jump(top);
                o.fLabel.here();

                break;

            case break_:
                scan();

                if (sym == ident) {
                    scan();

                    Obj obj = tab.find(t.str);
                    if (obj.kind != Obj.Kind.Label || obj.label == null) {
                        error(NO_LABEL);
                    } else {
                        code.jump(obj.label);
                    }
                } else {
                    if (breakLabel == null) {
                        error(NO_LOOP);
                    } else {
                        code.jump(breakLabel);
                    }
                }

                check(semicolon);
                break;

            case return_:
                scan();
                if (firstOfExpr.contains(sym)) {//Check for First(Expr)
                    if (currentMeth.type == Tab.noType) {
                        error(RETURN_VOID);
                    }

                    x = expr();
                    code.load(x);

                    if (!x.type.assignableTo(currentMeth.type)) {
                        error(RETURN_TYPE);
                    }
                } else {
                    if (currentMeth.type != Tab.noType) {
                        error(RETURN_NO_VAL);
                    }
                }

                code.put(OpCode.exit);
                code.put(OpCode.return_);
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
                block(breakLabel);
                break;

            case semicolon:
                scan();
                break;

            default:
                error(INVALID_STAT);
                break;
        }
    }

    private OpCode assignOp() {
        if (firstOfAssignOp.contains(sym)) {
            scan();
            switch (t.kind) { //Map the assignOp code to the bytecode
                case plusas:
                    return OpCode.add;
                case minusas:
                    return OpCode.sub;
                case timesas:
                    return OpCode.mul;
                case slashas:
                    return OpCode.div;
                case remas:
                    return OpCode.rem;
            }
        } else {
            error(ASSIGN_OP);
        }
        return OpCode.nop;
    }

    private void actPars(Operand x) {
        Operand ap;
        check(lpar);

        if (x.kind != Operand.Kind.Meth) {
            error(NO_METH);
            x.obj = tab.noObj;
        }

        int aPars = 0;
        int fPars = x.obj.nPars;

        //Iterate over items in x.obj.locals
        Iterator<Obj> iter = x.obj.locals.values().iterator();

        if (firstOfExpr.contains(sym)) { //First(Expr) = -, First(Term) = {-, ident, number, charConst, "new", "("}
            ap = expr();
            code.load(ap);
            aPars++;

            if (iter.hasNext()) {
                Obj next = iter.next();
                if (!ap.type.assignableTo(next.type)) {
                    error(PARAM_TYPE);
                }
            }

            while (sym == comma) {
                scan();
                ap = expr();
                code.load(ap);
                aPars++;

                if (iter.hasNext()) {
                    Obj next = iter.next();
                    if (!ap.type.assignableTo(next.type)) {
                        error(PARAM_TYPE);
                    }
                }
            }
        }

        if (aPars > fPars) {
            error(MORE_ACTUAL_PARAMS);
        } else if (aPars < fPars) {
            error(LESS_ACTUAL_PARAMS);
        }
        check(rpar);
    }

    private Operand condition() {
        Operand x = condTerm();

        while (sym == or) {
            code.tJump(x);
            scan();
            x.fLabel.here();
            Operand y = condTerm();
            x.fLabel = y.fLabel;
            x.op = y.op;
        }

        return x;
    }

    private Operand condTerm() {
        Operand x = condFact();

        while (sym == and) {
            code.fJump(x);
            scan();
            Operand y = condFact();
            x.op = y.op;
        }

        return x;
    }

    private Operand condFact() {
        Operand op1 = expr();
        code.load(op1);

        Code.CompOp op = relop();

        Operand op2 = expr();
        code.load(op2);

        if (!op1.type.compatibleWith(op2.type)) {
            error(INCOMP_TYPES);
        }
        if (op1.type.isRefType() && op != Code.CompOp.eq && op != Code.CompOp.ne) {
            error(EQ_CHECK);
        }

        return new Operand(op, code);
    }

    private Code.CompOp relop() {
        if (firstOfRelop.contains(sym)) {
            scan();

            switch (t.kind) { //EnumSet.of(eql, neq, gtr, geq, lss, leq);
                case eql:
                    return Code.CompOp.eq;
                case neq:
                    return Code.CompOp.ne;
                case gtr:
                    return Code.CompOp.gt;
                case geq:
                    return Code.CompOp.ge;
                case lss:
                    return Code.CompOp.lt;
                case leq:
                    return Code.CompOp.le;
            }
        } else {
            error(REL_OP);
        }
        return Code.CompOp.eq; //Just not return null at the moment
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
            OpCode opCode = addOp();

            code.load(x);
            Operand y = term();
            code.load(y);

            if (x.type != Tab.intType || y.type != Tab.intType) {
                error(NO_INT_OP); //Both terms need to be int for add/sub-operation
            }

            code.put(opCode);
        }

        return x;
    }

    private Operand term() {
        Operand x = factor();

        while (firstOfMulOp.contains(sym)) { //First(MulOp) = *, /, %
            OpCode opCode = mulOp();

            code.load(x);
            Operand y = factor();
            code.load(y);

            if (x.type != Tab.intType || y.type != Tab.intType) {
                error(NO_INT_OP); //Both factors need to be int for mult-operation
            }

            code.put(opCode);
        }

        return x;
    }

    private Operand factor() {
        Operand x;

        switch (sym) {
            case ident:
                x = designator();

                if (sym == lpar) { //First(ActPars) = (
                    if (x.obj.type == Tab.noType) {
                        error(INVALID_CALL);
                    }
                    actPars(x);

                    if (x.obj != tab.ordObj && x.obj != tab.chrObj) { //Copied and modified from lecture slides
                        if (x.obj == tab.lenObj) {
                            code.put(OpCode.arraylength);
                        }else {
                            code.put(OpCode.call);
                            code.put2(x.adr - (code.pc-1));
                        }
                    }
                    x.kind = Operand.Kind.Stack; //Manually set to stack because of being a method designator
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
                code.load(x); //Load field address on stack
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
                code.load(x); //Load elem address on stack
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

    private OpCode addOp() {
        if (sym == plus || sym == minus) {
            scan();
            switch (t.kind) {
                case plus:
                    return OpCode.add;
                case minus:
                    return OpCode.sub;
            }
        } else {
            error(ADD_OP);
        }
        return OpCode.nop;
    }

    private OpCode mulOp() {
        if (firstOfMulOp.contains(sym)) {
            scan();
            switch (t.kind) {
                case times:
                    return OpCode.mul;
                case slash:
                    return OpCode.div;
                case rem:
                    return OpCode.rem;
            }
        } else {
            error(MUL_OP);
        }

        return OpCode.nop;
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

    private void recoverStat() {
        error(INVALID_STAT);
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
