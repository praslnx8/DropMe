package com.prasilabs.dropme.modules.rideAlerts.view;

import android.content.Context;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.prasilabs.dropme.R;
import com.prasilabs.dropme.backend.dropMeApi.model.RideAlertIo;
import com.prasilabs.dropme.core.CoreAdapter;
import com.prasilabs.dropme.customs.FlowLayout;
import com.prasilabs.dropme.modules.rideAlerts.presenters.MyAlertsPresenter;
import com.prasilabs.dropme.utils.ViewUtil;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by prasi on 13/6/16.
 */
public class MyAlertsAdapter extends CoreAdapter<RideAlertIo, MyAlertsAdapter.MyAlertsViewHolder> implements MyAlertsPresenter.DeleteAlertCallBack
{
    private static MyAlertsAdapter instance;
    private Context context;
    private MyAlertsPresenter myAlertsPresenter;

    private MyAlertsAdapter(){}

    public static MyAlertsAdapter getInstance(Context context, MyAlertsPresenter myAlertsPresenter)
    {
        if(instance == null)
        {
            instance = new MyAlertsAdapter();
        }

        instance.context = context;
        instance.myAlertsPresenter = myAlertsPresenter;

        return instance;
    }

    @Override
    public MyAlertsViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = getInlfatedView(context, parent, R.layout.item_my_alert);

        return new MyAlertsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyAlertsViewHolder holder, int position)
    {
        holder.renderData(context, list.get(position));
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    @Override
    public void onResult(boolean status) {
        if (status) {
            ViewUtil.t(context, "Alert is deleted");
        } else {
            ViewUtil.t(context, "unable to delete");
        }
    }

    public class MyAlertsViewHolder extends RecyclerView.ViewHolder
    {

        @BindView(R.id.source_text)
        TextView sourceText;
        @BindView(R.id.dest_text)
        TextView destText;
        @BindView(R.id.menu_view)
        ImageButton menuView;
        @BindView(R.id.filter_layout)
        LinearLayout filterLayout;
        @BindView(R.id.filter_flow_layout)
        FlowLayout flowLayout;

        public MyAlertsViewHolder(View itemView)
        {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        public void renderData(final Context context, final RideAlertIo rideAlertIo)
        {
            sourceText.setText(rideAlertIo.getSourceName());
            destText.setText(rideAlertIo.getDestName());

            if (rideAlertIo.getGender() != null || rideAlertIo.getVehicleType() != null) {
                filterLayout.setVisibility(View.VISIBLE);

                flowLayout.removeAllViews();
                if (rideAlertIo.getGender() != null) {
                    View view = View.inflate(context, R.layout.item_filter_view, null);
                    TextView textView = (TextView) view.findViewById(R.id.filter_name_text);

                    textView.setText(rideAlertIo.getGender());

                    flowLayout.addView(view);
                }

                if (rideAlertIo.getVehicleType() != null) {
                    View view = View.inflate(context, R.layout.item_filter_view, null);
                    TextView textView = (TextView) view.findViewById(R.id.filter_name_text);

                    textView.setText(rideAlertIo.getVehicleType());

                    flowLayout.addView(view);
                }

                if (rideAlertIo.getStartTime() != null || rideAlertIo.getEndTime() != null) {
                    View view = View.inflate(context, R.layout.item_filter_view, null);
                    TextView textView = (TextView) view.findViewById(R.id.filter_name_text);

                    if (rideAlertIo.getStartTime() != null && rideAlertIo.getEndTime() != null) {
                        Calendar startCal = Calendar.getInstance();
                        startCal.setTimeInMillis(rideAlertIo.getStartTime().getValue());

                        Calendar endCal = Calendar.getInstance();
                        endCal.setTimeInMillis(rideAlertIo.getEndTime().getValue());

                        textView.setText(startCal.get(Calendar.HOUR_OF_DAY) + " - " + endCal.get(Calendar.HOUR_OF_DAY));
                    }
                    textView.setText(rideAlertIo.getVehicleType());

                    flowLayout.addView(view);
                } else if (rideAlertIo.getStartTime() != null) {

                    View view = View.inflate(context, R.layout.item_filter_view, null);
                    TextView textView = (TextView) view.findViewById(R.id.filter_name_text);

                    Calendar startCal = Calendar.getInstance();
                    startCal.setTimeInMillis(rideAlertIo.getStartTime().getValue());

                    textView.setText("> " + startCal.get(Calendar.HOUR_OF_DAY));

                    flowLayout.addView(view);
                } else if (rideAlertIo.getEndTime() != null) {
                    View view = View.inflate(context, R.layout.item_filter_view, null);
                    TextView textView = (TextView) view.findViewById(R.id.filter_name_text);

                    Calendar endCal = Calendar.getInstance();
                    endCal.setTimeInMillis(rideAlertIo.getEndTime().getValue());

                    textView.setText("< " + endCal.get(Calendar.HOUR_OF_DAY));

                    flowLayout.addView(view);
                }
            } else {
                filterLayout.setVisibility(View.GONE);
            }

            menuView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Menu menu = new MenuBuilder(context);
                    menu.add("Delete");

                    PopupMenu popupMenu = new PopupMenu(context, menuView);
                    popupMenu.getMenu().add(0, 1, 0, "Delete");

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getItemId() == 1) {
                                removeListItem(rideAlertIo);
                                myAlertsPresenter.deleteAlert(rideAlertIo.getId(), MyAlertsAdapter.this);
                            }
                            return false;
                        }
                    });

                    popupMenu.show();
                }
            });
        }
    }
}
