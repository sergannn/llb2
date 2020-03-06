package droidmentor.tabwithviewpager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;

import droidmentor.tabwithviewpager.dummy.DummyContent;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class TourneyDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public String tid = "item_id";
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TourneyDetailFragment() {

    }
    private ProgressDialog pdLoading;
    private RecyclerView mRVFishPrice;
    private AdapterPlayers mAdapter;
public String tname;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey("thref")) {
            tid=getArguments().getString("thref");
            tname=getArguments().getString("tname");
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(tid));
            new getPlayers().execute();
            Activity activity = this.getActivity();
       //   FrameLayout appBarLayout = (FrameLayout) activity.findViewById(R.id.toolbar_layout);
        //    if (appBarLayout != null) {
                //appBarLayout.setTitle(mItem.content);
               // appBarLayout.setTitle(tname);
           //     appBarLayout.Sub(tname);




        }
    }


    private class getPlayers extends AsyncTask<String, String, String> {
        ImageView bmImage;
        ProgressDialog pdLoading = new ProgressDialog(getActivity());
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
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
                String ur="http://1.u0156265.z8.ru/old/index.php?id=29&t=".concat(tid);
                Log.d("fuck",ur);
                url = new URL(ur);

            }
            catch(MalformedURLException e)

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

            } catch(IOException e1)

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

            } catch(
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
            if (pdLoading != null) {pdLoading.dismiss();

            }


            List<DataPlayer> data = new ArrayList<>();
            pdLoading.dismiss();
            try {
                JSONObject globalObject = new JSONObject(result);
                String tupdated=globalObject.getString("updated");
               // String tname=globalObject.getString("tournay_name");
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = preferences.edit();

                //reged tourneys in memory
                String registeredt=preferences.getString("registeredt","");



                JSONArray jArray = new JSONArray(globalObject.getString("players"));


                // Extract data from json and store into ArrayList as class objects
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    DataPlayer fishData = new DataPlayer();
                    fishData.sex = json_data.getString("sex");
                    fishData.nid = json_data.getString("nid");
//есть ли игрок в турнире?
                    if(json_data.getString("nid").equals("/node/4887"))
                    {
                        Toast.makeText(getContext(),"registered!",Toast.LENGTH_LONG).show();
                        editor.putBoolean(tid,true);


                    }
                    else {editor.putBoolean(tid,false);}
                    editor.apply();


                        fishData.bd = json_data.getString("bd");
                    fishData.elo = json_data.getString("elo");
                    fishData.name = json_data.getString("name");
                  //  fishData.face=bmImage.setImageBitmap(result);
                  //  fishData.club = json_data.getString("club");
                    Log.d("fuck",fishData.nid);
                    //  fishData.price = json_data.getInt("price");
                    data.add(fishData);
                }

                // Setup and Handover data to recyclerview
                mRVFishPrice = (RecyclerView) getActivity().findViewById(R.id.fishPriceList);
                TextView toolbartxt = (TextView) getActivity().findViewById(R.id.toolbar_title);
              //  int timestamp = (int) System.currentTimeMillis()*1000;
                int updated = Integer.parseInt(tupdated);
                int minutes=updated/60;
                Log.d("updated",tupdated);
                Log.d("minutes",Integer.toString(minutes));

               // toolbartxt.setText(Integer.toString(minutes).concat(" минут назад"));
                toolbartxt.setText(tname);
              //  (AppCompatActivity) getActivity()).getSupportActionBar().setTitle();
                mAdapter = new AdapterPlayers(getActivity(), data);
                mRVFishPrice.setAdapter(mAdapter);
                mRVFishPrice.setLayoutManager(new LinearLayoutManager(getActivity()));
                mSwipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.players_swipe_refresh_layout);
                mSwipeRefreshLayout.setRefreshing(false);
                mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        new getPlayers().execute();
                    }
                });

            } catch (JSONException e) {
                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
            }

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);
         pdLoading = new ProgressDialog(getActivity());
        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.item_detail)).setText(mItem.details);
        }

        return rootView;
    }
}
