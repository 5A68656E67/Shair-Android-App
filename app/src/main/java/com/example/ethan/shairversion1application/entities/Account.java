/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  18641 java smart phone development - final project - Shair
 *
 *  Name: Sen Yue (seny)
 *        Zheng Lei (zlei)
 *
 *  class name: Account
 *
 *  class properties:
 *  accountName:String
 *  accountPassword:String
 *  user:User
 *
 *  class methods:
 *  toJson():JsonObject
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package com.example.ethan.shairversion1application.entities;

import com.google.gson.JsonObject;

public class Account {
    private String accountName;
    private String accountPassword;
    private User user;

    public Account() {
        this.user = new User();
    }


    public Account(String accountName, String accountPassword, User user) {
        setAccountName(accountName);
        setAccountPassword(accountPassword);
        setUser(user);
    }



    public String getAccountPassword() {
        return accountPassword;
    }

    public void setAccountPassword(String accountPassword) {
        this.accountPassword = accountPassword;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public JsonObject toJson(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("account",accountName);
        jsonObject.addProperty("password",accountPassword);
        jsonObject.add("user",user.toJson());
        return jsonObject;
    }
}
