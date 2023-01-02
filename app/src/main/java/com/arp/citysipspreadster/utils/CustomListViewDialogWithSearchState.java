package com.arp.citysipspreadster.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arp.citysipspreadster.R;
import com.arp.citysipspreadster.adapter.editProfile.CountryListSearchAdapter;
import com.arp.citysipspreadster.adapter.editProfile.StateListSearchAdapter;
import com.arp.citysipspreadster.model.countryList.Country;
import com.arp.citysipspreadster.model.stateList.Category;

import java.util.ArrayList;
import java.util.List;

public class CustomListViewDialogWithSearchState extends Dialog implements View.OnClickListener {


    public Activity activity;
    public Dialog dialog;
    public Button yes, no;
    public EditText search;
    TextView title;
    RecyclerView recyclerView;
    StateListSearchAdapter adapter;
    List<Category> newParties;
    LinearLayout ll_error;
    private RecyclerView.LayoutManager mLayoutManager;

    public CustomListViewDialogWithSearchState(Context context, int themeResId) {
        super(context, themeResId);
    }

    public CustomListViewDialogWithSearchState(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    public CustomListViewDialogWithSearchState(Activity a, StateListSearchAdapter adapter, List<Category> newParties) {
        super(a);
        this.activity = a;
        this.adapter = adapter;
        this.newParties = newParties;
        setupLayout();


    }


    private void setupLayout() {




    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.recycler_view_with_search_state);
        recyclerView = findViewById(R.id.recycler_view_with_search);
        search = (EditText) findViewById(R.id.edt_search);
        mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);
        ll_error = findViewById(R.id.ll_error);


        recyclerView.setAdapter(adapter);
        //yes.setOnClickListener(this);
        //no.setOnClickListener(this);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                filterCategory(s.toString());

            }
        });

    }


    @Override
    public void onClick(View v) {
       /* switch (v.getId()) {
            case R.id.yes:
                //Do Something
                break;
            case R.id.no:
                dismiss();
                break;
            default:
                break;
        }*/
        dismiss();
    }

    public void filterCategory(String text) {


        ArrayList<Category> temp = new ArrayList<>();

        if (newParties != null) {
            for (Category d : newParties) {
                //or use .equal(text) with you want equal match
                //use .toLowerCase() for better matches
                if ((d.getName().toUpperCase()).contains(text.toUpperCase())) {

                    temp.add(d);
                }
            }
        }

        //update recyclerview
        //
        adapter.updatelist(temp);

        if (temp.size() == 0) {
            ll_error.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            ll_error.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

    }


}