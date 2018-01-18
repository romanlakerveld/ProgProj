package com.example.roman.interaction;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

/**
 * Created by roman on 18/01/2018.
 */

public class MapUtils {

    public static String getMapCoords(GoogleMap map) {

        //Get bounds of current screen. Northeastern corner and southwestern corner.
        LatLngBounds lngBounds = map.getProjection().getVisibleRegion().latLngBounds;
        LatLng northEast = lngBounds.northeast;
        LatLng southWest = lngBounds.southwest;

        // Get coordinate strings from corners
        String north = String.valueOf(northEast.latitude);
        String east = String.valueOf(northEast.longitude);
        String south = String.valueOf(southWest.latitude);
        String west = String.valueOf(southWest.longitude);

        // Concatenate string to form easy extra in format: west, south, east, north
        return west + "," + south + "," + east + "," + north;
    }

}
