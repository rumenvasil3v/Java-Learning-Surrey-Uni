#!/usr/bin/env python3

def evaluate_expression(expr):
    """
    Correct algorithm based on Java implementation:
    - Normal precedence: i(*) > e(+), c(-)
    - Special rule: i(*) after c(-) gets lower precedence
    - Left associative for same precedence
    """
    print(f"Evaluating: {expr}")
    
    # Replace letters with operators
    expr_ops = expr.replace('c', '-').replace('i', '*').replace('e', '+')
    print(f"With operators: {expr_ops}")
    
    # Tokenize
    tokens = []
    i = 0
    while i < len(expr_ops):
        char = expr_ops[i]
        if char.isdigit():
            num = 0
            while i < len(expr_ops) and expr_ops[i].isdigit():
                num = num * 10 + int(expr_ops[i])
                i += 1
            tokens.append(str(num))
        elif char in '+-*':
            tokens.append(char)
            i += 1
        else:
            i += 1
    
    print(f"Tokens: {tokens}")
    
    # Convert to RPN using shunting-yard with special precedence rules
    output = []
    op_stack = []
    prev_op = None
    
    def get_precedence(op, prev_op):
        if op == '*' and prev_op == '-':
            return 1  # Lower precedence after '-'
        return 2 if op == '*' else 1
    
    for i, token in enumerate(tokens):
        if token.isdigit():
            output.append(token)
        else:
            # Look at what the previous operator was
            prev_op_for_this = None
            if i > 1:  # There's a previous operator
                prev_op_for_this = tokens[i-2] if i >= 2 else None
            
            precedence = get_precedence(token, prev_op_for_this)
            
            while (op_stack and 
                   op_stack[-1] != '(' and
                   get_precedence(op_stack[-1], None) >= precedence):
                output.append(op_stack.pop())
            
            op_stack.append(token)
            prev_op = token
    
    while op_stack:
        output.append(op_stack.pop())
    
    print(f"RPN: {output}")
    
    # Evaluate RPN
    eval_stack = []
    for token in output:
        if token.isdigit():
            eval_stack.append(int(token))
        else:
            b = eval_stack.pop()
            a = eval_stack.pop()
            if token == '+':
                result = a + b
            elif token == '-':
                result = a - b
            elif token == '*':
                result = a * b
            eval_stack.append(result)
    
    final_result = eval_stack[0]
    print(f"Final result: {final_result}")
    return final_result

# Test with Java examples
test_cases = [
    ("4c6i7", -38),
    ("5c9i5e8", -12),
    ("4c3i6c8", -2),
    ("8c4e2", 6),
    ("3i8i9", 216),
    ("1e2c2c5e6c3", -1),
    # Additional failed cases from our runs
    ("1e9i4", 37),
    ("6e4i2", 14),
    ("3e4i7", 31),
    ("2e2i2", 6),
    ("4e1i7", 11),
    ("5e7i3", 26),
]

print("Testing Java-based algorithm:")
print("=" * 50)

for expr, expected in test_cases:
    print(f"\nTest: {expr} (expected: {expected})")
    result = evaluate_expression(expr)
    if result == expected:
        print("✓ CORRECT!")
    else:
        print(f"✗ WRONG! Expected {expected}, got {result}")
    print("-" * 30)