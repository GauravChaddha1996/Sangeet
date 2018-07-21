package com.gaurav.sangeet.activity;

import android.arch.lifecycle.ViewModelProviders;
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

import com.gaurav.domain.models.Album;
import com.gaurav.sangeet.R;
import com.gaurav.sangeet.utils.ItemClickSupport;
import com.gaurav.sangeet.viewmodels.albumdetails.AlbumDetailViewModel;
import com.gaurav.sangeet.viewmodels.albumdetails.AlbumDetailViewModelFactory;
import com.gaurav.sangeet.viewmodels.bottomsheet.BottomSheetViewModel;
import com.gaurav.sangeet.viewmodels.bottomsheet.BottomSheetViewModelFactory;
import com.gaurav.sangeet.views.implementations.bottomsheet.BottomSheetViewImpl;
import com.gaurav.sangeet.views.implementations.songs.SongsRVAdapter;
import com.gaurav.sangeet.views.interfaces.AlbumDetailView;
import com.gaurav.sangeet.views.uievents.albumdetails.AlbumDetailUIEvent;
import com.gaurav.sangeet.views.uievents.albumdetails.PlayAlbumDetailUIEvent;
import com.gaurav.sangeet.views.viewstates.AlbumDetailViewState;

import java.util.ArrayList;

import io.reactivex.subjects.PublishSubject;

public class AlbumDetailActivity extends AppCompatActivity implements AlbumDetailView {


    private AlbumDetailViewModel viewModel;
    private PublishSubject<AlbumDetailUIEvent> uiEventsSubject;

    // Views
    private Toolbar toolbar;
    private ImageView albumArtwork;
    private ImageView artistIcon;
    private ImageButton playAlbumButton;
    private RecyclerView albumSongRecyclerView;
    private BottomSheetViewImpl bottomSheetViewImpl;

    // View related objects
    private BottomSheetBehavior bottomSheetBehavior;
    private SongsRVAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);

        uiEventsSubject = PublishSubject.create();

        toolbar = findViewById(R.id.toolbar);
        albumArtwork = findViewById(R.id.albumArtwork);
        artistIcon = findViewById(R.id.artistIcon);
        playAlbumButton = findViewById(R.id.playAlbumButton);
        albumSongRecyclerView = findViewById(R.id.recyclerView);
        bottomSheetViewImpl = new BottomSheetViewImpl(findViewById(R.id.bottom_sheet));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getDrawable(android.R.drawable.arrow_up_float));
        playAlbumButton.setOnClickListener(v -> uiEventsSubject.onNext(new PlayAlbumDetailUIEvent(
                ((AlbumDetailViewState.Result) viewModel.getState().getValue()).getAlbum(),
                null)));
        adapter = new SongsRVAdapter(new ArrayList<>());
        albumSongRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        albumSongRecyclerView.setHasFixedSize(true);
        albumSongRecyclerView.setAdapter(adapter);
        ItemClickSupport.addTo(albumSongRecyclerView).setOnItemClickListener((albumSongRecyclerView,
                                                                              position, v) ->
                uiEventsSubject.onNext(new PlayAlbumDetailUIEvent(
                        ((AlbumDetailViewState.Result) viewModel.getState().getValue()).getAlbum()
                        , adapter.getSong(position))));

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
                new BottomSheetViewModelFactory(bottomSheetViewImpl,
                        v -> {
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
                new AlbumDetailViewModelFactory(this,
                        getIntent().getLongExtra("albumId", -1)))
                .get(AlbumDetailViewModel.class);
        viewModel.getState().observe(this, this::render);
    }

    @Override
    public void render(AlbumDetailViewState state) {
        if (state instanceof AlbumDetailViewState.Loading) {
            // show loading
        } else if (state instanceof AlbumDetailViewState.Error) {
            // show error
        } else {
            Album album = ((AlbumDetailViewState.Result) state).getAlbum();
            getSupportActionBar().setTitle(album.name);
            adapter.updateData(new ArrayList<>(album.songSet));
            // TODO: 7/15/18 update album artwork and artist icon here
        }
    }

    @Override
    public PublishSubject<AlbumDetailUIEvent> getUIEvents() {
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
