package com.texturelabs.rosera.spotifystreamer;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;


    /**
     * Name: MainActivityFragment
     * Task 1 - UI to search for an Artist
     */

public class MainActivityFragment extends Fragment {

    // Add global tag for debug
    private static final String TAG_NAME = MainActivityFragment.class.getSimpleName();
    private static final int TAG_ARTIST = 1;

    private ArrayAdapter<SpotifyContent>    mSpotifyArtistAdapter;
    private ArrayList<SpotifyContent>       mSpotifyArtist;
    private static ListView listView;
    private List<Artist> artists;

    /**
     * Name: MainActivityFragment
     * Initialise  memory
     */

    public MainActivityFragment() {
        // Initialise memory for the Artist Array List
//        if (mSpotifyArtist == null)
//            mSpotifyArtist = new ArrayList<SpotifyContent>();
    }

    /**
     * Name: onCreate
     * Try and retain instance information
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Grab parcelable information
        if (savedInstanceState == null || !savedInstanceState.containsKey("Artist")) {
            mSpotifyArtist = new ArrayList<SpotifyContent>();
        }
        else {
            mSpotifyArtist = savedInstanceState.getParcelableArrayList("Artist");
            populateArtistListView();
        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("Artist", mSpotifyArtist);
        super.onSaveInstanceState(outState);
    }


    /**
     * Name: onCreateView
     * Initialise the structures
     * Setup the listeners for edit control and listview
     */


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        listView = (ListView) rootView.findViewById(R.id.listViewArtists);

        // uncomment today - comment out in populate method
//        mSpotifyArtistAdapter =
//                new CustomListAdapter(this.getActivity(),
//                        mSpotifyArtist);
//        listView.setAdapter(mSpotifyArtistAdapter);

        // Add click behaviour for the title artist listview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, int position, long id) {
                // Grab the selected text from the adapterView (Artist)
//                final String strArtist = (String) adapterView.getItemAtPosition(position);
                SpotifyContent artistContent = (SpotifyContent) adapterView.getItemAtPosition(position);

//                // Store fragment data on the stack
//                getFragmentManager().beginTransaction()
//                        .addToBackStack(null)
//                        .commit();

                /**
                 * Code snippet: Explicit intent - ArtistActivity
                 * http://developer.android.com/guide/components/intents-filters.html#ExampleExplicit
                 *
                 * Description: Initiate an activity for the selected artist
                */
                Intent intent = new Intent(getActivity(), ArtistActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, artistContent._subTitle);      // Artist ID
                intent.putExtra(Intent.EXTRA_TITLE, artistContent._mainTitle);    // Artist Name
                startActivity(intent);

                // Indicate data refresh required
//                mSpotifyArtistAdapter.notifyDataSetChanged();
            }
        });


        /**
         * Interesting aside:
         * Cant use setOnKeyListener as this only works for hardware keys.
         * Instead use the solution from Android developers
         * Should have used a searchView :-(
         */

        final EditText searchArtist = (EditText) rootView.findViewById(R.id.editTextSearchArtist);
        searchArtist.setOnEditorActionListener(new EditText.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView Artist, int action, KeyEvent keyEvent) {
                switch (action) {
                    case EditorInfo.IME_ACTION_DONE:
                        if (searchArtist.getText().length() > 0) {
                            // Clear existing artist information
//                            mSpotifyArtist.clear();

                            // Execute the artist search
                            ArtistAsyncTask titleTask = new ArtistAsyncTask();
                            titleTask.execute(searchArtist.getText().toString());
                        }
                        break;

                    default:
                        break;
                }
                return false;
            }

        });

        return rootView;
    }

    /**
     * Name: populateArtistListView
     * Callback on postExecute - ArtistAsyncTask
     * Add the resultant data to the listView control
     * If no data returned - output a message
     * Otherwise - populate the ListView with the Artists found
     */

    public void populateArtistListView() {
        mSpotifyArtistAdapter =
                new CustomListAdapter(this.getActivity(),
                        mSpotifyArtist);


        // Error - Today
        // Get a reference to the ListView, and attach this adapter to it.
        listView.setAdapter(mSpotifyArtistAdapter);
        mSpotifyArtistAdapter.notifyDataSetChanged();
    }


    /**
     * Name: ArtistAsyncTask
     * Called from  searchArtist.setOnEditorActionListener
     * Access the Spotify Web API and store the information returned
     */
    private class ArtistAsyncTask extends AsyncTask<String, Void, ArtistsPager> {

        SpotifyApi api;
        SpotifyService spotifyService;

        @Override
        protected ArtistsPager doInBackground(String... params) {
            String searchArtist = params[0];
            boolean result = false;

            api = new SpotifyApi();
            spotifyService = api.getService();

            Log.i("Debug:", "Spotify: result");
            return (spotifyService.searchArtists(searchArtist));
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(ArtistsPager result) {

            Log.i("Debug:", "Download complete: result");

            artists = result.artists.items;
            mSpotifyArtist.clear();

            // Confirm results retrieved
            if (artists.size()==0) {
                Context context = getActivity();
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, "onPostExecute: No artists found", duration);
                toast.show();
            }
            else {
                for (Artist item: result.artists.items) {
                    SpotifyContent newArtist= null;

                    try {
                        newArtist = new SpotifyContent(
                                item.name,
                                item.id,
                                "",
                                (item.images.get(0).url),
                                TAG_ARTIST);
                    } catch (Exception e) {

                        // No image will cause an exception
                        if (item.images.size() == 0) {
                            // No image found - deal with this in the customer adapter
                            newArtist = new SpotifyContent(
                                    item.name,
                                    item.id,
                                    "",
                                    "",
                                    TAG_ARTIST);
                        }

                        Log.i (TAG_NAME, item.name + " Error");
                    }
                    // Store the artists found
                    mSpotifyArtist.add(newArtist);
                }

                // Update the listView
                populateArtistListView();
            }
        }
    }
}
