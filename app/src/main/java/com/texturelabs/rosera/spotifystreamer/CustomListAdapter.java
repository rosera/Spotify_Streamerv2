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

/**
 * Created by rosera on 06/06/15.
 * reference: http://www.vogella.com/tutorials/AndroidListView/article.html#adapterown
 * 3. Custom adapter implementations
 */
public class CustomListAdapter extends ArrayAdapter<SpotifyContent> {
    private static final String TAG_NAME = CustomListAdapter.class.getSimpleName();
    private static final int                TAG_ARTIST  = 1;
    private static final int                TAG_TITLE   = 2;
    Activity                                mActivity;
    ArrayList<SpotifyContent>               mSpotifyContent;

    /**
     * Name: CustomListAdapter
     * First custom list adapter - whoa!
     */
    public CustomListAdapter (Activity context,
                              ArrayList<SpotifyContent> spotifyContents) {

        // Compiler: Call to super must be the first statement in constructor
        super(context, 0, spotifyContents) ;

        // Initialise member variables
        this.mActivity  = context;
        this.mSpotifyContent = spotifyContents;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mSpotifyContent.size();
    }

    @Override
    public SpotifyContent getItem(int position) {
        // TODO Auto-generated method stub
        return mSpotifyContent.get(position);
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

        ViewHolder viewHolder;
        SpotifyContent  spotifyContent  = this.mSpotifyContent.get(position);

        switch(spotifyContent.mFragmentTask) {
            case TAG_ARTIST:
                if (view == null) {
                    view = LayoutInflater.from(mActivity).inflate(R.layout.list_item_spotify, null);
                    viewHolder = new ViewHolder();
                    viewHolder.artistTitle = (TextView) view.findViewById(R.id.textViewArtist);
                    viewHolder.artistImage = (ImageView) view.findViewById(R.id.imageViewArtist);
                    view.setTag(viewHolder);
                }
                else {
                    viewHolder = (ViewHolder) view.getTag();
                }

                viewHolder.artistTitle.setText(spotifyContent.mSpotifyTitle);

                // Apply image or add stock image if URI not entered
                if (spotifyContent.mSpotifyImageURI.length() > 0) {
                    Picasso.with(mActivity)
                            .load(spotifyContent.mSpotifyImageURI)
                            .into(viewHolder.artistImage);
                } else {
                    viewHolder.artistImage.setImageResource(R.drawable.blank_cd);
                }
                break;
            case TAG_TITLE:
                if (view == null) {
                    view = LayoutInflater.from(mActivity).inflate(R.layout.list_item_top_ten, null);
                    viewHolder = new ViewHolder();
                    viewHolder.artistTitle = (TextView) view.findViewById(R.id.textViewTopTenArtist);
                    viewHolder.artistSubTitle = (TextView) view.findViewById(R.id.textViewTopTenAlbum);
                    viewHolder.artistImage = (ImageView) view.findViewById(R.id.imageViewTopTenArtist);
                    view.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) view.getTag();
                }

                // Render text and image
                if (spotifyContent.mSpotifyTitle != null)
                    viewHolder.artistTitle.setText(spotifyContent.mSpotifyTitle);

                if (spotifyContent.mSpotifyAlbumTitle != null)
                    viewHolder.artistSubTitle.setText(spotifyContent.mSpotifyAlbumTitle);

                // Apply image or add stock image if URI not entered
                if (spotifyContent.mSpotifyImageURI.length() > 0) {
                    Picasso.with(mActivity)
                            .load(spotifyContent.mSpotifyImageURI)
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
