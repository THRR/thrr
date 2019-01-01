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

public class FragmentHeart extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_heart,container,false);
        String mex="Date: 28/12/2018  HeartRate: 78bp";
        String[] mylist={mex,mex,mex,mex};

        ListAdapter generalAdapterList = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, mylist);
        ListView heart_listView=(ListView)view.findViewById(R.id.heart_listView);
        heart_listView.setAdapter(generalAdapterList);
        return  view;


    }
}
