package com.slatrack.gtms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.slatrack.gtms.R;
import com.slatrack.gtms.model.ModelSchedule;

import java.util.ArrayList;


public class AdapterScheduleList  extends BaseAdapter {

    ArrayList<ModelSchedule> scheduleArrayList;
    Context context;
    LayoutInflater inflater;

    public AdapterScheduleList(ArrayList<ModelSchedule> scheduleArrayList, Context context) {
        this.scheduleArrayList = scheduleArrayList;
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return scheduleArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return scheduleArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        MyViewHolder holder = new MyViewHolder();

        if (view == null){

            view = inflater.inflate(R.layout.item_schedule,viewGroup,false);

            holder.shiftname  = (TextView) view.findViewById(R.id.shiftname);
            holder.schedulefrom  = (TextView) view.findViewById(R.id.schedulefrom);
            holder.scheduleto= (TextView) view.findViewById(R.id.scheduleto);
            holder.slaschedule_id= (TextView) view.findViewById(R.id.slaschedule_id);
            holder.slashift_id= (TextView) view.findViewById(R.id.slashift_id);


            holder.btnschduletselect= (Button) view.findViewById(R.id.btnschduletselect);

            view.setTag(holder);
        }else {
            holder = (MyViewHolder) view.getTag();
        }

        final ModelSchedule schedulelist = scheduleArrayList.get(position);
        holder.shiftname.setText(schedulelist.getShiftname());

        // ModelShift shiftCntx = (ModelShift) context;


        holder.btnschduletselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModelSchedule temp = scheduleArrayList.get(position);
                Toast.makeText(context, temp.getShiftname()+" is clicked", Toast.LENGTH_SHORT).show();

            }
        });

        return view;
    }

    private class MyViewHolder{

//        private String slaschedule_id;
//        private String slashift_id;
//        private String society_id;
//        private String user_id;
//        private String shiftname;
//        private String schedulefrom;
//        private String scheduleto;


        TextView shiftname,schedulefrom,scheduleto,slaschedule_id,slashift_id;
        Button btnschduletselect;
    }


}
