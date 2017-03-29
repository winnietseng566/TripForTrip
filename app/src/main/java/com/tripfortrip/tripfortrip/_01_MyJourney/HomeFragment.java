package com.tripfortrip.tripfortrip._01_MyJourney;


        import android.app.Activity;
        import android.app.DatePickerDialog;
        import android.app.Dialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.os.Bundle;
        import android.support.annotation.Nullable;
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
        import android.view.inputmethod.InputMethodManager;
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
        import com.tripfortrip.tripfortrip._00_Main.JourneyDetailFragment;
        import com.tripfortrip.tripfortrip._00_Main.init.MySQLiteOpenHelper;
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
    private Activity mainActivity;
    private static int mYear, mMonth, mDay, mHour, mMinute;
    MySQLiteOpenHelper helper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = getActivity();
        if(helper == null){
            helper = new MySQLiteOpenHelper(mainActivity);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        journeyList =  new ArrayList<>();
//        mainActivity = getActivity();
        Log.d("HomeFragment","onCreateView");
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
                final LinearLayout input = (LinearLayout) LayoutInflater.from(mainActivity).inflate(R.layout.journey_create,null);
                final EditText edJouneyName = (EditText) input.findViewById(R.id.edt_jouneyName);
                final EditText edJouneyStart = (EditText) input.findViewById(R.id.edt_jouneyStart);
                final ImageView iv_calendarStart = (ImageView) input.findViewById(R.id.iv_calendarStart);
                    iv_calendarStart.setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View v) { //選擇起始日期
                            if(!edJouneyName.getText().toString().equals("")) {
                                DatePickerDialogFragment datePickerFragment = new DatePickerDialogFragment();
                                datePickerFragment.setEdDate(edJouneyStart);
                                FragmentManager fm = getActivity().getSupportFragmentManager();
                                datePickerFragment.show(fm, "datePicker");
    //                        edJouneyStart.setText(mYear+"/"+mMonth+1+"/"+mDay);
                                Log.d("edJouneyStart", "click");

                                //鍵盤自動隱藏
                                InputMethodManager imm =
                                        (InputMethodManager)mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(edJouneyName.getWindowToken(),0);

                            }else {
                                //iv_calendarStart無動作
                            }
                        }
                    });

                final EditText edJouneyEnd =(EditText) input.findViewById(R.id.edt_jouneyEnd);
                ImageView iv_calendarEnd = (ImageView) input.findViewById(R.id.iv_calendarEnd);
                iv_calendarEnd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { //選擇結束日期
                        if(!edJouneyName.getText().toString().equals("") && !edJouneyStart.getText().toString().equals("")) {
                            DatePickerDialogFragment datePickerFragment = new DatePickerDialogFragment();
                            datePickerFragment.setEdDate( edJouneyEnd);
                            FragmentManager fm = getActivity().getSupportFragmentManager();
                            datePickerFragment.show(fm, "datePicker");
    //                        edJouneyEnd.setText(mYear+"/"+mMonth+1+"/"+mDay);
                            Log.d("edJouneyStart","click");
                        }else {
                            //iv_calendarStart無動作
                        }
                    }
                });
                showNow(); //按下新增我的行程按鈕日期一律是當天
                final AlertDialog createrJourneyDialog = new AlertDialog.Builder(mainActivity)
                        .setTitle("新增我的行程")
                        .setView(input)
                        .setPositiveButton("新增", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String jouneyName= edJouneyName.getText().toString();

                                String jouneyStart=edJouneyStart.getText().toString();

                                String jouneyEnd=edJouneyEnd.getText().toString();
                                if(!jouneyName.equals("") && !jouneyStart.equals("") && !jouneyEnd.equals("")){
                                    Toast.makeText(mainActivity,jouneyName+"/"+jouneyStart+"/"+jouneyEnd,Toast.LENGTH_SHORT).show();
//                                    int picRandom = getPicRandom();
//                                    JourneyBean journeyBean =
//                                            new JourneyBean(picRandom,jouneyName,jouneyStart,jouneyEnd);
//                                    journeyList.add(journeyBean);
//                                    showAllMyJournry(fgMyJournry);

                                    //改成SQLite
                                    int picRandom = getPicRandom();
                                    JourneyBean journeyBean =
                                            new JourneyBean(picRandom,jouneyName,jouneyStart,jouneyEnd);
                                    long rowId = helper.insert(journeyBean);
                                    if(rowId !=-1){
                                        Toast.makeText(mainActivity,"新增成功",Toast.LENGTH_SHORT).show();
                                        showAllMyJournry(rv_journey);
                                    }else {
                                        Toast.makeText(mainActivity,"新增失敗",Toast.LENGTH_SHORT).show();

                                    }
//                                    journeyList.add(journeyBean);
//                                    showAllMyJournry(fgMyJournry);

                                }else {
                                    Toast.makeText(mainActivity,"噢噢～是不是少輸入了什麼呢？",Toast.LENGTH_SHORT).show();

                                }

                            }

                            private int getPicRandom() {
                                int picRandom = (int) (Math.random()*8);
                                List<Integer> picRandomList = new ArrayList<Integer>();
                                picRandomList.add(R.drawable.view_jouney0);
                                picRandomList.add(R.drawable.view_jouney1);
                                picRandomList.add(R.drawable.view_jouney3);
                                picRandomList.add(R.drawable.view_jouney4);
                                picRandomList.add(R.drawable.view_jouney5);
                                picRandomList.add(R.drawable.view_jouney6);
                                picRandomList.add(R.drawable.view_jouney7);
                                picRandomList.add(R.drawable.view_jouney8);
                                return picRandomList.get(picRandom);
                            }
                        })
                        .setNegativeButton("取消",null)
