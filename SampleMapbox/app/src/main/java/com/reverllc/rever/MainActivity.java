package com.reverllc.rever;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.services.android.telemetry.location.LocationEngine;
import com.mapbox.services.android.telemetry.location.LocationEnginePriority;


public class MainActivity extends AppCompatActivity implements LifecycleRegistryOwner {

    private MapView mapView;
    private LifecycleRegistry lifecycleRegistry;
    private LocationLayerPlugin locationLayerPlugin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, "{our access Token}");

        setContentView(R.layout.activity_main);
        lifecycleRegistry = new LifecycleRegistry(this);
        mapView = (MapView) findViewById(R.id.mapview);
        this.mapView.onCreate(savedInstanceState);
        initializeMap();
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        this.mapView.onSaveInstanceState(outState);
    }


    @Override
    public void onStart() {
        super.onStart();
        this.mapView.onStart();
    }

    @Override
    public void onStop() {
        this.mapView.onStop();
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.mapView.onResume();
    }

    @Override
    public void onPause() {
        this.mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mapView.onDestroy();

    }

    @Override
    public void onLowMemory() {
        this.mapView.onLowMemory();
        super.onLowMemory();
    }

    void initializeMap() {
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                mapboxMap.getUiSettings().setCompassEnabled(false);

                LocationEngine locationEngine=getLocationEngine();
                locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
                locationEngine.activate();

                locationLayerPlugin = new LocationLayerPlugin(mapView, mapboxMap, locationEngine);
                locationLayerPlugin.setLinearAnimation(true);
                try {
                    locationLayerPlugin.setLocationLayerEnabled(LocationLayerMode.TRACKING);
                } catch (SecurityException e) {// ignore it
                }
                getLifecycle().addObserver(locationLayerPlugin);
            }
        });
    }

    private LocationEngine getLocationEngine(){
//        return new LostLocationEngine(MainActivity.this);
        return GoogleLocationEngine.getLocationEngine(MainActivity.this);

    }

}
