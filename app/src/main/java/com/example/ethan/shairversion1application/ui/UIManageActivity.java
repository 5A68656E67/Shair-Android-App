/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  18641 java smart phone development - final project - Shair
 *
 *  Name: Sen Yue (seny)
 *        Zheng Lei (zlei)
 *
 *  class name: UIManageActivity
 *
 *  class properties:
 *  toolbar:Toolbar
 *  pager:ViewPager
 *  adapter:ViewPagerAdapter
 *  tabs:SlidingTabLayout
 *  Titles:CharSequence
 *  Numboftabs:int
 *  doubleBackToExitPressedOnce:boolean
 *  sqlDataBase: SQLDataBase
 *  mNavigationDrawerFragment: NavigationDrawerFragment
 *  settings: MenuItem
 *  mTitle: CharSequence
 *  mDrawerLayout: DrawerLayout
 *  menu: Menu
 *
 *  class methods:
 *  onBackPressed():void
 *  onNavigationDrawerItemSelected(int position):void
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package com.example.ethan.shairversion1application.ui;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import com.example.ethan.shairversion1application.login.MainActivity;
import com.example.ethan.shairversion1application.R;
import com.example.ethan.shairversion1application.adapter.ViewPagerAdapter;
import com.example.ethan.shairversion1application.database.SQLDataBase;
import com.example.ethan.shairversion1application.entities.Account;
import com.example.ethan.shairversion1application.entities.Item;
import com.example.ethan.shairversion1application.navigation.NavigationDrawerFragment;
import com.example.ethan.shairversion1application.service.LikeService;
import com.example.ethan.shairversion1application.service.TransactionService;
import com.example.ethan.shairversion1application.settings.AboutUsDialog;
import com.example.ethan.shairversion1application.settings.EditProfileDialog;
import com.example.ethan.shairversion1application.settings.HelpDialog;
import com.example.ethan.shairversion1application.settings.InviteFriendsDialog;
import com.example.ethan.shairversion1application.settings.SettingsDialog;
import com.example.ethan.shairversion1application.slide.SlidingTabLayout;

import java.util.ArrayList;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class UIManageActivity extends AppCompatActivity implements ExploreFragment.OnFragmentInteractionListener, NeedFragment.OnFragmentInteractionListener,
        NotificationFragment.OnFragmentInteractionListener, ShareFragment.OnFragmentInteractionListener, NavigationDrawerFragment.NavigationDrawerCallbacks{
    private Toolbar toolbar;
    private ViewPager pager;
    private ViewPagerAdapter adapter;
    private SlidingTabLayout tabs;
    private CharSequence Titles[]={"Explore","Need","Share", "Notification"};
    private int Numboftabs =4;
    private boolean doubleBackToExitPressedOnce = false;
    private SQLDataBase sqlDataBase;

    private NavigationDrawerFragment mNavigationDrawerFragment;

    private MenuItem settings;

    private CharSequence mTitle;
    private DrawerLayout mDrawerLayout;

    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_uimanageactivity);
        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);                   // Setting toolbar as the ActionBar with setSupportActionBar() call

        sqlDataBase = new SQLDataBase(this);
        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);


        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                mDrawerLayout);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                final InputMethodManager imm = (InputMethodManager)getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(pager.getWindowToken(), 0);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        Intent transintent = new Intent(this, TransactionService.class);
        startService(transintent);
        Intent likesintent = new Intent(this, LikeService.class);
        startService(likesintent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.menu_uimanage, menu);
//            restoreActionBar();
            return true;
        }


//        getMenuInflater().inflate(R.menu.menu_uimanage, menu);
//        return true;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.settings) {
            if (!mNavigationDrawerFragment.isDrawerOpen()) {
                mDrawerLayout.openDrawer(GravityCompat.END);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


    @Override
    public void onFragmentInteraction(ArrayList<Item> arrayOfUsers) {

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (position) {
            // edit profile
            case 1 :  EditProfileDialog dialog1 = new EditProfileDialog();dialog1.show(fragmentManager, "edit_profile");break;
            // settings
            case 2 :  SettingsDialog dialog2 = new SettingsDialog();dialog2.show(fragmentManager, "add_things_to_share");break;
            // invite friends
            case 3 :  InviteFriendsDialog dialog3 = new InviteFriendsDialog();dialog3.show(fragmentManager, "invite_friends");break;
            // about
            case 4 :  AboutUsDialog dialog4 = new AboutUsDialog();dialog4.show(fragmentManager,"about_us");break;
            // help
            case 5 :  HelpDialog dialog5 = new HelpDialog();dialog5.show(fragmentManager,"get_help");break;
            case 6 :
                Account temp = sqlDataBase.getAcount().get(0);
                sqlDataBase.deleteAllAccount();
                sqlDataBase.addItem(temp.getAccountName(), temp.getAccountPassword(), 0);
                Intent intent = new Intent(UIManageActivity.this, MainActivity.class);startActivity(intent);

        }
    }

    // onSectionAttached - attach the different titles to different drawer items
    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.section_title_0);
                break;
            case 2:
                mTitle = getString(R.string.section_title_1);
                break;
            case 3:
                mTitle = getString(R.string.section_title_2);
                break;
            case 4:
                mTitle = getString(R.string.section_title_3);
                break;
            case 5:
                mTitle = getString(R.string.section_title_4);
                break;
            case 6:
                mTitle = getString(R.string.section_title_5);
                break;
            case 7:
                mTitle = getString(R.string.section_title_6);
                break;
        }
    }
}