package ca.bcit.androidProject;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ca.bcit.androidProject.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private ArrayList<States> statesList;

    private final static int RED_ZONE = Color.RED;

    private final static int YELLOW_ZONE = Color.YELLOW;

    private final static int GREEN_ZONE = Color.GREEN;

    private final static String SERVICE_URL = "https://sea-level-rise-data.herokuapp.com/api/v1/stations";

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        statesList = new ArrayList<States>();

        requestQueue = Volley.newRequestQueue(this);
        queueParseJSON();

        ca.bcit.androidProject.databinding.ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.gmap);

        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        LatLng van = new LatLng(49.2578263, -123.1939441);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(van));
    }

    public void onSearch() throws IOException {
        List<Address> addressList;

        String location;

        System.out.println("Areas found: " + statesList.size());
        for (int i = 0; i < statesList.size(); i++) {
            System.out.println("Area names: " + statesList.get(i).getStateName());
            location = statesList.get(i).getStateName();
            float slr = Float.parseFloat(statesList.get(i).getSlrRate());

            Geocoder geocoder = new Geocoder(this);

            try {
                addressList = geocoder.getFromLocationName(location, 1);
                if (!addressList.isEmpty()) {
                    Address adr = addressList.get(0);

                    System.out.println("Address: " + adr);
                    System.out.println("SLR: " + slr);
                    LatLng latLng = new LatLng(adr.getLatitude(), adr.getLongitude());

                    if (slr <= 1) {
                        mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(createPureTextIcon(Float.toString(slr), GREEN_ZONE))));
                    } else if (slr > 1 && slr <= 2) {
                        mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(createPureTextIcon(Float.toString(slr), YELLOW_ZONE))));
                    } else {
                        mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(createPureTextIcon(Float.toString(slr), RED_ZONE))));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
//        String location = statesList.get(1).getStateName();
//        float slr = Float.parseFloat(statesList.get(1).getSlrRate());
//        Geocoder geocoder = new Geocoder(this);
//        addressList = geocoder.getFromLocationName(location, 1);
//        Address adr = addressList.get(0);
//        LatLng latLng = new LatLng(adr.getLatitude(), adr.getLongitude());
//        mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(createPureTextIcon(Float.toString(slr), GREEN_ZONE))));

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