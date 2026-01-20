#!/usr/bin/env python3

def evaluate_expression(expr):
    """
    Correct algorithm based on Java implementation:
    The key insight: track the previous operator to determine precedence
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
    
    # Shunting-yard algorithm with special precedence
    output = []
    op_stack = []
    prev_op = None
    
    def get_precedence(op, prev_op):
        if op == '*' and prev_op is not None and prev_op == '-':
            return 1  # Lower precedence when * comes after -
        return 2 if op == '*' else 1
    
    def is_left_associative(op):
        return True
    
    for token in tokens:
        if token.isdigit():
            output.append(token)
        else:
            # Current operator
            precedence = get_precedence(token, prev_op)
            
            # Pop operators with higher or equal precedence
            while op_stack:
                top_op = op_stack[-1]
                if top_op == '(':
                    break
                
                top_precedence = get_precedence(top_op, prev_op)
                
                if (top_precedence > precedence or 
                    (top_precedence == precedence and is_left_associative(token))):
                    output.append(op_stack.pop())
                else:
                    break
            
            op_stack.append(token)
            prev_op = token
    
    # Pop remaining operators
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

# Test the specific failing case
print("Testing 4c6i7:")
result = evaluate_expression("4c6i7")
print(f"Result: {result}, Expected: -38")

# Let's manually trace what should happen:
# 4c6i7 -> 4-6*7
# With special rule: * after - gets lower precedence
# So it should be: (4-6)*7 = -2*7 = -14
# But expected is -38... 

# Wait, let me check if it's actually: 4-(6*7) = 4-42 = -38
# That would mean normal precedence, not the special rule

print("\nLet's try normal precedence:")
def normal_eval(expr):
    expr_ops = expr.replace('c', '-').replace('i', '*').replace('e', '+')
    return eval(expr_ops)

print(f"Normal eval of 4c6i7: {normal_eval('4c6i7')}")

# Test all cases with normal precedence
test_cases = [
    ("4c6i7", -38),
    ("5c9i5e8", -12),  # 5-9*5+8 = 5-45+8 = -32 (not -12)
]

print("\nTesting normal precedence:")
for expr, expected in test_cases:
    result = normal_eval(expr)
    print(f"{expr}: {result} (expected: {expected})")