package com.example.sreekanthojasengtest.ActivityClasses;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.sreekanthojasengtest.AdapterClasses.CustomAdapter;
import com.example.sreekanthojasengtest.AppClass;
import com.example.sreekanthojasengtest.ListenerClasses.UpdateCountedListLisetener;
import com.example.sreekanthojasengtest.R;
import com.example.sreekanthojasengtest.UtilClasses.HitsData;
import com.example.sreekanthojasengtest.UtilClasses.InfiniteScrollListener;
import com.example.sreekanthojasengtest.UtilClasses.Main;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements InfiniteScrollListener.OnLoadMoreListener, UpdateCountedListLisetener {

    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    ArrayList<HitsData> dataArrayList;
    CustomAdapter adapter;
    InfiniteScrollListener infiniteScrollListener;
    int pageCount = 1;
    SwipeRefreshLayout swipeContainer;
    ProgressBar progressBar;
    TextView textView_count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeUi();
    }

    private void initializeUi() {
        recyclerView = findViewById(R.id.recyclerView);
        swipeContainer = findViewById(R.id.swipeContainer);
        progressBar = findViewById(R.id.progressbar);
        textView_count = findViewById(R.id.textView_count);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        infiniteScrollListener = new InfiniteScrollListener(manager, this);
        infiniteScrollListener.setLoaded();
        recyclerView.setLayoutManager(manager);
        recyclerView.addOnScrollListener(infiniteScrollListener);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        callUriRequest(pageCount);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                callUriRequest(1);
            }
        });


    }

    private void callUriRequest(final int pageNum) {
        progressBar.setVisibility(View.VISIBLE);
        Log.e(TAG, "callUriRequest: "+pageNum);


        String url = "https://hn.algolia.com/api/v1/search_by_date?tags=story&page=" + pageNum;


        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
//                pDialog.hide();
                Gson gson = new Gson();


                if(adapter!=null){
                    Main main = gson.fromJson(response, Main.class);
                  ArrayList<HitsData>  dataArrayList1 = main.hitsData;
                    dataArrayList1.addAll(dataArrayList);
                    adapter.addData(dataArrayList1);
                    dataArrayList1=new ArrayList<>();
                }else{

                    Main main = gson.fromJson(response, Main.class);
                    dataArrayList = main.hitsData;
                    Log.e(TAG, "onResponse: " + dataArrayList.size());
                    adapter = new CustomAdapter(MainActivity.this, dataArrayList, MainActivity.this);
                    recyclerView.setAdapter(adapter);
                }
                pageCount++;

                swipeContainer.setRefreshing(false);
                progressBar.setVisibility(View.GONE);


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.getMessage());
//                pDialog.hide();
                swipeContainer.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
            }
        });

// Adding request to request queue
        AppClass.getInstance().addToRequestQueue(strReq);

    }

    @Override
    public void onLoadMore() {
        Log.e(TAG, "onLoadMore: ");
        adapter.addNullData();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.removeNull();

                callUriRequest(pageCount++);
                infiniteScrollListener.setLoaded();
            }
        }, 2000);
    }

    @Override
    public void updateCountedList(int count) {
        if (textView_count != null) {
            textView_count.setText("Selected Items: " + count);
        }

    }
}
