package com.prasilabs.dropme.customs;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.util.ArrayMap;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.prasilabs.dropme.debug.ConsoleLog;

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

/**
 * Created by prasi on 14/12/15.
 * Gives direction/route
 */
public class DirectionManager
{
    public void showDirection(MapLoader mapLoader, LatLng srcLatlng, LatLng destLatLng)
    {
        String url = DirectionManager.getDirectionsUrl(srcLatlng, destLatLng);

        if(url != null)
        {
            DownloadTask downloadTask = new DownloadTask(mapLoader);

            // Start downloading json data from Google Directions API
            downloadTask.execute(url);
        }
    }

    /** A method to download json data from url */
    private static String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

// Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

// Connecting to url
            urlConnection.connect();

// Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuilder sb = new StringBuilder();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e)
        {
            ConsoleLog.e(e);
        }finally
        {
            if(iStream != null)
            {
                iStream.close();
                urlConnection.disconnect();
            }
        }
        return data;
    }

    private static String getDirectionsUrl(LatLng origin, LatLng dest)
    {
        if(origin != null && dest != null) {
            // Origin of route
            String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

            // Destination of route
            String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

            // Sensor enabled
            String sensor = "sensor=false";

            // Building the parameters to the web service
            String parameters = str_origin + "&" + str_dest + "&" + sensor;

            // Output format
            String output = "json";

            // Building the url to the web service
            String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

            return url;
        }

        return null;
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, ArrayList<ArrayList<ArrayMap<String, String>>> > {

        // Parsing the data in non-ui thread
        private MapLoader mapLoader;
        public ParserTask(MapLoader mapLoader)
        {
            this.mapLoader = mapLoader;
        }

        @Override
        protected ArrayList<ArrayList<ArrayMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            ArrayList<ArrayList<ArrayMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionJSONParser parser = new DirectionJSONParser();

// Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(ArrayList<ArrayList<ArrayMap<String, String>>> result)
        {
            if (result != null && result.size() > 0)
            {
                ArrayList points = null;
                PolylineOptions lineOptions = null;
                MarkerOptions markerOptions = new MarkerOptions();

// Traversing through all the routes
                for (int i = 0; i < result.size(); i++)
                {
                    points = new ArrayList();
                    lineOptions = new PolylineOptions();


// Fetching i-th route
                    ArrayList<ArrayMap<String, String>> path = result.get(i);

// Fetching all the points in i-th route
                    for (int j = 0; j < path.size(); j++) {
                        ArrayMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }

// Adding all the points in the route to LineOptions
                    lineOptions.addAll(points);
                    lineOptions.width(12);
                    lineOptions.color(Color.RED);
                    lineOptions.geodesic(true);

                }

// Drawing polyline in the Google Map for the i-th route

                mapLoader.addPolyLine(lineOptions);

            }
        }
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        private MapLoader mapLoader;

        public DownloadTask(MapLoader mapLoader)
        {
            this.mapLoader = mapLoader;
        }

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

// For storing data from web service
            String data = "";

            try{
// Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                ConsoleLog.e(e);
            }
            return data;
        }

        // Executes in UI thread, after the execution of
// doInBackground()
        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask(mapLoader);

// Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    private class DirectionJSONParser
    {
        /** Receives a JSONObject and returns a list of lists containing latitude and longitude */
        public ArrayList<ArrayList<ArrayMap<String,String>>> parse(JSONObject jObject){

            ArrayList<ArrayList<ArrayMap<String, String>>> routes = new ArrayList<ArrayList<ArrayMap<String,String>>>() ;
            JSONArray jRoutes = null;
            JSONArray jLegs = null;
            JSONArray jSteps = null;

            try {

                jRoutes = jObject.getJSONArray("routes");

                /** Traversing all routes */
                for(int i=0;i<jRoutes.length();i++){
                    jLegs = ( (JSONObject)jRoutes.get(i)).getJSONArray("legs");
                    ArrayList<ArrayMap<String, String>> path = new ArrayList<ArrayMap<String, String>>();

                    /** Traversing all legs */
                    for(int j=0;j<jLegs.length();j++){
                        jSteps = ( (JSONObject)jLegs.get(j)).getJSONArray("steps");

                        /** Traversing all steps */
                        for(int k=0;k<jSteps.length();k++){
                            String polyline = "";
                            polyline = (String)((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");
                            List list = decodePoly(polyline);

                            /** Traversing all points */
                            for(int l=0;l <list.size();l++){
                                ArrayMap<String, String> hm = new ArrayMap<>();
                                hm.put("lat", Double.toString(((LatLng)list.get(l)).latitude) );
                                hm.put("lng", Double.toString(((LatLng)list.get(l)).longitude) );
                                path.add(hm);
                            }
                        }
                        routes.add(path);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }catch (Exception e){
            }

            return routes;
        }

        /**
         * Method to decode polyline points
         * Courtesy : http://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
         * */
        private List decodePoly(String encoded) {

            List poly = new ArrayList();
            int index = 0, len = encoded.length();
            int lat = 0, lng = 0;

            while (index < len) {
                int b, shift = 0, result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lat += dlat;

                shift = 0;
                result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lng += dlng;

                LatLng p = new LatLng((((double) lat / 1E5)),
                        (((double) lng / 1E5)));
                poly.add(p);
            }

            return poly;
        }
    }


}
