package c0.analyzer.ir;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
enum Instruction {
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
    NOT(0X2E),
    CMPI(0X30),
    CMPF(0X32),
    NEGI(0X34),
    NEGF(0X35),
    ITOF(0X36),
    FTOI(0X37),
    SETLT(0X39),
    SETGT(0X3A),
    BR(0X41),
    BRFALSE(0X42),
    BRTRUE(0X43),
    CALL(0X48),
    RET(0X49),
    CALLNAME(0X4a);

    int code;
}

