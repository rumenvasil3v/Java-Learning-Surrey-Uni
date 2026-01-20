import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;

public class CTFSolver {
    private static final String HOST = "ibeforee.baectf.com";
    private static final int PORT = 443;
    private static final String PASSWORD = "shamefulrobot";
    
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    
    public CTFSolver() throws IOException {
        socket = new Socket(HOST, PORT);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream(), true);
    }
    
    public static int evaluate(String expr) {
        System.out.println("Evaluating: " + expr);
        
        // Replace letters with operators
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
                    int topPrec = getPrecedence(topOp, null); // Don't pass prevOp for comparison
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
        
        int result = evalStack.pop();
        System.out.println("Final result: " + result);
        return result;
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
    
    public void solve() throws IOException {
        System.out.println("Connecting to " + HOST + ":" + PORT);
        
        // Read password prompt
        String prompt = reader.readLine();
        System.out.println("Server: " + prompt);
        
        // Send password
        System.out.println("Sending password: " + PASSWORD);
        writer.println(PASSWORD);
        
        // Read first challenge
        String firstChallenge = reader.readLine();
        System.out.println("First challenge: " + firstChallenge);
        
        int correctCount = 0;
        String currentData = firstChallenge;
        
        while (correctCount < 101) {
            // Extract expression from current data
            Pattern pattern = Pattern.compile("(\\d+[ice]\\d+(?:[ice]\\d+)*)");
            Matcher matcher = pattern.matcher(currentData);
            
            if (matcher.find()) {
                String expression = matcher.group(1);
                System.out.println("\nChallenge " + (correctCount + 1) + "/101: " + expression);
                
                // Calculate result
                int result = evaluate(expression);
                System.out.println("Sending: " + result);
                
                // Send answer
                writer.println(result);
                
                // Read response
                currentData = reader.readLine();
                System.out.println("Response: " + currentData);
                
                // Check response
                if (currentData.toLowerCase().contains("correct") || 
                    currentData.toLowerCase().contains("right")) {
                    correctCount++;
                    System.out.println("✓ Correct! Progress: " + correctCount + "/101");
                } else if (currentData.toLowerCase().contains("wrong")) {
                    System.out.println("✗ Wrong answer - restarting from 0");
                    correctCount = 0;
                } else if (currentData.toLowerCase().contains("flag") || 
                          currentData.toLowerCase().contains("ctf{") || 
                          currentData.toLowerCase().contains("bae{") ||
                          currentData.toLowerCase().contains("key is")) {
                    System.out.println("🎉 FLAG FOUND: " + currentData);
                    
                    // Try to read more data for the complete flag
                    try {
                        String additionalData = reader.readLine();
                        if (additionalData != null) {
                            System.out.println("Additional data: " + additionalData);
                        }
                    } catch (Exception e) {
                        // Ignore if no more data
                    }
                    break;
                } else if (currentData.toLowerCase().contains("slow")) {
                    System.out.println("Too slow! Restarting...");
                    correctCount = 0;
                } else if (currentData.toLowerCase().contains("win")) {
                    System.out.println("🎉 WE WON! " + currentData);
                    
                    // Try to read the key
                    try {
                        String keyData = reader.readLine();
                        if (keyData != null) {
                            System.out.println("THE KEY IS: " + keyData);
                        }
                    } catch (Exception e) {
                        // Ignore if no more data
                    }
                    break;
                }
                
                // Small delay to avoid being too fast
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } else {
                System.out.println("No expression found, trying to get new data...");
                currentData = reader.readLine();
                if (currentData != null) {
                    System.out.println("New data: " + currentData);
                }
            }
        }
    }
    
    public void close() throws IOException {
        if (reader != null) reader.close();
        if (writer != null) writer.close();
        if (socket != null) socket.close();
    }
    
    public static void main(String[] args) {
        CTFSolver solver = null;
        try {
            solver = new CTFSolver();
            solver.solve();
            System.out.println("Challenge completed successfully!");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (solver != null) {
                try {
                    solver.close();
                } catch (IOException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }
}