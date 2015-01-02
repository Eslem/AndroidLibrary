package com.alzatezabala.sazal.network;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Request {

    private String url;
    private Map<String, String> params;
    private NotifyRequest notifyRequest;

    public Request(String url, Map<String, String> par) {
        this.url = url;
        this.params = par;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public NotifyRequest getNotifyRequest() {
        return notifyRequest;
    }

    public void setNotifyRequest(NotifyRequest notifyRequest) {
        this.notifyRequest = notifyRequest;
    }

    public Request(String url) {
        this.url = url;
    }

    public void setParams(Map<String, String> par) {
        this.params = par;
    }

    public void post() {
        new AsyncTask<Request, Void, String>() {
            @Override
            protected String doInBackground(Request... params) {
                return params[0].request();
            }

            protected void onPostExecute(String msg) {
                if (notifyRequest != null) {
                    notifyRequest.postFinished(msg);
                }
                Log.d("post", msg);
            }

        }.execute(this);
    }


    public void get() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                InputStream inputStream = null;
                String result = "";
                try {

                    // create HttpClient
                    HttpClient httpclient = new DefaultHttpClient();
                    httpclient.getParams().setParameter("http.protocol.content-charset", HTTP.UTF_8);

                    // make GET request to the given URL
                    HttpResponse httpResponse = httpclient.execute(new HttpGet(url));


                    // receive response as inputStream
                    HttpEntity httpEntity = httpResponse.getEntity();
                    //Log.d("get", EntityUtils.toString(httpResponse.getEntity()));


                    // convert inputstream to string
                    if (httpEntity != null)
                        result = EntityUtils.toString(httpEntity).trim();
                    else
                        result = "Did not work!";

                } catch (Exception e) {
                    Log.d("InputStream", e.getLocalizedMessage());
                }

                return result;
            }

            protected void onPostExecute(String msg) {
                if (notifyRequest != null) {
                    notifyRequest.postFinished(msg);
                }
                Log.d("get", msg);
            }

        }.execute();
    }

    public String request() {
        String responseStr = "";
        String postReceiverUrl = url;
        // HttpClient
        HttpClient httpClient = new DefaultHttpClient();

        // post header
        HttpPost httpPost = new HttpPost(postReceiverUrl);

        // add your data
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

        if (params.isEmpty()) {
            Log.d("Post", "Empty params");
        } else {
            for (Entry<String, String> e : params.entrySet()) {
                nameValuePairs.add(new BasicNameValuePair(e.getKey(), e
                        .getValue()));
            }


            try {
                UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
                httpPost.setEntity(urlEncodedFormEntity);
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                Log.v("PostLib", e.toString());
            }

            // execute HTTP post request
            HttpResponse response = null;
            try {
                response = httpClient.execute(httpPost);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                Log.v("PostLib", e.toString());
            }
            HttpEntity resEntity = null;

            try {
                resEntity = response.getEntity();
            } catch (NullPointerException e) {
                Log.v("PostLib", e.toString());
            }

            if (resEntity != null) {

                try {
                    responseStr = EntityUtils.toString(resEntity).trim();
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    Log.v("PostLib", e.toString());
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    Log.v("PostLib", e.toString());
                }
            }
        }
        return responseStr;

    }


    public static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }
}
