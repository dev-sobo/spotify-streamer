package com.example.ian.project1spotifystreamerwindows;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;


public class MainActivity extends ActionBarActivity implements MainActivityFragment.Callback{

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String TOPTRACKSFRAGMENT_TAG = "TTFTAG";

    public static boolean mTwoChainz;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        if (findViewById(R.id.top_tracks_container) != null) {
            // This container will only be available when the large-screen [layout-sw600dp]
            // is present. if it isnt null, then the activity will be in two-pane mode
            mTwoChainz = true;
            // since we're in two-paine mode, show detail view in this activity by adding or replacing
            // the detail tracks fragment, within the container, with a fragment transaction
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.top_tracks_container, new TopTracksActivityFragment(), TOPTRACKSFRAGMENT_TAG)
                        .commit();
            }
        }
        else {
            mTwoChainz = false;
        }

    }

    @Override
    public void onItemSelected(String artistId) {
        // TODO: Put in the artistId EXTRA_TEXT data into a bundle if it is in TwoChainz mode, and commit a
        // TODO: FragmentTransaction, with the TopTracksFragment with the artistId as the bundle agruments
        // TODO: if it is NOT in TwoChainz mode then start TopTracksActivity with artistId as EXTRA_TEXT
        if (mTwoChainz) {
            Bundle args = new Bundle();
            //TODO: Call onItemSelected within mainactivityfragment and put in string artistId
            args.putString(Intent.EXTRA_TEXT, artistId);

            TopTracksActivityFragment fragment = new TopTracksActivityFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.top_tracks_container, fragment, TOPTRACKSFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, TopTracksActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT, artistId);
            startActivity(intent);
        }
    }
}
