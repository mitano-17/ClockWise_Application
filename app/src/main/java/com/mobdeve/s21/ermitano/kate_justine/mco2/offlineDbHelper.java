package com.mobdeve.s21.ermitano.kate_justine.mco2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class offlineDbHelper extends SQLiteOpenHelper {
   private static final String DATABASE_NAME = "clockwise_database.db";
   private static final int DATABASE_VERSION = 1;

   public static final String TABLE_EVENTS = "events";
   public static final String COLUMN_ID = "eventId";
   public static final String COLUMN_USER_ID = "userId";
   public static final String COLUMN_EVENT_NAME = "eventName";
   public static final String COLUMN_START_DATE = "startDate";
   public static final String COLUMN_START_TIME = "startTime";
   public static final String COLUMN_END_DATE = "endDate";
   public static final String COLUMN_END_TIME = "endTime";
   public static final String COLUMN_NUM_ATTENDEES = "numAttendees";
   public static final String COLUMN_COLOR = "color";
   public static final String COLUMN_RECEIVE_ALERT = "receiveAlert";
   public static final String COLUMN_EVENT_TYPE = "eventType";

   String userId, eventId, eventName, startDate, startTime, endDate, endTime, numAttendees, color, receiveAlert, eventType;
   public offlineDbHelper(Context context){
    super(context, offlineDbHelper.DATABASE_NAME, null, offlineDbHelper.DATABASE_VERSION);

   }

   @Override
    public void onCreate(SQLiteDatabase db){
       String createTable = "CREATE TABLE " + TABLE_EVENTS + " (" +
               COLUMN_ID + " TEXT PRIMARY KEY, " +
               COLUMN_USER_ID + " TEXT, " +
               COLUMN_EVENT_NAME + " TEXT, " +
               COLUMN_START_DATE + " TEXT, " +
               COLUMN_START_TIME + " TEXT, " +
               COLUMN_END_DATE + " TEXT, " +
               COLUMN_END_TIME + " TEXT, " +
               COLUMN_NUM_ATTENDEES + " TEXT, " +
               COLUMN_COLOR + " TEXT, " +
               COLUMN_RECEIVE_ALERT + " TEXT, " +
               COLUMN_EVENT_TYPE + " TEXT)";
       db.execSQL(createTable);
   }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        onCreate(db);
    }

    public void createEvent(Event event){
       SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_ID, event.getEventId());
        values.put(COLUMN_EVENT_NAME, event.getEventName());
        values.put(COLUMN_START_DATE, event.getStartDate());
        values.put(COLUMN_START_TIME, event.getStartTime());
        values.put(COLUMN_END_DATE, event.getEndDate());
        values.put(COLUMN_END_TIME, event.getEndTime());
        values.put(COLUMN_NUM_ATTENDEES, event.getNumAttendees());
        values.put(COLUMN_COLOR, event.getColor());
        values.put(COLUMN_RECEIVE_ALERT, event.getReceiveAlert());
        values.put(COLUMN_EVENT_TYPE, event.getEventType());

        db.insert(TABLE_EVENTS, null, values);
        db.close();
    }

    public ArrayList<Event> getEvents() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(offlineDbHelper.TABLE_EVENTS, null, null, null, null, null, null, null);

        ArrayList<Event> events = new ArrayList<>();
        while(c.moveToNext()) {

            events.add(new Event(
                    c.getString(c.getColumnIndexOrThrow(offlineDbHelper.COLUMN_ID)),
                    c.getString(c.getColumnIndexOrThrow(offlineDbHelper.COLUMN_USER_ID)),
                    c.getString(c.getColumnIndexOrThrow(offlineDbHelper.COLUMN_EVENT_NAME)),
                    c.getString(c.getColumnIndexOrThrow(offlineDbHelper.COLUMN_START_DATE)),
                    c.getString(c.getColumnIndexOrThrow(offlineDbHelper.COLUMN_START_TIME)),
                    c.getString(c.getColumnIndexOrThrow(offlineDbHelper.COLUMN_END_DATE)),
                    c.getString(c.getColumnIndexOrThrow(offlineDbHelper.COLUMN_END_TIME)),
                    c.getString(c.getColumnIndexOrThrow(offlineDbHelper.COLUMN_NUM_ATTENDEES)),
                    c.getString(c.getColumnIndexOrThrow(offlineDbHelper.COLUMN_COLOR)),
                    c.getString(c.getColumnIndexOrThrow(offlineDbHelper.COLUMN_RECEIVE_ALERT)),
                    c.getString(c.getColumnIndexOrThrow(offlineDbHelper.COLUMN_EVENT_TYPE))
            ));

        }
        c.close();
        db.close();
        return events;
    }

    public void updateEvent(Event event){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_ID, event.getEventId());
        values.put(COLUMN_EVENT_NAME, event.getEventName());
        values.put(COLUMN_START_DATE, event.getStartDate());
        values.put(COLUMN_START_TIME, event.getStartTime());
        values.put(COLUMN_END_DATE, event.getEndDate());
        values.put(COLUMN_END_TIME, event.getEndTime());
        values.put(COLUMN_NUM_ATTENDEES, event.getNumAttendees());
        values.put(COLUMN_COLOR, event.getColor());
        values.put(COLUMN_RECEIVE_ALERT, event.getReceiveAlert());
        values.put(COLUMN_EVENT_TYPE, event.getEventType());

        db.update(TABLE_EVENTS, values, COLUMN_ID + "=?", new String[]{event.getEventId()});
        db.close();
    }

    public void deleteEvent(String eventId){
       SQLiteDatabase db = this.getWritableDatabase();
       db.delete(TABLE_EVENTS,COLUMN_ID + "=?", new String[]{eventId});
       db.close();
    }

}
