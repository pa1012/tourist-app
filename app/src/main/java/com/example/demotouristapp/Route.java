package com.example.demotouristapp;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Route {
  Distance distance;
  Duration duration;
  public LatLng endLocation;
  LatLng startLocation;

  public List<LatLng> points;
}
