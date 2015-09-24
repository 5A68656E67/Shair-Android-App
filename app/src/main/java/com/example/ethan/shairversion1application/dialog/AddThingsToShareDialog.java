/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  18641 java smart phone development - final project - Shair
 *
 *  Name: Sen Yue (seny)
 *        Zheng Lei (zlei)
 *
 *  class name: AddThingsToShareDialog
 *
 *  class properties:
 *  stuffName:EditText
 *  stuffDescription:EditText
 *  stuffPrice:EditText
 *  stuffDuration:EditText
 *  stuffSecurityDeposit:EditText
 *  stuffDeadline:DatePicker
 *  seekBar:SeekBar
 *  customExceptionHandler:CustomExceptionHandler
 *  post:TextView
 *
 *
 *  class methods:
 *  onCreateDialog(Bundle savedInstanceState):Dialog
 *  initial(View view):void
 *  addPostListener():void
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package com.example.ethan.shairversion1application.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.ethan.shairversion1application.R;
import com.example.ethan.shairversion1application.cruditem.BuildAccount;
import com.example.ethan.shairversion1application.entities.Item;
import com.example.ethan.shairversion1application.exception.CustomExceptionHandler;
import com.example.ethan.shairversion1application.socket.DefaultSocketClient;
import com.example.ethan.shairversion1application.socket.QueryType;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class AddThingsToShareDialog extends DialogFragment{
    private EditText stuffName;
    private EditText stuffDescription;
    private EditText stuffPrice;
    private EditText stuffDuration;
    private EditText stuffSecurityDeposit;
    private ImageView plusimageView;
    private ArrayList<String> localImages;
    private ArrayList<ImageView> imageViewArrayList;
    private Switch aSwitch;
    private Item item;
    private DatePicker stuffDeadline;
    private SeekBar seekBar;
    private CustomExceptionHandler customExceptionHandler;
    private TextView post;
    private BuildAccount buildAccount;
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_EXISTING_PHOTO = 2;
    private String mCurrentPhotoPath;
    private static int count = 0;
    @SuppressWarnings("FieldCanBeLocal")
    private String timeStamp;
    private ArrayList<String> timeArraylist;
    private Context context;
    private static Handler handler;


    public AddThingsToShareDialog(){
        customExceptionHandler = new CustomExceptionHandler();
        localImages = new ArrayList<>();
        timeArraylist = new ArrayList<>();
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.dialog_addthingstosharedialog, null);
        buildAccount = new BuildAccount();
        builder.setView(dialogView);
        initial(dialogView);

        TextView cancel = (TextView) dialogView.findViewById(R.id.cancel_window);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                count = 0;

            }
        });

        final TextView percentage = (TextView) dialogView.findViewById(R.id.percentage);
        seekBar.setMax(100);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                percentage.setText(progress + "%");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        addPostListener();
        addPictureListener();
        return builder.create();

    }

    @Override
    public void onResume()
    {
        super.onResume();
        getDialog().setCanceledOnTouchOutside(false);
        Window window = getDialog().getWindow();
        window.setLayout(750, 700);
        window.setGravity(Gravity.CENTER);
        //TODO:
    }

    public void initial(View view) {
        item = new Item();
        timeArraylist = new ArrayList<>();
        stuffName = (EditText) view.findViewById(R.id.stuff_name);
        stuffDescription = (EditText) view.findViewById(R.id.stuff_description);
        stuffPrice = (EditText) view.findViewById(R.id.stuff_price);
        stuffDuration = (EditText) view.findViewById(R.id.stuff_duration);
        stuffSecurityDeposit = (EditText) view.findViewById(R.id.stuff_security_deposit);
        stuffDeadline = (DatePicker) view.findViewById(R.id.stuff_datapicker);
        seekBar = (SeekBar) view.findViewById(R.id.seek_bar);
        post = (TextView) view.findViewById(R.id.post);
        plusimageView = (ImageView) view.findViewById(R.id.item_add_picture);
        ImageView gallery1 = (ImageView) view.findViewById(R.id.item_picture_galarry_1);
        ImageView gallery2 = (ImageView) view.findViewById(R.id.item_picture_galarry_2);
        ImageView gallery3 = (ImageView) view.findViewById(R.id.item_picture_galarry_3);
        ImageView gallery4 = (ImageView) view.findViewById(R.id.item_picture_galarry_4);
        ImageView gallery5 = (ImageView) view.findViewById(R.id.item_picture_galarry_5);
        ImageView gallery6 = (ImageView) view.findViewById(R.id.item_picture_galarry_6);
        ImageView gallery7 = (ImageView) view.findViewById(R.id.item_picture_galarry_7);
        ImageView gallery8 = (ImageView) view.findViewById(R.id.item_picture_galarry_8);
        ImageView gallery9 = (ImageView) view.findViewById(R.id.item_picture_galarry_9);
        imageViewArrayList = new ArrayList<>();
        imageViewArrayList.add(gallery1);
        imageViewArrayList.add(gallery2);
        imageViewArrayList.add(gallery3);
        imageViewArrayList.add(gallery4);
        imageViewArrayList.add(gallery5);
        imageViewArrayList.add(gallery6);
        imageViewArrayList.add(gallery7);
        imageViewArrayList.add(gallery8);
        imageViewArrayList.add(gallery9);
        aSwitch = (Switch) view.findViewById(R.id.item_discuss_on_off);
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (msg.arg1 == 1) {
                    Toast.makeText(context, "Post item failed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Post item success", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    public void addPictureListener() {
        plusimageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                // alert dialog settings
                builder.setTitle("Update profile picture");
                builder.setMessage("Take a new photo or select one from your existing photo library.");
                // set the "delete image" button, if the use click it, it will delete the image from the database
                // then it will also be deleted from the gallery
                builder.setPositiveButton("TAKE A PHOTO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dispatchTakePictureIntent();
                    }
                });
                // set the "set as a wallpaper" button, if the user click it, it will set the chosen image as device's wallpaper
                builder.setNegativeButton("SELECT A PHOTO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dispatchChoosePictureIntent();
                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface arg0) {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.crimson));
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.grey));
                    }
                });
                dialog.show();
            }
        });
    }



    public void addPostListener() {
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context = getActivity().getApplicationContext();
                if (!customExceptionHandler.checkStuffName(stuffName.getText().toString())) {
                    Toast toast=Toast.makeText(getActivity(),"Please input the correct item name", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0); // last two args are X and Y are used for setting position
                    toast.show();
                    return;
                }
                item.setName(stuffName.getText().toString());
                if (!customExceptionHandler.checkStuffPrice(stuffPrice.getText().toString())) {
                    Toast toast=Toast.makeText(getActivity(),"Please input the item price", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0); // last two args are X and Y are used for setting position
                    toast.show();
                    return;
                }
                item.setPrice(Double.parseDouble(stuffPrice.getText().toString()));
                if (!customExceptionHandler.checkStuffDuration(stuffDuration.getText().toString())) {
                    Toast toast=Toast.makeText(getActivity(),"Please input the duration", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0); // last two args are X and Y are used for setting position
                    toast.show();
                    return;
                }
                item.setDuration(Integer.parseInt(stuffDuration.getText().toString()));
                if (!customExceptionHandler.checkStuffSecurityDepositr(stuffSecurityDeposit.getText().toString())) {
                    Toast toast=Toast.makeText(getActivity(),"Please input the deposit amount", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0); // last two args are X and Y are used for setting position
                    toast.show();
                    return;
                }
                item.setSecurityDeposit(Double.parseDouble(stuffSecurityDeposit.getText().toString()));
                if (!customExceptionHandler.checkDeadLine(stuffDeadline.getYear() * 10000 + (stuffDeadline.getMonth() + 1) * 100 + stuffDeadline.getDayOfMonth())) {
                    Toast toast=Toast.makeText(getActivity(),"Please input the correct deadline", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0); // last two args are X and Y are used for setting position
                    toast.show();
                    return;
                }
                item.setDeadLine((stuffDeadline.getYear() * 10000 + (stuffDeadline.getMonth() + 1) * 100 + stuffDeadline.getDayOfMonth()));

                item.setDiscuss(aSwitch.isChecked());
                LatLng latLng = getLocation();
                item.setLatitude(latLng.latitude);
                item.setLongitude(latLng.longitude);
                item.setNewDegree(seekBar.getProgress());

                item.setDescription(stuffDescription.getText().toString());
                JsonObject jsonObject = new JsonObject();
                //noinspection AccessStaticViaInstance
                jsonObject.addProperty("user_id", buildAccount.getAccount().getUser().getId());
                jsonObject.add("item", item.toJson());
//
      //          UploadItemTask uploadItemTask = new UploadItemTask(jsonObject);
      //          uploadItemTask.execute();
     //           new UploadItemTask(jsonObject, getActivity().getApplicationContext(), localImages).execute();
                DefaultSocketClient client = new DefaultSocketClient(QueryType.UPLOAD_ITEM, jsonObject, handler);
                client.start();

                getDialog().dismiss();
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                Thread upload = new Thread(){
                    @Override
                    public void run(){
                        for (int j = 0; j < localImages.size(); j++) {
                            uploadImage(localImages.get(j), j);
                        }
                    }
                };
                upload.start();

                count = 0;

            }
        });
    }

    private void uploadImage(String filePath, int i){
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                context, // Context
                "us-east-1:ee0cb121-488e-450a-9490-a22cd5389a4f", // Identity Pool ID
                Regions.US_EAST_1 // Region
        );
        AmazonS3Client s3Client = new AmazonS3Client(credentialsProvider.getCredentials());
        //noinspection AccessStaticViaInstance
        PutObjectRequest por = new PutObjectRequest( "shair-application-image", "item-image/" + buildAccount.getAccount().getAccountName() + "_" + timeArraylist.get(i), new java.io.File(filePath));
        Log.e("upload picture finish: ", Integer.toString(i));
        s3Client.putObject(por);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name

        //Log.e("Local TimeStamp",timeStamp);
        String imageFileName = "Shair" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date()) + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.e("######################", mCurrentPhotoPath);
        galleryAddPic();
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }

    private void dispatchChoosePictureIntent(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_EXISTING_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        timeArraylist.add(timeStamp);
        //noinspection AccessStaticViaInstance
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == getActivity().RESULT_OK) {
            localImages.add(mCurrentPhotoPath);
            imageViewArrayList.get(count++).setImageBitmap(BitmapFactory.decodeFile(mCurrentPhotoPath));
            //noinspection AccessStaticViaInstance
            item.getImageArrayList().add("http://s3.amazonaws.com/shair-application-image/item-image/" + buildAccount.getAccount().getAccountName() + "_" + timeStamp);
            Log.e("*******************", mCurrentPhotoPath);
        }
        //noinspection AccessStaticViaInstance
        if(requestCode == REQUEST_EXISTING_PHOTO && resultCode == getActivity().RESULT_OK && null != data){
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            mCurrentPhotoPath = cursor.getString(columnIndex);
            localImages.add(mCurrentPhotoPath);
            imageViewArrayList.get(count++).setImageBitmap(BitmapFactory.decodeFile(mCurrentPhotoPath));
            //noinspection AccessStaticViaInstance
            item.getImageArrayList().add("http://s3.amazonaws.com/shair-application-image/item-image/" +  buildAccount.getAccount().getAccountName() + "_" + timeStamp);
            cursor.close();
        }
    }

    public LatLng getLocation() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        // Get Current Location
        List<String> providers = locationManager.getAllProviders();
        for (String provider : providers) {
            Location myLocation = locationManager.getLastKnownLocation(provider);
            if (myLocation  != null) {

                double latitude = myLocation.getLatitude();

                // Get longitude of the current location
                double longitude = myLocation.getLongitude();

                // Create a LatLng object for the current location
                //noinspection UnnecessaryLocalVariable
                LatLng latLng = new LatLng(latitude, longitude);
                return latLng;
            }

        }
        return null;
    }
}
