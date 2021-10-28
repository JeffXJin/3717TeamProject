package ca.bcit.androidProject;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SeaLevelData {

    @SerializedName("GMSL")
    @Expose
    private double gmsl;

    public double getGmsl() {
        return gmsl;
    }

    public void setGmsl(double gmsl) {
        this.gmsl = gmsl;
    }

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
