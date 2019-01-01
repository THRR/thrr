package project.htrr.thrr;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class FragmentTemperature extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_temperature,container,false);
        String mex="Date: 28/12/2018 Temp: 34 degree C";
        String[] mylist={mex,mex,mex,mex};

        ListAdapter temperatureAdapterList = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, mylist);
        ListView temperature_listView=(ListView)view.findViewById(R.id.temperature_listView);
        temperature_listView.setAdapter(temperatureAdapterList);

        return view;
    }
}
