package com.live.fox.network;

import androidx.appcompat.app.AppCompatActivity;

import com.live.fox.R;

import org.json.JSONException;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;

import retrofit2.HttpException;

/**
 * 网络错误
 */
public class NetworkException {

    public static String getExceptInfo(Throwable throwable, AppCompatActivity activity) {

        String exceptInfo = activity.getString(R.string.app_network_error_unknown);

        if (throwable instanceof UnknownHostException) {
            exceptInfo = activity.getString(R.string.app_network_error_no_network);
        } else if (throwable instanceof SocketTimeoutException) {
            exceptInfo = activity.getString(R.string.app_network_error_time_out);
        } else if (throwable instanceof HttpException) {
            HttpException httpException = (HttpException) throwable;
            exceptInfo = networkServiceExceptInfo(httpException, activity);
        } else if (throwable instanceof ParseException || throwable instanceof JSONException) {
            exceptInfo = activity.getString(R.string.app_network_error_gson);
        }
        return exceptInfo;
    }

    public static String networkServiceExceptInfo(HttpException networkException, AppCompatActivity activity) {
        String exceptInfo = activity.getString(R.string.app_network_service);
        int code = networkException.code();
        if (code >= 500 && code < 600) {
            exceptInfo = activity.getString(R.string.app_network_service_500);
        } else if (code >= 400 && code < 500) {
            exceptInfo = activity.getString(R.string.app_network_service_400);
        } else if (code >= 300 && code < 400) {
            exceptInfo = activity.getString(R.string.app_network_service_300);
        }
        return exceptInfo;
    }
}
