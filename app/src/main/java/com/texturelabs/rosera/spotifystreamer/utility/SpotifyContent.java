package com.texturelabs.rosera.spotifystreamer.utility;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Parcelable class structure
 * Created by rosera on 05/07/15.
 */
public class SpotifyContent implements Parcelable {
    private String  mSpotifyTitle;          // Artist Name
    private String  mSpotifySubTitle;       // Artist ID
    private String  mSpotifyImageURI;       // Artist URI
    private String  mSpotifyAlbumTitle;     // Artist Album
    private int     mFragmentTask;          // Task indicator
    

    public SpotifyContent(String mainTitle, String subTitle, String albumTitle, String imageURI, int Task_Type) {
        this.mSpotifyTitle = mainTitle;
        this.mSpotifySubTitle = subTitle;
        this.mSpotifyImageURI = imageURI;
        this.mSpotifyAlbumTitle = albumTitle;
        this.mFragmentTask = Task_Type;
    }

    private SpotifyContent(Parcel in) {
        this.mSpotifyTitle = in.readString();
        this.mSpotifySubTitle = in.readString();
        this.mSpotifyAlbumTitle = in.readString();
        this.mSpotifyImageURI = in.readString();
        this.mFragmentTask = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mSpotifyTitle);
        dest.writeString(this.mSpotifySubTitle);
        dest.writeString(this.mSpotifyImageURI);
        dest.writeString(this.mSpotifyAlbumTitle);
        dest.writeInt(this.mFragmentTask);
    }

    public final Parcelable.Creator<SpotifyContent> CREATOR = new Parcelable.Creator<SpotifyContent>() {
        @Override
        public SpotifyContent createFromParcel(Parcel parcel) {
            return new SpotifyContent(parcel);
        }

        @Override
        public SpotifyContent[] newArray(int i) {
            return new SpotifyContent[i];
        }
    };

    /*
     * Get Methods
     *
     */



    public String getArtistTitle() { return mSpotifyTitle; }

    public String getArtistSubTitle() {
        return mSpotifySubTitle;
    }

    public String getArtistURI() { return mSpotifyImageURI; }

    public String getArtistAlbum() { return mSpotifyAlbumTitle; }

    public int getFragmentTask() { return mFragmentTask; }
}
