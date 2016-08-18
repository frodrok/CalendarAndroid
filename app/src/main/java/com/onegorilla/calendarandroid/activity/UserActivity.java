package com.onegorilla.calendarandroid.activity;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.onegorilla.calendarandroid.R;
import com.onegorilla.calendarandroid.fragment.EventFragment;
import com.onegorilla.calendarandroid.model.Event;
import com.onegorilla.calendarandroid.restclient.RestService;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindString;
import butterknife.ButterKnife;
import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.onegorilla.calendarandroid.helper.QuickLog.qLog;

public class UserActivity extends AppCompatActivity {

    private String username;
    private Long userId;
    private RestService rest;
    private Map<Date, Event> dateEventsMap;
    private CaldroidFragment caldroidFragment;

    @BindString(R.string.API_URL)
    String API_URL;

    @Override
    @TargetApi(16)
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        userId = getIntent().getExtras().getLong("userid");
        username = getIntent().getExtras().getString("username");

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user);

        ButterKnife.bind(this);

        dateEventsMap = new HashMap<>();

        rest = new RestService(API_URL, this);

        final EventFragment eventFragment = new EventFragment();

        caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        caldroidFragment.setArguments(args);

        android.support.v4.app.FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.add(R.id.userActivityContainer, caldroidFragment, "calFragment");
        t.add(R.id.userActivityContainer, eventFragment, "eventFragment");
        t.commit();

        caldroidFragment.setCaldroidListener(
                new CaldroidListener() {
                    @Override
                    public void onSelectDate(Date date, View view) {
                        Event dayEvent = eventsForDay(date);
                        if (dayEvent == null) {
                            return;
                        }

                        Drawable d = getResources().getDrawable(R.drawable.left_arrow);

//                        caldroidFragment.setBackgroundDrawableForDate(d, date);
                        eventFragment.drawEvent(dayEvent);

                        // view.setBackground(d);

                        caldroidFragment.refreshView();
                    }
                });

        refreshNotOnClick();
    }

    private void setupCalendar(CaldroidFragment caldroidFragment, List<Event> events) {
        Drawable doubled = new ColorDrawable(getResources().getColor(R.color.backgroundGreen));

        for (Event e : events) {
            Date from = new Date(e.getFrom());
            // qLog("putting " + from + " with key " + e);
            dateEventsMap.put(from, e);
            caldroidFragment.setBackgroundDrawableForDate(doubled, from);
        }

        caldroidFragment.refreshView();

    }

    /* should return List<Event> */
    private Event eventsForDay(Date check) {
        String checkerino = dateToString(check);

        for (Map.Entry<Date, Event> entry : dateEventsMap.entrySet()) {
            Date date = entry.getKey();
            String compare = dateToString(date);

            if (checkerino.equals(compare)) {
                return entry.getValue();
            }
        }

        return null;
    }

    private String dateToString(Date check) {
        int fromDay = check.getDate();
        int fromMonth = check.getMonth();

        return String.format("%s%s", fromMonth, fromDay);
    }

    private AlertDialog showNoEventsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("No events found for user")
                .setPositiveButton("Alrighty then", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        dialog.dismiss();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    private Observable<List<Event>> fetchEventsForUser() {
        return rest.getEventsForUser(userId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem menuUsername = menu.findItem(R.id.username);

        if (menuUsername == null) {
            return true;
        }
        menuUsername.setTitle(username);

        return true;
    }

    private void refreshNotOnClick() {
            fetchEventsForUser()
                    .doOnError(new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            showNoEventsDialog();
                        }
                    })
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(new Action1<List<Event>>() {
                        @Override
                        public void call(List<Event> events) {
                            if (events.size() < 1) {
                                showNoEventsDialog();
                                return;
                            }

                            setupCalendar(caldroidFragment, events);
                        }
                    });

    }

    public void refresh(MenuItem menuItem) {
        refreshNotOnClick();
    }
}
