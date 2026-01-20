#!/usr/bin/env python3

def evaluate_expression(expr):
    """Test the expression evaluation logic"""
    print(f"Evaluating: {expr}")
    
    # Parse the expression into numbers and operators
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
    
    # Add the last number
    if current_num:
        tokens.append(int(current_num))
    
    print(f"Tokens: {tokens}")
    
    if len(tokens) < 3:
        return tokens[0] if tokens else 0
    
    # Apply precedence: c (subtraction) first, then i (multiplication), then e (addition)
    
    # Step 1: Handle subtraction (c)
    i = 1
    while i < len(tokens):
        if tokens[i] == 'c':
            result = tokens[i-1] - tokens[i+1]
            print(f"Subtraction: {tokens[i-1]} - {tokens[i+1]} = {result}")
            tokens = tokens[:i-1] + [result] + tokens[i+2:]
            print(f"After subtraction: {tokens}")
        else:
            i += 2
    
    # Step 2: Handle multiplication (i)
    i = 1
    while i < len(tokens):
        if tokens[i] == 'i':
            result = tokens[i-1] * tokens[i+1]
            print(f"Multiplication: {tokens[i-1]} * {tokens[i+1]} = {result}")
            tokens = tokens[:i-1] + [result] + tokens[i+2:]
            print(f"After multiplication: {tokens}")
        else:
            i += 2
    
    # Step 3: Handle addition (e)
    i = 1
    while i < len(tokens):
        if tokens[i] == 'e':
            result = tokens[i-1] + tokens[i+1]
            print(f"Addition: {tokens[i-1]} + {tokens[i+1]} = {result}")
            tokens = tokens[:i-1] + [result] + tokens[i+2:]
            print(f"After addition: {tokens}")
        else:
            i += 2
    
    final_result = tokens[0]
    print(f"Final result: {final_result}")
    return final_result

# Test cases
test_expressions = [
    "5i3e2",      # 5*3+2 = 17
    "10c3i2",     # (10-3)*2 = 14
    "4e5i2",      # 4+(5*2) = 14
    "8c2e3",      # (8-2)+3 = 9
    "2i3i4",      # 2*3*4 = 24
    "10c5c2",     # (10-5)-2 = 3
    "1e2e3",      # 1+2+3 = 6
]

print("Testing expression evaluation:")
print("=" * 40)

for expr in test_expressions:
    print(f"\nTest: {expr}")
    result = evaluate_expression(expr)
    print(f"Result: {result}")
    print("-" * 20)