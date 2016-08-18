package com.onegorilla.calendarandroid.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.onegorilla.calendarandroid.R;
import com.onegorilla.calendarandroid.model.Event;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.onegorilla.calendarandroid.helper.QuickLog.qLog;

/**
 * Created by frodrok on 2016-08-17.
 */
public class EventFragment extends Fragment {

    private TextView title;
    private TextView to;
    private TextView from;
    private TextView color;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflater.inflate(R.layout.fragment_event, container);

        title = (TextView) container.findViewById(R.id.title);
        from = (TextView) container.findViewById(R.id.from);
        to = (TextView) container.findViewById(R.id.to);
        color = (TextView) container.findViewById(R.id.color);

        title.setText("");
        from.setText("");
        color.setText("");
        to.setText("");

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public boolean drawEvent(Event event) {

        final Date fromDate = new Date(event.getFrom());

        if (event.getTo() != null) {
            final Date toDate = new Date(event.getTo());
            to.setText(toDate.toString());
        }

        title.setText(event.getTitle());
        from.setText(fromDate.toString());
        color.setText(event.getColor());

        return true;
    }
}
