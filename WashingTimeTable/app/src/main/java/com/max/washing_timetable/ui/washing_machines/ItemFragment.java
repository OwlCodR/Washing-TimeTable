package com.max.washing_timetable.ui.washing_machines;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.max.washing_timetable.R;
import com.max.washing_timetable.dummy.DummyContent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.max.washing_timetable.MainActivity.TAG;

public class ItemFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private DatabaseReference mDatabase;
    private List<Object> mList = new ArrayList<>();

    public ItemFragment() {
    }

    @SuppressWarnings("unused")
    public static ItemFragment newInstance(int columnCount) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            final RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));


            WashingMachine washingMachine = new WashingMachine("1607762094", "1607763094");
            Map<String, WashingMachine> washingMachines = new HashMap<>();
            washingMachines.put("washingMachine1", washingMachine);
            washingMachines.put("washingMachine2", washingMachine);
            Floor floor = new Floor(washingMachines);
            Map<String, Floor> floors = new HashMap<>();
            floors.put("floor1", floor);
            floors.put("floor2", floor);
            floors.put("floor3", floor);
            floors.put("floor4", floor);
            floors.put("floor5", floor);
            Hostel hostel1 = new Hostel("Корпус №1: г. Москва, ул. Москворечье, д. 2, корпус 1", floors);
            Hostel hostel2 = new Hostel("Корпус №2: г. Москва, ул. Москворечье, д. 2, корпус 2", floors);
            Hostel hostel3 = new Hostel("Корпус №3: г. Москва, ул. Москворечье, д. 19, корпус 3", floors);
            Hostel hostel4 = new Hostel("Корпус №4: г. Москва, ул. Москворечье, д. 19, корпус 4", floors);
            Hostel hostel5 = new Hostel("Корпус №5: г. Москва, ул. Кошкина, д. 11, корпус 1", floors);
            Hostel hostel6 = new Hostel("Корпус №7: г. Москва, ул. Шкулева, д. 27, стр. 2", floors);
            Hostel hostel7 = new Hostel("Корпус №8: г. Москва, Пролетарский проспект, д. 8, корпус 2", floors);

            Map<String, Hostel> hostelHashMap = new HashMap<>();
            hostelHashMap.put("hostel1", hostel1);
            hostelHashMap.put("hostel2", hostel2);
            hostelHashMap.put("hostel3", hostel3);
            hostelHashMap.put("hostel4", hostel4);
            hostelHashMap.put("hostel5", hostel5);
            hostelHashMap.put("hostel6", hostel6);
            hostelHashMap.put("hostel7", hostel7);
            //mList.add(hostelHashMap);



            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("hostels").setValue(hostelHashMap);
            ValueEventListener listener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Hostel hostel = data.getValue(Hostel.class);
                            mList.add(hostel);
                        }
                    }

                    Toast.makeText(getActivity(), "YES " + mList.size(), Toast.LENGTH_SHORT).show();
                    recyclerView.setAdapter(new MyItemRecyclerViewAdapter(mList));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                }
            };
            //mDatabase.child("hostels").addListenerForSingleValueEvent(listener);
            mDatabase.child("hostels").addValueEventListener(listener);
        }
        return view;
    }
}