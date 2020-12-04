package c0.util;

import c0.analyzer.Instruction;
import lombok.SneakyThrows;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;

public class RichDataOutputStream {
    DataOutputStream dataOut;
    DataOutputStream s;
    ByteArrayOutputStream byteOut;
    int insCount;

    public RichDataOutputStream(OutputStream out) {
        this.dataOut = new DataOutputStream(out);
        this.s = this.dataOut;
    }

    public void setCache() {
        this.byteOut = new ByteArrayOutputStream();
        this.s = new DataOutputStream(byteOut);
        this.insCount = 0;
    }

    public int cancelCache() {
        this.s = this.dataOut;
        return insCount;
    }

    public byte[] getBytes() {
        return byteOut.toByteArray();
    }

    @SneakyThrows
    public void writeByte(int v) {
        s.writeByte(v);
    }

    @SneakyThrows
    public void writeInt(int v) {
        s.writeInt(v);
    }

    @SneakyThrows
    public void writeLong(long v) {
        s.writeLong(v);
    }

    @SneakyThrows
    public void writeString(String s) {
        this.s.writeBytes(s);
    }

    @SneakyThrows
    public void writeBytes(byte[] b) {
        s.write(b);
    }

    @SneakyThrows
    public void write(Instruction ins) {
        insCount++;
        s.writeByte(ins.getCode());
    }

    @SneakyThrows
    public void write(Instruction ins, int v) {
        insCount++;
        s.writeByte(ins.getCode());
        s.writeInt(v);
    }

    @SneakyThrows
    public void write(Instruction ins, long v) {
        insCount++;
        s.writeByte(ins.getCode());
        s.writeLong(v);
    }
}
