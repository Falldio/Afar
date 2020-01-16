package com.newsmap.afar.search;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.newsmap.afar.R;
import com.newsmap.afar.search.searchedNewsFragment.OnListFragmentInteractionListener;
import com.newsmap.afar.data.news;

import java.util.ArrayList;

/**
 * {@link RecyclerView.Adapter} that can display a {@link news} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */

 public class searchedNewsRecyclerViewAdapter extends RecyclerView.Adapter<searchedNewsRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<news> mValues;
    private final OnListFragmentInteractionListener mListener;

    public searchedNewsRecyclerViewAdapter(ArrayList<news> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_searchednews, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getTitle());
        holder.mContentView.setText(mValues.get(position).getContent());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
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
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public news mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = view.findViewById(R.id.item_number);
            mContentView = view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
