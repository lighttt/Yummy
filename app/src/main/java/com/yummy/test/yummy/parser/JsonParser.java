package com.yummy.test.yummy.parser;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ranja_000 on 8/29/2016.
 */
public class JsonParser {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    public JSONObject performPostCI(String requestURL,
                                    HashMap<String, String> postDataParams) {
        String json = null;
        JSONObject jObj = null;
        HttpURLConnection connection = null;
        InputStream in = null;
        String line = null;
        try {
            Log.e("url", requestURL);
            URL u;
            // Create connection
            u = new URL(requestURL);
            connection = (HttpURLConnection) u.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            connection.setDoOutput(true);
            connection.setConnectTimeout(10000);
            connection.connect();

            // Send request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(getPostDataString(postDataParams));
            try {
                wr.flush();
                wr.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }


        } catch (UnknownHostException e) {
            Log.e("connection", "failed here");
            e.printStackTrace();
            return jObj;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
        try {
            int status = connection.getResponseCode();
            if (status >= 200)
                in = connection.getInputStream();
            else
                in = connection.getErrorStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    in));
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            json = sb.toString();
            Log.e("response data", json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (json != null)
                jObj = new JSONObject(json);
            else {
                return jObj;
            }
        } catch (JSONException e) {
        } finally {
            connection.disconnect();
        }
        return jObj;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    public JSONObject getForJSONObject(String url,
                                       HashMap<String, String> entry) {
        String json = null;
        JSONObject jObj = null;
        HttpURLConnection connection = null;
        try {

//            Log.e("url", url);
            URL u;
            // Create connection
            u = new URL(url + "?" + entry.toString());
            connection = (HttpURLConnection) u.openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.setChunkedStreamingMode(0);

            connection.connect();

        } catch (UnknownHostException e) {
            Log.e("connection", "failed here");
            e.printStackTrace();
            return jObj;
        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            json = sb.toString();

//            Log.i("JSON", json);
        } catch (Exception e) {
        }
        try {
            if (json != null)
                jObj = new JSONObject(json);
            else {
                return jObj;
            }
        } catch (JSONException e) {
        } finally {
            connection.disconnect();
        }
        return jObj;
    }
}
