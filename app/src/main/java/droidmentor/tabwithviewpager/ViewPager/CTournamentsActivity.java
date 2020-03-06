package droidmentor.tabwithviewpager.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import droidmentor.tabwithviewpager.Fragment.NextFragment;
import droidmentor.tabwithviewpager.Fragment.OnlineFragment;
import droidmentor.tabwithviewpager.Fragment.ChatFragment;
import droidmentor.tabwithviewpager.Fragment.ResFragment;

import droidmentor.tabwithviewpager.R;
import droidmentor.tabwithviewpager.ViewPagerAdapter;

public class CTournamentsActivity extends AppCompatActivity {

    //This is our tablayout
    private TabLayout tabLayout;

    //This is our viewPager
    private ViewPager viewPager;

    //Fragments
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    ChatFragment chatFragment;
    NextFragment nextFragment;
    ResFragment resFragment;
    OnlineFragment onlineFragment;
    String[] tabTitle={"NEXT","ONLINE","RESULTS"};
    int[] unreadCount={0,0,0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_without_icon);

        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(3);
        setupViewPager(viewPager);

        //Initializing the tablayout
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

        try
        {
            setupTabIcons();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewPager.setCurrentItem(position,false);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        // Associate searchable configuration with the SearchView
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_community:
                Intent community=new Intent(this,CommunityActivity.class);
            //    startActivityForResult(community,1);
                startActivity(community);
            //    finish();
            //    Toast.makeText(this, "Home Status Click", Toast.LENGTH_SHORT).show();
//                final ViewGroup viewGroup = (ViewGroup) findViewById(R.id.content);
 //               viewGroup.addView(View.inflate(this, R.layout.login, null));
 //       this.setContentView(R.layout.login);
  //              TextView mEmailSignInButton = (TextView) findViewById(R.id.lin);
  //              mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
    //                @Override
      //              public void onClick(View view) {
          //              login();
        //            }
          //      });
                return true;
           /* case R.id.action_settings:
                Toast.makeText(this, "Home Settings Click", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_with_icon:
                Intent withicon=new Intent(this,TournamentsActivity.class);
                startActivity(withicon);
                finish();
                return true;
            case R.id.action_without_icon:
                Intent withouticon=new Intent(this,TabWOIconActivity.class);
                startActivity(withouticon);
                finish();
                return true; */
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        nextFragment=new NextFragment();
    //    chatFragment=new ChatFragment();
        onlineFragment=new OnlineFragment();
        resFragment=new ResFragment();
        adapter.addFragment(nextFragment,"a");
    //ДОБАВЛЯЕМ ФРАГМЕНТ next!

     //   adapter.addFragment(onlineFragment,"hz");
        //  adapter.addFragment(resFragment,"hz");
        viewPager.setAdapter(adapter);
    }

    private View prepareTabView(int pos) {
        View view = getLayoutInflater().inflate(R.layout.custom_tab,null);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        TextView tv_count = (TextView) view.findViewById(R.id.tv_count);
        tv_title.setText(tabTitle[pos]);
        if(unreadCount[pos]>0)
        {
            tv_count.setVisibility(View.VISIBLE);
            tv_count.setText(""+unreadCount[pos]);
        }
        else
            tv_count.setVisibility(View.GONE);


        return view;
    }
    private void login() {
        View view = getLayoutInflater().inflate(R.layout.login,null);
      //  TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
      //  String email = mEmailView.getText().toString();
      //  String password = mPasswordView.getText().toString();
    Log.d("login","hz");
        final ViewGroup viewGroup = (ViewGroup) findViewById(R.id.content);
        viewGroup.addView(View.inflate(this, R.layout.activity_tab_without_icon, null));



    }

    private void setupTabIcons()
    {

        for(int i=0;i<tabTitle.length;i++)
        {
            /*TabLayout.Tab tabitem = tabLayout.newTab();
            tabitem.setCustomView(prepareTabView(i));
            tabLayout.addTab(tabitem);*/

            tabLayout.getTabAt(i).setCustomView(prepareTabView(i));
        }


    }
}
