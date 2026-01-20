package main;

public class Test {

    public static void main(String[] args) {
        try {
            ServerClient client = new ServerClient();
            System.out.println("Connecting to server...");
            String expression = client.getExpression();
            System.out.println("Received expression: '" + expression + "'");
            System.out.println("Expression length: " + (expression != null ? expression.length() : "null"));

            if (expression != null && !expression.isEmpty()) {
                FunArithmetic calculator = new FunArithmetic();
                int result = calculator.runCalculation(expression);
                System.out.println("Calculated result: " + result);
            } else {
                System.out.println("No expression received from server!");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
