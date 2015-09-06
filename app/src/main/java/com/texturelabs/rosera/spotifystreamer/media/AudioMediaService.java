package com.texturelabs.rosera.spotifystreamer.media;

/*
 * Class: AudioMediaService
 * Description: Provide the basis for the player used in the dialog fragment
 *
 */

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import java.net.URI;


/*
 * Class: AudioMediaService
 * Description: Utility service for playing streaming audio
 *
 */

public class AudioMediaService  extends Service implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener {

    public static final String LOG_TAG = AudioMediaService.class.getSimpleName();
    MediaPlayer mediaPlayer;
    URI uriSpotify;

    AudioMediaService () {

    }


    /**************************************************************************
     * Name: AudioMedia Player
     * @return
     *
     */

    public boolean AudioMediaPlayer() {
        boolean setPlayState = false;

//        String url = "http://........"; // your URL here
//        MediaPlayer mediaPlayer = new MediaPlayer();
//        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//        mediaPlayer.setDataSource(url);
//        mediaPlayer.prepare(); // might take long! (for buffering, etc)
//        mediaPlayer.start();

        return setPlayState;
    }

    /**************************************************************************
     * Name: onBind
     * @param intent
     * @return IBinder object
     */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        Log.v("MediaPlayer method call", "onCreate");

        // Add URI for the title selected
        //mediaPlayer = MediaPlayer.create(uriSpotify);

        // Add a listener for completion of the streamed content
        mediaPlayer.setOnCompletionListener(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("MediaPlayer method call", "onStartCommand");

        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }

        return START_STICKY;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.v("MediaPlayer method call", "onCompletion");
        stopSelf();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Check player
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }

        mediaPlayer.release();
        Log.v("MediaPlayer method call", "onDestroy");
    }
}
