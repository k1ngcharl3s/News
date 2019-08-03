package com.example.news;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.support.annotation.Nullable;

import java.util.List;

/**
 * Loads the list for the news articles by utilizing the AsyncTask to execute the
 * network request from the URL.
 */
class NewsLoader extends AsyncTaskLoader<List<News>> {

    /**
     * The Query URL
     */
    private final String mUrl;

    /**
     * Build the new {@link NewsLoader}
     *
     * @param context for the activity
     * @param url to load the data
     */
    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * The background thread that is utilized
     */
    @Nullable
    @Override
    public List<News> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Execute the network request
        return QueryUtils.fetchNewsData(mUrl);
    }
}
