package com.uf.dancemarathon;


import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;


public class TwitterTimelineFragment extends ListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final UserTimeline userTimeline = new UserTimeline.Builder()
                .screenName("floridadm")
                .build();

        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(getActivity())
                .setTimeline(userTimeline)
                .build();
        
        setListAdapter(adapter);


//        final UserTimeline userTimeline1 = new UserTimeline.Builder()
//                .screenName("CMNHospitals")
//                .build();
//
//        final TweetTimelineListAdapter adapter1 = new TweetTimelineListAdapter.Builder(getActivity())
//                .setTimeline(userTimeline1)
//                .build();
//
//        setListAdapter(adapter1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_twitter_timeline, container, false);
    }


}
