package project.htrr.thrr;

import android.app.Activity;
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

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class FragmentGeneral extends Fragment {
    private FirebaseAuth mAuth;
    View view;
    List<thrrReadings> arraylist= new ArrayList<thrrReadings>();
    ListAdapter generalAdapterList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       //create and fill the layout
       String mex="Date: 28/12/2018  HeartRate: 78bp  Temp: 34 degree C";
       String[] mylist={mex,mex,mex,mex};

// ------ code for listview DB

        view=inflater.inflate(R.layout.fragment_general,container,false);

        if(!isNetworkAvailable()){
            retriveOfflineData();
            generalAdapterList = new ArrayAdapter<thrrReadings>(getActivity(),android.R.layout.simple_list_item_1,arraylist );
            ListView general_listView;
            general_listView=(ListView)view.findViewById(R.id.general_listView);
            general_listView.setAdapter(generalAdapterList);

            //call the function to load preferences and fill up arraylist
            //fill up listview with arreylist
        }else{

            //do normal activity and save offline data in case
        SetListView();
        Log.d("array2",arraylist.toString());

        }
        return view;

      //I inflate the view, save it in a variable then i modify it adding the list, after I return it to the calling activity
    }

    public boolean SetListView(){
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

                        thrrReadings readings =new thrrReadings();//= rd.getValue(thrrReadings.class);
                        readings.setTimestamp(rd.getValue(thrrReadings.class).getTimestamp());
                        readings.setHr(rd.getValue(thrrReadings.class).getHr());
                        readings.setTemperature(rd.getValue(thrrReadings.class).getTemperature());

                        //   Log.d("value", readings.PrintMe());
                        arraylist.add(readings);
                        Log.d("value", readings.PrintMe());

                    }
                    //if the remember button is pressed I will save the arraylist, means the next login will have this data in offline mode
                    if(LoginActivity.CanISaveDataOffline==true){
                        SaveOfflineData();
                    }else{
                        //if i set to not remember me I reset the login of offline Data
                        resetOfflineData();
                    }
                    generalAdapterList = new ArrayAdapter<thrrReadings>(getActivity(),android.R.layout.simple_list_item_1,arraylist );
                    ListView general_listView;
                    general_listView=(ListView)view.findViewById(R.id.general_listView);
                    general_listView.setAdapter(generalAdapterList);

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
        SharedPreferences sharedPreferences = ctx.getSharedPreferences("GeneralReadings", ctx.MODE_PRIVATE);

        // Put the json format string to SharedPreferences object.
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("readings", userInfoListJsonString);
        editor.apply();

        // Popup a toast message in screen bottom.
        Toast.makeText(ctx, "Saving Complete", Toast.LENGTH_SHORT).show();

    }

    public void resetOfflineData(){
        Context ctx = getActivity();
        SharedPreferences sharedPreferences = ctx.getSharedPreferences("GeneralReadings", ctx.MODE_PRIVATE);

        // Put the json format string to SharedPreferences object.
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("readings", "");
        editor.apply();
    }

    public void retriveOfflineData(){
        Context ctx = getActivity();
        SharedPreferences sharedPreferences = ctx.getSharedPreferences("GeneralReadings", ctx.MODE_PRIVATE);
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
