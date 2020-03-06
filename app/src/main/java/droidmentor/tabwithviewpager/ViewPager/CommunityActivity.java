package droidmentor.tabwithviewpager.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import droidmentor.tabwithviewpager.Fragment.NextFragment;
import droidmentor.tabwithviewpager.Fragment.ChatFragment;
import droidmentor.tabwithviewpager.Fragment.ContactsFragment;
import droidmentor.tabwithviewpager.R;
import droidmentor.tabwithviewpager.ViewPagerAdapter;

public class CommunityActivity extends AppCompatActivity {

    //This is our tablayout
    private TabLayout tabLayout;

    //This is our viewPager
    private ViewPager viewPager;

    ViewPagerAdapter adapter;

    //Fragments

    ChatFragment chatFragment;
    NextFragment nextFragment;
    ContactsFragment contactsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);
        //Initializing viewPager

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewPager = (ViewPager) findViewById(R.id.community_viewpager);
        viewPager.setOffscreenPageLimit(3);
        setupViewPager(viewPager);

        //Initializing the tablayout
        tabLayout = (TabLayout) findViewById(R.id.tablayout);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(),false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.getTabAt(position).select();

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
            case R.id.home: {
                NavUtils.navigateUpFromSameTask(this);
            //    Intent intent = new Intent(this, TournamentsActivity.class);
              //  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //    startActivity(intent);
                return true;

            }
            case R.id.action_tournaments: {
                Intent tournaments=new Intent(this,TournamentsActivity.class);
                startActivity(tournaments);
                finish();
                return true;
            }
            case R.id.action_community: {
                Intent community=new Intent(this,CommunityActivity.class);
                startActivity(community);
                finish();
                return true;
            }
       /*     case R.id.action_settings:
                Toast.makeText(this, "Home Settings Click", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_without_icon:
                Intent withouticon=new Intent(this,TabWOIconActivity.class);
                startActivity(withouticon);
                finish();
                return true;
            case R.id.action_customtab:
                Intent custom_tab=new Intent(this,CTournamentsActivity.class);
                startActivity(custom_tab);
                finish();
                return true; */
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupViewPager(ViewPager viewPager)
    {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //callsFragment=new ChatFragment();
        chatFragment=new ChatFragment();
        contactsFragment=new ContactsFragment();
        adapter.addFragment(chatFragment,"Объявления");
     //   adapter.addFragment(chatFragment,"CHAT");
        adapter.addFragment(contactsFragment,"Коммерция");
        viewPager.setAdapter(adapter);
    }

}
