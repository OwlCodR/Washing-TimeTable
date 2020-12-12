package com.max.washing_timetable.ui.washing_machines;

import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.max.washing_timetable.R;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.max.washing_timetable.MainActivity.TAG;

public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> implements View.OnClickListener {

    private final List<Object> mList;

    public MyItemRecyclerViewAdapter(List<Object> items) {
        mList = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mView.setOnClickListener(this);

        Type genericClass = mList.get(position).getClass();
        if (genericClass == Hostel.class) {
            holder.mainImage.setImageResource(R.drawable.hostel);
            holder.title.setText("Общежитие");
            holder.subtitle.setText(((Hostel) mList.get(position)).getAddress());
            holder.stateImage.setVisibility(View.GONE);
        } else if (genericClass == Floor.class) {
            holder.mainImage.setImageResource(R.drawable.floor);
            holder.title.setText("Этаж " + (position + 1));
            holder.subtitle.setText("Количество стиральных машин на этом этаже: "
                    + ((Floor) mList.get(position)).getWashingMachines().size());
            holder.stateImage.setVisibility(View.GONE);
        } else if (genericClass == WashingMachine.class) {
            holder.mainImage.setImageResource(R.drawable.washing_machine);
            holder.title.setText("Стиральная Машина " + (position + 1));
            WashingMachine washingMachine = (WashingMachine) mList.get(position);
            long itemLong = (long) (Integer.parseInt(washingMachine.getEndTime()) / 1000);
            Date date = new Date(itemLong * 1000L);
            String itemDate = new SimpleDateFormat("dd-MMM HH:mm").format(date);

            holder.subtitle.setText("Время окончания работы: " + itemDate);
            holder.stateImage.setImageResource(R.drawable.ic_online);
            holder.stateImage.setVisibility(View.VISIBLE);
        } else {
            Log.d(TAG, "Error" + genericClass);
        }
        // @TODO else if (genericClass == User.class)
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onClick(View v) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public TextView title;
        public TextView subtitle;
        public ImageView mainImage;
        public ImageView stateImage;
        //public DummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            title = view.findViewById(R.id.item_title);
            subtitle = view.findViewById(R.id.item_subtitle);
            mainImage = view.findViewById(R.id.imageViewItem);
            stateImage = view.findViewById(R.id.imageViewItemState);
        }
    }
}