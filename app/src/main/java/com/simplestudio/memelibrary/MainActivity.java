package com.simplestudio.memelibrary;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.service.voice.VoiceInteractionSession;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    RecyclerView memRecycler;
    ArrayList<MemeModel> modelArrayList;
    MemeAdaptor memeAdaptor;
    SwipeRefreshLayout swipeRefreshLayout1;
    SnapHelper snapHelper;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        memRecycler = findViewById(R.id.memeRecyclerView);
        swipeRefreshLayout1 = findViewById(R.id.swipeRefreshLayout);
        modelArrayList = new ArrayList<>();
        snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(memRecycler);
        loadMeme();
        memeAdaptor = new MemeAdaptor(MainActivity.this,modelArrayList);
        memRecycler.setAdapter(memeAdaptor);

        swipeRefreshLayout1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                modelArrayList.clear();
                loadMeme();
                swipeRefreshLayout1.setRefreshing(false);
            }
        });




    }

    private void loadMeme(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://meme-api.herokuapp.com/gimme/50";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("memes");

                            for(int i=0;i< jsonArray.length();i++){

                                JSONObject object = jsonArray.getJSONObject(i);
                                MemeModel model = new MemeModel(object.getString("title"), object.getString("url") );
                                modelArrayList.add(model);
                            }

                            memeAdaptor.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsonObjectRequest);
    }

    @Override
    protected void onStop() {
        super.onStop();
        deleteCache(MainActivity.this);
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static boolean deleteDir(File dir) {
        if(dir != null && dir.isDirectory()) {
            String[] children = dir.list();

            for (int i = 0; i < Objects.requireNonNull(children).length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));

                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        }
        else if(dir!=null && dir.isFile())
        {
            return dir.delete();
        }
        else{
            return false;
        }


    }
}