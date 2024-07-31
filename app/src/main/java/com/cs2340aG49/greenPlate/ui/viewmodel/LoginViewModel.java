package com.cs2340aG49.greenPlate.ui.viewmodel;

import com.cs2340aG49.greenPlate.ui.model.Account;
import com.cs2340aG49.greenPlate.ui.model.Database;

public class LoginViewModel {

    private final Database database;

    public LoginViewModel() {
        database = Database.getInstance();
    }

    public boolean login(String username, String password) {
        if (!validate(username, password)) {
            return false;
        }
        if (!database.accountExists(username)) {
            // account needs to be created first
            return false;
        }
        return database.checkUsernameAndPassword(username, password);
    }

    public boolean createAccount(String username, String password) {
        if (!validate(username, password)) {
            return false;
        }
        if (database.accountExists(username)) {
            // account alr exists can't create
            return false;
        }
        database.addAccount(new Account(username, password));
        return true;
    }

    private boolean validate(String username, String password) {
        if (username.trim().isEmpty()) {
            return false;
        } else if (username.length() < 5) {
            return false;
        } else if (username.contains(" ") || username.contains("\n")) {
            return false;
        }
        // Validate password
        if (password.trim().isEmpty()) {
            return false;
        } else if (password.length() < 5) {
            return false;
        } else {
            return !password.contains(" ") && !username.contains("\n");
        }
    }
}
