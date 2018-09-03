package com.construction.app.cpms.miscellaneous;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.construction.app.cpms.R;
import com.construction.app.cpms.miscellaneous.bean.ForumPost;

public class addForumPost extends AppCompatActivity {

    private Toolbar toolbar;
    private AppCompatActivity appCompatActivity;

    private TextInputEditText title;
    private TextInputEditText description; //body


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_forum_post);

        this.title = findViewById(R.id.ap_forumTitle_editText);
        this.description = findViewById(R.id.ap_forumDescription_editText);

        setUpToolbar(); //Toolbar init

    }

    public void setUpToolbar(){

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Create Post");

        toolbar.setNavigationIcon(R.drawable.ic_forum_down_arrow);
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.btm_navbar_item_notchecked));
        appCompatActivity = this;
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //used stackoverflow forum to find this..
                appCompatActivity.onBackPressed();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        //getActivity().getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        getMenuInflater().inflate(R.menu.forum_create_post_toolbar,menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.cp_createPost:
                Toast toast = Toast.makeText(this, "submit selected" + title.getText() +" " + description.getText(), Toast.LENGTH_SHORT);
                toast.show();
                ForumPost insertThis = new ForumPost("",this.title.getText().toString(), "12", this.description.getText().toString());  /*Login change requiedr*/
                ForumPost.insertPost(this, insertThis,"12");    /*Login change required*/
                appCompatActivity.onBackPressed();  //return to main page

                break;
            }

        return super.onOptionsItemSelected(item);
    }
}
