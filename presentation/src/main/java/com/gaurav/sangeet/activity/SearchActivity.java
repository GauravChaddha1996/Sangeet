package com.gaurav.sangeet.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;

import com.gaurav.sangeet.R;
import com.gaurav.sangeet.utils.TransitionListenerAdapter;

import static android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE;
import static com.gaurav.sangeet.Constants.Search.EXTRA_CIRCULAR_REVEAL_X;
import static com.gaurav.sangeet.Constants.Search.EXTRA_CIRCULAR_REVEAL_Y;
import static com.gaurav.sangeet.Constants.Search.RETURN_ANIMATION_DURATION;
import static com.gaurav.sangeet.Constants.Search.SEARCH_ICON_ENTRY_ANIMATION_DURATION;
import static com.gaurav.sangeet.Constants.Search.SEARCH_ICON_RETURN_ANIMATION_DURATION;

public class SearchActivity extends AppCompatActivity {

    // Views
    View searchRootLayout;
    ImageView searchIcon;
    EditText searchEditText;
    View emptyView;

    // Helper variables
    private int revealX;
    private int revealY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Views
        searchRootLayout = findViewById(R.id.search_root_layout);
        searchIcon = findViewById(R.id.search_icon);
        searchEditText = findViewById(R.id.search_edit_text);
        emptyView = findViewById(R.id.empty_view);

        searchEditText.requestFocus();
        getWindow().setSoftInputMode(SOFT_INPUT_STATE_VISIBLE);
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // todo add done method here
                return true;
            }
            return false;
        });

        // Helper vars
        revealX = getIntent().getIntExtra(EXTRA_CIRCULAR_REVEAL_X, searchRootLayout.getWidth());
        revealY = getIntent().getIntExtra(EXTRA_CIRCULAR_REVEAL_Y, 0);

        setWindowAnimations();
    }

    @Override
    public void onBackPressed() {
        unrevealActivityCircularly();
        finishAfterTransition();
    }

    private void setWindowAnimations() {
        Transition sharedElementEnterTransition = new ChangeBounds()
                .setDuration(SEARCH_ICON_ENTRY_ANIMATION_DURATION)
                .setInterpolator(new AnticipateOvershootInterpolator(1.5f))
                .addListener(new TransitionListenerAdapter() {
                    @Override
                    public void onTransitionStart(Transition transition) {
                        revealActivity(revealX, revealY);
                    }

                    @Override
                    public void onTransitionEnd(Transition transition) {
                        transition.removeListener(this);
                    }
                });
        getWindow().setSharedElementEnterTransition(sharedElementEnterTransition);

        Transition sharedElementReturnTransition = getWindow().getSharedElementReturnTransition()
                .setDuration(SEARCH_ICON_RETURN_ANIMATION_DURATION);
        getWindow().setSharedElementReturnTransition(sharedElementReturnTransition);

        Transition returnTransition = getWindow().getReturnTransition();
        returnTransition.setDuration(RETURN_ANIMATION_DURATION);
        getWindow().setReturnTransition(returnTransition);
    }

    private void revealActivity(int x, int y) {
        float finalRadius = (float) (searchRootLayout.getHeight() * 1.25);

        Animator circularReveal = ViewAnimationUtils.createCircularReveal(searchRootLayout,
                searchRootLayout.getWidth() - x, y, 0, finalRadius);
        circularReveal.setDuration(300);
        circularReveal.setInterpolator(new AccelerateInterpolator());
        circularReveal.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                searchEditText.animate()
                        .alpha(1f)
                        .start();
                emptyView.animate()
                        .alpha(1f)
                        .start();
            }
        });
        circularReveal.start();

    }

    private void unrevealActivityCircularly() {
        float finalRadius = (float) (searchRootLayout.getHeight() * 1.25);
        Animator circularReveal = ViewAnimationUtils.createCircularReveal(
                searchRootLayout, searchRootLayout.getWidth() - revealX, revealY, finalRadius, 0);
        circularReveal.setDuration(500);
        circularReveal.setInterpolator(new AccelerateInterpolator());
        circularReveal.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                searchRootLayout.setVisibility(View.INVISIBLE);
            }
        });
        circularReveal.start();
    }
}
