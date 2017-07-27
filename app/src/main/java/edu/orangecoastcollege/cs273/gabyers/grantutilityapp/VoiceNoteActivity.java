package edu.orangecoastcollege.cs273.gabyers.grantutilityapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VoiceNoteActivity extends AppCompatActivity {

    private int CONSTANT = 101;
    private MediaPlayer mediaPlayer;
    private MediaRecorder recorder;
    private String OUTPUT_FILE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_note);

        //implement runtime permissions
        int recPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        int writePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        List permList = new ArrayList<String>();
        if(writePermission != PackageManager.PERMISSION_GRANTED)
            permList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(readPermission != PackageManager.PERMISSION_GRANTED)
            permList.add(Manifest.permission.READ_EXTERNAL_STORAGE);

        if(recPermission != PackageManager.PERMISSION_GRANTED)
            permList.add(Manifest.permission.RECORD_AUDIO);

        //Need string array for Permissions Used ArrayList for varying size
        if(permList.size() > 0)
        {
            String[] pList = new String[permList.size()];
            permList.toArray(pList);
            ActivityCompat.requestPermissions(this, pList, CONSTANT);
        }


        OUTPUT_FILE = Environment.getExternalStorageDirectory()+"/audiorecorder.3gpp";

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    public void buttonPressed(View view){
        switch(view.getId()){
            case R.id.startButton:
                try{
                    Toast.makeText(this, "Recording Started", Toast.LENGTH_SHORT).show();
                    beginRecording();
                }catch(Exception e){
                    e.printStackTrace();
                }
                break;
            case R.id.finishButton:
                try{
                    finishRecording();
                }catch(Exception e){
                    e.printStackTrace();
                }break;
            case R.id.playButton:
                try{
                    beginPlayback();
                }catch(Exception e){
                    e.printStackTrace();
                }break;
            case R.id.stopPlayButton:
                try{
                    finishPlayback();
                }catch(Exception e){
                    e.printStackTrace();
                }break;
        }
    }
    private void ditchMediaRecorder(){
    if(recorder != null)
        recorder.release();
}
    private void ditchMediaPlayer(){
    if(mediaPlayer != null){
        try{
            mediaPlayer.release();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
    private void finishRecording() {
        if(recorder != null)
            recorder.stop();
    }
    private void beginRecording() throws Exception {
        ditchMediaRecorder();
        File outfile = new File(OUTPUT_FILE);
        if(outfile.exists())
            outfile.delete();

        //implementing Media Recorder
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        //save to file
        recorder.setOutputFile(OUTPUT_FILE);
        recorder.prepare();
        recorder.start();
    }

    private void beginPlayback() throws Exception{
        ditchMediaPlayer();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(this,Uri.parse(OUTPUT_FILE));
        mediaPlayer.prepare();
        mediaPlayer.start();
    }
    private void finishPlayback(){
        if(mediaPlayer != null)
            mediaPlayer.stop();
    }

}
