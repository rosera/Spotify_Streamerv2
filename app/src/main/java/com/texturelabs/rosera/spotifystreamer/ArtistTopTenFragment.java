package com.texturelabs.rosera.spotifystreamer;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.ArtistsPager;


public class ArtistTopTenFragment extends Fragment {
//    private OnFragmentInteractionListener mListener;
    ArrayAdapter<String> mSpotifyTrackAdapter;

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment ArtistTopTenFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static ArtistTopTenFragment newInstance(String param1, String param2) {
//        ArtistTopTenFragment fragment = new ArtistTopTenFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    public ArtistTopTenFragment() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }


    /*
     *
     */

    public ArtistTopTenFragment() {
    }

    /*
     *
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Create some dummy data for the textView.
        String[] strTrackNames = {
                "Track 1 - Album 1",
                "Track 2 - Album 2",
                "Track 3 - Album 3",
                "Track 4 - Album 4",
                "Track 5 - Album 5",
                "Track 6 - Album 6",
                "Track 7 - Album 7",
                "Track 8 - Album 8"
        };

        // Add dummy images for the imageView
        int[] intTrackResource = {
                R.drawable.album_1,
                R.drawable.album_2,
                R.drawable.album_3,
                R.drawable.album_4,
                R.drawable.album_5,
                R.drawable.album_6,
                R.drawable.album_7,
                R.drawable.album_8
        };

        // Change the title - todo (add string resource)
        getActivity().setTitle("Top 10 Tracks");

        SpotifyApi api = new SpotifyApi();
        SpotifyService spotify = api.getService();
        ArtistsPager results = spotify.searchArtists("coldplay");

//        // Create a temporary list for the artist information
//        List<String> trackNameList = new ArrayList<String>(Arrays.asList(strTrackNames));
//        List<Integer> trackImageResource = new ArrayList<Integer>(Arrays.asList(intTrackResource));

        // Implement a customer adapter to display imageView & TextView with a listView.
        mSpotifyTrackAdapter =
                new CustomListAdapter(
                        getActivity(),
                        strTrackNames,
                        intTrackResource,
                        R.layout.list_item_top_ten,
                        R.id.textViewTopTenArtist,
                        R.id.imageViewTopTenArtist
                );
        // inflate the track list view for top ten
        View rootView = inflater.inflate(R.layout.fragment_artist_top_ten , container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listViewArtistTopTenTracks);
        listView.setAdapter(mSpotifyTrackAdapter);


        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_artist_top_ten, container, false);
        return rootView;
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p/>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        public void onFragmentInteraction(Uri uri);
//    }

}