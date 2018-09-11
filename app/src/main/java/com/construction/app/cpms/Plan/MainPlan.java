package com.construction.app.cpms.Plan;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ListView;

import com.construction.app.cpms.MainActivity;
import com.construction.app.cpms.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainPlan extends AppCompatActivity {

    //new
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private CustomAdapter adapter;
    private List<MyData> data_list;


    FloatingActionButton fab;
    private static final String TAG = "mainPlan";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_plan);

       /* //Navigate from Plan to Upload plan through a fab
        fab = (FloatingActionButton) findViewById(R.id.fabP);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainPlan.this, addPlan.class);
                startActivity(myIntent);
            }
        }); */

        Log.d(TAG, "onCreate: Started");

        //new stuff, just in case delete everything below
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        data_list = new ArrayList<>();
        load_data_from_server(0);

        gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new CustomAdapter(this, data_list);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (gridLayoutManager.findLastCompletelyVisibleItemPosition() == data_list.size() - 1) {
                    load_data_from_server(data_list.get(data_list.size() - 1).getPid());
                }
            }
        });
    }

    private void load_data_from_server(final int pID){
        AsyncTask<Integer,Void,Void> task = new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... integers) {

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("https://projectcpms99.000webhostapp.com/scripts/Harshan/fetchPlans.php?id="+pID)
                        .build();
                try {
                    Response response = client.newCall(request).execute();

                    JSONArray array = new JSONArray(response.body().string());

                    for(int i = 0; i<array.length(); i++){
                        JSONObject object = array.getJSONObject(i);
                        MyData data = new MyData(object.getInt("pID"),object.getString("Name"),object.getString("Image"));

                        data_list.add(data);
                    }
                    adapter.notifyDataSetChanged();

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    System.out.println("End of Content");
                }
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid){
                adapter.notifyDataSetChanged();
            }
        };
        task.execute();
    }


}






  /* restore      ListView pListView = (ListView) findViewById(R.id.listViewPlan);

        ArrayList<String> planList = new ArrayList<>();
        planList.add(getString(R.string.planDescription));
        planList.add(getString(R.string.planDescription));
        planList.add(getString(R.string.planDescription));
        planList.add(getString(R.string.planDescription));
        planList.add(getString(R.string.planDescription));
        planList.add(getString(R.string.planDescription));
        planList.add(getString(R.string.planDescription));
        planList.add(getString(R.string.planDescription));
        planList.add(getString(R.string.planDescription));
        planList.add(getString(R.string.planDescription));
        planList.add(getString(R.string.planDescription));
        planList.add(getString(R.string.planDescription));
        planList.add(getString(R.string.planDescription));
        planList.add(getString(R.string.planDescription));
        planList.add(getString(R.string.planDescription));
        planList.add(getString(R.string.planDescription));
        planList.add(getString(R.string.planDescription));
        planList.add(getString(R.string.planDescription));
        planList.add(getString(R.string.planDescription));
        planList.add(getString(R.string.planDescription));
        planList.add(getString(R.string.planDescription));
        planList.add(getString(R.string.planDescription));
        planList.add(getString(R.string.planDescription));
        planList.add(getString(R.string.planDescription));
        planList.add(getString(R.string.planDescription));

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, planList);
        pListView.setAdapter(adapter); */


        /*//Navigate backwards from AddPlan to Plan
        ImageButton backBn = (ImageButton)findViewById(R.id.backButton);
        backBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myyIntent = new Intent(addPlan.this, MainPlan.class);
                startActivity(myyIntent);
            }
        });
    }
}
*/