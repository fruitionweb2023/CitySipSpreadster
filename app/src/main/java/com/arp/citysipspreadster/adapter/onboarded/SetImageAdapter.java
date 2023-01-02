package com.arp.citysipspreadster.adapter.onboarded;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import androidx.recyclerview.widget.RecyclerView;
import com.arp.citysipspreadster.R;
import com.arp.citysipspreadster.model.onBoardedBusinesses.OnboardedBusiness1;
import java.util.List;

public class SetImageAdapter extends RecyclerView.Adapter<SetImageAdapter.ViewHolder> {

    Context context;
    List<OnboardedBusiness1> stringArrayList;
    OnItemClickListner addButtonClick;

    public SetImageAdapter(Context context, List<OnboardedBusiness1> stringArrayList, OnItemClickListner addButtonClick ) {
        this.context = context;
        this.stringArrayList = stringArrayList;
        this.addButtonClick = addButtonClick;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == stringArrayList.size()) ? R.layout.raw_required_reminder_onboarded_businesses : R.layout.raw_select_all_checkbox_onboard_businesses;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView;
        if (viewType == R.layout.raw_required_reminder_onboarded_businesses) {
            itemView = LayoutInflater.from(context).inflate(R.layout.raw_required_reminder_onboarded_businesses,viewGroup,false);
        } else {
            itemView = LayoutInflater.from(context).inflate(R.layout.raw_select_all_checkbox_onboard_businesses,viewGroup,false);
        }
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        if (position == stringArrayList.size()) {


        } else {



        }
    }


    @Override
    public int getItemCount() {
        return stringArrayList.size() + 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox singal;
        CheckBox multiple;
        Button btnDone;

        public ViewHolder(View itemView ) {
            super(itemView);

            singal = (CheckBox) itemView.findViewById(R.id.cbOnBusiness);
            multiple = (CheckBox) itemView.findViewById(R.id.cbSelectAllOnBusiness);
            btnDone = (Button) itemView.findViewById(R.id.btnDone);
        }


    }

    public interface OnItemClickListner{
        public void onAddButtonClick(int postion);

    }

}