package project.htrr.thrr;

import android.app.Fragment;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Homepage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawer;
   // private    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        // code for DB data retrive







        /* for the side menu */
        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); //set the toolbar as the action bar
        //now we create the top left button for the menu (the menu open/close button)
        drawer=findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toogle=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,
                R.string.navigation_drawer_open);
        drawer.addDrawerListener(toogle);
        toogle.syncState(); //take care of the turning-animation of the icon that we click to open the menu

        /*for the bottom menu */
        BottomNavigationView bottomNav=findViewById(R.id.bottom_HomeMenu);
        bottomNav.setOnNavigationItemSelectedListener(navListener);


        /*intent - activity handling for the side menu */
        NavigationView sideNav=findViewById(R.id.navigation_view);
        sideNav.setNavigationItemSelectedListener(this);
        //so the function below returns the fragment we want to display but there is no predefined one,
        // so we manually set one as pre-default that will start at the beginning of the app
        sideNav.setCheckedItem(R.id.side_home);
        if(savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new FragmentGeneral()).commit();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =new BottomNavigationView.OnNavigationItemSelectedListener() {
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            android.support.v4.app.Fragment selectedFragment = null;
            switch(item.getItemId()){
                case R.id.general:
                    selectedFragment=new FragmentGeneral();
                    break;
                case R.id.heartRate:
                    selectedFragment=new FragmentHeart();
                    break;
                // add the item selection
                case R.id.temperature:
                    selectedFragment=new FragmentTemperature();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
            return true;
    }
};

    //when the app is closed or move to another activity we want to close the menu first, so close it and the make the action
    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        android.support.v4.app.Fragment selectedFragment = null;
        switch(item.getItemId()){
            case R.id.side_home:
                selectedFragment=new FragmentGeneral();
                break;
            case R.id.side_settings:
                Intent goToSettings=new Intent(getApplicationContext(),Settings.class);
                startActivity(goToSettings);
                break;
            // add the item selection
            case R.id.side_help:
                Intent goToHelp=new Intent(getApplicationContext(),Help.class);
                startActivity(goToHelp);
                break;
            case R.id.side_exit:
                Toast.makeText(this,"log out",Toast.LENGTH_SHORT).show();
                break;
            case R.id.side_log_out:
                finish();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
