package c0.analyzer.ir;

import c0.entity.Variable;
import lombok.Getter;

import java.util.List;

@Getter
public class IR {
    int magic;
    int version;
    int globalNumCount;
    List<String> globalStrings;
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
