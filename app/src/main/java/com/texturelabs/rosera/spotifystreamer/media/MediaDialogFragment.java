package com.texturelabs.rosera.spotifystreamer.media;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
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

    String  mSpotifyAlbum;
    String  mSpotifyArtist;
    String  mSpotifyUri;
    String  mSpotifyTitle;
    Long    mSpotifyDuration;

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

//        getDialog().setTitle("Spotify Media Player");


//        Intent intent = getActivity().getIntent();
        Bundle args = getArguments();

        if (args != null){
            if (args.containsKey("Album"))
                this.mSpotifyAlbum = args.getString("Album");

            if (args.containsKey("Name"))
                this.mSpotifyArtist = args.getString("Name");

            if (args.containsKey("ImageUrl"))
                this.mSpotifyUri = args.getString("ImageUrl");

            if (args.containsKey("Title"))
                this.mSpotifyTitle = args.getString("Title");

            if (args.containsKey("Duration"))
                this.mSpotifyDuration = args.getLong("Duration");
        }

        // Apply the data to the dialog
        TextView mediaAlbum = (TextView) rootView.findViewById(R.id.textviewMediaAlbum);
        mediaAlbum.setText(mSpotifyAlbum);

        TextView mediaArtist = (TextView) rootView.findViewById(R.id.textviewMediaArtist);
        mediaArtist.setText(mSpotifyArtist);

        ImageView mediaImageUri = (ImageView) rootView.findViewById(R.id.imageviewMediaAlbum);
//        mediaImageUri.setText(mSpotifyUri);

        // Apply image or add stock image if URI not entered
        if (mSpotifyUri.length() > 0) {
            Picasso.with(getActivity())
                    .load(mSpotifyUri)
                    .into(mediaImageUri);
        } else {
            mediaImageUri.setImageResource(R.drawable.blank_cd);
        }

        TextView mediaTitle = (TextView) rootView.findViewById(R.id.textviewMediaTitle);
        mediaTitle.setText(mSpotifyTitle);

        TextView mediaDuration = (TextView) rootView.findViewById(R.id.endTime);
        mediaDuration.setText(mSpotifyDuration.toString());


        /*
         * Get the id of the track to be played
         */

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
