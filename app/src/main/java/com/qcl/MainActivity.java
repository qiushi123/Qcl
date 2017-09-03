package com.qcl;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.qcl.adapter.MyAdapter;
import com.qcl.net.HttpResponse;
import com.qcl.net.callback.JsonCallback;
import com.qcl.net.model.ItemHome;

import org.reactivestreams.Subscription;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    MyAdapter mAdapter;
    String TAG = "qclqcl";
    Subscription mSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        //创建默认的线性LayoutManager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new MyAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initData() {
        OkGo.<HttpResponse<List<ItemHome>>>get(Urls.BASEURL).
                tag(this).
                execute(new JsonCallback<HttpResponse<List<ItemHome>>>() {
                    @Override
                    public void onSuccess(HttpResponse<List<ItemHome>> response) {
                        if (response.data != null) {
                            mAdapter.setDatas(response.data);
                            mAdapter.notifyDataSetChanged();
                        }
                        //创建并设置Adapter

                    }

                    @Override
                    public void onError(Response<HttpResponse<List<ItemHome>>> response) {
                        super.onError(response);
                        Log.e(TAG, "请求失败" + response.getException().getMessage());
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.info, menu);
        //menu.add(1, Menu.FIRST, 1, "Change Site ID");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.publish:
                startActivity(new Intent(this, PublishActivity.class));
                return true;
            default:
                return false;
        }
    }

}
