package com.example.acararev9;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MapView mapView;
    private com.mapbox.mapboxsdk.maps.MapboxMap mapboxMap;
    private Button btnGoToLocation, btnZoomIn, btnZoomOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this);
        setContentView(R.layout.activity_main);

        mapView = findViewById(R.id.mapView);
        btnGoToLocation = findViewById(R.id.btnGoToLocation);
        btnZoomIn = findViewById(R.id.btnZoomIn);
        btnZoomOut = findViewById(R.id.btnZoomOut);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(map -> {
            mapboxMap = map;
            String tmsUrl = "https://mt1.google.com/vt/lyrs=y&x={x}&y={y}&z={z}";
            String styleJson = "{\n" +
                    "  \"version\": 8,\n" +
                    "  \"sources\": {\n" +
                    "    \"tms-tiles\": {\n" +
                    "      \"type\": \"raster\",\n" +
                    "      \"tiles\": [\"" + tmsUrl + "\"],\n" +
                    "      \"tileSize\": 256\n" +
                    "    }\n" +
                    "  },\n" +
                    "  \"layers\": [\n" +
                    "    {\n" +
                    "      \"id\": \"tms-tiles\",\n" +
                    "      \"type\": \"raster\",\n" +
                    "      \"source\": \"tms-tiles\",\n" +
                    "      \"minzoom\": 0,\n" +
                    "      \"maxzoom\": 22\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";

            mapboxMap.setStyle(new Style.Builder().fromJson(styleJson)
                            .withImage("marker-icon-1", getResources().getDrawable(R.drawable.baseline_pin_drop_24))
                            .withImage("marker-icon-2", getResources().getDrawable(R.drawable.baseline_pin_drop_24))
                            .withImage("marker-icon-3", getResources().getDrawable(R.drawable.baseline_pin_drop_24)),
                    style -> {
                        // Add markers
                        List<Feature> features = new ArrayList<>();
                        LatLng[] locations = {
                                new LatLng(-7.750943085375556, 110.49123396318615),  // Candi Prambanan
                                new LatLng(-7.76118455123211, 110.44745064013806),  // Candi Sambisari
                                new LatLng(-7.760747194995613, 110.47329644364426)   // Candi Sari
                        };

                        for (int i = 0; i < locations.length; i++) {
                            Feature feature = Feature.fromGeometry(Point.fromLngLat(
                                    locations[i].getLongitude(), locations[i].getLatitude()));
                            feature.addStringProperty("icon-id", "marker-icon-" + (i + 1));
                            features.add(feature);
                        }

                        GeoJsonSource geoJsonSource = new GeoJsonSource("marker-source", FeatureCollection.fromFeatures(features));
                        style.addSource(geoJsonSource);

                        SymbolLayer symbolLayer = new SymbolLayer("marker-layer", "marker-source")
                                .withProperties(
                                        PropertyFactory.iconImage("{icon-id}"),
                                        PropertyFactory.iconAllowOverlap(true),
                                        PropertyFactory.iconIgnorePlacement(true)
                                );
                        style.addLayer(symbolLayer);

                        // Set initial camera position
                        mapboxMap.setCameraPosition(new CameraPosition.Builder()
                                .target(locations[0])
                                .zoom(15.0)
                                .build());
                    });

            // Go to X Y button functionality
            btnGoToLocation.setOnClickListener(v -> {
                LatLng customLocation = new LatLng(-8.450, 115.260); // Custom location
                mapboxMap.animateCamera(com.mapbox.mapboxsdk.camera.CameraUpdateFactory.newLatLngZoom(customLocation, 14.0));
            });

            // Zoom in and out button functionality
            btnZoomIn.setOnClickListener(v -> mapboxMap.animateCamera(com.mapbox.mapboxsdk.camera.CameraUpdateFactory.zoomIn()));
            btnZoomOut.setOnClickListener(v -> mapboxMap.animateCamera(com.mapbox.mapboxsdk.camera.CameraUpdateFactory.zoomOut()));
        });
    }

    @Override
    protected void onStart() { super.onStart(); mapView.onStart(); }
    @Override
    protected void onResume() { super.onResume(); mapView.onResume(); }
    @Override
    protected void onPause() { super.onPause(); mapView.onPause(); }
    @Override
    protected void onStop() { super.onStop(); mapView.onStop(); }
    @Override
    public void onLowMemory() { super.onLowMemory(); mapView.onLowMemory(); }
    @Override
    protected void onDestroy() { super.onDestroy(); mapView.onDestroy(); }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
