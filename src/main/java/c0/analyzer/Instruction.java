package c0.analyzer;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Instruction {
    PUSH(0X01),
    POPN(0X3),
    LOCA(0X0A),
    ARGA(0X0B),
    GLOBA(0X0C),
    LOAD64(0X13),
    STORE64(0X17),
    STACKALLOC(0X1A),
    ADDI(0X20),
    SUBI(0X21),
    MULI(0X22),
    DIVI(0X23),
    ADDF(0X24),
    SUBF(0X25),
    MULF(0X26),
    DIVF(0X27),
    NEGI(0X34),
    NEGF(0X35),
    ITOF(0X36),
    FTOI(0X37),
    CALL(0X48),
    RET(0X49),
    CALLNAME(0X4a);

    int code;
}

