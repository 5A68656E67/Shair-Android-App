/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  18641 java smart phone development - final project - Shair
 *
 *  Name: Sen Yue (seny)
 *        Zheng Lei (zlei)
 *
 *  class name: ProxyAccount
 *
 *  class properties:
 *  account: Account
 *  exploreArraylist: ArrayList<Item>
 *  searchArraylist: ArrayList<Item>
 *
 *
 *  class methods:
 *  build(JsonObject jsonObject): void
 *  printAccount(Account account): void
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package com.example.ethan.shairversion1application.cruditem;


import com.example.ethan.shairversion1application.entities.Account;
import com.example.ethan.shairversion1application.entities.Item;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;



@SuppressWarnings("ALL")
public abstract class ProxyAccount {
    private static Account account;
    private static ArrayList<Item> exploreArraylist = new ArrayList<>();
    private static ArrayList<Item> searchArraylist = new ArrayList<>();

    public ProxyAccount() {
    }


    public static ArrayList<Item> getSearchArraylist() {
        return searchArraylist;
    }

    public static void setSearchArraylist(ArrayList<Item> searchArraylist) {
        ProxyAccount.searchArraylist = searchArraylist;
    }

    public static ArrayList<Item> getExploreArraylist() {
        return exploreArraylist;
    }

    public static void setExploreArraylist(ArrayList<Item> exploreArraylist) {
        ProxyAccount.exploreArraylist = exploreArraylist;
    }

    public static Account getAccount() {
        return account;
    }

    public static void setAccount(Account account) {
        ProxyAccount.account = account;
    }

    public void CreateAccount(String name) {}

    public void UpdateAccount(String name, double price) {

    }
    public Item ReadAccount(String name) {
        return null;
    }

    public void DeleteAccount(String name) {}

