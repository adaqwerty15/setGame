package lav.isu.ru.setgame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private EditText name;
    private Button registration;
    MyTask mt;
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_NAME = "token";
    SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        name = (EditText)findViewById(R.id.name);
        registration = (Button)findViewById(R.id.registration);

    }

    public void onClick(View view) throws IOException, ExecutionException, InterruptedException, JSONException {

        mt = new MyTask();
        String res = mt.execute().get();
        JSONObject resp = new JSONObject(res);
        String token = resp.get("token").toString();
        if (!token.equals("-1")) {
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putString(APP_PREFERENCES_NAME, token);
            editor.apply();
        }
        else {
            token = mSettings.getString(APP_PREFERENCES_NAME, "");
        }

        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        System.out.println(token);
        intent.putExtra("token", token);
        startActivity(intent);

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
                String json = "{\"action\": \"register\", \"nickname\": \""+name.getText().toString()+"\"}";

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


