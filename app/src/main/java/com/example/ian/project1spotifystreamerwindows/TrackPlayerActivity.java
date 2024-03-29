package com.example.ian.project1spotifystreamerwindows;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class TrackPlayerActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_player);

        if (savedInstanceState == null){
            Intent intent = getIntent();

            Bundle args = new Bundle();
            args.putParcelableArrayList("PARCELED_LIST", intent.getParcelableArrayListExtra("PARCELED_LIST"));
            args.putInt("POSITION_INTEGER", intent.getIntExtra("POSITION_INTEGER", 0));

            TrackPlayerFragment fragment = new TrackPlayerFragment();
            fragment.setArguments(args);

            getFragmentManager().beginTransaction()
                    .add(R.id.fragment_player, fragment)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_track_player, menu);
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


}
