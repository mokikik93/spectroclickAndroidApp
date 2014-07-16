package com.example.spectroclick;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ResultActivity extends FragmentActivity{
	
    CollectionPagerAdapter cpa;
    private static JSONObject js;
    private static JSONArray instructions;
    private static int inst_length;

    ViewPager vp;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        final ActionBar actionBar = getActionBar();
        
        try {
			init();} 
        catch (JSONException e) {
			e.printStackTrace();}

        cpa = new CollectionPagerAdapter(getSupportFragmentManager(),inst_length);

        actionBar.setDisplayHomeAsUpEnabled(true);

        vp = (ViewPager) findViewById(R.id.pager);
        vp.setAdapter(cpa);
    }
    
    public void init() throws JSONException{
    	Intent intent = getIntent();
    	String raw = intent.getStringExtra(BaseActivity.RAW_RESULT);
    	js = new JSONObject(raw);
    	instructions = js.getJSONArray("inst");
    	inst_length = instructions.length();
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent upIntent = new Intent(this, BaseActivity.class);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    TaskStackBuilder.from(this)
                            .addNextIntent(upIntent)
                            .startActivities();
                    finish();
                } else {
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class CollectionPagerAdapter extends FragmentStatePagerAdapter {
    	static int pageCount;
    	static String raw;

        public CollectionPagerAdapter(FragmentManager fm, int pc) {
            super(fm);
            pageCount = pc;
        }

        @Override
        public Fragment getItem(int index) {
            Fragment fragment = new InstructionFragment();
            Bundle args = new Bundle();
            args.putInt(InstructionFragment.ARG_PAGE_NUMBER, index + 1);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return pageCount;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "STEP " + (position + 1);
        }
    }

    public static class InstructionFragment extends Fragment {

        public static final String ARG_PAGE_NUMBER = "page number";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_instruction, container, false);
            Bundle args = getArguments();
            int page_number = args.getInt(ARG_PAGE_NUMBER);
            JSONObject inst;
            try {
				inst = instructions.getJSONObject((page_number-1));
				((TextView) rootView.findViewById(android.R.id.text1)).setText(inst.getString("context"));
			} catch (JSONException e) {
				e.printStackTrace();
			}   
            return rootView;
        }
    }
}
