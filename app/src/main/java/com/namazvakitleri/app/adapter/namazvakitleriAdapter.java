package com.namazvakitleri.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.namazvakitleri.app.R;
import com.namazvakitleri.app.entity.namazvakitleri;

import java.util.List;


public class namazvakitleriAdapter extends BaseAdapter {
    private LayoutInflater userInflater;
    private List<namazvakitleri> namazvakitleriList;

    public namazvakitleriAdapter(Activity activity, List<namazvakitleri> namazvakitleriList) {
        userInflater = (LayoutInflater) activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        this.namazvakitleriList = namazvakitleriList;
    }

    @Override
    public int getCount() {
        return namazvakitleriList.size();
    }

    @Override
    public Object getItem(int i) {
        return namazvakitleriList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final View lineView;
        lineView = userInflater.inflate(R.layout.single_namaz_vakitleri, null);

        TextView textview_imsak_ismi = lineView.findViewById(R.id.textview_imsak_ismi);
        TextView textview_imsak_vakti = lineView.findViewById(R.id.textview_imsak_vakti);

        TextView textview_gunes_ismi = lineView.findViewById(R.id.textview_gunes_ismi);
        TextView textview_gunes_vakti = lineView.findViewById(R.id.textview_gunes_vakti);

        TextView textview_ogle_ismi = lineView.findViewById(R.id.textview_ogle_ismi);
        TextView textview_ogle_vakti = lineView.findViewById(R.id.textview_ogle_vakti);

        TextView textview_ikindi_ismi = lineView.findViewById(R.id.textview_ikindi_ismi);
        TextView textview_ikindi_vakti = lineView.findViewById(R.id.textview_ikindi_vakti);

        TextView textview_aksam_ismi = lineView.findViewById(R.id.textview_aksam_ismi);
        TextView textview_aksam_vakti = lineView.findViewById(R.id.textview_aksam_vakti);

        TextView textview_yatsi_ismi = lineView.findViewById(R.id.textview_yatsi_ismi);
        TextView textview_yatsi_vakti = lineView.findViewById(R.id.textview_yatsi_vakti);


        final namazvakitleri vakit = namazvakitleriList.get(i);
        textview_imsak_vakti.setText(vakit.getImsak());
        textview_gunes_vakti.setText(vakit.getGunes());
        textview_ogle_vakti.setText(vakit.getOgle());
        textview_ikindi_vakti.setText(vakit.getIkindi());
        textview_aksam_vakti.setText(vakit.getAksam());
        textview_yatsi_vakti.setText(vakit.getYatsi());
        return lineView;
    }
}
