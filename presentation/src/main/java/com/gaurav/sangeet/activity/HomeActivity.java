package com.gaurav.sangeet.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.gaurav.domain.interfaces.MusicStateManager;
import com.gaurav.domain.usecases.interfaces.CommandUseCases;
import com.gaurav.domain.usecases.interfaces.FetchUseCases;
import com.gaurav.sangeet.MusicApplication;
import com.gaurav.sangeet.R;
import com.gaurav.sangeet.viewModels.bottomSheet.BottomSheetViewModel;
import com.gaurav.sangeet.viewModels.bottomSheet.BottomSheetViewModelFactory;
import com.gaurav.sangeet.views.implementations.bottomSheet.BottomSheetViewImpl;
import com.rom4ek.arcnavigationview.ArcNavigationView;

import static com.gaurav.sangeet.Constants.Search.EXTRA_CIRCULAR_REVEAL_X;
import static com.gaurav.sangeet.Constants.Search.EXTRA_CIRCULAR_REVEAL_Y;

public class HomeActivity extends AppCompatActivity {

    // Use cases
    private FetchUseCases fetchUseCases;
    private CommandUseCases commandUseCases;

    private MusicStateManager musicStateManager;

    // Views
    private DrawerLayout drawerLayout;
    private ArcNavigationView navigationView;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private BottomSheetViewImpl bottomSheetViewImpl;

    // View related objects
    private PageAdapter pageAdapter;
    private BottomSheetBehavior bottomSheetBehavior;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // TODO: 6/1/18 improve via DI
        fetchUseCases = ((MusicApplication) getApplication()).fetchUseCases;
        commandUseCases = ((MusicApplication) getApplication()).commandUseCases;
        musicStateManager = ((MusicApplication) getApplication()).musicStateManager;

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        tabLayout = findViewById(R.id.tab_layout);
        toolbar = findViewById(R.id.toolbar);
        viewPager = findViewById(R.id.viewPager);

        bottomSheetViewImpl = new BottomSheetViewImpl(findViewById(R.id.bottom_sheet));

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Sangeet");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

        pageAdapter = new PageAdapter(getSupportFragmentManager(), fetchUseCases, commandUseCases);
        viewPager.setAdapter(pageAdapter);
        viewPager.setCurrentItem(0);
        tabLayout.setupWithViewPager(viewPager);

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
        BottomSheetViewModel viewModel = ViewModelProviders.of(HomeActivity.this,
                new BottomSheetViewModelFactory(bottomSheetViewImpl, commandUseCases, musicStateManager,
                        v -> {
                            if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                            }
                        }))
                .get(BottomSheetViewModel.class);
        viewModel.getViewState().observe(this, bottomSheetViewImpl::render);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.search_button:
                presentSearchActivity(findViewById(R.id.search_button));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
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

    @SuppressWarnings("unchecked")
    private void presentSearchActivity(View view) {
        view.setTransitionName("search_icon");
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, new Pair<>(view, "search_icon"));

        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra(EXTRA_CIRCULAR_REVEAL_X,
                view.getX() + view.getWidth() / 2);
        intent.putExtra(EXTRA_CIRCULAR_REVEAL_Y,
                view.getY() + view.getHeight() / 2);

        startActivity(intent, options.toBundle());
    }

}