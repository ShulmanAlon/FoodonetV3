package com.foodonet.foodonet.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.foodonet.foodonet.R;
import com.foodonet.foodonet.model.Group;

import java.util.ArrayList;

public class GroupsDBHandler {
    private Context context;

    public GroupsDBHandler(Context context) {
        this.context = context;
    }

    /** get all groups */
    public ArrayList<Group> getAllGroups(){
        Cursor c = context.getContentResolver().query(FoodonetDBProvider.GroupsDB.CONTENT_URI,null,null,null,null);
        ArrayList<Group> groups = new ArrayList<>();
        String groupName;
        long userID, groupID;
        while(c!=null && c.moveToNext()){
            groupID = c.getLong(c.getColumnIndex(FoodonetDBProvider.GroupsDB.GROUP_ID_COLUMN));
            groupName = c.getString(c.getColumnIndex(FoodonetDBProvider.GroupsDB.GROUP_NAME_COLUMN));
            userID = c.getLong(c.getColumnIndex(FoodonetDBProvider.GroupsDB.ADMIN_ID_COLUMN));

            groups.add(new Group(groupName,userID,groupID));
        }
        if(c!=null){
            c.close();
        }
        return groups;
    }

    /** get all groups including 0 - public */
    public ArrayList<Group> getAllGroupsWithPublic(){
        Cursor c = context.getContentResolver().query(FoodonetDBProvider.GroupsDB.CONTENT_URI,null,null,null,null);
        ArrayList<Group> groups = new ArrayList<>();
        groups.add(new Group(context.getResources().getString(R.string.audience_public),-1,0));
        String groupName;
        long userID, groupID;
        while(c!=null && c.moveToNext()){
            groupID = c.getLong(c.getColumnIndex(FoodonetDBProvider.GroupsDB.GROUP_ID_COLUMN));
            groupName = c.getString(c.getColumnIndex(FoodonetDBProvider.GroupsDB.GROUP_NAME_COLUMN));
            userID = c.getLong(c.getColumnIndex(FoodonetDBProvider.GroupsDB.ADMIN_ID_COLUMN));

            groups.add(new Group(groupName,userID,groupID));
        }
        if(c!=null){
            c.close();
        }
        return groups;
    }

    /**
     * get the specified group object
     * @param groupID the group id to get
     * @return the Group object of the specified group ID
     */
    public Group getGroup(long groupID){
        if(groupID == 0){
            return new Group(context.getString(R.string.audience_public),-1,groupID);
        }
        String where = String.format("%1$s = ?",FoodonetDBProvider.GroupsDB.GROUP_ID_COLUMN);
        String[] whereArgs = {String.valueOf(groupID)};
        Cursor c = context.getContentResolver().query(FoodonetDBProvider.GroupsDB.CONTENT_URI,null,where,whereArgs,null);
        if(c!=null && c.moveToNext()){
            String groupName = c.getString(c.getColumnIndex(FoodonetDBProvider.GroupsDB.GROUP_NAME_COLUMN));
            long userID = c.getLong(c.getColumnIndex(FoodonetDBProvider.GroupsDB.ADMIN_ID_COLUMN));
            return new Group(groupName,userID,groupID);
        }
        if(c!=null){
            c.close();
        }
        return null;
    }

    /**
     * returns a group name
     * @param groupID the group ID to check
     * @return String the group name
     */
    public String getGroupName(long groupID) {
        String groupName = null;
        String[] projection = {FoodonetDBProvider.GroupsDB.GROUP_NAME_COLUMN};
        String where = String.format("%1$s =?",
                FoodonetDBProvider.GroupsDB.GROUP_ID_COLUMN);
        String[] whereArgs = {String.valueOf(groupID)};
        Cursor c = context.getContentResolver().query(FoodonetDBProvider.GroupsDB.CONTENT_URI,projection,where,whereArgs,null);
        if(c!=null && c.moveToNext()){
            groupName = c.getString(c.getColumnIndex(FoodonetDBProvider.GroupsDB.GROUP_NAME_COLUMN));
        }
        if(c!=null){
            c.close();
        }
        return groupName;
    }

    /**
     * gets all groups' IDs in the db
     * @return ArrayList of the group IDs
     */
    public ArrayList<Long> getGroupsIDs(){
        Cursor c = context.getContentResolver().query(FoodonetDBProvider.GroupsDB.CONTENT_URI,null,null,null,null);
        ArrayList<Long> groupsIDs = new ArrayList<>();
        long groupID;
        while(c!=null && c.moveToNext()){
            groupID = c.getLong(c.getColumnIndex(FoodonetDBProvider.GroupsDB.GROUP_ID_COLUMN));

            groupsIDs.add(groupID);
        }
        if(c!=null){
            c.close();
        }
        return groupsIDs;
    }

    /**
     * inserts a new Group to the db
     * @param group the Group object to insert
     */
    public void insertGroup(Group group) {
        ContentResolver resolver = context.getContentResolver();

        ContentValues values = new ContentValues();
        values.put(FoodonetDBProvider.GroupsDB.GROUP_ID_COLUMN,group.getGroupID());
        values.put(FoodonetDBProvider.GroupsDB.GROUP_NAME_COLUMN,group.getGroupName());
        values.put(FoodonetDBProvider.GroupsDB.ADMIN_ID_COLUMN,group.getUserID());

        resolver.insert(FoodonetDBProvider.GroupsDB.CONTENT_URI,values);
    }

    /**
     * deletes current groups in db and inserts new ones
     * @param groups the current new groups to insert after deleting the old ones
     */
    public void replaceAllGroups(ArrayList<Group> groups){
        // first, delete all current groups from db
        deleteAllGroups();

        ContentResolver resolver = context.getContentResolver();
        ContentValues values;
        Group group;
        for (int i = 0; i < groups.size(); i++) {
            values = new ContentValues();
            group = groups.get(i);
            values.put(FoodonetDBProvider.GroupsDB.GROUP_ID_COLUMN,group.getGroupID());
            values.put(FoodonetDBProvider.GroupsDB.GROUP_NAME_COLUMN,group.getGroupName());
            values.put(FoodonetDBProvider.GroupsDB.ADMIN_ID_COLUMN,group.getUserID());

            resolver.insert(FoodonetDBProvider.GroupsDB.CONTENT_URI,values);
        }
    }

    /** deletes all groups from the db */
    public void deleteAllGroups(){
        context.getContentResolver().delete(FoodonetDBProvider.GroupsDB.CONTENT_URI,null,null);
    }

    /**
     * delete a specific group by group ID
     * @param groupID the group ID of the group to delete
     */
    public void deleteGroup(long groupID) {
        String where = String.format("%1$s = ?",FoodonetDBProvider.GroupsDB.GROUP_ID_COLUMN);
        String[] whereArgs = {String.valueOf(groupID)};
        context.getContentResolver().delete(FoodonetDBProvider.GroupsDB.CONTENT_URI,where,whereArgs);
    }
}
