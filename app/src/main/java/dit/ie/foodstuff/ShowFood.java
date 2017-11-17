package dit.ie.foodstuff;

import android.support.v4.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ShowFood extends Fragment
{
    private AppBarLayout appBarLayout;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.show_food, container, false);

        View content_main = (View)container.getParent();
        appBarLayout = (AppBarLayout)content_main.findViewById(R.id.appbar);
        tabLayout = new TabLayout(getActivity());
        tabLayout.setTabTextColors(Color.parseColor("#FFFFFF"), Color.parseColor("#FFFFFF"));

        viewPager = (ViewPager)view.findViewById(R.id.pager);
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        appBarLayout.removeView(tabLayout);
    }

    public class ViewPagerAdapter extends FragmentStatePagerAdapter
    {
        public ViewPagerAdapter(FragmentManager fragmentManager)
        {
            super(fragmentManager);
        }

        String[] titleTabs = {"Fridge", "Fruit & Veg", "Confectionery", "Miscellaneous"};

        @Override
        public Fragment getItem(int position)
        {
            switch(position)
            {
                case 0:
                    return new TabFridge();
                case 1:
                    return new TabFruitVeg();
                case 2:
                    return new TabConfectionery();
                case 3:
                    return new TabMiscellaneous();
            }
            return null;
        }

        @Override
        public int getCount()
        {
            return 4;
        }

        public CharSequence getPageTitle (int position)
        {
            return titleTabs[position];
        }
    }
}
