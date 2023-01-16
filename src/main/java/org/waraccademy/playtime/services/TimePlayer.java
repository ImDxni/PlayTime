package org.waraccademy.playtime.services;

import java.sql.Timestamp;

public class TimePlayer {
    private long day,week,month;
    private Timestamp lastSeen;

    public TimePlayer(){}

    public long getDay() {
        return day;
    }

    public long getWeek() {
        return week;
    }

    public long getMonth() {
        return month;
    }

    public long getLastSeen() {
        return System.currentTimeMillis() - lastSeen.getTime();
    }

    public void setDay(long day) {
        this.day = day;
    }

    public void setWeek(long week) {
        this.week = week;
    }

    public void setMonth(long month) {
        this.month = month;
    }

    public void setLastSeen(Timestamp lastSeen) {
        this.lastSeen = lastSeen;
    }
}