//                         .create();
                        .show();

//                createrJourneyDialog.setOnShowListener(new DialogInterface.OnShowListener() {
//
//                    @Override
//                    public void onShow(DialogInterface dialog) {
//
//                        Button b = ((AlertDialog)createrJourneyDialog).getButton(AlertDialog.BUTTON_POSITIVE);
//                        b.setOnClickListener(new View.OnClickListener() {
//
//                            @Override
//                            public void onClick(View view) {
//                                            // TODO Do something
//                                String jouneyName= edJouneyName.getText().toString();
//
//                                String jouneyStart=edJouneyStart.getText().toString();
//
//                                String jouneyEnd=edJouneyEnd.getText().toString();
//                                if(!jouneyName.equals("") && !jouneyStart.equals("") && !jouneyEnd.equals("")){
//                                    Toast.makeText(mainActivity,jouneyName+"/"+jouneyStart+"/"+jouneyEnd,Toast.LENGTH_SHORT).show();
//                                    int picRandom = getPicRandom();
//                                    JourneyBean journeyBean =
//                                            new JourneyBean(picRandom,jouneyName,jouneyStart,jouneyEnd);
//                                    journeyList.add(journeyBean);
//                                    showAllMyJournry(fgMyJournry);
//                                    // Dismiss once everything is OK.
////                                    createrJourneyDialog.dismiss();
//
//                                }else {
//                                    Toast.makeText(mainActivity,"噢噢～是不是少輸入了什麼呢？",Toast.LENGTH_SHORT).show();
//
//                                }
//
//
//                            }
//                            private int getPicRandom() {
//                                int picRandom = (int) (Math.random()*8);
//                                List<Integer> picRandomList = new ArrayList<Integer>();
//                                picRandomList.add(R.drawable.view_jouney0);
//                                picRandomList.add(R.drawable.view_jouney1);
//                                picRandomList.add(R.drawable.view_jouney3);
//                                picRandomList.add(R.drawable.view_jouney4);
//                                picRandomList.add(R.drawable.view_jouney5);
//                                picRandomList.add(R.drawable.view_jouney6);
//                                picRandomList.add(R.drawable.view_jouney7);
//                                picRandomList.add(R.drawable.view_jouney8);
//                                return picRandomList.get(picRandom);
//                            }
//                        });
//                    }
//                });
            }
        });
        return fgMyJournry;
    }

    @Override
    public void onStart() {
        super.onStart();

        showAllMyJournry(fgMyJournry);
        Log.d("onStart","onStart");




    }

    @Override
    public void onResume() {
        super.onResume();
        mainActivity.setTitle(R.string.text_myJourney);//重新設定顯示名稱
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(helper !=null){
            helper.close();
        }
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
//        journeyList = getJourneyList();

        journeyList = helper.getAllJourney();

        rv_journey.setLayoutManager(new LinearLayoutManager(mainActivity));
        JourneyAdapter adapter = new JourneyAdapter(mainActivity,journeyList);
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
                    Fragment fragment = new JourneyDetailFragment();
                    //把journey的資料用bundle帶去JourneyDetailFragment
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("journey",journey);
                    fragment.setArguments(bundle);
                    final FragmentManager fragmentManager =((FragmentActivity)context).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction =
                            fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.body, fragment);
                    fragmentTransaction.addToBackStack(null);//關掉backbutton的預設（才會記得Fragment)
                    fragmentTransaction.commit();
//
                    ((Activity)context).setTitle(journey.getTitle()+"|"+journey.getDateStart()+"~"+journey.getDateEnd());

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
//    private  void switchFragment(Fragment fragment) {
//        FragmentTransaction fragmentTransaction =
//                getFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.body, fragment);
//        fragmentTransaction.addToBackStack(null);//關掉backbutton的預設（才會記得Fragment)
//        fragmentTransaction.commit();
//    }

}


