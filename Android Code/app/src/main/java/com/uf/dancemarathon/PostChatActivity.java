package com.uf.dancemarathon;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;

public class PostChatActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_chat);

        Intent intent = getIntent();
    }

}
