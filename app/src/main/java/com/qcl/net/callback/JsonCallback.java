package com.qcl.net.callback;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.lzy.okgo.callback.AbsCallback;
import com.qcl.net.HttpResponse;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.UnknownHostException;

import okhttp3.Response;
import okhttp3.ResponseBody;

public abstract class JsonCallback<T> extends AbsCallback<T> {

    private boolean mHideErrorToast;

    public JsonCallback() {
    }

    public JsonCallback(boolean hideErrorToast) {
        this.mHideErrorToast = hideErrorToast;
    }

    @Override
    public T convertResponse(Response response) throws Throwable {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        Type type = params[0];

        if (!(type instanceof ParameterizedType))
            throw new IllegalStateException("没有填写泛型参数");
        Type rawType = ((ParameterizedType) type).getRawType();
        Type typeArgument = ((ParameterizedType) type).getActualTypeArguments()[0];

        ResponseBody body = response.body();
        if (body == null)
            return null;
        Gson gson = new Gson();
        JsonReader jsonReader = new JsonReader(body.charStream());
        if (rawType != HttpResponse.class) {
            T data = gson.fromJson(jsonReader, type);
            response.close();
            return data;
        } else {
            if (typeArgument == Void.class) {
                HttpResponse httpResponse = gson.fromJson(jsonReader, HttpResponse.class);
                response.close();
                return (T) httpResponse;
            } else {
                HttpResponse httpResponse = gson.fromJson(jsonReader, type);
                response.close();

                if (!httpResponse.sucess) {
                    //                    Error error = httpResponse.error;
                    //                    if (!TextUtils.isEmpty(error.msg)) {
                    //                        throw new IllegalStateException(error.msg);
                    //                    }
                    //
                    //                    if (!TextUtils.isEmpty(error.message)) {
                    //                        throw new IllegalStateException(error.message);
                    //                    }
                    throw new IllegalStateException("后台返回错误");
                } else {
                    return (T) httpResponse;
                }
            }
        }
    }

    @Override
    public void onError(com.lzy.okgo.model.Response<T> response) {
        super.onError(response);
        Throwable exception = response.getException();
        if (exception == null)
            return;

        if (exception instanceof IllegalStateException) {
            if (!mHideErrorToast) {
                if (!TextUtils.isEmpty(exception.getMessage())) {
                    //                    ToastUtil.showShort();
                    //                    Utils.showToast(exception.getMessage());
                }
            }
            return;
        }

        if (exception instanceof UnknownHostException) {
            if (!mHideErrorToast) {
                //                Utils.showToast(R.string.generic_check);
            }
        }
    }

    @Override
    public void onSuccess(com.lzy.okgo.model.Response<T> response) {
        try {
            T t = response.body();
            if (t != null) {
                onSuccess(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCacheSuccess(com.lzy.okgo.model.Response<T> response) {
        super.onCacheSuccess(response);
        try {
            T t = response.body();
            if (t != null) {
                onCacheSuccess(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onCacheSuccess(T t) {
    }

    public abstract void onSuccess(T t);
}