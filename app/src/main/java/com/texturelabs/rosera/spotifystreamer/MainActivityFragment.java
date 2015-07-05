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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.app.ListActivity;
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
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    ArrayAdapter<String> mSpotifyArtistAdapter;
    int enteredTextLength = 0;
    SpotifyApi api;
    SpotifyService spotify;
    ArtistsPager artistsPager;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // List method start ******************************************//

        // Create some dummy data for the textView.
        String[] strArtistNames = {
                "Coldplay",
                "Coldplay & Lele",
                "Coldplay & Rhianna",
                "Various Artists - Coldplay",
                "Coldplay & Xtc",
                "Coldplay & me",
                "Coldplay & Kate Bush",
                "Coldplay & Florence & the machine "
        };

        // Add dummy images for the imageView
        int[] intArtistResource = {
                R.drawable.album_1,
                R.drawable.album_2,
                R.drawable.album_3,
                R.drawable.album_4,
                R.drawable.album_5,
                R.drawable.album_6,
                R.drawable.album_7,
                R.drawable.album_8
        };


//        // Initiate the spotify API
//        api = new SpotifyApi();
//        spotify = api.getService();
////        ArtistsPager results = spotify.searchArtists("coldplay");
//
//        // Call asynctask
////        new NetworkTask().execute("coldplay","","");

        // Implement a customer adapter to display imageView & TextView with a listView.
        mSpotifyArtistAdapter =
                new CustomListAdapter(
                        getActivity(),
                        strArtistNames,
                        intArtistResource,
                        R.layout.list_item_spotify,
                        R.id.textViewArtist,
                        R.id.imageViewArtist
                );

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listViewArtists);
        listView.setAdapter(mSpotifyArtistAdapter);

        // Add click behaviour for the title artist listview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, int position, long id) {

                // Grab the selected text from the adapterView (Artist)
                final String strArtist = (String) adapterView.getItemAtPosition(position);
//                Context context = getActivity();
//                int duration = Toast.LENGTH_SHORT;

//                Toast toast = Toast.makeText(context, strArtist, duration);
//                toast.show();

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
//        final EditText searchArtist = (EditText) rootView.findViewById(R.id.editTextArtist);
//        searchArtist.addTextChangedListener(new TextWatcher() {
//
//            public void afterTextChanged(Editable s) {}
//
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                String enteredText = searchArtist.getText().toString().trim();
//                ArtistsPager results;
//
//                /*
//                 * Use the count variable to ensure you dont call the web api too often
//                 * If the count is the same - dont call
//                 * Otherwise make the api call
//                 *
//                 */
//
//                // Ensure there is search text or clear the existing list
//                if (enteredText.isEmpty()) {
//                    // Selected enter from the keyboard
//                    Context context = getActivity();
//                    int duration = Toast.LENGTH_SHORT;
//
//                    Toast toast = Toast.makeText(context, "onTextChanged:clear the list", duration);
//                    toast.show();
//                }
//                else if (enteredTextLength == enteredText.length()) {
//                    // Selected enter from the keyboard
//                    Context context = getActivity();
//                    int duration = Toast.LENGTH_SHORT;
//
//                    Toast toast = Toast.makeText(context, "onTextChanged:list same length", duration);
//                    toast.show();
//                }
//                else if (enteredText.length() > 2) {
//
////                    // Selected enter from the keyboard
////                    Context context = getActivity();
////                    int duration = Toast.LENGTH_SHORT;
////
////                    Toast toast = Toast.makeText(context, "onTextChanged:Execute Spotify Web API call", duration);
////                    toast.show();
//
//
////                    // Clear the listView
////                    mSpotifyArtistAdapter.clear();
//
//                    // Execute the artist search
//                    new ArtistAsyncTask().execute(enteredText, "", "");
////                    if (results != null)
////                        mSpotifyArtistAdapter.addAll(results.artists.items);
//                }
//
//                // Hold the prior length for comparison on the next iteration
//                enteredTextLength = enteredText.length();
//            }
//
//
//        });

        return rootView;
    }

    /*
     * Callback on postExecute - ArtistAsyncTask
     * Add the resultant data to the listView control
     * If no data returned - output a message
     * Otherwise - populate the ListView with the Artists found
     */

    public void populateArtistListView(ArtistsPager result) {

        Log.i("Debug:", "Download complete: result");
        // Selected enter from the keyboard
        Context context = getActivity();
        int duration = Toast.LENGTH_SHORT;

        artistsPager = result;

        if (result.equals(null) == false) {
            // Clear the listView
//        mSpotifyArtistAdapter.clear();

            // Iterate through the artist list
            for (Artist item: artistsPager.artists.items) {

            }

            Toast toast = Toast.makeText(context, "populateArtistListView:Add data", duration);
            toast.show();
        }
        else {
            Toast toast = Toast.makeText(context, "populateArtistListView:No data", duration);
            toast.show();
        }

    }

    private class ArtistAsyncTask extends AsyncTask<String, Void, ArtistsPager> {

        SpotifyApi api;
        SpotifyService spotifyService;
        ArtistsPager spotifyArtistResults;

        @Override
        protected ArtistsPager doInBackground(String... params) {
            String searchArtist = params[0];

            api = new SpotifyApi();
            spotifyService = api.getService();
//            spotifyArtistResults = spotifyService.searchArtists(searchArtist);

            Log.d("Debug:", "Spotify: result");
//            return null;
            return (spotifyService.searchArtists(searchArtist));
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(ArtistsPager result) {

            Log.d("Debug:", "Download complete: result");
            populateArtistListView(result);
        }
    }


}
