package com.gaurav.sangeet;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.graphics.drawable.Animatable2Compat;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.widget.ImageView;

import io.reactivex.disposables.Disposable;

public class SplashActivity extends AppCompatActivity {

    private Disposable initTasksDisposable;

    private ImageView logo;
    private Animatable logoAnimatable;
    private boolean animate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        logo = findViewById(R.id.logo);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // start logo animation
        startLogoAnimation();

        // Call initialization for modules.
        initTasksDisposable = ((MusicApplication) getApplication()).init()
                .subscribe(() -> {
                    animate = false;
                    if (!logoAnimatable.isRunning()) {
                        launchHomeActivity();
                    }
                }, Throwable::printStackTrace);
    }

    @Override
    protected void onStop() {
        if (initTasksDisposable != null && !initTasksDisposable.isDisposed()) {
            initTasksDisposable.dispose();
        }
        super.onStop();
    }

    @SuppressWarnings("ConstantConditions")
    private void startLogoAnimation() {
        AnimatedVectorDrawableCompat avd = AnimatedVectorDrawableCompat.create(this,
                R.drawable.logo_color_scheme_1);
        avd.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
            @Override
            public void onAnimationEnd(Drawable drawable) {
                if (!animate) {
                    launchHomeActivity();
                }
            }
        });
        logo.setImageDrawable(avd);
        logoAnimatable = (Animatable) logo.getDrawable();
        logoAnimatable.start();
    }

    private void launchHomeActivity() {
        SplashActivity.this.startActivity(
                new Intent(SplashActivity.this, HomeActivity.class));
    }
}
