package com.example.vtg_opencv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class InfoActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    TextView built, loc, arch, area, about, title;
    String strbuilt, strloc, strarch, strarea, strabout;
    ImageView audio, map;
    public  static  String name1;
    TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        title = findViewById(R.id.title);
        built = findViewById(R.id.builtc);
        loc = findViewById(R.id.locc);
        arch = findViewById(R.id.archc);
        area = findViewById(R.id.areac);
        about = findViewById(R.id.aboutc);
        audio = findViewById(R.id.AudioBtn);
        map = findViewById(R.id.LocBtn);
        textToSpeech = new TextToSpeech(this, this);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        final String name = getIntent().getStringExtra("name");
        name1 = name;
        title.setText(name);

        assert name != null;
        DocumentReference docRef = db.collection("landmarks").document(name);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            private String TAG = "Information document";

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        strbuilt = document.getString("built");
                        built.setText(strbuilt);
                        strloc = document.getString("location");
                        loc.setText(strloc);
                        strarch = document.getString("architectural style");
                        arch.setText(strarch);
                        strarea = document.getString("area");
                        area.setText(strarea);
                        strabout = document.getString("about");
                        about.setText(strabout);
                        Log.d(TAG, "About: " + about);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }

                audio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!textToSpeech.isSpeaking()){
                            HashMap<String,String> stringStringHashMap = new HashMap<String, String>();
                            stringStringHashMap.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"Hello how are you");
                            textToSpeech.speak(strabout,TextToSpeech.QUEUE_ADD,stringStringHashMap);
                            //auioBtn.setVisibility(Button.GONE);
                        }
                        else if(textToSpeech.isSpeaking())
                        {
                            if (textToSpeech!=null){
                                textToSpeech.stop();
                            }
                        }
                        else {
                            textToSpeech.stop();
                        }
                    }
                });

                map.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent;
                        intent = new Intent(InfoActivity.this, MapsActivity.class);
                        intent.putExtra("name", name);
                        startActivity(intent);
                    }
                });
            }
        });



    }

    @Override
    public void onInit(int status) {

    }
}