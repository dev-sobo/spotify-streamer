package com.example.ian.project1spotifystreamerwindows;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ian on 7/8/2015.
 */
public class SpotifySongAdapter extends ArrayAdapter<AlbumSongData> {

    public SpotifySongAdapter(Activity context, List<AlbumSongData> albumSongDatas){
        super(context,0,albumSongDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AlbumSongData albumSongData = getItem(position);

      //  convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_song,parent,false);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_song,parent,false);
        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.albumImageViewId);
        Picasso.with(getContext()).load(albumSongData.getAlbumImageURL()).placeholder(R.drawable.greybox).error(R.drawable.greybox).into(imageView);


        TextView songName = (TextView) convertView.findViewById(R.id.albumSongId);
        songName.setText(albumSongData.getSongName());

        TextView albumName = (TextView) convertView.findViewById(R.id.albumNameId);
        albumName.setText(albumSongData.getAlbumName());

        return convertView;
    }
}
