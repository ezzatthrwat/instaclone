package com.example.zizoj.instaclone.Utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SectionsStatePagerAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final HashMap<Fragment , Integer> mfragments = new HashMap<>();
    private final HashMap<String , Integer> mfragmentsNumbers = new HashMap<>();
    private final HashMap<Integer , String> mfragmentsNames = new HashMap<>();

    public SectionsStatePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }


    public void addFragments(Fragment fragment , String fragmentName){

        mFragmentList.add(fragment);
        mfragments.put(fragment,mFragmentList.size()-1);
        mfragmentsNames.put(mFragmentList.size()-1,fragmentName);
        mfragmentsNumbers.put(fragmentName,mFragmentList.size()-1);

    }

    /**
     * returns the fragment with the name @param
     * @param fragment
     * @return
     */

    public Integer getFragmentNumber(Fragment fragment){
        if (mfragmentsNumbers.containsKey(fragment)){
            return mfragmentsNumbers.get(fragment);
        }else {
            return null;
        }
    }

    /**
     * returns the fragment with the name @param
     * @param fragmentName
     * @return
     */

    public Integer getFragmentNumber(String fragmentName){
        if (mfragmentsNumbers.containsKey(fragmentName)){
            return mfragmentsNumbers.get(fragmentName);
        }else {
            return null;
        }
    }

    /**
     * returns the fragment with the name @param
     * @param fragmentNumber
     * @return
     */

    public String getFragmentName(Integer fragmentNumber){
        if (mfragmentsNames.containsKey(fragmentNumber)){
            return mfragmentsNames.get(fragmentNumber);
        }else {
            return null;
        }
    }

}
