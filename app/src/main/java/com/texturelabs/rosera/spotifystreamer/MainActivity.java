/*
 * Name: Spotify Streamer v2
 * Course: Android Developer Nanodegree
 * Author: Richard Rose
 * Cohort: June 2015
 */


package com.texturelabs.rosera.spotifystreamer;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

/*
 * Class: MainActivity
 * Extends: AppCompatActivity
 * Description: Main Spotify Stream activity
 *
 */
public class MainActivity extends AppCompatActivity {

//    private final  String TAG_FRAGMENT          = "ArtistFragment";
    private final  String ARTISTFRAGMENT_TAG    = "ArtistFragment";

    /*
     * Project 2 - Amend UI for dual pane
     * Confirm whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane = false;


    /*
     * Name: onCreate
     * @param savedInstanceState
     * Description: Inflate code ready to display on device
     * Comment: Added master detail flow to existing code (see layout-sw600dp)
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if id.container is found - this means two pane fragments (static and dynamic) are being displayed
        if (findViewById(R.id.dynamic_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;

            // TODO: Use a bundle to store mTwoPane state and pass through as argument

//            Bundle arguments = new Bundle();
//            arguments.putBoolean("TwoPane", mTwoPane);

            ArtistTopTenFragment fragment = new ArtistTopTenFragment();
//            fragment.setArguments(arguments);

            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
//            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.dynamic_container, fragment, ARTISTFRAGMENT_TAG)
                        .commit();
//            }
        }

    }

    /*
     * Name: onCreateOptionsMenu
     * @param menu - menu to be inflated
     * @return boolean - flag to indicate success
     * Description: Inflate menu to display on device
     * Comment: Added SearchView to replace the original EditView used in Task 1
     *
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Retrieve the SearchView and plug it into SearchManager
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        // Review: use SearchView instead of EditText
        searchView.setIconifiedByDefault(true);
        searchView.setQueryHint(getResources().getString(R.string.artist_search_hint));

// Udacity Review: use SearchView instead of EditText
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                /* Ensure the device is online prior to initiating a network task */
                if (isDeviceOnline()) {
                    String searchKeyword = searchView.getQuery().toString();

                    // Find the fragment instance
                    MainActivityFragment fragment = (MainActivityFragment)
                            getSupportFragmentManager().findFragmentById(R.id.fragment_container);

                    // Confirm the fragment exists
                    if (fragment != null) {
                        // Make a Async call to get new artist
                        fragment.fetchSpotifyContent(searchKeyword);

//                        if (findViewById(R.id.dynamic_container) != null) { // mTwoPane == true
////                        if (mTwoPane) {
//                            ArtistTopTenFragment fragmentTopTen = (ArtistTopTenFragment)
//                                    getSupportFragmentManager().findFragmentById(R.id.dynamic_container);
//
//                            // Remove existing artist top ten data
//                            if (fragmentTopTen != null)
//                                fragmentTopTen.removeTopTenTitles();
//                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
// Udacity Review: use SearchView instead of EditText

        return true;
    }


    /*
     * Name: onOptionsItemSelected
     * @param item - Indicate the selected menu item
     * @return boolean - Indicate the selected item
     * Description: Handler for menu item selection
     *
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        // Todo: Add country selection on the setting
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
     * Name: isDeviceOnline
     * @return boolean - flag to indicate network status
     * Description: Check on the device network status
     * Comment: Standard method on which to check the network availability
     *          Ensure required permissions have been added to Android.Manifest
     */

    public boolean isDeviceOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
