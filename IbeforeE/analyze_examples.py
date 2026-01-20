#!/usr/bin/env python3

# Let's analyze the examples to understand the correct precedence
examples = [
    ("8c4e2", 6),
    ("8c9c4", -5),
    ("3i8i9", 216),
    ("6i4e2", 26),
    ("5i5i7", 175),
    ("6c1e6", 11),
    ("7i1i9", 63),
    ("4e4i7", 32),
    ("5i4e8e4", 32),
    ("5c6e8i9", 71),
    ("7i2e1c8", 7),
    ("8i1c9c5", -6),
    ("4c3i6c8", -2),
    ("6c5i2c2", 0),
    ("2e2c4i5", 0),
    ("1i5i6i8", 240),
    ("6c9c1e8", 4),
    ("8i2i3e4", 52),
    ("3i5e1i6", 21),
    ("5c9i5e8", -12),
    ("8e9i5i4", 188),
    ("8i2c5e3", 14),
    ("2i5c7c8i3", -15),
    ("1c6i6e9c7", -28),
    ("2i1e3c4c3", -2),
    ("1i3i6i9e9", 171),
    ("1e5i9e5c8", 43),
    ("1i3i4i5i2", 120),
    ("7i8i7i6c3", 2349),
    ("3e2e3i9i9", 248),
    ("6c7i2e7c7", -2),
    ("5i6c5i6i6", 900),
    ("5e9c2c4e8", 16),
    ("3i7c8i5i4", 260),
    ("1i2c5i8c1", -25),
    ("1c1e2e3i2", 8),
    ("1e9e5c7i2i9", 144),
    ("8i8e1i5c1i3", 204),
    ("4e2e3e8i5i1", 49),
    ("4i1i7c9i6c8", 106),
    ("3c1i2c5c8c1", -10),
    ("3i7c2i6e1c9", 106),
    ("3e8i3c9c8c8", 2),
    ("3e8i5i9e3c9", 357),
    ("4i6e6i9i4e4", 244),
    ("1e2c2c5e6c3", -1),  # This was wrong in my calculation
]

def manual_check(expr, expected):
    """Let's manually check some expressions to understand the pattern"""
    print(f"\nAnalyzing: {expr} = {expected}")
    
    # Let's try different interpretations
    
    # Method 1: Standard math precedence (*, then +/- left to right)
    try:
        standard = expr.replace('i', '*').replace('c', '-').replace('e', '+')
        result1 = eval(standard)
        print(f"  Standard math: {standard} = {result1}")
    except:
        print(f"  Standard math: failed")
    
    # Method 2: All multiplication first, then left to right
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
    
    print(f"  Tokens: {tokens}")
    
    # Try: all i first, then left to right for c and e
    tokens_copy = tokens[:]
    
    # First all multiplications
    i = 1
    while i < len(tokens_copy):
        if tokens_copy[i] == 'i':
            result = tokens_copy[i-1] * tokens_copy[i+1]
            tokens_copy = tokens_copy[:i-1] + [result] + tokens_copy[i+2:]
        else:
            i += 2
    
    # Then left to right for c and e
    i = 1
    while i < len(tokens_copy):
        if tokens_copy[i] == 'c':
            result = tokens_copy[i-1] - tokens_copy[i+1]
            tokens_copy = tokens_copy[:i-1] + [result] + tokens_copy[i+2:]
        elif tokens_copy[i] == 'e':
            result = tokens_copy[i-1] + tokens_copy[i+1]
            tokens_copy = tokens_copy[:i-1] + [result] + tokens_copy[i+2:]
        else:
            i += 2
    
    result2 = tokens_copy[0] if tokens_copy else 0
    print(f"  Method 2 (i first, then left-right): {result2}")
    
    if result2 == expected:
        print(f"  ✓ Method 2 MATCHES!")
    
    return result2 == expected

# Test key examples
key_examples = [
    ("1e2c2c5e6c3", -1),
    ("8i2c5e3", 14),
    ("2i5c7c8i3", -15),
    ("6i4e2", 26),
    ("4e4i7", 32),
]

print("Testing key examples:")
print("=" * 50)

for expr, expected in key_examples:
    manual_check(expr, expected)