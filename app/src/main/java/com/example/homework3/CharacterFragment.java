package com.example.homework3;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import cz.msebera.android.httpclient.Header;

public class CharacterFragment extends Fragment {

    private View view;
    private ImageView imageView_character;
    private TextView textView_character, textView_status, textView_species, textView_gender, textView_origin, textView_location, textView_episodes;
    private String character_url = "https://rickandmortyapi.com/api/character/";
    private static AsyncHttpClient client = new AsyncHttpClient();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        view = inflater.inflate(R.layout.character_fragment, container, false);
        imageView_character = view.findViewById(R.id.imageView_character);
        textView_character = view.findViewById(R.id.textView_character);
        textView_status = view.findViewById(R.id.textView_status);
        textView_species = view.findViewById(R.id.textView_species);
        textView_gender = view.findViewById(R.id.textView_gender);
        textView_origin = view.findViewById(R.id.textView_origin);
        textView_location = view.findViewById(R.id.textView_location);
        textView_episodes = view.findViewById(R.id.textView_episodes);

        client.addHeader("accept", "application/json");
        client.get(character_url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject jsonObj = new JSONObject(new String(responseBody));
                    String count = jsonObj.getJSONObject("info").getString("count");
                    int characterNum = Integer.parseInt(count);
                    Random rand = new Random();
                    int numSearch = rand.nextInt(characterNum + 1);
                    String newCharacterUrl = character_url + numSearch; //gets a new string for api
                    client.get(newCharacterUrl, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            try {
                                JSONObject jsonObj2 = new JSONObject((new String(responseBody)));
                                String image_url = jsonObj2.getString("image");
                                Picasso.get().load(image_url).into(imageView_character);
                                String CharacterName = jsonObj2.getString("name");
                                textView_character.setText(CharacterName);
                                String status = jsonObj2.getString("status");
                                textView_status.setText("Status: " + status);
                                String species = jsonObj2.getString("species");
                                textView_species.setText("Species: " + species);
                                String gender = jsonObj2.getString("gender");
                                textView_gender.setText("Gender: " + gender);
                                String origin = jsonObj2.getJSONObject("origin").getString("name");
                                textView_origin.setText("Origin: " + origin);
                                String location = jsonObj2.getJSONObject("location").getString("name");
                                textView_location.setText("Location: " + location);
                                JSONArray episodesArray = jsonObj2.getJSONArray("episode");
                                ArrayList<String> episodeList = new ArrayList<>();
                                Log.i("episodes",String.valueOf(episodesArray));

                                for (int i = 0; i < episodesArray.length(); i++) {
                                    Log.i("episodes", String.valueOf(episodesArray.length()));
                                    String episodeVal = episodesArray.getString(i);
                                    String episodeEdited = episodeVal.replace("https://rickandmortyapi.com/api/episode/", "");
                                    episodeList.add(episodeEdited);
                                }
                                String finalList = Arrays.toString(episodeList.toArray()).replace("[", "").replace("]", "");
                                textView_episodes.setText("Appeared in Episodes: " + finalList);
                                Log.i("episodes", String.valueOf(episodeList));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Log.e("error", "error");
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("error", "error");
            }
        });

        return view;
    }

}
