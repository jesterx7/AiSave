package com.example.see.aisafe;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class SektorGMap extends Fragment implements OnMapReadyCallback{
    View myView;
    GoogleMap mMap;
    EditText edtLoc;
    Button btnSearchLocation;
    MapView map;

    private String lokasiSektor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.gps_sektor, container, false);

        edtLoc = myView.findViewById(R.id.edtLokasi);
        btnSearchLocation = myView.findViewById(R.id.btnSearchLocation);

        btnSearchLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findOnMap(edtLoc.getText().toString());
            }
        });

        lokasiSektor = getArguments().getString("lokasiSektor");

        return myView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        map = myView.findViewById(R.id.mapSektor);
        if (map != null) {
            map.onCreate(null);
            map.onResume();
            map.getMapAsync(this);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        mMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        findOnMap(lokasiSektor);
    }

    public void goToLoc(double latitude, double longtitude, int zoom, String location) {
        LatLng latLng = new LatLng(latitude, longtitude);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        mMap.addMarker(new MarkerOptions().position(latLng).title(location));
        mMap.moveCamera(update);
    }

    public void findOnMap(String location) {
        Geocoder geocoder = new Geocoder(getContext());
        try {
            List<Address> myList = geocoder.getFromLocationName(location, 1);
            if (!myList.isEmpty()) {
                Address address = myList.get(0);
                String locality = address.getCountryName();
                Toast.makeText(getContext(), locality, Toast.LENGTH_SHORT).show();
                double lat = address.getLatitude();
                double longs = address.getLongitude();
                goToLoc(lat, longs, 30, location);
            } else {
                Toast.makeText(getContext(), "Tidak Ditemukan", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
