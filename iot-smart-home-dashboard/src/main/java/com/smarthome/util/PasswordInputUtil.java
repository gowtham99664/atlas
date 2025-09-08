package com.smarthome.util;

import java.io.Console;
import java.io.IOException;
import java.util.Scanner;

public class PasswordInputUtil {
    
    public static String readMaskedPassword(String prompt) {
        Console console = System.console();
        
        if (console != null) {
            char[] password = console.readPassword(prompt);
            return password != null ? new String(password) : "";
        } else {
            return readPasswordFallback(prompt);
        }
    }
    
    private static String readPasswordFallback(String prompt) {
        System.out.print(prompt);
        StringBuilder password = new StringBuilder();
        
        try {
            while (true) {
                int ch = System.in.read();
                
                if (ch == '\n' || ch == '\r') {
                    System.out.println();
                    break;
                } else if (ch == 8 || ch == 127) {
                    if (password.length() > 0) {
                        password.deleteCharAt(password.length() - 1);
                        System.out.print("\b \b");
                    }
                } else if (ch >= 32 && ch <= 126) {
                    password.append((char) ch);
                    System.out.print("*");
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading password: " + e.getMessage());
        }
        
        return password.toString();
    }
    
    public static String readMaskedPasswordWithScanner(Scanner scanner, String prompt) {
        Console console = System.console();
        
        if (console != null) {
            char[] password = console.readPassword(prompt);
            return password != null ? new String(password) : "";
        } else {
            // Simplified approach for CLI compatibility - just use Scanner
            System.out.print(prompt);
            String password = "";
            try {
                if (scanner.hasNextLine()) {
                    password = scanner.nextLine();
                }
            } catch (Exception e) {
                System.err.println("Error reading password input.");
            }
            
            return password != null ? password : "";
        }
    }
}