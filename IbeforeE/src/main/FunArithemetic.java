package main;
import java.util.*;

public class FunArithemetic {
    public static int evaluate(String expr) {
        // Replace letters
        expr = expr.replace('c', '-').replace('i', '*').replace('e', '+');
        
        // Tokenize: numbers and operators
        Queue<String> tokens = new LinkedList<>();
        int i = 0;
        while (i < expr.length()) {
            char c = expr.charAt(i);
            if (Character.isDigit(c)) {
                int num = 0;
                while (i < expr.length() && Character.isDigit(expr.charAt(i))) {
                    num = num * 10 + (expr.charAt(i) - '0');
                    i++;
                }
                tokens.add(String.valueOf(num));
            } else if (c == '+' || c == '-' || c == '*') {
                tokens.add(String.valueOf(c));
                i++;
            } else {
                i++; // skip invalid
            }
        }

        // Shunting-yard: output queue (RPN), operator stack
        Queue<String> output = new LinkedList<>();
        Stack<String> opStack = new Stack<>();
        String prevOp = null;

        while (!tokens.isEmpty()) {
            String token = tokens.poll();

            if (isNumber(token)) {
                output.add(token);
            } else {
                // Determine precedence
                int precedence = getPrecedence(token, prevOp);
                
                while (!opStack.isEmpty() && !opStack.peek().equals("(")) {
                    String topOp = opStack.peek();
                    int topPrec = getPrecedence(topOp, prevOp);
                    if (topPrec > precedence ||
                        (topPrec == precedence && isLeftAssociative(token))) {
                        output.add(opStack.pop());
                    } else {
                        break;
                    }
                }
                opStack.push(token);
                prevOp = token;
            }
        }

        while (!opStack.isEmpty()) {
            output.add(opStack.pop());
        }

        // Evaluate RPN
        Stack<Integer> evalStack = new Stack<>();
        while (!output.isEmpty()) {
            String token = output.poll();
            if (isNumber(token)) {
                evalStack.push(Integer.parseInt(token));
            } else {
                int b = evalStack.pop();
                int a = evalStack.pop();
                switch (token) {
                    case "+" -> evalStack.push(a + b);
                    case "-" -> evalStack.push(a - b);
                    case "*" -> evalStack.push(a * b);
                }
            }
        }
        return evalStack.pop();
    }

    private static boolean isNumber(String s) {
        return s.matches("\\d+");
    }

    private static int getPrecedence(String op, String prevOp) {
        if (op.equals("*") && prevOp != null && prevOp.equals("-")) {
            return 1; // low precedence after c
        }
        return op.equals("*") ? 2 : 1;
    }

    private static boolean isLeftAssociative(String op) {
        return true; // all are left-associative
    }

    // Test
    public static void main(String[] args) {
        System.out.println(evaluate("4c1e2c7i9i9c2i8c4i8"));     // → -38
    }
}