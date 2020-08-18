package com.example.demotouristapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static com.example.demotouristapp.GoogleMapHelper.buildCameraUpdate;
import static com.example.demotouristapp.GoogleMapHelper.getDefaultPolyLines;
import static com.example.demotouristapp.GoogleMapHelper.getDottedPolylines;

//import com.afollestad.materialdialogs.MaterialDialog;

//import com.afollestad.materialdialogs.MaterialDialog;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, DirectionFinderListener {

  private enum PolylineStyle {
    DOTTED,
    PLAIN
  }

  private static final String[] POLYLINE_STYLE_OPTIONS = new String[]{
          "PLAIN",
          "DOTTED"
  };

  private PolylineStyle polylineStyle = PolylineStyle.PLAIN;

  private static final int REQ_PERMISSION = 1 ;
  private GoogleMap mMap;
  private Landmark mLandmark;
  private Marker mMarker;
  private TextToSpeech mText2Speech;
  private boolean mIsText2SpeechReady = false;
  private Polyline polyline;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_maps);
    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
            .findFragmentById(R.id.map);
    mapFragment.getMapAsync(this);

    loadData();
    initComponents();


  }

  private void initComponents() {
    mText2Speech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
      @Override
      public void onInit(int status) {
        mIsText2SpeechReady = true;
      }
    });
   // origin =
  }

  private void loadData() {
    Intent intent = getIntent();
    mLandmark = new Landmark(
            intent.getStringExtra("name"),
            intent.getStringExtra("description"),
            intent.getIntExtra("logoid", 0),
            new LatLng(intent.getDoubleExtra("lat", 0),
                    intent.getDoubleExtra("long", 0))
    );
  }

  @Override
  public void onMapReady(GoogleMap googleMap) {
    mMap = googleMap;
    /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      // TODO: Consider calling
      //    ActivityCompat#requestPermissions
      // here to request the missing permissions, and then overriding
      //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
      //                                          int[] grantResults)
      // to handle the case where the user grants the permission. See the documentation
      // for ActivityCompat#requestPermissions for more details.
      askPermission();
      //return;
    }
*/
    if(checkPermission())
      mMap.setMyLocationEnabled(true);
    else askPermission();

    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
      @Override
      public boolean onMarkerClick(Marker marker) {
        if (mIsText2SpeechReady)
        {
          mText2Speech.speak(mLandmark.getDescription(),
                  TextToSpeech.QUEUE_FLUSH, null);
          Toast.makeText(getApplicationContext(),
                  mLandmark.getDescription(),
                  Toast.LENGTH_SHORT
          ).show();
        }
        return false;
      }
    });
    displayMarkers();
  }

  private void askPermission() {

    ActivityCompat.requestPermissions(
            this,
            new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
            REQ_PERMISSION
    );
  }

  private boolean checkPermission() {

    // Ask for permission if it wasn't granted yet
    return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED );
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    switch ( requestCode ) {
      case REQ_PERMISSION: {
        if ( grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
          // Permission granted
          if(checkPermission())
            mMap.setMyLocationEnabled(true);

        } else {
          // Permission denied

        }
        break;
      }
    }
  }


  private void displayMarkers() {
    Bitmap bmp = BitmapFactory.decodeResource(getResources(), mLandmark.getLogoID());
    bmp = Bitmap.createScaledBitmap(bmp, bmp.getWidth()/4, bmp.getHeight()/4, false);
    BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bmp);
    mMarker = mMap.addMarker(new MarkerOptions()
            .position(mLandmark.getLatlong())
            .title(mLandmark.getName())
            .snippet(mLandmark.getDescription())
            .icon(bitmapDescriptor)
    );

    mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
    CameraPosition cameraPosition = new CameraPosition.Builder()
            .target(mLandmark.getLatlong())     // Sets the center of the map to Mountain View
            .zoom(15)                           // Sets the zoom
            .bearing(90)                        // Sets the orientation of the camera to east
            .tilt(30)                           // Sets the tilt of the camera to 30 degrees
            .build();
    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
  }

  public void btn_direct_onclick(View view) {
   /* String msg = "" +
            "Cài đặt chức năng khi nhấn vào button Direct, " +
            "vẽ một polygon để chỉ đường từ vị trí hiện tại tới vị trí mLandmark\n" +
            "Gợi ý:\n" +
            "* dùng AsyncTask để call API chỉ đường của google (đăng ký API key nếu cần)\n"+
            "* path = mMap.addPolygon()\n" +
            "* khi cần xóa polygon: path.remove()";

    Toast.makeText(this, msg,
            Toast.LENGTH_LONG).show();*/


   String origin = String.valueOf(mMap.getMyLocation().getLatitude()) + ',' + String.valueOf(mMap.getMyLocation().getLongitude());

   String destination = String.valueOf(mLandmark.getLat()) + ',' + String.valueOf(mLandmark.getLong());
   fetchDirections(origin, destination);
  }

  private void fetchDirections(String origin, String destination) {
    try {
      new DirectionFinder(this, origin, destination).execute();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }


  @Override
  public void onDirectionFinderStart() {

    Toast.makeText(this, "Fetching Directions...",Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onDirectionFinderSuccess(List<Route> routes) {

    if (!routes.isEmpty() && polyline != null) polyline.remove();
    try {
      for (Route route : routes) {
        PolylineOptions polylineOptions = getDefaultPolyLines(route.points);
        if (polylineStyle == PolylineStyle.DOTTED)
          polylineOptions = getDottedPolylines(route.points);
        polyline = mMap.addPolyline(polylineOptions);
      }
    } catch (Exception e) {
      Toast.makeText(this, "Error occurred on finding the directions...", Toast.LENGTH_SHORT).show();
    }
    mMap.animateCamera(buildCameraUpdate(routes.get(0).endLocation), 10, null);
  }

}
