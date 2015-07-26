package com.texturelabs.rosera.spotifystreamer;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.RetrofitError;

/**
 * Name: MainActivityFragment
 * Task 2 - Top Ten Titles for an Artist
 */

public class ArtistTopTenFragment extends Fragment {
    private static final String TAG_NAME = ArtistTopTenFragment.class.getSimpleName();
    private static final int                TAG_TITLE = 2;
    private ArrayAdapter<SpotifyContent>    mSpotifyTrackAdapter;
    private ArrayList<SpotifyContent>       mSpotifyTracks;
    String                                  mSpotifyArtist;
    String                                  mSpotifyID;
    ListView                                mListViewTitle;
    boolean                                 mTitleParcelable = false;

    public ArtistTopTenFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Grab parcelable information
        if (savedInstanceState == null || !savedInstanceState.containsKey("Tracks")) {
            mTitleParcelable = false;
        }
        else {
                mSpotifyTracks = savedInstanceState.getParcelableArrayList("Tracks");
                mTitleParcelable = true;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("Tracks", mSpotifyTracks);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

// Review: pass fragment arguments rather than amend constructor signature
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            this.mSpotifyID = intent.getStringExtra(Intent.EXTRA_TEXT);
            this.mSpotifyArtist = intent.getStringExtra(Intent.EXTRA_TITLE);
        }
// Review: pass fragment arguments rather than amend constructor signature

        // Change the title - todo (add string resource)
        // Amend the Fragment title
        ActionBar actionBar = getActivity().getActionBar();
        try {
            actionBar.setSubtitle(mSpotifyArtist);
        }
        catch (NullPointerException e){
            Log.i(TAG_NAME, "Exception:" + e.getMessage());
        }

        getActivity().setTitle("Top 10 Tracks");

        View rootView = inflater.inflate(R.layout.fragment_artist_top_ten , container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        mListViewTitle = (ListView) rootView.findViewById(R.id.listViewArtistTopTenTracks);

        if (!mTitleParcelable)
            this.mSpotifyTracks = new ArrayList<>();
        mSpotifyTrackAdapter = new CustomListAdapter(getActivity(), mSpotifyTracks);
        mListViewTitle.setAdapter(mSpotifyTrackAdapter);

        // Add click behaviour for the title artist listview
        mListViewTitle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, int position, long id) {
                Context context = getActivity();
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, "Intent for Task 2", duration);
                toast.show();

                // Grab the selected text from the adapterView (Artist)
//                final String strArtist = (String) adapterView.getItemAtPosition(position);
//                SpotifyContent trackContent = (SpotifyContent) adapterView.getItemAtPosition(position);

                /**
                 * Code snippet: Explicit intent - ArtistActivity
                 * http://developer.android.com/guide/components/intents-filters.html#ExampleExplicit
                 *
                 * Description: Initiate an activity for the selected artist
                 */
//                Intent intent = new Intent(getActivity(), ArtistActivity.class)
//                        .putExtra(Intent.EXTRA_TEXT, artistContent.mSpotifyTitle);
//                startActivity(intent);
            }
        });


        // Inflate the layout for this fragment
        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (!mTitleParcelable)
            // Execute the artist search
            new TracksAsyncTask().execute(mSpotifyID);
    }

    /**
     * Name: populateArtistListView
     * Callback on postExecute - ArtistAsyncTask
     * Add the resultant data to the listView control
     * If no data returned - output a message
     * Otherwise - populate the ListView with the Artists found
     */

    public void populateTrackListView() {

        mSpotifyTrackAdapter.notifyDataSetChanged();
    }


    /**
     * Name: ArtistAsyncTask
     * Called from  searchArtist.setOnEditorActionListener
     * Access the Spotify Web API and store the information returned
     */
    private class TracksAsyncTask extends AsyncTask<String, Void, Tracks> {

        SpotifyApi api;
        SpotifyService spotifyService;
        Tracks spotifyContent = (Tracks)null;

        @Override
        protected Tracks doInBackground(String... params) {
            String searchArtist = params[0];

            api = new SpotifyApi();
            spotifyService = api.getService();

            try {
                // Initialise the country code
                Map<String, Object> countryCode= new HashMap<>();
                countryCode.put("country", Locale.getDefault().getCountry());

                spotifyContent = spotifyService.getArtistTopTrack(searchArtist, countryCode);
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
            SpotifyContent newTrack = null;

            if (spotifyTracks == null) {
                Context context = getActivity();
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, "Please check your internet connection", duration);
                toast.show();
            }
            else {
                // Clear the tracks
                mSpotifyTracks.clear();

                for (Track item: spotifyTracks.tracks) {
                    try {
                        newTrack = new SpotifyContent(
                                item.name,
                                item.id,
                                item.album.name,
                                (item.album.images.get(0).url),
                                TAG_TITLE);
                    } catch (Exception e) {

                        // No image will cause an exception
                        if (item.album.images.size() == 0) {
                            // No image found - deal with this in the customer adapter
                            newTrack = new SpotifyContent(
                                    item.name,
                                    item.id,
                                    item.album.name,
                                    "",
                                    TAG_TITLE);
                        }

                        Log.i (TAG_NAME, item.name + " Error");
                    }

                    // Store the artists found
                    mSpotifyTracks.add(newTrack);
                }

                if (mSpotifyTracks.size() == 0) {
                    Context context = getActivity();
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, "No tracks found for " + mSpotifyArtist.toString(), duration);
                    toast.show();
                }
                else {
                    mSpotifyTrackAdapter.notifyDataSetChanged();
                }
            }
        }
    }
}
