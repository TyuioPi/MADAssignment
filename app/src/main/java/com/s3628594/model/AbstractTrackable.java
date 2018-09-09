package com.s3628594.model;

import com.s3628594.model.interfaces.Trackable;

public abstract class AbstractTrackable implements Trackable {

    private static int idCounter = 0;
    private int id;
    protected String name;
    protected String description;
    String url;
    protected String category;

    AbstractTrackable(String name, String description, String url,
                             String category) {
        id = setTrackableId();
        this.name = name;
        this.description = description;
        this.url = url;
        this.category = category;
    }

    @Override
    public int setTrackableId() {
        idCounter++;
        id = idCounter;
        return id;
    }

    public int getTrackableId() {
        return id;
    }

    public abstract String getTrackableName();

    public abstract String getTrackableDesc();

    public abstract String getTrackableUrl();

    public abstract String getTrackableCategory();
}
