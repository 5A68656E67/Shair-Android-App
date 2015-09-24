/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  18641 java smart phone development - final project - Shair
 *
 *  Name: Sen Yue (seny)
 *        Zheng Lei (zlei)
 *
 *  class name: TransactionAdapter
 *
 *  class properties:
 *  Titles[]: CharSequence
 *  NumbOfTabs: int
 *
 *  class methods:
 *  getItem(int position): Fragment
 *  getPageTitle(int position): CharSequence
 *  getCount(): int
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package com.example.ethan.shairversion1application.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.ethan.shairversion1application.ui.ExploreFragment;
import com.example.ethan.shairversion1application.ui.NeedFragment;
import com.example.ethan.shairversion1application.ui.NotificationFragment;
import com.example.ethan.shairversion1application.ui.ShareFragment;


public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    private int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }
    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {
        if(position == 0) // if the position is 0 we are returning the First tab
        {
            // ArtistInfoFragment artistInfoFragment = new ArtistInfoFragment();
            return new ExploreFragment();
        }
        else if(position == 1)
        {
            //   MusicFragment musicFragment = new MusicFragment();
            return new NeedFragment();
        } else if(position == 2){
            //    VideoFragment videoFragment = new VideoFragment();
            return new ShareFragment();
        } else {
            //    MailingFragment mailingFragment = new MailingFragment();
            return new NotificationFragment();
        }
    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip
    @Override
    public int getCount() {
        return NumbOfTabs;
    }

}
