package com.texturelabs.rosera.spotifystreamer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rosera on 06/06/15.
 * reference: http://www.vogella.com/tutorials/AndroidListView/article.html#adapterown
 * 3. Custom adapter implementations
 */
public class CustomListAdapter extends ArrayAdapter<SpotifyContent> {

    private final Activity      _ObjectContext;
//    private final String[]      _strArtistName;
//    private final int[]         _intArtistImage;
//
//    private final int      _layoutView;
//    private final int      _imageView;
//    private final int      _textView;

    // First custom list adapter - whoa!
    // Pass the string and integer array to populate the listView
    public CustomListAdapter (Activity context,
                              ArrayList<SpotifyContent> spotifyContents) {
        // Pass information to the ArrayAdapter

        // Compiler: Call to super must be the first statement in constructor
        super(context, 0, spotifyContents) ;

//        // Initialise member variables
        _ObjectContext  = context;
//        _strArtistName  = strArtistName;
//        _intArtistImage = imageIdArtist;
//
//        // Grab the resource
//        //        TextView textViewArtist = (TextView) rowView.findViewById(R.id.textViewArtist);
////        ImageView imageViewArtist = (ImageView) rowView.findViewById(R.id.imageViewArtist);
//
//        _layoutView = iResourceLayout;
//        _textView   = iTextView;
//        _imageView  = iImageView;

    }

    /*
     * getView
     * Description: Inflates the layout passed and populates the image and textview
     */

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        SpotifyContent spotifyContent = getItem(position);


        LayoutInflater inflater = _ObjectContext.getLayoutInflater();

        // Inflate the RowView containing a ImageView and TextView(s)
        View rowView = inflater.inflate(R.layout.list_item_spotify, parent, false);
//        View rowView = inflater.inflate((int) _layoutView, parent, false);

        if (spotifyContent != null) {

            TextView textViewArtist = (TextView) rowView.findViewById(R.id.textViewArtist);
            ImageView imageViewArtist = (ImageView) rowView.findViewById(R.id.imageViewArtist);

//        TextView textViewArtist = (TextView) rowView.findViewById((int)_textView);
//        ImageView imageViewArtist = (ImageView) rowView.findViewById((int)_imageView);

//        View rowView = inflater.inflate(rowView, parent, false);
//        TextView textViewArtist = (TextView) rowView.findViewById(_textView);
//        ImageView imageViewArtist = (ImageView) rowView.findViewById(_imageView);
//
//        // Render text and image
            String s = spotifyContent._mainTitle;
            textViewArtist.setText(s);
//        textViewArtist.setText("Artists - getView");

            if (spotifyContent._imageURI.length()>0) {
                Picasso.with(_ObjectContext)
                        .load(spotifyContent._imageURI)
                        .into(imageViewArtist);
            }
            else {
                imageViewArtist.setImageResource(R.drawable.blank_cd);
            }

//        int image = spotifyContent._imageResource;
//        imageViewArtist.setImageResource(image);
//        imageViewArtist.setImageResource(R.drawable.ic_launcher);

        }

        // Return the rowView created
        return rowView;
    }

}
