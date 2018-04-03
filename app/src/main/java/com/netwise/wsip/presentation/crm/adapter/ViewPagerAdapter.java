package com.netwise.wsip.presentation.crm.adapter;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;

import com.netwise.wsip.R;
import com.netwise.wsip.WsipApp;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Filter;

/**
 * Created by dawido on 13.03.2018.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
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

    @Override
    public CharSequence getPageTitle(int position) {
        SpannableStringBuilder sb;
        if(mFragmentList.size() == 1 || position == 0){
            Drawable myDrawable = WsipApp.getAppContext().getDrawable(R.drawable.school48);
            sb = new SpannableStringBuilder( "     " + mFragmentTitleList.get(position));
            myDrawable.setBounds(0, 0, myDrawable.getIntrinsicWidth() / 2, myDrawable.getIntrinsicHeight() /2);
            ImageSpan span = new ImageSpan(myDrawable, DynamicDrawableSpan.ALIGN_BOTTOM);
            sb.setSpan(span, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }else{
            Drawable myDrawable = WsipApp.getAppContext().getDrawable(R.drawable.user48);
            sb = new SpannableStringBuilder("    " + mFragmentTitleList.get(position));
            myDrawable.setBounds(0, 0, myDrawable.getIntrinsicWidth() /2 , myDrawable.getIntrinsicHeight() /2);
            ImageSpan span = new ImageSpan(myDrawable, DynamicDrawableSpan.ALIGN_BOTTOM);
            sb.setSpan(span, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return sb;
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

}
