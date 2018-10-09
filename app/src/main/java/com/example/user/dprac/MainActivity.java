package com.example.user.dprac;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.dprac.Adapters.NavbarListAdapter;
import com.example.user.dprac.models.NavbarList;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    ImageView bar_icon;
    TextView bar_title;
    RelativeLayout drawer_layout_list;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        toolbar  =(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_toolbar,null);
        actionBar.setCustomView(view);
        drawer_layout_list = (RelativeLayout)findViewById(R.id.drawer_layout_list);
        bar_icon = (ImageView)findViewById(R.id.bar_icon);
        bar_title = (TextView)findViewById(R.id.bar_title);
        bar_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        setUpLayout();


        drawerListView = (ListView) findViewById(R.id.left_drawer);
        navbarData();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);



        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                null  ,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        );

        // Set actionBarDrawerToggle as the DrawerListener
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        drawerListView.setOnItemClickListener(new DrawerItemClickListener());
    }

    @Override
    public void onClick(View view) {

    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {

            switch (position){
                case 0:
                    if(Constants.inner_fragment_position==0){
                        drawerLayout.closeDrawer(drawer_layout_list);
                    }else{
                        drawerLayout.closeDrawer(drawer_layout_list);
                        MainFragment mainFragment = new MainFragment();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.popBackStack();
                        fragmentManager.beginTransaction().replace(R.id.container,mainFragment).commit();
                        Constants.inner_fragment_position=0;
                       // Toast.makeText(MainActivity.this,String.valueOf(Constants.inner_fragment_position),Toast.LENGTH_LONG).show();
                    }

                    break;

                case 1:
                    if(Constants.inner_fragment_position==1){
                        drawerLayout.closeDrawer(drawer_layout_list);
                    }else{
                        drawerLayout.closeDrawer(drawer_layout_list);
                        HistoryFragment historyFragment = new HistoryFragment();
                        replaceFragment(historyFragment);
                        Constants.inner_fragment_position=1;
                    }
                    break;
                case 2:
                    if(Constants.inner_fragment_position==2){
                        drawerLayout.closeDrawer(drawer_layout_list);
                    }else{
                        /**
                         * Write some login for Notification Fragment
                         */
                        drawerLayout.closeDrawer(drawer_layout_list);
                    }
                    break;
                case 3:
                    if(Constants.inner_fragment_position==3){
                        drawerLayout.closeDrawer(drawer_layout_list);
                    }else{
                        /**
                         * Write some login for Settings Fragment
                         */
                        drawerLayout.closeDrawer(drawer_layout_list);
                    }

                    break;

                case 4:
                    SharedPrefManager.getInstance(getApplicationContext()).Logout();
                    Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                    intent.putExtra("flag",true);
                    finishAffinity();
                    startActivity(intent);
                    break;

            }



        }
    }


    public void setUpLayout(){
        MainFragment mainFragment =new MainFragment();
        addFragment(mainFragment);

    }
    public void addFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container,fragment)
                .commit();
    }

    public void replaceFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container,fragment)
                .addToBackStack(null)
                .commit();
    }

    public void navbarData(){
        ArrayList<NavbarList> arrayList = new ArrayList<NavbarList>();
        NavbarList model1 = new NavbarList("Dashboard",R.drawable.dashboard_icon);
        arrayList.add(model1);

        NavbarList model3 = new NavbarList("History",R.drawable.history_icon);
        arrayList.add(model3);

        NavbarList model4 = new NavbarList("Notifications",R.drawable.notification_icon);
        arrayList.add(model4);

        NavbarList model5 = new NavbarList("Settings",R.drawable.settings_icon);
        arrayList.add(model5);

        NavbarList model6 = new NavbarList("Logout",R.drawable.logtout_icon);
        arrayList.add(model6);
        NavbarListAdapter navbarListAdapter = new NavbarListAdapter(MainActivity.this,arrayList);
        navbarListAdapter.notifyDataSetChanged();
        drawerListView.setAdapter(navbarListAdapter);
    }
}
