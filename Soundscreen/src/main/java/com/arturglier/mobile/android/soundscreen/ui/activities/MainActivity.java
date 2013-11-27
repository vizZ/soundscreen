package com.arturglier.mobile.android.soundscreen.ui.activities;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.arturglier.mobile.android.soundscreen.R;
import com.arturglier.mobile.android.soundscreen.SoundscreenPreferenceActivity;
import com.arturglier.mobile.android.soundscreen.SoundscreenWallpaperService;
import com.arturglier.mobile.android.soundscreen.net.services.SyncService;
import com.arturglier.mobile.android.soundscreen.ui.adapters.MainViewPagerAdapter;
import com.arturglier.mobile.android.soundscreen.ui.fragments.AboutFragment;
import com.viewpagerindicator.CirclePageIndicator;

public class MainActivity extends SherlockFragmentActivity implements View.OnClickListener {

    private FragmentPagerAdapter mAdapter;
    private ViewPager mPager;
    private CirclePageIndicator mIndicator;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdapter = new MainViewPagerAdapter(this, new Class<?>[]{AboutFragment.class});

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);

        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= 16) {
            intent.setAction(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
            intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(this, SoundscreenWallpaperService.class));
        } else {
            intent.setAction(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
        }
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_resync:
                SyncService.restart(this);
                return true;
            case R.id.menu_settings:
                startActivity(new Intent(this, SoundscreenPreferenceActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
