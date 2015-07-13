package com.texturelabs.rosera.spotifystreamer;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;


public class ArtistActivity extends AppCompatActivity {
    private static final String TAG_NAME = ArtistActivity.class.getSimpleName();
    private ArtistTopTenFragment _fragmentTitle;
    private final  String TAG_FRAGMENT = "TitleFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);

        // Get the information passed
        Bundle extras = getIntent().getExtras();

        // Check the extras have been populated
        if (extras != null) {
            String spotifyID = extras.getString(Intent.EXTRA_TEXT);         // Artist ID
            String spotifyArtist = extras.getString(Intent.EXTRA_TITLE);    // Artist Name

            // Call the fragment for Top 10 Tracks
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, new ArtistTopTenFragment(spotifyID, spotifyArtist))
//                        .addToBackStack(null)
                        .commit();

//                FragmentManager fragmentManager = getSupportFragmentManager();
//                _fragmentTitle = (ArtistTopTenFragment)fragmentManager.findFragmentByTag(TAG_FRAGMENT);
//
//                if (_fragmentTitle == null) {
//                    _fragmentTitle = new ArtistTopTenFragment(spotifyID, spotifyArtist);
//                    fragmentManager.beginTransaction()
////                            .add(_fragmentTitle, TAG_FRAGMENT)
//                            .addToBackStack(TAG_FRAGMENT)
//                            .commit();
//                }
            }

            // Amend the Fragment title
            android.support.v7.app.ActionBar actionBar = getSupportActionBar();
            try {
                actionBar.setSubtitle(spotifyArtist);
            }
            catch (NullPointerException e){
                Log.i(TAG_NAME, "Exception:" + e.getMessage());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_artist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // *********************************** Not working!!!

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                this.finish();
            }
            else
                getSupportFragmentManager().popBackStack();
        }
        return false;
    }
    // ********************************** Not working!!!

//
//
//    @Override
//    public void onBackPressed() {
//        // Catch back action and pops from backstack
//        // (if you called previously to addToBackStack() in your transaction)
//        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
////            getSupportFragmentManager().popBackStack();
//            this.finish();
//        }
//        // Default action on back pressed
//        else
//            getSupportFragmentManager().popBackStack();
//    }
}
