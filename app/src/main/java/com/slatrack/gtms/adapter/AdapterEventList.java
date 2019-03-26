package com.slatrack.gtms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.slatrack.gtms.R;
import com.slatrack.gtms.activity.ActivityWelcome;
import com.slatrack.gtms.fragment.FragmentEventList;
import com.slatrack.gtms.fragment.FragmentScan;
import com.slatrack.gtms.model.ModelEvents;

import java.util.ArrayList;

public class AdapterEventList  extends BaseAdapter {

    ArrayList<ModelEvents> eventsArrayList;
    Context context;
    LayoutInflater inflater;
    ActivityWelcome activityWelcome;
    FragmentEventList event;


    public AdapterEventList(ArrayList<ModelEvents> eventsArrayList, Context context) {
        this.activityWelcome = (ActivityWelcome)context;
        this.eventsArrayList = eventsArrayList;
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        event = (FragmentEventList) activityWelcome.getSupportFragmentManager().getFragments().get(0);
    }

    @Override
    public int getCount() {
        return eventsArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return eventsArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        MyViewHolder holder = new MyViewHolder();

        if (view == null){
            view = inflater.inflate(R.layout.item_event,viewGroup,false);

            holder.itemCheckbox= (CheckBox) view.findViewById(R.id.list_view_item_checkbox);
            holder.eventname  = (TextView) view.findViewById(R.id.eventname);
            view.setTag(holder);
        }else {
            holder = (MyViewHolder) view.getTag();
        }

        final ModelEvents events = eventsArrayList.get(position);
        holder.eventname.setText(events.getEventname());


        holder.itemCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    ModelEvents temp = eventsArrayList.get(position);
                    if(!temp.isChecked()) {
                        temp.setChecked(true);
                    }
                    else{
                        temp.setChecked(false);
                    }
                }
        });

        return view;
    }

    private class MyViewHolder{
        TextView eventname;
        CheckBox itemCheckbox;
    }

    private void  InsertEventInCard(String chkPointCode){
//        FragmentScan scan = (FragmentScan) context;
//
//        ClassDatabase dbLocal = scan.database;
//
//        Date date = new Date();
//        SimpleDateFormat mdformatTime = new SimpleDateFormat("hh:MM:ss ");
//        String swipeTime = mdformatTime.format(date);
//
//        SimpleDateFormat mdformatDate = new SimpleDateFormat("dd/MM/yyyy");
//        String swipeDate = mdformatDate.format(date);
//
//        String readerCode = scan.globalVariableOld.getReaderCode();
//
//        ModelCheckINData chkData = new ModelCheckINData(readerCode,chkPointCode,swipeTime,swipeDate);
//        dbLocal.insertCheckInData(chkData);

    }

}
