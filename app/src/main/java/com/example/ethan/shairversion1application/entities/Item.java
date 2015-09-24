/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  18641 java smart phone development - final project - Shair
 *
 *  Name: Sen Yue (seny)
 *        Zheng Lei (zlei)
 *
 *  class name: Item
 *
 *  class properties:
 *  name:String
 *  distance:double
 *  price:double
 *  duration:int
 *  startData:String
 *  discuss:boolean
 *  newDegree:int
 *  imageArrayList:ArrayList<String>
 *  latitute:double
 *  longitute:double
 *  securityDeposit:double
 *  deadLine:String
 *  description:String
 *
 *  class properties:
 *  toJson():JsonObject
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package com.example.ethan.shairversion1application.entities;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.Serializable;
import java.util.ArrayList;

public class Item implements Serializable{
    private int id;
    private String name;
    private double distance;
    private double price;
    private int duration;
    private int startDate;
    private boolean discuss;
    private int newDegree;
    private ArrayList<String> imageArrayList;
    private double latitude;
    private double longitude;
    private double securityDeposit;
    private int deadLine;
    private String description;
    private String sharer;
    private int sharerID;
    private int neederID;

    public Item() {
        imageArrayList = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @SuppressWarnings("unused")
    public int getStartDate() {
        return startDate;
    }
    @SuppressWarnings("unused")
    public void setStartDate(int startDate) {
        this.startDate = startDate;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSharerID() {
        return sharerID;
    }

    public void setSharerID(int sharerID) {
        this.sharerID = sharerID;
    }

    public int getNeederID() {
        return neederID;
    }

    public void setNeederID(int neederID) {
        this.neederID = neederID;
    }



    public double getSecurityDeposit() {
        return securityDeposit;
    }

    public void setSecurityDeposit(double securityDeposit) {
        this.securityDeposit = securityDeposit;
    }

    public ArrayList<String> getImageArrayList() {
        return imageArrayList;
    }

    public void setImageArrayList(ArrayList<String> imageArrayList) {
        this.imageArrayList = imageArrayList;
    }
    @SuppressWarnings("unused")
    public String getSharer() {
        return sharer;
    }
    @SuppressWarnings("unused")
    public void setSharer(String sharer) {
        this.sharer = sharer;
    }

    public int getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(int deadLine) {
        this.deadLine = deadLine;
    }

    public String getDescription() {
        return description;
    }


    public int getStartData() {
        return startDate;
    }

    public void setStartData(int startDate) {
        this.startDate = startDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getNewDegree() {
        return newDegree;
    }

    public void setNewDegree(int newDegree) {
        this.newDegree = newDegree;
    }

    public boolean isDiscuss() {
        return discuss;
    }

    public void setDiscuss(boolean discuss) {
        this.discuss = discuss;
    }


    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", this.id);
        jsonObject.addProperty("name", this.name);
        jsonObject.addProperty("price", this.price);
        jsonObject.addProperty("duration", this.duration);
        jsonObject.addProperty("start_date", this.startDate);
        jsonObject.addProperty("discuss", this.discuss);
        jsonObject.addProperty("new_degree", this.newDegree);
        jsonObject.addProperty("latitude", this.latitude);
        jsonObject.addProperty("longitude", this.longitude);
        jsonObject.addProperty("security_deposit", this.securityDeposit);
        jsonObject.addProperty("deadline", this.deadLine);
        jsonObject.addProperty("description", this.description);
        jsonObject.addProperty("sharer_id", this.sharerID);
        jsonObject.addProperty("needer_id", this.neederID);

        JsonArray jsonArray = new JsonArray();
        for (int i = 0; i < imageArrayList.size(); i++) {
            JsonObject jsonObject1 = new JsonObject();
            jsonObject1.addProperty("path", imageArrayList.get(i));
            jsonArray.add(jsonObject1);

        }

//        Gson gson = new Gson();
//        JsonElement element = gson.toJsonTree(imageArrayList, new TypeToken<List<Item>>() {}.getType());
//
//        JsonArray jsonArray = element.getAsJsonArray();
        jsonObject.add("images", jsonArray);
        return jsonObject;

    }

}
