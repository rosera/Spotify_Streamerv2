package com.texturelabs.rosera.spotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rosera on 05/07/15.
 */
public class SpotifyContent implements Parcelable {
    String  _mainTitle;
    String  _subTitle;
    String  _imageURI;
    String  _albumTitle;
    int     _Task;
    

    public SpotifyContent(String mainTitle, String subTitle, String albumTitle, String imageURI, int Task_Type) {
        this._mainTitle     = mainTitle;
        this._subTitle      = subTitle;
        this._imageURI      = imageURI;
        this._albumTitle    = albumTitle;
        this._Task          = Task_Type;
    }

    private SpotifyContent(Parcel in) {
        this._mainTitle = in.readString();
        this._subTitle = in.readString();
        this._albumTitle = in.readString();
        this._imageURI = in.readString();
        this._Task = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this._mainTitle);
        dest.writeString(this._subTitle);
        dest.writeString(this._imageURI);
        dest.writeString(this._albumTitle);
        dest.writeInt(this._Task);
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
