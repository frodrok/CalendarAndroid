package com.onegorilla.calendarandroid.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import com.onegorilla.calendarandroid.R;

import static com.onegorilla.calendarandroid.helper.QuickLog.qLog;

/**
 * Created by frodrok on 2016-08-16.
 */
public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        String username = null;
        if (savedInstanceState == null) {
            username = "frodrok";
        } else {
            username = savedInstanceState.getString("username");
        }

        qLog(username);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}
