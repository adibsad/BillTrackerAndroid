package com.sadadib.billtracker.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BillData implements Parcelable {
    private static final String NOT_FOUND = "";
    private static final String SPACE = " ";

    private String billId;
    private String billType;
    private String displayTitle;
    private Date statusAt;
    private Date introducedAt;
    private String party;
    private Map<String, String> sponsorInfo;

    private String sponsorName;
    private String sponsorState;
    private String sponsorDistrict;

    BillData(Map<String, Object> data) {
        this.billId = toString(data.get("bill_id"));
        this.billType = toString(data.get("bill_type"));
        this.displayTitle = toString(data.get("display_title"));
        this.introducedAt = timestampToDate(data.get("introduced_at"));
        this.statusAt = timestampToDate(data.get("status_at"));
        this.party = toString(data.get("party"));

        //Additional space in sponsor_info field by accident
        this.sponsorInfo = toMap(data.get("sponsor_info" + SPACE));

        sponsorName = sponsorInfo.get("name");
        sponsorDistrict = sponsorInfo.get("district");
        sponsorState = sponsorInfo.get("state");
    }

    protected BillData(Parcel in) {
        billId = in.readString();
        billType = in.readString();
        displayTitle = in.readString();
        statusAt = new Date(in.readLong());
        introducedAt = new Date(in.readLong());
        party = in.readString();
        sponsorName = in.readString();
        sponsorDistrict = in.readString();
        sponsorState = in.readString();
    }

    public static final Creator<BillData> CREATOR = new Creator<BillData>() {
        @Override
        public BillData createFromParcel(Parcel in) {
            return new BillData(in);
        }

        @Override
        public BillData[] newArray(int size) {
            return new BillData[size];
        }
    };

    public Date getIntroducedAt() {
        return introducedAt;
    }

    public String getBillId() {
        return billId;
    }

    public String getBillType() {
        return billType;
    }

    public String getDisplayTitle() {
        return displayTitle;
    }

    public Date getStatusAt() {
        return statusAt;
    }

    public String getParty() {
        return party;
    }

    public Map<String, String> getSponsorInfo() {
        return sponsorInfo;
    }

    @Override
    public String toString() {
        return "BillData{" +
                "billId='" + billId + '\'' +
                ", billType='" + billType + '\'' +
                ", displayTitle='" + displayTitle + '\'' +
                ", statusAt=" + statusAt +
                ", party='" + party + '\'' +
                ", sponsorInfo=" + sponsorInfo +
                '}';
    }

    private Date timestampToDate(Object time) {
        if (time instanceof Timestamp) {
            return ((Timestamp) time).toDate();
        }
        return null;
    }

    private String toString(Object string) {
        if (string instanceof String) {
            return (String) string;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> toMap(Object map) {
        if (map instanceof Map) {
            return (Map<String, String>) map;
        }
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(billId);
        parcel.writeString(billType);
        parcel.writeString(displayTitle);
        parcel.writeLong(statusAt.getTime());
        parcel.writeLong(introducedAt.getTime());
        parcel.writeString(party);

        parcel.writeString(sponsorName);
        parcel.writeString(sponsorDistrict);
        parcel.writeString(sponsorState);
    }
}
