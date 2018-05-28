package com.gaurav.sangeet;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.gaurav.domain.interfaces.MusicInteractor;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.TreeSet;

import io.reactivex.disposables.Disposable;

public class SplashActivity extends AppCompatActivity {

    private Disposable initTasksDisposable;
    private MusicInteractor musicInteractor;
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
        musicInteractor = ((MusicApplication) getApplication()).musicInteractor;

        titleView.animate()
                .setDuration(2000)
                .rotationBy(360)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        animation.removeListener(this);
                        if (animate) {
                            animation.start();
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
                }).start();
        initTasksDisposable = ((MusicApplication) getApplication())
                .init()
                .subscribe(() -> animate = false, Throwable::printStackTrace);
    }

    @Override
    protected void onStop() {
        if (initTasksDisposable != null && !initTasksDisposable.isDisposed()) {
            initTasksDisposable.dispose();
        }
        super.onStop();
    }
}
