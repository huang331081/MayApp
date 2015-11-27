package com.haley.may.mayapp;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.view.WindowManager;

import com.haley.may.mayapp.Base.BaseActivity;
import com.haley.may.mayapp.Manager.MayFragmentManager;
import com.haley.may.mayapp.NavigationDrawerFragment;
import com.haley.may.mayapp.R;

public class MainActivity extends BaseActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private int beforePosition = 0;

    private static MainActivity instance = null;

    public static MainActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        //haley:
        //this.getSupportActionBar().hide();
        //this.getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.WeatherBadDrawable));
        //((DrawerLayout)findViewById(R.id.drawer_layout)).openDrawer(GravityCompat.START);
        instance = this;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.anim_fragment_in_alpha, R.anim.anim_fragment_out_alpha)
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();

        //translate的变化方式
//        if (position > beforePosition)
//            fragmentManager.beginTransaction()
//                    .setCustomAnimations(R.anim.anim_fragment_in_bottom, R.anim.anim_fragment_out_bottom)
//                    .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
//                    .commit();
//        else if (position < beforePosition)
//            fragmentManager.beginTransaction()
//                    .setCustomAnimations(R.anim.anim_fragment_in_up, R.anim.anim_fragment_out_up)
//                    .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
//                    .commit();
//        else
//            fragmentManager.beginTransaction()
//                    .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
//                    .commit();

        beforePosition = position;
    }

    public void onSectionAttached(int number) {
//        switch (number) {
//            case 1:
//                mTitle = getString(R.string.title_section1);
//                break;
//            case 2:
//                mTitle = getString(R.string.title_section2);
//                break;
//            case 3:
//                mTitle = getString(R.string.title_section3);
//                break;
//        }

        //haley:
        mTitle = MayFragmentManager.getInstance().getTitles().get(number-1);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);

        //haley:
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i("MainActivity","onCreateOptionsMenu-->>");
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.

            //haley:
            MayFragmentManager.getInstance().getShowingFragment().initActionBar(getMenuInflater(),menu);
            //getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            Log.i("PlaceholderFragment", "newInstance-->>");
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            MayFragmentManager.getInstance().initViews(inflater);
            View roowView = MayFragmentManager.getInstance().getView(this.getArguments().getInt(ARG_SECTION_NUMBER),inflater,container);
            return roowView;
        }


        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
