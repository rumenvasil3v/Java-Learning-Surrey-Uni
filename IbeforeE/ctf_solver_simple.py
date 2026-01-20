#!/usr/bin/env python3
import socket
import re
import time

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
    
    if len(tokens) < 3:
        return tokens[0] if tokens else 0
    
    # Check what operators we have
    operators = [token for token in tokens if isinstance(token, str)]
    has_i = 'i' in operators
    has_c = 'c' in operators
    has_e = 'e' in operators
    
    # Apply precedence rules
    if has_i and has_c:
        # Rule: when 'i' and 'c' are present, do 'c' first, then 'i'
        
        # First handle all 'c' (subtraction)
        i = 1
        while i < len(tokens):
            if tokens[i] == 'c':
                result = tokens[i-1] - tokens[i+1]
                tokens = tokens[:i-1] + [result] + tokens[i+2:]
            else:
                i += 2
        
        # Then handle all 'i' (multiplication)
        i = 1
        while i < len(tokens):
            if tokens[i] == 'i':
                result = tokens[i-1] * tokens[i+1]
                tokens = tokens[:i-1] + [result] + tokens[i+2:]
            else:
                i += 2
        
        # Finally handle all 'e' (addition)
        i = 1
        while i < len(tokens):
            if tokens[i] == 'e':
                result = tokens[i-1] + tokens[i+1]
                tokens = tokens[:i-1] + [result] + tokens[i+2:]
            else:
                i += 2
                
    elif has_e and has_i:
        # Rule: when 'e' and 'i' are present, do 'i' first
        
        # First handle all 'i' (multiplication)
        i = 1
        while i < len(tokens):
            if tokens[i] == 'i':
                result = tokens[i-1] * tokens[i+1]
                tokens = tokens[:i-1] + [result] + tokens[i+2:]
            else:
                i += 2
        
        # Then handle all 'e' (addition)
        i = 1
        while i < len(tokens):
            if tokens[i] == 'e':
                result = tokens[i-1] + tokens[i+1]
                tokens = tokens[:i-1] + [result] + tokens[i+2:]
            else:
                i += 2
                
    elif has_e and has_c:
        # Rule: when 'e' and 'c' are present, work left to right
        
        # Process left to right
        i = 1
        while i < len(tokens):
            if tokens[i] in ['e', 'c']:
                if tokens[i] == 'e':
                    result = tokens[i-1] + tokens[i+1]
                else:  # tokens[i] == 'c'
                    result = tokens[i-1] - tokens[i+1]
                
                tokens = tokens[:i-1] + [result] + tokens[i+2:]
            else:
                i += 2
    else:
        # Only one type of operator, process normally
        if has_i:
            i = 1
            while i < len(tokens):
                if tokens[i] == 'i':
                    result = tokens[i-1] * tokens[i+1]
                    tokens = tokens[:i-1] + [result] + tokens[i+2:]
                else:
                    i += 2
        elif has_c:
            i = 1
            while i < len(tokens):
                if tokens[i] == 'c':
                    result = tokens[i-1] - tokens[i+1]
                    tokens = tokens[:i-1] + [result] + tokens[i+2:]
                else:
                    i += 2
        elif has_e:
            i = 1
            while i < len(tokens):
                if tokens[i] == 'e':
                    result = tokens[i-1] + tokens[i+1]
                    tokens = tokens[:i-1] + [result] + tokens[i+2:]
                else:
                    i += 2
    
    return tokens[0]

def main():
    host = "ibeforee.baectf.com"
    port = 443
    password = "shamefulrobot"
    
    print(f"Connecting to {host}:{port}")
    
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    sock.connect((host, port))
    
    try:
        # Read password prompt
        data = sock.recv(1024).decode('utf-8')
        print(f"Server: {data}")
        
        # Send password
        sock.send((password + '\n').encode('utf-8'))
        
        # Read first challenge
        data = sock.recv(1024).decode('utf-8')
        print(f"First challenge: {data}")
        
        correct_count = 0
        
        while correct_count < 101:
            # Extract expression from current data
            expr_match = re.search(r'(\d+[ice]\d+(?:[ice]\d+)*)', data)
            
            if expr_match:
                expression = expr_match.group(1)
                print(f"\nChallenge {correct_count + 1}/101: {expression}")
                
                # Calculate result
                result = evaluate_expression(expression)
                print(f"Sending: {result}")
                
                # Send answer
                sock.send(f"{result}\n".encode('utf-8'))
                
                # Read response
                data = sock.recv(1024).decode('utf-8')
                print(f"Response: {data}")
                
                # Check response
                if 'correct' in data.lower() or 'right' in data.lower():
                    correct_count += 1
                    print(f"✓ Correct! Progress: {correct_count}/101")
                elif 'wrong' in data.lower():
                    print("✗ Wrong answer")
                elif 'flag' in data.lower() or 'ctf{' in data.lower() or 'bae{' in data.lower():
                    print(f"🎉 FLAG FOUND: {data}")
                    break
                elif 'slow' in data.lower():
                    print("Too slow! Restarting...")
                    correct_count = 0
                
                # Small delay
                time.sleep(0.01)
            else:
                print("No expression found, trying to get new data...")
                data = sock.recv(1024).decode('utf-8')
                print(f"New data: {data}")
    
    except Exception as e:
        print(f"Error: {e}")
    finally:
        sock.close()

if __name__ == "__main__":
    main()