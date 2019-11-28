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
        public final static Property Type = new Property(2, int.class, "type", false, "TYPE");
        public final static Property Duration = new Property(3, int.class, "duration", false, "DURATION");
        public final static Property Status = new Property(4, int.class, "status", false, "STATUS");
        public final static Property Speaker = new Property(5, String.class, "speaker", false, "SPEAKER");
        public final static Property UrlPath = new Property(6, String.class, "urlPath", false, "URL_PATH");
        public final static Property LocalFilePath = new Property(7, String.class, "localFilePath", false, "LOCAL_FILE_PATH");
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
                "\"TYPE\" INTEGER NOT NULL ," + // 2: type
                "\"DURATION\" INTEGER NOT NULL ," + // 3: duration
                "\"STATUS\" INTEGER NOT NULL ," + // 4: status
                "\"SPEAKER\" TEXT," + // 5: speaker
                "\"URL_PATH\" TEXT," + // 6: urlPath
                "\"LOCAL_FILE_PATH\" TEXT);"); // 7: localFilePath
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
        stmt.bindLong(3, entity.getType());
        stmt.bindLong(4, entity.getDuration());
        stmt.bindLong(5, entity.getStatus());
 
        String speaker = entity.getSpeaker();
        if (speaker != null) {
            stmt.bindString(6, speaker);
        }
 
        String urlPath = entity.getUrlPath();
        if (urlPath != null) {
            stmt.bindString(7, urlPath);
        }
 
        String localFilePath = entity.getLocalFilePath();
        if (localFilePath != null) {
            stmt.bindString(8, localFilePath);
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
        stmt.bindLong(3, entity.getType());
        stmt.bindLong(4, entity.getDuration());
        stmt.bindLong(5, entity.getStatus());
 
        String speaker = entity.getSpeaker();
        if (speaker != null) {
            stmt.bindString(6, speaker);
        }
 
        String urlPath = entity.getUrlPath();
        if (urlPath != null) {
            stmt.bindString(7, urlPath);
        }
 
        String localFilePath = entity.getLocalFilePath();
        if (localFilePath != null) {
            stmt.bindString(8, localFilePath);
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
            cursor.getInt(offset + 2), // type
            cursor.getInt(offset + 3), // duration
            cursor.getInt(offset + 4), // status
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // speaker
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // urlPath
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7) // localFilePath
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Resource entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setType(cursor.getInt(offset + 2));
        entity.setDuration(cursor.getInt(offset + 3));
        entity.setStatus(cursor.getInt(offset + 4));
        entity.setSpeaker(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setUrlPath(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setLocalFilePath(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
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
