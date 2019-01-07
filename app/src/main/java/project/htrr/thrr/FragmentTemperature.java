package project.htrr.thrr;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class FragmentTemperature extends Fragment {
    @Nullable

    private FirebaseAuth mAuth;
    View view;
    List<thrrReadings> arraylist= new ArrayList<thrrReadings>();
    ListAdapter temperatureAdapterList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_temperature,container,false);
        String mex="Date: 28/12/2018 Temp: 34 degree C";
        String[] mylist={mex,mex,mex,mex};
        if(!isNetworkAvailable()) {
            retriveOfflineData();
            temperatureAdapterList = new ArrayAdapter<thrrReadings>(getActivity(),android.R.layout.simple_list_item_1, arraylist);
            ListView temperature_listView=(ListView)view.findViewById(R.id.temperature_listView);
            temperature_listView.setAdapter(temperatureAdapterList);
        }else{
            SetListViewTemperature();

        }
        return view;
    }

    public boolean SetListViewTemperature(){
        // Get a reference to our posts
        mAuth=mAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        final FirebaseDatabase database =FirebaseDatabase.getInstance();

        DatabaseReference ref = database.getReference("readings").child(user.getUid());
        //database=FirebaseDatabase.getInstance();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    for (DataSnapshot rd : dataSnapshot.getChildren()) {

                        thrrReadings readings=new thrrReadings();
                        readings.setTemperature(rd.getValue(thrrReadings.class).getTemperature());
                        readings.setTimestamp(rd.getValue(thrrReadings.class).getTimestamp());


                        //   Log.d("value", readings.PrintMe());
                        arraylist.add(readings);
                        Log.d("value", readings.PrintMe());

                    }
                    if(LoginActivity.CanISaveDataOffline==true){
                        SaveOfflineData();
                    }else{
                        //if i set to not remember me I reset the login of offline Data
                        resetOfflineData();
                    }
                    temperatureAdapterList = new ArrayAdapter<thrrReadings>(getActivity(),android.R.layout.simple_list_item_1, arraylist);
                    ListView temperature_listView=(ListView)view.findViewById(R.id.temperature_listView);
                    temperature_listView.setAdapter(temperatureAdapterList);

                    // System.out.print(arraylist.toString());
                    Log.d("array1",arraylist.toString());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println(databaseError.getCode());
                Log.d("faile",databaseError.getMessage());
            }
        });
        return true;
    }


    public void SaveOfflineData(){
        Gson gson = new Gson();
        String userInfoListJsonString = gson.toJson(arraylist);

        // Create SharedPreferences object.
        Context ctx = getActivity();
        SharedPreferences sharedPreferences = ctx.getSharedPreferences("TemperatureReadings", ctx.MODE_PRIVATE);

        // Put the json format string to SharedPreferences object.
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("readings", userInfoListJsonString);
        editor.apply();

        // Popup a toast message in screen bottom.
        Toast.makeText(ctx, "Saving Complete", Toast.LENGTH_SHORT).show();

    }

    public void resetOfflineData(){
        Context ctx = getActivity();
        SharedPreferences sharedPreferences = ctx.getSharedPreferences("TemperatureReadings", ctx.MODE_PRIVATE);

        // Put the json format string to SharedPreferences object.
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("readings", "");
        editor.apply();
    }

    public void retriveOfflineData(){
        Context ctx = getActivity();
        SharedPreferences sharedPreferences = ctx.getSharedPreferences("TemperatureReadings", ctx.MODE_PRIVATE);
        Gson gson = new Gson();
        String userInfoListJsonString = sharedPreferences.getString("readings", "");

        arraylist=gson.fromJson(userInfoListJsonString, new TypeToken<ArrayList<thrrReadings>>(){}.getType());

        for(thrrReadings a : arraylist){
            Log.d("array", a.toString());
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager =(ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
