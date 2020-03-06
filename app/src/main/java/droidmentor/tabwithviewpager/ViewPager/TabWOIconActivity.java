package droidmentor.tabwithviewpager.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import droidmentor.tabwithviewpager.Fragment.NextFragment;
import droidmentor.tabwithviewpager.Fragment.ChatFragment;
import droidmentor.tabwithviewpager.Fragment.ContactsFragment;
import droidmentor.tabwithviewpager.Fragment.OnlineFragment;
import droidmentor.tabwithviewpager.R;
import droidmentor.tabwithviewpager.ViewPagerAdapter;

public class TabWOIconActivity extends AppCompatActivity {

    //This is our tablayout
    private TabLayout tabLayout;

    //This is our viewPager
    private ViewPager viewPager;

    //Fragments
    OnlineFragment onlineFragment;
    ChatFragment chatFragment;
    NextFragment nextFragment;
    ContactsFragment contactsFragment;
   // ContactsFragment contactsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_without_icon);
        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(3);

        //Initializing the tablayout
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

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

        setupViewPager(viewPager);


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
                startActivity(community);
                finish();
                return true;
      /*      case R.id.action_settings:
                Toast.makeText(this, "Home Settings Click", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_with_icon:
                Intent withicon=new Intent(this,TournamentsActivity.class);
                startActivity(withicon);
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
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        nextFragment =new NextFragment();
        chatFragment=new ChatFragment();
        contactsFragment=new ContactsFragment();
        adapter.addFragment(nextFragment,"CALLS");
        adapter.addFragment(chatFragment,"CHAT");
        adapter.addFragment(contactsFragment,"CONTACTS");
        viewPager.setAdapter(adapter);
    }

}
