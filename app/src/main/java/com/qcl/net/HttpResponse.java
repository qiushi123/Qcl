package com.qcl.net;

import java.io.Serializable;

/**
 * 请求数据的公有response
 */
public class HttpResponse<T> implements Serializable {
    public T data;
    public boolean sucess;
    public Error error;
}