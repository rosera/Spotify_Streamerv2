package com.texturelabs.rosera.spotifystreamer;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.app.ListActivity;
import android.widget.TextView;
import android.widget.Toast;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Pager;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private static final String TAG_NAME = MainActivityFragment.class.getSimpleName();

    ArrayAdapter<SpotifyContent> mSpotifyArtistAdapter;
    int enteredTextLength = 0;
    SpotifyApi api;
    SpotifyService spotify;
    ArtistsPager artistsPager;
    ArtistsPager spotifyArtistResults;
    SpotifyContent[]  spotifyContent;
    ArrayList<SpotifyContent> mSpotifyArtist;
    ListView listView;
    List<Artist> artists;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // List method start ******************************************//

//
//        // Create some dummy data for the textView.
//        String[] strArtistNames = {
//                "Coldplay",
//                "Coldplay & Lele",
//                "Coldplay & Rhianna",
//                "Various Artists - Coldplay",
//                "Coldplay & Xtc",
//                "Coldplay & me",
//                "Coldplay & Kate Bush",
//                "Coldplay & Florence & the machine "
//        };
//
//        // Add dummy images for the imageView
//        int[] intArtistResource = {
//                R.drawable.album_1,
//                R.drawable.album_2,
//                R.drawable.album_3,
//                R.drawable.album_4,
//                R.drawable.album_5,
//                R.drawable.album_6,
//                R.drawable.album_7,
//                R.drawable.album_8
//        };
//

//        // Initiate the spotify API
//        api = new SpotifyApi();
//        spotify = api.getService();
////        ArtistsPager results = spotify.searchArtists("coldplay");
//
//        // Call asynctask
////        new NetworkTask().execute("coldplay","","");

        // Initialise memory for the Artist Array List
        mSpotifyArtist = new ArrayList<SpotifyContent>();

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);


        if (spotifyContent != null) {
            // Implement a customer adapter to display imageView & TextView with a listView.
            mSpotifyArtistAdapter =
                    new CustomListAdapter(
                            getActivity(),
                            mSpotifyArtist);

        }

        // Get a reference to the ListView, and attach this adapter to it.
        listView = (ListView) rootView.findViewById(R.id.listViewArtists);

        // Add click behaviour for the title artist listview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, int position, long id) {

                // Grab the selected text from the adapterView (Artist)
                final String strArtist = (String) adapterView.getItemAtPosition(position);

                /*
                 * Code snippet: Explicit intent - ArtistActivity
                 * http://developer.android.com/guide/components/intents-filters.html#ExampleExplicit
                 *
                 * Description: Initiate an activity for the selected artist
                */
                Intent intent = new Intent(getActivity(), ArtistActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, strArtist);
                startActivity(intent);
            }
        });

        // List method end ******************************************//



        /*
         * Interesting aside:
         * Cant use setOnKeyListener as this only works for hardware keys.
         * Instead use the solution from Android developers
         * Should have used a searchView :-(
         *
         */

        final EditText searchArtist = (EditText) rootView.findViewById(R.id.editTextSearchArtist);
        searchArtist.setOnEditorActionListener(new EditText.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView Artist, int action, KeyEvent keyEvent) {
                switch (action) {
                    case EditorInfo.IME_ACTION_DONE:
                        // Selected enter from the keyboard
                        Context context = getActivity();
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, "onTextChanged:Execute Spotify Web API call", duration);
                        toast.show();
//
//
////                    // Clear the listView
                        if (mSpotifyArtistAdapter !=null)
                            mSpotifyArtistAdapter.clear();

                        // Execute the artist search
                        new ArtistAsyncTask().execute(searchArtist.getText().toString());

                        //
//                        populateArtistListView();
                        break;

                    default:
                        break;
                }
                return false;
            }

        });

        return rootView;
    }

    /*
     * Callback on postExecute - ArtistAsyncTask
     * Add the resultant data to the listView control
     * If no data returned - output a message
     * Otherwise - populate the ListView with the Artists found
     */

    public void populateArtistListView() {


        // Null pointer?!!! on spotifyArtistResults
//        List<Artist> artists = spotifyArtistResults.artists.items;


//        for (int index = 0; index < mSpotifyArtist.size(); index++) {
//            SpotifyContent artist = mSpotifyArtist.get(index);
//            spotifyContent[index] = new SpotifyContent(artist.name, "", R.drawable.album_1);
//        }

        mSpotifyArtistAdapter =
                new CustomListAdapter(getActivity(),
                        mSpotifyArtist);

        // Get a reference to the ListView, and attach this adapter to it.
//        ListView listView = (ListView) rootView.findViewById(R.id.listViewArtists);
        listView.setAdapter(mSpotifyArtistAdapter);
        mSpotifyArtistAdapter.notifyDataSetChanged();

//        Log.i("Debug:", "Download complete: result");
//        // Selected enter from the keyboard
//        Context context = getActivity();
//        int duration = Toast.LENGTH_SHORT;
//
//        artists                for (int index = 0; index < artists.size(); index++) {
//                    Artist artist = artists.get(index);
//                    spotifyContent[index] = new SpotifyContent(artist.name, "", R.drawable.album_1);
//                }

//
//        if (result.equals(null) == false) {
//            // Clear the listView
////        mSpotifyArtistAdapter.clear();
//
//            // Iterate through the artist list
////            for (Artist item: artistsPager.artists.items) {
//
////            }
//
//            Toast toast = Toast.makeText(context, "populateArtistListView:Add data", duration);
//            toast.show();
//        }
//        else {
//            Toast toast = Toast.makeText(context, "populateArtistListView:No data", duration);
//            toast.show();
//        }

    }

    private class ArtistAsyncTask extends AsyncTask<String, Void, ArtistsPager> {

        SpotifyApi api;
        SpotifyService spotifyService;
//        ArtistsPager spotifyArtistResults;



        @Override
        protected ArtistsPager doInBackground(String... params) {
            String searchArtist = params[0];
            boolean result = false;

            api = new SpotifyApi();
            spotifyService = api.getService();
//            spotifyArtistResults = spotifyService.searchArtists(searchArtist);

//            if (spotifyArtistResults !=null)
//                result = true;

            Log.i("Debug:", "Spotify: result");
            return (spotifyService.searchArtists(searchArtist));
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(ArtistsPager result) {

            Log.i("Debug:", "Download complete: result");
//            List<Artist> artists = result.artists.items;

            artists = result.artists.items;

            if (artists.size()==0) {
                Context context = getActivity();
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, "onPostExecute: No artists found", duration);
                toast.show();
            }else {

                for (Artist item: result.artists.items) {
                    SpotifyContent newArtist= null;

                    try {
                        newArtist = new SpotifyContent(item.name, item.id, (item.images.get(0).url));
//                        newArtist = new SpotifyContent(item.name, item.id, R.drawable.album_1);

                    } catch (Exception e) {

                        if (item.images.size() == 0) {
                            // No image found - deal with this in the customer adapter
                            newArtist = new SpotifyContent(item.name, item.id, "");
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
