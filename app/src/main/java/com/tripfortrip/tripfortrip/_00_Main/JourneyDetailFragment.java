package com.tripfortrip.tripfortrip._00_Main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tripfortrip.tripfortrip.R;
import com.tripfortrip.tripfortrip._01_MyJourney.JourneyBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.R.attr.format;
import static android.R.attr.logo;

/**
 * Created by wei-tzutseng on 2017/3/20.
 */

public class JourneyDetailFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View fgJourneyDetailFragment = inflater.inflate(R.layout.journey_detail_fragment, container, false);
//        Activity activity = this.getActivity();

//        MainActivity  mainActivity = (MainActivity) this.getActivity();
//
//
//        Toolbar toolbar = (Toolbar) mainActivity.findViewById(R.id.toolbar);
//        mainActivity.setSupportActionBar(toolbar);
////        final ActionBar actionBar = getSupportActionBar();
////        actionBar.setDisplayHomeAsUpEnabled(false);
//        mainActivity.getActionBar().setDisplayHomeAsUpEnabled(false);

        ViewPager viewPager = (ViewPager) fgJourneyDetailFragment.findViewById(R.id.viewpager);
        if (viewPager != null) {
            setUpViewPager(viewPager);
        }
        TabLayout tabLayout = (TabLayout) fgJourneyDetailFragment.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        return fgJourneyDetailFragment;

    }

    private void setUpViewPager(ViewPager viewPager) {
        long day=0;
        Adapter adapter = new Adapter(getActivity().getSupportFragmentManager());
        Bundle bundle = getArguments();
        if (bundle != null) {
            JourneyBean journey = (JourneyBean) bundle.getSerializable("journey");
            String DateStart = journey.getDateStart();
            String DateEnd = journey.getDateEnd();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            try {
                Date beginDate= dateFormat.parse(DateStart);
                Date endDate= dateFormat.parse(DateEnd);
                day=(endDate.getTime()-beginDate.getTime())/(24*60*60*1000)+1;
                Log.d("day:",beginDate+"~"+endDate+"="+day+"天");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        for(int i =1 ;i<=day;i++){
            adapter.addFragment(new JourneyDetailEveryDayFragment(), "第"+i+"天");

        }

//        adapter.addFragment(new JourneyDetailEveryDayFragment(), "第一天");
//        adapter.addFragment(new JourneyDetailEveryDayFragment(), "第二天");
//        adapter.addFragment(new JourneyDetailEveryDayFragment(), "第三天");
        viewPager.setAdapter(adapter);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}
