package com.example.ian.project1spotifystreamerwindows;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ian on 7/8/2015.
 */
public class AlbumSongData implements Parcelable {
    String mAlbumName;
    String mSongName;
    String mAlbumImageURL;
    String mArtistName;
    String mPreviewURL;


    public String getAlbumImageURL() {
        return mAlbumImageURL;
    }

    public void setAlbumImageURL(String albumImageURL) {
        mAlbumImageURL = albumImageURL;
    }

    public String getSongName() {
        return mSongName;
    }

    public void setSongName(String songName) {
        mSongName = songName;
    }

    public String getAlbumName() {
        return mAlbumName;
    }

    public void setAlbumName(String albumName) {
        mAlbumName = albumName;
    }



    public AlbumSongData (String albumName, String songName, String albumImageURL, String artistName, String previewURL){
        this.mAlbumName = albumName;
        this.mSongName = songName;
        this.mAlbumImageURL = albumImageURL;
        this.mArtistName = artistName;
        this.mPreviewURL = previewURL;

    }


    protected AlbumSongData(Parcel in) {
        mAlbumName = in.readString();
        mSongName = in.readString();
        mAlbumImageURL = in.readString();
        mArtistName = in.readString();
        mPreviewURL = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //Parcelable[] array = {mAlbumName,}
        dest.writeString(mAlbumName);
        dest.writeString(mSongName);
        dest.writeString(mAlbumImageURL);
        dest.writeString(mArtistName);
        dest.writeString(mPreviewURL);
        //dest.writeParcelableArray();
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<AlbumSongData> CREATOR = new Parcelable.Creator<AlbumSongData>() {
        @Override
        public AlbumSongData createFromParcel(Parcel in) {
            return new AlbumSongData(in);
        }

        @Override
        public AlbumSongData[] newArray(int size) {
            return new AlbumSongData[size];
        }
    };
}