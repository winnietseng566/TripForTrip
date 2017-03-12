package com.tripfortrip.tripfortrip._00_Main;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import com.tripfortrip.tripfortrip.R;

public class HomeFragment extends Fragment {
    RecyclerView rv_journey;
    List<Journey> journeyList;
    private ListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        rv_journey = (RecyclerView) view.findViewById(R.id.rv_journey);
        journeyList = getJourneyList();

        Context context = this.getActivity();
        rv_journey.setLayoutManager(new LinearLayoutManager(context));
        JourneyAdapter adapter = new JourneyAdapter(context,journeyList);
        rv_journey.setAdapter(adapter);


        final View coordinatorLayout = view.findViewById(R.id.coordinatorLayout);
        View btAdd = view.findViewById(R.id.btAdd);
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Having a CoordinatorLayout in your view hierarchy
                // allows Snackbar to enable certain features,
                // such as swipe-to-dismiss and
                // automatically moving of widgets like FloatingActionButton.
                Snackbar.make(
                        coordinatorLayout,
                        "Add button clicked",
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        });
        return view;
    }

    private List<Journey> getJourneyList(){
        List<Journey> journeyList = new ArrayList<>();
        journeyList.add(new Journey(R.drawable.navigate_background_tanzania,"Tanzania","2000/10/10"));
        journeyList.add(new Journey(R.drawable.navigate_background_tanzania,"Tanzania","2000/10/10"));
        journeyList.add(new Journey(R.drawable.navigate_background_tanzania,"Tanzania","2000/10/10"));
        journeyList.add(new Journey(R.drawable.navigate_background_tanzania,"Tanzania","2000/10/10"));
        return journeyList;
    }

    private class JourneyAdapter extends RecyclerView.Adapter<JourneyAdapter.ViewHolder> {
        Context context;
        List<Journey> journeyList;
        View itemView;
        private JourneyAdapter(Context context,List<Journey> journeyList){
            this.context = context;
            this.journeyList = journeyList;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //展開layout items
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            itemView = layoutInflater.inflate(R.layout.journey_card,parent,false);
            return new ViewHolder(itemView); //展開的layout傳給ViewHolder連結元件
        }

        @Override
        public void onBindViewHolder(JourneyAdapter.ViewHolder holder, int position) {
            final Journey journey = journeyList.get(position);
            holder.journeyPic.setImageResource(journey.getPic());
            holder.journeyTitle.setText(journey.getTitle());
            holder.journeyInfo.setText(journey.getInfo());
        }

        @Override
        public int getItemCount() {
            return journeyList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            //連結到元件的變數
            ImageView journeyPic;
            TextView journeyTitle, journeyInfo;
            public ViewHolder(View itemView) {
                super(itemView);
                journeyPic = (ImageView) itemView.findViewById(R.id.iv_journey_pic);
                journeyTitle = (TextView) itemView.findViewById(R.id.txv_journey_title);
                journeyInfo = (TextView) itemView.findViewById(R.id.txv_journey_info);
            }

        }
    }

}
