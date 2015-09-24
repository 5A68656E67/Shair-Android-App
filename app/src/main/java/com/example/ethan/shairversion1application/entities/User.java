/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  18641 java smart phone development - final project - Shair
 *
 *  Name: Sen Yue (seny)
 *        Zheng Lei (zlei)
 *
 *  class name: User
 *
 *  class properties:
 *  name:String
 *  imgPath:String
 *  bithday:String
 *  email:String
 *  phone:String
 *  postedItemArrayList:ArrayList<Item>
 *  sharedItemArrayList:ArrayList<Item>
 *  borrowedItemArrayList:ArrayList<Item>
 *  address:String
 *
 *  class methods:
 *  toJson():JsonObject
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package com.example.ethan.shairversion1application.entities;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable{
    private String name;
    private String imgPath;
    private int birthday;
    private String email;
    private String phone;
    private int id;
    private ArrayList<Item> postedItemArrayList;
    private ArrayList<Item> sharedItemArrayList;
    private ArrayList<Item> borrowedItemArrayList;
    private String address;

    public User() {
        this.postedItemArrayList = new ArrayList<>();
        this.sharedItemArrayList = new ArrayList<>();
        this.borrowedItemArrayList = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Item> getPostedItemArrayList() {
        return postedItemArrayList;
    }

    @SuppressWarnings("unused")
    public void setPostedItemArrayList(ArrayList<Item> postedItemArrayList) {
        this.postedItemArrayList = postedItemArrayList;
    }

    public ArrayList<Item> getSharedItemArrayList() {
        return sharedItemArrayList;
    }
    @SuppressWarnings("unused")
    public void setSharedItemArrayList(ArrayList<Item> sharedItemArrayList) {
        this.sharedItemArrayList = sharedItemArrayList;
    }

    public ArrayList<Item> getBorrowedItemArrayList() {
        return borrowedItemArrayList;
    }
    @SuppressWarnings("unused")
    public void setBorrowedItemArrayList(ArrayList<Item> borrowedItemArrayList) {
        this.borrowedItemArrayList = borrowedItemArrayList;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public int getBirthday() {
        return birthday;
    }

    public void setBirthday(int birthday) {
        this.birthday = birthday;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Item getPostedItemById(int id){
        Item item = null;
        for(Item i : postedItemArrayList){
            if(i.getId() == id){
                item = i;
                break;
                }
            }
        return item;
    }

    public void removePostedItem(int id) {
        for(Item i : postedItemArrayList){
            if(i.getId() == id){
                postedItemArrayList.remove(i);
                break;
            }
        }
    }

    public JsonObject toJson(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id",this.id);
        jsonObject.addProperty("name",this.name);
        jsonObject.addProperty("profile_image",this.imgPath);
        jsonObject.addProperty("birthdate",this.birthday);
        jsonObject.addProperty("email",this.email);
        jsonObject.addProperty("phone", this.phone);
        jsonObject.addProperty("address", this.address);

        Gson gson = new Gson();
        JsonElement element = gson.toJsonTree(sharedItemArrayList, new TypeToken<List<Item>>() {}.getType());

        JsonArray jsonArray = element.getAsJsonArray();
        jsonObject.add("shared_items",jsonArray);

        element = gson.toJsonTree(borrowedItemArrayList, new TypeToken<List<Item>>() {}.getType());
        jsonArray = element.getAsJsonArray();
        jsonObject.add("borrowed_items",jsonArray);

        element = gson.toJsonTree(postedItemArrayList, new TypeToken<List<Item>>() {}.getType());
        jsonArray = element.getAsJsonArray();
        jsonObject.add("posted_items",jsonArray);

        return jsonObject;
        // posted_items
    }

}