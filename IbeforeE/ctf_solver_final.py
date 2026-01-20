#!/usr/bin/env python3
import socket
import re
import time

def evaluate_expression(expr):
    """
    Direct translation of the Java algorithm
    """
    print(f"Evaluating: {expr}")
    
    # Replace letters
    expr = expr.replace('c', '-').replace('i', '*').replace('e', '+')
    
    # Tokenize: numbers and operators
    tokens = []
    i = 0
    while i < len(expr):
        c = expr[i]
        if c.isdigit():
            num = 0
            while i < len(expr) and expr[i].isdigit():
                num = num * 10 + int(expr[i])
                i += 1
            tokens.append(str(num))
        elif c in '+-*':
            tokens.append(c)
            i += 1
        else:
            i += 1
    
    # Shunting-yard: output queue (RPN), operator stack
    output = []
    op_stack = []
    prev_op = None
    
    def is_number(s):
        return s.isdigit()
    
    def get_precedence(op, prev_op):
        if op == '*' and prev_op is not None and prev_op == '-':
            return 1  # low precedence after c
        return 2 if op == '*' else 1
    
    def is_left_associative(op):
        return True  # all are left-associative
    
    for token in tokens:
        if is_number(token):
            output.append(token)
        else:
            # Determine precedence
            precedence = get_precedence(token, prev_op)
            while (op_stack and 
                   op_stack[-1] != '(' and
                   (get_precedence(op_stack[-1], prev_op) > precedence or
                    (get_precedence(op_stack[-1], prev_op) == precedence and is_left_associative(token)))):
                output.append(op_stack.pop())
            
            op_stack.append(token)
            prev_op = token
    
    while op_stack:
        output.append(op_stack.pop())
    
    # Evaluate RPN
    eval_stack = []
    for token in output:
        if is_number(token):
            eval_stack.append(int(token))
        else:
            b = eval_stack.pop()
            a = eval_stack.pop()
            if token == '+':
                eval_stack.append(a + b)
            elif token == '-':
                eval_stack.append(a - b)
            elif token == '*':
                eval_stack.append(a * b)
    
    result = eval_stack.pop()
    print(f"Final result: {result}")
    return result

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
                    print("✗ Wrong answer - restarting from 0")
                    correct_count = 0
                elif 'flag' in data.lower() or 'ctf{' in data.lower() or 'bae{' in data.lower():
                    print(f"🎉 FLAG FOUND: {data}")
                    break
                elif 'slow' in data.lower():
                    print("Too slow! Restarting...")
                    correct_count = 0
                
                # Very small delay
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