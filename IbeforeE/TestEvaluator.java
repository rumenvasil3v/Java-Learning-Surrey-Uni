public class TestEvaluator {
    
    public static void main(String[] args) {
        // Test cases from the CTF challenge
        String[][] testCases = {
            {"4c6i7", "-38"},
            {"5c9i5e8", "-12"},
            {"4c3i6c8", "-2"},
            {"8c4e2", "6"},
            {"3i8i9", "216"},
            {"1e2c2c5e6c3", "-1"},
            {"1e9i4", "37"},
            {"6e4i2", "14"},
            {"3e4i7", "31"},
            {"2e2i2", "6"},
            {"4e1i7", "11"},
            {"5e7i3", "26"}
        };
        
        System.out.println("Testing Java CTF Solver Algorithm:");
        System.out.println("=" .repeat(50));
        
        int passed = 0;
        int total = testCases.length;
        
        for (String[] testCase : testCases) {
            String expression = testCase[0];
            int expected = Integer.parseInt(testCase[1]);
            
            System.out.println("\nTest: " + expression + " (expected: " + expected + ")");
            int result = CTFSolver.evaluate(expression);
            
            if (result == expected) {
                System.out.println("✓ PASSED!");
                passed++;
            } else {
                System.out.println("✗ FAILED! Expected " + expected + ", got " + result);
            }
            System.out.println("-".repeat(30));
        }
        
        System.out.println("\nResults: " + passed + "/" + total + " tests passed");
        
        if (passed == total) {
            System.out.println("🎉 All tests passed! The algorithm is working correctly.");
        } else {
            System.out.println("❌ Some tests failed. Check the algorithm implementation.");
        }
    }
}