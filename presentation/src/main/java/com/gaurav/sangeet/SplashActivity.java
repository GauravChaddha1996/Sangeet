package com.gaurav.sangeet;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewPropertyAnimator;
import android.widget.TextView;

import com.gaurav.domain.interfaces.MusicInteractor;

import io.reactivex.disposables.Disposable;

public class SplashActivity extends AppCompatActivity {

    private Disposable initTasksDisposable;
    private MusicInteractor musicInteractor;
    private TextView titleView;
    private boolean animate = true;
    private ViewPropertyAnimator viewPropertyAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        titleView = findViewById(R.id.title);
    }

    @Override
    protected void onStart() {
        super.onStart();
        musicInteractor = ((MusicApplication) getApplication()).musicInteractor;
        viewPropertyAnimator = titleView.animate()
                .setDuration(1000)
                .rotationBy(360)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        System.out.println("anim start");
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        animation.removeListener(this);
                        System.out.println("anim end");
                        if (animate) {
                            viewPropertyAnimator
                                    .setListener(this)
                                    .start();
                        } else {
                            SplashActivity.this
                                    .startActivity(new Intent(SplashActivity.this,
                                            HomeActivity.class));
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
        initTasksDisposable = ((MusicApplication) getApplication())
                .init()
                .doOnSubscribe(disposable -> System.out.println("starts subs"))
                .subscribe(() -> {
                    animate = false;
                    SplashActivity.this
                            .startActivity(new Intent(SplashActivity.this,
                                    HomeActivity.class));
                }, Throwable::printStackTrace);
    }

    @Override
    protected void onStop() {
        if (initTasksDisposable != null && !initTasksDisposable.isDisposed()) {
            initTasksDisposable.dispose();
        }
        super.onStop();
    }
}
