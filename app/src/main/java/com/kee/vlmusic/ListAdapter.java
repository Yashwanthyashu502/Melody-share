package com.kee.vlmusic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import androidx.cardview.widget.CardView;

public class ListAdapter extends BaseAdapter {
    List<String> songNames;
    List<String> thumbnails;
    List<String> songArtist;
    List<String> songDuration;
    Context context;

    public ListAdapter(Context context, List<String> songNames, List<String> thumbnails, List<String> songArtist, List<String> songDuration) {
        this.context = context;
        this.songNames = songNames;
        this.thumbnails = thumbnails;
        this.songArtist = songArtist;
        this.songDuration = songDuration;
    }

    @Override
    public int getCount() {
        return songNames.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint({"InflateParams", "ViewHolder"})
    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.songs_list_layout, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(15)
                .build();

        Picasso.get().load(thumbnails.get(position)).transform(transformation).into(viewHolder.thumbnail);
        viewHolder.songName.setText(songNames.get(position));
        viewHolder.artistName.setText(songArtist.get(position));
        viewHolder.songDuration.setText(songDuration.get(position));

        // Set click listener for currentlyPlaying ImageView
        viewHolder.currentlyPlaying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(viewHolder.currentlyPlaying, position);
            }
        });

        return view;
    }

    private void showPopupMenu(View anchor, final int position) {
        PopupMenu popupMenu = new PopupMenu(context, anchor);
        popupMenu.getMenuInflater().inflate(R.menu.delete_menu, popupMenu.getMenu());

        // Set click listener for the delete menu item
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(android.view.MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.action_delete) {
                    deleteItem(position);
                    showDeleteToast();
                    return true;
                }
                return false;
            }
        });

        popupMenu.show();
    }
    private void showDeleteToast() {
        Toast.makeText(context, "Song deleted successfully", Toast.LENGTH_SHORT).show();
    }

    public void deleteItem(int position) {
        songNames.remove(position);
        thumbnails.remove(position);
        songArtist.remove(position);
        songDuration.remove(position);
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        TextView songName;
        TextView artistName;
        TextView songDuration;
        ImageView thumbnail;
        CardView cardView;
        ImageView currentlyPlaying;

        ViewHolder(View view) {
            songName = view.findViewById(R.id.songName);
            thumbnail = view.findViewById(R.id.songThumbnail);
            artistName = view.findViewById(R.id.artistName);
            songDuration = view.findViewById(R.id.songDuration);
            cardView = view.findViewById(R.id.cardView);
            currentlyPlaying = view.findViewById(R.id.currentlyPlaying);
        }
    }
}
