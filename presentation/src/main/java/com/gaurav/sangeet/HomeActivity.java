package com.gaurav.sangeet;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.gaurav.domain.usecases.CommandUseCases;
import com.gaurav.domain.usecases.FetchUseCases;
import com.github.florent37.bubbletab.BubbleTab;

public class HomeActivity extends AppCompatActivity {

    private FetchUseCases fetchUseCases;
    private CommandUseCases commandUseCases;

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
        fetchUseCases = ((MusicApplication) getApplication()).fetchUseCases;
        commandUseCases = ((MusicApplication) getApplication()).commandUseCases;
        pageAdapter = new PageAdapter(getSupportFragmentManager(), fetchUseCases, commandUseCases);
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
