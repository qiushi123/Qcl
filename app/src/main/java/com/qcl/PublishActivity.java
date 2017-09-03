package com.qcl;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.qcl.utils.ToastUtil;

public class PublishActivity extends BaseActivity {
    EditText edId;
    EditText edTitle;
    EditText edUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        initView();
    }

    private void initView() {
        edId = (EditText) findViewById(R.id.ed_id);
        edTitle = (EditText) findViewById(R.id.ed_title);
        edUrl = (EditText) findViewById(R.id.ed_url);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addData();
            }
        });
    }



    //添加数据
    private void addData() {
        OkGo.<String>post(Urls.BASEURL_PUBLISH)
                .tag(this)
                .params("id", edId.getText().toString())
                .params("title", edTitle.getText().toString())
                .params("url", edUrl.getText().toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        ToastUtil.showShort(PublishActivity.this, R.string.sucess);
                        finish();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        ToastUtil.showShort(PublishActivity.this, R.string.no_sucess);
                    }
                });
    }

}
