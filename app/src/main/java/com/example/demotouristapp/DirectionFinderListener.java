package com.example.demotouristapp;

import java.util.List;

public interface DirectionFinderListener {
  void onDirectionFinderStart();

  void onDirectionFinderSuccess(List<Route> route);
}
