package com.foodonet.foodonet.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.foodonet.foodonet.R;
import com.foodonet.foodonet.adapters.NotificationsRecyclerAdapter;
import com.foodonet.foodonet.commonMethods.CommonConstants;
import com.foodonet.foodonet.commonMethods.CommonMethods;
import com.foodonet.foodonet.db.NotificationsDBHandler;

public class NotificationsFragment extends Fragment {
    private NotificationsRecyclerAdapter adapter;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_notifications, container, false);

        RecyclerView recyclerNotifications = (RecyclerView) v.findViewById(R.id.recyclerNotifications);
        recyclerNotifications.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NotificationsRecyclerAdapter(getContext(),this);
        recyclerNotifications.setAdapter(adapter);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.updateNotifications();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.notification_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.clear_notifications){
            NotificationsDBHandler notificationsDBHandler = new NotificationsDBHandler(getContext());
            notificationsDBHandler.deleteAllNotification();
            CommonMethods.updateUnreadNotificationID(getContext(), CommonConstants.NOTIFICATION_ID_CLEAR);
            adapter.clearNotifications();
        }
        return true;
    }
}
