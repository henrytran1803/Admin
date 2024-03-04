package com.example.admin;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class HttpUtils {
    private static final String BASE_URL = "https://7544-2001-ee0-5006-5c70-b9b1-4338-8c6a-31e3.ngrok-free.app/revenue/";

    private static AsyncHttpClient client = new AsyncHttpClient();
    public static void getFiveRecentYears(int year, AsyncHttpResponseHandler responseHandler) {
        String url = BASE_URL + "year/"+year;
        client.get(url, null, responseHandler);
    }
    public static void getMonthRecentYears(int year,int month, AsyncHttpResponseHandler responseHandler) {
        String url = BASE_URL + "month/"+month+ "/year/"+year;
        client.get(url, null, responseHandler);
    }
    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void getByUrl(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(url, params, responseHandler);
    }

    public static void postByUrl(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(url, params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}