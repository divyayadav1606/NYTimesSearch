package com.dyadav.nytimessearch.modal;

import android.os.Parcel;
import android.os.Parcelable;

public class Filter implements Parcelable {
    private String begin_date;
    private String sort_order;
    private String news_desk;

    public Filter (){}

    protected Filter(Parcel in) {
        this.begin_date = in.readString();
        this.sort_order = in.readString();
        this.news_desk = in.readString();
    }

    public static final Creator<Filter> CREATOR = new Creator<Filter>() {
        @Override
        public Filter createFromParcel(Parcel in) {
            return new Filter(in);
        }

        @Override
        public Filter[] newArray(int size) {
            return new Filter[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.begin_date);
        parcel.writeString(this.sort_order);
        parcel.writeString(this.news_desk);
    }


    public String getBegin_date() {
        return begin_date;
    }

    public void setBegin_date(String begin_date) {
        this.begin_date = begin_date;
    }

    public String getSort_order() {
        return sort_order;
    }

    public void setSort_order(String sort_order) {
        this.sort_order = sort_order;
    }

    public String getNews_desk() {
        return news_desk;
    }

    public void setNews_desk(String news_desk) {
        this.news_desk = news_desk;
    }

    public static Creator<Filter> getCREATOR() {
        return CREATOR;
    }
}
