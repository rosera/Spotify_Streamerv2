package com.texturelabs.rosera.spotifystreamer;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.app.ListActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    ArrayAdapter<String> mSpotifyArtistAdapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Create some dummy data for the ListView.  Here's a sample weekly forecast
        String[] strArtistNames = {
                "Artist 0",
                "Artist 1",
                "Artist 2",
                "Artist 3",
                "Artist 4",
                "Artist 5",
                "Artist 6",
                "Artist 7"
        };

        Integer[] intArtistResource = {
                R.drawable.ic_launcher,
                R.drawable.ic_launcher,
                R.drawable.ic_launcher,
                R.drawable.ic_launcher,
                R.drawable.ic_launcher,
                R.drawable.ic_launcher,
                R.drawable.ic_launcher,
                R.drawable.ic_launcher
        };

        // Create a temporary list for the artist information
        List<String> artistNameList = new ArrayList<String>(Arrays.asList(strArtistNames));
        List<Integer> artistImageResource = new ArrayList<Integer>(Arrays.asList(intArtistResource));

        // Now that we have some dummy forecast data, create an ArrayAdapter.
        // The ArrayAdapter will take data from a source (like our dummy forecast) and
        // use it to populate the ListView it's attached to.
//        mSpotifyArtistAdapter =
//                new ArrayAdapter<String>(
//                        getActivity(), // The current context (this activity)
//                        R.layout.list_item_spotify, // The name of the layout ID.
//                        R.id.textViewArtist, // The ID of the textview to populate.
//                        artistNameList);


        mSpotifyArtistAdapter =
                new CustomListAdapter(
                        getActivity(),
                        artistNameList,
                        artistImageResource);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listViewArtists);
        listView.setAdapter(mSpotifyArtistAdapter);
//        setListAdapter(mSpotifyArtistAdapter);
        return rootView;
    }
}
