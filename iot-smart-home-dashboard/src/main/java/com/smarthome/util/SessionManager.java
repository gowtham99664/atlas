package com.smarthome.util;

import com.smarthome.model.Customer;

public class SessionManager {
    
    private static SessionManager instance;
    private Customer currentUser;
    
    private SessionManager() {}
    
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }
    
    public void login(Customer customer) {
        this.currentUser = customer;
    }
    
    public void logout() {
        this.currentUser = null;
    }
    
    public boolean isLoggedIn() {
        return currentUser != null;
    }
    
    public Customer getCurrentUser() {
        return currentUser;
    }
    
    public void updateCurrentUser(Customer customer) {
        this.currentUser = customer;
    }
}