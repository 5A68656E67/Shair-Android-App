/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  18641 java smart phone development - final project - Shair
 *
 *  Name: Sen Yue (seny)
 *        Zheng Lei (zlei)
 *
 *  class name: Notification
 *
 *  class properties:
 *  itemImg: String
 *  neederImg: String
 *  itemName: String
 *  neederName: String
 *  timeStamp: String
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package com.example.ethan.shairversion1application.entities;

public class Notification {
   private String itemImg;
    private String neederImg;
    private String itemName;
    private String neederName;
    private String timeStamp;

    public String getTimeStamp() {
        return timeStamp;
    }

    @SuppressWarnings("unused")
    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getItemImg() {
        return itemImg;
    }

    public void setItemImg(String itemImg) {
        this.itemImg = itemImg;
    }



    public String getNeederImg() {
        return neederImg;
    }

    public void setNeederImg(String neederImg) {
        this.neederImg = neederImg;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getNeederName() {
        return neederName;
    }

    public void setNeederName(String neederName) {
        this.neederName = neederName;
    }
}
