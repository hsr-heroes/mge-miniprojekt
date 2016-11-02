package ch.hsr.mge.gadgeothek;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import ch.hsr.mge.gadgeothek.domain.Gadget;
import ch.hsr.mge.gadgeothek.domain.Loan;
import ch.hsr.mge.gadgeothek.domain.Reservation;
import ch.hsr.mge.gadgeothek.service.Callback;
import ch.hsr.mge.gadgeothek.service.LibraryService;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = getSharedPreferences("User", MODE_PRIVATE);

        LibraryService.setServerAddress(settings.getString("server", "http://mge1.dev.ifs.hsr.ch"));
        LibraryService.setTokenFromString(settings.getString("token", LibraryService.getTokenAsString()));

        drawer = (DrawerLayout) findViewById(R.id.drawerLayout);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        TextView headerServer = (TextView) navigationView.getHeaderView(0).findViewById(R.id.header_server);
        headerServer.setText(LibraryService.getServerAddress());

        getFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, GadgetListFragment.newInstance(1))
                .commit();
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void setToolbarTitle(String title) {
        toolbar.setTitle(title);
    }

    @Override
    public void onListFragmentInteraction(Object item) {
        Fragment fragment;
        if (item instanceof Gadget) {
            getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, GadgetDetailFragment.newInstance((Gadget) item))
                .addToBackStack(null)
                .commit();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.drawerGadgets:
                fragment = GadgetListFragment.newInstance(1);
                break;
            case R.id.drawerMyLoans:
                fragment = LoanListFragment.newInstance(1);
                break;
            case R.id.drawerMyReservations:
                fragment = ReservationListFragment.newInstance(1);
                break;
            default: // case R.id.drawerLogout :
                LibraryService.logout(new Callback<Boolean>() {
                    @Override
                    public void onCompletion(Boolean input) {
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("token", "");
                        editor.apply();

                        Intent intent = new Intent(getBaseContext(), StartActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(String message) {
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("token", "");
                        editor.apply();

                        Intent intent = new Intent(getBaseContext(), StartActivity.class);
                        startActivity(intent);
                    }
                });
        }
        if (fragment != null) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
        item.setChecked(true);
        drawer.closeDrawers();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

    }
}