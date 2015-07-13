package com.texturelabs.rosera.spotifystreamer;

import android.content.Context;
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
import java.util.List;
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
    private static final int TAG_TITLE = 2;
    private ArrayAdapter<SpotifyContent> mSpotifyTrackAdapter;
    private ArrayList<SpotifyContent> mSpotifyTracks;
    String spotifyArtist;
    String spotifyID;
    ListView listView;

    /**
     * Name: ArtistTopTenFragment
     * Pass the artist ID, needed for Spotify Web API
     */

    public ArtistTopTenFragment(String ID, String artist) {
        // Initialise memory for the Tracks Array List
//        mSpotifyTracks = new ArrayList<SpotifyContent>();
        this.spotifyArtist = artist;
        this.spotifyID = ID;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Grab parcelable information
        if (savedInstanceState == null || !savedInstanceState.containsKey("Tracks")) {
            this.mSpotifyTracks = new ArrayList<>();

//            // Call spotify API
//            if (mSpotifyTracks != null) {
//                mSpotifyTracks.clear();
//
//            }
        }
        else {
                mSpotifyTracks = savedInstanceState.getParcelableArrayList("Tracks");
                populateTrackListView();
        }

        // Try and kept the fragment alive
        setRetainInstance(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("Tracks", mSpotifyTracks);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Change the title - todo (add string resource)
        getActivity().setTitle("Top 10 Tracks");

        View rootView = inflater.inflate(R.layout.fragment_artist_top_ten , container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        listView = (ListView) rootView.findViewById(R.id.listViewArtistTopTenTracks);
        //listView.setAdapter(mSpotifyTrackAdapter);

        // Add click behaviour for the title artist listview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
//                        .putExtra(Intent.EXTRA_TEXT, artistContent._mainTitle);
//                startActivity(intent);
            }
        });


        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Initialise the list
//        mSpotifyTracks.clear();

        // Execute the artist search
        new TracksAsyncTask().execute(spotifyID);
    }

    /**
     * Name: populateArtistListView
     * Callback on postExecute - ArtistAsyncTask
     * Add the resultant data to the listView control
     * If no data returned - output a message
     * Otherwise - populate the ListView with the Artists found
     */

    public void populateTrackListView() {
        if (mSpotifyTrackAdapter != null)
            mSpotifyTrackAdapter.clear();

        mSpotifyTrackAdapter =
                new CustomListAdapter(this.getActivity(),
                        mSpotifyTracks);

        // Get a reference to the ListView, and attach this adapter to it.
        this.listView.setAdapter(mSpotifyTrackAdapter);
//        mSpotifyTrackAdapter.notifyDataSetChanged();
    }


    /**
     * Name: ArtistAsyncTask
     * Called from  searchArtist.setOnEditorActionListener
     * Access the Spotify Web API and store the information returned
     */
    private class TracksAsyncTask extends AsyncTask<String, Void, Tracks> {

        SpotifyApi api;
        SpotifyService spotifyService;

        @Override
        protected Tracks doInBackground(String... params) {
            String searchArtist = params[0];

            api = new SpotifyApi();
            spotifyService = api.getService();

            // Initialise the country code
            Map<String, Object> countryCode= new HashMap<>();
            countryCode.put("country", Locale.getDefault().getCountry());

            return (spotifyService.getArtistTopTrack(searchArtist, countryCode));
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(Tracks spotifyTracks) {

            Log.i("Debug:", "Download complete: result");
            SpotifyContent newTrack = null;

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

            // Update the listView
            populateTrackListView();
        }
    }
}
