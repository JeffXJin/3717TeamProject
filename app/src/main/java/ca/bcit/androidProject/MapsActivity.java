package ca.bcit.androidProject;

import androidx.fragment.app.FragmentActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ca.bcit.androidProject.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.gmap);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng van = new LatLng(49.2578263,-123.1939441);
//        mMap.addMarker(new MarkerOptions().position(van).icon(BitmapDescriptorFactory.fromBitmap(createPureTextIcon("+ 0.8mm"))));

        addPoints();

        mMap.moveCamera(CameraUpdateFactory.newLatLng(van));
    }

    public void addPoints() {
        LatLng[] p = new LatLng[6];
        p[0] = new LatLng(49.232369, -123.206863);
        p[1] = new LatLng(49.266900, -123.262609);
        p[2] = new LatLng(49.191833, -123.207692);
        p[3] = new LatLng(49.149167, -123.197055);
        p[4] = new LatLng(49.270455, -123.182764);
        p[5] = new LatLng(49.333670, -123.172028);

        for (int i = 0; i < 6; i ++) {
            mMap.addMarker(new MarkerOptions().position(p[i]).icon(BitmapDescriptorFactory.fromBitmap(createPureTextIcon("+ 0.8mm"))));
        }
    }

    public void onZoom(View v) {
        if (v.getId() == R.id.btnZoomIn)
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
        else
            mMap.animateCamera(CameraUpdateFactory.zoomOut());
    }

    public Bitmap createPureTextIcon(String text) {

        Paint textPaint = new Paint();

        textPaint.setTextSize(20f);
        textPaint.setColor(Color.RED);
        float textWidth = textPaint.measureText(text);
        float textHeight = textPaint.getTextSize();
        int width = (int) (textWidth * 1.25);
        int height = (int) (textHeight * 1.25);

        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);

        canvas.translate(0, height);


        // Background color
//        canvas.drawColor(Color.BLACK);

//        textPaint = setTextSizeForWidth(textPaint,400,text);
        canvas.drawText(text, 0, -5, textPaint);
        BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(image);
        Bitmap resized = Bitmap.createScaledBitmap(image,width * 5,height * 5,false);
        return resized;
    }
}