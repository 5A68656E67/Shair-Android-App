/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  18641 java smart phone development - final project - Shair
 *
 *  Name: Sen Yue (seny)
 *        Zheng Lei (zlei)
 *
 *  class name: LikeService
 *
 *  class properties:
 *  myThread: MyThread
 *  manager: NotificationManager
 *  notification: Notification
 *  pi: PendingIntent
 *  jsonStr: String
 *  buildAccount: BuildAccount
 *
 *  class methods:
 *  notification(String content, String number, String date):void
 *
 *
 *  inner class name:
 *  MyThread
 *
 *  inner class methods:
 *  run(): void
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */

package com.example.ethan.shairversion1application.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import com.example.ethan.shairversion1application.R;
import com.example.ethan.shairversion1application.cruditem.BuildAccount;
import com.example.ethan.shairversion1application.notification.LikeActivity;
import com.example.ethan.shairversion1application.socket.DefaultSocketClient;
import com.example.ethan.shairversion1application.socket.QueryType;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.ObjectInputStream;


public class LikeService extends Service{
    @SuppressWarnings("FieldCanBeLocal")
    private MyThread myThread;
    @SuppressWarnings("FieldCanBeLocal")
    private NotificationManager manager;
    @SuppressWarnings("FieldCanBeLocal")
    private Notification notification;
    @SuppressWarnings("FieldCanBeLocal")
    private PendingIntent pi;
    private String jsonStr;
    private BuildAccount buildAccount;
    private static int i = 0;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        buildAccount = new BuildAccount();
        this.myThread = new MyThread();
        this.myThread.start();
      //  Toast.makeText(this, "like Service started", Toast.LENGTH_LONG).show();
    }

    private void notification(String content, String number, String date) {
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //noinspection deprecation
        notification = new Notification(R.drawable.abc_btn_rating_star_on_mtrl_alpha, content,
                System.currentTimeMillis());
        notification.defaults = Notification.DEFAULT_ALL;
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notification.flags |= Notification.FLAG_NO_CLEAR;
        Intent intent = new Intent(getApplicationContext(),
                LikeActivity.class);
        intent.putExtra("content", content);
        intent.putExtra("number", number);
        intent.putExtra("date", date);
        pi = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        //noinspection deprecation
        notification.setLatestEventInfo(getApplicationContext(), "Shair Notification", content, pi);
        manager.notify(0, notification);
    }



    private class MyThread extends Thread  {
        @Override
        public void run() {
            super.run();
            //noinspection InfiniteLoopStatement
            while (true) {
                System.out.println("send request..................");
                JsonObject jsonObject = new JsonObject();

                //noinspection AccessStaticViaInstance
                jsonObject.addProperty("user_id",buildAccount.getAccount().getUser().getId());
                DefaultSocketClient client = new DefaultSocketClient(QueryType.LIKE_NOTIFICATIONS,jsonObject);
                client.start();
                ObjectInputStream ois = client.getInputStream();
                //noinspection TryWithIdenticalCatches
                try {
                        jsonStr = (String) ois.readObject();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                client.setSuccess(true);
                JsonParser parser = new JsonParser();
                if (i == 0) {
                    i = 1;
                    JsonArray jsonArray = parser.parse(jsonStr).getAsJsonArray();
                    int len = jsonArray.size();
                    for (int i = 0; i < len; i++) {
                        JsonObject jsonObject1 = jsonArray.get(i).getAsJsonObject();
                        com.example.ethan.shairversion1application.entities.Notification notification1 = new com.example.ethan.shairversion1application.entities.Notification();
                        notification1.setItemName(jsonObject1.get("item_name").getAsString());
                        notification1.setNeederImg(jsonObject1.get("needer_img").isJsonNull() ? "https://s3.amazonaws.com/startupshair/itemimg/profile_image_default.png" : jsonObject1.get("needer_img").getAsString());
                        notification1.setNeederName(jsonObject1.get("needer_name").getAsString());
                        notification1.setItemImg(jsonObject1.get("item_img").isJsonNull() ? "https://s3.amazonaws.com/startupshair/itemimg/no-image-thumb.png" : jsonObject1.get("item_img").getAsString());
                        LikeActivity.likes.add(notification1);
                    }
                } else {
                    JsonArray jsonArray = parser.parse(jsonStr).getAsJsonArray();
                    int len = jsonArray.size();
                    if (len != LikeActivity.likes.size() && len != 0) {
                        for (int i = LikeActivity.likes.size(); i < len; i++) {
                            JsonObject jsonObject1 = jsonArray.get(i).getAsJsonObject();
                            com.example.ethan.shairversion1application.entities.Notification notification1 = new com.example.ethan.shairversion1application.entities.Notification();
                            notification1.setItemName(jsonObject1.get("item_name").getAsString());
                            notification1.setNeederImg(jsonObject1.get("needer_img").isJsonNull() ? "https://s3.amazonaws.com/startupshair/itemimg/profile_image_default.png" : jsonObject1.get("needer_img").getAsString());
                            notification1.setNeederName(jsonObject1.get("needer_name").getAsString());
                            notification1.setItemImg(jsonObject1.get("item_img").isJsonNull() ? "https://s3.amazonaws.com/startupshair/itemimg/no-image-thumb.png" : jsonObject1.get("item_img").getAsString());
                            notification(notification1.getNeederName() + " likes your item " + notification1.getItemName(), "1", "07/30/2015");
                            LikeActivity.likes.add(notification1);
                        }
                    }

                }
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }



            }
        }
    }

}
