/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  18641 java smart phone development - final project - Shair
 *
 *  Name: Sen Yue (seny)
 *        Zheng Lei (zlei)
 *
 *  class name: EditProfileDialog
 *
 *  class properties:
 *  dialogView:View
 *  mCurrentPhotoPath:String
 *  profileImg:ImageView
 *  profileName:EditText
 *  profileBirthDate:EditText
 *  profileEmail:EditText
 *  profilePhone:EditText
 *  profileLocation:EditText
 *  confirmButton:Button
 *  customExceptionHandler:CustomExceptionHandler
 *
 *
 *  class methods:
 *  onActivityResult(int requestCode, int resultCode, Intent data):void
 *  uploadImage(String filePath):void
 *  dispatchChoosePictureIntent():void
 *  dispatchTakePictureIntent():void
 *  createImageFile():File
 *  galleryAddPic():void
 *  initial():void
 *  addConfirmListener():void
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package com.example.ethan.shairversion1application.settings;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.ethan.shairversion1application.R;
import com.example.ethan.shairversion1application.cruditem.BuildAccount;
import com.example.ethan.shairversion1application.entities.User;
import com.example.ethan.shairversion1application.exception.CustomExceptionHandler;
import com.example.ethan.shairversion1application.socket.DefaultSocketClient;
import com.example.ethan.shairversion1application.socket.QueryType;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class EditProfileDialog extends DialogFragment {
    private View dialogView;
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_EXISTING_PHOTO = 2;
    private String mCurrentPhotoPath;
    private ImageView profileImg;
    private EditText profileName;
    private EditText profileBirthDate;
    private EditText profileEmail;
    private EditText profilePhone;
    private EditText profileLocation;
    private Button confirmButton;
    private CustomExceptionHandler customExceptionHandler;
    private BuildAccount buildAccount;
    private Context context;
    private String imageLink;
    private boolean update = false;
    public EditProfileDialog(){}

    @SuppressLint("InflateParams")
    @SuppressWarnings("NullableProblems")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        customExceptionHandler = new CustomExceptionHandler();
        dialogView = inflater.inflate(R.layout.settings_editprofiledialog, null);
        builder.setView(dialogView);
        context = getActivity().getApplicationContext();
        initial();
        addConfirmListener();
        buildAccount = new BuildAccount();
        TextView cancel = (TextView) dialogView.findViewById(R.id.cancel_window);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update = false;
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                getDialog().dismiss();
            }
        });

        ImageView camera = (ImageView) dialogView.findViewById(R.id.add_image);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        profileBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Date Picker Dialog
                //noinspection OctalInteger
                DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox
                                profileBirthDate.setText(Integer.toString(year * 10000 + (monthOfYear + 1) * 100 + dayOfMonth));
                            }
                        }, 1990, 06, 01);
                dpd.show();

            }
        });

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

    @Override
    // handle the activity of choosing images from device
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        //noinspection AccessStaticViaInstance
        imageLink = "http://s3.amazonaws.com/shair-application-image/profile-image/" + buildAccount.getAccount().getAccountName();
        //noinspection AccessStaticViaInstance
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == getActivity().RESULT_OK) {
            update = true;
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
            cursor.close();
            update = true;
        }
//        profileImg.setImageBitmap(BitmapFactory.decodeFile(mCurrentPhotoPath));
        Ion.with(profileImg).load(mCurrentPhotoPath);
    }


    private void uploadImage(String filePath){
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                context, // Context
                "us-east-1:ee0cb121-488e-450a-9490-a22cd5389a4f", // Identity Pool ID
                Regions.US_EAST_1 // Region
        );
        AmazonS3Client s3Client = new AmazonS3Client(credentialsProvider.getCredentials());
        //noinspection AccessStaticViaInstance
        PutObjectRequest por = new PutObjectRequest( "shair-application-image", "profile-image/" + buildAccount.getAccount().getAccountName(), new java.io.File(filePath));
        s3Client.putObject(por);
    }



    private void dispatchChoosePictureIntent(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_EXISTING_PHOTO);
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
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        Log.e("Local TimeStamp",timeStamp);
        String imageFileName = "Shair" + timeStamp + "_";
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

    public void initial() {
        profileName = (EditText) dialogView.findViewById(R.id.profile_name);
        profileBirthDate = (EditText) dialogView.findViewById(R.id.profile_birthday);
        profileEmail = (EditText) dialogView.findViewById(R.id.profile_email);
        profilePhone = (EditText) dialogView.findViewById(R.id.profile_phone);
        profileLocation = (EditText) dialogView.findViewById(R.id.profile_location);
        profileImg = (ImageView) dialogView.findViewById(R.id.profile_image);
        confirmButton = (Button) dialogView.findViewById(R.id.edit_profile_confirm);
//noinspection AccessStaticViaInstance
        User user = new BuildAccount().getAccount().getUser();
        profileName.setText(user.getName());
        profileBirthDate.setText(user.getBirthday() == 0 ? "" : Integer.toString(user.getBirthday()));
        profileEmail.setText(user.getEmail());
        profilePhone.setText(user.getPhone());
        profileLocation.setText(user.getAddress());
        Ion.with(profileImg).load(user.getImgPath());
    }

    public void addConfirmListener() {
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!customExceptionHandler.checkProfileName(profileName.getText().toString())) {
                    Toast toast=Toast.makeText(getActivity(),"Please input the correct profile name", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0); // last two args are X and Y are used for setting position
                    toast.show();
                    return;
                }
                //noinspection AccessStaticViaInstance
                User user = buildAccount.getAccount().getUser();
                if(update){
                    user.setImgPath(imageLink);
                    Thread upload = new Thread(){
                        @Override
                        public void run(){
                            uploadImage(mCurrentPhotoPath);
                        }
                    };
                    upload.start();
                }
                // get updated value
                String updatedName = profileName.getText().toString();
                int updatedBirthdate = Integer.parseInt(profileBirthDate.getText().toString().length() == 0 ? "0" : profileBirthDate.getText().toString());
                String updatedEmail = profileEmail.getText().toString();
                String updatedPhone = profilePhone.getText().toString();
                String updatedLocation = profileLocation.getText().toString();
                // update the values in static account
                user.setName(updatedName);
                user.setBirthday(updatedBirthdate);
                user.setEmail(updatedEmail);
                user.setPhone(updatedPhone);
                user.setAddress(updatedLocation);
                // build json to send to the server
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("id",user.getId());
                jsonObject.addProperty("name",updatedName);
                jsonObject.addProperty("birthdate", updatedBirthdate);
                jsonObject.addProperty("email",updatedEmail);
                jsonObject.addProperty("phone",updatedPhone);
                jsonObject.addProperty("location", updatedLocation);
                jsonObject.addProperty("image_path", update ? imageLink : user.getImgPath());
                DefaultSocketClient client = new DefaultSocketClient(QueryType.EDIT_PROFILE, jsonObject);
                client.start();
                Toast.makeText(getActivity(),"Profile Updated",Toast.LENGTH_SHORT).show();
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                getDialog().dismiss();
            }
        });
    }
}

