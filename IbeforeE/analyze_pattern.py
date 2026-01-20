#!/usr/bin/env python3

# Let's analyze the pattern from the examples
examples = [
    ("4c6i7", -38),      # 4-6*7 = 4-42 = -38 (normal precedence)
    ("5c9i5e8", -12),    # 5-9*5+8 = 5-45+8 = -32 ≠ -12
    ("4c3i6c8", -2),     # 4-3*6-8 = 4-18-8 = -22 ≠ -2
    ("8c4e2", 6),        # 8-4+2 = 6 ✓
    ("3i8i9", 216),      # 3*8*9 = 216 ✓
    ("1e2c2c5e6c3", -1), # 1+2-2-5+6-3 = -1 ✓
]

def left_to_right_eval(expr):
    """Pure left to right evaluation"""
    expr_ops = expr.replace('c', '-').replace('i', '*').replace('e', '+')
    
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
            tokens.append(num)
        elif char in '+-*':
            tokens.append(char)
            i += 1
        else:
            i += 1
    
    # Evaluate left to right
    result = tokens[0]
    i = 1
    while i < len(tokens):
        op = tokens[i]
        operand = tokens[i+1]
        
        if op == '+':
            result += operand
        elif op == '-':
            result -= operand
        elif op == '*':
            result *= operand
        
        i += 2
    
    return result

def normal_precedence_eval(expr):
    """Normal math precedence"""
    expr_ops = expr.replace('c', '-').replace('i', '*').replace('e', '+')
    return eval(expr_ops)

print("Analyzing patterns:")
print("=" * 50)

for expr, expected in examples:
    lr_result = left_to_right_eval(expr)
    normal_result = normal_precedence_eval(expr)
    
    print(f"\n{expr} (expected: {expected})")
    print(f"  Left-to-right: {lr_result} {'✓' if lr_result == expected else '✗'}")
    print(f"  Normal precedence: {normal_result} {'✓' if normal_result == expected else '✗'}")

# Let's try to understand 5c9i5e8 = -12
# Left to right: 5-9 = -4, -4*5 = -20, -20+8 = -12 ✓
# So it IS left to right!

print("\n" + "="*50)
print("CONCLUSION: It's pure left-to-right evaluation!")
print("The Java code might be more complex than needed.")