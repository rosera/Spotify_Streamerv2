package com.texturelabs.rosera.spotifystreamer.media;

/*
 * Class: AudioMediaService
 * Description: Provide the basis for the player used in the dialog fragment
 *
 */

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.os.Handler;

import com.texturelabs.rosera.spotifystreamer.utility.SpotifyContent;

import java.io.IOException;
import java.net.URI;
import java.util.logging.LogRecord;


/*
 * Class: AudioMediaService
 * Description: Utility service for playing streaming audio
 *
 */

public class AudioMediaService  extends Service implements
        MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener {

    public static final String LOG_TAG = AudioMediaService.class.getSimpleName();

    public static final String ACTION_PLAY = "com.texturelabs.rosera.spotifystreamer.media.ACTION_PLAY";
    public static final String ACTION_PAUSE = "com.texturelabs.rosera.spotifystreamer.media.ACTION_PAUSE";
    public static final String ACTION_REWIND = "com.texturelabs.rosera.spotifystreamer.media.ACTION_REWIND";
    public static final String ACTION_FFORWARD = "com.texturelabs.rosera.spotifystreamer.media.ACTION_FFORWARD";

    public static final String SET_SEEK = "com.texturelabs.rosera.spotifystreamer.media.SET_SEEK";

    private BroadcastReceiver broadcastReceiver;

    int     mTimeLapse       = 0;

    MediaPlayer mediaPlayer;
    Intent      intentSeekBar;
    Handler handlerSeekBar;

    public AudioMediaService () {
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
        mediaPlayer = new MediaPlayer();
        intentSeekBar = new Intent(SET_SEEK);
        handlerSeekBar = new Handler();

        /* only listen for these messages */
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_PLAY);
        filter.addAction(ACTION_PAUSE);
        filter.addAction(ACTION_FFORWARD);
        filter.addAction(ACTION_REWIND);

/************************/

        /*
         * Todo:
         * Refactor - setup listeners
         */
        // Add a listener for completion of the streamed content
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
                mediaPlayer.start();
            }
        });
//        mediaPlayer.setOnCompletionListener(this);
/************************/


/************************/
        /*
         * Todo:
         * Refactor - Broadcast receiver
         */
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                switch (action) {
                    case ACTION_PLAY:
                        if (!mediaPlayer.isPlaying()) {
                            mediaPlayer.start();

                            // Update the seekBar
                            handlerSeekBar.removeCallbacks(seekBarProgress);
                            handlerSeekBar.postDelayed(seekBarProgress, 300);
                        }
                        break;

                    case ACTION_PAUSE:
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.pause();
                        }
                        break;

//                    case ACTION_REWIND:
//                        if (mediaPlayer.isPlaying()) {
//                            mediaPlayer.stop(            /************************/
//
////                            mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()-TimeLapse);
////
////                            handlerSeekBar.removeCallbacks(seekBarProgress);
////                            handlerSeekBar.postDelayed(seekBarProgress, 300);
//                        }
//                        break;
//
//                    case ACTION_FFORWARD:
//                        if (mediaPlayer.isPlaying()) {
//                            mediaPlayer.stop();
////                            mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()+TimeLapse);
////
////                            handlerSeekBar.removeCallbacks(seekBarProgress);
////                            handlerSeekBar.postDelayed(seekBarProgress, 300);
//                        }        // Update the seekBar
//
//                        break;

                    default:
                        break;
                }
            }
        };

        registerReceiver(broadcastReceiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("MediaPlayer method call", "onStartCOmmand");
        String action = intent.getAction();
        switch (action) {
            case "ACTION_PLAY":

                if (!mediaPlayer.isPlaying()) {
                    String previewURI = intent.getStringExtra("PREVIEW_URI");

                    try {
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mediaPlayer.setDataSource(previewURI);
                        mediaPlayer.prepareAsync();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case "ACTION_PAUSE":

                if (!mediaPlayer.isPlaying()) {
                    String previewURI = intent.getStringExtra("PREVIEW_URI");
                    mTimeLapse = intent.getIntExtra("SEEKBAR_POSITION", 0);

                    try {
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mediaPlayer.setDataSource(previewURI);
                        mediaPlayer.prepareAsync();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;

            default:
                break;
        }
        return START_STICKY;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.v("MediaPlayer method call", "onCompletion");
        stopSelf();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // Update the seekBar
        handlerSeekBar.removeCallbacks(seekBarProgress);
        handlerSeekBar.postDelayed(seekBarProgress, 300);
        // Check player
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }

        // Clean up
        handlerSeekBar.removeCallbacks(seekBarProgress);
        mediaPlayer.release();
        unregisterReceiver(broadcastReceiver);
        Log.v("MediaPlayer method call", "onDestroy");
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        // Check player
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()+mTimeLapse);
            mediaPlayer.start();
        }

        // Update the seekBar
        handlerSeekBar.removeCallbacks(seekBarProgress);
        handlerSeekBar.postDelayed(seekBarProgress, 250);
    }

    public int getProgress() {
        int mediaPos = 0;
        if (mediaPlayer.isPlaying())
            mediaPos= mediaPlayer.getCurrentPosition();

        return mediaPos;
    }

    private Runnable seekBarProgress = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer.isPlaying()) {
                intentSeekBar.putExtra("SEEKBAR_POSITION", mediaPlayer.getCurrentPosition());
                sendBroadcast(intentSeekBar);
                handlerSeekBar.postDelayed(this, 250);
            }
        }
    };
}
