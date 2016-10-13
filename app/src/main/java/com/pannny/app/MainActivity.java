package com.pannny.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.pannny.view.StretchCardView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final StretchCardView scv1, scv2;
        scv1 = (StretchCardView) findViewById(R.id.sCardView1);
        scv2 = (StretchCardView) findViewById(R.id.sCardView2);
        scv1.setStretchListener(new StretchCardView.StretchListener() {
            @Override
            public void onStretchChangedListener(boolean stretch) {
                scv2.setTitleTouchAble(stretch);
            }

            @Override
            public void onStretchBlocked(boolean stretch) {
                scv2.clickTitle();
            }
        });
        scv2.setStretchListener(new StretchCardView.StretchListener() {
            @Override
            public void onStretchChangedListener(boolean stretch) {
                scv1.setTitleTouchAble(stretch);
            }

            @Override
            public void onStretchBlocked(boolean stretch) {
                scv1.clickTitle();
            }
        });
    }
}
