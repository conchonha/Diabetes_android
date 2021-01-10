package com.example.diabetes.adapter;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diabetes.R;
import com.example.diabetes.model.DateModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CalendaAdapter extends RecyclerView.Adapter<CalendaAdapter.Viewhdler> {
    private ArrayList<DateModel>arrayList;
    private Context context;
    private int layout;

    public CalendaAdapter(ArrayList<DateModel> arrayList, Context context, int layout) {
        this.arrayList = arrayList;
        this.context = context;
        this.layout = layout;
    }

    @NonNull
    @Override
    public Viewhdler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context,layout,null);
        return new Viewhdler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Viewhdler holder, int position) {
        Calendar calendar = Calendar.getInstance();
        final LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0,5,30,0);
        int today = calendar.get(Calendar.DAY_OF_WEEK);

        holder.txtTextCalendar.setText(arrayList.get(position).getDate()+"");
        if(arrayList.get(position).getDate() == (today+1)){
            holder.txtTextCalendar.setBackgroundResource(R.drawable.boder_text_calenda);
            holder.txtTextCalendar.setTextColor(context.getResources().getColor(R.color.colorwhile));
            holder.txtTextCalendar.setLayoutParams(lp);
            holder.txtTextCalendar.setGravity(Gravity.CENTER);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class Viewhdler extends RecyclerView.ViewHolder{
        private TextView txtTextCalendar;
        public Viewhdler(@NonNull View itemView) {
            super(itemView);
            txtTextCalendar = itemView.findViewById(R.id.txtTextCalendar);

        }
    }
}
