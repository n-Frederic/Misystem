package M11.Ex2;

public class Calculator {
    private double accumulator = 0; //存结果
    private String op;

    public double getAccumulator() {
        return accumulator;
    }

    public void setAccumulator(double accumulator) {
        this.accumulator = accumulator;
    }

    // 加法
    public String opAdd(String number) {
        accumulator += Double.parseDouble(number);
        return String.valueOf(accumulator);
    }

    // 减法
    public String opSubtract(String number) {
        accumulator -= Double.parseDouble(number);
        return String.valueOf(accumulator);
    }

    // 乘法
    public String opMultiply(String number) {
        accumulator *= Double.parseDouble(number);
        return String.valueOf(accumulator);
    }

    // 除法
    public String opDivide(String number) {
        if (accumulator == 0) {
            throw new ArithmeticException("Division by zero is not allowed (accumulator is zero)");
        }
        accumulator /= Double.parseDouble(number);
        return String.valueOf(accumulator);
    }

    // 等于号
    public String opEquals() {
        String result = String.valueOf(accumulator);
        return result;
    }

    // 执行算术运算的辅助方法
    private double performOperation(String number, String operator) {
        double operand = Double.parseDouble(number);
        switch (operator) {
            case "+":
                return accumulator + operand;
            case "-":
                return accumulator - operand;
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator);
        }
    }

    // 重置累积器
    public void reset() {
        accumulator = 0;
    }
}