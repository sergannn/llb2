package droidmentor.tabwithviewpager.Fragment;


import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import droidmentor.tabwithviewpager.R;
import droidmentor.tabwithviewpager.chatlist.adapter;
import droidmentor.tabwithviewpager.chatlist.list;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

import static droidmentor.tabwithviewpager.Fragment.NextFragment.CONNECTION_TIMEOUT;
import static droidmentor.tabwithviewpager.Fragment.NextFragment.READ_TIMEOUT;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFragment extends Fragment {
    private list data;
    private ListView listView;
    private ArrayList<list> dataArrayList;
    private adapter listAdapter;
    private OkHttpClient client;
    public ContactsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = new OkHttpClient();
        start("wss://a5abeef5.ngrok.io");
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_chat, container, false);

        listView =  view.findViewById(R.id.msg_listview);
        dataArrayList = new ArrayList<>();


        dataArrayList.add(data = new list("Jenni2", "Where are you boy????",R.drawable.girl));
        dataArrayList.add(data = new list("Jenni2", "Where are you boy????",R.drawable.girl));
        dataArrayList.add(data = new list("Christa", "I am here dude",R.drawable.girl2));


        listAdapter = new adapter(getActivity(), dataArrayList);
        listView.setAdapter(listAdapter);

      //  listView.setMenuCreator(creator);
      //  new SocketLCheck().execute();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_contacts_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    private final class EchoWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            webSocket.send("Hello, it's SSaurel !");
            webSocket.send("What's up ?");
            webSocket.send(ByteString.decodeHex("deadbeef"));
            //       webSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye !");
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            output("Receiving : " + text);
            // EventBus.getDefault().post(new SocketMessageEvent(message));
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            output("Receiving bytes : " + bytes.hex());
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
            output("Closing : " + code + " / " + reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            output("Error : " + t.getMessage());
        }
    }
    //добавить статик- иначе leaks errors

    private void start(String socketurl) {
        Log.d("start","start");

        //Request request = new Request.Builder().url("wss://bd380c91.ngrok.io").build();
        Request request = new Request.Builder().url("wss://3b740cb5.ngrok.io/").build();
        //Request request = new Request.Builder().url("wss://d8067a6b.ngrok.io").build();
        ContactsFragment.EchoWebSocketListener listener = new ContactsFragment.EchoWebSocketListener();
        WebSocket ws = client.newWebSocket(request, listener);
        //   client.dispatcher().executorService().shutdown();
    }
    private void output(final String txt) {

        if(getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(getContext(), txt,
                            Toast.LENGTH_LONG).show();
                    dataArrayList.add(data = new list("Christa", txt, R.drawable.girl2));
                    listAdapter.notifyDataSetChanged();

                }
            });
        }
    }
}
/*
    private class SocketLCheck extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(getActivity());
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                //  url = new URL("http://1.u0156265.z8.ru/old/index.php?id=30");
                url = new URL("http://1.u0156265.z8.ru/old/websocket.json");

            } catch (MalformedURLException e)

            {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try

            {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);

            } catch (IOException e1)

            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try

            {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (
                    IOException e)

            {
                e.printStackTrace();
                return e.toString();
            } finally

            {
                conn.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            pdLoading.dismiss();
            Toast.makeText(getContext(),"got socket"+result,Toast.LENGTH_LONG).show();


            start(result);
            pdLoading.dismiss();


        }
    }
 */