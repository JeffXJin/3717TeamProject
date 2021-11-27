package ca.bcit.androidProject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.Lottie;
import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import ca.bcit.androidProject.databinding.ActivityMapsBinding;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private ArrayList<States> statesList;

    private final static String SERVICE_URL = "https://sea-level-rise-data.herokuapp.com/api/v1/stations";

    private RequestQueue requestQueue;

    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        statesList = new ArrayList<>();

        ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.gmap);

        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        requestQueue = Volley.newRequestQueue(this);
        queueParseJSON();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Sea Level Global Map");

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
    }



    public void onSearch() throws IOException {
        final ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        List<Address> addressList;

        String location;
         int size = statesList.size() /2;
        System.out.println("Areas found: " + statesList.size());
        for (int i = 0; i < size; i++) {
            location = statesList.get(i).getStateName();
            float slr = Float.parseFloat(statesList.get(i).getSlrRate());


            Geocoder geocoder = new Geocoder(this);

            try {
                addressList = geocoder.getFromLocationName(location, 1);
                String locationName = statesList.get(i).getStateName();
                // Check if JSON data location has a valid address
                if (!addressList.isEmpty()) {
                    Address adr = addressList.get(0);

                    LatLng latLng = new LatLng(adr.getLatitude(), adr.getLongitude());

                    if (slr <= 1) {
                         mMap.addMarker(new MarkerOptions().position(latLng)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                 .title(locationName + ": " + slr + "mm")).showInfoWindow();
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                    } else if (slr > 1 && slr <= 2) {
                        mMap.addMarker(new MarkerOptions().position(latLng)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                                .title(locationName + ": " + slr + "mm")).showInfoWindow();
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    } else if (slr > 2) {
                        mMap.addMarker(new MarkerOptions().position(latLng)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                                .title(locationName + ": " + slr + "mm"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        progressBar.setVisibility(View.INVISIBLE);

        TextView textView = findViewById(R.id.loading_text);
        textView.setText("Displaying SLR data for " + statesList.size() + " locations");
    }


    private void queueParseJSON() {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, SERVICE_URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        String jsonStr = "{\"states\":" + response.toString() + "}";
                        Gson gson = new Gson();
                        BaseState baseState = gson.fromJson(jsonStr, BaseState.class);
                        statesList = baseState.getStates();

                        System.out.println("Full list: " + statesList);
                        try {
                            onSearch();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        LatLng van = new LatLng(49.2578263, -123.1939441);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(van));
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    }

    public void onZoom(View v) {
        if (v.getId() == R.id.btnZoomIn)
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
        else
            mMap.animateCamera(CameraUpdateFactory.zoomOut());
    }




    public Bitmap createPureTextIcon(String text, int color) {

        Paint textPaint = new Paint();

        textPaint.setTextSize(20f);
        textPaint.setColor(color);
        float textWidth = textPaint.measureText(text);
        float textHeight = textPaint.getTextSize();
        int width = (int) (textWidth * 1.25);
        int height = (int) (textHeight * 1.25);

        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);

        canvas.translate(0, height);

        canvas.drawText(text, 0, -5, textPaint);
        return Bitmap.createScaledBitmap(image, width * 5, height * 5, false);
    }
}