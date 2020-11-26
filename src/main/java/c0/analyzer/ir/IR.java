package c0.analyzer.ir;

import java.util.List;

public class IR {
    int magic;
    int version;
    List<IRFunction> functions;

    public IR() {
        this.magic =  0x72303b3e;
        this.version = 1;
        functions.add(new IRFunction());
    }

    public void add(IRFunction function) {
        functions.add(function);
    }
}
