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
 * Created by rosera on 12/07/15.
 */
public class TitleCustomAdapter extends ArrayAdapter<SpotifyContent> {
    private static final String TAG_NAME = CustomListAdapter.class.getSimpleName();
    Activity _ObjectContext;
    ArrayList<SpotifyContent> _spotifyContent;

    /**
     * Name: CustomListAdapter
     * First custom list adapter - whoa!
     */
    public TitleCustomAdapter(Activity context,
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
//        ViewHolder titleViewHolder      = null;
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
                    viewHolder.artistImage = (ImageView) view.findViewById(R.id.imageViewArtist);
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
//        SpotifyContent spotifyContent = getItem(position);
//        LayoutInflater inflater = _ObjectContext.getLayoutInflater();
//        View rowView = null;
//
//        if (spotifyContent != null) {
//
//            switch (spotifyContent._Task) {
//                case 1:
//                    rowView = inflater.inflate(R.layout.list_item_spotify, parent, false);
//                    TextView textViewArtist = (TextView) rowView.findViewById(R.id.textViewArtist);
//                    ImageView imageViewArtist = (ImageView) rowView.findViewById(R.id.imageViewArtist);
//
//                    // Render text and image
//                    textViewArtist.setText(spotifyContent._mainTitle);
//
//                    // Apply image or add stock image if URI not entered
//                    if (spotifyContent._imageURI.length() > 0) {
//                        Picasso.with(_ObjectContext)
//                                .load(spotifyContent._imageURI)
//                                .into(imageViewArtist);
//                    } else {
//                        imageViewArtist.setImageResource(R.drawable.blank_cd);
//                    }
//                    break;
//
//                case 2:
//                    rowView = inflater.inflate(R.layout.list_item_top_ten, parent, false);
//                    TextView textViewTitle1 = (TextView) rowView.findViewById(R.id.textViewTopTenArtist);
//                    TextView textViewTitle2 = (TextView) rowView.findViewById(R.id.textViewTopTenAlbum);
//                    ImageView imageViewArtist1 = (ImageView) rowView.findViewById(R.id.imageViewTopTenArtist);
//
//                    // Render text and image
//                    if (spotifyContent._mainTitle != null)
//                        textViewTitle1.setText(spotifyContent._mainTitle);
//
//                    if (spotifyContent._albumTitle != null)
//                        textViewTitle2.setText(spotifyContent._albumTitle);
//
//                    // Apply image or add stock image if URI not entered
//                    if (spotifyContent._imageURI.length() > 0) {
//                        Picasso.with(_ObjectContext)
//                                .load(spotifyContent._imageURI)
//                                .into(imageViewArtist1);
//                    } else {
//                        imageViewArtist1.setImageResource(R.drawable.blank_cd);
//                    }
//                    break;
//
//                default:
//                    break;
//            }
//        }

        // Return the rowView [image+text(s)] to insert into the ListView control
        return view;
    }

    private static class ViewHolder {
        TextView    artistTitle;
        TextView    artistSubTitle;
        ImageView   artistImage;
    }


}
