package ssw.mj.impl;

import ssw.mj.codegen.Code;
import ssw.mj.codegen.Label;

import java.util.ArrayList;
import java.util.List;

public final class LabelImpl extends Label {

    private final List<Integer> fixupList;

    // TODO Exercise 6: Implementation of Labels for management of jump targets
    public LabelImpl(Code code) {
        super(code);
        adr = -1;
        fixupList = new ArrayList<>();
    }

    /**
     * Generates code for a jump to this label.
     */
    @Override
    public void put() {
        if (adr >= 0) {
            code.put2(adr - (code.pc - 1));
        } else {
            fixupList.add(code.pc);
            code.put2(0);
        }
    }

    /**
     * Defines <code>this</code> label to be at the current pc position
     */
    @Override
    public void here() {
        if (adr < 0) {
            for (int pos : fixupList) {
                code.put2(pos, code.pc - (pos - 1));
            }
            adr = code.pc;
        }
    }
}
