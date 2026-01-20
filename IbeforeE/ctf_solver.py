#!/usr/bin/env python3
import socket
import re
import time

def connect_to_server(host, port):
    """Connect to the CTF server"""
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    sock.connect((host, port))
    return sock

def send_password(sock, password):
    """Send the password to authenticate"""
    # Read initial prompt
    response = sock.recv(1024).decode('utf-8')
    print(f"Server: {response}")
    
    # Send password
    sock.send((password + '\n').encode('utf-8'))
    response = sock.recv(1024).decode('utf-8')
    print(f"Server: {response}")

def evaluate_expression(expr):
    """
    Evaluate mathematical expression with custom operators:
    i -> multiplication (*)
    c -> subtraction (-)
    e -> addition (+)
    
    Rule: "i before e" - multiply before add, but if c is present, multiply after subtract
    """
    # Replace custom operators with standard ones
    expr = expr.replace('i', '*').replace('c', '-').replace('e', '+')
    
    # Handle precedence: "i before e" but "c before i"
    # This means: subtraction, then multiplication, then addition
    # We'll use Python's eval with proper precedence
    try:
        result = eval(expr)
        return int(result)
    except:
        # If eval fails, let's parse manually
        return manual_evaluate(expr)

def manual_evaluate(expr):
    """Manual evaluation following the precedence rules"""
    # Remove spaces
    expr = expr.replace(' ', '')
    
    # Split into tokens (numbers and operators)
    tokens = re.findall(r'\d+|[+\-*]', expr)
    
    if len(tokens) == 1:
        return int(tokens[0])
    
    # Convert to numbers and operators
    numbers = []
    operators = []
    
    for i, token in enumerate(tokens):
        if i % 2 == 0:  # Even indices are numbers
            numbers.append(int(token))
        else:  # Odd indices are operators
            operators.append(token)
    
    # Apply precedence: subtraction first, then multiplication, then addition
    # Handle subtraction first
    i = 0
    while i < len(operators):
        if operators[i] == '-':
            result = numbers[i] - numbers[i + 1]
            numbers = numbers[:i] + [result] + numbers[i + 2:]
            operators = operators[:i] + operators[i + 1:]
        else:
            i += 1
    
    # Handle multiplication
    i = 0
    while i < len(operators):
        if operators[i] == '*':
            result = numbers[i] * numbers[i + 1]
            numbers = numbers[:i] + [result] + numbers[i + 2:]
            operators = operators[:i] + operators[i + 1:]
        else:
            i += 1
    
    # Handle addition
    i = 0
    while i < len(operators):
        if operators[i] == '+':
            result = numbers[i] + numbers[i + 1]
            numbers = numbers[:i] + [result] + numbers[i + 2:]
            operators = operators[:i] + operators[i + 1:]
        else:
            i += 1
    
    return numbers[0]

def solve_challenge():
    """Main function to solve the CTF challenge"""
    host = "ibeforee.baectf.com"
    port = 443
    password = "shamefulrobot"
    
    print(f"Connecting to {host}:{port}")
    
    try:
        sock = connect_to_server(host, port)
        
        # Send password
        send_password(sock, password)
        
        correct_count = 0
        
        while correct_count < 101:
            # Receive the mathematical expression
            data = sock.recv(1024).decode('utf-8').strip()
            print(f"Received: {data}")
            
            # Extract the expression (assuming it's in the received data)
            # Look for patterns like "5i3e2" or similar
            expr_match = re.search(r'(\d+[ice]\d+(?:[ice]\d+)*)', data)
            
            if expr_match:
                expression = expr_match.group(1)
                print(f"Expression to evaluate: {expression}")
                
                # Evaluate the expression
                result = evaluate_expression(expression)
                print(f"Result: {result}")
                
                # Send the result
                sock.send(f"{result}\n".encode('utf-8'))
                
                # Read response
                response = sock.recv(1024).decode('utf-8')
                print(f"Server response: {response}")
                
                if "correct" in response.lower():
                    correct_count += 1
                    print(f"Correct answers: {correct_count}/100")
                elif "wrong" in response.lower():
                    print("Wrong answer, restarting...")
                    correct_count = 0
                elif "flag" in response.lower() or "ctf" in response.lower():
                    print(f"FLAG FOUND: {response}")
                    break
            else:
                print("Could not find expression in response")
                # Try to continue anyway
                time.sleep(0.1)
        
        sock.close()
        
    except Exception as e:
        print(f"Error: {e}")

solve_challenge()