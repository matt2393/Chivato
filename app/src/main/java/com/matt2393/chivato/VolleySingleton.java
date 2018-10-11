package com.matt2393.ind;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {
    private static VolleySingleton volleySingleton;
    private static Context context;
    private RequestQueue requestQueue;

    private VolleySingleton(Context ctx){
        context=ctx;
        requestQueue=getRequestQueue();
    }
    public static synchronized VolleySingleton getInstance(Context ctx){
        if(volleySingleton==null)
            volleySingleton=new VolleySingleton(ctx);
        return volleySingleton;
    }

    public RequestQueue getRequestQueue() {
        if(requestQueue==null)
            requestQueue= Volley.newRequestQueue(context.getApplicationContext());

        return requestQueue;
    }
    public <T> void addRequest(Request<T> req){
        getRequestQueue().add(req);
    }
}
