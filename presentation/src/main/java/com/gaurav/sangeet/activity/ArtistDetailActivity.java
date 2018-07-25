package com.gaurav.sangeet.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.gaurav.domain.models.Artist;
import com.gaurav.sangeet.R;
import com.gaurav.sangeet.utils.ItemClickSupport;
import com.gaurav.sangeet.viewmodels.artistdetails.ArtistDetailViewModel;
import com.gaurav.sangeet.viewmodels.artistdetails.ArtistDetailViewModelFactory;
import com.gaurav.sangeet.viewmodels.bottomsheet.BottomSheetViewModel;
import com.gaurav.sangeet.viewmodels.bottomsheet.BottomSheetViewModelFactory;
import com.gaurav.sangeet.views.implementations.albums.AlbumsRVAdapter;
import com.gaurav.sangeet.views.implementations.bottomsheet.BottomSheetViewImpl;
import com.gaurav.sangeet.views.implementations.songs.SongsRVAdapter;
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
    private Toolbar toolbar;
    private ImageView artistJoinedArtwork;
    private ImageView artistIcon;
    private ImageButton playArtistButton;
    private RecyclerView artistSongRecyclerView;
    private RecyclerView artistAlbumRecyclerView;
    private BottomSheetViewImpl bottomSheetViewImpl;

    // View related objects
    private BottomSheetBehavior bottomSheetBehavior;
    private SongsRVAdapter songsRVAdapter;
    private AlbumsRVAdapter albumsRVAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_detail);

        uiEventsSubject = PublishSubject.create();

        toolbar = findViewById(R.id.toolbar);
        artistJoinedArtwork = findViewById(R.id.artistJoinedArtwork);
        artistIcon = findViewById(R.id.artistIcon);
        playArtistButton = findViewById(R.id.playArtistButton);
        artistSongRecyclerView = findViewById(R.id.songRecyclerView);
        artistAlbumRecyclerView = findViewById(R.id.albumRecyclerView);
        bottomSheetViewImpl = new BottomSheetViewImpl(findViewById(R.id.bottom_sheet));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getDrawable(android.R.drawable.arrow_up_float));
        playArtistButton.setOnClickListener(v -> uiEventsSubject.onNext(new PlayArtistDetailUIEvent(
                ((ArtistDetailViewState.Result) viewModel.getState().getValue()).getArtist(),
                null)));
        songsRVAdapter = new SongsRVAdapter(new ArrayList<>());
        albumsRVAdapter = new AlbumsRVAdapter(new ArrayList<>());
        artistSongRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        artistSongRecyclerView.setHasFixedSize(true);
        artistSongRecyclerView.setAdapter(songsRVAdapter);
        artistAlbumRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        artistAlbumRecyclerView.setHasFixedSize(true);
        artistAlbumRecyclerView.setAdapter(albumsRVAdapter);
        ItemClickSupport.addTo(artistSongRecyclerView)
                .setOnItemClickListener((recyclerView, position, v) ->
                        uiEventsSubject.onNext(new PlayArtistDetailUIEvent(
                        ((ArtistDetailViewState.Result) viewModel.getState().getValue()).getArtist()
                        , songsRVAdapter.getSong(position))));
        ItemClickSupport.addTo(artistAlbumRecyclerView)
                .setOnItemClickListener((recyclerView, position, v) -> {
            startActivity(new Intent(this, AlbumDetailActivity.class).putExtra(
                    "albumId", albumsRVAdapter.getAlbum(position).id));
        });

        // TODO: 7/15/18 FInd a better way tro manage bottom sheet and it's info
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetViewImpl.getBaseView());
        bottomSheetBehavior.setHideable(false);
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
                // TODO: 7/8/18 UI task: change the position of views according to this.
            }
        });
        BottomSheetViewModel viewModel = ViewModelProviders.of(this,
                new BottomSheetViewModelFactory(bottomSheetViewImpl, v -> {
                    if (bottomSheetBehavior.getState() ==
                            BottomSheetBehavior.STATE_COLLAPSED) {
                                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                            }
                        }))
                .get(BottomSheetViewModel.class);
        viewModel.getViewState().observe(this, bottomSheetViewImpl::render);


    }

    @Override
    protected void onStart() {
        super.onStart();
        viewModel = ViewModelProviders.of(this,
                new ArtistDetailViewModelFactory(this,
                        getIntent().getLongExtra("artistId", -1)))
                .get(ArtistDetailViewModel.class);
        viewModel.getState().observe(this, this::render);
    }

    @Override
    public void render(ArtistDetailViewState state) {
        if (state instanceof ArtistDetailViewState.Loading) {
            // show loading
        } else if (state instanceof ArtistDetailViewState.Error) {
            // show error
        } else {
            Artist artist = ((ArtistDetailViewState.Result) state).getArtist();
            getSupportActionBar().setTitle(artist.name);
            songsRVAdapter.updateData(new ArrayList<>(artist.songSet));
            albumsRVAdapter.updateData(new ArrayList<>(artist.albumSet));
            // TODO: 7/15/18 update album artwork and artist icon here
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
            super.onBackPressed();
        }
    }
}
