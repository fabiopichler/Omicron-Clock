package net.fabiopichler.omicronclock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener,
        PopupMenu.OnMenuItemClickListener,
        ChangeVoiceDialog.ChangedEventListener {

    public static final String APP_PREFERENCES_NAME = "AppPreferences";

    private Audio mAudio;
    private DrawerLayout mDrawer;

    private SharedPreferences mPreferences;

    private String mVoicePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.menuDrawer).setOnClickListener(this);
        findViewById(R.id.menuButton).setOnClickListener(this);
        findViewById(R.id.playButton).setOnClickListener(this);

        mDrawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mPreferences = getSharedPreferences(APP_PREFERENCES_NAME, 0);
        mVoicePath = mPreferences.getString("VoicePath", "01");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menuDrawer:
                mDrawer.openDrawer(GravityCompat.START);
                break;

            case R.id.menuButton:
                showPopupMenu();
                break;

            case R.id.playButton:
                play();
                break;
        }
    }

    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.action_changeVoice)
            changeVoice();

        return true;
    }

    private void showPopupMenu() {
        PopupMenu popup = new PopupMenu(this, findViewById(R.id.menuButton));
        popup.getMenuInflater().inflate(R.menu.menu_main, popup.getMenu());
        popup.setOnMenuItemClickListener(this);
        popup.show();
    }

    private void changeVoice() {
        ChangeVoiceDialog dialog = new ChangeVoiceDialog(this, mVoicePath);
        dialog.setChangedEventListener(this);
        dialog.show(getSupportFragmentManager(), "ChangeVoiceDialog");
    }

    private void play() {
        if (mAudio != null)
            mAudio.release();

        mAudio = new Audio(getAssets(), mVoicePath);
    }

    @Override
    public void onVoiceChanged(String voicePath) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString("VoicePath", voicePath);
        editor.apply();

        mVoicePath = voicePath;
    }

    @Override
    protected void onDestroy() {
        if (mAudio != null)
            mAudio.release();

        super.onDestroy();
    }

    /*BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context ctx, Intent intent) {
            String action = intent.getAction();

            if (action != null && action.compareTo(Intent.ACTION_TIME_TICK) == 0) {
            }
        }
    };*/

    @Override
    public void onStart() {
        super.onStart();

        //registerReceiver(mBroadcastReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
    }

    @Override
    public void onStop() {
        super.onStop();

        //unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START))
            mDrawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        mDrawer.closeDrawer(GravityCompat.START);

        switch (item.getItemId()) {
            case R.id.menuItemAbout:
                startActivity(new Intent(this, AboutActivity.class));
                break;

            case R.id.menuItemWebsite:
                openURL("https://fabiopichler.net/omicron-clock");
                break;

            case R.id.menuItemFacebook:
                openURL("https://www.facebook.com/fabiopichler.net");
                break;

            case R.id.menuItemTwitter:
                openURL("https://twitter.com/FabioPichler");
                break;

            case R.id.menuItemExit:
                finishAffinity();
                break;
        }

        return true;
    }

    private void openURL(String url) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }
}
