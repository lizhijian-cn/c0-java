package c0.analyzer.ir;

import lombok.SneakyThrows;

import java.io.DataOutputStream;
import java.io.OutputStream;

class RichDataOutputStream {
    DataOutputStream s;

    RichDataOutputStream(OutputStream out) {
        this.s = new DataOutputStream(out);
    }

    @SneakyThrows
    void writeByte(int v) {
        s.writeByte(v);
    }

    @SneakyThrows
    void writeInt(int v) {
        s.writeInt(v);
    }

    @SneakyThrows
    void writeLong(long v) {
        s.writeLong(v);
    }

    @SneakyThrows
    void writeString(String s) {
        this.s.writeBytes(s);
    }

    @SneakyThrows
    void writeBytes(byte[] b) {
        s.write(b);
    }
}
