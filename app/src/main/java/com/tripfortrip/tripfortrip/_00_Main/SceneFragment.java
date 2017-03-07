package com.tripfortrip.tripfortrip._00_Main;

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
import java.util.List;

public class SceneFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.scene_fragment, container, false);
        List<News> newsList = new ArrayList<>();
        newsList.add(new News("Title 1", "Detail 1"));
        newsList.add(new News("Title 2", "Detail 2"));
        newsList.add(new News("Title 3", "Detail 3"));

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new NewsAdapter(inflater, newsList));
        return view;
    }

    private class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
        private LayoutInflater inflater;
        private List<News> newsList;
        private View visibleView;

        public NewsAdapter(LayoutInflater inflater, List<News> newsList) {
            this.inflater = inflater;
            this.newsList = newsList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = inflater.inflate(R.layout.scene_item, viewGroup, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int position) {
            News news = newsList.get(position);
            viewHolder.tvTitle.setText(news.getTitle());
            viewHolder.tvDetail.setText(news.getDetail());
            viewHolder.tvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (viewHolder.tvDetail.getVisibility()) {
                        case View.VISIBLE:
                            viewHolder.tvDetail.setVisibility(View.GONE);
                            break;
                        case View.GONE:
                            if (visibleView != null) {
                                visibleView.setVisibility(View.GONE);
                            }
                            viewHolder.tvDetail.setVisibility(View.VISIBLE);
                            visibleView = viewHolder.tvDetail;
                            break;
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return newsList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle, tvDetail;

            public ViewHolder(View itemView) {
                super(itemView);
                tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
                tvDetail = (TextView) itemView.findViewById(R.id.tvDetail);
            }
        }
    }
}
