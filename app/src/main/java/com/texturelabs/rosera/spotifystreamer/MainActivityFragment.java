package com.texturelabs.rosera.spotifystreamer;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.texturelabs.rosera.spotifystreamer.utility.CustomListAdapter;
import com.texturelabs.rosera.spotifystreamer.utility.SpotifyContent;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import retrofit.RetrofitError;


/*
 * Class: MainActivityFragment
 * Extends: Fragment
 * Description: Main Spotify Stream activity
 *
 */

public class MainActivityFragment extends Fragment {

    // Add global tag for debug
//    private final String ARTISTFRAGMENT_TAG = "ArtistFragment";
    private final String TAG_NAME = MainActivityFragment.class.getSimpleName();
    private final int TAG_ARTIST = 1;

    private ArrayAdapter<SpotifyContent> mArtistAdapter;
    private ArrayList<SpotifyContent> mSpotifyArtist = new ArrayList<>();

    private boolean mParcelable = false;

    /*
     * Name: MainActivityFragment
     * Comment: Do not do any memory allocation in a fragment constructor
     */

    public MainActivityFragment() {
    }

    /*
     * Name: onCreate
     * @param savedInstanceState
     * Comment: Added parcelable functionality
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Grab parcelable information
        if (savedInstanceState == null || !savedInstanceState.containsKey("Artist")) {
            if (mSpotifyArtist != null)
                mParcelable = true;
        } else {
            mSpotifyArtist = savedInstanceState.getParcelableArrayList("Artist");
            mParcelable = true;
        }
    }

    /**************************************************************************
     * Name: onSaveInstanceState
     *
     * @param outState Comment: Save information into the paracleable object
     */

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("Artist", mSpotifyArtist);
    }


    /**************************************************************************
     * Name: onActivityCreated
     *
     * @param savedInstanceState Comment:
     */

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        // TODO: Test parcelable Fri 18th Sept
        if (mParcelable) {
            populateArtistListView();
//            mArtistAdapter.notifyDataSetChanged();
        }
    }


    /**************************************************************************
     * Name: onCreateView
     *
     * @param inflater           - pipeline of data
     * @param container          - existing viewgroup        Bundle args = getArguments();
     * @param savedInstanceState - saved instance state information
     * @return View - the created view
     * Initialise the structures
     * Setup the listeners for edit control and listview
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ListView listViewArtist;
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

//        if (!mParcelable) {
//            mSpotifyArtist = new ArrayList<>();
//        }

        mArtistAdapter = new CustomListAdapter(getActivity(), mSpotifyArtist);

        // Get a reference to the ListView, and attach this adapter to it.
        listViewArtist = (ListView) rootView.findViewById(R.id.listViewArtists);
        listViewArtist.setAdapter(mArtistAdapter);

        // Add click behaviour for the title artist listview
        listViewArtist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, int position, long id) {

                Boolean mTwoPane = false;

                if (getActivity().findViewById(R.id.dynamic_container) != null)
                    mTwoPane = true;

                // Grab the selected text from the adapterView (Artist)
                SpotifyContent artistContent = (SpotifyContent) adapterView.getItemAtPosition(position);

                // TODO: Amend code to use Bundle
                // TODO: Single fragment code versus dual pane implementation

                if (mTwoPane) {
                    Bundle arguments = new Bundle();

                    arguments.putString("ArtistID", artistContent.getArtistSubTitle());
                    arguments.putString("Name", artistContent.getArtistTitle());

                    ArtistTopTenFragment fragment = new ArtistTopTenFragment();
                    fragment.setArguments(arguments);

                    // TODO: Oh oh
                    getFragmentManager().beginTransaction()
                            .replace(R.id.dynamic_container, fragment, TAG_NAME)
                            .commit();
                } else {

                    /**
                     * Code snippet: Explicit intent - ArtistActivity
                     * http://developer.android.com/guide/components/intents-filters.html#ExampleExplicit
                     *
                     * Description: Initiate an activity for the selected artist
                     */
                    Intent intent = new Intent(getActivity(), ArtistActivity.class)
                            .putExtra(Intent.EXTRA_TITLE, artistContent.getArtistSubTitle()) // Artist ID
                            .putExtra(Intent.EXTRA_TEXT, artistContent.getArtistTitle());   // Artist Name

                    startActivity(intent);
                }
            }
        });



        return rootView;
    }


    /**************************************************************************
     * Name: fetchSpotifyContent
     *
     * @param artist - name of artist to search
     *               Description: Access the Spotify API
     */

    public void fetchSpotifyContent(String artist) {
        FetchArtistTask task = new FetchArtistTask();
        task.execute(artist);
    }

    /**
     * Name: populateArtistListView
     * Callback on postExecute - ArtistAsyncTask
     * Add the resultant data to the listView control
     * If no data returned - output a message
     * Otherwise - populate the ListView with the Artists found
     */
    //mSpotifyArtistAdapter.notifyDataSetChanged();
    public void populateArtistListView() {
        mArtistAdapter.notifyDataSetChanged();
    }





    /*
     * Name: ArtistAsyncTask
     * Called from  searchArtist.setOnEditorActionListener
     * Access the Spotify Web API and store the information returned
     */

    private class FetchArtistTask extends AsyncTask<String, Void, ArtistsPager> {

        SpotifyApi api;
        SpotifyService spotifyService;
        ArtistsPager spotifyContent = (ArtistsPager)null;

        @Override
        protected ArtistsPager doInBackground(String... params) {
            String searchArtist = params[0];
//            boolean result = false;

            api = new SpotifyApi();
            spotifyService = api.getService();

// Udacity Review: check for exception on spotify call. Also covers lack of internet
            try {
               spotifyContent = spotifyService.searchArtists(searchArtist);
            }
            catch (RetrofitError ex) {
                Log.i(TAG_NAME, ex.toString());
            }

// Udacity Review: check for exception on spotify call. Also covers lack of internet

            return (spotifyContent);
        }


        /*
         * Name: onProgressUpdate
         * @param progress
         * Description: N/A
         *
         */

        protected void onProgressUpdate(Integer... progress) {
        }

        /*
         * Name: onPostExecute
         * Description:
         *
         */

        protected void onPostExecute(ArtistsPager result) {
            List<Artist>                        mListSpotifyArtist;
            mListSpotifyArtist = result.artists.items;
            mSpotifyArtist.clear();

            // If not artists retrieved display a message to the user
            if (mListSpotifyArtist.size()==0) {
                    displayFragmentMessage("No artists information found");
            } else {
                setSpotifyArtistInformation(result);

                // Update the listView
                populateArtistListView();
//                mArtistAdapter.notifyDataSetChanged();
            }
        }
    }

    public void displayFragmentMessage(String msg) {
        Context context = getActivity();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, msg, duration);
        toast.show();
    }

    public void setSpotifyArtistInformation (ArtistsPager result) {

        for (Artist item: result.artists.items) {
            SpotifyContent newArtist= null;

            try {
                newArtist = new SpotifyContent(
                        item.name,
                        item.id,
                        item.id,
                        "",
                        (item.images.get(0).url),
                        TAG_ARTIST,
                        "");

            } catch (Exception e) {
                // No image will cause an exception
                if (item.images.size() == 0) {
                    // No image found - deal with this in the customer adapter
                    newArtist = new SpotifyContent(
                            item.name,
                            item.id,
                            item.id,
                            "",
                            "",
                            TAG_ARTIST,
                            "");
                }

                Log.i (TAG_NAME, e.toString());
            }
            // Store the artists found
            mSpotifyArtist.add(newArtist);
        }

        return ;
    }
}
