package com.example.homework3;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class LocationFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private ArrayList<Location> locations;
    private String location_url = "https://rickandmortyapi.com/api/location/?page=1";
    private static AsyncHttpClient client = new AsyncHttpClient();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        view = inflater.inflate(R.layout.location_fragment, container, false);
        recyclerView = view.findViewById(R.id.recyclerView_location);
        locations = new ArrayList<>();
        client.addHeader("accept", "application/json");
        client.get(location_url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject locationJSON = new JSONObject(new String(responseBody));
                    JSONArray locationsArray = locationJSON.getJSONArray("results");

                    for(int i = 0; i < locationsArray.length(); i++){
                        JSONObject locationObject = locationsArray.getJSONObject(i);
                        Location location = new Location(locationObject.getString("name"),
                                locationObject.getString("type"),
                                locationObject.getString("dimension"));
                        locations.add(location);
                    }
                    LocationAdapter adapter = new LocationAdapter(locations);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("Error", "Error");
            }
        });
        return view;
    }
}
