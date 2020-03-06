package droidmentor.tabwithviewpager.Fragment;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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


public class ChatFragment extends Fragment {
    private Activity activity;


    private SwipeMenuListView listView;
    private ArrayList<list> dataArrayList;
    private adapter listAdapter;
    private list data;
    private OkHttpClient client;
    private ImageView output;

    //private String socketurl;
    public ChatFragment() {

    }

    private void start(String socketurl) {
        Log.d("start", "start");

        Request request = new Request.Builder().url(socketurl).build();
        System.out.println(request);
        //  Request request = new Request.Builder().url("wss://3b740cb5.ngrok.io/").build();
        //Request request = new Request.Builder().url("wss://d8067a6b.ngrok.io").build();
        EchoWebSocketListener listener = new EchoWebSocketListener();
        WebSocket ws = client.newWebSocket(request, listener);
           client.dispatcher().executorService().shutdown();

    }

    private void output(final String txt) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    Toast.makeText(getContext(), txt,
                            Toast.LENGTH_LONG).show();
                    dataArrayList.add(data = new list(txt, txt, R.drawable.girl2));
                    listAdapter.notifyDataSetChanged();

                }
            });
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        client = new OkHttpClient();
    //    start("wss://a5abeef5.ngrok.io");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat, container, false);
        // Inflate the layout for this fragment
        listView = view.findViewById(R.id.listview);
        dataArrayList = new ArrayList<>();


//        dataArrayList.add(data = new list("Jenni", "Where are you boy?",R.drawable.girl));
//        dataArrayList.add(data = new list("Christa", "I am here dude",R.drawable.girl2));


        listAdapter = new adapter(getActivity(), dataArrayList);
        listView.setAdapter(listAdapter);

        listView.setMenuCreator(creator);

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:

                        Toast.makeText(getContext(), "Deleted!", Toast.LENGTH_SHORT).show();
                        dataArrayList.remove(position);
                        listAdapter.notifyDataSetChanged();

                        break;
                    case 1:
                        break;
                }
                return false;
            }
        });
        new SocketLCheck().execute();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_chat_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    SwipeMenuCreator creator = new SwipeMenuCreator() {

        @Override
        public void create(SwipeMenu menu) {
            // create "delete" item
            SwipeMenuItem deleteItem = new SwipeMenuItem(
                    getContext());
            // set item background
            deleteItem.setBackground(new ColorDrawable(Color.parseColor("#F45557")));
            // set item width
            deleteItem.setWidth(150);
            deleteItem.setTitle("x");
            deleteItem.setTitleColor(Color.WHITE);
            deleteItem.setTitleSize(15);
            // add to menu
            menu.addMenuItem(deleteItem);
        }
    };

    private final class EchoWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            webSocket.send("Hello, it's SSaurel !");
            webSocket.send("What's up ?");
            //  webSocket.send(ByteString.decodeHex("deadbeef"));
            //       webSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye !");

        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
           // JSONObject globalObject = null;
            try {
                JSONObject msg = new JSONObject(text);

                String msgtext = msg.getJSONObject("data").getString("text");

                //  String registered = globalObject.getString("registered");
                // Extract data from json and store into ArrayList as class objects
              //  JSONObject json_data = jArray.getJSONObject(i);
                output("Receiving : " + msgtext);
            } catch (JSONException e) {
                e.printStackTrace();
            }



        }
        /*
        @Override      public void onMessage(WebSocket webSocket, String text) {
            output("Receiving : " + text);
            // EventBus.getDefault().post(new SocketMessageEvent(message));
        }
*/
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
            output("Error... : " + t.getMessage()+ response);
        }
    }
    //добавить статик- иначе leaks errors

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            this.activity = (Activity) context;
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.activity = null;

    }


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
                url = new URL("http://1.u0156265.z8.ru/old/socket.json");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try {

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
                    IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            pdLoading.dismiss();
            output(result);
            Toast.makeText(getContext(), "got socket" + result, Toast.LENGTH_LONG).show();

            String converted_socketurl = result.replace("https", "wss");
            System.out.print("converted:"+converted_socketurl);
            start(result);
            //wss://8409bbee.ngrok.io
            //   start("wss://8409bbee.ngrok.io");
            pdLoading.dismiss();


        }
    }
}

