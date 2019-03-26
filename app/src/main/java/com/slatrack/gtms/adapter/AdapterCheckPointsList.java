package com.slatrack.gtms.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.slatrack.gtms.activity.ActivityWelcome;
import com.slatrack.gtms.fragment.FragmentScan;
import com.slatrack.gtms.R;
import com.slatrack.gtms.model.ModelCheckINData;
import com.slatrack.gtms.model.ModelCheckpoints;
import com.slatrack.gtms.model.ModelEventData;
import com.slatrack.gtms.utils.DialogEventList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class AdapterCheckPointsList extends BaseAdapter  {

    ArrayList<ModelCheckpoints> checkpointsArrayList;
    LayoutInflater inflater;
    private Context context;
    private int resource;
    private int iSwipeCount;
    ActivityWelcome activityWelcome;
    FragmentScan scan;

    private String gpslat = "0.00";
    private String gpslong= "0.00";



    public int getiSwipeCount() {
        return iSwipeCount;
    }

    public void setiSwipeCount(int iSwipeCount) {
        this.iSwipeCount = iSwipeCount;
    }

    public AdapterCheckPointsList(ArrayList<ModelCheckpoints> checkpointsArrayList, Context context) {

        this.activityWelcome = (ActivityWelcome)context;
        this.checkpointsArrayList = checkpointsArrayList;
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        scan = (FragmentScan) activityWelcome.getSupportFragmentManager().getFragments().get(0);

    }

    @Override
    public int getCount() {
        return checkpointsArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return checkpointsArrayList.get(i);
    }

    private void UpdateCheckedItem() {
        int countSelected = 0;
        int iCount = checkpointsArrayList.size();
        for(int counter = 0; counter<iCount; counter++){
            ModelCheckpoints checkpoints = checkpointsArrayList.get(counter);
            if(checkpoints.isChecked().equalsIgnoreCase("YES")){
                countSelected++;
            }
        }
        setiSwipeCount(countSelected);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        MyViewHolder holder = new MyViewHolder();

        if (view == null){
            view = inflater.inflate(R.layout.item_checkpoint,viewGroup,false);
            holder.checkPointName  = (TextView) view.findViewById(R.id.checkpointName);
            holder.checkb = (AppCompatImageView) view.findViewById(R.id.checkb);
            view.setTag(holder);
        }else {
            holder = (MyViewHolder) view.getTag();
        }

        final ModelCheckpoints checkpoints = checkpointsArrayList.get(position);
        holder.checkPointName.setText(checkpoints.getCheckpointname());

        if (checkpoints.getCheckpointcode().equalsIgnoreCase(scan.checkInCode)) {

            holder.checkb.setImageDrawable(ContextCompat.getDrawable(activityWelcome,R.drawable.check_animator));

            if(checkpoints.isChecked().equalsIgnoreCase("NO")){

                checkpoints.setChecked("YES");
                InsertCheckInCard(scan.checkInCode);
                UpdateCheckedItem();

                String formatted = String.format("%03d", getiSwipeCount());
                scan.swipeCountText.setText(formatted);

                scan.startscan.setText("START");
                final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
                animation.setDuration(1000); // duration - half a second
                animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
                animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
                animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in

                if(activityWelcome.database.getOrganization().getSubtype().equalsIgnoreCase("FMS")) {
                    DialogEventList dialogEventList = new DialogEventList(activityWelcome, scan.checkInCode);
                    dialogEventList.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                            WindowManager.LayoutParams.MATCH_PARENT);
                    dialogEventList.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                    dialogEventList.show();
                }

            }

        }else {

            if(checkpoints.isChecked().equalsIgnoreCase("NO")) {
                holder.checkb.setImageDrawable(ContextCompat.getDrawable(activityWelcome,R.drawable.ic_unchecked));
            }
        }

        return view;
    }




    private class MyViewHolder{
        TextView checkPointName;//,swipeDate,swipeTime;
        AppCompatImageView checkb;
    }

    private void  InsertCheckInCard(String chkPointCode){

        Date date = new Date();
        SimpleDateFormat mdformatTime = new SimpleDateFormat("HH:mm:ss ");
        String swipeTime = mdformatTime.format(date);

        SimpleDateFormat mdformatDate = new SimpleDateFormat("dd/MM/yyyy");
        String swipeDate = mdformatDate.format(date);
        String readerCode = scan.globalVariable.getReaderCode();
        String eventcode = scan.globalVariable.getEventcode();

        if(scan.database.getOrganization().getSubtype().equalsIgnoreCase("EVENTS")) {
            if(scan.database.getFeature().getEnable_gps().equalsIgnoreCase("1")){
                //getLocation();
            }
            else{
                 gpslat = "0.00";
                 gpslong= "0.00";
            }
            ModelEventData evData = new ModelEventData(readerCode, chkPointCode,eventcode,swipeTime, swipeDate,gpslat,gpslong);
            scan.database.insertEventData(evData);
        }
        else{
            ModelCheckINData chkData = new ModelCheckINData(readerCode, chkPointCode, swipeTime, swipeDate);
            scan.database.insertCheckInData(chkData);
        }



    }

}
