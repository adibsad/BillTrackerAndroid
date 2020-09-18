package com.sadadib.billtracker.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class BillFormat {

    public static String formatDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
        return format.format(date);
    }

    public static String formatBillId(String id) {
        String regex = "(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)";
        String[] typeAndId = id.split(regex);

        if (typeAndId.length != 2) {
            return "";
        }

        String type = typeAndId[0];
        String numId = typeAndId[1];
        switch (type) {
            case "hr":
                return "H.R. " + numId;
            case "s":
                return "S. " + numId;
            case "hres":
                return "H.Res. " + numId;
            case "hjres":
                return "H.J.Res. " + numId;
            case "sjres":
                return "S.J.Res. " + numId;
            case "hconres":
                return "H.Con.Res. " + numId;
            case "sres":
                return "S.Res. " + numId;
            case "sconres":
                return "S.Con.Res. " + numId;
            default:
                return type + numId;
        }

    }

    public static String formatSponsor(Map<String, String> sponsor, String party) {
        String name = sponsor.get("name");
        String state = sponsor.get("state");

        if (party != null && party.length() > 1) {
            party = String.valueOf(party.charAt(0));
        }

        if (state != null)
            return String.format("Sponsor: %1$s [%2$s - %3$S]", name, party, state);
        else
            return String.format("Sponsor: %1$s [%2$s]", name, party);
    }

}
