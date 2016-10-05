package com.uf.dancemarathon;

import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class CalendarActivity extends AppCompatActivity {

    private String ACTION_BAR_TITLE = "Calendar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        //Add Calendar fragment
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.calendar_fragment_container, CalendarFragment.newInstance(this)).commit();

        //Customize action bar
        ActionBar bar = getSupportActionBar();
        TextView customBar = ActionBarUtility.customizeActionBar(this, bar, R.color.action_bar_color, R.color.White, Gravity.CENTER, 20, ACTION_BAR_TITLE);
        FontSetter.setFont(this, FontSetter.fontName.ALTB, customBar);

        int color = getResources().getColor(R.color.action_bar_color);
        ColorDrawable cd = new ColorDrawable();
        cd.setColor(color);
        bar.setBackgroundDrawable(cd);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calendar, menu);

        return false; //Return false to hide the menu
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
