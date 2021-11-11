package ca.bcit.androidProject;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BaseState {
    @SerializedName("states")
    @Expose
    private ArrayList<States> states = new ArrayList<>();

    public ArrayList<States> getStates() {
        return states;
    }

    public void setStates(ArrayList<States> states) {
        this.states = states;
    }
}
