package com.example.sreekanthojasengtest.AdapterClasses;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sreekanthojasengtest.ListenerClasses.UpdateCountedListLisetener;
import com.example.sreekanthojasengtest.R;
import com.example.sreekanthojasengtest.UtilClasses.HitsData;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {
    private static final String TAG = "CustomAdapter";
    public static int DATA = 1;
    public static int PROGRESS = -1;
    Context mContext;
    ArrayList<HitsData> list;
    UpdateCountedListLisetener lisetener;
    int count=0;

    public CustomAdapter(Context c, ArrayList<HitsData> data, UpdateCountedListLisetener listLisetener) {
        this.mContext = c;
        this.list = data;
        this.lisetener = listLisetener;
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position) != null)
            return DATA;
        else
            return PROGRESS;
    }

    public void addNullData() {
        list.add(null);
        notifyItemInserted(list.size() - 1);
    }

    public void removeNull() {
        list.remove(list.size() - 1);
        notifyItemRemoved(list.size());
    }

    public void addData(ArrayList<HitsData> hitsData) {
        list.addAll(hitsData);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        Log.e(TAG, "onCreateViewHolder: " + viewType);
        if (viewType == DATA) {
            v = LayoutInflater.from(mContext).inflate(R.layout.data, parent, false);
            return new DataViewHolder(v);
        } else {
            v = LayoutInflater.from(mContext).inflate(R.layout.progress, parent, false);
            return new ProgressUpdate(v);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Log.e(TAG, "onBindViewHolder: ");
        if (holder instanceof DataViewHolder) {

            final DataViewHolder h = (DataViewHolder) holder;
            h.textViewCreatedAt.setText("Title: "+list.get(position).created_at);
            h.textViewTitle.setText("Created At: "+list.get(position).title);
//            ((DataViewHolder) holder).switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
//                    mContext = compoundButton.getContext();
//                    bChecked=h.switchView.isChecked();
//                    if (bChecked) {
//
//                        if(count>=1){
//                            count--;
//                            lisetener.updateCountedList(count);
//                        }
//
//                    } else {
//                        Log.e(TAG, "onCheckedChanged: "+count );
//                        count++;
//                        lisetener.updateCountedList(count);
//
//
//                    }
//
//
//                }
//
//
//            });

            h.layoutParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    boolean bChecked=h.switchView.isChecked();

                    if (bChecked) {
                        h.switchView.setChecked(false);

                        if(count>=1){
                            count--;
                            lisetener.updateCountedList(count);
                        }

                    } else {
                        h.switchView.setChecked(true);
                        count++;
                        lisetener.updateCountedList(count);



                    }

                }
            });

        } else {

        }


    }

    @NonNull


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    class DataViewHolder extends CustomViewHolder {
        TextView textViewTitle;
        TextView textViewCreatedAt;
        Switch switchView;
        RelativeLayout layoutParent;

        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCreatedAt = itemView.findViewById(R.id.textView_createdDate);
            textViewTitle = itemView.findViewById(R.id.textView_title);
            switchView = itemView.findViewById(R.id.switch_view);
            layoutParent = itemView.findViewById(R.id.layout_parent);
        }
    }

    class ProgressUpdate extends CustomViewHolder {

        public ProgressUpdate(@NonNull View itemView) {
            super(itemView);
        }
    }
}
