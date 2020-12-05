package c0.analyzer.ir;

import lombok.Getter;
import lombok.SneakyThrows;

import java.util.*;

@Getter
public class ControlFlow {
    RichDataOutputStream s;
    List<Integer> opcodes;
    List<Optional<Object>> operands;

    List<Jumper> jumpers;
    Deque<Label> breakStack;
    Deque<Label> continueStack;

    public ControlFlow(RichDataOutputStream s) {
        this.s = s;
        this.opcodes = new ArrayList<>();
        this.operands = new ArrayList<>();
        this.jumpers = new ArrayList<>();
        this.breakStack = new LinkedList<>();
        this.continueStack = new LinkedList<>();
    }

    int getLine() {
        return opcodes.size();
    }

    // TODO: control flow check, may use dfs
    @SneakyThrows
    public void flush() {
        jumpers.forEach(Jumper::setBrOffset);

        for (int i = 0; i < opcodes.size(); i++) {
            s.writeByte(opcodes.get(i));
            if (operands.get(i).isPresent()) {
                var operand = operands.get(i).get();
                if (operand instanceof Integer v) {
                    s.writeInt(v);
                }
                if (operand instanceof Long v) {
                    s.writeLong(v);
                }
            }
        }
        opcodes.clear();
        operands.clear();
        breakStack.clear();
        continueStack.clear();
        jumpers.clear();
    }

    @SneakyThrows
    public void write(Instruction ins) {
        opcodes.add(ins.getCode());
        operands.add(Optional.empty());
    }

    @SneakyThrows
    public void write(Instruction ins, int v) {
        opcodes.add(ins.getCode());
        operands.add(Optional.of(v));
    }

    @SneakyThrows
    public void write(Instruction ins, long v) {
        opcodes.add(ins.getCode());
        operands.add(Optional.of(v));
    }
}
