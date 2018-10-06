package com.s3628594.model;

import java.util.List;

public class Tracking {

    private static int idCounter = 0;
    private String trackingId;
    private int trackableId;
    private String title;
    private String startTime;
    private String endTime;
    private String meetTime;
    private String currLoc;
    private String meetLoc;
    private List<TrackingService.TrackingInfo> routeInfo;
    private boolean reminder;



    public Tracking(int trackableId, String title, String startTime, String endTime,
                    String meetTime, String currLoc, String meetLoc, List<TrackingService.TrackingInfo> routeInfo
    , boolean reminder) {
        this.trackingId = setTrackingId();
        this.trackableId = trackableId;
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.meetTime = meetTime;
        this.currLoc = currLoc;
        this.meetLoc = meetLoc;
        this.routeInfo = routeInfo;
        this.reminder = reminder;

    }

    public void setRouteInfo(List<TrackingService.TrackingInfo> routeInfo) {
        this.routeInfo = routeInfo;
    }

    public String toString(){
        return String.format("%s %s %s %s", this.title, this.startTime, this.endTime, this.meetLoc);
    }

    private String setTrackingId() {
        idCounter++;
        trackableId = idCounter;
        return Integer.toString(trackableId);
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setMeetTime(String meetTime) {
        this.meetTime = meetTime;
    }

    public String getMeetTime() {
        return meetTime;
    }

    public String getMeetLoc() {
        return meetLoc;
    }

    public String getCurrLoc() {
        return currLoc;
    }

    public List<TrackingService.TrackingInfo> getRouteInfo() { return routeInfo; }

    public boolean isReminder() {
        return reminder;
    }

    public void setReminder(boolean reminder) {
        this.reminder = reminder;
    }
}
