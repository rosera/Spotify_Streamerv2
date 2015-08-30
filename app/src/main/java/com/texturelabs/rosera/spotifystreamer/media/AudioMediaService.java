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


/*
 * Class: AudioMediaService
 * Description: Utility service for playing streaming audio
 *
 */

public class AudioMediaService  extends Service implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener {

    AudioMediaService () {

    }

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

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public void onPrepared(MediaPlayer mp) {

    }

    @Override
    public void onDestroy() {

    }
}
