package com.example.demotouristapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.demotouristapp.R;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
  private GridView _gridView;
  private GridViewArrayAdapter _adapter;
  private ArrayList<Landmark> _landmarks;

  private GridView.OnItemClickListener _gridViewItemOnClick = new GridView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      Intent intent = new Intent(MainActivity.this, MapsActivity.class);

      Landmark lndmk = _landmarks.get(position);
      intent.putExtra("name", lndmk.getName());
      intent.putExtra("description", lndmk.getDescription());
      intent.putExtra("logoid", lndmk.getLogoID());
      intent.putExtra("lat", lndmk.getLatlong().latitude);
      intent.putExtra("long", lndmk.getLatlong().longitude);

      startActivity(intent);
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    loadData();
    initComponents();
  }

  private void loadData() {
    _landmarks = new ArrayList<>();
    Landmark lndmk1 = new Landmark("Bến Nhà Rồng",
            "Nơi Bác Hồ ra đi tìm đường cứu nước năm 1911",
            R.drawable.logo_ben_nha_rong,
            new LatLng(10.768313, 106.706793));
    Landmark lndmk2 = new Landmark("Chợ Bến Thành",
            "Địa danh nổi tiếng qua các thời kỳ của Sài Gòn",
            R.drawable.logo_cho_ben_thanh,
            new LatLng(10.772535, 106.698034));
    Landmark lndmk3 = new Landmark("Nhà thờ Đức Bà",
            "Công trình kiến trúc độc đáo, nét đặc trưng của Sài Gòn",
            R.drawable.logo_nha_tho_duc_ba,
            new LatLng(10.779742, 106.699188));
    _landmarks.add(lndmk1);
    _landmarks.add(lndmk2);
    _landmarks.add(lndmk3);
  }

  private void initComponents() {
    _gridView = findViewById(R.id.gridview_places);
    _adapter = new GridViewArrayAdapter(this, R.layout.gridview_place_item, _landmarks);
    _gridView.setAdapter(_adapter);
    _gridView.setOnItemClickListener(_gridViewItemOnClick);
  }
}
