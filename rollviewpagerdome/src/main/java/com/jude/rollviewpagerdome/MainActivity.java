package com.jude.rollviewpagerdome;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jude.rollviewpager.RollPagerView;

/**
 * Created by zhuchenxi on 2016/12/13.
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void simple(View view){
        Intent i = new Intent(this,SimpleActivity.class);
        startActivity(i);
    }

    public void loop(View view){
        Intent i = new Intent(this,LoopActivity.class);
        startActivity(i);
    }

    public void netImage(View view){
        Intent i = new Intent(this,NetImageActivity.class);
        startActivity(i);
    }
}
