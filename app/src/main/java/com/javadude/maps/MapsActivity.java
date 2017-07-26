package com.javadude.maps;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.javadude.maps.R.id.map;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

	private GoogleMap mMap;
	private PermissionManager permissionManager = new PermissionManager();
	private SupportMapFragment mapFragment;
	private BitmapDescriptor redUfo;
	private Marker marker;
	private Polyline route;
    private LatLngBounds.Builder bounds;
    private List<LatLng> latLngs;
    private int padding;

    // save all markers on map
    // https://stackoverflow.com/questions/13692398/remove-a-marker-from-a-googlemap
    private final Map<Integer, Marker> mMarkers = new ConcurrentHashMap<>();

    // saved list of
    private UFOPositionReporter.Stub reporter = new UFOPositionReporter.Stub() {
        @Override
        public void report(List<UFOPosition> ufoPositions) {
            final ArrayList<UFOPosition> ufoPositionList = (ArrayList) ufoPositions;
            final List<Integer> currentUFOShipIDs = new ArrayList<>();

            // perform UFO calculations in a single thread
            runOnUiThread(new Runnable() {
                public void run() {
                    // ensure UFO positions are returned
                    if (ufoPositionList.size() > 0) {
                        // loop over current list of UFO positions
                        for (int i = 0; i < ufoPositionList.size(); i++) {
                            // clear old latlon values
                            latLngs = new ArrayList<>();

                            // get current UFO
                            UFOPosition currentUFO = ufoPositionList.get(i);

                            // save UFO id to list
                            currentUFOShipIDs.add(currentUFO.getShipNumber());

                            // save current UFO position
                            LatLng currentPosition = new LatLng(currentUFO.getLat(), currentUFO.getLon());

                            // save current latlon in list for drawing lines
                            latLngs.add(currentPosition);

                            // try to get current UFO ship marker
                            Marker oldUFO = mMarkers.get(currentUFO.getShipNumber());

                            // check to see if ship already has been seen
                            if (oldUFO != null) {
                                LatLng previousPosition = oldUFO.getPosition();

                                    // save last position in list for drawing lines
                                    latLngs.add(previousPosition);

                                // draw line between both points
                                    route = mMap.addPolyline(new PolylineOptions()
                                            .addAll(latLngs)
                                            .color(Color.RED)
                                            .width(8)
                                    );

                                    // remove old marker
                                    oldUFO.remove();

                                    mMarkers.remove(currentUFO.getShipNumber());
                            }

                            // add UFO marker to map
                            marker = mMap.addMarker(new MarkerOptions()
                                    .anchor(0.5f, 0.5f)
                                    .icon(redUfo)
                                    .title("UFO Ship: " + currentUFO.getShipNumber())
                                    .snippet("Lat/Lng: " + currentPosition.latitude + ", " + currentPosition.longitude)
                                    .position(currentPosition)
                            );

                            // save current position
                            mMarkers.put(currentUFO.getShipNumber(), marker);

                            bounds = bounds.include(currentPosition);
                        }

                        //set bounds with all the map points
                        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), padding));

                        // get ship numbers that weren't returned in the last set
                        List<Integer> mMarkersList = new ArrayList<>(mMarkers.keySet());
                        mMarkersList.removeAll(currentUFOShipIDs);

                        // remove ships that are no longer valid
                        for (int i = 0; i < mMarkersList.size(); i++) {
                            int shipID = mMarkersList.get(i);
                            mMarkers.get(shipID).remove();
                            mMarkers.remove(shipID);
                        }
                    }
                    // there are no more UFO positions, remove all markers
                    else {
                        List<Integer> mMarkersList = new ArrayList<>(mMarkers.keySet());
                        for (int i = 0; i < mMarkersList.size(); i++) {
                            int shipID = mMarkersList.get(i);
                            mMarkers.get(shipID).remove();
                            mMarkers.remove(shipID);
                        }
                    }
                }
            });
        }
    };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);

		// Obtain the SupportMapFragment and get notified when the map is ready to be used.
		mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(map);

		redUfo = BitmapDescriptorFactory.fromResource(R.mipmap.red_ufo);

        padding = (int) getResources().getDimension(R.dimen.padding);
	}

	/**
	 * Manipulates the map once available.
	 */
	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;

		permissionManager.run(enableMyLocationAction);

        bounds = new LatLngBounds.Builder();

        Intent intent = new Intent();
        intent.setClassName("com.javadude.maps", "com.javadude.maps.UFOServiceImpl");
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
	}

    @Override
    protected void onStop() {
        super.onStop();

        // if connection sever it
        if (serviceConnection != null) {
            unbindService(serviceConnection);
        }
    }

    @Override
	protected void onPause() {
		super.onPause();

		if (mMap.isMyLocationEnabled()) {
            //noinspection MissingPermission
            mMap.setMyLocationEnabled(false);
        }
	}

	@Override
	protected void onResume() {
		super.onResume();
		mapFragment.getMapAsync(this);
	}

	private PermissionAction enableMyLocationAction = new PermissionAction(this, null, Manifest.permission.ACCESS_FINE_LOCATION) {
		@Override
		protected void onPermissionGranted() {
			//noinspection MissingPermission
			mMap.setMyLocationEnabled(true);
		}

		@Override
		protected void onPermissionDenied() {
		}
	};

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		permissionManager.handleRequestPermissionsResult(requestCode, permissions, grantResults);
	}

    private UFOService remoteService;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            remoteService = UFOService.Stub.asInterface(service);

            try {
                Log.d("onServiceConnected", "Got here to add service");
                remoteService.add(reporter);
            } catch (RemoteException e) {
                Log.e(getClass().getSimpleName(), "Cannot add reporter", e);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            remoteService = null;
        }
    };
}
