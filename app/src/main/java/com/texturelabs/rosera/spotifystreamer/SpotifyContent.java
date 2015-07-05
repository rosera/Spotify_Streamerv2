package com.texturelabs.rosera.spotifystreamer;

/**
 * Created by rosera on 05/07/15.
 */
public class SpotifyContent {
    String  _mainTitle;
    String  _subTitle;
    String  _imageURI;

    public SpotifyContent(String mainTitle, String subTitle, String imageURI) {
        this._mainTitle     = mainTitle;
        this._subTitle      = subTitle;
        this._imageURI      = imageURI;
    }
}
