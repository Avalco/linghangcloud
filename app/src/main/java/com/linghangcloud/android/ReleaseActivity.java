package com.linghangcloud.android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.onegravity.rteditor.RTEditText;
import com.onegravity.rteditor.RTManager;
import com.onegravity.rteditor.RTToolbar;
import com.onegravity.rteditor.api.RTApi;
import com.onegravity.rteditor.api.RTMediaFactoryImpl;
import com.onegravity.rteditor.api.RTProxyImpl;
import com.onegravity.rteditor.fonts.FontManager;

public class ReleaseActivity extends AppCompatActivity {
private Button back;
private RTManager mrtManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.ThemeLight);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_release);
        back=findViewById(R.id.back_home);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // create RTManager
        RTApi rtApi = new RTApi(this, new RTProxyImpl(this), new RTMediaFactoryImpl(this, true));
         mrtManager = new RTManager(rtApi, savedInstanceState);
        // register toolbar
        ViewGroup toolbarContainer = (ViewGroup) findViewById(R.id.rte_toolbar_container);
        RTToolbar rtToolbar = (RTToolbar) findViewById(R.id.rte_toolbar);
        if (rtToolbar != null) {
            mrtManager.registerToolbar(toolbarContainer, rtToolbar);
        }
        // register editor & set text
        RTEditText rtEditText = (RTEditText) findViewById(R.id.rtEditText);
        mrtManager.registerEditor(rtEditText, true);
        FontManager.preLoadFonts(this);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mrtManager.onSaveInstanceState(outState);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mrtManager.onDestroy(isFinishing());
    }
}
