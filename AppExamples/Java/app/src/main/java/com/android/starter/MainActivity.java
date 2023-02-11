package com.android.starter;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.starter.network.Repository;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        new Thread(() -> {
            try {
                Repository repository = new Repository();
                String execution = repository.started();
                startProgram(execution);
                repository.logData();
                repository.done();
            } catch (Exception e) {
                Log.e("error", e.getMessage());
            }
        }).start();
    }

    void startProgram(String response) throws InterruptedException {
        String[] responseSplit = response.split("-");
        String program = responseSplit[0];
        int parameter = Integer.parseInt(responseSplit[1]);

        //do something here

        Log.d("DEBUG","Running " + program +  "with " + parameter +  "as input");
        Thread.sleep(5000L);
    }

}
