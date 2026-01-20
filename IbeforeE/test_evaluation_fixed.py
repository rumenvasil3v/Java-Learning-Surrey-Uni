#!/usr/bin/env python3

def evaluate_expression(expr):
    """
    Evaluate expression with correct precedence rules:
    - When 'e' and 'i' are present: do 'i' first
    - When 'i' and 'c' are present: do 'c' first, then 'i'
    - When 'e' and 'c' are present: work left to right
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
    
    # Check what operators we have
    operators = [token for token in tokens if isinstance(token, str)]
    has_i = 'i' in operators
    has_c = 'c' in operators
    has_e = 'e' in operators
    
    print(f"Operators present: i={has_i}, c={has_c}, e={has_e}")
    
    # Apply precedence rules
    if has_i and has_c:
        # Rule: when 'i' and 'c' are present, do 'c' first, then 'i'
        print("Rule: c before i")
        
        # First handle all 'c' (subtraction)
        i = 1
        while i < len(tokens):
            if tokens[i] == 'c':
                result = tokens[i-1] - tokens[i+1]
                print(f"Subtraction: {tokens[i-1]} - {tokens[i+1]} = {result}")
                tokens = tokens[:i-1] + [result] + tokens[i+2:]
                print(f"After subtraction: {tokens}")
            else:
                i += 2
        
        # Then handle all 'i' (multiplication)
        i = 1
        while i < len(tokens):
            if tokens[i] == 'i':
                result = tokens[i-1] * tokens[i+1]
                print(f"Multiplication: {tokens[i-1]} * {tokens[i+1]} = {result}")
                tokens = tokens[:i-1] + [result] + tokens[i+2:]
                print(f"After multiplication: {tokens}")
            else:
                i += 2
        
        # Finally handle all 'e' (addition)
        i = 1
        while i < len(tokens):
            if tokens[i] == 'e':
                result = tokens[i-1] + tokens[i+1]
                print(f"Addition: {tokens[i-1]} + {tokens[i+1]} = {result}")
                tokens = tokens[:i-1] + [result] + tokens[i+2:]
                print(f"After addition: {tokens}")
            else:
                i += 2
                
    elif has_e and has_i:
        # Rule: when 'e' and 'i' are present, do 'i' first
        print("Rule: i before e")
        
        # First handle all 'i' (multiplication)
        i = 1
        while i < len(tokens):
            if tokens[i] == 'i':
                result = tokens[i-1] * tokens[i+1]
                print(f"Multiplication: {tokens[i-1]} * {tokens[i+1]} = {result}")
                tokens = tokens[:i-1] + [result] + tokens[i+2:]
                print(f"After multiplication: {tokens}")
            else:
                i += 2
        
        # Then handle all 'e' (addition)
        i = 1
        while i < len(tokens):
            if tokens[i] == 'e':
                result = tokens[i-1] + tokens[i+1]
                print(f"Addition: {tokens[i-1]} + {tokens[i+1]} = {result}")
                tokens = tokens[:i-1] + [result] + tokens[i+2:]
                print(f"After addition: {tokens}")
            else:
                i += 2
                
    elif has_e and has_c:
        # Rule: when 'e' and 'c' are present, work left to right
        print("Rule: e and c left to right")
        
        # Process left to right
        i = 1
        while i < len(tokens):
            if tokens[i] in ['e', 'c']:
                if tokens[i] == 'e':
                    result = tokens[i-1] + tokens[i+1]
                    print(f"Addition: {tokens[i-1]} + {tokens[i+1]} = {result}")
                else:  # tokens[i] == 'c'
                    result = tokens[i-1] - tokens[i+1]
                    print(f"Subtraction: {tokens[i-1]} - {tokens[i+1]} = {result}")
                
                tokens = tokens[:i-1] + [result] + tokens[i+2:]
                print(f"After operation: {tokens}")
            else:
                i += 2
    else:
        # Only one type of operator, process normally
        if has_i:
            print("Only multiplication")
            i = 1
            while i < len(tokens):
                if tokens[i] == 'i':
                    result = tokens[i-1] * tokens[i+1]
                    print(f"Multiplication: {tokens[i-1]} * {tokens[i+1]} = {result}")
                    tokens = tokens[:i-1] + [result] + tokens[i+2:]
                    print(f"After multiplication: {tokens}")
                else:
                    i += 2
        elif has_c:
            print("Only subtraction")
            i = 1
            while i < len(tokens):
                if tokens[i] == 'c':
                    result = tokens[i-1] - tokens[i+1]
                    print(f"Subtraction: {tokens[i-1]} - {tokens[i+1]} = {result}")
                    tokens = tokens[:i-1] + [result] + tokens[i+2:]
                    print(f"After subtraction: {tokens}")
                else:
                    i += 2
        elif has_e:
            print("Only addition")
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
    "5i3e2",      # i before e: (5*3)+2 = 17
    "10c3i2",     # c before i: (10-3)*2 = 14
    "4e5i2",      # i before e: 4+(5*2) = 14
    "8c2e3",      # e and c left to right: (8-2)+3 = 9
    "2i3i4",      # only i: 2*3*4 = 24
    "10c5c2",     # only c: (10-5)-2 = 3
    "1e2e3",      # only e: 1+2+3 = 6
    "6i7e3",      # i before e: (6*7)+3 = 45
    "9e4c2",      # e and c left to right: (9+4)-2 = 11
]

print("Testing corrected expression evaluation:")
print("=" * 50)

for expr in test_expressions:
    print(f"\nTest: {expr}")
    result = evaluate_expression(expr)
    print(f"Result: {result}")
    print("-" * 30)