package com.mycodingdesk.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Cesar Egoavil on 6/21/2018.
 */
public class MovieTrailer implements Parcelable {

    private String id;
    private String type;
    private String size;
    private String site;
    private String name;
    private String key;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(type);
        dest.writeString(name);
        dest.writeString(size);
        dest.writeString(site);
        dest.writeString(key);
    }

    public MovieTrailer(Parcel in){
        id = in.readString();
        type = in.readString();
        name = in.readString();
        size = in.readString();
        site = in.readString();
        key = in.readString();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public MovieTrailer createFromParcel(Parcel in) {
            return new MovieTrailer(in);
        }

        public MovieTrailer[] newArray(int size) {
            return new MovieTrailer[size];
        }
    };
}
