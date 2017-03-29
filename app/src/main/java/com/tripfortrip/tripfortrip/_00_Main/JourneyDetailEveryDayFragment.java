package com.tripfortrip.tripfortrip._00_Main;

/**
 * Created by wei-tzutseng on 2017/3/19.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tripfortrip.tripfortrip.R;

import java.util.ArrayList;
/**
 * Created by coderzpassion on 05/04/16.
 */
public class JourneyDetailEveryDayFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.journey_detail_everyday_fragment,container,false);
//        RecyclerView recyclerView=(RecyclerView) inflater.inflate(R.layout.journey_detail_everyday_fragment,container,false);
        RecyclerView recyclerView= (RecyclerView) view.findViewById(R.id.recyclerviewEverday);
        setUpRecyclerView(recyclerView);
        return  view;
    }
    private void setUpRecyclerView(RecyclerView rv)
    {
        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
        rv.setAdapter(new AboutUsAdapter(rv.getContext(), getListForItems()));
    }
    public ArrayList<String> getListForItems()
    {
        ArrayList<String> list =new ArrayList<>();
        list.add("First Item");
        list.add("Second Item");
//        list.add("Third Item");
//        list.add("Fourth Item");
//        list.add("Fifth Item");
//        list.add("Six Item");
//        list.add("Seven Item");
//        list.add("8 Item");
//        list.add("9 Item");
//        list.add("10 Item");
//        list.add("11 Item");
//        list.add("12 Item");
//        list.add("13 Item");
//        list.add("14 Item");
//        list.add("15 Item");
//        list.add("16 Item");
//        list.add("17 Item");
//        list.add("18 Item");
//        list.add("19 Item");
//        list.add("20 Item");
//        list.add("21 Item");
        return list;
    }
    public  static class AboutUsAdapter extends RecyclerView.Adapter<AboutUsAdapter.ViewHolder>
    {
        ArrayList<String> aboutlist=new ArrayList<>();
        Context aboutuscontext;
        public static class ViewHolder extends RecyclerView.ViewHolder
        {
            public final TextView items;
            public ViewHolder(View view)
            {
                super(view);
                items=(TextView)view.findViewById(R.id.itemname);
            }
        }
        public AboutUsAdapter(Context context,ArrayList<String> list)
        {
            aboutuscontext=context;
            aboutlist=list;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.listitems, parent, false);
            return  new ViewHolder(view);
        }
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.items.setText(aboutlist.get(position));
        }
        @Override
        public int getItemCount() {
            return aboutlist.size();
        }
    }
}
