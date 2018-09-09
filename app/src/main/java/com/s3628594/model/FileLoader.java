package com.s3628594.model;

import android.content.Context;

import com.s3628594.geotracking.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileLoader {

    // Load food_truck_data.txt and initialize our trackable
    public void loadFoodTruckFile(Context context) {

        try {
            InputStream inputStream = context.getResources().openRawResource(R.raw.food_truck_data);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            boolean hasNextLine = true;
            while (hasNextLine) {
                String line = bufferedReader.readLine();

                if (line == null) {
                    hasNextLine = false;
                } else {
                    // Assuming the data of the food truck is always in correct format
                    String[] foodTruckDetails = line.split("((,\")|(\",\"))|(\")");
                    TrackableImplementation.getSingletonInstance().addFoodTruck(
                            new FoodTruck(foodTruckDetails[1], foodTruckDetails[2], foodTruckDetails[3], foodTruckDetails[4]));
                    TrackableImplementation.getSingletonInstance().setCategoryList(foodTruckDetails[4]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
