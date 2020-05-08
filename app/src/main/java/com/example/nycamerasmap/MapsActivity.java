package com.example.nycamerasmap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Random;

import static android.graphics.Typeface.BOLD;
import static android.graphics.Typeface.ITALIC;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.github.nkzawa.emitter.Emitter;

import org.json.JSONException;
import org.json.JSONObject;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    IconGenerator iconFactory,iconFactory2,iconFactory3;
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("");                                                                //TODO uzupełnić link do socket
        } catch (URISyntaxException e) {}
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSocket.on("new message", onNewMessage);                                                  //stan nowej wiadomosci
        mSocket.connect();                                                                              //Podłączenie socketa
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        //mMap.setOnInfoWindowClickListener(RegActivity.this);


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                //demoTask();
                demoJSON();
                //Toast.makeText(MapsActivity.this, "update", Toast.LENGTH_SHORT).show();
                handler.postDelayed(this, 5000);
            }
        }, 5000);
    }

    private void demoJSON() {
                                                                                                            //todo stworzyc obiekt jsona do testów?
    }
    public int number2CAM1,number9CAM2,number9CAM1;
    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {

            MapsActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String cameraId;
                    String crowdOfCars;
                    try {
                        cameraId = data.getString("camId");
                        crowdOfCars = data.getString("crowd");
                    } catch (JSONException e) {
                        return;
                    }

                    // add the message to view
                    showStatusOnMap(cameraId, crowdOfCars);
                }

                private void showStatusOnMap(String cameraId, String numberOfCars) {
                    mMap.clear();

                    int number = Integer.parseInt(numberOfCars);
                    LatLng cameraLocation;
                    if(cameraId.equals("2CAM1"))
                    {
                        number2CAM1 = number;
                    }
                    else if (cameraId.equals("9CAM2"))
                    {
                        number9CAM2 = number;
                    }
                    else if (cameraId.equals("9CAM1"))
                    {
                        number9CAM1 = number;
                    }
                    iconFactory.setStyle(getMyColor(number2CAM1));
                    cameraLocation = new LatLng(40.78, -73.85);
                    addIcon(iconFactory, number2CAM1+"%", cameraLocation);

                    iconFactory2.setStyle(getMyColor(number9CAM2));
                    cameraLocation = new LatLng(40.75, -73.8);
                    addIcon(iconFactory2, number9CAM2+"%", cameraLocation);

                    cameraLocation = new LatLng(40.73, -74);
                    iconFactory3.setStyle(getMyColor(number9CAM1));
                    addIcon(iconFactory3, number9CAM1+"%", cameraLocation);
                }
            });
        }
    };
    public void demoTask()
    {
        Random r = new Random();
        int number = r.nextInt(100);
        int number2 = r.nextInt(100);
        int number3 = r.nextInt(100);

        mMap.clear();

        iconFactory.setStyle(getMyColor(number));
        LatLng cameraLocation = new LatLng(40.78, -73.85);
        addIcon(iconFactory, number+"%", cameraLocation);

        iconFactory2.setStyle(getMyColor(number2));
        cameraLocation = new LatLng(40.75, -73.8);
        addIcon(iconFactory2, number2+"%", cameraLocation);

        cameraLocation = new LatLng(40.73, -74);
        iconFactory3.setStyle(getMyColor(number3));
        addIcon(iconFactory3, number3+"%", cameraLocation);

    }
    public int getMyColor(int number)
    {
        if(number<=50)
        {
            return 5;
            //zielony 5
        }
        else if (number>50 & number<80)
        {
            return 7;
            //pomaranc 7
        }
        else if (number>=80)
        {
            return 3;
            //czerwony 3
        }
        else return 1;//default

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
        ArrayList<LatLng> polygonPointsList = new ArrayList<>();
        // Add a marker in Sydney and move the camera
        LatLng cameraLocation = new LatLng(40.78, -73.85);
        polygonPointsList.add(cameraLocation);
        iconFactory = new IconGenerator(this);
        iconFactory.setStyle(IconGenerator.STYLE_RED);
        addIcon(iconFactory, "Loading", cameraLocation);

        cameraLocation = new LatLng(40.75, -73.8);
        polygonPointsList.add(cameraLocation);
        iconFactory2 = new IconGenerator(this);
        iconFactory2.setStyle(IconGenerator.STYLE_GREEN);
        addIcon(iconFactory2, "Loading", cameraLocation);


        cameraLocation = new LatLng(40.73, -74);
        polygonPointsList.add(cameraLocation);
        iconFactory3 = new IconGenerator(this);
        iconFactory3.setStyle(IconGenerator.STYLE_ORANGE);
        addIcon(iconFactory3, "Loading", cameraLocation);
        LatLng triangulatedCenterLat = getPolygonCenterPoint(polygonPointsList);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(triangulatedCenterLat, 11f));
         /*mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {/do odczytywanie współrzędnych z mapy aby nie strzelac na ślepo
            @Override
            public void onMapClick(LatLng latLng) {

                Toast.makeText(
                        MapsActivity.this,
                        "Lat : " + latLng.latitude + " , "
                                + "Long : " + latLng.longitude,
                        Toast.LENGTH_LONG).show();

            }
        });*/
    }
    private LatLng getPolygonCenterPoint(ArrayList<LatLng> polygonPointsList){
        LatLng centerLatLng = null;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for(int i = 0 ; i < polygonPointsList.size() ; i++)
        {
            builder.include(polygonPointsList.get(i));
        }
        LatLngBounds bounds = builder.build();
        centerLatLng =  bounds.getCenter();

        return centerLatLng;
    }

    private void addIcon(IconGenerator iconFactory, CharSequence text, LatLng position) {
        MarkerOptions markerOptions = new MarkerOptions().
                icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(text))).
                position(position).
                anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());

        mMap.addMarker(markerOptions);
    }

    private CharSequence makeCharSequence() {
        String prefix = "Mixing ";
        String suffix = "different fonts";
        String sequence = prefix + suffix;
        SpannableStringBuilder ssb = new SpannableStringBuilder(sequence);
        ssb.setSpan(new StyleSpan(ITALIC), 0, prefix.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(new StyleSpan(BOLD), prefix.length(), sequence.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ssb;
    }

}
