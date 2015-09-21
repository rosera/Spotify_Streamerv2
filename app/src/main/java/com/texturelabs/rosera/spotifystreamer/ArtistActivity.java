package com.texturelabs.rosera.spotifystreamer;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

/*
 * Class: ArtistActivity
 * Extends: AppCompatActivity
 * Description: Main Spotify Stream activity
 *
 */

public class ArtistActivity extends AppCompatActivity {

    private final String TAG_NAME = ArtistActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);
        Intent intent = getIntent();



        // Call the fragment for Top 10 Tracks
        if (savedInstanceState == null) {
            Fragment fragment = new ArtistTopTenFragment();
            Bundle arguments = new Bundle();

            arguments.putString("ArtistID", intent.getStringExtra(Intent.EXTRA_TITLE));
            arguments.putString("Name", intent.getStringExtra(Intent.EXTRA_TEXT));

            fragment.setArguments(arguments);

            // Amend the Fragment title
            ActionBar actionBar = getActionBar();
            try {
                actionBar.setSubtitle(intent.getStringExtra(Intent.EXTRA_TEXT));

            } catch (NullPointerException e) {
                Log.i(TAG_NAME, "Exception:" + e.getMessage());
            }
            setTitle("Top 10 Tracks");

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }

    }


    /*********************************************
     * Name: onCreateOptionsMenuNo view found for id
     * @param menu
     * @return
     * Description:
     *
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_artist, menu);
        return true;
    }

    /***********************************************
     * Name: onOptionsItemSelected
     * @param item
     * @return
     * Description:
     *
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    /************************************************
//     * Name: onBackPressed
//     * Description:
//     *
//     */
//
//    @Override
//    public void onBackPressed() {
//        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
//            getSupportFragmentManager().popBackStack();
//        }
//        else {
//            super.onBackPressed();
//        }
//    }
}
