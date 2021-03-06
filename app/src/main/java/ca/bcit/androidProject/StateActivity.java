package ca.bcit.androidProject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;

public class StateActivity extends AppCompatActivity {
    private static final String TAG = StateActivity.class.getSimpleName();
    private RecyclerView _recyclerView;
    private ArrayList<States> _toonsList;
    private static final String SERVICE_URL = "https://sea-level-rise-data.herokuapp.com/api/v1/stations";

    private RecyclerAdapter _recyclerAdapter;
    private RequestQueue _requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state);

        _recyclerView = findViewById(R.id.recycler_view);
        _recyclerView.setHasFixedSize(true);

        StaggeredGridLayoutManager lm = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        _recyclerView.setLayoutManager(lm);

        _toonsList = new ArrayList<States>();

        _requestQueue = Volley.newRequestQueue(this);
        queueParseJSON();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Sea Level Rise Data");

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void queueParseJSON() {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, SERVICE_URL, null,
                new Response.Listener<JSONArray>() {
                    @Override

                    public void onResponse(JSONArray response) {
                        String jsonStr = "{\"states\":" + response.toString() + "}";
                        Gson gson = new Gson();
                        BaseState baseToon = gson.fromJson(jsonStr, BaseState.class);
                        _toonsList = baseToon.getStates();
                        _recyclerAdapter = new RecyclerAdapter(StateActivity.this, _toonsList);
                        _recyclerView.setAdapter(_recyclerAdapter);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }
        );
        _requestQueue.add(request);
    }
}