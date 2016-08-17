package com.onegorilla.calendarandroid.model;

/**
 * Created by frodrok on 2016-08-17.
 */
public final class Event {

    /*"id": 16,
            "title": "Redovisning Android",
            "from": 1471599000000,
            "to": 0,
            "groupId": 2,
            "background": false,
            "color": null*/

    private final Long id;
    private final String title;
    private final Long from;
    private final Long to;
    private final Integer groupId;
    private final boolean background;
    private final String color;

    public Event(Long id, String title, Long from, Long to, Integer groupId, boolean background, String color) {
        this.id = id;
        this.title = title;
        this.from = from;
        this.to = to;
        this.groupId = groupId;
        this.background = background;
        this.color = color;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Long getFrom() {
        return from;
    }

    public Long getTo() {
        return to;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public boolean isBackground() {
        return background;
    }

    public String getColor() {
        return color;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", from=" + from +
                ", to=" + to +
                ", groupId=" + groupId +
                ", background=" + background +
                ", color='" + color + '\'' +
                '}';
    }
}
