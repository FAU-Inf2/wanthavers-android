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

import de.fau.cs.mad.wanthavers.common.DesireFilter;

/**
 * Created by Nico on 09.06.2016.
 */
public class FilterDatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "wanthavers_filter.db";

    private static final int DATABASE_VERSION = 1;

    private static FilterDatabaseHelper INSTANCE;

    private FilterDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static FilterDatabaseHelper getInstance(@NonNull Context context) {
        if(INSTANCE == null) {
            INSTANCE = new FilterDatabaseHelper(context);
        }
        return INSTANCE;
    }

    private RuntimeExceptionDao<DesireFilter, Integer> desireFilterRuntimeDao = null;

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            Log.i(FilterDatabaseHelper.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, DesireFilter.class);
        } catch (SQLException e) {
            Log.e(FilterDatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

    @Override
    public void close() {
        super.close();
        desireFilterRuntimeDao = null;
    }

    public RuntimeExceptionDao<DesireFilter, Integer> getDesireFilterRuntimeDao() {
        if(desireFilterRuntimeDao == null) {
            desireFilterRuntimeDao = getRuntimeExceptionDao(DesireFilter.class);
        }
        return desireFilterRuntimeDao;
    }

    public DesireFilter createOrUpdate(DesireFilter desireFilter) {
        if(desireFilter.getId() == null) {
            Integer id = desireFilterRuntimeDao.create(desireFilter);
            desireFilter.setId(id);
        } else {
            desireFilterRuntimeDao.update(desireFilter);
        }

        return desireFilter;
    }

    public int delete(DesireFilter desireFilter) {
        return desireFilterRuntimeDao.delete(desireFilter);
    }

    public int deleteById(Integer id) {
        return desireFilterRuntimeDao.deleteById(id);
    }

    public DesireFilter getById(Integer id) {
        return desireFilterRuntimeDao.queryForId(id);
    }

    public List<DesireFilter> getAll() {
        return desireFilterRuntimeDao.queryForAll();
    }

}
