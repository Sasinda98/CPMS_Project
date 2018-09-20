package com.construction.app.cpms.Plan;
import com.bumptech.glide.Glide;
import com.construction.app.cpms.R;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class displayPlan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_plan);

        getIncomingIntent();
    }

    private void getIncomingIntent(){
        if (getIntent().hasExtra("name") && getIntent().hasExtra("image") && getIntent().hasExtra("description")){
            String Name = getIntent().getStringExtra("name");
            String Image = getIntent().getStringExtra("image");
            String Description = getIntent().getStringExtra("description");

            setImage(Name, Image, Description);
        }
    }

    private void setImage(String Name, String Image, String description){
        EditText name = findViewById(R.id.d_name);
        name.setText(Name);

        EditText des = findViewById(R.id.d_description);
        des.setText(description);

        ImageView image = findViewById(R.id.d_image);
        Glide.with(this)
                .load(Image)
                .asBitmap()
                .into(image);
    }


}
/**Note
 *In display plans changing the textView to editText makes the editing of description possible
 * But try to find out a way to edit the textView
 * According to research, textView has limitations, as in, textView can be chaned to ditable, but the existing data might not exactly change
 */
