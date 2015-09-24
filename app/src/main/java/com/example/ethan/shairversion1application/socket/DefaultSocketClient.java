/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  18641 java smart phone development - final project - Shair
 *
 *  Name: Sen Yue (seny)
 *        Zheng Lei (zlei)
 *
 *  class name: DefaultSocketClient
 *
 *  class properties:
 *  PORT: int
 *  HOST: String
 *  queryType: QueryType
 *  jsonObject: JsonObject
 *  socket: Socket
 *  input: BufferedReader
 *  ois: ObjectInputStream
 *  oos: ObjectOutputStream
 *  bos: BufferedOutputStream
 *  handler: Handler
 *  success: boolean
 *
 *  class methods:
 *  openConnection(): boolean
 *  handle(): void
 *  close(): void
 *  setSuccess(boolean success): void
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package com.example.ethan.shairversion1application.socket;


import android.os.Handler;
import android.os.Message;
import com.google.gson.JsonObject;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class DefaultSocketClient extends Thread {

    private final static int PORT = 80;
    // public ip of aws instance
    private final static String HOST = "52.5.68.104";

    private QueryType queryType;
    private JsonObject jsonObject;

    private Socket socket;
    private BufferedReader input;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private BufferedOutputStream bos;
    private Handler handler;

    private boolean success = false;


    public DefaultSocketClient(QueryType queryType, JsonObject jsonObject, Handler handler){
        this.queryType = queryType;
        this.jsonObject = jsonObject;
        this.handler = handler;
    }

    public DefaultSocketClient(QueryType queryType, JsonObject jsonObject){
        this.queryType = queryType;
        this.jsonObject = jsonObject;
    }
    // run() - start a thread
    public void run(){
        // first open the connection to the server
        if(openConnection()){
            try {
                // handle different requests
                handle();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("The client quit abnormally.");
            }
            close();
        }
    }
    // open the connection to the server
    public boolean openConnection(){
        try {
            socket = new Socket(HOST, PORT);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
            bos = new BufferedOutputStream(socket.getOutputStream());
            input = new BufferedReader(new InputStreamReader(System.in));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    // using loop to handle different requests from the users
    public void handle() throws Exception{
        switch (queryType){
            case REGISTER_ACCOUNT:
                oos.writeObject(1);
                oos.writeObject(this.jsonObject.toString());
                //noinspection StatementWithEmptyBody
                while(!success){
                    // wait
                }
                break;
            case LOGIN_ACCOUNT:
                oos.writeObject(2);
                oos.writeObject(this.jsonObject.toString());
                //noinspection StatementWithEmptyBody
                while(!success){
                    //wait
                }
                break;
            case UPLOAD_ITEM:
                oos.writeObject(3);
                oos.writeObject(this.jsonObject.toString());
                int i = (Integer) ois.readObject();
                if(i == 0){
                    Message msg = handler.obtainMessage();
                    msg.arg1 = 1;
                    handler.sendMessage(msg);
                }else{
                    Message msg = handler.obtainMessage();
                    msg.arg1 = 2;
                    handler.sendMessage(msg);
                }
                break;
            case REQUEST_NEARBY:
                oos.writeObject(4);
                oos.writeObject(this.jsonObject.toString());
                //noinspection StatementWithEmptyBody
                while(!success){
                    //wait
                }
                break;
            case EDIT_PROFILE:
                oos.writeObject(5);
                oos.writeObject(this.jsonObject.toString());
                break;
            case REQUEST_USER_ON_ITEM:
                oos.writeObject(6);
                oos.writeObject(this.jsonObject.toString());
                //noinspection StatementWithEmptyBody
                while(!success){
                    //wait
                }
                break;
            case UPDATE_LIKE:
                oos.writeObject(7);
                oos.writeObject(this.jsonObject.toString());
                break;
            case UPDATE_SHARE:
                oos.writeObject(8);
                oos.writeObject(this.jsonObject.toString());
                break;
            case REQUEST_SEARCH:
                oos.writeObject(9);
                oos.writeObject(this.jsonObject.toString());
                //noinspection StatementWithEmptyBody
                while(!success){
                    //wait
                }
                break;
            case UPDATE_ITEM:
                oos.writeObject(10);
                oos.writeObject(this.jsonObject.toString());
                break;
            case DELETE_ITEM:
                oos.writeObject(11);
                oos.writeObject(this.jsonObject.toString());
                break;
            case TRANSACTION_NOTIFICATIONS:
                oos.writeObject(12);
                oos.writeObject(this.jsonObject.toString());
                //noinspection StatementWithEmptyBody
                while(!success){
                    //wait
                }
                break;
            case LIKE_NOTIFICATIONS:
                oos.writeObject(13);
                oos.writeObject(this.jsonObject.toString());
                //noinspection StatementWithEmptyBody
                while(!success){
                    //wait
                }
                break;
            case REQUEST_NEEDER_ON_ITEM:
                oos.writeObject(14);
                oos.writeObject(this.jsonObject.toString());
                //noinspection StatementWithEmptyBody
                while(!success){
                // wait
                }
                break;
            case FIND_PASSWORD:
                oos.writeObject(15);
                oos.writeObject(this.jsonObject.toString());
                break;
        }

    }
    // close the Socket, InputStream, OutputStream, etc.
    public void close(){
        try{
            socket.close();
            input.close();
            ois.close();
            oos.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public ObjectInputStream getInputStream(){
        //noinspection StatementWithEmptyBody
        while(this.ois == null){
//            Log.e("ObjectInputStream","Fail to fetch Object Input Stream");
        }
        return this.ois;
    }

    public void setSuccess(boolean success){
        this.success = success;
    }
}
