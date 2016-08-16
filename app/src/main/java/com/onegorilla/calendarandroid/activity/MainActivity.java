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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<String> list = Arrays.asList("Android", "Ubuntu", "Mac OS");
        List<String> list1 = Arrays.asList("Android1", "Ubuntu1", "Mac OS");
        List<String> list2 = Arrays.asList("Android2", "Ubuntu2", "Mac OS");
        List<String> list3 = Arrays.asList("Android3", "Ubuntu3", "Mac OS");

        Observable<List<String>> listObservable = Observable.just(list, list1, list2, list3);
        listObservable.subscribe(new Observer<List<String>>() {

            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onNext(List<String> list) {
                Log.d("list", list.toString());
            }
        });

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // showSpinningProgressNigga();

                if (API_URL == null) {
                    return;
                }

                final String localUsername = username.getText().toString();
                final String localPassword = password.getText().toString();

                verifyLogin(localUsername, localPassword).subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {

                            sendToUserActivity(localUsername);


                        } else {
                            showLoginFailedDialog().show();
                        }
                    }
                });


            }
        });
    }

    private void sendToUserActivity(String username) {
        Intent intent = new Intent(this, UserActivity.class);
        qLog("sending" + username + "to useractivity");
        intent.putExtra("username", username);
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

    private Observable<Boolean> verifyLogin(final String username, final String password) {
        JsonObject obj = new JsonObject();
        obj.addProperty("username", username);
        obj.addProperty("password", password);

        String requestTo = API_URL + "/validateuser";

        Future<Response<String>> ionFuture = Ion.with(this).load("GET", requestTo)
                .setJsonObjectBody(obj)
                .asString()
                .withResponse();

        return Observable.from(ionFuture).map(new Func1<Response<String>, Boolean>() {
            @Override
            public Boolean call(Response<String> stringResponse) {
                return stringResponse.getHeaders().code() == 200;
            }
        });
    }
}
