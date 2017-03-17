package com.igordubrovin.trainstimetable.utils;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;

/**
 * Created by Игорь on 17.03.2017.
 */

public abstract class LoaderHtml extends AsyncTask<String, Void, Document> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Document doInBackground(String... params) {
        String url;
        Document doc = null;
        if (params != null) {
            url = params[0];
        } else {
            return null;
        }
        try {
            doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36")
                    .referrer("http://www.google.com")
                    .timeout(5000)
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc;
    }

    @Override
    protected void onPostExecute(Document document) {
        super.onPostExecute(document);
        onPostParse(new HtmlHelper().htmlParse(document));
    }

    protected abstract void onPostParse(List<Train> trainsList);

}