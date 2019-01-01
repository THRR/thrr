package project.htrr.thrr;

import android.app.Activity;
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

public class FragmentGeneral extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       //create and fill the layout
       String mex="Date: 28/12/2018  HeartRate: 78bp  Temp: 34 degree C";
       String[] mylist={mex,mex,mex,mex};

        ListAdapter generalAdapterList = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, mylist);

      //I inflate the view, save it in a variable then i modify it adding the list, after I return it to the calling activity
        View view=inflater.inflate(R.layout.fragment_general,container,false);

        ListView general_listView;
        general_listView=(ListView)view.findViewById(R.id.general_listView);
        general_listView.setAdapter(generalAdapterList);
        return  view;
    }
}
