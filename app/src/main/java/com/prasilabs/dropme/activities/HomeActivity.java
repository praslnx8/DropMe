package com.prasilabs.dropme.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.prasilabs.dropme.R;
import com.prasilabs.dropme.backend.dropMeApi.model.VDropMeUser;
import com.prasilabs.dropme.core.CoreActivity;
import com.prasilabs.dropme.customs.FragmentNavigator;
import com.prasilabs.dropme.managers.UserManager;
import com.prasilabs.dropme.modules.home.views.HomeFragment;
import com.prasilabs.dropme.utils.ViewUtil;

import butterknife.BindView;

public class HomeActivity extends CoreActivity implements NavigationView.OnNavigationItemSelectedListener
{
    @BindView(R.id.container)
    LinearLayout containerLayout;

    public static void callHomeActivity(Context context)
    {
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentNavigator.navigateToFragment(this, HomeFragment.getHomeFragment(), false, containerLayout.getId());

        setNavigationHeaderData(navigationView);
    }

    private void setNavigationHeaderData(NavigationView navigationView)
    {
        View header = navigationView.getHeaderView(0);
        ImageView userImage = (ImageView) header.findViewById(R.id.user_image);
        TextView nameText = (TextView) header.findViewById(R.id.name_text);
        TextView emailText = (TextView) header.findViewById(R.id.email_text);

        VDropMeUser vDropMeUser = UserManager.getDropMeUser(this);

        if(vDropMeUser != null)
        {
            ViewUtil.renderImage(userImage, vDropMeUser.getPicture(), true);
            if(!TextUtils.isEmpty(vDropMeUser.getName()))
            {
                nameText.setText(ViewUtil.formatAsName(vDropMeUser.getName()));
            }
            if(!TextUtils.isEmpty(vDropMeUser.getEmail()))
            {
                emailText.setText(vDropMeUser.getEmail());
            }
        }
        else
        {
            Intent intent = new Intent(this, SplashActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_help) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
