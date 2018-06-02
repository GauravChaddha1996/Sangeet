package com.gaurav.sangeet;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.gaurav.domain.interfaces.MusicInteractor;
import com.gaurav.sangeet.home.FakeFragment;
import com.gaurav.sangeet.home.HomeViewModel;
import com.github.florent37.bubbletab.BubbleTab;

public class HomeActivity extends AppCompatActivity {

    MusicInteractor musicInteractor;
    HomeViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // TODO: 6/1/18 improve via DI
        musicInteractor = ((MusicApplication) getApplication()).musicInteractor;
        viewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        // todo find a better way for this
        viewModel.setMusicInteractor(musicInteractor);
        ((ViewPager) findViewById(R.id.viewPager))
                .setAdapter(
                        new FragmentStatePagerAdapter(getSupportFragmentManager()) {
                            @Override
                            public Fragment getItem(int position) {
                                return new FakeFragment(viewModel);
                            }

                            @Override
                            public int getCount() {
                                return 5;
                            }
                        });
        ((ViewPager) findViewById(R.id.viewPager)).setCurrentItem(0);
        ((BubbleTab) findViewById(R.id.bubbleTab)).setupWithViewPager(findViewById(R.id.viewPager));
    }
}
