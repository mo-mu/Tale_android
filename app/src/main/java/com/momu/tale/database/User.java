package com.momu.tale.database;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * 사용자 database for firebase
 * Created by knulps on 2017. 2. 4..
 */
@IgnoreExtraProperties
public class User {

    public String username;
    public String email;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }
}