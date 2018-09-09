package com.s3628594.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

// Class to handle anything related to a tracking
public class TrackingImplementation {

    private static TrackingImplementation INSTANCE = null;
    private ArrayList<Tracking> trackingList = new ArrayList<>();

    // Our singleton
    private TrackingImplementation() {}

    // Return the singleton
    public static TrackingImplementation getSingletonInstance()
    {
        if (INSTANCE == null) {
            INSTANCE = new TrackingImplementation();
        }
        return INSTANCE;
    }

    public void addTracking(Tracking tracking) {
        trackingList.add(tracking);
    }

    public void removeTracking(Tracking tracking) {
        Iterator<Tracking> it = trackingList.iterator();
        while (it.hasNext()) {
            if (tracking.equals(it.next())) {
                it.remove();
            }
        }
    }

    public void editTrackingTitle(Tracking tracking, String title) {
        tracking.setTitle(title);
    }

    public void editTrackingMeetTime(Tracking tracking, String meetTime) {
        tracking.setMeetTime(meetTime);
    }

    public Tracking getTrackingById(String trackingId) {
        for(Tracking tracking : trackingList) {
            if (tracking.getTrackingId().equals(trackingId)){
                return tracking;
            }
        }
        return null;
    }

    public ArrayList<Tracking> setDateSortedTrackingList() {
        Collections.sort(trackingList, new Comparator<Tracking>() {
            @Override
            public int compare(Tracking t1, Tracking t2) {
                return t1.getStartTime().compareToIgnoreCase(t2.getStartTime());
            }
        });
        return trackingList;
    }

    public ArrayList<Tracking> getTrackingList() {
        return trackingList;
    }
}
