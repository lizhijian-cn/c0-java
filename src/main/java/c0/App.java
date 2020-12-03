package c0;

import c0.analyzer.Dumper;
import c0.analyzer.IRGenerator;
import c0.analyzer.TypeChecker;
import c0.lexer.CharIterator;
import c0.lexer.Lexer;
import c0.parser.Parser;
import c0.util.RichDataOutputStream;

import java.io.*;
import java.util.List;

public class App {
    public static void main(String[] args) {
        var filename = args[0];
        var tok = filename.split("\\.");
        if (!tok[1].equals("c0")) {
            System.err.printf("unrecognized extension: %s\n", tok[1]);
            return;
        }
        InputStream in;
        OutputStream out;
        try {
            in = new FileInputStream(filename);
            out = new FileOutputStream(tok[0].concat(".o0"));
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
            return;
        }

        var charIter = new CharIterator(new InputStreamReader(in));
        var lexer = new Lexer(charIter);
        var parser = new Parser(lexer);
        var ast = parser.parse();
        var dumper = new Dumper(System.out);
//        ast.accept(dumper);
        var typeChecker = new TypeChecker();
        ast.accept(typeChecker);
        var irGenerator = new IRGenerator(new RichDataOutputStream(out));
        ast.accept(irGenerator);
    }
}
