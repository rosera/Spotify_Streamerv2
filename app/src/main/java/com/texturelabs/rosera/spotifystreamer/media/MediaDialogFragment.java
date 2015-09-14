package com.texturelabs.rosera.spotifystreamer.media;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.texturelabs.rosera.spotifystreamer.R;
import com.texturelabs.rosera.spotifystreamer.utility.SpotifyContent;

import java.util.ArrayList;

/*
 * Class: MediaDialogFragment
 * Description: Play title sample from Spotify Web API
 *
 * Save yourself some pain and read http://www.vogella.com/tutorials/AndroidServices/article.html
 *
 */
public class MediaDialogFragment extends DialogFragment
        implements View.OnClickListener {

    static final String LOG_TAG = MediaDialogFragment.class.getSimpleName();

    Context     mContext;
    String      mSpotifyAlbum;          // Album name
    String      mSpotifyArtist;         // Artist name
    String      mSpotifyUri;            // Image URI
    String      mSpotifyTitle;          // Title

    String      mSpotifyPreview;       // Song duration
    private     ArrayList<SpotifyContent> mSpotifyTracks;

    int         mTrackIndex;

/***************************/
/*
 * Todo: Refactor
 * Add controls for dialog fragment
 */
    TextView    mediaAlbum;
    TextView    mediaArtist;
    ImageView   mediaImageUri;
    TextView    mediaTitle;

    ImageButton media_play;
    Boolean     mButtonState = false;
    Boolean     mSavedInstance = false;
    int         mTrackProgress = 0;

    Intent              intentAudioService;     // Audio Service
    BroadcastReceiver   broadcastReceiver;      // Broadcast receiver handle messages

    public MediaDialogFragment() {
        mContext = getActivity();
    }

    /*
     * Name: onCreateView
     * Description:
     * Comment:
     *
     */



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_media, container,
                false);

        mediaAlbum      = (TextView) rootView.findViewById(R.id.textviewMediaAlbum);
        mediaArtist     = (TextView) rootView.findViewById(R.id.textviewMediaArtist);
        mediaImageUri   = (ImageView) rootView.findViewById(R.id.imageviewMediaAlbum);
        mediaTitle      = (TextView) rootView.findViewById(R.id.textviewMediaTitle);

        // Grab parcelable information
        if (savedInstanceState == null || !savedInstanceState.containsKey("Media")) {

            Bundle args = getArguments();

/************************/
        /*
         * Todo:
         * Refactor - public void getArgumentsMedia(Bundle args)
         */
            if (args != null){
                if (args.containsKey("Tracks"))
                    this.mSpotifyTracks = args.getParcelableArrayList("Tracks");

                if (args.containsKey("Index"))
                    this.mTrackIndex = args.getInt("Index");

                if (args.containsKey("Artist"))
                    this.mSpotifyArtist = args.getString("Artist");
            }        // Todo: Create intent for audioservice

            /************************/
            // Todo: Populate the current track from mSpotifyTracks
            // Assumption: 10 Tracks - i.e. 0 to 9 available
            // public void setTrack(int trackIndex)

            SpotifyContent selectedTrack = mSpotifyTracks.get(mTrackIndex);
            mSpotifyAlbum = selectedTrack.getArtistAlbum();
            mSpotifyUri = selectedTrack.getArtistURI();
            mSpotifyTitle = selectedTrack.getArtistTitle();
            mSpotifyPreview = selectedTrack.getArtistPreview();
        }
        else {
            mSavedInstance = true;

            mSpotifyTracks = savedInstanceState.getParcelableArrayList("Media");
            mTrackIndex = savedInstanceState.getInt("Index");
            mButtonState = savedInstanceState.getBoolean("State");
            mTrackProgress = savedInstanceState.getInt("Progress");

            /************************/
            // Todo: Populate the current track from mSpotifyTracks
            // Assumption: 10 Tracks - i.e. 0 to 9 available
            // public void setTrack(int trackIndex)

            SpotifyContent selectedTrack = mSpotifyTracks.get(mTrackIndex);
            mSpotifyAlbum = selectedTrack.getArtistAlbum();
            mSpotifyUri = selectedTrack.getArtistURI();
            mSpotifyTitle = selectedTrack.getArtistTitle();
            mSpotifyPreview = selectedTrack.getArtistPreview();

            /************************/
        }



