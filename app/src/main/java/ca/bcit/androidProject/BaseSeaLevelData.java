package ca.bcit.androidProject;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;

public class BaseSeaLevelData {

    @SerializedName("seaLevelData")
    @Expose
    private ArrayList<SeaLevelData> seaLevelData = new ArrayList<>();

    public ArrayList<SeaLevelData> getSeaLevelData() {
        return seaLevelData;
    }

    public void setSeaLevelData(ArrayList<SeaLevelData> seaLevelData) {
        this.seaLevelData = seaLevelData;
    }


}
