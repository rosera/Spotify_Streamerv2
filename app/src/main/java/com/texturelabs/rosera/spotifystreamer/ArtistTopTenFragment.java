package com.texturelabs.rosera.spotifystreamer;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.texturelabs.rosera.spotifystreamer.media.MediaDialogFragment;
import com.texturelabs.rosera.spotifystreamer.utility.CustomListAdapter;
import com.texturelabs.rosera.spotifystreamer.utility.SpotifyContent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.RetrofitError;

/*
 * Class: ArtistTopTenFragment
 * Extends: Fragment
 * Description: Display the top ten tracks for an artist
 *
 */

public class ArtistTopTenFragment extends Fragment {
    private final String TAG_NAME = ArtistTopTenFragment.class.getSimpleName();
    private static final int                TAG_TITLE = 2;
    private ArrayAdapter<SpotifyContent>    mTrackAdapter;
    private ArrayList<SpotifyContent>       mSpotifyTracks = new ArrayList<>();

    private String                          mSpotifyID;
    private String                          mArtistName;
    private boolean                         mTitleParcelable = false;

    public ArtistTopTenFragment() {
    }

    /*************
     * Name: onCreate
     * @param savedInstanceState
     * Description:
     *
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Grab parcelable information
        if (savedInstanceState == null || !savedInstanceState.containsKey("Tracks")) {
            if (mSpotifyTracks != null)
                mTitleParcelable = true;
        }
        else {
            mSpotifyTracks = savedInstanceState.getParcelableArrayList("Tracks");
            mSpotifyID = savedInstanceState.getString("ID");
            mTitleParcelable = true;
        }
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
        outState.putParcelableArrayList("Tracks", mSpotifyTracks);
        outState.putString("ID", mSpotifyID);
    }


    /***************
     * Name: onActivityCreated
     * @param savedInstanceState
     * Description:
     */

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mSpotifyID != null) {
            new TracksAsyncTask().execute(mSpotifyID);
        }
    }


    /*******************
     * Name: onCreateView
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     * Description:
     *
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("Debug:", "onCreateView: ArtistTopTenFragment");
        ListView listViewTitle;
        String  strSpotifyArtist = "";

        View rootView = inflater.inflate(R.layout.fragment_artist_top_ten , container, false);

        Bundle args = getArguments();

        if (args != null){
            if (args.containsKey("ArtistID"))
                mSpotifyID = args.getString("ArtistID");

            if (args.containsKey("Name"))
                mArtistName = args.getString("Name");
        }

//        mSpotifyTracks = new ArrayList<>();
        mTrackAdapter = new CustomListAdapter(getActivity(), mSpotifyTracks);

        // Get a reference to the ListView, and attach this adapter to it.
        listViewTitle = (ListView) rootView.findViewById(R.id.listViewTopTen);
        listViewTitle.setAdapter(mTrackAdapter);

        if (mSpotifyID != null) {

            // Add click behaviour for the title artist listview
            final String finalStrSpotifyArtist = strSpotifyArtist;
            listViewTitle.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, final View view, int position, long id) {
                    Log.i("Debug:", "onItemClick: ArtistTopTenFragment");

                    /**
                     * Code snippet: Explicit intent - ArtistActivity
                     * http://developer.android.com/guide/components/intents-filters.html#ExampleExplicit
                     *
                     * Description: Initiate an activity for the selected artist
                     */

                    // Grab the selected item information and pass to the dialog fragment

                    // Todo: Change to an tracks object
                    Bundle arguments = new Bundle();

                    arguments.putString("Artist", finalStrSpotifyArtist);
                    arguments.putInt("Index", position);
                    arguments.putParcelableArrayList("Tracks", mSpotifyTracks);

                    FragmentManager fm = getFragmentManager();
                    MediaDialogFragment mediaFragment = new MediaDialogFragment();
                    mediaFragment.setArguments(arguments);
                    mediaFragment.show(fm, "dialog");
                }
            });
        }

        // Inflate the layout for this fragment
        return rootView;
    }

    /**
     * Name: ArtistAsyncTask
     * Called from  searchArtist.setOnEditorActionListener
     * Access the Spotify Web API and store the information returned
     *
     */
    private class TracksAsyncTask extends AsyncTask<String, Void, Tracks> {

        SpotifyApi api;
        SpotifyService spotifyService;
        Tracks spotifyContent = (Tracks)null;
        String  mSearchArtist;

        @Override
        protected Tracks doInBackground(String... params) {
            mSearchArtist = params[0];

            api = new SpotifyApi();
            spotifyService = api.getService();

            try {
                // Initialise the country code
                Map<String, Object> countryCode= new HashMap<>();
                countryCode.put("country", Locale.getDefault().getCountry());

                spotifyContent = spotifyService.getArtistTopTrack(mSearchArtist, countryCode);
            }
            catch (RetrofitError ex) {
                Log.i(TAG_NAME, ex.toString());
            }

            return (spotifyContent);
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(Tracks spotifyTracks) {
            Log.i("Debug:", "Download complete: result");

            // Clear the tracks
            mSpotifyTracks.clear();

            if (spotifyTracks.tracks.size() == 0) {
                displayFragmentMessage("No tracks found for " + mArtistName.toString());
            }
            else {
                setTrackInformation(spotifyTracks);
                mTrackAdapter.notifyDataSetChanged();
            }
        }
    }

    public void displayFragmentMessage(String msg) {
        Context context = getActivity();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, msg, duration);
        toast.show();
    }


    public void setTrackInformation (Tracks spotifyTracks) {

        for (Track item: spotifyTracks.tracks) {
            SpotifyContent newTrack = null;

            try {
                newTrack = new SpotifyContent(
                        item.name,
                        item.id,
                        item.id,
                        item.album.name,
                        (item.album.images.get(0).url),
                        TAG_TITLE,
                        item.preview_url);
            } catch (Exception e) {

                // No image will cause an exception
                if (item.album.images.size() == 0) {
                    // No image found - deal with this in the customer adapter
                    newTrack = new SpotifyContent(
                            item.name,
                            item.id,
                            item.id,
                            item.album.name,
                            "",
                            TAG_TITLE,
                            item.preview_url);
                }

                Log.i (TAG_NAME, item.name + " Error");
            }

            // Store the artists found
            mSpotifyTracks.add(newTrack);
        }
    }
}
