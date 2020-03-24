package com.tsc.collapsingtoolbar.workers;

import android.os.AsyncTask;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tsc.collapsingtoolbar.adapters.RecyclerAdapter;
import com.tsc.collapsingtoolbar.dialogs.LoadingDialog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Loader extends AsyncTask<Void, Void, Void> {

    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> imgURL = new ArrayList<>();
    private ArrayList<String> rating = new ArrayList<>();
    private ArrayList<Integer> status = new ArrayList<>();

    private RecyclerView recyclerView;

    private double latitude, longitude;
    private FragmentManager fragmentManager;

    private LoadingDialog loadingDialog = new LoadingDialog();

    public Loader(FragmentManager fragmentManager, RecyclerView recyclerView, double latitude, double longitude) {
        this.recyclerView = recyclerView;
        this.latitude = latitude;
        this.longitude = longitude;
        this.fragmentManager = fragmentManager;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        RecyclerAdapter adapter = new RecyclerAdapter(names, rating, imgURL,status);
        recyclerView.setAdapter(adapter);
        loadingDialog.dismiss();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loadingDialog.show(fragmentManager.beginTransaction(),"abc");
    }

    @Override
    protected Void doInBackground(Void... voids) {

        initLists();
        String json = getJSON(latitude, longitude);
        JsonArray array = JsonParser.parseString(json).getAsJsonObject().get("nearby_restaurants").getAsJsonArray();
        for (JsonElement element : array) {
            JsonObject obj = element.getAsJsonObject().get("restaurant").getAsJsonObject();
            names.add(obj.get("name").getAsString());
            imgURL.add(obj.get("thumb").getAsString());
            status.add(obj.get("is_delivering_now").getAsInt());
            rating.add(obj.get("user_rating").getAsJsonObject().get("aggregate_rating").getAsString());
        }
        return null;
    }

    private void initLists() {
        names.clear();
        imgURL.clear();
        rating.clear();
        status.clear();
    }

    private String getJSON(double latitude, double longitude) {
        StringBuilder sb = new StringBuilder("");
        try {
            URL url = new URL("https://developers.zomato.com/api/v2.1/geocode?lat=" + latitude + "&lon=" + longitude);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.addRequestProperty("user-key", "df32418e0a801d4af519efb52d671e66");
            InputStream is = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String data;
            while ((data = br.readLine()) != null) {
                sb.append(data);
            }
            br.close();
            is.close();
            urlConnection.disconnect();
            return sb.toString();
        } catch (IOException e) {
            return e.getLocalizedMessage();
        }
    }
}
