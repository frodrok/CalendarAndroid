package com.onegorilla.calendarandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.Observer;

public class MainActivity extends AppCompatActivity {

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
    }
}
