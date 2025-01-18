package easton.varcommands;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public enum Operation {
    ADDITION('+', Operation::add),
    SUBTRACTION('-', Operation::subtract),
    MULTIPLICATION('*', Operation::multiply),
    DIVISION('/', Operation::divide),
    EXPONENTIATION('^', Operation::exponentiate);

    public static final Map<Character, Operation> CHAR_TO_OP = new HashMap<>();
    private final char c;
    private final BiFunction<Number, Number, Number> function;

    Operation(char c, BiFunction<Number, Number, Number> function) {
        this.function = function;
        this.c = c;
    }

    public Number execute(Number term, Number term2) {
        return this.function.apply(term, term2);
    }

    static {
        for (Operation op : Operation.values())
            CHAR_TO_OP.put(op.c, op);
    }

    private static Number add(Number addend, Number addend2) {
        Number sum = addend.doubleValue() + addend2.doubleValue();
        if (addend instanceof Long && addend2 instanceof Long) return sum.longValue();
        return sum;
    }

    private static Number subtract(Number minuend, Number subtrahend) {
        Number difference = minuend.doubleValue() - subtrahend.doubleValue();
        if (minuend instanceof Long && subtrahend instanceof Long) return difference.longValue();
        return difference;
    }

    private static Number multiply(Number multiplier, Number multiplicand) {
        Number product = multiplier.doubleValue() * multiplicand.doubleValue();
        if (multiplier instanceof Long && multiplicand instanceof Long) return product.longValue();
        return product;
    }

    private static Number divide(Number dividend, Number divisor) {
        Number quotient = dividend.doubleValue() / divisor.doubleValue();
        if (dividend instanceof Long && divisor instanceof Long) return quotient.longValue();
        return quotient;
    }

    private static Number exponentiate(Number base, Number exponent) {
        Number power = Math.pow(base.doubleValue(), exponent.doubleValue());
        if (base instanceof Long && exponent instanceof Long) return power.longValue();
        return power;
    }

}