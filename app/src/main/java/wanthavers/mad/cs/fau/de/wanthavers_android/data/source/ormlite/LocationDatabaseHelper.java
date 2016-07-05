package wanthavers.mad.cs.fau.de.wanthavers_android.data.source.ormlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

import de.fau.cs.mad.wanthavers.common.Location;

/**
 * Created by Nico on 09.06.2016.
 */
public class LocationDatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "wanthavers_location.db";

    private static final int DATABASE_VERSION = 1;

    private static LocationDatabaseHelper INSTANCE;

    private RuntimeExceptionDao<Location, Integer> desireFilterRuntimeDao;

    private LocationDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        desireFilterRuntimeDao = getRuntimeExceptionDao(Location.class);
    }

    public static LocationDatabaseHelper getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new LocationDatabaseHelper(context);
        }
        return INSTANCE;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            Log.i(LocationDatabaseHelper.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, Location.class);
        } catch (SQLException e) {
            Log.e(LocationDatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

    public Location createOrUpdate(Location location) {
        desireFilterRuntimeDao.createOrUpdate(location);

        return location;
    }

    public int delete(Location location) {
        return desireFilterRuntimeDao.delete(location);
    }

    public int deleteById(Integer id) {
        return desireFilterRuntimeDao.deleteById(id);
    }

    public Location getById(Integer id) {
        return desireFilterRuntimeDao.queryForId(id);
    }

    public List<Location> getAll() {
        return desireFilterRuntimeDao.queryForAll();
    }

}
