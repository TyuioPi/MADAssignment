package com.s3628594.model;

public class FoodTruck extends AbstractTrackable {

    FoodTruck(String name, String description, String url, String category) {
        super(name, description, url, category);
    }

    @Override
    public String getTrackableName() {
        return name;
    }

    @Override
    public String getTrackableDesc() {
        return description;
    }

    @Override
    public String getTrackableUrl() {
        return url;
    }

    @Override
    public String getTrackableCategory() {
        return category;
    }
}
