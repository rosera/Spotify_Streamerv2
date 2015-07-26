package com.texturelabs.rosera.spotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Parcelable class structure
 * Created by rosera on 05/07/15.
 */
public class SpotifyContent implements Parcelable {
    String  mSpotifyTitle;
    String  mSpotifySubTitle;
    String  mSpotifyImageURI;
    String  mSpotifyAlbumTitle;
    int     mFragmentTask;
    

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
}
