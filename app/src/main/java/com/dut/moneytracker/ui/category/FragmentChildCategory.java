package com.dut.moneytracker.ui.category;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dut.moneytracker.R;
import com.dut.moneytracker.adapter.ChildCategoryAdapter;
import com.dut.moneytracker.adapter.ClickItemListener;
import com.dut.moneytracker.adapter.ClickItemRecyclerView;
import com.dut.moneytracker.objects.Category;

import io.realm.RealmResults;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 09/03/2017.
 */

public class FragmentChildCategory extends Fragment implements ClickItemListener {
    private RecyclerView mRecyclerCategory;
    private ChildCategoryAdapter childCategoryRecyclerAdapter;
    private RealmResults<Category> categories;
    private PickCategoryListener pickCategoryListener;

    public interface PickCategoryListener {
        void onResultCategory(Category category);
    }

    public void setCategories(RealmResults<Category> categories) {
        this.categories = categories;
    }

    public void registerPickCategory(PickCategoryListener pickCategoryListener) {
        this.pickCategoryListener = pickCategoryListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragament_category, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        childCategoryRecyclerAdapter = new ChildCategoryAdapter(getActivity(), categories);
        mRecyclerCategory = (RecyclerView) view.findViewById(R.id.recyclerCategory);
        mRecyclerCategory.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerCategory.setAdapter(childCategoryRecyclerAdapter);
        mRecyclerCategory.addOnItemTouchListener(new ClickItemRecyclerView(getActivity(), this));
    }

    @Override
    public void onClick(View view, int position) {
        pickCategoryListener.onResultCategory(categories.get(position));
    }

}
