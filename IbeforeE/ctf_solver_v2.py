#!/usr/bin/env python3
import socket
import re
import time
import sys

class CTFSolver:
    def __init__(self, host, port, password):
        self.host = host
        self.port = port
        self.password = password
        self.sock = None
    
    def connect(self):
        """Connect to the server"""
        try:
            self.sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            self.sock.settimeout(30)  # 30 second timeout
            print(f"Connecting to {self.host}:{self.port}")
            self.sock.connect((self.host, self.port))
            return True
        except Exception as e:
            print(f"Connection failed: {e}")
            return False
    
    def send_data(self, data):
        """Send data to server"""
        try:
            self.sock.send((data + '\n').encode('utf-8'))
            return True
        except Exception as e:
            print(f"Send failed: {e}")
            return False
    
    def receive_data(self, timeout=5):
        """Receive data from server"""
        try:
            self.sock.settimeout(timeout)
            data = self.sock.recv(4096).decode('utf-8', errors='ignore')
            return data
        except socket.timeout:
            print("Receive timeout")
            return None
        except Exception as e:
            print(f"Receive failed: {e}")
            return None
    
    def authenticate(self):
        """Send password to authenticate"""
        print("Waiting for password prompt...")
        
        # Read initial data
        initial_data = self.receive_data(10)
        if initial_data:
            print(f"Server: {initial_data}")
        
        # Send password
        print(f"Sending password: {self.password}")
        if not self.send_data(self.password):
            return False
        
        # Read authentication response
        auth_response = self.receive_data()
        if auth_response:
            print(f"Auth response: {auth_response}")
            return auth_response
        return None
    
    def evaluate_expression(self, expr):
        """
        Evaluate expression with custom operators and precedence rules
        i -> multiplication, c -> subtraction, e -> addition
        Rule: "i before e" but if c is present, do c before i
        """
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
        
        if len(tokens) < 3:
            return tokens[0] if tokens else 0
        
        # Apply precedence: c (subtraction) first, then i (multiplication), then e (addition)
        
        # Step 1: Handle subtraction (c)
        i = 1
        while i < len(tokens):
            if tokens[i] == 'c':
                result = tokens[i-1] - tokens[i+1]
                tokens = tokens[:i-1] + [result] + tokens[i+2:]
            else:
                i += 2
        
        # Step 2: Handle multiplication (i)
        i = 1
        while i < len(tokens):
            if tokens[i] == 'i':
                result = tokens[i-1] * tokens[i+1]
                tokens = tokens[:i-1] + [result] + tokens[i+2:]
            else:
                i += 2
        
        # Step 3: Handle addition (e)
        i = 1
        while i < len(tokens):
            if tokens[i] == 'e':
                result = tokens[i-1] + tokens[i+1]
                tokens = tokens[:i-1] + [result] + tokens[i+2:]
            else:
                i += 2
        
        final_result = tokens[0]
        print(f"Result: {final_result}")
        return final_result
    
    def solve(self):
        """Main solving loop"""
        if not self.connect():
            return False
        
        auth_response = self.authenticate()
        if not auth_response:
            print("Authentication failed")
            return False
        
        correct_count = 0
        
        try:
            # Process the first challenge from auth response
            challenge_data = auth_response
            
            while correct_count < 101:
                print(f"\n--- Attempt {correct_count + 1}/101 ---")
                
                if not challenge_data:
                    # Receive new challenge
                    challenge_data = self.receive_data(10)
                    if not challenge_data:
                        print("No challenge received, retrying...")
                        continue
                
                print(f"Challenge: {challenge_data}")
                
                # Look for mathematical expression
                # Try different patterns
                patterns = [
                    r'(\d+[ice]\d+(?:[ice]\d+)*)',  # Standard pattern
                    r'(\d+(?:[ice]\d+)+)',          # Alternative pattern
                    r'([0-9ice]+)',                 # Broad pattern
                ]
                
                expression = None
                for pattern in patterns:
                    match = re.search(pattern, challenge_data)
                    if match:
                        expression = match.group(1)
                        break
                
                if not expression:
                    print("Could not find expression, trying to continue...")
                    time.sleep(0.5)
                    continue
                
                # Evaluate and send result
                try:
                    result = self.evaluate_expression(expression)
                    
                    print(f"Sending answer: {result}")
                    if not self.send_data(str(result)):
                        print("Failed to send answer")
                        break
                    
                    # Get response
                    response = self.receive_data()
                    if response:
                        print(f"Response: {response}")
                        
                        # Check for success indicators
                        if any(word in response.lower() for word in ['correct', 'right', 'good']):
                            correct_count += 1
                            print(f"✓ Correct! Progress: {correct_count}/101")
                            challenge_data = response  # Next challenge might be in response
                        elif any(word in response.lower() for word in ['wrong', 'incorrect', 'bad']):
                            print("✗ Wrong answer")
                            challenge_data = response  # Next challenge might be in response
                        elif any(word in response.lower() for word in ['flag', 'ctf{', 'bae{']):
                            print(f"🎉 FLAG FOUND: {response}")
                            return True
                        elif 'slow' in response.lower():
                            print("Too slow! Restarting...")
                            correct_count = 0
                            challenge_data = response  # Next challenge might be in response
                        else:
                            challenge_data = None  # Need to receive new data
                    else:
                        challenge_data = None  # Need to receive new data
                    
                except Exception as e:
                    print(f"Error processing expression: {e}")
                    continue
                
                # Small delay to avoid being too fast
                time.sleep(0.05)
        
        except KeyboardInterrupt:
            print("\nInterrupted by user")
        except Exception as e:
            print(f"Unexpected error: {e}")
        finally:
            if self.sock:
                self.sock.close()
        
        return False

def main():
    solver = CTFSolver("ibeforee.baectf.com", 443, "shamefulrobot")
    success = solver.solve()
    
    if success:
        print("Challenge completed successfully!")
    else:
        print("Challenge failed or incomplete")

main()