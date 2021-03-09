package com.example.homework3;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private Button button_character, button_episode, button_location;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        imageView.setImageBitmap(ImageViaAssets("hw#3.jpg"));

        button_character = findViewById(R.id.button_character);
        button_episode = findViewById(R.id.button_episode);
        button_location = findViewById(R.id.button_location);

        button_character.setOnClickListener(v -> loadFragment(new CharacterFragment()));
        button_episode.setOnClickListener(v -> loadFragment(new EpisodeFragment()));
        button_location.setOnClickListener(v -> loadFragment(new LocationFragment()));
    }

    public Bitmap ImageViaAssets(String fileName){
        AssetManager assetManager = getAssets();
        InputStream is = null;{
            try{
                is = assetManager.open(fileName);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        return bitmap;
    }

    public void loadFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment);
        fragmentTransaction.commit();
    }
}