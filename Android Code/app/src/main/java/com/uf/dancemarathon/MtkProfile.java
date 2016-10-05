package com.uf.dancemarathon;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class MtkProfile extends AppCompatActivity
{
	private Kid kid;
    private Button titleButton;

    private String ACTION_BAR_TITLE = "Meet The Kids";

	public MtkProfile()
	{
		// Required empty public constructor
	}
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mtk_profile);
		
		//Get user from intent
		Kid kid = getIntent().getExtras().getParcelable("kid");
		this.kid = kid;

        setFields(kid);


		//Customize action bar
		ActionBar bar = getSupportActionBar();
		TextView customBar = ActionBarUtility.customizeActionBar(this, bar, R.color.action_bar_color, R.color.White, Gravity.CENTER, 20, ACTION_BAR_TITLE);
		FontSetter.setFont(this, FontSetter.fontName.ALTB, customBar);
	}
	
	public static MtkProfile newInstance()
	{
		MtkProfile f = new MtkProfile();
		return f;
	}
	
	private void setFields(final Kid kid)
	{
		ImageView pic  = (ImageView) findViewById(R.id.kid_pic);
		TextView story = (TextView) findViewById(R.id.kid_story);
        FontSetter.setFont(this, FontSetter.fontName.AGBReg, story);

        pic.setImageResource(kid.getImageId(this));

        story.setText(kid.getStory());
        FontSetter.setFont(this, FontSetter.fontName.ALTMO, story);

        setupStoryPanel();
        setupTitleButton();
	}

    private void setupTitleButton(){
        titleButton = (Button) findViewById(R.id.kid_title_button);

        final String milestone_id = kid.getYoutube_id();
        if (kid.hasMilestone())
        {
            titleButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    openLink(milestone_id);

                }

            });
        }
        else
         titleButton.setClickable(false);

        setTitleButtonText(kid.hasMilestone());
    }
    private void setTitleButtonText(boolean hasMilestone){
        String name = kid.getName();

        if(hasMilestone){
            titleButton.setText("Watch\n" + name + "'s\nMilestone");
            titleButton.setPaintFlags(titleButton.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            FontSetter.setFont(this, FontSetter.fontName.P, titleButton);
        }
        else{
            titleButton.setText(name);
            FontSetter.setFont(this, FontSetter.fontName.P, titleButton);
        }
    }

    private void setupStoryPanel(){
        if(!kid.hasStory()){

            //Clear out all views from the story scroll view
            ScrollView scroll = (ScrollView) findViewById(R.id.story_scroll_view);
            scroll.removeAllViews();

            //Add NoStoryAvailableFragment
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.story_scroll_view, NoStoryAvailableFragment.newInstance()).commit();
        }
    }


	public void openLink(String youtubeId)
	{
	    //Log.d("tag", youtubeId);
	    Intent intent = getOpenYouTubeIntent(this, youtubeId);
	    startActivity(intent);
	}
	
	public static Intent getOpenYouTubeIntent(Context context, String id)
	{
		
		// Open YouTube video in YouTube app
		try
		{
			context.getPackageManager().getPackageInfo("com.google.android.youtube", 0);
			return new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
		}
		// Open YouTube video in browser
		catch (Exception e)
		{
			return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + id));
		}
	}

}
