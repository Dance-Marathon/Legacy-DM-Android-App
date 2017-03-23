package com.uf.dancemarathon;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ListView;

public class ChatActivity extends Activity {

    private String ACTION_BAR_TITLE = "Chat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ListView list = (ListView) findViewById(R.id.chat_list);


    }

    /** Called when the user taps the Send button */
    public void sendMessage(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, PostChatActivity.class);
        startActivity(intent);

    }
}

