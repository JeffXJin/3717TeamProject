package ca.bcit.androidProject;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class States {
    @SerializedName("id")
    @Expose
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @SerializedName("name")
    @Expose
    private String stateName;

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    @SerializedName("slr_rate")
    @Expose
    private String slrRate;

    public String getSlrRate() {
        return slrRate;
    }

    public void setSlrRate(String slrRate) {
        this.slrRate = slrRate;
    }

    public States(int id, String firstName, String stateName, String slrRate) {
        this.id = id;
        this.stateName = stateName;
        this.slrRate = slrRate;
    }
}

