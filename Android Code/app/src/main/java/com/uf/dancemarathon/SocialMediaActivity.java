package com.uf.dancemarathon;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class SocialMediaActivity extends AppCompatActivity {

	private String ACTION_BAR_TITLE = "Dance Marathon @ UF";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_social_media);

		//Add Twitter Timeline fragment
		FragmentManager fm = getSupportFragmentManager();
		fm.beginTransaction().replace(R.id.twitter_timeline_fragment_container, new TwitterTimelineFragment()).commit();

		//Customize action bar
		ActionBar bar = getSupportActionBar();
		bar.setTitle(ACTION_BAR_TITLE);

		int color = getResources().getColor(R.color.action_bar_color);
		ColorDrawable cd = new ColorDrawable();
		cd.setColor(color);
		bar.setBackgroundDrawable(cd);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_mtk, menu);

		return false; //return false to hide the menu
	}
}