/********************/
        /*
         * Todo: Refactor
         */
        mediaTitle.setText(mSpotifyTitle);
        mediaAlbum.setText(mSpotifyAlbum);
        mediaArtist.setText(mSpotifyArtist);
        if (mSpotifyUri.length() > 0) {
            Picasso.with(getActivity())
                    .load(mSpotifyUri)
                    .into(mediaImageUri);
        } else {
            mediaImageUri.setImageResource(R.drawable.blank_cd);
        }


            /************************/
        /*
         * Get the id of the track to be played
         */

        // Set onClickListener()
        media_play = (ImageButton)rootView.findViewById(R.id.media_play);
        media_play.setOnClickListener(this);

        ImageButton media_fforward = (ImageButton)rootView.findViewById(R.id.media_fforward);
        media_fforward.setOnClickListener(this);

        //        // Rewind onscreen button - media_rewind
        ImageButton media_rewind = (ImageButton)rootView.findViewById(R.id.media_rewind);
        media_rewind.setOnClickListener(this);

        /*
         * Todo:
         * Refactor: seekBar Listener
         */
//        // Seekbar onscreen button - seekBar
        final SeekBar media_seekBar = (SeekBar)rootView.findViewById(R.id.seekBar);

        media_seekBar.setClickable(false);
        media_seekBar.setMax(30000);
        media_seekBar.setProgress(1);

        media_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                media_seekBar.setProgress(intentAudioService.getProgress()) ;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

/************************/
//        if (!mSavedInstance) {

            /*
             * Todo:
             * Refactor: Split out the broadcast code
             */
            IntentFilter filter = new IntentFilter();
            filter.addAction(AudioMediaService.SET_SEEK);

            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();

                    switch (action) {
                        case AudioMediaService.SET_SEEK:
                            mTrackProgress = intent.getIntExtra("SEEKBAR_POSITION", 0);

                            media_seekBar.setProgress(mTrackProgress);

                            break;

                        default:
                            break;
                    }
                }
            };

            getActivity().registerReceiver(broadcastReceiver, filter);

            /************************/

            /************************/

            /*
             * Todo:
             * Refactor
             */

            // Todo: Create intent for audioservice
            intentAudioService = new Intent(getActivity(), AudioMediaService.class);

            // Initialise the MediaPlayer
            if (!mSavedInstance)
                intentAudioService.setAction("ACTION_PLAY");
            else {
                intentAudioService.setAction("ACTION_PAUSE");
                intentAudioService.putExtra("SEEKBAR_POSITION", mTrackProgress);
            }

            intentAudioService.putExtra("PREVIEW_URI", mSpotifyPreview);
            getActivity().startService(intentAudioService);
            setMediaPlayingState();
