package com.slatrack.gtms.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.slatrack.gtms.R;
import com.slatrack.gtms.activity.ActivitySubList;
import com.slatrack.gtms.fragment.FragmentSubList;
import com.slatrack.gtms.model.ModelSubscription;
import com.slatrack.gtms.utils.ClassCommon;
import com.slatrack.gtms.utils.ClassUtility;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class AdapterActivitySubsList extends BaseAdapter {

    ArrayList<ModelSubscription> subrateArrayList;
    Context context;
    LayoutInflater inflater;
    ActivitySubList activitysublist;
//    activitysublist activitysublist;

    public AdapterActivitySubsList(ArrayList<ModelSubscription> subrateArrayList, Context context) {
        this.activitysublist = (ActivitySubList)context;
        this.subrateArrayList = subrateArrayList;
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

//        activitysublist = (FragmentSubList) activitysublist.getSupportFragmentManager().getFragments().get(0);
    }

    @Override
    public int getCount() {
        return subrateArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return subrateArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        MyViewHolder holder = new MyViewHolder();

        if (view == null){

            view = inflater.inflate(R.layout.item_subrates,viewGroup,false);

            holder.subnametxt  = (AppCompatTextView) view.findViewById(R.id.subnametxt);
            holder.subratestxt  = (AppCompatTextView) view.findViewById(R.id.subratestxt);
            holder.subperiodtxt= (AppCompatTextView) view.findViewById(R.id.subperiodtxt);
            holder.suboffertxt= (AppCompatTextView) view.findViewById(R.id.suboffertxt);
            holder.subamounttxt= (AppCompatTextView) view.findViewById(R.id.subamounttxt);
            holder.subscribe= (AppCompatButton) view.findViewById(R.id.subscribe);
            holder.gradientView= (RelativeLayout) view.findViewById(R.id.gradientView);

            view.setTag(holder);
        }else {
            holder = (MyViewHolder) view.getTag();
        }

        final ModelSubscription subrates = subrateArrayList.get(position);

        holder.subnametxt.setText(String.valueOf(subrates.getSubname()));
        holder.subratestxt.setText(String.valueOf(subrates.getSubrates()));
        holder.subperiodtxt.setText(String.valueOf(subrates.getSubperiod()));
        holder.suboffertxt.setText(String.valueOf(subrates.getSuboffertext()));
        holder.subamounttxt.setText(String.valueOf(subrates.getSubamount()));

        holder.subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(context.getResources().getString(R.string.subscribe_msg));
                builder.setPositiveButton(context.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        requestSubscription(subrates.getSubid().toString());
                    }
                });
                builder.setNegativeButton(context.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                builder.show();
            }
        });

        if (position%3 == 1){

            holder.gradientView.setBackground(ContextCompat.getDrawable(context,R.drawable.gradient_view_1));

        }else if ((position%3 == 2)){

            holder.gradientView.setBackground(ContextCompat.getDrawable(context,R.drawable.gradient_view_2));

        }else {

            holder.gradientView.setBackground(ContextCompat.getDrawable(context,R.drawable.gradient_view_3));
        }


        return view;
    }

    private class MyViewHolder{

        AppCompatTextView subnametxt, subratestxt, subperiodtxt, subamounttxt, suboffertxt;
        AppCompatButton subscribe;
        RelativeLayout gradientView;

    }

    private void requestSubscription(String subid){


        AsyncHttpClient client = new AsyncHttpClient();

        if(!ClassUtility.IsConnectedToNetwork(activitysublist)){
            Toast.makeText(activitysublist, activitysublist.getResources().getString(R.string.nonetwork_mobiledataoff), Toast.LENGTH_SHORT).show();
            return;
        }

        activitysublist.progressBar.setVisibility(View.VISIBLE);
        String imei = activitysublist.globalVariable.getImei();
        String configURL = ClassCommon.BASE_URL+ClassCommon.SET_SUBSCRIPTIONREQUEST+"data="+imei+","+subid;

        client.post(configURL,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {

                    String response = new String(responseBody,"UTF-8");
                    JSONObject responseObj = new JSONObject(response);

                    if (!responseObj.getBoolean("ERROR")){
                        String strResponseCode = responseObj.getString("STATUS");

                        if (strResponseCode.equals(ClassCommon.SUCCESS)) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage(context.getResources().getString(R.string.thankyou_subscription));
                            builder.setPositiveButton(context.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                  //  requestSubscription(subrates.getSubid().toString());
                                }
                            });

                            builder.show();

                        } else if (strResponseCode.equals(ClassCommon.REQUESTINPROCESS)) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            String msgString = String.format(context.getString(R.string.subscription_pending), activitysublist.globalVariable.getSupportphone());
                            builder.setMessage(msgString);

                            builder.setPositiveButton(context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //  requestSubscription(subrates.getSubid().toString());
                                }
                            });
                            builder.show();
                            activitysublist.progressBar.setVisibility(View.GONE);

                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage(context.getResources().getString(R.string.nonetwork_text));
                            builder.setPositiveButton(context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //  requestSubscription(subrates.getSubid().toString());
                                }
                            });
                            builder.show();
                            activitysublist.progressBar.setVisibility(View.GONE);
                        }


                    }else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage(context.getResources().getString(R.string.nonetwork_text));
                        builder.setPositiveButton(context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //  requestSubscription(subrates.getSubid().toString());
                            }
                        });
                        builder.show();
                        activitysublist.progressBar.setVisibility(View.GONE);
                    }


                }catch (Exception e){
                    e.printStackTrace();
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage(context.getResources().getString(R.string.nonetwork_text));
                    builder.setPositiveButton(context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //  requestSubscription(subrates.getSubid().toString());
                        }
                    });
                    builder.show();
                    activitysublist.progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {


                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(context.getResources().getString(R.string.nonetwork_text));
                builder.setPositiveButton(context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //  requestSubscription(subrates.getSubid().toString());
                    }
                });
                builder.show();
                activitysublist.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {
                activitysublist.progressBar.setVisibility(View.GONE);
            }
        });
    }


}
