package com.spacesofting.weshare.mvp.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.spacesofting.weshare.R;
import com.spacesofting.weshare.api.model.place.Place;
import com.spacesofting.weshare.views.CustomTypefaceSpan;
import com.spacesofting.weshare.yandex.YandexMapsAddress;

import java.util.ArrayList;
import java.util.List;

public class AddressSearchAdapter<T> extends SelectableAdapter<AddressSearchAdapter<T>.ViewHolder> {

    private final List<T> mValues;
    private final OnItemClickListener mListener;

    public AddressSearchAdapter(List<T> items, Object activity) {
        mValues = items;
        mListener = (OnItemClickListener) activity;
    }

    public AddressSearchAdapter(Object activity) {

        this.mListener = (OnItemClickListener) activity;
        mValues = new ArrayList<>(  );
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_address_place, parent, false);
        return new ViewHolder(view, parent.getContext());
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.place = mValues.get(position);

        if (holder.place instanceof YandexMapsAddress) {
            String displayName = ((YandexMapsAddress) mValues.get(position)).getDisplayName();
            if (!TextUtils.isEmpty(displayName)) {
                setText(holder.context, holder.displayName, displayName, Color.BLACK);
                holder.displayName.setVisibility(View.VISIBLE);
            }
            else {
                holder.displayName.setVisibility(View.INVISIBLE);
            }

            String value = ((YandexMapsAddress)mValues.get(position)).getValue();
            if (!TextUtils.isEmpty(value)) {
                setText(holder.context, holder.value, value, Color.GRAY);
                holder.value.setVisibility(View.VISIBLE);
            }
            else {
                holder.value.setVisibility(View.INVISIBLE);
            }
        }
        else if (holder.place instanceof Place) {
            String address = ((Place) mValues.get(position)).getAddress();
            if (!TextUtils.isEmpty(address)) {
                setText(holder.context, holder.displayName, address, Color.BLACK);
                holder.displayName.setVisibility(View.VISIBLE);
            }
            else {
                holder.displayName.setVisibility(View.INVISIBLE);
            }

            String fullAddress = ((Place) mValues.get(position)).getFullAddress();
            if (!TextUtils.isEmpty(fullAddress)) {
                setText(holder.context, holder.value, fullAddress, Color.GRAY);
                holder.value.setVisibility(View.VISIBLE);
            }
            else {
                holder.value.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void setText(Context context, TextView textView, String text, int color) {
        Typeface RobotoCondensedLight = Typeface.createFromAsset(context.getAssets(), "fonts/RobotoCondensed-Light.ttf");

        Spannable spannable = new SpannableString(text);

        spannable.setSpan(new CustomTypefaceSpan("RobotoCondensed", RobotoCondensedLight), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new ForegroundColorSpan(color), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(spannable);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void refill(List<T> places) {
        this.mValues.clear();

        this.mValues.addAll(places);

        notifyDataSetChanged();
    }

    public T getItem(int position) {
        return this.mValues.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener{
        public final TextView displayName;
        public final TextView value;
        public T place;
        public Context context;

        public ViewHolder(View view, Context context) {
            super(view);
            displayName = (TextView) view.findViewById(R.id.id);
            value = (TextView) view.findViewById(R.id.content);
            this.context = context;

            this.itemView.setOnClickListener(this);
            this.itemView.setOnLongClickListener(this);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + value.getText() + "'";
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onListItemClick(getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (mListener != null) {
                mListener.onListItemLongClick(getAdapterPosition());
            }
            return true;
        }
    }
}
