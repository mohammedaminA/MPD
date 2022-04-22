package com.example.abdullah_mohammedaminsultan_s1906568;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.abdullah_mohammedaminsultan_s1906568.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    Intent i;
    String lat, lon, tit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        i = getIntent();
        lat = i.getStringExtra("lat");
        lon = i.getStringExtra("lon");
        tit = i.getStringExtra("t");
        Log.e("TAG", lat+tit+lon);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Log.e("TAG2", lat+tit+lon);
        LatLng mp = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
        mMap.addMarker(new MarkerOptions().position(mp).title(tit));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mp));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mp, 15));
    }
}