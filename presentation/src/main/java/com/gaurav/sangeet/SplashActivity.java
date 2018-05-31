package com.gaurav.sangeet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import io.reactivex.disposables.Disposable;

public class SplashActivity extends AppCompatActivity {

    private Disposable initTasksDisposable;

    private TextView titleView;
    private boolean animate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        titleView = findViewById(R.id.title);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // start logo animation
        startLogoAnimation();

        // Call initialization for modules.
        initTasksDisposable = ((MusicApplication) getApplication()).init()
                .subscribe(() -> animate = false, Throwable::printStackTrace);
    }

    @Override
    protected void onStop() {
        if (initTasksDisposable != null && !initTasksDisposable.isDisposed()) {
            initTasksDisposable.dispose();
        }
        super.onStop();
    }

    private void startLogoAnimation() {
        Animation animation = getSplashAnimation();
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Animation anim = getSplashAnimation();
                anim.setAnimationListener(this);
                if (animate) {
                    titleView.startAnimation(anim);
                } else {
                    SplashActivity.this.startActivity(
                            new Intent(SplashActivity.this, HomeActivity.class));
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        titleView.startAnimation(animation);
    }

    public Animation getSplashAnimation() {
        return AnimationUtils.makeInChildBottomAnimation(this);
    }
}
