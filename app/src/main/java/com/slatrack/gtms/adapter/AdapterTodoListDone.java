package com.slatrack.gtms.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.slatrack.gtms.R;
import com.slatrack.gtms.activity.ActivityWelcome;
import com.slatrack.gtms.fragment.FragmentTxnReport;
import com.slatrack.gtms.model.ModelDoneItem;
import com.slatrack.gtms.model.ModelPunchReport;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdapterTodoListDone extends BaseAdapter {

    private Context mContext;
    private List<ModelDoneItem> modelDoneItems;
    private LayoutInflater inflater;
    ActivityWelcome activityWelcome;
    FragmentTxnReport txnSummary;

    public AdapterTodoListDone(Context context, List<ModelDoneItem> modelDoneItems) {
        this.activityWelcome = (ActivityWelcome)context;
        this.mContext = context;
        this.modelDoneItems = modelDoneItems;
        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        txnSummary = (FragmentTxnReport) activityWelcome.getSupportFragmentManager().getFragments().get(0);
    }

    @Override
    public int getCount() {
        return modelDoneItems.size();
    }

    @Override
    public Object getItem(int i) {
        return modelDoneItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        ViewHolder holder = new ViewHolder();
        if (view == null) {

            view = inflater.inflate(R.layout.item_todolist_done, viewGroup, false);

            holder.checkpointName = (AppCompatTextView) view.findViewById(R.id.checkpointName);
            holder.plannedDate = (AppCompatTextView) view.findViewById(R.id.plannedDate);
            holder.actualDate = (AppCompatTextView) view.findViewById(R.id.actualDate);
            holder.nextDue = (AppCompatTextView) view.findViewById(R.id.nextDue);
            holder.status = (AppCompatTextView) view.findViewById(R.id.status);
            holder.performedBy = (AppCompatTextView) view.findViewById(R.id.performedBy);
            holder.mapIcon = (AppCompatImageView) view.findViewById(R.id.mapIcon);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }


        final ModelDoneItem modelDoneItem = modelDoneItems.get(position);
        holder.checkpointName.setText(String.valueOf(modelDoneItem.getChkname()));
        holder.plannedDate.setText(String.valueOf(modelDoneItem.getPlanneddate()));
        holder.actualDate.setText(String.valueOf(modelDoneItem.getActualdate()));
        holder.nextDue.setText(String.valueOf(modelDoneItem.getNextdue()));
        holder.status.setText("-");
        holder.performedBy.setText(String.valueOf(modelDoneItem.getUsername()));

        holder.mapIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Double.parseDouble(modelDoneItem.getGpsLat()) != 0.0 && Double.parseDouble(modelDoneItem.getGpsLong()) != 0.0){
                    String uri = String.format(Locale.ENGLISH, "geo:%f,%f", 18.6069074, 73.76315269999999);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    mContext.startActivity(intent);
                }
            }
        });

        return view;
    }

    private class ViewHolder {
        AppCompatTextView checkpointName, plannedDate, actualDate, nextDue,status,performedBy;
        AppCompatImageView mapIcon;

    }


}
