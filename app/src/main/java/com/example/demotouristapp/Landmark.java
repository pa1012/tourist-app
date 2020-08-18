package com.example.demotouristapp;
import com.google.android.gms.maps.model.LatLng;

public class Landmark {
  private String _name;
  private String _description;
  private int _logoID;
  private LatLng _latlong;

  public Landmark(String name, String description, int logoID, LatLng latlong) {
    this._name = name;
    this._description = description;
    this._logoID = logoID;
    this._latlong = latlong;
  }

  public String getName() {
    return _name;
  }

  public void setName(String name) {
    this._name = name;
  }

  public String getDescription() {
    return _description;
  }

  public void setDescription(String description) {
    this._description = description;
  }

  public int getLogoID() {
    return _logoID;
  }

  public void setLogoID(int logoID) {
    this._logoID = logoID;
  }

  public LatLng getLatlong() {
    return _latlong;
  }

  public double getLat(){
    return _latlong.latitude;
  }

  public double getLong(){
    return _latlong.longitude;
  }


  public void setLatlong(LatLng latlong) {
    this._latlong = latlong;
  }
}
