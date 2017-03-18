package com.tripfortrip.tripfortrip._00_Main;


        import android.app.Activity;
        import android.app.DatePickerDialog;
        import android.app.Dialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.os.Bundle;
        import android.support.design.widget.Snackbar;
        import android.support.design.widget.TextInputLayout;
        import android.support.v4.app.DialogFragment;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentActivity;
        import android.support.v4.app.FragmentManager;
        import android.support.v4.app.FragmentTransaction;
        import android.support.v4.content.ContextCompat;
        import android.support.v4.widget.SwipeRefreshLayout;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.DatePicker;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.ListAdapter;
        import android.widget.Switch;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.tripfortrip.tripfortrip.R;
        import com.tripfortrip.tripfortrip._01_MyJourney.JourneyBean;

        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.List;

public class HomeFragment extends Fragment {
    RecyclerView rv_journey;
    List<JourneyBean> journeyList;//我的行程list
    private SwipeRefreshLayout swipeRefreshLayout; //更新
    private ListAdapter adapter;
    private View fgMyJournry;
    private Context context;
    private static int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        journeyList =  new ArrayList<>();
        context = this.getActivity();
        fgMyJournry = inflater.inflate(R.layout.home_fragment, container, false);
        //向下拉更新動畫
        swipeRefreshLayout =
                (SwipeRefreshLayout) fgMyJournry.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);//動畫開始
                showAllMyJournry(fgMyJournry);
                swipeRefreshLayout.setRefreshing(false);//動畫結束
            }
        });
        // Snackbar掛在coordinatorLayout上，確保不會和原來的畫面重疊可以右滑移除
        final View coordinatorLayout = fgMyJournry.findViewById(R.id.coordinatorLayout);
        final View btAdd = fgMyJournry.findViewById(R.id.btAdd);



        //新增我的行程按鈕
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //新增我的行程輸入內容
                final LinearLayout input = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.journey_create,null);
                final EditText edJouneyStart= (EditText) input.findViewById(R.id.edt_jouneyStart);
                ImageView iv_calendarStart = (ImageView) input.findViewById(R.id.iv_calendarStart);
                iv_calendarStart.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerDialogFragment datePickerFragment = new DatePickerDialogFragment();
                        datePickerFragment.setEdDate( edJouneyStart);
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        datePickerFragment.show(fm, "datePicker");
//                        edJouneyStart.setText(mYear+"/"+mMonth+1+"/"+mDay);
                        Log.d("edJouneyStart","click");
                    }
                });
                final EditText edJouneyEnd =(EditText) input.findViewById(R.id.edt_jouneyEnd);
                ImageView iv_calendarEnd = (ImageView) input.findViewById(R.id.iv_calendarEnd);
                iv_calendarEnd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerDialogFragment datePickerFragment = new DatePickerDialogFragment();
                        datePickerFragment.setEdDate( edJouneyEnd);
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        datePickerFragment.show(fm, "datePicker");
//                        edJouneyEnd.setText(mYear+"/"+mMonth+1+"/"+mDay);
                        Log.d("edJouneyStart","click");
                    }
                });
                showNow();
                new AlertDialog.Builder(context)
                        .setTitle("新增我的行程")
                        .setView(input)
                        .setPositiveButton("新增", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String jouneyName= ((EditText) input.findViewById(R.id.edt_jouneyName)).getText().toString();

                                String jouneyStart=edJouneyStart.getText().toString();

                                String jouneyEnd=edJouneyEnd.getText().toString();
                                if(!jouneyName.equals("") && !jouneyStart.equals("") && !jouneyEnd.equals("")){
                                    Toast.makeText(context,jouneyName+"/"+jouneyStart+"/"+jouneyEnd,Toast.LENGTH_SHORT).show();
                                    int picRandom = getPicRandom();
                                    JourneyBean journeyBean =
                                            new JourneyBean(picRandom,jouneyName,jouneyStart,jouneyEnd);
                                    journeyList.add(journeyBean);
                                    showAllMyJournry(fgMyJournry);
                                }else {
                                    Toast.makeText(context,"噢噢～是不是少輸入了什麼呢？",Toast.LENGTH_SHORT).show();

                                }

                            }

                            private int getPicRandom() {
                                int picRandom = (int) (Math.random()*8);
                                switch (picRandom){
                                    case 0:
                                        picRandom = R.drawable.view_jouney0;
                                        break;
                                    case 1:
                                        picRandom = R.drawable.view_jouney1;
                                        break;
                                    case 2:
                                        picRandom = R.drawable.view_jouney2;
                                        break;
                                    case 3:
                                        picRandom = R.drawable.view_jouney3;
                                        break;
                                    case 4:
                                        picRandom = R.drawable.view_jouney4;
                                        break;
                                    case 5:
                                        picRandom = R.drawable.view_jouney5;
                                        break;
                                    case 6:
                                        picRandom = R.drawable.view_jouney6;
                                        break;
                                    case 7:
                                        picRandom = R.drawable.view_jouney7;
                                        break;
                                    case 8:
                                        picRandom = R.drawable.view_jouney8;
                                        break;
                                }
                                return picRandom;
                            }
                        })
                        .setNegativeButton("取消",null)
                        .show();
            }
        });
        return fgMyJournry;
    }

    @Override
    public void onStart() {
        super.onStart();
        showAllMyJournry(fgMyJournry);
    }
    private static void showNow() {
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
//        mHour = calendar.get(Calendar.HOUR_OF_DAY);
//        mMinute = calendar.get(Calendar.MINUTE);

    }

    //  日期選擇器
    public static class DatePickerDialogFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {
        private EditText edDate;

        public void setEdDate(EditText edDate) {
            this.edDate = edDate;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new DatePickerDialog(
                    this.getActivity(), this, mYear, mMonth, mDay);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            mYear = year;
            mMonth = month;
            mDay = day;
            edDate.setText(mYear+"/"+(mMonth+1)+"/"+mDay);
        }
    }

    private void showAllMyJournry(View view) {
        rv_journey = (RecyclerView) view.findViewById(R.id.rv_journey);
        journeyList = getJourneyList();

        rv_journey.setLayoutManager(new LinearLayoutManager(context));
        JourneyAdapter adapter = new JourneyAdapter(context,journeyList);
        rv_journey.setHasFixedSize(true);
        rv_journey.setAdapter(adapter);
    }

    private List<JourneyBean> getJourneyList(){
//        journeyList.add(new JourneyBean(R.drawable.navigate_background_tanzania,"Tanzania","2000/10/10"));
//        journeyList.add(new JourneyBean(R.drawable.navigate_background,"where","2000/10/10"));
//        journeyList.add(new JourneyBean(R.drawable.navigate_background_paris,"Paris","2000/10/10"));
//        journeyList.add(new JourneyBean(R.drawable.navigate_background_tanzania,"Tanzania","2000/10/10"));
        return journeyList;
    }

    private static class JourneyAdapter extends RecyclerView.Adapter<JourneyAdapter.ViewHolder>{
        Context context;
        List<JourneyBean> journeyList;
        View itemView;
//        private OnRecyclerViewItemClickListener mOnItemClickListener = null;


        private JourneyAdapter(Context context,List<JourneyBean> journeyList){
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
            final JourneyBean journey = journeyList.get(position);
            holder.journeyPic.setImageResource(journey.getPic());
            holder.journeyTitle.setText(journey.getTitle());
            holder.journeyInfo.setText(journey.getDateStart()+"~"+journey.getDateEnd());
            holder.itemView.setOnClickListener(new View.OnClickListener() {//點擊我的旅程
                @Override
                public void onClick(View v) {
                    Fragment fragment = new com.tripfortrip.tripfortrip._00_Main.SceneFragment();
//                    switchFragment(fragment);
                    ((Activity)context).setTitle(R.string.text_Scene);

                    Log.d(" onItemClick"," onItemClick");
//                    switchFragment(fragment);  //設定返回鍵是記錄Fragment而不是結束Activity
                }
            });
        }

        @Override
        public int getItemCount() {
            return journeyList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            //類似捷徑的概念，宣告連結到元件的變數
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
    private  void switchFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.body, fragment);
        fragmentTransaction.addToBackStack(null);//關掉backbutton的預設（才會記得Fragment)
        fragmentTransaction.commit();
    }

}


