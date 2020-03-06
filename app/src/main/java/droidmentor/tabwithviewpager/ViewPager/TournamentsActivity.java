package droidmentor.tabwithviewpager.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import droidmentor.tabwithviewpager.Fragment.OnlineFragment;
import droidmentor.tabwithviewpager.Fragment.NextFragment;
import droidmentor.tabwithviewpager.Fragment.ChatFragment;
import droidmentor.tabwithviewpager.Fragment.ContactsFragment;
import droidmentor.tabwithviewpager.R;
import droidmentor.tabwithviewpager.ViewPagerAdapter;

public class TournamentsActivity extends AppCompatActivity {

    //This is our tablayout
    private TabLayout tabLayout;

    //This is our viewPager
    private ViewPager viewPager;

    ViewPagerAdapter adapter;

    //Fragments
    OnlineFragment OnlineFragment;
    ChatFragment chatFragment;
    NextFragment nextFragment;
    ContactsFragment contactsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournaments);

        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.tournaments_viewpager);
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
            //запуск чата?
            case R.id.action_community: {
                Intent community=new Intent(this,CommunityActivity.class);
                startActivity(community);
                finish();
                return true;
            }
            case R.id.action_tournaments:
                Toast.makeText(this, "Home Status Click", Toast.LENGTH_SHORT).show();
                return true;
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
        nextFragment =new NextFragment();
        chatFragment=new ChatFragment();
   //     contactsFragment=new ContactsFragment();
        OnlineFragment =new OnlineFragment();
        adapter.addFragment(nextFragment,"Next");
        adapter.addFragment(OnlineFragment,"Online");
        adapter.addFragment(contactsFragment,"Results");

        viewPager.setAdapter(adapter);
    }

}
