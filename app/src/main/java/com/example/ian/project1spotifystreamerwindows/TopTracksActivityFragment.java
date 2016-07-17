package com.example.ian.project1spotifystreamerwindows;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.RetrofitError;


public  class TopTracksActivityFragment extends Fragment {
    private final String LOG_TAG = TopTracksActivityFragment.class.getSimpleName();
    public SpotifySongAdapter spotifySongAdapter;
    public List<AlbumSongData> trackDataList = new ArrayList<>();

    public TopTracksActivityFragment() {
    }

    public interface CallbackTrack {
        void onTrackSelected(List<AlbumSongData> trackData, int position);
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_top_tracks, container, false);

        //spotifySongAdapter = new SpotifySongAdapter(getActivity(), new ArrayList<AlbumSongData>());
        spotifySongAdapter = new SpotifySongAdapter(getActivity(), trackDataList);
        ListView listView = (ListView) rootView.findViewById(R.id.topTracksId);
        listView.setAdapter(spotifySongAdapter);

        // treat all incoming data as a bundle, and get those arugments from a bundle
        // and put it into a string variable.
        String artistId;
        Bundle args = getArguments();
        if (args != null) {
            artistId = args.getString(Intent.EXTRA_TEXT);
            ArtistTopTracksTask artistTopTracksTask = new ArtistTopTracksTask();
            artistTopTracksTask.execute(artistId);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Data that we need from the albumsongdata that backs up the listview' adapter:
                // Artist Name, Album Name, Song Name, Image URL, song's Preview URL
                // TODO: pass in needed data in a bundle

                ((CallbackTrack) getActivity())
                        .onTrackSelected(trackDataList, position);
                /*if (MainActivity.mTwoChainz == true){
                    DialogFragment tracksFragment = new TrackPlayerFragment();
                    tracksFragment.show(getActivity().getFragmentManager(), "TRACK_PLAYER");

                } else {
                    Intent intent = new Intent(getActivity(), TrackPlayerActivity.class);
                    intent.putParcelableArrayListExtra("PARCELED_LIST", (ArrayList<? extends Parcelable>) trackDataList);
                    intent.putExtra("POSITION_INTEGER", spotifySongAdapter.getPosition(spotifySongAdapter.getItem(position)));
                    startActivity(intent);
                }*/
            }
        });
        return rootView;
    }



    public class ArtistTopTracksTask extends AsyncTask<String, Void, List<Track>> {
        private final String LOG_TAG = ArtistTopTracksTask.class.getSimpleName();

        //REMEMBER TO PASS IN THE ARTISTID STRING ABOVE
        @Override
        protected List<Track> doInBackground(String... strings) {
            String artistId = strings[0];
            List<Track> listOfTracks = Collections.emptyList();

            SpotifyApi spotifyApi = new SpotifyApi();
            SpotifyService spotifyService = spotifyApi.getService();
            Map<String, Object> options = new HashMap<>();
            options.put(SpotifyService.COUNTRY, Locale.getDefault().getCountry());
            try {
                Tracks tracks = spotifyService.getArtistTopTrack(artistId, options);
                listOfTracks = spotifyService.getArtistTopTrack(artistId, options).tracks;
                Log.i(LOG_TAG, "ARTIST TOP TRACKS: " + tracks);
                Log.i(LOG_TAG, "TOP TRACKS LIST: " + listOfTracks );
            } catch (RetrofitError error) {
                if (error.getKind() == RetrofitError.Kind.NETWORK) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "Network error. Check your connection.", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
            return listOfTracks;
        }

        @Override
        protected void onPostExecute(List<Track> listOfTracks) {
            //super.onPostExecute(tracks);
            if (listOfTracks.isEmpty()) {
                Toast.makeText(getActivity(), "No tracks for this artist.", Toast.LENGTH_SHORT).show();
            }
            searchTracks(listOfTracks);
        }

        protected void searchTracks(List<Track> listOfTracks) {
           // List<AlbumSongData> albumSongDataList = new ArrayList<>();
            String albumImageURL;
            spotifySongAdapter.clear();
            for (int i = 0; i < listOfTracks.size(); i++) {
                Track track = listOfTracks.get(i);
                Log.i(LOG_TAG, "ARRAY POSITION: " + i + "TRACK NAME: " + track.name);


                albumImageURL = getAlbumImageURL(track.album.images);
                AlbumSongData albumSongData =
                        new AlbumSongData(track.album.name, track.name, albumImageURL, track.artists.get(0).name, track.preview_url);
                //trackDataList.add(albumSongData);


                spotifySongAdapter.add(albumSongData);
                spotifySongAdapter.notifyDataSetChanged();
            }

        }

        /**
         * here we will iterate through the passed in list of artist images, making sure both that there IS an image to extract within the list,
         * as well as picking the approiate size of the image through it's pixels, namely it's length.
         */
        protected String getAlbumImageURL(List<Image> albumImageList) {
            String albumImageURL = "http://i.imgur.com/UB7jqDI.png";
            if (albumImageList.isEmpty()) {
                return albumImageURL;
            }
            for (int i = 0; i < albumImageList.size(); i++) {
                Image albumImage = albumImageList.get(i);

                // albumImageUrl = albumImage.url
                if (albumImage.width >= 300) {
                    albumImageURL = albumImage.url;
                    Log.v(LOG_TAG, "POSITION IN IMAGE ARRAY: " + i + "IMAGE URL: " + albumImageURL);
                }
                // albumImageUrl = albumImage.url
            }

            return albumImageURL;
        }
    }
}