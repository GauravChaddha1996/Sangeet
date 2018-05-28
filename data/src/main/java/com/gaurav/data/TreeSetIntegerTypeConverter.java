package com.gaurav.data;

import android.arch.persistence.room.TypeConverter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.TreeSet;

public class TreeSetIntegerTypeConverter {

    @TypeConverter
    public static String toString(TreeSet<Integer> songIds) {
        JSONArray jsonArray = new JSONArray();
        for (Integer integer : songIds) {
            jsonArray.put(integer);
        }
        return jsonArray.toString();
    }

    @TypeConverter
    public static TreeSet<Integer> toTreeSet(String songIdsString) {
        TreeSet<Integer> treeSet = new TreeSet<>();
        try {
            JSONArray jsonArray = new JSONArray(songIdsString);
            for (int i = 0; i < jsonArray.length(); i++) {
                treeSet.add(jsonArray.getInt(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return treeSet;

    }
}
