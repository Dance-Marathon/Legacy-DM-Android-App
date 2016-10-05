package com.uf.dancemarathon;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class NoStoryAvailableFragment extends Fragment {


    public NoStoryAvailableFragment() {
        // Required empty public constructor
    }

    public static NoStoryAvailableFragment newInstance()
    {
        NoStoryAvailableFragment lf = new NoStoryAvailableFragment();
        return lf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_no_story_available, container, false);
        TextView text = (TextView) v.findViewById(R.id.no_story_available_text);
        FontSetter.setFont(getActivity(), FontSetter.fontName.P, text);
        return v;
    }

}
