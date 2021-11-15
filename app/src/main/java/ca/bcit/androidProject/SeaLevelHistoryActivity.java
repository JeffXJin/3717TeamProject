package ca.bcit.androidProject;

import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

public class SeaLevelHistoryActivity extends AppCompatActivity {
    private static final int LOWEST_YEAR = 1900;
    private static final int YEAR_RANGE = 2013 - LOWEST_YEAR;
    private final double OFFSET = 130.1;
    private final String SERVICE_URL = "https://pkgstore.datahub.io/core/sea-level-rise/csiro_recons_gmsl_yr_2015_json/data/7a914a104e4360e3e364b111ed4aca40/csiro_recons_gmsl_yr_2015_json.json";
    private int selectedYear = LOWEST_YEAR;


    private SeekBar yearSeekBar;
    private TextView yearTextView;
    private RequestQueue requestQueue;
    private SeaLevelBar seaLevelBar;

    private ArrayList<SeaLevelData> seaLevelDataList = new ArrayList<>();
    private HashMap<Integer, Double> seaLevelDataMap = new HashMap<>();

    private View barGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sea_level_history);

        seaLevelBar = findViewById(R.id.sea_level_bar);
        yearSeekBar = findViewById(R.id.year_seekBar);
        yearTextView = findViewById(R.id.year_textView);
        TextView testView = findViewById(R.id.test_tv);
        yearSeekBar.setMax(YEAR_RANGE);

        requestQueue = Volley.newRequestQueue(this);
        queueParseJSON();


        yearSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                selectedYear = i + LOWEST_YEAR;
                yearTextView.setText(Integer.toString(selectedYear));
                if (seaLevelDataMap.size() != 0) {
                    double gmsl = seaLevelDataMap.get(selectedYear) + OFFSET;
                    testView.setText(String.valueOf(gmsl));
                    seaLevelBar.setCurrentHeight((float) gmsl);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //yearTextView.setText("Hello world");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // sets the title to the toolbar
        getSupportActionBar().setTitle("Sea Level History");
    }

    private void queueParseJSON() {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, SERVICE_URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        String jsonStr = "{\"seaLevelData\":" + response.toString() + "}";
                        Gson gson = new Gson();
                        BaseSeaLevelData baseSeaLevelData = gson.fromJson(jsonStr, BaseSeaLevelData.class);
                        seaLevelDataList = baseSeaLevelData.getSeaLevelData();
                        createMap();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }
        );
        requestQueue.add(request);
    }

    private void createMap() {
        for (SeaLevelData sld: seaLevelDataList) {
            String[] yearArray = sld.getTime().split("-");
            int yearInt = Integer.parseInt(yearArray[0]);
            seaLevelDataMap.put(yearInt, sld.getGmsl());
        }
    }


    // back button function
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}