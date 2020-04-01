package com.newsmap.afar.search;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;

import com.newsmap.afar.R;
import com.newsmap.afar.search.searchedNewsFragment.OnListFragmentInteractionListener;
import com.newsmap.afar.data.news;

import java.util.ArrayList;

//将新闻搜索结果存入RecyclerView
public class searchedNewsRecyclerViewAdapter extends RecyclerView.Adapter<searchedNewsRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<news> mValues;
    private final OnListFragmentInteractionListener mListener;

    searchedNewsRecyclerViewAdapter(ArrayList<news> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    //创建新的View
    @Override
    @NonNull public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_searched_news, parent, false);
        return new ViewHolder(view);
    }

    //改变View内容，将ViewHolder绑定到数据上
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mTitleView.setText(mValues.get(position).getTitle());
        holder.mSourceView.setText(mValues.get(position).getSource());
        holder.mDateView.setText(mValues.get(position).getDate());
        holder.mContentView.setText(mValues.get(position).getContent());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    //每一个Item对应的子View
    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mTitleView;
        final TextView mContentView;
        final TextView mSourceView;
        final TextView mDateView;
        news mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = view.findViewById(R.id.title);
            mContentView = view.findViewById(R.id.content);
            mSourceView=view.findViewById(R.id.source);
            mDateView=view.findViewById(R.id.date);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
