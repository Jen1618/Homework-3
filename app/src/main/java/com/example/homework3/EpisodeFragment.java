package com.example.homework3;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.util.Random;

import cz.msebera.android.httpclient.Header;

public class EpisodeFragment extends Fragment {

    private View view;
    private ImageView imageView_one, imageView_two, imageView_three;
    private TextView textView_title, textView_airDate, textView_characters;
    private Button button_info;
    private String episodes_url = "https://rickandmortyapi.com/api/episode/";
    private static AsyncHttpClient client = new AsyncHttpClient();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        view = inflater.inflate(R.layout.episode_fragment, container, false);
        imageView_one = view.findViewById(R.id.imageView_one);
        imageView_two = view.findViewById(R.id.imageView_two);
        imageView_three = view.findViewById(R.id.imageView_three);
        textView_title = view.findViewById(R.id.textView_title);
        textView_airDate = view.findViewById(R.id.textView_airDate);
        textView_characters = view.findViewById(R.id.textView_characters);
        button_info = view.findViewById(R.id.button_moreInfo);
     //   button_info.setOnClickListener(v -> );

        client.addHeader("accept", "application/json");
        client.get(episodes_url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject jsonObj = new JSONObject(new String(responseBody));
                    String episode = jsonObj.getJSONObject("info").getString("count");
                    int episodeNum = Integer.parseInt(episode);
                    Random rand = new Random();
                    int episodeNumber = rand.nextInt(episodeNum + 1);
                    String episodeSpecificURL = episodes_url + episodeNumber;

                    client.get(episodeSpecificURL, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            try {
                                JSONObject jsonObj2 = new JSONObject(new String(responseBody));
                                String episode = jsonObj2.getString("episode");
                                String title = jsonObj2.getString("name");
                                textView_title.setText(episode + " "+ title);
                                String airDate = jsonObj2.getString("air_date");
                                textView_airDate.setText("Aired on: " + airDate);
                                textView_characters.setText("Characters in this episode");
                                JSONArray charactersArray = jsonObj2.getJSONArray("characters");
                                String character1 = charactersArray.getString(0);
                                String character2 = charactersArray.getString(1);
                                String character3 = charactersArray.getString(2);
                                Log.i("test", character1); //delete

                                client.get(character1, new AsyncHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                        try {
                                            JSONObject jsonObj3 = new JSONObject(new String(responseBody));
                                            String image_url = jsonObj3.getString("image");
                                            Picasso.get().load(image_url).into(imageView_one);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                        Log.e("error", "error");
                                    }
                                });

                                client.get(character2, new AsyncHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                        try {
                                            JSONObject jsonObj4 = new JSONObject(new String(responseBody));
                                            String image_url2 = jsonObj4.getString("image");
                                            Picasso.get().load(image_url2).into(imageView_two);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                        Log.e("error", "error");
                                    }
                                });

                                client.get(character3, new AsyncHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                        try {
                                            JSONObject jsonObj4 = new JSONObject(new String(responseBody));
                                            String image_url3 = jsonObj4.getString("image");
                                            Picasso.get().load(image_url3).into(imageView_three);
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

   /* private void createNotificationChannel(){

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.new_mail)
                .setContentTitle(emailObject.getSenderName())
                .setContentText(emailObject.getSubject())
                .setLargeIcon(emailObject.getSenderAvatar())
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(emailObject.getSubjectAndSnippet()))
                .build();
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    } */
}
