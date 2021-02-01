package com.c1ctech.highlighttextdemo;

import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    TextToSpeech tts;

    String sentence = "The quick brown fox jumps over the lazy dog.";

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.tv_text);
        textView.setText(sentence);

        // creating TTS instance
        tts = new TextToSpeech(this, this);

    }

    public void onInit(int status) {

        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {

            //called when speaking starts
            @Override
            public void onStart(String utteranceId) {
                Log.i("TTS", "utterance started");
            }

            //called when speaking is finished.
            @Override
            public void onDone(String utteranceId) {
                Log.i("TTS", "utterance done");
            }

            //called when an error has occurred during processing.
            @Override
            public void onError(String utteranceId) {
                Log.i("TTS", "utterance error");
            }

            //called when the TTS service is about to speak
            //the specified range of the utterance with the given utteranceId.
            @Override
            public void onRangeStart(String utteranceId,
                                     final int start,
                                     final int end,
                                     int frame) {
                Log.i("TTS", "onRangeStart() ... utteranceId: " + utteranceId + ", start: " + start
                        + ", end: " + end + ", frame: " + frame);


                // onRangeStart (and all UtteranceProgressListener callbacks) do not run on main thread
                // so we explicitly manipulate views on the main thread.
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Spannable textWithHighlights = new SpannableString(sentence);


                        textWithHighlights.setSpan(new ForegroundColorSpan(Color.RED), start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                        textView.setText(textWithHighlights);

                    }
                });

            }

        });

    }


    public void startClicked(View ignored) {

        tts.speak(sentence, TextToSpeech.QUEUE_FLUSH, null, "UNIQUE_UTTERANCE_ID");

    }


    @Override
    public void onDestroy() {
        //don't forget to shutdown tts
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

}