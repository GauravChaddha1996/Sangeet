package com.gaurav.sangeet;

import android.content.Intent;
import android.os.Bundle;
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

import com.gaurav.domain.usecases.CommandUseCases;
import com.gaurav.domain.usecases.FetchUseCases;
import com.rom4ek.arcnavigationview.ArcNavigationView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import static com.gaurav.sangeet.Constants.Search.EXTRA_CIRCULAR_REVEAL_X;
import static com.gaurav.sangeet.Constants.Search.EXTRA_CIRCULAR_REVEAL_Y;

public class HomeActivity extends AppCompatActivity {

    // Use cases
    private FetchUseCases fetchUseCases;
    private CommandUseCases commandUseCases;

    // Views
    private DrawerLayout drawerLayout;
    private ArcNavigationView navigationView;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private View bottomSheet;

    // View related objects
    private PageAdapter pageAdapter;
    private BottomSheetBehavior bottomSheetBehavior;

    // Disposables
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // TODO: 6/1/18 improve via DI
        fetchUseCases = ((MusicApplication) getApplication()).fetchUseCases;
        commandUseCases = ((MusicApplication) getApplication()).commandUseCases;

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        tabLayout = findViewById(R.id.tab_layout);
        toolbar = findViewById(R.id.toolbar);
        viewPager = findViewById(R.id.viewPager);
        bottomSheet = findViewById(R.id.bottom_sheet);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Sangeet");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

        pageAdapter = new PageAdapter(getSupportFragmentManager(), fetchUseCases, commandUseCases);
        viewPager.setAdapter(pageAdapter);
        // todo remember the tab you were at before instead of 0
        viewPager.setCurrentItem(0);
        tabLayout.setupWithViewPager(viewPager);

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setHideable(false);
        bottomSheet.setOnClickListener(v -> {
            if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        disposable = ((MusicApplication) getApplication()).musicStateManager.observeMusicState()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(musicState -> {
                    // TODO: 6/12/18 Update bottom sheet view here
                });
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

    @Override
    protected void onDestroy() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        super.onDestroy();
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
