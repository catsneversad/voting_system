package com.example.final_project.service;

import java.util.concurrent.Callable;


public class UsernamePasswordChecker implements Callable<Boolean> {
    private String username;
    private String password;

    public UsernamePasswordChecker(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public Boolean call() throws Exception {
        if (this.password.length() >= 6 && this.password.length() <= 20) {
            return true;
        } else {
            return false;
        }
    }
}
