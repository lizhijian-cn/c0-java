package c0.analyzer.ir;

class Label {
    private final ControlFlow flow;
    int loc;

    Label(ControlFlow flow) {
        this.flow = flow;
    }

    void set() {
        this.loc = flow.getLine();
    }
}
