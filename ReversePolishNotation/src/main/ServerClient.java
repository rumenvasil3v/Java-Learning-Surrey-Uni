package main;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class ServerClient {

    public String getExpression() throws Exception {
        // 1. Create socket connection
        Socket socket = new Socket("ibeforee.baectf.com", 443);

        // 2. Set up input/output streams
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

        // 3. Send password immediately
        writer.println("shamefulrobot");

        // 4. Read all server responses
        String line1 = reader.readLine();
        System.out.println("Line 1: " + line1);

        String line2 = reader.readLine();
        System.out.println("Line 2: " + line2);

        // 5. Extract the expression from the appropriate line
        String expression = null;
        if (line2 != null && line2.contains("Evaluate:")) {
            expression = line2.substring(line2.indexOf("Evaluate: ") + 10);
        } else if (line1 != null && line1.contains("Evaluate:")) {
            expression = line1.substring(line1.indexOf("Evaluate: ") + 10);
        }

        // 7. Close connection
        reader.close();
        writer.close();
        socket.close();

        // 8. Return result
        return expression;
    }
}
