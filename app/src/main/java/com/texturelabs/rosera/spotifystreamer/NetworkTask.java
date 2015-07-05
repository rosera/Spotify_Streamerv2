package com.texturelabs.rosera.spotifystreamer;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import android.util.Log;


import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.ArtistsPager;

/**
 * Created by rosera on 19/06/15.
 * http://www.vogella.com/tutorials/AndroidBackgroundProcessing/article.html#concurrency_asynchtask
 */

public class NetworkTask extends AsyncTask <String, Void, ArtistsPager> {

    @Override
    protected ArtistsPager doInBackground(String... params) {
        String searchArtist = params[0];

        SpotifyApi api = new SpotifyApi();
        SpotifyService spotifyService = api.getService();
//        ArtistsPager results = spotifyService.searchArtists(searchArtist);

        Log.d("Debug:", "Spotify: result");
        return spotifyService.searchArtists(searchArtist);
    }

    protected void onProgressUpdate(Integer... progress) {

    }

    protected void onPostExecute(Long result) {
        Log.d("Debug:", "Download complete: result");
    }
}
