package com.texturelabs.rosera.spotifystreamer.media;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.texturelabs.rosera.spotifystreamer.R;

/*
 * Class: MediaDialogFragment
 * Description: Play title sample from Spotify Web API
 *
 */
public class MediaDialogFragment extends DialogFragment {

    static final String LOG_TAG = MediaDialogFragment.class.getSimpleName();
    Context mContext;
    boolean isMediaPaused;
    boolean isMediaPlaying;



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

        getDialog().setTitle("Spotify Media Player");

        /*
         * Comment: Add listeners for the onscreen buttons
         */

        // Play onscreen button

        // Forward onscreen button

        // Rewind onscreen button

        // Seekbar onscreen button

        // Do something else
        return rootView;
    }


    /*
     * Name: setMediaPlayingState
     * Description: Amend the onscreen button to the correct state
     * Arguments:
     *          false   - show play button
     *          true    - show pause button
     */
    public void setMediaPlayingState(boolean mediaButtonState) {
        if (mediaButtonState) {
            // Set the button to paused
        } else {
            // Set the button to play
        }
    }


}
