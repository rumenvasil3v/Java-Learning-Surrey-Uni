#!/usr/bin/env python3

def debug_expression(expr, expected):
    print(f"\nDebugging: {expr} = {expected}")
    
    # Let's try different approaches
    
    # Method 1: Pure left to right
    tokens = []
    current_num = ""
    for char in expr:
        if char.isdigit():
            current_num += char
        elif char in 'ice':
            if current_num:
                tokens.append(int(current_num))
                current_num = ""
            tokens.append(char)
    if current_num:
        tokens.append(int(current_num))
    
    print(f"Tokens: {tokens}")
    
    # Pure left to right
    tokens_lr = tokens[:]
    i = 1
    while i < len(tokens_lr):
        if tokens_lr[i] == 'i':
            result = tokens_lr[i-1] * tokens_lr[i+1]
        elif tokens_lr[i] == 'c':
            result = tokens_lr[i-1] - tokens_lr[i+1]
        elif tokens_lr[i] == 'e':
            result = tokens_lr[i-1] + tokens_lr[i+1]
        
        print(f"Left-to-right: {tokens_lr[i-1]} {tokens_lr[i]} {tokens_lr[i+1]} = {result}")
        tokens_lr = tokens_lr[:i-1] + [result] + tokens_lr[i+2:]
        print(f"After: {tokens_lr}")
    
    result_lr = tokens_lr[0] if tokens_lr else 0
    print(f"Left-to-right result: {result_lr}")
    
    if result_lr == expected:
        print("✓ LEFT-TO-RIGHT MATCHES!")
        return True
    
    return False

# Test the problematic case
debug_expression("2i5c7c8i3", -15)

# Let's also test a few more to see if left-to-right is the pattern
test_cases = [
    ("8i2c5e3", 14),
    ("4c3i6c8", -2),
    ("6c5i2c2", 0),
    ("2e2c4i5", 0),
]

for expr, expected in test_cases:
    debug_expression(expr, expected)