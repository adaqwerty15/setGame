package lav.isu.ru.setgame;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;



public class GameActivity extends AppCompatActivity {
    MyTask mt;
    LinearLayout llMain;
    String token;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        llMain = (LinearLayout)findViewById(R.id.limain);
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        token =  getIntent().getExtras().getString("token");
        mt = new MyTask();
        String res = "";
        try {
             res = mt.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(res);
        CardS cardS = gson.fromJson(res, CardS.class);
        LinearLayout.LayoutParams viewLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        for (int i=0; i<4; i++) {
            LinearLayout tableRow = new LinearLayout(this);
            tableRow.setLayoutParams(viewLayoutParams);
            tableRow.setOrientation(LinearLayout.HORIZONTAL);
            llMain.addView(tableRow);
            for (int j=0; j<3; j++) {
                CardView cardView = new CardView(this, cardS.cards[i*3+j]);
                Display display = getWindowManager().getDefaultDisplay();
                LinearLayout.LayoutParams imageViewLayoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, display.getHeight()/4-30,1);
                cardView.setLayoutParams(imageViewLayoutParams);
                tableRow.addView(cardView);
            }
        }

    }

    class CardS {
        String status;
        Card[] cards;

        public CardS(String status, Card[] cards) {
            this.status = status;
            this.cards = cards;
        }
    }

    class Card {
        int count, fill, shape, color;

        public Card(int count, int fill, int shape, int color) {
            this.count = count;
            this.fill = fill;
            this.shape = shape;
            this.color = color;
        }
    }

     class MyTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            String res ="";
            try {
                URL url = new URL("http://194.176.114.21:8050");
                String json =  "{\"action\" : \"fetch_cards\", \"token\" : "+token+" }";

                HttpURLConnection conn= (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");

                OutputStream os = conn.getOutputStream();
                os.write(json.getBytes("UTF-8"));
                os.close();

                InputStream in = new BufferedInputStream(conn.getInputStream());

                Scanner sc = new Scanner(in);
                while (sc.hasNext()){
                    res+=(sc.nextLine());
                }


                in.close();
                conn.disconnect();
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }
}
