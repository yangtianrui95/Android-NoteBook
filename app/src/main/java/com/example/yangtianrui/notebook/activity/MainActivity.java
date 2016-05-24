package com.example.yangtianrui.notebook.activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.example.yangtianrui.notebook.R;
import com.example.yangtianrui.notebook.adapter.NaviListAdapter;
import com.example.yangtianrui.notebook.fragment.AllNotesFragment;
import com.example.yangtianrui.notebook.fragment.SearchNoteFragment;
import com.example.yangtianrui.notebook.fragment.SettingFragment;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener, AdapterView.OnItemClickListener {

    private Toolbar mToolbar;
    private ActionBarDrawerToggle mToggle;
    private FrameLayout mFlContent;
    private ListView mLvNavi;
    private DrawerLayout mDlLayout;
    private String[] mLabels = new String[]{
            "AllNodes", "SearchByTitle", "Setting"
    };
    private Fragment mFragments[] = new Fragment[mLabels.length];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragments[0] = new AllNotesFragment();
        mFragments[1] = new SearchNoteFragment();
        mFragments[2] = new SettingFragment();
        initView();
        showFragment();
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.id_toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setOnMenuItemClickListener(this);
        mDlLayout = (DrawerLayout) findViewById(R.id.id_dl_main_layout);
        mFlContent = (FrameLayout) findViewById(R.id.id_fl_main_content);
        mLvNavi = (ListView) findViewById(R.id.id_lv_navi);
        NaviListAdapter adapter = new NaviListAdapter(this, Arrays.asList(mLabels));
        mToggle = new ActionBarDrawerToggle(this, mDlLayout, mToolbar, R.string.app_name, R.string.app_name);
        mToggle.syncState();
        mDlLayout.setDrawerListener(mToggle);
        mLvNavi.setAdapter(adapter);
        // 为每个ListItem添加点击事件,即启动相应的Fragment
        mLvNavi.setOnItemClickListener(this);
    }

    private void showFragment() {
        AllNotesFragment notesFragment = new AllNotesFragment();
        getFragmentManager().beginTransaction().replace(R.id.id_fl_main_content, notesFragment).commit();
    }


    /**
     * 添加菜单
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    /**
     * 设置添加事件
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.id_menu_add_note:
                Intent intent = new Intent(this, NoteDetailActivity.class);
                startActivity(intent);
                break;
        }
        return false;
    }

    /**
     * 切换到相应的Fragment
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        getFragmentManager().beginTransaction()
                .replace(R.id.id_fl_main_content, mFragments[position]).commit();
        mDlLayout.closeDrawers();
    }
}
