package com.gaurav.sangeet;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.gaurav.domain.interfaces.MusicInteractor;
import com.github.florent37.bubbletab.BubbleTab;

public class HomeActivity extends AppCompatActivity {

    private MusicInteractor musicInteractor;
    private HomeViewModel viewModel;

    // Views
    private BubbleTab bubbleTab;
    private ViewPager viewPager;
    private PageAdapter pageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bubbleTab = findViewById(R.id.bubbleTab);
        viewPager = findViewById(R.id.viewPager);

        // TODO: 6/1/18 improve via DI
        musicInteractor = ((MusicApplication) getApplication()).musicInteractor;
        viewModel = ViewModelProviders.of(this, new ViewModelFactory(musicInteractor))
                .get(HomeViewModel.class);
        pageAdapter = new PageAdapter(getSupportFragmentManager(),viewModel);
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewPager.setAdapter(pageAdapter);

        // todo remember the tab you were at before instead of 0
        viewPager.setCurrentItem(0);
        bubbleTab.setupWithViewPager(viewPager);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
