package com.uart.entitylib.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.uart.entitylib.entity.SleepData;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "SLEEP_DATA".
*/
public class SleepDataDao extends AbstractDao<SleepData, Long> {

    public static final String TABLENAME = "SLEEP_DATA";

    /**
     * Properties of entity SleepData.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property UsageRecordId = new Property(1, Long.class, "usageRecordId", false, "USAGE_RECORD_ID");
        public final static Property Type = new Property(2, Integer.class, "type", false, "TYPE");
        public final static Property Vigor = new Property(3, Integer.class, "vigor", false, "VIGOR");
        public final static Property StartTime = new Property(4, Long.class, "startTime", false, "START_TIME");
        public final static Property EndTime = new Property(5, Long.class, "endTime", false, "END_TIME");
    }


    public SleepDataDao(DaoConfig config) {
        super(config);
    }
    
    public SleepDataDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SLEEP_DATA\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"USAGE_RECORD_ID\" INTEGER," + // 1: usageRecordId
                "\"TYPE\" INTEGER," + // 2: type
                "\"VIGOR\" INTEGER," + // 3: vigor
                "\"START_TIME\" INTEGER," + // 4: startTime
                "\"END_TIME\" INTEGER);"); // 5: endTime
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SLEEP_DATA\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, SleepData entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long usageRecordId = entity.getUsageRecordId();
        if (usageRecordId != null) {
            stmt.bindLong(2, usageRecordId);
        }
 
        Integer type = entity.getType();
        if (type != null) {
            stmt.bindLong(3, type);
        }
 
        Integer vigor = entity.getVigor();
        if (vigor != null) {
            stmt.bindLong(4, vigor);
        }
 
        Long startTime = entity.getStartTime();
        if (startTime != null) {
            stmt.bindLong(5, startTime);
        }
 
        Long endTime = entity.getEndTime();
        if (endTime != null) {
            stmt.bindLong(6, endTime);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, SleepData entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long usageRecordId = entity.getUsageRecordId();
        if (usageRecordId != null) {
            stmt.bindLong(2, usageRecordId);
        }
 
        Integer type = entity.getType();
        if (type != null) {
            stmt.bindLong(3, type);
        }
 
        Integer vigor = entity.getVigor();
        if (vigor != null) {
            stmt.bindLong(4, vigor);
        }
 
        Long startTime = entity.getStartTime();
        if (startTime != null) {
            stmt.bindLong(5, startTime);
        }
 
        Long endTime = entity.getEndTime();
        if (endTime != null) {
            stmt.bindLong(6, endTime);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public SleepData readEntity(Cursor cursor, int offset) {
        SleepData entity = new SleepData( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // usageRecordId
            cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2), // type
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3), // vigor
            cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4), // startTime
            cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5) // endTime
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, SleepData entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUsageRecordId(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setType(cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2));
        entity.setVigor(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
        entity.setStartTime(cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4));
        entity.setEndTime(cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(SleepData entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(SleepData entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(SleepData entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
