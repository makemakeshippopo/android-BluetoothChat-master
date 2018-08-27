/*
* Copyright 2013 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/


package com.example.android.bluetoothchat;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ViewAnimator;

import com.example.android.common.activities.SampleActivityBase;
import com.example.android.common.logger.Log;
import com.example.android.common.logger.LogFragment;
import com.example.android.common.logger.LogWrapper;
import com.example.android.common.logger.MessageOnlyLogFilter;

//////////////////////

import android.media.AudioAttributes;
import android.media.SoundPool;

///
import android.os.Handler;
///
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

//import android.util.Log;

import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.Button;

//////////////////////

/**
 * A simple launcher activity containing a summary sample description, sample log and a custom
 * {@link android.support.v4.app.Fragment} which can display a view.
 * <p>
 * For devices with displays with a width of 720dp or greater, the sample log is always visible,
 * on other devices it's visibility is controlled by an item on the Action Bar.
 */
public class MainActivity extends SampleActivityBase {



    ///////////////////////////

    private SoundPool soundPool;
    private int soundHB;
    private Button button1, button2, button3;
    private int flag = 0;
    private int dflag = 0;

//////////////////////////////




    public static final String TAG = "MainActivity";

    // Whether the Log Fragment is currently shown
    private boolean mLogShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ///////////////////////

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                // USAGE_MEDIA
                // USAGE_GAME
                .setUsage(AudioAttributes.USAGE_GAME)
                // CONTENT_TYPE_MUSIC
                // CONTENT_TYPE_SPEECH, etc.
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build();

        soundPool = new SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                // ストリーム数に応じて
                .setMaxStreams(2)
                .build();

        // hb_c.wav をロードしておく
        soundHB = soundPool.load(this, R.raw.hb_c, 1);

        // load が終わったか確認する場合
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                Log.d("debug","sampleId="+sampleId);
                Log.d("debug","status="+status);
            }
        });

        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ////////
                flag = 1;
                dflag = 1;
                /*
                final Handler handler = new Handler();
                final Runnable r = new Runnable() {
                    int count = 0;
                    @Override
                    public void run() {
                        // UIスレッド
                        count++;
                        if (count > 1000) { //1000回実行したら終了
                            return;
                        }
                        soundPool.play(soundHB, 1.0f, 1.0f, 1, 0, 1);
                        if(flag != 1)
                            return;
                        //doSomething(); // 何かやる
                        handler.postDelayed(this, 1000);
                    }
                };
                handler.post(r);
                */
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ////////
                flag = 2;
                if(dflag != 2) {
                    dflag = 2;
                    final Handler handler = new Handler();
                    final Runnable r = new Runnable() {

                        @Override
                        public void run() {
                            // UIスレッド
                            if (flag != 2) {
                             //   dflag = 0;
                                return;
                            }

                            soundPool.play(soundHB, 1.0f, 1.0f, 1, 0, 1);

                            //doSomething(); // 何かやる
                            handler.postDelayed(this, 1000);
                        }
                    };
                    handler.post(r);
                }
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ////////
                flag = 3;
                if(dflag != 3) {
                    dflag = 3;
                    final Handler handler = new Handler();
                    final Runnable r = new Runnable() {

                        @Override
                        public void run() {
                            if (flag != 3) {
                               // dflag = 0;
                                return;
                            }

                            soundPool.play(soundHB, 1.0f, 1.0f, 1, 0, 1);

                            //doSomething(); // 何かやる
                            handler.postDelayed(this, 500);
                        }
                    };
                    handler.post(r);
                }
            }
        });

        ///////////////////////


        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            BluetoothChatFragment fragment = new BluetoothChatFragment();
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem logToggle = menu.findItem(R.id.menu_toggle_log);
        logToggle.setVisible(findViewById(R.id.sample_output) instanceof ViewAnimator);
        logToggle.setTitle(mLogShown ? R.string.sample_hide_log : R.string.sample_show_log);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_toggle_log:
                mLogShown = !mLogShown;
                ViewAnimator output = (ViewAnimator) findViewById(R.id.sample_output);
                if (mLogShown) {
                    output.setDisplayedChild(1);
                } else {
                    output.setDisplayedChild(0);
                }
                supportInvalidateOptionsMenu();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /** Create a chain of targets that will receive log data */
    @Override
    public void initializeLogging() {
        // Wraps Android's native log framework.
        LogWrapper logWrapper = new LogWrapper();
        // Using Log, front-end to the logging chain, emulates android.util.log method signatures.
        Log.setLogNode(logWrapper);

        // Filter strips out everything except the message text.
        MessageOnlyLogFilter msgFilter = new MessageOnlyLogFilter();
        logWrapper.setNext(msgFilter);

        // On screen logging via a fragment with a TextView.
        LogFragment logFragment = (LogFragment) getSupportFragmentManager()
                .findFragmentById(R.id.log_fragment);
        msgFilter.setNext(logFragment.getLogView());

        Log.i(TAG, "Ready");
    }
}