    public void build(JsonObject jsonObject){
        Account account = new Account();
        account.setAccountName(jsonObject.get("account").getAsString());
        account.setAccountPassword(jsonObject.get("password").getAsString());
        JsonObject userJson = jsonObject.get("user").getAsJsonObject();
        account.getUser().setId(userJson.get("id").getAsInt());
        account.getUser().setName(userJson.get("name").getAsString());
        account.getUser().setBirthday(userJson.get("birthdate").isJsonNull() ? 0 : userJson.get("birthdate").getAsInt());
        account.getUser().setEmail(userJson.get("email").isJsonNull() ? null : userJson.get("email").getAsString());
        account.getUser().setPhone(userJson.get("phone").isJsonNull() ? null : userJson.get("phone").getAsString());
        account.getUser().setAddress(userJson.get("address").isJsonNull() ? null : userJson.get("address").getAsString());
        account.getUser().setImgPath(userJson.get("profile_image").isJsonNull() ? null : userJson.get("profile_image").getAsString());
        ArrayList<Item> postedItemArrayList = account.getUser().getPostedItemArrayList();
        ArrayList<Item> sharedItemArrayList = account.getUser().getSharedItemArrayList();
        ArrayList<Item> borrowedItemArrayList = account.getUser().getBorrowedItemArrayList();

        if(!userJson.get("posted_items").isJsonNull()) {
            JsonArray postedItemArray = userJson.get("posted_items").getAsJsonArray();
            for (int i = 0; i < postedItemArray.size(); i++) {
                JsonObject itemJson = postedItemArray.get(i).getAsJsonObject();
                Item item = new Item();
                item.setId(itemJson.get("id").getAsInt());
                item.setName(itemJson.get("name").getAsString());
                item.setDescription(itemJson.get("description").isJsonNull() ? null : itemJson.get("description").getAsString());
                item.setNewDegree(itemJson.get("new_degree").getAsInt());
                item.setPrice(itemJson.get("price").getAsDouble());
                item.setDuration(itemJson.get("duration").getAsInt());
                item.setDiscuss(itemJson.get("discuss").getAsBoolean());
                item.setSecurityDeposit(itemJson.get("security_deposit").getAsDouble());
                item.setStartData(itemJson.get("start_date").getAsInt());
                item.setDeadLine(itemJson.get("deadline").getAsInt());
                item.setLongitude(itemJson.get("longitude").getAsDouble());
                item.setLatitude(itemJson.get("latitude").getAsDouble());
                item.setSharerID(itemJson.get("sharer_id").getAsInt());
                item.setNeederID(itemJson.get("needer_id").getAsInt());

                ArrayList<String> images = item.getImageArrayList();
                JsonArray imagesJsonArray = itemJson.get("images").getAsJsonArray();
                for (int j = 0; j < imagesJsonArray.size(); j++) {
                    images.add(imagesJsonArray.get(j).getAsJsonObject().get("path").getAsString());
                }
                postedItemArrayList.add(item);
            }
        }

        if(!userJson.get("shared_items").isJsonNull()) {
            JsonArray sharedItemArray = userJson.get("shared_items").getAsJsonArray();
            for (int i = 0; i < sharedItemArray.size(); i++) {
                JsonObject itemJson = sharedItemArray.get(i).getAsJsonObject();
                Item item = new Item();
                item.setId(itemJson.get("id").getAsInt());
                item.setName(itemJson.get("name").getAsString());
                item.setDescription(itemJson.get("description").isJsonNull() ? null : itemJson.get("description").getAsString());
                item.setNewDegree(itemJson.get("new_degree").getAsInt());
                item.setPrice(itemJson.get("price").getAsDouble());
                item.setDuration(itemJson.get("duration").getAsInt());
                item.setDiscuss(itemJson.get("discuss").getAsBoolean());
                item.setSecurityDeposit(itemJson.get("security_deposit").getAsDouble());
                item.setStartData(itemJson.get("start_date").getAsInt());
                item.setDeadLine(itemJson.get("deadline").getAsInt());
                item.setLongitude(itemJson.get("longitude").getAsDouble());
                item.setLatitude(itemJson.get("latitude").getAsDouble());
                item.setSharerID(itemJson.get("sharer_id").getAsInt());
                item.setNeederID(itemJson.get("needer_id").getAsInt());

                ArrayList<String> images = item.getImageArrayList();
                JsonArray imagesJsonArray = itemJson.get("images").getAsJsonArray();
                for (int j = 0; j < imagesJsonArray.size(); j++) {
                    images.add(imagesJsonArray.get(j).getAsJsonObject().get("path").getAsString());
                }
                sharedItemArrayList.add(item);
            }
        }

        if(!userJson.get("borrowed_items").isJsonNull()) {
            JsonArray borrowedItemArray = userJson.get("borrowed_items").getAsJsonArray();
            for (int i = 0; i < borrowedItemArray.size(); i++) {
                JsonObject itemJson = borrowedItemArray.get(i).getAsJsonObject();
                Item item = new Item();
                item.setId(itemJson.get("id").getAsInt());
                item.setName(itemJson.get("name").getAsString());
                item.setDescription(itemJson.get("description").isJsonNull() ? null : itemJson.get("description").getAsString());
                item.setNewDegree(itemJson.get("new_degree").getAsInt());
                item.setPrice(itemJson.get("price").getAsDouble());
                item.setDuration(itemJson.get("duration").getAsInt());
                item.setDiscuss(itemJson.get("discuss").getAsBoolean());
                item.setSecurityDeposit(itemJson.get("security_deposit").getAsDouble());
                item.setStartData(itemJson.get("start_date").getAsInt());
                item.setDeadLine(itemJson.get("deadline").getAsInt());
                item.setLongitude(itemJson.get("longitude").getAsDouble());
                item.setLatitude(itemJson.get("latitude").getAsDouble());
                item.setSharerID(itemJson.get("sharer_id").getAsInt());
                item.setNeederID(itemJson.get("needer_id").getAsInt());

                ArrayList<String> images = item.getImageArrayList();
                JsonArray imagesJsonArray = itemJson.get("images").getAsJsonArray();
                for (int j = 0; j < imagesJsonArray.size(); j++) {
                    images.add(imagesJsonArray.get(j).getAsJsonObject().get("path").getAsString());
                }
                borrowedItemArrayList.add(item);
            }
        }
        this.account = account;
        printAccount(account);

    }

