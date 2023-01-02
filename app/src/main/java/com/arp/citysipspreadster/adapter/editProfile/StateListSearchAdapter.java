package com.arp.citysipspreadster.adapter.editProfile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arp.citysipspreadster.R;
import com.arp.citysipspreadster.model.countryList.Country;
import com.arp.citysipspreadster.model.stateList.Category;

import java.util.List;


public class StateListSearchAdapter extends RecyclerView.Adapter<StateListSearchAdapter.FruitViewHolder> {

    List<Category> mDataset;
    RecyclerViewItemClickListener recyclerViewItemClickListener;

    public StateListSearchAdapter(List<Category> myDataset, RecyclerViewItemClickListener listener) {
        mDataset = myDataset;
        this.recyclerViewItemClickListener = listener;
    }
    @NonNull
    @Override
    public FruitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_recycler_view, parent, false);

        FruitViewHolder vh = new FruitViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull FruitViewHolder fruitViewHolder, int i) {


        fruitViewHolder.mTextView.setText(mDataset.get(i).getName());

    }

    @Override
    public int getItemCount() {
        return (mDataset == null) ? 0 : mDataset.size();
    }


    public interface RecyclerViewItemClickListener {
        void clickOnStateListItem(String data, String id);
    }

    public class FruitViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mTextView, mId;

        public FruitViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.tvName);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recyclerViewItemClickListener.clickOnStateListItem(mDataset.get(this.getAdapterPosition()).getName(), mDataset.get(this.getAdapterPosition()).getId());

        }


    }

    public void updatelist(List<Category> productMasters) {
        mDataset = productMasters;
        notifyDataSetChanged();
    }


}