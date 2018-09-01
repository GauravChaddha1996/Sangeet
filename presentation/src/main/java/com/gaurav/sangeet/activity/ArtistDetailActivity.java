package com.gaurav.sangeet.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaurav.domain.models.Artist;
import com.gaurav.domain.models.Song;
import com.gaurav.sangeet.R;
import com.gaurav.sangeet.utils.ItemClickSupport;
import com.gaurav.sangeet.viewmodels.artistdetails.ArtistDetailViewModel;
import com.gaurav.sangeet.viewmodels.artistdetails.ArtistDetailViewModelFactory;
import com.gaurav.sangeet.viewmodels.bottomsheet.BottomSheetViewModel;
import com.gaurav.sangeet.views.helperviews.DialogViewHelper;
import com.gaurav.sangeet.views.implementations.artistdetails.ArtistDetailsAlbumsRVAdapter;
import com.gaurav.sangeet.views.implementations.artistdetails.ArtistDetailsSongsRVAdapter;
import com.gaurav.sangeet.views.implementations.bottomsheet.BottomSheetViewImpl;
import com.gaurav.sangeet.views.interfaces.ArtistDetailView;
import com.gaurav.sangeet.views.uievents.artistdetails.ArtistDetailUIEvent;
import com.gaurav.sangeet.views.uievents.artistdetails.PlayArtistDetailUIEvent;
import com.gaurav.sangeet.views.viewstates.ArtistDetailViewState;

import java.util.ArrayList;

import io.reactivex.subjects.PublishSubject;

public class ArtistDetailActivity extends AppCompatActivity implements ArtistDetailView {

    private ArtistDetailViewModel viewModel;
    private PublishSubject<ArtistDetailUIEvent> uiEventsSubject;

    // Views
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private TextView artistTotalAlbumsSongs;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView artistArtwork;
    private FloatingActionButton playArtistButton;
    private TextView songSection;
    private TextView albumSection;
    private RecyclerView albumRecyclerView;
    private RecyclerView songRecyclerView;
    private BottomSheetViewImpl bottomSheetViewImpl;