    public static void printAccount(Account account) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("account name: " + account.getAccountName() + "\n");
        stringBuffer.append("account password: " + account.getAccountPassword() + "\n");
        stringBuffer.append("user id: " + account.getUser().getId() + "\n");
        stringBuffer.append("user name: " + account.getUser().getName() + "\n");
        stringBuffer.append("user image path: " + account.getUser().getImgPath() + "\n");
        stringBuffer.append("user birthday: " + account.getUser().getBirthday() + "\n");
        stringBuffer.append("user email: " + account.getUser().getEmail() + "\n");
        stringBuffer.append("user phone: " + account.getUser().getAddress() + "\n");
        int len1 = account.getUser().getPostedItemArrayList().size();
        int len2 = account.getUser().getSharedItemArrayList().size();
        int len3 = account.getUser().getBorrowedItemArrayList().size();
        stringBuffer.append("\nposted item:\n");
        for (int i = 0; i < len1; i++) {
            stringBuffer.append("item " + i + "\n");
            stringBuffer.append("item name: " + account.getUser().getPostedItemArrayList().get(i).getName() + "\n");
            stringBuffer.append("item price: " + account.getUser().getPostedItemArrayList().get(i).getPrice() + "\n");
            stringBuffer.append("item duration: " + account.getUser().getPostedItemArrayList().get(i).getDuration() + "\n");
            stringBuffer.append("item startdate: " + account.getUser().getPostedItemArrayList().get(i).getStartData() + "\n");
            stringBuffer.append("item discuss: " + account.getUser().getPostedItemArrayList().get(i).isDiscuss() + "\n");
            stringBuffer.append("item new degree: " + account.getUser().getPostedItemArrayList().get(i).getNewDegree() + "\n");
            stringBuffer.append("item latitute: " + account.getUser().getPostedItemArrayList().get(i).getLatitude() + "\n");
            stringBuffer.append("item longitute: " + account.getUser().getPostedItemArrayList().get(i).getLongitude() + "\n");
            stringBuffer.append("item securityDeposit: " + account.getUser().getPostedItemArrayList().get(i).getSecurityDeposit() + "\n");
            stringBuffer.append("item deadline: " + account.getUser().getPostedItemArrayList().get(i).getDeadLine() + "\n");
            stringBuffer.append("item description: " + account.getUser().getPostedItemArrayList().get(i).getDescription() + "\n");
            int length = account.getUser().getPostedItemArrayList().get(i).getImageArrayList().size();
            for (int j = 0; j < length; j++) {
                stringBuffer.append("image " + j + "\n");
                stringBuffer.append(account.getUser().getPostedItemArrayList().get(i).getImageArrayList().get(j) + "\n");
            }
        }
        stringBuffer.append("\nshared item:\n");
        for (int i = 0; i < len2; i++) {
            stringBuffer.append("item " + i + "\n");
            stringBuffer.append("item name: " + account.getUser().getSharedItemArrayList().get(i).getName() + "\n");
            stringBuffer.append("item price: " + account.getUser().getSharedItemArrayList().get(i).getPrice() + "\n");
            stringBuffer.append("item duration: " + account.getUser().getSharedItemArrayList().get(i).getDuration() + "\n");
            stringBuffer.append("item startdate: " + account.getUser().getSharedItemArrayList().get(i).getStartData() + "\n");
            stringBuffer.append("item discuss: " + account.getUser().getSharedItemArrayList().get(i).isDiscuss() + "\n");
            stringBuffer.append("item new degree: " + account.getUser().getSharedItemArrayList().get(i).getNewDegree() + "\n");
            stringBuffer.append("item latitute: " + account.getUser().getSharedItemArrayList().get(i).getLatitude() + "\n");
            stringBuffer.append("item longitute: " + account.getUser().getSharedItemArrayList().get(i).getLongitude() + "\n");
            stringBuffer.append("item securityDeposit: " + account.getUser().getSharedItemArrayList().get(i).getSecurityDeposit() + "\n");
            stringBuffer.append("item deadline: " + account.getUser().getSharedItemArrayList().get(i).getDeadLine() + "\n");
            stringBuffer.append("item description: " + account.getUser().getSharedItemArrayList().get(i).getDescription() + "\n");
            int length = account.getUser().getPostedItemArrayList().get(i).getImageArrayList().size();
            for (int j = 0; j < length; j++) {
                stringBuffer.append("image " + j + "\n");
                stringBuffer.append(account.getUser().getSharedItemArrayList().get(i).getImageArrayList().get(j) + "\n");
            }
        }
        stringBuffer.append("\nborrowed item:\n");
        for (int i = 0; i < len3; i++) {
            stringBuffer.append("item " + i + "\n");
            stringBuffer.append("item name: " + account.getUser().getBorrowedItemArrayList().get(i).getName() + "\n");
            stringBuffer.append("item price: " + account.getUser().getBorrowedItemArrayList().get(i).getPrice() + "\n");
            stringBuffer.append("item duration: " + account.getUser().getBorrowedItemArrayList().get(i).getDuration() + "\n");
            stringBuffer.append("item startdate: " + account.getUser().getBorrowedItemArrayList().get(i).getStartData() + "\n");
            stringBuffer.append("item discuss: " + account.getUser().getBorrowedItemArrayList().get(i).isDiscuss() + "\n");
            stringBuffer.append("item new degree: " + account.getUser().getBorrowedItemArrayList().get(i).getNewDegree() + "\n");
            stringBuffer.append("item latitute: " + account.getUser().getBorrowedItemArrayList().get(i).getLatitude() + "\n");
            stringBuffer.append("item longitute: " + account.getUser().getBorrowedItemArrayList().get(i).getLongitude() + "\n");
            stringBuffer.append("item securityDeposit: " + account.getUser().getBorrowedItemArrayList().get(i).getSecurityDeposit() + "\n");
            stringBuffer.append("item deadline: " + account.getUser().getBorrowedItemArrayList().get(i).getDeadLine() + "\n");
            stringBuffer.append("item description: " + account.getUser().getBorrowedItemArrayList().get(i).getDescription() + "\n");
            int length = account.getUser().getPostedItemArrayList().get(i).getImageArrayList().size();
            for (int j = 0; j < length; j++) {
                stringBuffer.append("image " + j + "\n");
                stringBuffer.append(account.getUser().getBorrowedItemArrayList().get(i).getImageArrayList().get(j) + "\n");
            }
        }

        System.out.println(stringBuffer.toString());
    }



}