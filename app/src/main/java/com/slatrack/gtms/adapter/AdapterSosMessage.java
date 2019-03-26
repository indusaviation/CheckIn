package com.slatrack.gtms.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import com.slatrack.gtms.R;
import com.slatrack.gtms.fragment.FragmentSosmessage;
import com.slatrack.gtms.activity.ActivityWelcome;
import com.slatrack.gtms.model.ModelSosMessage;


public class AdapterSosMessage extends BaseAdapter {

    ArrayList<ModelSosMessage> sosMessageArrayLists;
    Context context;
    LayoutInflater inflater;
    ActivityWelcome activityWelcome;
    FragmentSosmessage sosfragment;

    public AdapterSosMessage(ArrayList<ModelSosMessage> sosArrayList, Context context,FragmentSosmessage sosfragment) {
        this.activityWelcome = (ActivityWelcome)context;
        this.sosMessageArrayLists = sosArrayList;
        this.context = context;
        this.sosfragment = sosfragment;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        sosfragment = (FragmentSosmessage) activityWelcome.getSupportFragmentManager().getFragments().get(0);
    }

    @Override
    public int getCount() {
        return sosMessageArrayLists.size();
    }

    @Override
    public Object getItem(int i) {
        return sosMessageArrayLists.get(i);
    }

    public int GetCheckedItem() {
        int countSelected = 0;
        int iCount = sosMessageArrayLists.size();
        for(int counter = 0; counter<iCount; counter++){
            ModelSosMessage sosmsg = sosMessageArrayLists.get(counter);
        }
        return countSelected;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

         MyViewHolder holder = new MyViewHolder();

        if (view == null){
            view = inflater.inflate(R.layout.item_sosmessage,viewGroup,false);
            holder.item_msgconst  = (TextView) view.findViewById(R.id.item_msgconst);
            holder.item_msgdetails  = (TextView) view.findViewById(R.id.msgdetails);
            holder.sendsos = (Button) view.findViewById(R.id.sendsos);
            holder.gradientView = (RelativeLayout) view.findViewById(R.id.gradientView);
            view.setTag(holder);
        }else {
            holder = (MyViewHolder) view.getTag();
        }

        final ModelSosMessage sosmessage = sosMessageArrayLists.get(position);
        holder.item_msgconst.setText(sosmessage.getMsgconst());
        holder.item_msgdetails.setText(sosmessage.getMsgdetails());

        holder.sendsos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              //  ModelSosMessage sostemp = sosMessageArrayLists.get(position);
              //  sosfragment.SendSOSMessage(sostemp.getMsgconst(),sostemp.getMsgdetails());
            }
        });

        if (position%3 == 2){
            holder.gradientView.setBackground(ContextCompat.getDrawable(context,R.drawable.gradient_view_1));
        }else if ((position%3 == 1)){
            holder.gradientView.setBackground(ContextCompat.getDrawable(context,R.drawable.gradient_view_3));
        }else {
            holder.gradientView.setBackground(ContextCompat.getDrawable(context,R.drawable.gradient_view_2));
        }


        return view;
    }

    private class MyViewHolder{
        TextView item_msgconst,item_msgdetails;//,swipeDate,swipeTime;
       // CheckBox item_soscheck;
        Button sendsos;
        RelativeLayout gradientView;
    }

}
