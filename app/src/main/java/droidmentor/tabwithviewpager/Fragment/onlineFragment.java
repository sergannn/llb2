package droidmentor.tabwithviewpager.Fragment;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

import droidmentor.tabwithviewpager.AdapterFish;
import droidmentor.tabwithviewpager.DataFish;
import droidmentor.tabwithviewpager.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class OnlineFragment extends Fragment {
    public OnlineFragment() {
        // Required empty public constructor
    }

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView mRVFishPrice;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private AdapterFish mAdapter;
    private String thref;
    private Boolean registered;
    private int selectedViewIndex;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        Toast.makeText(getActivity(),"online",Toast.LENGTH_LONG).show();
//        new AsyncLogin().execute();
    }
    public void onViewCreated (View view,
                               Bundle savedInstanceState)
    {    mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.activity_main_swipe_refresh_layout);

    mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            new AsyncLogin().execute();

        }
    });

}
    private class AsyncLogin extends AsyncTask<String, String, String> {
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
                  url = new URL("http://1.u0156265.z8.ru/old/index.php?id=30");
             //   url = new URL("http://1.u0156265.z8.ru/old/next-tournaments.json");

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
            Toast.makeText(getActivity(),"post"+result,Toast.LENGTH_LONG).show();
            pdLoading.dismiss();
            List<DataFish> data = new ArrayList<>();

            pdLoading.dismiss();
            try {
                JSONObject globalObject = new JSONObject(result);

                JSONArray jArray = new JSONArray(globalObject.getString("tourneys").toString());
          //      String tupdated = globalObject.getString("time");
          //      String dupdated = globalObject.getString("date");
              //  String registered = globalObject.getString("registered");
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());


                // Extract data from json and store into ArrayList as class objects
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    DataFish fishData = new DataFish();
              //      Toast.makeText(getActivity(),json_data.getString("name"),Toast.LENGTH_LONG).show();
                    fishData.name = json_data.getString("name");
                    Boolean registeredt=preferences.getBoolean(json_data.getString("href"),false);

                    if(registeredt) {fishData.registered="1";}
                    else            {fishData.registered="0";}

                    fishData.reg = json_data.getString("reg");
                    fishData.date = json_data.getString("date");
                    fishData.club = json_data.getString("club");
                    fishData.href = json_data.getString("href");


                    Log.d("fuck", fishData.name);
                    //  fishData.price = json_data.getInt("price");
                    data.add(fishData);
                }

                // Setup and Handover data to recyclerview
                AppCompatTextView textElement = (AppCompatTextView) getActivity().findViewById(R.id.updated);
             //   textElement.setText("Обновлено " + dupdated + " " + tupdated);
                mRVFishPrice = (RecyclerView) getView().findViewById(R.id.fishPriceList);
                registerForContextMenu(mRVFishPrice);
                mAdapter = new AdapterFish(getActivity(), data);
                mRVFishPrice.setAdapter(mAdapter);
                mRVFishPrice.setLayoutManager(new LinearLayoutManager(getActivity()));
                mSwipeRefreshLayout.setRefreshing(false);

            } catch (JSONException e) {
                Toast.makeText(getActivity(), "catch", Toast.LENGTH_LONG).show();
             //   Toast.makeText(getActivity(), e.toString()+"catch", Toast.LENGTH_LONG).show();
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_online, container, false);
        // Inflate the layout for this fragment
       // ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
       // setupViewPager(viewPager);
        // Set Tabs inside Toolbar
       // TabLayout tabs = (TabLayout) view.findViewById(R.id.result_tabs);
       // tabs.setupWithViewPager(viewPager);

        return view;

    }
    private void setupViewPager(ViewPager viewPager) {


        Adapter adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(new ChatFragment(), "Today");
        adapter.addFragment(new ContactsFragment(), "Week");

        viewPager.setAdapter(adapter);



    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_calls_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
//         selectedViewIndex = (int)v.getTag();
        v.getTag();

     //   menu.setHeaderTitle("Действие");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        //registered = preferences.getString("registered", "");
      //  Toast.makeText()
        String regtitle="register";
        thref = preferences.getString("tmenuhref", "");
        registered=preferences.getBoolean(thref,false);

        if(!registered) {regtitle="Регистрация";} else {regtitle="Отменить регистрацию";}

        menu.add(0, 1, 0, regtitle);//groupId, itemId, order, title
        menu.add(0, 2, 1, "Обновить");//groupId, itemId, order, title
    //    menu.add(0, v.getId(), 0, "Просмотр");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //  String href=mAdapter.getItemId()
        switch(item.getItemId()) {
            case 1:
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

                thref = preferences.getString("tmenuhref", "");
            //    registered = preferences.getString("registered", "");
                Toast.makeText(getContext(), thref, Toast.LENGTH_LONG).show();
       //         new register().execute();
                break;
            case 2:
               Toast.makeText(getContext(),"2",Toast.LENGTH_LONG).show();
                break;
        }
        return super.onContextItemSelected(item);


    }

    private class register extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(getActivity());
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
            super.onPreExecute();

            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
//            pdLoading.show();


        }

        @Override
        protected String doInBackground(String... params) {
            try {
                  url = new URL("http://1.u0156265.z8.ru/old/index.php?id=30");
              //  url = new URL("http://1.u0156265.z8.ru/old/index.php?id=35&tid=".concat(thref)+"&un=".concat(registered.toString()));

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

        protected void onPostExecute(String result) {
            pdLoading.dismiss();
            new AsyncLogin().execute();
            //this method will be running on UI thread
Toast.makeText(getContext(),result,Toast.LENGTH_LONG).show();



        }
    }

    @Override
    public void onResume() {
        super.onResume();
        new AsyncLogin().execute();
// add your code here which executes when the Fragment gets visible.
    }
    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}