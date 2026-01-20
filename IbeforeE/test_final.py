#!/usr/bin/env python3

def evaluate_expression(expr):
    """
    Correct algorithm:
    1. First do all multiplications (i)
    2. Then do additions (e) and subtractions (c) from left to right
    """
    print(f"Evaluating: {expr}")
    
    # Parse into tokens
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
    
    if len(tokens) < 3:
        return tokens[0] if tokens else 0
    
    # Step 1: Handle all multiplications (i) first
    i = 1
    while i < len(tokens):
        if tokens[i] == 'i':
            result = tokens[i-1] * tokens[i+1]
            print(f"Multiplication: {tokens[i-1]} * {tokens[i+1]} = {result}")
            tokens = tokens[:i-1] + [result] + tokens[i+2:]
            print(f"After multiplication: {tokens}")
        else:
            i += 2
    
    # Step 2: Handle additions (e) and subtractions (c) from left to right
    i = 1
    while i < len(tokens):
        if tokens[i] == 'c':
            result = tokens[i-1] - tokens[i+1]
            print(f"Subtraction: {tokens[i-1]} - {tokens[i+1]} = {result}")
            tokens = tokens[:i-1] + [result] + tokens[i+2:]
            print(f"After subtraction: {tokens}")
        elif tokens[i] == 'e':
            result = tokens[i-1] + tokens[i+1]
            print(f"Addition: {tokens[i-1]} + {tokens[i+1]} = {result}")
            tokens = tokens[:i-1] + [result] + tokens[i+2:]
            print(f"After addition: {tokens}")
        else:
            i += 2
    
    final_result = tokens[0]
    print(f"Final result: {final_result}")
    return final_result

# Test the problematic examples
test_cases = [
    ("1e2c2c5e6c3", -1),
    ("8i2c5e3", 14),
    ("2i5c7c8i3", -15),
    ("6i4e2", 26),
    ("4e4i7", 32),
    ("4c6i7", None),  # The new one we need to solve
]

print("Testing final algorithm:")
print("=" * 40)

for expr, expected in test_cases:
    print(f"\nTest: {expr}")
    if expected is not None:
        print(f"Expected: {expected}")
    result = evaluate_expression(expr)
    if expected is not None:
        if result == expected:
            print("✓ CORRECT!")
        else:
            print(f"✗ WRONG! Expected {expected}, got {result}")
    print("-" * 30)