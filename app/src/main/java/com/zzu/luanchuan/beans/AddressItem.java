package com.zzu.luanchuan.beans;

import com.amap.api.services.core.LatLonPoint;
import com.zzu.luanchuan.constant.Constants;

public class AddressItem {

    private String title;
    private String content;
    private LatLonPoint point;
    private boolean is_selected;

    public AddressItem(String title, String content, LatLonPoint point, boolean is_selected) {
        this.title = title;
        this.content = content;
        this.point = point;
        this.point.setLatitude(this.point.getLatitude()+ Constants.lat_scale);
        this.point.setLongitude(this.point.getLongitude()+ Constants.lon_scale);

        this.is_selected = is_selected;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LatLonPoint getPoint() {
        return point;
    }

    public void setPoint(LatLonPoint point) {
        this.point = point;
    }

    public boolean isIs_selected() {
        return is_selected;
    }

    public void setIs_selected(boolean is_selected) {
        this.is_selected = is_selected;
    }

    @Override
    public String toString() {
        return "AddressItem{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", point=" + point +
                ", is_selected=" + is_selected +
                '}';
    }


}
