package com.uf.dancemarathon;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Chris on 1/25/2016.
 */
public class ActionBarUtility {

    /**
     *
     * @param c The context
     * @param ab The action bar to modify
     * @param colorResourceId An R.color resource id (i.e. R.color.Black)
     * @param textGravity A Gravity specification like Gravity.CENTER
     * @param textSize A text size in pts
     * @param abText The text for the action bar
     * @return A reference to the TextView that the action bar is now using
     */
    public static TextView customizeActionBar(Context c, ActionBar ab, int colorResourceId, int textColorResourceId, int textGravity, int textSize, String abText)
    {
        // Set the ActionBar background color
        int color = c.getResources().getColor(colorResourceId);
        ColorDrawable cd = new ColorDrawable();
        cd.setColor(color);
        ab.setBackgroundDrawable(cd);

        // Create a TextView programmatically.
        TextView tv = new TextView(c);

        // Create a LayoutParams for TextView
        ViewGroup.LayoutParams lp = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, // Width of TextView
                ViewGroup.LayoutParams.WRAP_CONTENT); // Height of TextView

        // Apply the layout parameters to TextView widget
        tv.setLayoutParams(lp);


        tv.setText(abText);
        tv.setTextColor(c.getResources().getColor(textColorResourceId));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
        tv.setGravity(textGravity);

        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ab.setCustomView(tv);

        return tv;
    }
}
