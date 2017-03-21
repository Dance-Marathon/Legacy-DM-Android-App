package com.uf.dancemarathon;

import android.os.Bundle;
import android.app.Activity;
import android.widget.ListView;

public class ChatActivity extends Activity {

    private String ACTION_BAR_TITLE = "Chat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ListView list = (ListView) findViewById(R.id.chat_list);


    }

}
