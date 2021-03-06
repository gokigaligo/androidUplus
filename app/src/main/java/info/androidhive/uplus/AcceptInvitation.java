package info.androidhive.uplus;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import info.androidhive.uplus.activity.HomeActivity;

/**
 * Created by user on 02/01/2018.
 */

public class AcceptInvitation extends AsyncTask<String,Void,String> {
    String result = "";
    ProgressDialog progress;
    Context context;
    String groupId = "";

    public AcceptInvitation(Context context, ProgressDialog progress) {
        this.context = context;
        this.progress = progress;
    }

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String login_url = "http://67.205.139.137/api/index.php";
        if (type.equals("invite")) {
            try {
                String action = "inviteMember";
                groupId = params[1];
                String invitorId = params[2];
                String invitedPhone = params[3];
                URL url = new URL(login_url);
                HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
                //set POST as request method
                httpCon.setRequestMethod("POST");
                httpCon.setDoOutput(true);
                httpCon.setDoInput(true);
                OutputStream outStream = httpCon.getOutputStream();
                BufferedWriter bufferWriter = new BufferedWriter(new OutputStreamWriter(outStream, "UTF-8"));
                String post_data = URLEncoder.encode("action", "UTF-8") + "=" + URLEncoder.encode(action, "UTF-8") + "&"
                        + URLEncoder.encode("groupId", "UTF-8") + "=" + URLEncoder.encode(groupId, "UTF-8") + "&"
                        + URLEncoder.encode("invitorId", "UTF-8") + "=" + URLEncoder.encode(invitorId, "UTF-8") + "&"
                        + URLEncoder.encode("invitedPhone", "UTF-8") + "=" + URLEncoder.encode(invitedPhone, "UTF-8");
                bufferWriter.write(post_data);
                bufferWriter.flush();
                bufferWriter.close();
                outStream.close();
                InputStream inStream = httpCon.getInputStream();
                BufferedReader buffReader = new BufferedReader(new InputStreamReader(inStream, "iso-8859-1"));

                String line = "";
                while ((line = buffReader.readLine()) != null) {
                    result += line;
                }
                buffReader.close();
                inStream.close();
                httpCon.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        BackgroundTask backgroundTask = new BackgroundTask(context, progress);
        backgroundTask.execute("members", groupId);
        progress.dismiss();
        Toast.makeText(context, result, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(context, HomeActivity.class);
        Invitation.invitationpage.finish();
        Invitation.invitationpage.startActivity(intent);
    }
}