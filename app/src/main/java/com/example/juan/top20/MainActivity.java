package com.example.juan.top20;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static List<Pelicula> Pelis = new ArrayList<>();
    //Con solo cambiar el valor de max puede cambiar el top que se desea.
    final static int max = 20;
    static GridView tablero;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new MoviesDownloader().execute();
    }


    private class MoviesDownloader extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(MainActivity.this);
            progress.setTitle("Top " + String.valueOf(max) + " Movies of 2017");
            progress.setMessage("Cargando...");
            progress.setIndeterminate(false);
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            final StringBuilder builder = new StringBuilder();
            try {
                Document doc = Jsoup.connect("http://www.imdb.com/list/ls064079588/").get();
                Element parent;
                String title,score,metascore,link;
                int i = 0;
                for (Element el : doc.select("div[class=lister-item-content]")) {
                    parent = el.parent();
                    if (i<max) {
                        title = parent.text();
                        title = title.substring(title.indexOf(".") + 1, title.indexOf("(") - 1);

                        score = parent.text();
                        score = score.substring(score.indexOf("Rate") - 4, score.indexOf("Rate") - 1);

                        metascore = parent.text();
                        metascore = metascore.substring(metascore.indexOf("Metascore") - 4, metascore.indexOf("Metascore") - 1);

                        link = parent.select("a[href]").toString();
                        if (link.contains("loadlate=")) {
                            link = link.substring(link.indexOf("loadlate=") + 10, link.indexOf("data-tconst") - 2);
                            builder.append(String.valueOf(i+1)).append("-").append(title)
                                    .append("\n").append("Calificacion: ").append(score)
                                    .append("\n").append("Metascore: ").append(metascore);

                            Pelis.add(new Pelicula(builder.toString(),getBitmapFromURL(link)));
                            builder.delete(0,builder.length());
                        }
                    }
                    i++;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        tablero = (GridView) findViewById(R.id.tablero);
                        AdaptadorDePeliculas adaptador = new AdaptadorDePeliculas(getApplicationContext());
                        tablero.setAdapter(adaptador);

                    }
                });


            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progress.dismiss();
        }
    }


    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }


}





