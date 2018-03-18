package com.example.trnclong.sqlite;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by TRẦN ĐỨC LONG on 3/18/2018.
 */

public class AdapterPerson extends RecyclerView.Adapter<AdapterPerson.MyViewHolder> {
        private List<ModelPerson> modelPersonList;
        private Context mContext;
        private LayoutInflater mInflater;

        public AdapterPerson(List<ModelPerson> modelPersonList, Context context){
            this.mContext = context;
            this.modelPersonList = modelPersonList;
            this.mInflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.item_contact,parent,false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
            ModelPerson mModelPerson = modelPersonList.get(position);
            holder.mTvName.setText(mModelPerson.getmName());

            holder.line.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickedListener != null) {
                        onItemClickedListener.onItemClick(view,position);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return modelPersonList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder{
            TextView mTvName;
            LinearLayout line;

            public MyViewHolder(View itemView){
                super(itemView);
                itemView.setClickable(true);
                mTvName = itemView.findViewById(R.id.tvName);
                line = itemView.findViewById(R.id.layout_line);
            }
        }

        public interface OnItemClickedListener {
            void onItemClick(View view, int position);
        }

        private OnItemClickedListener onItemClickedListener;

        public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
            this.onItemClickedListener = onItemClickedListener;
        }
    }