//        }
/************************/

        // Do something else
        return rootView;
    }


    /******************
     * Name: onSaveInstanceState
     * @param outState
     * Description:
     *
     */

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("Media", mSpotifyTracks);
        outState.putInt("Index", mTrackIndex);
        outState.putBoolean("State", mButtonState);
        outState.putInt("Progress", mTrackProgress);
    }


    //
    /*
     * Name: setMediaPlayingState
     * Description: Amend the onscreen button to the correct state
     * Arguments:
     *          false   - show play button - drawable/ic_media_play
     *          true    - show pause button - drawable/ic_media_pause
     *
     */
    public void setMediaPlayingState() {

        // If button is currently set to play (false) - change state to pause (true)
        if (!mButtonState) {
            // Set the button to paused
            media_play.setImageResource(android.R.drawable.ic_media_pause);
            mButtonState = true;

        } else {
            // Set the button to play
            media_play.setImageResource(android.R.drawable.ic_media_play);
            mButtonState = false;
        }
    }

    @Override
    public void onClick(View v) {
        Log.i(LOG_TAG, "onClick: Start Service");
        Intent intent = new Intent();

        switch (v.getId()) {
            case R.id.media_play:

                // Play
                if (!mButtonState) {
                    intent.setAction(AudioMediaService.ACTION_PLAY);
                    getActivity().sendBroadcast(intent);
                } else {
                    intent.setAction(AudioMediaService.ACTION_PAUSE);
                    getActivity().sendBroadcast(intent);
                }

                setMediaPlayingState();
                //Toast.makeText(getActivity(), "play/pause button pressed", Toast.LENGTH_SHORT).show();
                break;
            case R.id.media_fforward:
                if (mTrackIndex < 9) {
                    mTrackIndex++;

                    SpotifyContent selectedTrack = mSpotifyTracks.get(mTrackIndex);

                    mSpotifyAlbum = selectedTrack.getArtistAlbum();
                    mSpotifyUri = selectedTrack.getArtistURI();
                    mSpotifyTitle = selectedTrack.getArtistTitle();
                    mSpotifyPreview = selectedTrack.getArtistPreview();

                    mediaTitle.setText(mSpotifyTitle);
                    mediaAlbum.setText(mSpotifyAlbum);
                    mediaArtist.setText(mSpotifyArtist);
                    if (mSpotifyUri.length() > 0) {
                        Picasso.with(getActivity())
                                .load(mSpotifyUri)
                                .into(mediaImageUri);
                    } else {
                        mediaImageUri.setImageResource(R.drawable.blank_cd);
                    }

                    getActivity().stopService(intentAudioService);

                    intentAudioService.setAction("ACTION_PLAY");
                    intentAudioService.putExtra("PREVIEW_URI", mSpotifyPreview);
                    getActivity().startService(intentAudioService);

                    // Reset the button
                    mButtonState = false;
                    setMediaPlayingState();
                }

//                intent.setAction(AudioMediaService.ACTION_FFORWARD);
//                getActivity().sendBroadcast(intent);


//                intentAudioService.setAction("ACTION_FFORWARD");
//                getActivity().startService(intentAudioService);
//                Toast.makeText(getActivity(), "fforward button pressed", Toast.LENGTH_SHORT).show();
                break;
            case R.id.media_rewind:
                // Initialise the MediaPlayer
                if (mTrackIndex > 0) {
                    mTrackIndex--;

                    SpotifyContent selectedTrack = mSpotifyTracks.get(mTrackIndex);

                    mSpotifyAlbum = selectedTrack.getArtistAlbum();
                    mSpotifyUri = selectedTrack.getArtistURI();
                    mSpotifyTitle = selectedTrack.getArtistTitle();
                    mSpotifyPreview = selectedTrack.getArtistPreview();

                    mediaTitle.setText(mSpotifyTitle);
                    mediaAlbum.setText(mSpotifyAlbum);
                    mediaArtist.setText(mSpotifyArtist);
                    if (mSpotifyUri.length() > 0) {
                        Picasso.with(getActivity())
                                .load(mSpotifyUri)
                                .into(mediaImageUri);
                    } else {
                        mediaImageUri.setImageResource(R.drawable.blank_cd);
                    }

                    getActivity().stopService(intentAudioService);

                    intentAudioService.setAction("ACTION_PLAY");
                    intentAudioService.putExtra("PREVIEW_URI", mSpotifyPreview);
                    getActivity().startService(intentAudioService);

                    // Reset the button
                    mButtonState = false;
                    setMediaPlayingState();
                }
//                intent.setAction(AudioMediaService.ACTION_REWIND);
//                getActivity().sendBroadcast(intent);

//                intentAudioService.setAction("ACTION_REWIND");
//                getActivity().startService(intentAudioService);
//                Toast.makeText(getActivity(), "rewind button pressed", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;

        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // View going away - broadcast a pause
//        Intent intent = new Intent();
//        intent.setAction(AudioMediaService.ACTION_PAUSE);
//        getActivity().sendBroadcast(intent);
//
//        setMediaPlayingState();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Clean up
        try {
            getActivity().stopService(intentAudioService);
            getActivity().unregisterReceiver(broadcastReceiver);
        }
        catch (NullPointerException np) {
            np.printStackTrace();
        }

    }
}
