package com.gaurav.sangeet.views.implementations.bottomSheet;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gaurav.domain.models.Song;
import com.gaurav.domain.musicState.MusicState;
import com.gaurav.sangeet.R;
import com.gaurav.sangeet.views.interfaces.BottomSheetView;
import com.gaurav.sangeet.views.uiEvents.bottomSheet.BaseViewUIEvent;
import com.gaurav.sangeet.views.uiEvents.bottomSheet.BottomSheetUIEvent;
import com.gaurav.sangeet.views.uiEvents.bottomSheet.NextUIEvent;
import com.gaurav.sangeet.views.uiEvents.bottomSheet.PlayPauseUIEvent;
import com.gaurav.sangeet.views.uiEvents.bottomSheet.PrevUIEvent;
import com.gaurav.sangeet.views.uiEvents.bottomSheet.RepeatUIEvent;
import com.gaurav.sangeet.views.uiEvents.bottomSheet.ShuffleUIEvent;
import com.gaurav.sangeet.views.viewStates.BottomSheetViewState;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.subjects.PublishSubject;

public class BottomSheetViewImpl implements BottomSheetView {
    private View baseView;
    private ImageView songArtwork;
    private TextView songName;
    private TextView artistName;
    private TextView albumName;
    private ListView songQueueView;
    private ImageButton prevButton;
    private ImageButton playPauseButton;
    private ImageButton nextButton;
    private ImageButton shuffleButton;
    private ImageButton repeatButton;
    private TextView progressSeekbar;

    private PublishSubject<BottomSheetUIEvent> uiEventSubject;

    // helper private objects
    private MusicState musicState;

    public BottomSheetViewImpl(View baseView) {

        this.baseView = baseView;
        songArtwork = baseView.findViewById(R.id.songArtwork);
        songName = baseView.findViewById(R.id.songName);
        artistName = baseView.findViewById(R.id.artistName);
        albumName = baseView.findViewById(R.id.albumName);
        songQueueView = baseView.findViewById(R.id.songQueue);
        prevButton = baseView.findViewById(R.id.prev);
        playPauseButton = baseView.findViewById(R.id.playPause);
        nextButton = baseView.findViewById(R.id.next);
        shuffleButton = baseView.findViewById(R.id.shuffle);
        repeatButton = baseView.findViewById(R.id.repeat);
        progressSeekbar = baseView.findViewById(R.id.progress);

        uiEventSubject = PublishSubject.create();


        baseView.setOnClickListener(v -> uiEventSubject.onNext(new BaseViewUIEvent(baseView)));
        playPauseButton.setOnClickListener(v -> uiEventSubject.onNext(new PlayPauseUIEvent()));
        prevButton.setOnClickListener(v -> uiEventSubject.onNext(new PrevUIEvent()));
        nextButton.setOnClickListener(v -> uiEventSubject.onNext(new NextUIEvent()));
        shuffleButton.setOnClickListener(v -> uiEventSubject.onNext(new ShuffleUIEvent()));
        repeatButton.setOnClickListener(v -> uiEventSubject.onNext(new RepeatUIEvent()));
        // TODO: 7/8/18 add seekbar on click listener to action
    }

    @Override
    public void render(BottomSheetViewState viewState) {
        this.musicState = viewState.getMusicState();
        if (musicState.isShowStatus()) {
            if (viewState.isUpdateCurrentSongDetails()) {
                updateCurrentSongDetails(viewState.getCurrentSong());
            }
            updateSongQueue(musicState.getSongQueue());
            updatePlayPauseButtonState(musicState.isPlaying());
            updatePrevButtonState(musicState.isDisablePrev());
            updateRepeatShuffleState(musicState.isRepeat(), musicState.isShuffle());
            updateProgress(musicState.getProgress());
            if (baseView.getVisibility() != View.VISIBLE) {
                show();
            }
        } else {
            hide();
        }
    }

    @Override
    public PublishSubject<BottomSheetUIEvent> getUIEvents() {
        return uiEventSubject;
    }

    /* Private helper functions */

    private void updateProgress(long progress) {
        progressSeekbar.setText(String.valueOf(progress));
    }

    private void updateCurrentSongDetails(Song currentSong) {
        // todo fetch and update artwork
        songName.setText(currentSong.title);
        artistName.setText(currentSong.artist);
        albumName.setText(currentSong.album);
    }

    private void updateSongQueue(List<Song> songQueue) {
        // TODO: 7/8/18 Implement diffUtils in the update of songQueueView.
        List<String> data = new ArrayList<>();
        for (Song song : songQueue) {
            data.add(song.title);
        }
        this.songQueueView.setAdapter(new ArrayAdapter<String>(baseView.getContext(),
                android.R.layout.simple_list_item_1, data));

    }

    private BottomSheetViewImpl updatePlayPauseButtonState(boolean isPlaying) {
        if (isPlaying) {
            this.playPauseButton.setImageDrawable(baseView.getContext().getDrawable(android.R.drawable.ic_media_pause));
        } else {
            this.playPauseButton.setImageDrawable(baseView.getContext().getDrawable(android.R.drawable.ic_media_play));
        }
        return this;
    }

    private void updatePrevButtonState(boolean disablePrev) {
        prevButton.setEnabled(!disablePrev);
    }

    private void updateRepeatShuffleState(boolean repeat, boolean shuffle) {
        repeatButton.setEnabled(repeat);
        shuffleButton.setEnabled(shuffle);
    }

    private void show() {
        baseView.setVisibility(View.VISIBLE);
    }

    private void hide() {
        baseView.setVisibility(View.GONE);
    }

    public View getBaseView() {
        return baseView;
    }
}
