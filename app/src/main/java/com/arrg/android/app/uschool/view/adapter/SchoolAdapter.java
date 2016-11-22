package com.arrg.android.app.uschool.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.dragselectrecyclerview.DragSelectRecyclerViewAdapter;
import com.arrg.android.app.uschool.R;
import com.arrg.android.app.uschool.USchool;
import com.arrg.android.app.uschool.model.entity.School;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alberto on 13-Nov-16.
 */

public class SchoolAdapter extends DragSelectRecyclerViewAdapter<SchoolAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onClick(ViewHolder viewHolder, View itemView, int position);

        void onLongClick(ViewHolder viewHolder, View itemView, int position);
    }

    private ArrayList<School> schools;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private USchool uSchool;

    public SchoolAdapter(ArrayList<School> schools, Context context) {
        uSchool = USchool.getInstance();

        this.schools = schools;
        this.context = context;
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.school_item_layout, parent, false), onItemClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        School school = schools.get(position);

        String path = "http://" + uSchool.getHost() + ":" + uSchool.getPort() + "/uSchool/img/" + school.getNit() + "/logo.png";

        Log.e(getClass().getSimpleName(), path);

        Glide.with(context).load(path).into(holder.schoolLogo);

        holder.schoolName.setText(school.getName());
        holder.address.setText(String.format(": %s, %s, %s", school.getAddress(), school.getMunicipality(), school.getDepartment()));
        holder.telephone.setText(String.format(": %s", school.getPhone()));
        holder.qualification.setText(String.format("%s/5", school.getQualification()));
    }

    @Override
    public int getItemCount() {
        return schools.size();
    }

    @Override
    protected boolean isIndexSelectable(int index) {
        return super.isIndexSelectable(index);
    }

    public School getSchool(int position) {
        return schools.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        @BindView(R.id.schoolLogo)
        ImageView schoolLogo;
        @BindView(R.id.schoolName)
        TextView schoolName;
        @BindView(R.id.address)
        TextView address;
        @BindView(R.id.telephone)
        TextView telephone;
        @BindView(R.id.qualification)
        TextView qualification;

        private OnItemClickListener onItemClickListener;

        public ViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            this.onItemClickListener = onItemClickListener;

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null) {
                onItemClickListener.onClick(this, itemView, getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View view) {
            if (onItemClickListener != null) {
                onItemClickListener.onLongClick(this, itemView, getAdapterPosition());
            }
            return false;
        }
    }
}
