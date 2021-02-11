package ssw.mj.impl;

import ssw.mj.Parser;
import ssw.mj.codegen.Code;
import ssw.mj.codegen.Label;
import ssw.mj.codegen.Operand;
import ssw.mj.symtab.Obj;
import ssw.mj.symtab.Tab;

import static ssw.mj.Errors.Message.*;

public final class CodeImpl extends Code {

    public CodeImpl(Parser p) {
        super(p);
    }

    void load(Operand x) { //method taken from the exercise course material
        switch (x.kind) {
            case Con:
                loadConst(x.val);
                break;
            case Local:
                switch (x.adr) {
                    case 0:
                        put(OpCode.load_0);
                        break;
                    case 1:
                        put(OpCode.load_1);
                        break;
                    case 2:
                        put(OpCode.load_2);
                        break;
                    case 3:
                        put(OpCode.load_3);
                        break;
                    default:
                        put(OpCode.load);
                        put(x.adr);
                        break;
                }
                break;
            case Static:
                put(OpCode.getstatic);
                put2(x.adr);
                break;
            case Stack:
                break; // nothing to do (already loaded)
            case Fld:
                put(OpCode.getfield);
                put2(x.adr);
                break;
            case Elem:
                if (x.type == Tab.charType) {
                    put(OpCode.baload);
                } else {
                    put(OpCode.aload);
                }
                break;
            default:
                parser.error(NO_VAL);
        }
        x.kind = Operand.Kind.Stack; // remember that value is now loaded onto the stack
    }

    void loadConst(int val) { //method taken from the exercise course material
        switch (val) {
            case 0:
                put(OpCode.const_0);
                break;
            case 1:
                put(OpCode.const_1);
                break;
            case 2:
                put(OpCode.const_2);
                break;
            case 3:
                put(OpCode.const_3);
                break;
            case 4:
                put(OpCode.const_4);
                break;
            case 5:
                put(OpCode.const_5);
                break;
            case -1:
                put(OpCode.const_m1);
                break;
            default:
                put(OpCode.const_);
                put4(val);
        }
    }

    void assign(Operand x) { //method taken from the exercise course material and modified
        switch (x.kind) {
            case Local:
                switch (x.adr) {
                    case 0:
                        put(OpCode.store_0);
                        break;
                    case 1:
                        put(OpCode.store_1);
                        break;
                    case 2:
                        put(OpCode.store_2);
                        break;
                    case 3:
                        put(OpCode.store_3);
                        break;
                    default:
                        put(OpCode.store);
                        put(x.adr);
                        break;
                }
                break;
            case Static:
                put(OpCode.putstatic);
                put2(x.adr);
                break;
            case Fld:
                put(OpCode.putfield);
                put2(x.adr);
                break;
            case Elem:
                if (x.type == Tab.charType) {
                    put(OpCode.bastore);
                } else {
                    put(OpCode.astore);
                }
                break;
            default:
                parser.error(NO_VAR);
        }
    }

    void assign(Operand x, Operand y) { //method taken from the exercise course material and modified
        load(y);
        assign(x);
    }

    public void tJump (Operand x) {
        setOpCode(x.op);
        x.tLabel.put();
    }

    public void fJump (Operand x) {
        setOpCode(CompOp.invert(x.op));
        x.fLabel.put();
    }

    //puts the (inverted) opcode on the code array
    private void setOpCode(CompOp comp) {
        switch (comp) {
            case eq:
                put(OpCode.jeq);
                break;
            case ne:
                put(OpCode.jne);
                break;
            case lt:
                put(OpCode.jlt);
                break;
            case le:
                put(OpCode.jle);
                break;
            case gt:
                put(OpCode.jgt);
                break;
            case ge:
                put(OpCode.jge);
                break;
        }
    }

    public void jump(Label label) {
        put(OpCode.jmp);
        label.put();
    }

    public void inc (Operand x, int amount) {
        if (x.type != Tab.intType) {
            parser.error(NO_INT);
        }
        if (x.obj != null && x.obj.kind != Obj.Kind.Var) {
            parser.error(NO_VAR);
        }

        if (x.kind == Operand.Kind.Local) {
            put(OpCode.inc);
            put(x.adr);
            put(amount);

        } else {
            if (x.kind == Operand.Kind.Fld) {
                put(OpCode.dup);
            } else if (x.kind == Operand.Kind.Elem) {
                put(OpCode.dup2);
            }

            Operand.Kind xKind = x.kind;
            load(x);
            load(new Operand(amount));
            put(OpCode.add);

            x.kind = xKind; //reset kind as now it isn't on stack anymore
            assign(x);
        }
    }
}
