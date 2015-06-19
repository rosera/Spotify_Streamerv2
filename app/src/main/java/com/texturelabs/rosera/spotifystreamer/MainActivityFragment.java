package com.texturelabs.rosera.spotifystreamer;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.app.ListActivity;
import android.widget.TextView;
import android.widget.Toast;

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

//        // Add dummy images for the imageView
//        Integer[] intArtistResource = {
//                R.drawable.ic_launcher,
//                R.drawable.ic_launcher,
//                R.drawable.ic_launcher,
//                R.drawable.ic_launcher,
//                R.drawable.ic_launcher,
//                R.drawable.ic_launcher,
//                R.drawable.ic_launcher,
//                R.drawable.ic_launcher
//        };

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

//        // Initialise the resources to populate
//        Integer[] intLayoutResource = {
//                R.layout.fragment_main,
//                R.id.textViewArtist,
//                R.id.imageViewArtist
//        };

//        View rowView = inflater.inflate(R.layout.fragment_main, container, false);
//        TextView textViewArtist = (TextView) rowView.findViewById(R.id.textViewArtist);
//        ImageView imageViewArtist = (ImageView) rowView.findViewById(R.id.imageViewArtist);


        // Ok - not sure this step is necessary
        // Create a temporary list for the artist information
//        List<String> artistNameList = new ArrayList<String>(Arrays.asList(strArtistNames));
//        List<Integer> artistImageResource = new ArrayList<Integer>(Arrays.asList(intArtistResource));


        // Now that we have some dummy forecast data, create an ArrayAdapter.
        // The ArrayAdapter will take data from a source (like our dummy forecast) and
        // use it to ask_richard@hotmail.compopulate the ListView it's attached to.
//        mSpotifyArtistAdapter =
//                new ArrayAdapter<String>(
//                        getActivity(), // The current context (this activity)
//                        R.layout.list_item_spotify, // The name of the layout ID.
//                        R.id.textViewArtist, // The ID of the textview to populate.
//                        artistNameList);




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

        return rootView;
    }

}
