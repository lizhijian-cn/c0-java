package c0.analyzer.ir;

import java.util.Optional;

class UnCondJump implements Jumper {
    private final int src;
    private final Label dst;
    private final ControlFlow flow;

    UnCondJump(ControlFlow flow, Label dst) {
        this.flow = flow;
        this.src = flow.getLine();
        this.dst = dst;
        flow.write(Instruction.BR, -1);
    }

    @Override
    public void setBrOffset() {
        flow.getOperands().set(src, Optional.of(dst.loc - src - 1));
    }
}
