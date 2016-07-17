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
 * Created by chernyee on 7/2/2015.
 */
public class SpotifyDataAdapter extends ArrayAdapter<ArtistData> {

    public SpotifyDataAdapter(Activity context, List<ArtistData> artistData) {
        super(context,0,artistData);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ArtistData artistData = getItem(position);


        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_artist, parent, false);
        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.artistImageViewId);
        imageView.setImageResource(artistData.image);
        Picasso.with(getContext()).load(artistData.imageURL).placeholder(R.drawable.greybox).error(R.drawable.greybox).into(imageView);

        TextView nameTextView = (TextView) convertView.findViewById(R.id.songNameId);
        nameTextView.setText(artistData.name);

        TextView descriptTextView = (TextView) convertView.findViewById(R.id.songDescriptId);
        descriptTextView.setText(artistData.description);



        //return super.getView(position, convertView, parent);

        return convertView;
    }
}
