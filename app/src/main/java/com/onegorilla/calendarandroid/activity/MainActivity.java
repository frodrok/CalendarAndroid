package com.onegorilla.calendarandroid.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.onegorilla.calendarandroid.R;
import com.onegorilla.calendarandroid.restclient.RestService;

import java.util.Arrays;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.onegorilla.calendarandroid.helper.QuickLog.*;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.loginButton)
    Button loginButton;

    @BindView(R.id.username)
    EditText username;

    @BindView(R.id.password)
    EditText password;

    @BindString(R.string.API_URL)
    String API_URL;

    private RestService rest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        rest = new RestService(API_URL, this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // showSpinningProgressNigga();

                if (API_URL == null) {
                    return;
                }

                final String localUsername = username.getText().toString();
                final String localPassword = password.getText().toString();

                rest.verifyLogin(localUsername, localPassword).subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {

                            rest.getIdForUser(localUsername).subscribe(new Action1<Long>() {
                                @Override
                                public void call(Long aLong) {
                                    sendToUserActivity(aLong, localUsername);
                                }
                            });

                        } else {
                            showLoginFailedDialog().show();
                        }
                    }
                });


            }
        });

        autoLogin();
    }

    private void autoLogin() {
        username.setText("fredrik");
        password.setText("captainanus");
        loginButton.callOnClick();
    }

    private void sendToUserActivity(Long userId, String username) {
        Intent intent = new Intent(this, UserActivity.class);
        intent.putExtra("userid", userId);
        intent.putExtra("username", username);
        qLog("sending {" + userId + "}: " + username + " to useractivity");
        startActivity(intent);
    }

    private AlertDialog showLoginFailedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Login failed")
                .setPositiveButton("Alrighty then", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Bail!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        dialog.dismiss();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }


}
