package com.uart.entitylib.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.uart.entitylib.entity.Resource;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "RESOURCE".
*/
public class ResourceDao extends AbstractDao<Resource, Long> {

    public static final String TABLENAME = "RESOURCE";

    /**
     * Properties of entity Resource.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Extension = new Property(2, String.class, "extension", false, "EXTENSION");
        public final static Property Type = new Property(3, Integer.class, "type", false, "TYPE");
        public final static Property Status = new Property(4, Integer.class, "status", false, "STATUS");
        public final static Property Duration = new Property(5, Integer.class, "duration", false, "DURATION");
        public final static Property DurationStr = new Property(6, String.class, "durationStr", false, "DURATION_STR");
        public final static Property Speaker = new Property(7, String.class, "speaker", false, "SPEAKER");
        public final static Property UrlPath = new Property(8, String.class, "urlPath", false, "URL_PATH");
        public final static Property LocalFilePath = new Property(9, String.class, "localFilePath", false, "LOCAL_FILE_PATH");
    }


    public ResourceDao(DaoConfig config) {
        super(config);
    }
    
    public ResourceDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"RESOURCE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"NAME\" TEXT," + // 1: name
                "\"EXTENSION\" TEXT," + // 2: extension
                "\"TYPE\" INTEGER," + // 3: type
                "\"STATUS\" INTEGER," + // 4: status
                "\"DURATION\" INTEGER," + // 5: duration
                "\"DURATION_STR\" TEXT," + // 6: durationStr
                "\"SPEAKER\" TEXT," + // 7: speaker
                "\"URL_PATH\" TEXT," + // 8: urlPath
                "\"LOCAL_FILE_PATH\" TEXT);"); // 9: localFilePath
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"RESOURCE\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Resource entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String extension = entity.getExtension();
        if (extension != null) {
            stmt.bindString(3, extension);
        }
 
        Integer type = entity.getType();
        if (type != null) {
            stmt.bindLong(4, type);
        }
 
        Integer status = entity.getStatus();
        if (status != null) {
            stmt.bindLong(5, status);
        }
 
        Integer duration = entity.getDuration();
        if (duration != null) {
            stmt.bindLong(6, duration);
        }
 
        String durationStr = entity.getDurationStr();
        if (durationStr != null) {
            stmt.bindString(7, durationStr);
        }
 
        String speaker = entity.getSpeaker();
        if (speaker != null) {
            stmt.bindString(8, speaker);
        }
 
        String urlPath = entity.getUrlPath();
        if (urlPath != null) {
            stmt.bindString(9, urlPath);
        }
 
        String localFilePath = entity.getLocalFilePath();
        if (localFilePath != null) {
            stmt.bindString(10, localFilePath);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Resource entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String extension = entity.getExtension();
        if (extension != null) {
            stmt.bindString(3, extension);
        }
 
        Integer type = entity.getType();
        if (type != null) {
            stmt.bindLong(4, type);
        }
 
        Integer status = entity.getStatus();
        if (status != null) {
            stmt.bindLong(5, status);
        }
 
        Integer duration = entity.getDuration();
        if (duration != null) {
            stmt.bindLong(6, duration);
        }
 
        String durationStr = entity.getDurationStr();
        if (durationStr != null) {
            stmt.bindString(7, durationStr);
        }
 
        String speaker = entity.getSpeaker();
        if (speaker != null) {
            stmt.bindString(8, speaker);
        }
 
        String urlPath = entity.getUrlPath();
        if (urlPath != null) {
            stmt.bindString(9, urlPath);
        }
 
        String localFilePath = entity.getLocalFilePath();
        if (localFilePath != null) {
            stmt.bindString(10, localFilePath);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Resource readEntity(Cursor cursor, int offset) {
        Resource entity = new Resource( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // extension
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3), // type
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // status
            cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5), // duration
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // durationStr
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // speaker
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // urlPath
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9) // localFilePath
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Resource entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setExtension(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setType(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
        entity.setStatus(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setDuration(cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5));
        entity.setDurationStr(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setSpeaker(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setUrlPath(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setLocalFilePath(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Resource entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Resource entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Resource entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
