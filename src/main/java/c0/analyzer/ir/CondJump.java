package c0.analyzer.ir;

import java.util.Optional;

class CondJump implements Jumper {
    private final int src;
    private final Label thenLabel;
    private final Label elseLabel;
    ControlFlow flow;

    CondJump(ControlFlow flow, Label thenLabel, Label elseLabel) {
        this.flow = flow;
        this.src = flow.getLine();
        this.thenLabel = thenLabel;
        this.elseLabel = elseLabel;
        flow.write(Instruction.BRTRUE, -1);
        flow.write(Instruction.BR, -1);
    }

    @Override
    public void setBrOffset() {
        flow.getOperands().set(src, Optional.of(thenLabel.loc - src - 1));
        flow.getOperands().set(src + 1, Optional.of(elseLabel.loc - src - 2));
    }
}
