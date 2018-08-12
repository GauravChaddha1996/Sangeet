package com.gaurav.sangeet.views.helperviews;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.graphics.Palette;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.gaurav.domain.models.Album;
import com.gaurav.domain.models.Song;
import com.gaurav.sangeet.R;
import com.gaurav.sangeet.activity.AlbumDetailActivity;
import com.gaurav.sangeet.activity.ArtistDetailActivity;

public class DialogViewHelper {

    private Context context;
    private View mainView;
    private TextView dialogTitle;
    private TextView dialogArtistAlbum;
    private TextView dialogButtonGotoAlbum;
    private TextView dialogButtonGotoArtist;

    private boolean isSongDialog;
    private String title;
    private String body;
    private Bitmap bitmap;
    private long albumId;
    private long artistId;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;


    public DialogViewHelper(Context context, Song song) {
        this.context = context;
        // Find values to set
        isSongDialog = true;
        title = song.title;
        body = String.format("%s • %s", song.artist, song.album);
        bitmap = !song.artworkPath.equals("null") ? BitmapFactory.decodeFile(song.artworkPath) :
                BitmapFactory.decodeResource(context.getResources(), R.drawable.default_item_icon);
        albumId = song.albumId;
        artistId = song.artistId;

        initAndSetupViews();
    }

    public DialogViewHelper(Context context, Album album) {
        this.context = context;
        // Find values to set
        isSongDialog = false;
        title = album.name;
        body = String.format("%s • %s %s",
                album.multipleArtists ? "Various Artists" : album.artistName,
                album.songSet.size(), album.songSet.size() == 1 ? "Song" : "Songs");
        bitmap = !album.songSet.first().artworkPath.equals("null") ?
                BitmapFactory.decodeFile(album.songSet.first().artworkPath) :
                BitmapFactory.decodeResource(context.getResources(), R.drawable.default_item_icon);
        artistId = album.artistId;

        initAndSetupViews();
    }

    public AlertDialog getDialog() {
        builder = new AlertDialog.Builder(context);

        // set values and colors
        dialogTitle.setText(title);
        dialogArtistAlbum.setText(body);
        new Palette.Builder(bitmap).generate(palette -> {
            boolean set = false;
            Palette.Swatch swatch = palette.getVibrantSwatch();
            if (swatch != null && !set) {
                set = true;
                mainView.setBackgroundColor(swatch.getRgb());
                dialogTitle.setTextColor(swatch.getTitleTextColor());
                dialogArtistAlbum.setTextColor(swatch.getTitleTextColor());
                dialogButtonGotoAlbum.setTextColor(swatch.getBodyTextColor());
                dialogButtonGotoArtist.setTextColor(swatch.getBodyTextColor());
            }
            swatch = palette.getDominantSwatch();
            if (swatch != null && !set) {
                mainView.setBackgroundColor(swatch.getRgb());
                dialogTitle.setTextColor(swatch.getTitleTextColor());
                dialogArtistAlbum.setTextColor(swatch.getTitleTextColor());
                dialogButtonGotoAlbum.setTextColor(swatch.getBodyTextColor());
                dialogButtonGotoArtist.setTextColor(swatch.getBodyTextColor());
            }
        });
        builder.setView(mainView);
        dialog = builder.create();
        setClickListeners();
        setAnimation();
        return dialog;
    }

    private void initAndSetupViews() {
        mainView = LayoutInflater.from(context).inflate(R.layout.detail_dialog, null);
        dialogTitle = mainView.findViewById(R.id.dialog_title);
        dialogArtistAlbum = mainView.findViewById(R.id.dialog_artist_album);
        dialogButtonGotoAlbum = mainView.findViewById(R.id.dialog_button_goto_album);
        dialogButtonGotoArtist = mainView.findViewById(R.id.dialog_button_goto_artist);
        if (!isSongDialog) {
            dialogTitle.setGravity(Gravity.NO_GRAVITY);
            dialogArtistAlbum.setGravity(Gravity.NO_GRAVITY);
            dialogButtonGotoAlbum.setVisibility(View.INVISIBLE);
        }
    }

    private void setClickListeners() {
        dialogButtonGotoAlbum.setOnClickListener(v -> {
            context.startActivity(new Intent(context, AlbumDetailActivity.class)
                    .putExtra("albumId", albumId));
            dialog.dismiss();
        });
        dialogButtonGotoArtist.setOnClickListener(v -> {
            context.startActivity(new Intent(context, ArtistDetailActivity.class)
                    .putExtra("artistId", artistId));
            dialog.dismiss();
        });
    }

    private void setAnimation() {
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
    }
}
