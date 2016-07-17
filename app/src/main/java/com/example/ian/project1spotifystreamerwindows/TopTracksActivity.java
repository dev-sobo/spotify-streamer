package com.example.ian.project1spotifystreamerwindows;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class TopTracksActivity extends ActionBarActivity implements TopTracksActivityFragment.CallbackTrack {

    private static final String LOG_TAG = TopTracksActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_tracks);
        //  treat all incoming intent data as a Bundle Extra, put it into agruments, and commit a fragment transaction
        //  with the TopTracksActivityFragment, within the R.id.top_Tracks_container
        if (savedInstanceState == null) {
            // Create TopTracksFragment, add it to the activity using a fragment transaction
            // Also, get the received intent's data, put into a bundle args, and add it to the fragment's args
            Intent intent = new Intent();
            intent = getIntent();
           /* String intent_test = intent.getStringExtra(Intent.EXTRA_TEXT);
            Log.i(LOG_TAG, "INCOMING INTENT STRING EXTRA: " + intent_test);*/

            Bundle args = new Bundle();
            args.putString(Intent.EXTRA_TEXT, intent.getStringExtra(Intent.EXTRA_TEXT));

            TopTracksActivityFragment fragment = new TopTracksActivityFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.top_tracks_container, fragment)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_top_tracks, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTrackSelected(List<AlbumSongData> trackData, int position) {
        // TODO: This is where we send the arugments if it is in tablet mode or not.
        // TODO: If it's in tablet mode, then we send the arugments as a bundle, and show the dialogFragment
        // TODO: if it's in phone mode, then we send the arugments as an intent
        if (MainActivity.mTwoChainz == true) { // TABLET MODE
            Bundle args = new Bundle();
            args.putParcelableArrayList("PARCELED_LIST", (ArrayList<? extends Parcelable>) trackData);
            args.putInt("POSITION_NUMBER", position);

            DialogFragment trackPlayerFragment = new TrackPlayerFragment();
            trackPlayerFragment.setArguments(args);

            trackPlayerFragment.setShowsDialog(true);
            trackPlayerFragment.show(getFragmentManager(), "TRACK_PLAYER");
        } else {
            Intent intent = new Intent(this, TrackPlayerActivity.class);
            intent.putParcelableArrayListExtra("PARCELED_LIST", (ArrayList<? extends Parcelable>) trackData);
            intent.putExtra("POSITION_INTEGER", position);
            startActivity(intent);
        }
    }

}
