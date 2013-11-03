package com.example.RushHour.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import com.example.RushHour.R;

/**
 * Created with IntelliJ IDEA.
 * User: GSUS
 * Date: 27.10.2013
 * Time: 22:13
 * To change this template use File | Settings | File Templates.
 */
public class AboutActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        TextView t = (TextView)findViewById(R.id.aboutText);
        t.setText("Made by:\n Gunnar Sigurðsson\n Vignir Guðmundsson\n Víðir Orri Reynisson\n");
    }
}
