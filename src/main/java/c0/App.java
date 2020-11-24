package c0;

import c0.analyzer.Dumper;
import c0.lexer.CharIterator;
import c0.lexer.Lexer;
import c0.parser.Parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class App {
    public static void main(String[] args) {
        var inputFileName = args[0];
        InputStream input;
        if (inputFileName.equals("-")) {
            input = System.in;
        } else {
            try {
                input = new FileInputStream(inputFileName);
            } catch (FileNotFoundException e) {
                System.err.println("No such file: " + inputFileName);
                return;
            }
        }

        var reader = new InputStreamReader(input);
        var charIter = new CharIterator(reader);
        var lexer = new Lexer(charIter);
        var parser = new Parser(lexer);

        var ast = parser.parse();
        var dumper = new Dumper(System.out);
        ast.accept(dumper);
    }
}
