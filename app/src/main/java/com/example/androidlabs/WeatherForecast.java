package com.example.androidlabs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherForecast extends AppCompatActivity {
    protected static final String URL_STRING = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric";
    protected static final String UV_RATING_URL = "http://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389";
    protected static final String URL_IMAGE = "http://openweathermap.org/img/w/";
    protected static final String ACTIVITY_NAME = "WeatherForecast";
    private ImageView weatherImageView;
    private TextView currentTextView;
    private TextView minTextView;
    private TextView maxTextView;
    private TextView uvRatingTextView;
    private TextView windSpeedTextView;
    private ProgressBar progressBar;


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        weatherImageView = (ImageView) findViewById(R.id.currentWeatherImageView);
        currentTextView = (TextView) findViewById(R.id.currentTempTextView);
        minTextView = (TextView) findViewById(R.id.minTempTextView);
        maxTextView = (TextView) findViewById(R.id.maxTempTextView);
        uvRatingTextView = (TextView) findViewById(R.id.uvRatingTextView);
        windSpeedTextView = (TextView) findViewById(R.id.windSpeedTextView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setMax(100);

        new ForecastQuery().execute(null, null, null);

    }

    private class ForecastQuery extends AsyncTask<String, Integer, String> {
        private String currentTemp = null;
        private String minTemp = null;
        private String maxTemp = null;
        private String windSpeed = null;
        private String uvRating = null;
        private String iconFilename = null;
        private Bitmap image = null;

        @Override
        protected String doInBackground(String... strings) {
            getTemeratureAndWindSpeed();
            getUVRating();
            return null;
        }

        private void getTemeratureAndWindSpeed() {
            InputStream inputStream = null;
            try {
                URL url = new URL(URL_STRING);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();
                inputStream = conn.getInputStream();
                parseInput(inputStream);
            } catch (IOException e) {
                Log.i(ACTIVITY_NAME, "IOException: " + e.getMessage());
            } catch (XmlPullParserException e) {
                Log.i(ACTIVITY_NAME, "XmlPullParserException: " + e.getMessage());
            } finally {
                if (inputStream != null)
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        Log.i(ACTIVITY_NAME, "IOException: " + e.getMessage());
                    }
            }
        }

        private void getUVRating() {
            HttpURLConnection uvConnection = null;
            InputStream inStream = null;
            try {
                URL uvURL = new URL(UV_RATING_URL);
                uvConnection = (HttpURLConnection) uvURL.openConnection();
                inStream = uvConnection.getInputStream();

                //create a JSON object from the response
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String result = sb.toString();

                //now a JSON table:
                JSONObject jObject = new JSONObject(result);
                double aDouble = jObject.getDouble("value");
                Log.i("UV is:", "" + aDouble);
                uvRating = String.valueOf(aDouble);

            } catch (IOException | JSONException e) {
                Log.i(ACTIVITY_NAME, "IOException: " + e.getMessage());
            } finally {
                if (uvConnection != null) {
                    uvConnection.disconnect();
                }
            }
        }

        private void parseInput(InputStream inputStream) throws XmlPullParserException, IOException {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inputStream, null);

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if(eventType == XmlPullParser.START_TAG) {
                    if (parser.getName().equalsIgnoreCase("temperature")) {
                        setTemperatures(parser);
                    } else if (parser.getName().equalsIgnoreCase("weather")) {
                        getIconFile(parser);
                    } else if (parser.getName().equalsIgnoreCase("speed")) {
                        windSpeed = parser.getAttributeValue(null, "value");
                    }
                }
                eventType = parser.next();
            }
        }

        private void setTemperatures(XmlPullParser parser) {
            try {
                currentTemp = parser.getAttributeValue(null, "value");
                Thread.sleep(1000);
                publishProgress(25);
                minTemp = parser.getAttributeValue(null, "min");
                Thread.sleep(1000);
                publishProgress(50);
                maxTemp = parser.getAttributeValue(null, "max");
                Thread.sleep(1000);
                publishProgress(75);
            } catch (InterruptedException e) {
                Log.i(ACTIVITY_NAME , "Thread sleep failed");
                e.printStackTrace();
            }
        }

        private void getIconFile(XmlPullParser parser) {
            iconFilename = parser.getAttributeValue(null, "icon") + ".png";
            File file = getBaseContext().getFileStreamPath(iconFilename);
            if (!file.exists()) {
                saveWeatherImage(iconFilename);
            } else {
                Log.i(ACTIVITY_NAME, "Saved icon, " + iconFilename + " is displayed.");
                try {
                    FileInputStream in = new FileInputStream(file);
                    image = BitmapFactory.decodeStream(in);
                } catch (FileNotFoundException e) {
                    Log.i(ACTIVITY_NAME, "Saved icon, " + iconFilename + " is not found.");
                }
            }
            publishProgress(100);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            currentTextView.setText("Current Temp" + String.format("%.2f", Double.parseDouble(currentTemp)));
            minTextView.setText("Min Temp" + String.format("%.2f", Double.parseDouble(minTemp)));
            maxTextView.setText("Max Temp" + String.format("%.2f", Double.parseDouble(maxTemp)));
            uvRatingTextView.setText("UV Rating " + String.format("%.2f", Double.parseDouble(uvRating)));
            windSpeedTextView.setText("Wind Speed " + String.format("%.2f", Double.parseDouble(windSpeed)));
            weatherImageView.setImageBitmap(image);
            progressBar.setVisibility(View.INVISIBLE);
        }

        private void saveWeatherImage(String fname) {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(URL_IMAGE + fname);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    image = BitmapFactory.decodeStream(connection.getInputStream());
                    FileOutputStream outputStream = openFileOutput(fname, Context.MODE_PRIVATE);
                    image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                    outputStream.flush();
                    outputStream.close();
                    Log.i(ACTIVITY_NAME, "Weather icon, " + fname + " is downloaded and displayed.");
                } else
                    Log.i(ACTIVITY_NAME, "Can't connect to the weather icon for downloading.");
            } catch (Exception e) {
                Log.i(ACTIVITY_NAME, "weather icon download error: " + e.getMessage());
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
    }
}