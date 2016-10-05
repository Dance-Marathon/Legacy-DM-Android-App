package com.uf.dancemarathon;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class MtkFragment extends Fragment
{
	private GridView gridview;
	private MtkAdapter adapter;
	private Context mActivity;

	public MtkFragment()
	{
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.fragment_mtk, container, false);
		gridview = (GridView) v.findViewById(R.id.mtk_gridview);
	    adapter = new MtkAdapter(mActivity);
	    gridview.setAdapter(adapter);
		gridview.setOnItemClickListener(new OnItemClickListener() 
	    {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position,
                    long id) 
            {

        		Intent intent = new Intent(getActivity(), MtkProfile.class);
        		Bundle b = new Bundle();
        		b.putParcelable("kid", (Kid)parent.getItemAtPosition(position));
        		intent.putExtras(b);
        		startActivity(intent);
        }
	    });
		// Inflate the layout for this fragment
		return v;
	}
	

	public static MtkFragment newInstance(Context c)
	 {
		 MtkFragment f = new MtkFragment();
		 f.mActivity = c;
		 return f;
	 }
	
}