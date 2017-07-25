package com.javadude.maps;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.AsyncTask;
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
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
            final List<Integer> currentUFOShips = new ArrayList<>();

            runOnUiThread(new Runnable() {
                public void run() {
                    // loop over current list of UFO positions
                    for (int i = 0; i < ufoPositionList.size(); i++) {
                        // clear old latlon values
                        latLngs = new ArrayList<>();

                        // get current UFO
                        UFOPosition currentUFO = ufoPositionList.get(i);

                        // save UFO id to list
                        currentUFOShips.add(currentUFO.getShipNumber());

                        // save current UFO position
                        LatLng currentPosition = new LatLng(currentUFO.getLat(), currentUFO.getLon());

                        // save current latlon in list for drawing lines
                        latLngs.add(currentPosition);

                        Log.d("ShipNumber", ""+currentUFO.getShipNumber());

                        // ensure there are current UFOs saved
                        if (mMarkers.size() > 0) {
                            // try to get current UFO ship marker
                            Marker oldUFO = mMarkers.get(currentUFO.getShipNumber());

                            // check to see if ship already has been seen
                            if (oldUFO != null) {
                                LatLng previousPosition = oldUFO.getPosition();

                                // if it has moved then remove old marker
                                // draw line between both points
                                if (previousPosition.latitude != currentUFO.getLat() && previousPosition.longitude != currentUFO.getLon()) {
                                    // save last position latlon in list for drawing lines
                                    latLngs.add(previousPosition);

                                    route = mMap.addPolyline(new PolylineOptions()
                                            .addAll(latLngs)
                                            .color(Color.RED)
                                            .width(8)
                                    );

                                    // remove old marker
                                    oldUFO.remove();

                                    mMarkers.remove(currentUFO.getShipNumber());
                                }
                            }
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

                    // check if UFO was not returned
//                    for (int i = 0; i < mMarkers.size(); i++) {
//                        int ship = mMarkers.get(i);
//                    }
                }
            });
        }
    };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);

		// Obtain the SupportMapFragment and get notified when the map is ready to be used.
		mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

		redUfo = BitmapDescriptorFactory.fromResource(R.mipmap.red_ufo);

        padding = (int) getResources().getDimension(R.dimen.padding);
	}

	/**
	 * Manipulates the map once available.
	 * This callback is triggered when the map is ready to be used.
	 * This is where we can add markers or lines, add listeners or move the camera. In this case,
	 * we just add a marker near Sydney, Australia.
	 * If Google Play services is not installed on the device, the user will be prompted to install
	 * it inside the SupportMapFragment. This method will only be triggered once the user has
	 * installed Google Play services and returned to the app.
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

//    case R.id.action_show_route: {
//        Location myLocation = mMap.getMyLocation();
//        new RouteFetcher().execute(myLocation.getLatitude(), myLocation.getLongitude(), savedPosition.latitude, savedPosition.longitude);
//
//    case R.id.action_remember_location: {
//        Location myLocation = mMap.getMyLocation();
//        savedPosition = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
//
//        if (marker != null) {
//            marker.remove();
//        }
//
//        marker = mMap.addMarker(new MarkerOptions()
//                .anchor(0.5f, 0.5f)
//                .icon(redUfo)
//                .title("Saved Location")
//                .position(savedPosition)
//        );

	private class RouteFetcher extends AsyncTask<Double, Void, List<LatLng>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

            if (route != null) {
                route.remove();
            }
		}

		@Override
		protected void onPostExecute(List<LatLng> latLngs) {
			super.onPostExecute(latLngs);

			route = mMap.addPolyline(new PolylineOptions()
					.addAll(latLngs)
					.color(Color.RED)
					.width(8)
			);
		}

		@Override
		protected List<LatLng> doInBackground(Double... params) {
			double myLat = params[0];
			double myLon = params[1];
			double savedLat = params[2];
			double savedLon = params[3];

			try {
				String uriString = "http://maps.googleapis.com/maps/api/directions/json?mode=walking&origin=" +
                        myLat + ',' + myLon + "&destination=" + savedLat + ',' +
                        savedLon + "&sensor=true";

				URL url = new URL(uriString);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();

				connection.setRequestMethod("GET");
				connection.connect();

				int responseCode = connection.getResponseCode();
				String content = "";

				if (responseCode < 300) {
					InputStream in = connection.getInputStream();
					InputStreamReader isr = new InputStreamReader(in);
					BufferedReader br = new BufferedReader(isr);
					String line;

					while ((line = br.readLine()) != null) {
						content += line + "\n";
					}

					// SHOW THE ROUTE ON THE MAP
					JSONObject object = new JSONObject(content);
					JSONArray steps = object.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONArray("steps");
					List<LatLng> allLatLngs = new ArrayList<>();

					for (int i = 0; i < steps.length(); i++) {
						JSONObject step = steps.getJSONObject(i);
						String points = step.getJSONObject("polyline").getString("points");
						List<LatLng> latLngs = PolyUtil.decode(points);
						allLatLngs.addAll(latLngs);
					}

					return allLatLngs;
				}

				throw new RuntimeException("Could not access routing information. Response from server: " + responseCode);

			} catch (IOException| JSONException e) {
				throw new RuntimeException(e);
			}
		}
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
