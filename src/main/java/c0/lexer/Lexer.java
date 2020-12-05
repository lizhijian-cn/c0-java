package c0.lexer;

import c0.error.UnreachableException;
import c0.RichIterator;

import java.nio.channels.NonReadableChannelException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Lexer extends RichIterator<Token> {
    CharIterator charIter;
    LinkedHashMap<Predicate<Character>, Supplier<Token>> actions;

    public Lexer(CharIterator charIter) {
        this.charIter = charIter;
        actions = new LinkedHashMap<>(Map.of(
                Character::isWhitespace, this::skipSpace,
                this::isUnderlineOrLetter, this::lexIdentOrKeyword,
                Character::isDigit, this::lexUIntOrDouble,
                ch -> ch.equals('"'), this::lexString,
                ch -> ch.equals('\''), this::lexChar,
                this::isOperator, this::lexOperator
                // TODO lexComment
        ));
    }

    @Override
    public Optional<Token> getNext() {
        if (!charIter.hasNext()) {
            return Optional.of(new Token(TokenType.EOF));
        }
        for (var action : actions.entrySet()) {
            if (charIter.check(action.getKey())) {
                return Optional.of(action.getValue().get());
            }
        }
        throw new RuntimeException("unrecognized character");
    }

    Token skipSpace() {
        while (charIter.check(Character::isWhitespace)) {
            charIter.next();
        }
        return getNext().orElseThrow(NonReadableChannelException::new);
    }

    boolean isUnderlineOrLetter(Character ch) {
        return Character.isLetter(ch) || ch == '_';
    }

    boolean isUnderlineOrLetterOrDigit(Character ch) {
        return Character.isDigit(ch) || isUnderlineOrLetter(ch);
    }

    Token lexIdentOrKeyword() {
        var sb = new StringBuilder();
        while (charIter.check(this::isUnderlineOrLetterOrDigit)) {
            sb.append(charIter.next());
        }
        var value = sb.toString();
        return switch (value) {
            case "let" -> new Token(TokenType.LET);
            case "const" -> new Token(TokenType.CONST);
            case "as" -> new Token(TokenType.AS);
            case "fn" -> new Token(TokenType.FN);
            case "return" -> new Token(TokenType.RETURN);
            case "if" -> new Token(TokenType.IF);
            case "else" -> new Token(TokenType.ELSE);
            case "while" -> new Token(TokenType.WHILE);
            case "continue" -> new Token(TokenType.CONTINUE);
            case "break" -> new Token(TokenType.BREAK);
            default -> new Token(TokenType.IDENT, value);
        };
    }

    Token lexUIntOrDouble() {
        var sb = new StringBuilder();
        while (charIter.check(Character::isDigit)) {
            sb.append(charIter.next());
        }
        if (charIter.test('.')) {
            sb.append('.');
            if (charIter.check(Character::isDigit)) {
                while (charIter.check(Character::isDigit)) {
                    sb.append(charIter.next());
                }
                if (charIter.check(x -> x == 'e' || x == 'E')) {
                    sb.append(charIter.next());
                    if (charIter.check(x -> x == '+' || x == '-')) {
                        sb.append(charIter.next());
                    }
                    if (charIter.check(Character::isDigit)) {
                        while (charIter.check(Character::isDigit)) {
                            sb.append(charIter.next());
                        }
                        return new Token(TokenType.DOUBLE_LITERAL, sb.toString());
                    } else {
                        throw new RuntimeException("invalid double");
                    }
                }
                return new Token(TokenType.DOUBLE_LITERAL, sb.toString());
            } else {
                throw new RuntimeException("invalid double");
            }
        }
        return new Token(TokenType.UINT_LITERAL, sb.toString());
    }

    Optional<Character> lexSpecialOrLiteralChar() {
        if (!charIter.hasNext()) {
            return Optional.empty();
        }
        if (charIter.check('"')) {
            return Optional.empty();
        }
        if (charIter.test('\\')) {
            if (!charIter.hasNext()) {
                throw new RuntimeException("lack of special character");
            }
            return switch (charIter.next()) {
                case '\\' -> Optional.of('\\');
                case '"' -> Optional.of('"');
                case '\'' -> Optional.of('\'');
                case 'n' -> Optional.of('\n');
                case 'r' -> Optional.of('\r');
                case 't' -> Optional.of('\t');
                default -> throw new RuntimeException("Unrecognised Character");
            };
        }
        return Optional.of(charIter.next());
    }

    Token lexString() {
        var sb = new StringBuilder();
        charIter.expect('"');
        while (true) {
            var op = lexSpecialOrLiteralChar();
            if (op.isEmpty()) {
                break;
            }
            sb.append(op.get());
        }
        charIter.expect('"');
        return new Token(TokenType.STRING_LITERAL, sb.toString());
    }

    Token lexChar() {
        charIter.expect('\'');
        var op = lexSpecialOrLiteralChar();
        var value = op.orElseThrow(() -> new RuntimeException("char literal must not be empty"));
        charIter.expect('\'');
        return new Token(TokenType.CHAR_LITERAL, Integer.valueOf((int) value).toString());
    }

    boolean isOperator(char ch) {
        return List.of('+', '-', '*', '/', '=', '!', '<', '>', '(', ')', '{', '}', ',', ':', ';').contains(ch);
    }

    Token lexOperator() {
        return switch (charIter.next()) {
            case '+' -> new Token(TokenType.PLUS);
            case '-' -> {
                if (charIter.test('>')) {
                    yield new Token(TokenType.ARROW);
                }
                yield new Token(TokenType.MINUS);
            }
            case '*' -> new Token(TokenType.MUL);
            case '/' -> new Token(TokenType.DIV);
            case '=' -> {
                if (charIter.test('=')) {
                    yield new Token(TokenType.EQ);
                }
                yield new Token(TokenType.ASSIGN);
            }
            case '!' -> {
                charIter.expect('=');
                yield new Token(TokenType.NEQ);
            }
            case '<' -> {
                if (charIter.test('=')) {
                    yield new Token(TokenType.LE);
                }
                yield new Token(TokenType.LT);
            }
            case '>' -> {
                if (charIter.test('=')) {
                    yield new Token(TokenType.GE);
                }
                yield new Token(TokenType.GT);
            }
            case '(' -> new Token(TokenType.L_PAREN);
            case ')' -> new Token(TokenType.R_PAREN);
            case '{' -> new Token(TokenType.L_BRACE);
            case '}' -> new Token(TokenType.R_BRACE);
            case ',' -> new Token(TokenType.COMMA);
            case ':' -> new Token(TokenType.COLON);
            case ';' -> new Token(TokenType.SEMICOLON);
            default -> throw new UnreachableException();
        };
    }
}
