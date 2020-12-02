package c0;

import c0.analyzer.ir.IR;
import c0.analyzer.ir.IRFunction;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;

@AllArgsConstructor
public class AsmGenerator {
    IR ir;
    DataOutputStream s;

    @SneakyThrows
    void generate() {
        s.writeInt(ir.getMagic());
        s.writeInt(ir.getVersion());

        s.writeInt(ir.getGlobalNumCount() + ir.getGlobalStrings().size());
        for (int i = 0; i < ir.getGlobalNumCount(); i++) {
            s.writeByte(0);
            s.writeInt(8);
            s.writeLong(0);
        }

        for (String string : ir.getGlobalStrings()) {
            s.writeByte(0);
            s.writeInt(string.length());
            s.writeBytes(string);
        }

        s.writeInt(ir.getFunctions().size());
        ir.getFunctions().forEach(x -> write(x));
    }

    void write (IRFunction function) {

    }
}
