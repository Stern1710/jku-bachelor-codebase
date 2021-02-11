package ssw.mj.impl;

import ssw.mj.Parser;
import ssw.mj.symtab.Obj;
import ssw.mj.symtab.Scope;
import ssw.mj.symtab.Struct;
import ssw.mj.symtab.Tab;

import static ssw.mj.Errors.Message.*;

public final class TabImpl extends Tab {

    /**
     * Set up "universe" (= predefined names).
     */
    public TabImpl(Parser p) {
        super(p);

        openScope();
        insert(Obj.Kind.Type, "int", intType);
        insert(Obj.Kind.Type, "char", charType);
        insert(Obj.Kind.Con, "null", nullType);
        chrObj = insert(Obj.Kind.Meth, "chr", charType);
        ordObj = insert(Obj.Kind.Meth, "ord", intType);
        lenObj = insert(Obj.Kind.Meth, "len", intType);
        noObj = new Obj(Obj.Kind.Var, "noObj", noType);

        //Set method variable "i"
        openScope();
        insert(Obj.Kind.Var, "i", intType);
        curScope.locals().get("i").level = 1;
        chrObj.locals = curScope.locals();
        chrObj.nPars = 1;
        closeScope();

        //Set method variable "ch"
        openScope();
        insert(Obj.Kind.Var, "ch", charType);
        curScope.locals().get("ch").level = 1;
        ordObj.locals = curScope.locals();
        ordObj.nPars = 1;
        closeScope();

        //Set method variable "arr" of noType
        openScope();
        StructImpl noTypeStr = new StructImpl(noType);
        insert(Obj.Kind.Var, "arr", noTypeStr);
        curScope.locals().get("arr").level = 1;
        lenObj.locals = curScope.locals();
        lenObj.nPars = 1;
        closeScope();

    }

    public void openScope() {
        Scope s = new Scope(curScope);
        curScope = s;
        curLevel++;
    }

    public void closeScope() {
        curScope = curScope.outer();
        curLevel--;
    }

    public Obj insert(Obj.Kind kind, String name, StructImpl type) {
        Obj obj = new Obj(kind, name, type);

        if (kind == Obj.Kind.Var) {
            obj.adr = curScope.nVars();
            obj.level = curLevel;
        }

        if (curScope.findLocal(name) == null) {
            curScope.insert(obj);
        } else {
            parser.error(DECL_NAME, name);
        }

        return obj;
    }

    public Obj find(String name) {
        Obj obj = curScope.findGlobal(name);

        if (obj != null) {
            return obj;
        }

        parser.error(NOT_FOUND, name);
        return noObj;
    }

    public Obj findField(String name, Struct type) {
        Obj obj = type.findField(name);

        if (obj != null) {
            return obj;
        }

        parser.error(NO_FIELD, name);
        return noObj;
    }
}
