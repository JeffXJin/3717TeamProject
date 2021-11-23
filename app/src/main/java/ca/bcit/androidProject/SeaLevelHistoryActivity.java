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
import java.util.Locale;

/*
Activity that shows how the global mean sea level has changed through the years.
 */

public class SeaLevelHistoryActivity extends AppCompatActivity {
    // The base year
    private static final int LOWEST_YEAR = 1900;
    // The final year in our data set
    private static final int HIGHEST_YEAR = 2013;
    private static final int YEAR_RANGE = HIGHEST_YEAR - LOWEST_YEAR;
    // The offset that makes the global mean sea level 0 for year 1900
    private final double OFFSET = 130.1;
    // The source of our data
    private final String SERVICE_URL = "https://pkgstore.datahub.io/core/sea-level-rise/csiro_recons_gmsl_yr_2015_json/data/7a914a104e4360e3e364b111ed4aca40/csiro_recons_gmsl_yr_2015_json.json";
    private int selectedYear = LOWEST_YEAR;
    // Display the sea level up to one decimal place
    private final String SEA_LEVEL_TEXTVIEW_FORMAT = "%.1f";


    private SeekBar yearSeekBar;
    private TextView yearTextView;
    private RequestQueue requestQueue;
    private SeaLevelBar seaLevelBar;

    private ArrayList<SeaLevelData> seaLevelDataList = new ArrayList<>();
    private HashMap<Integer, Double> seaLevelDataMap = new HashMap<>();

    /**
     * Creates the activity, sets the seek bar listener
     * @param savedInstanceState
     */
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
            /**
             * Gets the value of the seek bar, converts it into a year, gets the gmsl from seaLevelDataMap,
             * and sends the gmsl to the SeaLevelBar view
             * @param seekBar the SeekBar
             * @param i an integer
             * @param b a boolean
             */
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                selectedYear = i + LOWEST_YEAR;
                yearTextView.setText(Integer.toString(selectedYear));
                if (seaLevelDataMap.size() != 0) {
                    double gmsl = seaLevelDataMap.get(selectedYear) + OFFSET;
                    testView.setText(String.format(Locale.CANADA, SEA_LEVEL_TEXTVIEW_FORMAT, gmsl));
                    seaLevelBar.setCurrentHeight((float) gmsl);
                }
            }

            /**
             * Unused
             * @param seekBar a SeekBar
             */
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //yearTextView.setText("Hello world");
            }

            /**
             * Unused
             * @param seekBar a SeekBar
             */
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
        getSupportActionBar().setTitle(getResources().getString(R.string.history_action_bar_text));
    }

    /**
     * Parses the data into an array list of SeaLevelData objects, then calls a helper method
     * to enter the data in a hash map
     */
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

    /*
    Creates a hash map of year (Integer) keys with global mean sea level (Double) values
     */
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
