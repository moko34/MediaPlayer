package com.example.mediaplayer;

import android.Manifest;
import android.content.ContentUris;
import android.database.Cursor;
import android.media.session.MediaSession;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.example.mediaplayer.model.MyVideo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.PerformanceHintManager;
import android.provider.MediaStore;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentContainerView;


import android.view.Menu;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements VideoFragment.VideoAdapterIsClicked {

    private FloatingActionButton fab;
    private Toolbar toolbar;
    private int code=1000;
    private VideoView videoView;
    private FragmentContainerView fragmentContainerView;
    private TextView condition;
    private MediaController mediaController;
    private ArrayList<MyVideo> videos;
    private VideoFragment videoFragment;
    private int currentVideoIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();
        requestPermission();
        videos=fetchUris();
        registerController();
        videoView.setMediaController(mediaController);
        videoView.suspend();


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            showAllVideos();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private ArrayList<MyVideo> fetchUris (){
        ArrayList<MyVideo> videos=new ArrayList<>();
        Uri uri;
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
            uri= MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        }else{
            uri=MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        }

        String[] projection=new String[]{MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media.DURATION,MediaStore.Video.Media._ID};
        String order=MediaStore.Video.Media.DATE_ADDED;
        try(Cursor cursor=getApplicationContext(). getContentResolver().query(uri,projection,null,null,order)){
            //identify column indices of projection
            int nameColumnIndex=cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
            int durationColumnIndex=cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);
            int idColumnIndex=cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);


            while (cursor.moveToNext()){
                String name = cursor.getString(nameColumnIndex);
                int duration=cursor.getInt(durationColumnIndex);
                long id=cursor.getLong(idColumnIndex);
                Uri newUri=ContentUris.withAppendedId(uri,id);
                videos.add(new MyVideo(name,duration,newUri));
            }

        }


        return videos;
    }

    private void showToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    private  void requestPermission(){
        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},code);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==code){
            if(grantResults.length<=0){
                requestPermission();
            }else {
                showToast("we good");
            }
        }
    }

    private void registerController(){
        mediaController=new MediaController(this);
        mediaController.setMediaPlayer(mediaPlayerControl);
        mediaController.setPrevNextListeners(view -> {
            if(currentVideoIndex < videos.size()-1){
                videoView.setVideoURI(Uri.parse(videos.get(currentVideoIndex+1).getUri().toString()));
                currentVideoIndex+=1;
            }else{
                showToast("No more videos");
            }

        }, view -> {
            if(currentVideoIndex>0){
            videoView.setVideoURI(Uri.parse(videos.get(currentVideoIndex-1).getUri().toString()));
            currentVideoIndex-=1;}
            else{
                showToast("No more videos");
            }

        });
        mediaController.setAnchorView(videoView);



    }

    MediaController.MediaPlayerControl mediaPlayerControl=new MediaController.MediaPlayerControl() {
        @Override
        public void start() {

        }

        @Override
        public void pause() {

        }

        @Override
        public int getDuration() {
            return 0;
        }

        @Override
        public int getCurrentPosition() {
            return 0;
        }

        @Override
        public void seekTo(int pos) {

        }

        @Override
        public boolean isPlaying() {
            return false;
        }

        @Override
        public int getBufferPercentage() {
            return 0;
        }

        @Override
        public boolean canPause() {
            return false;
        }

        @Override
        public boolean canSeekBackward() {
            return false;
        }

        @Override
        public boolean canSeekForward() {
            return false;
        }

        @Override
        public int getAudioSessionId() {
            return 0;
        }
    };

   private  void init(){
       videoView=findViewById(R.id.videoView);
       condition=findViewById(R.id.condition);
       fragmentContainerView=findViewById(R.id.fragmentContainer);
    }

    private void showAllVideos(){
       videoFragment= VideoFragment.newInstance(videos);
       videoView.setVisibility(View.GONE);
       fragmentContainerView.setVisibility(View.VISIBLE);
       condition.setVisibility(View.GONE);
       getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer,videoFragment).commit();

    }

    @Override
    public void videoIsClicked(MyVideo video,int position) {
       currentVideoIndex=position;
        getSupportFragmentManager().beginTransaction().remove(videoFragment).commit();
        condition.setVisibility(View.GONE);
        fragmentContainerView.setVisibility(View.GONE);
        videoView.setVisibility(View.VISIBLE);
        videoView.setVideoURI(video.getUri());
        videoView.start();

    }
}