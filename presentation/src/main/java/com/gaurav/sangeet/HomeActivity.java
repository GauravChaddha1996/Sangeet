package com.gaurav.sangeet;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gaurav.domain.interfaces.MusicInteractor;
import com.gaurav.domain.models.Playlist;

import java.util.Random;
import java.util.TreeSet;

import io.reactivex.schedulers.Schedulers;

public class HomeActivity extends AppCompatActivity {

    MusicInteractor musicInteractor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        musicInteractor  = ((MusicApplication)getApplication()).musicInteractor;
    }

}
