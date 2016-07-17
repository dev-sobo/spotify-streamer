package com.example.ian.project1spotifystreamerwindows;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Image;
import retrofit.RetrofitError;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName();
  //  protected String userString = " ";

    private SpotifyDataAdapter spotifyDataAdapter;

    public MainActivityFragment() {

    }

    public interface Callback {

        public void onItemSelected(String artistId);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);




       spotifyDataAdapter = new SpotifyDataAdapter(getActivity(), new ArrayList<ArtistData>());


        final ListView listView = (ListView) rootView.findViewById(R.id.artistListView);
        listView.setAdapter(spotifyDataAdapter);
       // spotifyDataAdapter.clear();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // Artist artist = (Artist) listView.getAdapter().getItem(position));
              //  String artistId = artist.id;
                // TODO: Call the interface on the MainActivity (getActivity()) when the item is clicked with String artistId
                String artistId = spotifyDataAdapter.getItem(position).id;
                ((Callback) getActivity())
                        .onItemSelected(artistId);
             /*   Intent intent = new Intent(getActivity(),TopTracksActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, artistId);
                Log.i(LOG_TAG, "ARTIST ID IN MAIN FRAGMENT: " + artistId);
                startActivity(intent);*/

            }
        });

        EditText editText = (EditText) rootView.findViewById(R.id.searchFieldId);
        //userString = editText.getText().toString();
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                   String userString = v.getText().toString();
                    if (userString.equals("")){
                        Toast.makeText(getActivity(),"Enter an artist's name...", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    if (userString.equals(null)){
                        return false;
                    }
                    SearchSpotifyTask spotifyTask = new SearchSpotifyTask();
                    spotifyTask.execute(userString);
                    return true;
                }
                return false;
            }
        });

        return rootView;
    }

    // Pass in a string here from the EditText and use it as the parameter for the SearchArtists
    public class SearchSpotifyTask extends AsyncTask<String, Void, List<Artist>>
    {

        private final String LOG_TAG = SearchSpotifyTask.class.getSimpleName();

        @Override
        protected void onPreExecute() {
         //   super.onPreExecute();
            ProgressBar progressBar = (ProgressBar) getActivity().findViewById(R.id.progressBarId);
            progressBar.setVisibility(View.VISIBLE);
        }


        @Override
        protected List<Artist> doInBackground(String... params) {
            List<Artist> listOfArtists = Collections.emptyList();
            SpotifyApi spotifyApi = new SpotifyApi();
            SpotifyService spotifyService = spotifyApi.getService();
            if (params.length == 0) {
                return null;
            }
            Log.v(LOG_TAG, "params: " + params[0]);
            if (params[0] == null) {
                return null;
            }
            try {
                ArtistsPager artistResults = spotifyService.searchArtists(params[0]);
                listOfArtists = artistResults.artists.items;
                String hrefTest = artistResults.artists.href;
                Log.v(LOG_TAG, "HREF TEST: " + hrefTest);
            } catch(RetrofitError error){
                if (error.getKind() == RetrofitError.Kind.NETWORK){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity().getApplicationContext(),"Network error. Check your connection.", Toast.LENGTH_LONG).show();
                        }
                    });
                } /*else if (error.getKind() == RetrofitError.Kind.HTTP){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity().getApplicationContext(), "Server error. Refine your search, or try again later.", Toast.LENGTH_LONG).show();
                        }
                    });
                }*/
            }

            return listOfArtists;

        }



        @Override
        protected void onPostExecute(List<Artist> artists) {
            // Crashing here because of differences in array sizes.
            // There are currently only 3 artistData objects in the artistDatas array created
            // A seperate crash is coming from the fact that the search result is of ambigious size:
            // it may return only 1 result for example, and if the loop is calling for something bigger, it will crash
            ProgressBar progressBar = (ProgressBar) getActivity().findViewById(R.id.progressBarId);
            progressBar.setVisibility(View.INVISIBLE);
            if (artists.isEmpty()){
                Toast.makeText(getActivity(), "No artists found.", Toast.LENGTH_SHORT).show();
            }
            searchArtists(artists);

        }
        // make brand new ArtistData objects, clear the Adapter, and add the new objects to the adapter.
        // finally, notify the adapter that data has changed.
        protected void searchArtists(List<Artist> artists) {
            List<ArtistData> artistDataList = new ArrayList<>();
            String artistImageURL;
            spotifyDataAdapter.clear();
            for (int i = 0; i < artists.size(); i++ ) {
                Artist artist = artists.get(i);
                Log.i(LOG_TAG, "ARRAY POSITION: " + i + "ARTIST NAME: " + artist.name);
//                Log.i(LOG_TAG, "POSITION IN LIST: " + i + "ARTIST IMAGE: " + artist.images.get(0).url);

                artistImageURL = getArtistImageURL(artist.images);

                ArtistData artistData =
                        new ArtistData(artist.name, artistImageURL, artist.id);
               artistDataList.add(artistData);

                //Log.i(LOG_TAG, "POSITION IN LIST: " + i + "OBJECT AT THAT POSITION: " + artistDataList.get(i).getName());
               // artistDatas[i].setName(artist.name);
                /*spotifyDataAdapter.clear();
                spotifyDataAdapter.add(artistData);
                spotifyDataAdapter.notifyDataSetChanged(); */

                spotifyDataAdapter.add(artistDataList.get(i));
                spotifyDataAdapter.notifyDataSetChanged();
            }
          //  spotifyDataAdapter.clear();
         //   spotifyDataAdapter.add(artistDataList.);
         //   spotifyDataAdapter.notifyDataSetChanged();
        }
        /**
        here we will iterate through the passed in list of artist images, making sure both that there IS an image to extract within the list,
        as well as picking the approiate size of the image through it's pixels, namely it's length.
         */
        protected String getArtistImageURL(List<Image> artistImageList) {
            String artistImageURL = "http://i.imgur.com/UB7jqDI.png";
            if (artistImageList.isEmpty()){
                //artistImageURL = "http://i.imgur.com/UB7jqDI.png";
                return artistImageURL;
                /*String greyBoxPic = "http://i.imgur.com/UB7jqDI.png";
                return greyBoxPic;*/
            }
            for (int i = 0; i < artistImageList.size(); i++) {

                Image artistImage = artistImageList.get(i);
                artistImageURL = artistImage.url;
                if (artistImage.width <= 300) {
                    artistImageURL = artistImage.url;
                    Log.v(LOG_TAG, "POSITION IN IMAGE ARRAY: " + i + " FINAL ARTIST IMAGE WIDTH: " + artistImage.width);
                    break;
                }
                // artistImageURL = artistImage.url;

            }
            return artistImageURL;
        }
    }

}
