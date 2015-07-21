package com.texturelabs.rosera.spotifystreamer;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.internal.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rosera on 06/06/15.
 * reference: http://www.vogella.com/tutorials/AndroidListView/article.html#adapterown
 * 3. Custom adapter implementations
 */
public class CustomListAdapter extends ArrayAdapter<SpotifyContent> {
    private static final String TAG_NAME = CustomListAdapter.class.getSimpleName();
    Activity                    _ObjectContext;
    ArrayList<SpotifyContent>   _spotifyContent;


    /**
     * Name: CustomListAdapter
     * First custom list adapter - whoa!
     */
    public CustomListAdapter (Activity context,
                              ArrayList<SpotifyContent> spotifyContents) {

        // Compiler: Call to super must be the first statement in constructor
        super(context, 0, spotifyContents) ;

        // Initialise member variables
        this._ObjectContext  = context;
        this._spotifyContent = spotifyContents;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return _spotifyContent.size();
    }

    @Override
    public SpotifyContent getItem(int position) {
        // TODO Auto-generated method stub
        return _spotifyContent.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }


    /**
     * Name: getView
     * Inflates the layout passed and populates the image and textview
     */
    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder viewHolder     = null;
        SpotifyContent  spotifyContent  = this._spotifyContent.get(position);

        switch(spotifyContent._Task) {
            case 1:
                if (view == null) {
                    view = LayoutInflater.from(this._ObjectContext).inflate(R.layout.list_item_spotify, null);
                    viewHolder = new ViewHolder();
                    viewHolder.artistTitle = (TextView) view.findViewById(R.id.textViewArtist);
                    viewHolder.artistImage = (ImageView) view.findViewById(R.id.imageViewArtist);
                    view.setTag(viewHolder);
                }
                else {
                    viewHolder = (ViewHolder) view.getTag();
                }

                viewHolder.artistTitle.setText(spotifyContent._mainTitle);

                // Apply image or add stock image if URI not entered
                if (spotifyContent._imageURI.length() > 0) {
                    Picasso.with(_ObjectContext)
                            .load(spotifyContent._imageURI)
                            .into(viewHolder.artistImage);
                } else {
                    viewHolder.artistImage.setImageResource(R.drawable.blank_cd);
                }
                break;
            case 2:
                if (view == null) {
                    view = LayoutInflater.from(this._ObjectContext).inflate(R.layout.list_item_top_ten, null);
                    viewHolder = new ViewHolder();
                    viewHolder.artistTitle = (TextView) view.findViewById(R.id.textViewTopTenArtist);
                    viewHolder.artistSubTitle = (TextView) view.findViewById(R.id.textViewTopTenAlbum);
                    viewHolder.artistImage = (ImageView) view.findViewById(R.id.imageViewTopTenArtist);
                    view.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) view.getTag();
                }

                // Render text and image
                if (spotifyContent._mainTitle != null)
                    viewHolder.artistTitle.setText(spotifyContent._mainTitle);

                if (spotifyContent._albumTitle != null)
                    viewHolder.artistSubTitle.setText(spotifyContent._albumTitle);

                // Apply image or add stock image if URI not entered
                if (spotifyContent._imageURI.length() > 0) {
                    Picasso.with(_ObjectContext)
                            .load(spotifyContent._imageURI)
                            .into(viewHolder.artistImage);
                } else {
                    viewHolder.artistImage.setImageResource(R.drawable.blank_cd);
                }
                break;
            default:
                break;

        }

        // Return the rowView [image+text(s)] to insert into the ListView control
        return view;
    }


    /**
     * Name: ViewHolder
     * More efficient content management for the getView routine
     */
    private static class ViewHolder {
        TextView    artistTitle;
        TextView    artistSubTitle;
        ImageView   artistImage;
    }

}
