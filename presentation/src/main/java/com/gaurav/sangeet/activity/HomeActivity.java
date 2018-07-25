package com.gaurav.sangeet.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.gaurav.sangeet.R;
import com.gaurav.sangeet.utils.BottomPaddingDachshundIndicator;
import com.gaurav.sangeet.viewmodels.bottomsheet.BottomSheetViewModel;
import com.gaurav.sangeet.viewmodels.bottomsheet.BottomSheetViewModelFactory;
import com.gaurav.sangeet.views.implementations.bottomsheet.BottomSheetViewImpl;
import com.kekstudio.dachshundtablayout.DachshundTabLayout;

import static com.gaurav.sangeet.Constants.Search.EXTRA_CIRCULAR_REVEAL_X;
import static com.gaurav.sangeet.Constants.Search.EXTRA_CIRCULAR_REVEAL_Y;

public class HomeActivity extends AppCompatActivity {

    // Views
    private Toolbar toolbar;
    private DachshundTabLayout tabLayout;
    private ViewPager viewPager;
    private BottomSheetViewImpl bottomSheetViewImpl;

    // View related objects
    private PageAdapter pageAdapter;
    private BottomSheetBehavior bottomSheetBehavior;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initViews();
        setUpViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_button:
                presentSearchActivity(findViewById(R.id.search_button));
                return true;
        }
        return super.onOptionsItemSelected(item);
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

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.viewPager);
        bottomSheetViewImpl = new BottomSheetViewImpl(findViewById(R.id.bottom_sheet));
    }

    private void setUpViews() {
        // create view related objects
        pageAdapter = new PageAdapter(getSupportFragmentManager());
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetViewImpl.getBaseView());

        // setup toolbar, viewPager and tab layout
        toolbar.setTitleTextColor(getColor(R.color.toolbarTitleColor));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.app_name));

        viewPager.setAdapter(pageAdapter);
        viewPager.setCurrentItem(0);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setAnimatedIndicator(new BottomPaddingDachshundIndicator(tabLayout));

        // setup bottom sheet
        // TODO: 7/20/18 Clean up code in a such a way of bottom sheet that it's managed easily
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
