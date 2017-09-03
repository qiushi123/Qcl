package com.qcl.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.qcl.R;
import com.qcl.Urls;
import com.qcl.net.model.ItemHome;
import com.qcl.utils.ToastUtil;
import com.qcl.web.BrowserActivity;

import java.util.ArrayList;
import java.util.List;

import static com.qcl.web.BrowserActivity.PARAM_URL;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    List<ItemHome> mDatas = new ArrayList<ItemHome>();
    Activity mActivity;

    public MyAdapter(Activity activity) {
        mActivity = activity;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_home, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    public void setDatas(List<ItemHome> data) {
        mDatas = data;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.tvId.setText("我的id为" + mDatas.get(position).id);
        viewHolder.mTitle.setText(mDatas.get(position).title);
        viewHolder.mTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, BrowserActivity.class);
                intent.putExtra(PARAM_URL, mDatas.get(position).url);
                mActivity.startActivity(intent);
            }
        });
        viewHolder.mTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ToastUtil.showShort(mActivity, "长按删除了");
                showBar(position);
                return true;
            }
        });

    }


    //获取数据的数量
    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTitle;
        public TextView tvId;

        public ViewHolder(View view) {
            super(view);
            mTitle = (TextView) view.findViewById(R.id.title);
            tvId = (TextView) view.findViewById(R.id.tv_id);
        }
    }


    /*
    * 长按删除
    * */
    private void showBar(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("确定要删除这条数据吗");
        builder.setMessage("删除后将无法恢复");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (TextUtils.isEmpty(mDatas.get(position).id)) {
                    ToastUtil.showShort(mActivity, "id不能为空");
                } else {
                    deleteData(mDatas.get(position).id, position);
                }
            }
        });
        if (mActivity != null && !mActivity.isFinishing()) {
            builder.show();
        }
    }

    //删除数据
    private void deleteData(String id, final int position) {
        OkGo.<String>post(Urls.BASEURL_DELETE)
                .tag(this)
                .params("id", Integer.parseInt(id))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        ToastUtil.showShort(mActivity, R.string.sucess);
                        mDatas.remove(position);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        ToastUtil.showShort(mActivity, R.string.no_sucess);
                    }
                });
    }

}
