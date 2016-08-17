package com.onegorilla.calendarandroid.restclient;

import android.content.Context;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.onegorilla.calendarandroid.model.Event;
import com.onegorilla.calendarandroid.model.User;

import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

import static com.onegorilla.calendarandroid.helper.QuickLog.qLog;

/**
 * Created by frodrok on 2016-08-17.
 */
public final class RestService {

    private final String API_URL;
    private final Context context;

    public RestService(final String API_URL, Context context) {
        if (API_URL == null  || context == null) {
            throw new IllegalArgumentException();
        }
        
        this.API_URL = API_URL;
        this.context = context;
    }

    public Observable<Boolean> verifyLogin(final String username, final String password) {
        JsonObject obj = new JsonObject();
        obj.addProperty("username", username);
        obj.addProperty("password", password);

        String requestTo = API_URL + "/validateuser";

        Future<Response<String>> ionFuture = Ion.with(context).load("GET", requestTo)
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


    public Observable<Long> getIdForUser(String localUsername) {
        String requestTo = API_URL + "/users/username/" + localUsername;
        
        Future<User> ionFuture = Ion.with(context).load(requestTo).as(User.class);
        
        return Observable.from(ionFuture).map(new Func1<User, Long>() {
            @Override
            public Long call(User user) {
                return user.getId();
            }
        });
    }

    public Observable<List<Event>> getEventsForUser(Long userId) {
        String requestTo = API_URL + "/users/" + userId + "/events";

        Future<List<Event>> ionEvents = Ion.with(context).load(requestTo).as(new TypeToken<List<Event>>(){});

        /*Ion.with(context).load(requestTo).as(new TypeToken<List<Event>>(){}).setCallback(new FutureCallback<List<Event>>() {
            @Override
            public void onCompleted(Exception e, List<Event> result) {
                qLog("events for user: ");
                if (result != null) {
                    for (Event event : result) {
                        qLog(event);
                    }
                } else {
                    qLog("no events found");
                }
            }
        });*/

        return Observable.from(ionEvents);
    }
}
