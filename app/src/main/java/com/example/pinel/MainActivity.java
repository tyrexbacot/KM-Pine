
package com.example.pinel;

import android.view.Gravity;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.pinel.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView tv = new TextView(this);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(20);
        if (isPro()) {
            tv.setText("Pro");
        } else {
            tv.setText("Free");
        }

        setContentView(tv);
    }

    public boolean isPro() {
        return false;
    }
}