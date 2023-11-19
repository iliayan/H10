import java.util.Stack;

public class SimpleCalculator {
    public static void main(String[] args) {
        String expression = "(3 + 5)";
        try {
            double result = evaluateExpression(expression);
            System.out.println("Результат: " + result);
        } catch (Exception e) {
            System.out.println("Помилка: " + e.getMessage());
        }
    }

    public static double evaluateExpression(String expression) throws Exception {
        char[] tokens = expression.toCharArray();

        // Стек для чисел
        Stack<Double> values = new Stack<>();

        // Стек для операцій
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < tokens.length; i++) {
            // Пропускаємо пробіли
            if (tokens[i] == ' ')
                continue;

            // Якщо поточний символ - цифра, читаємо число з рядка
            if (Character.isDigit(tokens[i])) {
                StringBuilder sbuf = new StringBuilder();
                // Зчитуємо всю цифру
                while (i < tokens.length && (Character.isDigit(tokens[i]) || tokens[i] == '.')) {
                    sbuf.append(tokens[i]);
                    i++;
                }
                i--;

                // Додаємо число до стеку значень
                values.push(Double.parseDouble(sbuf.toString()));
            } else if (tokens[i] == '(') {
                // Додаємо відкриваючу дужку до стеку операцій
                operators.push(tokens[i]);
            } else if (tokens[i] == ')') {
                // Виконуємо операції до зустрічі відкриваючої дужки
                while (operators.peek() != '(') {
                    values.push(applyOperator(operators.pop(), values.pop(), values.pop()));
                }
                // Видаляємо відкриваючу дужку
                operators.pop();
            } else if (tokens[i] == '+' || tokens[i] == '-' || tokens[i] == '*' || tokens[i] == '/') {
                // Виконуємо операції з вищим пріоритетом, якщо вони є на вершині стеку операцій
                while (!operators.isEmpty() && hasPrecedence(tokens[i], operators.peek())) {
                    values.push(applyOperator(operators.pop(), values.pop(), values.pop()));
                }
                // Додаємо поточний оператор до стеку операцій
                operators.push(tokens[i]);
            } else {
                throw new Exception("Невідомий символ: " + tokens[i]);
            }
        }

        // Виконуємо залишок операцій у стеку
        while (!operators.isEmpty()) {
            values.push(applyOperator(operators.pop(), values.pop(), values.pop()));
        }

        // Результат повинен бути на вершині стеку значень
        return values.pop();
    }

    // Визначаємо пріоритет операцій
    private static int getPrecedence(char operator) {
        if (operator == '+' || operator == '-')
            return 1;
        if (operator == '*' || operator == '/')
            return 2;
        return 0;
    }

    // Перевіряємо, чи один оператор має вищий пріоритет за інший
    private static boolean hasPrecedence(char op1, char op2) {
        int precedence1 = getPrecedence(op1);
        int precedence2 = getPrecedence(op2);

        return precedence1 >= precedence2;
    }

    // Застосовуємо оператор до двох значень
    private static double applyOperator(char operator, double b, double a) {
        switch (operator) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) {
                    throw new ArithmeticException("Ділення на нуль");
                }
                return a / b;
        }
        return 0;
    }
}