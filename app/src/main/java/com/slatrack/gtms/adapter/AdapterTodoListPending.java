package com.slatrack.gtms.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.slatrack.gtms.R;
import com.slatrack.gtms.activity.ActivityWelcome;
import com.slatrack.gtms.fragment.FragmentTxnReport;
import com.slatrack.gtms.model.ModelDoneItem;
import com.slatrack.gtms.model.ModelPendingItem;

import java.util.List;

public class AdapterTodoListPending extends BaseAdapter {

    private Context mContext;
    private List<ModelPendingItem> modelPendingItems;
    private LayoutInflater inflater;
    ActivityWelcome activityWelcome;
    FragmentTxnReport txnSummary;

    public AdapterTodoListPending(Context context, List<ModelPendingItem> modelPendingItems) {
        this.activityWelcome = (ActivityWelcome)context;
        this.mContext = context;
        this.modelPendingItems = modelPendingItems;
        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        txnSummary = (FragmentTxnReport) activityWelcome.getSupportFragmentManager().getFragments().get(0);
    }

    @Override
    public int getCount() {
        return modelPendingItems.size();
    }

    @Override
    public Object getItem(int i) {
        return modelPendingItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        ViewHolder holder = new ViewHolder();
        if (view == null) {

            view = inflater.inflate(R.layout.item_todolist_pending, viewGroup, false);

            holder.checkpointName = (AppCompatTextView) view.findViewById(R.id.checkpointName);
            holder.plannedDate = (AppCompatTextView) view.findViewById(R.id.plannedDate);
            holder.actualDate = (AppCompatTextView) view.findViewById(R.id.actualDate);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }


        ModelPendingItem modelPendingItem = modelPendingItems.get(position);
        holder.checkpointName.setText(String.valueOf(modelPendingItem.getChkname()));
        holder.plannedDate.setText(String.valueOf(modelPendingItem.getPlanneddate()));

        return view;
    }

    private class ViewHolder {
        AppCompatTextView checkpointName, plannedDate, actualDate;

    }


}
