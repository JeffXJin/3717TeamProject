package ca.bcit.androidProject;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/*
An object representing the global mean sea level and the date when that level was recorded
 */

public class SeaLevelData {

    /** The global mean sea level in millimeters represented as a double */
    @SerializedName("GMSL")
    @Expose
    private double gmsl;

    public double getGmsl() {
        return gmsl;
    }

    public void setGmsl(double gmsl) {
        this.gmsl = gmsl;
    }

    /** The date that the global mean sea level was recorded in YYYY-MM-DD format */
    @SerializedName("Time")
    @Expose
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
