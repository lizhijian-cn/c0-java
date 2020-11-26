package c0.analyzer.ir.instruction;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Instruction {
    InstructionOp op;
    long operand;

    public Instruction(InstructionOp op) {
        this(op, 0);
    }
}
