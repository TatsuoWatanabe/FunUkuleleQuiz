package com.tatsuowatanabe.funukulelequiz.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.tatsuowatanabe.funukulelequiz.MainActivity;
import com.tatsuowatanabe.funukulelequiz.R;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by tatsuo on 5/24/16.
 */
public class QuizzesLoader {
    public static final String URL = "https://mongoquizserver.herokuapp.com/api/";

    /** Callback interface for delivering quizzes. */
    public interface Listener {
        /** Called when in_right quizzes is received. */
        void onLoad(Quizzes quizzes);
    }

    public static final void LoadQuizzes(final QuizGame game, final RequestQueue que, final Integer quizAmount, final Listener listener) {
        final Integer      TIMEOUT_MS  = (10 /* seconds */ * 1000);
        final MainActivity activity    = game.getActivity();
        final String       url         = QuizzesLoader.getApiUrl(quizAmount);
        activity.views.fab.setVisibility(View.GONE);

        if (activity.prefs.isLocalMode()) {
            // show with local data.
            Quizzes quizzes = getQuizzesFromLocalJsonFile(activity, quizAmount);
            listener.onLoad(quizzes);
            return;
        }

        Response.Listener<JSONArray> jsonRecListener = new Response.Listener<JSONArray>() {
            @Override public void onResponse(JSONArray response) {
                activity.views.loadingArea.setVisibility(View.GONE);
                Log.d(" response", response.toString());
                Quizzes quizzes = Quizzes.fromJson(response).setContext(activity).shuffle();
                listener.onLoad(quizzes);
            }
        };
        Response.ErrorListener resErrListener = new Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                activity.views.loadingArea.setVisibility(View.GONE);
                String errMsg = game.langIs(R.string.lang_ja) ? activity.getString(R.string.msg_err_network_ja, error.toString(), activity.getString(R.string.msg_use_local_data_ja)) :
                                game.langIs(R.string.lang_en) ? activity.getString(R.string.msg_err_network_en, error.toString(), activity.getString(R.string.msg_use_local_data_en)) : "";
                activity.displayMessage(errMsg);
                Log.d(" onErrorResponse", error.toString());

                // If failed to get the quizzes, load the local quizzes.
                Quizzes quizzes = getQuizzesFromLocalJsonFile(activity, quizAmount);
                listener.onLoad(quizzes);
            }
        };
        JsonArrayRequest jsonRec = new JsonArrayRequest(Request.Method.GET, url, jsonRecListener, resErrListener);

        // specify the request timeout setting.
        jsonRec.setRetryPolicy(new DefaultRetryPolicy(
                TIMEOUT_MS.intValue(),
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        Log.d(" Ukulele Quiz API URL", url);
        Log.d(" getCurrentTimeout", String.valueOf(jsonRec.getRetryPolicy().getCurrentTimeout()) + " ms");
        activity.views.loadingArea.setVisibility(View.VISIBLE);
        que.add(jsonRec); // send.
    }

    /**
     * load the local json file and get the Quizzes instance.
     * @param ma
     * @param quizAmount
     * @return
     */
    private static final Quizzes getQuizzesFromLocalJsonFile(final MainActivity ma, final Integer quizAmount) {
        final String localQuizzesString = readStringFromResource(ma, R.raw.quizzes);
        final Quizzes quizzes = Quizzes.fromJson(localQuizzesString).setContext(ma).shuffle().limitQuizzes(quizAmount);
        return quizzes;
    }

    /**
     * Read the resource and get the String.
     * @param ctx
     * @param resourceID
     * @return
     */
    @NonNull
    private static final String readStringFromResource(Context ctx, int resourceID) {
        final int DEFAULT_READ_LENGTH = 1024 * 8; // the size of buffer in characters.
        final StringBuilder contents = new StringBuilder();
        final String sep = System.getProperty("line.separator");

        try {
            InputStream is = ctx.getResources().openRawResource(resourceID);
            BufferedReader input =  new BufferedReader(new InputStreamReader(is), DEFAULT_READ_LENGTH);
            try {
                String line = null;
                while ((line = input.readLine()) != null){
                    contents.append(line);
                    contents.append(sep);
                }
            } finally {
                input.close();
            }
        } catch (FileNotFoundException ex) {
            Log.e("", "Couldn't find the file " + resourceID  + " " + ex);
            return "";
        } catch (IOException ex){
            Log.e("", "Error reading file " + resourceID + " " + ex);
            return "";
        }

        return contents.toString();
    }

    /**
     * Get the api url.
     * @param limit
     * @return
     */
    private static String getApiUrl(int limit) {
        return URL + "?limit=" + String.valueOf(limit);
    }

}