    // View related objects
    private BottomSheetBehavior bottomSheetBehavior;
    private ArtistDetailsAlbumsRVAdapter albumsRVAdapter;
    private ArtistDetailsSongsRVAdapter songsRVAdapter;
    private String albumsString, songsString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_artist_detail);

        uiEventsSubject = PublishSubject.create();
        initViews();
        setupViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewModel = ViewModelProviders.of(this, new ArtistDetailViewModelFactory(
                getIntent().getLongExtra("artistId", -1)))
                .get(ArtistDetailViewModel.class);
        viewModel.attachArtistDetailView(this);
        viewModel.getState().observe(this, this::render);
    }

    @Override
    public void onEnterAnimationComplete() {
        playArtistButton.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(getResources().getInteger(R.integer.detailPlayButtonAnimDuration))
                .start();
        bottomSheetViewImpl.getBaseView().setAlpha(1f);
        bottomSheetViewImpl.getBaseView().startAnimation(
                AnimationUtils.loadAnimation(this, R.anim.bottom_sheet_slide_up));
    }


    @Override
    public void render(ArtistDetailViewState state) {
        if (state instanceof ArtistDetailViewState.Loading) {
            // show loading
        } else if (state instanceof ArtistDetailViewState.Error) {
            // show error
        } else {
            Artist artist = ((ArtistDetailViewState.Result) state).getArtist();
            collapsingToolbarLayout.setTitle(artist.name);
            if (artist.albumSet.size() == 0) {
                albumsString = "";
            } else {
                albumsString = artist.albumSet.size() + " ";
                albumsString += artist.albumSet.size() == 1 ? "Album" : "Albums";
            }
            songsString = artist.songSet.size() + " ";
            songsString += artist.songSet.size() == 1 ? "Song" : "Songs";
            albumSection.setText(albumsString);
            songSection.setText(songsString);
            albumSection.animate().alpha(1f).setDuration(getResources().getInteger(R.integer.detailArtistAlbumSongSectionDuration))
                    .setStartDelay(getResources().getInteger(R.integer.detailArtistAlbumSongSectionDuration)).start();
            songSection.animate().alpha(1f).setDuration(getResources().getInteger(R.integer.detailArtistAlbumSongSectionDuration))
                    .setStartDelay(getResources().getInteger(R.integer.detailArtistAlbumSongSectionDuration)).start();
            artistTotalAlbumsSongs.setText(String.format("%s • %s", albumsString, songsString));
            songsRVAdapter.updateData(new ArrayList<>(artist.songSet));
            albumsRVAdapter.updateData(new ArrayList<>(artist.albumSet));

            LayoutAnimationController songController = AnimationUtils.loadLayoutAnimation(this,
                    R.anim.song_list_layout_animation);
            LayoutAnimationController albumController = AnimationUtils.loadLayoutAnimation(this,
                    R.anim.album_list_layout_animation);
            songController.getAnimation().setStartOffset(getResources().getInteger(R.integer.detailListOffset));
            albumController.getAnimation().setStartOffset(getResources().getInteger(R.integer.detailListOffset));
            songRecyclerView.setLayoutAnimation(songController);
            songRecyclerView.scheduleLayoutAnimation();
            albumRecyclerView.setLayoutAnimation(albumController);
            albumRecyclerView.scheduleLayoutAnimation();

            String artworkPath = "null";
            for (Song song : artist.songSet) {
                if (!song.artworkPath.equals("null")) {
                    artworkPath = song.artworkPath;
                    break;
                }
            }

            if (!artworkPath.equals("null")) {
                artistArtwork.setImageBitmap(BitmapFactory.decodeFile(artworkPath));
                new Palette.Builder(BitmapFactory.decodeFile(artworkPath))
                        .generate(this::updateColors);
            } else {
                artistArtwork.setImageDrawable(getDrawable(R.drawable.default_item_icon));
                new Palette.Builder(BitmapFactory.decodeResource(getResources(),
                        R.drawable.default_item_icon)).generate(this::updateColors);
            }
        }
    }

    @Override
    public PublishSubject<ArtistDetailUIEvent> getUIEvents() {
        return uiEventsSubject;
    }

    @Override
    public void onBackPressed() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED ||
                bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_DRAGGING ||
                bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_SETTLING) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            finishWithAnimation();
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finishWithAnimation();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        appBarLayout = findViewById(R.id.appBarLayout);
        toolbar = findViewById(R.id.toolbar);
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        artistTotalAlbumsSongs = findViewById(R.id.artistTotalAlbumsSongs);
        artistArtwork = findViewById(R.id.artistArtwork);
        playArtistButton = findViewById(R.id.playArtistButton);
        songSection = findViewById(R.id.artistDetailsSongSection);
        albumSection = findViewById(R.id.artistDetailsAlbumSection);
        albumRecyclerView = findViewById(R.id.artistDetailsAlbumRecyclerView);
        songRecyclerView = findViewById(R.id.artistDetailsSongRecyclerView);
        bottomSheetViewImpl = new BottomSheetViewImpl(findViewById(R.id.bottom_sheet));
    }

    private void setupViews() {
        // create view related objects
        albumsRVAdapter = new ArtistDetailsAlbumsRVAdapter(new ArrayList<>());
        songsRVAdapter = new ArtistDetailsSongsRVAdapter(new ArrayList<>());
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetViewImpl.getBaseView());

        // setup toolbar
        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            artistTotalAlbumsSongs.setTranslationY(verticalOffset / 3);
        });
        toolbar.setTitleTextAppearance(this, R.style.toolbarTitleFont);
        toolbar.setTitleTextColor(getColor(R.color.toolbarTitleColor));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);

        // setup album play button
        playArtistButton.setOnClickListener(v -> uiEventsSubject.onNext(new PlayArtistDetailUIEvent(
                ((ArtistDetailViewState.Result) viewModel.getState().getValue()).getArtist(),
                null)));
        playArtistButton.setScaleX(0f);
        playArtistButton.setScaleY(0f);

        albumSection.setAlpha(0f);
        songSection.setAlpha(0f);

        // setup recycler views
        albumRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        albumRecyclerView.setHasFixedSize(true);
        albumRecyclerView.setAdapter(albumsRVAdapter);
        ItemClickSupport.addTo(albumRecyclerView).setOnItemClickListener(
                (albumSongRecyclerView, position, v) ->
                        startActivity(new Intent(this, AlbumDetailActivity.class)
                                .putExtra("albumId", albumsRVAdapter.getAlbum(position).id)));
        songRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        songRecyclerView.setHasFixedSize(true);
        songRecyclerView.setAdapter(songsRVAdapter);
        ItemClickSupport.addTo(songRecyclerView)
                .setOnItemClickListener((recyclerView, position, v) ->
                        uiEventsSubject.onNext(new PlayArtistDetailUIEvent(
                                ((ArtistDetailViewState.Result) viewModel.getState().getValue()).getArtist()
                                , songsRVAdapter.getSong(position))));
        ItemClickSupport.addTo(songRecyclerView)
                .setOnItemLongClickListener((recyclerView, position, v) -> {
                    new DialogViewHelper(this, songsRVAdapter.getSong(position),
                            true, false).getDialog().show();
                    return true;
                });

        // TODO: 7/15/18 FInd a better way tro manage bottom sheet and it's info
        bottomSheetBehavior.setHideable(false);
        bottomSheetViewImpl.getBaseView().setAlpha(0f);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    // TODO: 7/8/18 add menu items for actions like showQueue, gotoAlbum,gotoArtist
                    // add to playlist here. Also hide these menus when state becomes moving or
                    // collapsed.
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                float scale = 1 - 3 * slideOffset;
                scale = scale < 0 ? 0 : scale;
                playArtistButton.setScaleX(scale);
                playArtistButton.setScaleY(scale);
                // TODO: 7/8/18 UI task: change the position of views according to this.
            }
        });
        BottomSheetViewModel viewModel = ViewModelProviders.of(this).get(BottomSheetViewModel.class);
        viewModel.attachBottomSheetView(bottomSheetViewImpl);
        viewModel.setOnClickListener(v -> {
            if (bottomSheetBehavior.getState() ==
                    BottomSheetBehavior.STATE_COLLAPSED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        viewModel.getViewState().observe(this, bottomSheetViewImpl::render);

    }

    private void updateColors(Palette palette) {
        Palette.Swatch swatch = palette.getVibrantSwatch();
        if (swatch == null) swatch = palette.getDominantSwatch();
        collapsingToolbarLayout.setContentScrimColor(swatch.getRgb());
        collapsingToolbarLayout.setStatusBarScrimColor(swatch.getRgb());
        collapsingToolbarLayout.setExpandedTitleColor(swatch.getTitleTextColor());
        collapsingToolbarLayout.setCollapsedTitleTextColor(swatch.getTitleTextColor());
        artistTotalAlbumsSongs.setTextColor(swatch.getBodyTextColor());
        playArtistButton.setBackgroundTintList(ColorStateList.valueOf(
                palette.getVibrantColor(getColor(R.color.colorAccent))));
    }

    private void finishWithAnimation() {
        finish();
        overridePendingTransition(R.anim.scale_down, R.anim.detail_fade_out);
    }
}
