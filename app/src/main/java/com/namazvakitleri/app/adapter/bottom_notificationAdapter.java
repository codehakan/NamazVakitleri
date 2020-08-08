package com.namazvakitleri.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.namazvakitleri.app.R;
import com.namazvakitleri.app.entity.bottom_bildirim;
import com.namazvakitleri.app.entity.namazvakitleri;

import java.util.List;

public class bottom_notificationAdapter extends BaseAdapter {
    private LayoutInflater userInflater;
    private List<bottom_bildirim> bottom_bildirims;

    public bottom_notificationAdapter(Activity activity, List<bottom_bildirim> bottom_Bildirims) {
        userInflater = (LayoutInflater) activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        this.bottom_bildirims = bottom_Bildirims;
    }

    @Override
    public int getCount() {
        return bottom_bildirims.size();
    }

    @Override
    public Object getItem(int i) {
        return bottom_bildirims.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final View lineView;
        lineView = userInflater.inflate(R.layout.single_bildirimler, null);

        Switch switch_imsak = lineView.findViewById(R.id.switch_imsak);
        Switch switch_gunes = lineView.findViewById(R.id.switch_gunes);
        Switch switch_ogle = lineView.findViewById(R.id.switch_ogle);
        Switch switch_ikindi = lineView.findViewById(R.id.switch_ikindi);
        Switch switch_aksam = lineView.findViewById(R.id.switch_aksam);
        Switch switch_yatsi = lineView.findViewById(R.id.switch_yatsi);


        /*final bottom_bildirim temp = bottom_bildirims.get(i);
        switch_imsak.setChecked(temp.);
        textview_gunes_vakti.setText(vakit.getGunes());
        textview_ogle_vakti.setText(vakit.getOgle());
        textview_ikindi_vakti.setText(vakit.getIkindi());
        textview_aksam_vakti.setText(vakit.getAksam());
        textview_yatsi_vakti.setText(vakit.getYatsi());*/
        return lineView;
    }

}
