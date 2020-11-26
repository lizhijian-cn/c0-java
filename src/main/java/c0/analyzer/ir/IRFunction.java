package c0.analyzer.ir;

import c0.analyzer.ir.instruction.Instruction;

import java.util.List;

public class IRFunction {
    List<Instruction> instructions;

    public void add(Instruction instruction) {
        instructions.add(instruction);
    }

    public void add(List<Instruction> instructions) {
        instructions.addAll(instructions);
    }
}
