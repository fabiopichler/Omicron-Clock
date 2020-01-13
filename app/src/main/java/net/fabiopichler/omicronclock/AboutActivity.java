package net.fabiopichler.omicronclock;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity
        implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView appVersion = findViewById(R.id.appVersion);
        appVersion.setText(getResources().getString(R.string.app_version, BuildConfig.VERSION_NAME));

        findViewById(R.id.backButton).setOnClickListener(this);
        findViewById(R.id.okButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backButton || v.getId() == R.id.okButton)
            finish();
    }
}
