package com.texturelabs.rosera.spotifystreamer;

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
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import retrofit.RetrofitError;


/**
     * Name: MainActivityFragment
     * Task 1 - UI to search for an Artist
     */

public class MainActivityFragment extends Fragment {

    // Add global tag for debug
    private static final String             TAG_NAME = MainActivityFragment.class.getSimpleName();
    private static final int                TAG_ARTIST = 1;

    private ArrayAdapter<SpotifyContent>        mSpotifyArtistAdapter;
    private static ArrayList<SpotifyContent>    mSpotifyArtist;
    //private static String                       mArtist;
    private ListView                            mListViewArtist;
    private List<Artist>                        mListSpotifyArtist;
    private boolean                             mParcelable = false;

    /**
     * Name: MainActivityFragment
     * Initialise  memory
     */

    public MainActivityFragment() {

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
            if (mSpotifyArtist != null)
                mParcelable = true;
        }
        else {
            mSpotifyArtist = savedInstanceState.getParcelableArrayList("Artist");
            mParcelable = true;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("Artist", mSpotifyArtist);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mParcelable)
            populateArtistListView();
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
        mListViewArtist = (ListView) rootView.findViewById(R.id.listViewArtists);

        if (!mParcelable)
            mSpotifyArtist = new ArrayList<>();

        mSpotifyArtistAdapter = new CustomListAdapter(getActivity(), mSpotifyArtist);
        mListViewArtist.setAdapter(mSpotifyArtistAdapter);

        // Add click behaviour for the title artist listview
        mListViewArtist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, int position, long id) {
                // Grab the selected text from the adapterView (Artist)
                SpotifyContent artistContent = (SpotifyContent) adapterView.getItemAtPosition(position);

                /**
                 * Code snippet: Explicit intent - ArtistActivity
                 * http://developer.android.com/guide/components/intents-filters.html#ExampleExplicit
                 *
                 * Description: Initiate an activity for the selected artist
                 */
                Intent intent = new Intent(getActivity(), ArtistActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, artistContent.mSpotifySubTitle)      // Artist ID
                        .putExtra(Intent.EXTRA_TITLE, artistContent.mSpotifyTitle);    // Artist Name
                startActivity(intent);
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

//// Review: use SearchView instead of EditText
//        final SearchView searchText = (SearchView) rootView.findViewById(R.id.searchText);
//
//        searchText.setIconifiedByDefault(false);
//        searchText.setQueryHint(getResources().getString(R.string.artist_search_hint));
//        searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                searchKeyword = searchText.getQuery().toString();
//                if (isNetworkAvailable()) {
//                    FetchArtistTask task = new FetchArtistTask();
//                    task.execute(searchText.getQuery().toString());
//                } else {
//                    Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
//                }
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
//// Review: use SearchView instead of EditText

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
//        mSpotifyArtistAdapter =
//                new CustomListAdapter(this.getActivity(),
//                        mSpotifyArtist);
//
//
//        // Error - Today
//        // Get a reference to the ListView, and attach this adapter to it.
//        listView.setAdapter(mSpotifyArtistAdapter);
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
        ArtistsPager spotifyContent = (ArtistsPager)null;

        @Override
        protected ArtistsPager doInBackground(String... params) {
            String searchArtist = params[0];
            boolean result = false;

            api = new SpotifyApi();
            spotifyService = api.getService();

// Review: check for exception on spotify call. Also covers lack of internet
            try {
               spotifyContent = spotifyService.searchArtists(searchArtist);
            }
            catch (RetrofitError ex) {
                Log.i(TAG_NAME, ex.toString());
            }

//            return (spotifyService.searchArtists(searchArtist));
// Review: check for exception on spotify call. Also covers lack of internet

            return (spotifyContent);
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(ArtistsPager result) {

            Log.i("Debug:", "Download complete: result");

            if (result == null) {
                Context context = getActivity();
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, "Please check your internet connection", duration);
                toast.show();
            }
            else {
                mListSpotifyArtist = result.artists.items;
                mSpotifyArtist.clear();

                // Confirm results retrieved
                if (mListSpotifyArtist.size()==0) {
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

                            Log.i (TAG_NAME, e.toString());
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
}
