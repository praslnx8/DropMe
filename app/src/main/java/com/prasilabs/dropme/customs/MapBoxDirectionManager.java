package com.prasilabs.dropme.customs;

/**
 * Created by prasi on 7/6/16.
 */
/*public class MapBoxDirectionManager
{
    private static final String TAG = MapBoxDirectionManager.class.getSimpleName();

    public static void showDirection(final Context context, final MapboxMap mapboxMap, com.google.android.gms.maps.model.LatLng source, com.google.android.gms.maps.model.LatLng dest)
    {
        MapboxDirections.Builder builder = new MapboxDirections.Builder();
        builder.setAccessToken(MapBoxMapLoader.MAPBOX_ACCESS_TOKEN);

        Position origin = Position.fromCoordinates(source.latitude, source.longitude);
        Position destination = Position.fromCoordinates(dest.latitude, dest.longitude);

        builder.setProfile(DirectionsCriteria.PROFILE_DRIVING);
        builder.setOrigin(origin);
        builder.setDestination(destination);

        try
        {
            MapboxDirections mapboxDirections = builder.build();

            mapboxDirections.enqueueCall(new Callback<DirectionsResponse>() {
                @Override
                public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response)
                {
                    if(response.body() == null)
                    {
                        ConsoleLog.i(TAG, "response body is null");
                        return;
                    }

                    DirectionsRoute route = response.body().getRoutes().get(0);
                    ViewUtil.t(context, "Route is " +  route.getDistance() + " meters long.");

                    drawRoute(mapboxMap, route);
                }

                @Override
                public void onFailure(Call<DirectionsResponse> call, Throwable t)
                {
                    ConsoleLog.t(t);
                }
            });

        }
        catch (ServicesException e)
        {
            ConsoleLog.e(e);
        }
    }

    private static void drawRoute(MapboxMap mapboxMap, DirectionsRoute route) {
        // Convert LineString coordinates into LatLng[]
        LineString lineString = LineString.fromPolyline(route.getGeometry(), Constants.OSRM_PRECISION_V5);
        List<Position> coordinates = lineString.getCoordinates();
        LatLng[] points = new LatLng[coordinates.size()];
        for (int i = 0; i < coordinates.size(); i++) {
            points[i] = new LatLng(
                    coordinates.get(i).getLatitude(),
                    coordinates.get(i).getLongitude());
        }

        // Draw Points on MapView
        mapboxMap.addPolyline(new PolylineOptions()
                .add(points)
                .color(Color.parseColor("#009688"))
                .width(5));
    }
}*/
