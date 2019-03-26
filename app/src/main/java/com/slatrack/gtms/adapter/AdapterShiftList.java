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
import com.slatrack.gtms.model.ModelShift;

import java.util.ArrayList;


public class AdapterShiftList  extends BaseAdapter {

    ArrayList<ModelShift> shiftArrayList;
    Context context;
    LayoutInflater inflater;

    public AdapterShiftList(ArrayList<ModelShift> shiftArrayList, Context context) {
        this.shiftArrayList = shiftArrayList;
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return shiftArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return shiftArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        MyViewHolder holder = new MyViewHolder();

        if (view == null){

            view = inflater.inflate(R.layout.item_shift,viewGroup,false);

            holder.shiftname  = (TextView) view.findViewById(R.id.shiftname);
            holder.shiftfrom  = (TextView) view.findViewById(R.id.shiftfrom);
            holder.shiftto= (TextView) view.findViewById(R.id.shiftto);
            holder.slashift_id= (TextView) view.findViewById(R.id.slashift_id);

            holder.btnshiftselect= (Button) view.findViewById(R.id.btnshiftselect);

            view.setTag(holder);
        }else {
            holder = (MyViewHolder) view.getTag();
        }

        final ModelShift shiftlist = shiftArrayList.get(position);
        holder.shiftname.setText(shiftlist.getShiftname());

       // ModelShift shiftCntx = (ModelShift) context;


        holder.btnshiftselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModelShift temp = shiftArrayList.get(position);
                Toast.makeText(context, temp.getShiftname()+" is clicked", Toast.LENGTH_SHORT).show();

            }
        });

        return view;
    }

    private class MyViewHolder{


        TextView shiftname,shiftfrom,shiftto,slashift_id;
        Button btnshiftselect;
    }


}
