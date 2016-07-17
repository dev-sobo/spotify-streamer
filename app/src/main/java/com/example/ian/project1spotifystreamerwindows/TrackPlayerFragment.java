package com.example.ian.project1spotifystreamerwindows;

import android.app.DialogFragment;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class TrackPlayerFragment extends DialogFragment {
    private static final String LOG_TAG = TrackPlayerFragment.class.getSimpleName();

    public TrackPlayerFragment() {
    }

    int position;
    List<AlbumSongData> trackDataList = new ArrayList<>();
    String artistName;
    String albumName;
    String songName;
    String imageURL;
    String previewURL;


    TextView artistText;
    TextView albumText;
    TextView songText;
    TextView currentPositionTextView;
    ImageView imageURLView;
    ImageButton forwardButton;
    ImageButton backwardButton;
    ImageButton playButton;
    SeekBar seekBar;

    Handler handler = new Handler();
    MediaPlayer mediaPlayer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_track_player, container, false);


        artistText = (TextView) rootView.findViewById(R.id.artistNamePlayerId);
        albumText = (TextView) rootView.findViewById(R.id.albumNamePlayerId);
        songText = (TextView) rootView.findViewById(R.id.songNamePlayerId);
        imageURLView = (ImageView) rootView.findViewById(R.id.albumImagePlayerId);

        playButton = (ImageButton) rootView.findViewById(R.id.playButtonId);
        forwardButton = (ImageButton) rootView.findViewById(R.id.forwardButtonId);
        backwardButton = (ImageButton) rootView.findViewById(R.id.backwardButtonId);
        seekBar = (SeekBar) rootView.findViewById(R.id.seekBarPlayerId);
        currentPositionTextView = (TextView) rootView.findViewById(R.id.currentPositionTextViewId);



        // Temporary data exchange.
        // TODO: Replace intent getting with Bundles.
        // TODO: Replace all these getStringExtras with getParcelableExtra, and getIntExtra for the position.
        // TODO: Using the position, get the desired track that was tapped on. Get all required information from that track to play music, and put that information into the member variables.

        // TODO: Create new Bundle object, and getArguments. Assign trackDataList and position from the bundle's arguments
        Bundle args = getArguments();
        if (args != null) {
            trackDataList = args.getParcelableArrayList("PARCELED_LIST");
            position = args.getInt("POSITION_INTEGER", 0);
            updateViews(position, trackDataList);
        }


        // TODO: THESE ARE TEMPORARY. REFACTOR
//        Intent intent = getActivity().getIntent();
//        trackDataList = intent.getParcelableArrayListExtra("PARCELED_LIST");
//        position = intent.getIntExtra("POSITION_INTEGER", 0);
//        updateViews(position, trackDataList);

       // Toast.makeText(getActivity(), (artistName + albumName + songName + imageURL + previewURL + trackDataList), Toast.LENGTH_LONG).show();
//        Log.d(LOG_TAG, "PASSED IN DATA FROM TOPTRACKS FRAGMENT: " + trackDataList.get(position) + " " + trackDataList.get(position).mSongName);
// + artistName + albumName + songName + imageURL + previewURL


        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //stuff is already playing and showing a pause button. check if its already playing, and pause it if its clicked
                if (mediaPlayer.isPlaying() == true) {
                    mediaPlayer.pause();
                    playButton.setImageResource(android.R.drawable.ic_media_play);
                } else {
                    mediaPlayer.start();
                    playButton.setImageResource(android.R.drawable.ic_media_pause);
                }

            }
        });

        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position <= 8) {
                    mediaPlayer.release();
                    position = position + 1;
                    updateViews(position, trackDataList);
                }
            }
        });

        backwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position >= 1) {
                    mediaPlayer.release();
                    position = position - 1;
                    updateViews(position, trackDataList);
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress * 1000);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mediaPlayer.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.start();

            }
        });


        return rootView;
    }

    public void updateViews(int position, List<AlbumSongData> trackDataList) {
        // Update all member variables with data from the passed in list of track data,
        // with the correct track given the passed in position
        if (position <= 9 && position >= 0) {
            artistName = trackDataList.get(position).mArtistName;
            albumName = trackDataList.get(position).mAlbumName;
            songName = trackDataList.get(position).mSongName;
            imageURL = trackDataList.get(position).mAlbumImageURL;
            previewURL = trackDataList.get(position).mPreviewURL;
            // Update all relevant text & images
            artistText.setText(artistName);
            albumText.setText(albumName);
            songText.setText(songName);
            Picasso.with(getActivity()).load(imageURL).placeholder(R.drawable.greybox).error(R.drawable.greybox).into(imageURLView);
            // Finally, call the method to play the music
            playMedia(previewURL);
        } else {
            // TODO: Handle position out of bounds.
            Toast.makeText(getActivity(), "No more songs", Toast.LENGTH_SHORT).show();
        }
    }

    public void playMedia(String previewURL) {
//        mediaPlayer.release();
        mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource(previewURL);
            mediaPlayer.prepareAsync();
            //mediaPlayer.setLooping(true);
        } catch (IOException exception) {
            Toast.makeText(getActivity(), "IOException: " + exception, Toast.LENGTH_SHORT).show();
            exception.printStackTrace();
        } catch (IllegalArgumentException error) {
            Toast.makeText(getActivity(), "IllegalArgumentException: " + error, Toast.LENGTH_SHORT).show();
            error.printStackTrace();
        }
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                playButton.setImageResource(android.R.drawable.ic_media_pause);
                mediaPlayer.start();

            }
        });

        run.run();
    }
    Runnable run = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null) {
                seekBar.setMax(mediaPlayer.getDuration() / 1000);
                //seekBar.setMax(mediaPlayer.getDuration());
                ///1000) - 1
                int currentPosition = mediaPlayer.getCurrentPosition() / 1000;
                currentPosition = currentPosition + 1;
                seekBar.setProgress(currentPosition);
                currentPositionTextView.setText((":" + currentPosition) + "");

            }
            handler.postDelayed(this, 100);
        }
    };



    @Override
    public void onPause() {
        super.onPause();
        if (mediaPlayer != null) mediaPlayer.pause();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mediaPlayer!= null) mediaPlayer.pause();
    }
}

    /*boolean PlayMedia(String soundURL) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(soundURL);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException exception) {
            Toast.makeText(getActivity(), "IOException: " + exception, Toast.LENGTH_SHORT).show();
            exception.printStackTrace();
        } catch (IllegalArgumentException error) {
            Toast.makeText(getActivity(), "IllegalArgumentException: " + error, Toast.LENGTH_SHORT).show();
            error.printStackTrace();
        }

        return mediaPlayer.isPlaying();
    }*/



