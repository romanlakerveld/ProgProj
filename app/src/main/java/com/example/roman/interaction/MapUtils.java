package com.example.roman.interaction;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.math.BigDecimal;

import static java.lang.Math.round;

/**
 * Helper class for getting coordinates from the google map.
 */

class MapUtils {

    /**
     * Takes a GoogleMap object and extracts the coordinates of the current projection
     * @param   map GoogleMaps object to be examined
     * @return      String of west south east and north coordinates
     */
    static String getMapCoords(GoogleMap map) {

        //Get bounds of current screen. Northeastern corner and southwestern corner.
        LatLngBounds lngBounds = map.getProjection().getVisibleRegion().latLngBounds;
        LatLng northEast = lngBounds.northeast;
        LatLng southWest = lngBounds.southwest;

        // Get coordinate strings from corners
        String north = String.valueOf(round(northEast.latitude, 2, BigDecimal.ROUND_HALF_UP));
        String east = String.valueOf(round(northEast.longitude, 2, BigDecimal.ROUND_HALF_UP));
        String south = String.valueOf(round(southWest.latitude, 2, BigDecimal.ROUND_HALF_UP));
        String west = String.valueOf(round(southWest.longitude, 2, BigDecimal.ROUND_HALF_UP));

        // Concatenate string to form easy extra in format: west, south, east, north
        return west + "," + south + "," + east + "," + north;
    }

    /**
     * Rounds a double to a specified precision
     *
     * @param unrounded     Double to be rounded
     * @param precision     Precision desired
     * @param roundingMode  How the double should be rounded
     * @return              Return the rounded double
     */
    private static double round(double unrounded, int precision, int roundingMode)
    {
        BigDecimal bd = new BigDecimal(unrounded);
        BigDecimal rounded = bd.setScale(precision, roundingMode);
        return rounded.doubleValue();
    }

}
