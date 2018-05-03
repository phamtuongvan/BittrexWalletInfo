package pamobile.co.bittrexwalletinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import pamobile.co.bittrexwalletinfo.CryptoEvents.CryptoEventFragment;
import pamobile.co.bittrexwalletinfo.Service.BackgroundService;
import pamobile.co.bittrexwalletinfo.Settings.SettingsFragment;
import pamobile.co.bittrexwalletinfo.Wallet.WalletFragment;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    SettingsFragment settingsFragment = new SettingsFragment();
    WalletFragment walletFragment = new WalletFragment();
    CryptoEventFragment cryptoEventFragment = new CryptoEventFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        startService(new Intent(this, BackgroundService.class));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = getIntent();
        if(intent.getExtras() != null && intent.getExtras().containsKey("Fragment")){

        }else {
            onNavigationItemSelected(navigationView.getMenu().getItem(0));
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
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment currentFragment = fm.findFragmentById(R.id.content);

        if (id == R.id.action_settings) {
            if (!(currentFragment instanceof SettingsFragment)) {
                if(currentFragment != null){
                    ft.remove(currentFragment);
                }

                ft.add(R.id.content, settingsFragment, SettingsFragment.class.getSimpleName());
                ft.addToBackStack(SettingsFragment.class.getSimpleName());
                ft.commit();
            }
            return true;
            // Handle the camera action
        }
        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment currentFragment = fm.findFragmentById(R.id.content);
        int id = item.getItemId();

        if (id == R.id.nav_wallet) {
            if (!(currentFragment instanceof WalletFragment)) {
                if(currentFragment != null){
                    ft.remove(currentFragment);
                }

                ft.add(R.id.content, walletFragment, WalletFragment.class.getSimpleName());
                ft.addToBackStack(WalletFragment.class.getSimpleName());
                ft.commit();
            }
            // Handle the camera action
        }else if(id == R.id.nav_cryptoevent){
            if (!(currentFragment instanceof CryptoEventFragment)) {
                if(currentFragment != null){
                    ft.remove(currentFragment);
                }

                ft.add(R.id.content, cryptoEventFragment, CryptoEventFragment.class.getSimpleName());
                ft.addToBackStack(WalletFragment.class.getSimpleName());
                ft.commit();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
