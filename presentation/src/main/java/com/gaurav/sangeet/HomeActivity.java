package com.gaurav.sangeet;

import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.gaurav.domain.usecases.CommandUseCases;
import com.gaurav.domain.usecases.FetchUseCases;
import com.github.florent37.awesomebar.AwesomeBar;
import com.github.florent37.bubbletab.BubbleTab;
import com.rom4ek.arcnavigationview.ArcNavigationView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class HomeActivity extends AppCompatActivity {

    private FetchUseCases fetchUseCases;
    private CommandUseCases commandUseCases;

    // Views
    private DrawerLayout drawerLayout;
    private ArcNavigationView navigationView;
    private BubbleTab bubbleTab;
    private ViewPager viewPager;
    private PageAdapter pageAdapter;
    private AwesomeBar awesomeBar;
    private BottomSheetBehavior bottomSheetBehavior;
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        awesomeBar = findViewById(R.id.bar);
        bubbleTab = findViewById(R.id.bubbleTab);
        viewPager = findViewById(R.id.viewPager);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // TODO: 6/7/18 add a animated search icon
        awesomeBar.addAction(R.drawable.awsb_ic_edit_animated, "Search");
        awesomeBar.setActionItemClickListener((position, actionItem) -> Toast.makeText(this, "Clicked " + actionItem.getText(),
                Toast.LENGTH_SHORT).show());
        awesomeBar.setOnMenuClickedListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        // TODO: 6/1/18 improve via DI
        fetchUseCases = ((MusicApplication) getApplication()).fetchUseCases;
        commandUseCases = ((MusicApplication) getApplication()).commandUseCases;
        pageAdapter = new PageAdapter(getSupportFragmentManager(), fetchUseCases, commandUseCases);

        viewPager.setAdapter(pageAdapter);

        // todo remember the tab you were at before instead of 0
        viewPager.setCurrentItem(0);
        bubbleTab.setupWithViewPager(viewPager);

        disposable = ((MusicApplication) getApplication()).musicStateManager.observeMusicState()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(musicState -> {

                });
        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottom_sheet));
        bottomSheetBehavior.setHideable(false);
        findViewById(R.id.bottom_sheet).setOnClickListener(v -> {
            if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Log.d("Back", "is pressed");
        if (drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED ||
                bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_DRAGGING ||
                bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_SETTLING) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        super.onDestroy();
    }
}
