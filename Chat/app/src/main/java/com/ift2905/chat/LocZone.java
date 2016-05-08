package com.ift2905.chat;

/**
 * Created by Poste on 20/04/2016.
 */
public class LocZone {

    private static double[] NW = {-73.932804, 45.695730};
    private static double[] SW = {-73.932804, 45.396623};
    private static double[] SE = {-73.457645, 45.396623};
    private static double[] NE = {-73.457645, 45.695730};

    private static int splitX = 20;
    private static int splitY = 20;

    private static int currentZone = -1;

    private static final String dumpZone = "Purgatoire";

    public static String zoneNames = "";


    public static int getCurrentZone() {
        return LocZone.currentZone;
    }

    public static void setCurrentZone(int zone) {
        LocZone.currentZone = zone;
        LocZone.zoneNames = zone + "";
    }

    public static String getDumpZone() {
        return LocZone.dumpZone;
    }

    public static String getCurrentZoneName() {
        if (LocZone.getCurrentZone() == 1) return LocZone.dumpZone;
        return LocZone.zoneNames + "";
    }

    public static int findCurrentZone(double longitude, double latitude) {

        if (latitude > LocZone.NW[1] || latitude < LocZone.SE[1] ||
                longitude < LocZone.NW[0] || longitude > SE[0]) {
            //Ici on n'est pas dans la zone
            return -1;
        }

        int currentZone = 0;
        currentZone += Math.floor((longitude - LocZone.NW[0]) /
                (((LocZone.NE[0] - LocZone.NW[0]) / LocZone.splitX)));
        currentZone += LocZone.splitX * Math.floor(((latitude - LocZone.SW[1]) /
                (((LocZone.NE[1] - LocZone.SW[1]) / LocZone.splitY))));

        return currentZone;
    }

    public static double[][] getZoneCoord(double longitude, double latitude) {
        double pasX = (LocZone.NE[0] - LocZone.NW[0]) / LocZone.splitX;
        double pasY = (LocZone.NE[1] - LocZone.SE[1]) / LocZone.splitY;

        double zx = Math.floor((longitude - LocZone.SW[0]) / pasX);
        double x0 = LocZone.NW[0] + zx * pasX;
        double x1 = x0 + pasX;

        double zy = Math.floor((latitude - LocZone.SW[1]) / pasY);
        double y0 = LocZone.SW[1] + zy * pasY;
        double y1 = y0 + pasY;


        double[][] coord = {{x0, y1}, {x1, y1}, {x0, y0}, {x1, y0}};
        return coord;
    }
}
