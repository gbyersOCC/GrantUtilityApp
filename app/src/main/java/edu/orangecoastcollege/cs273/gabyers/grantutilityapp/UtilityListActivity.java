package edu.orangecoastcollege.cs273.gabyers.grantutilityapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.AnyRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


import java.util.ArrayList;

public class UtilityListActivity extends AppCompatActivity {
    private static final int CONSTANT = 100;
    private Uri imageURI;
    private ImageView profilePicture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utility_list);
        profilePicture = (ImageView)findViewById(R.id.profileImageView);
    }




    public void addProfilePic(View view){
        //Need array list for all permissions
        ArrayList<String> permissionsList = new ArrayList<>();

        //hook up each one to manifest (also checks to see current state of permission)
        int cameraPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        int writePermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);


        //check to see if permission is granted for each one
        //if its not equal to granted add it to ArrayList
        if(cameraPermission != PackageManager.PERMISSION_GRANTED)
            permissionsList.add(Manifest.permission.CAMERA);

        if(writePermission != PackageManager.PERMISSION_GRANTED)
            permissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(readPermission != PackageManager.PERMISSION_GRANTED)
            permissionsList.add(Manifest.permission.READ_EXTERNAL_STORAGE);

        //Need string array for Permissions Used ArrayList for varying size
        if(permissionsList.size() > 0)
        {
            String[] permList = new String[permissionsList.size()];
            permissionsList.toArray(permList);
            ActivityCompat.requestPermissions(this, permList, CONSTANT);
        }

        //open imageGallery (by checking that all permissions equal granted)
        if(cameraPermission==PackageManager.PERMISSION_GRANTED && writePermission==PackageManager.PERMISSION_GRANTED && readPermission==PackageManager.PERMISSION_GRANTED){
            //use an intent to launch gallery and take pics
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, CONSTANT);
        }else{
            imageURI = getUriToResource(this, R.drawable.pie_skull);
        }


    }

    public void startVoiceActivity(View view){

        if(view.getId() == R.id.voiceNoteButton){
            Intent voiceIntent = new Intent(this, VoiceNoteActivity.class);
            startActivity(voiceIntent);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data ){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CONSTANT && resultCode==RESULT_OK){
            imageURI = data.getData();
            profilePicture.setImageURI(imageURI);
        }else{
            imageURI = getUriToResource(this, R.drawable.pie_skull);
        }
    }

    public static Uri getUriToResource(@NonNull Context context, @AnyRes int resId)throws Resources.NotFoundException{
        Resources res = context.getResources();
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+
                "://" + res.getResourcePackageName(resId)+
                '/'+res.getResourceTypeName(resId)+
                '/'+res.getResourceEntryName(resId));
    }
}
