package wanthavers.mad.cs.fau.de.wanthavers_android.data.source.ormlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.Haver;
import de.fau.cs.mad.wanthavers.common.Media;
import de.fau.cs.mad.wanthavers.common.Rating;
import de.fau.cs.mad.wanthavers.common.User;

/**
 * Created by Nico on 23.05.2016.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "wanthavers.db";

    private static final int DATABASE_VERSION = 1;

    private static DatabaseHelper INSTANCE;

    private Dao<User, Long> userDao = null;
    private Dao<Desire, Long> desireDao = null;
    private Dao<Haver, Long> haverDao = null;
    private Dao<Media, Long> mediaDao = null;
    private Dao<Rating, Long> ratingDao = null;

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION /*R.raw.ormlite_config*/);
    }

    public static DatabaseHelper getInstance(Context context) {
        if(INSTANCE == null) {
            INSTANCE = new DatabaseHelper(context);
        }
        return INSTANCE;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        System.out.println("DB onCreate");
        try {
            Log.i(DatabaseHelper.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, User.class);
            TableUtils.createTable(connectionSource, Desire.class);
            TableUtils.createTable(connectionSource, Haver.class);
            TableUtils.createTable(connectionSource, Media.class);
            TableUtils.createTable(connectionSource, Rating.class);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        //TODO: edit this method if we change something important in the database
    }

    public Dao<User, Long> getUserDao() throws SQLException {
        if(userDao == null) {
            userDao = getDao(User.class);
        }
        return userDao;
    }

    public Dao<Desire, Long> getDesireDao() throws SQLException {
        if(desireDao == null) {
            desireDao = getDao(Desire.class);
        }
        return desireDao;
    }

    public Dao<Haver, Long> getHaverDao() throws SQLException {
        if(haverDao == null) {
            haverDao = getDao(Haver.class);
        }
        return haverDao;
    }

    public Dao<Media, Long> getMediaDao() throws SQLException {
        if(mediaDao == null) {
            mediaDao = getDao(Media.class);
        }
        return mediaDao;
    }

    public Dao<Rating, Long> getRatingDao() throws SQLException {
        if(ratingDao == null) {
            ratingDao = getDao(Rating.class);
        }
        return ratingDao;
    }

    @Override
    public void close() {
        super.close();
        userDao = null;
        desireDao = null;
        haverDao = null;
        mediaDao = null;
        ratingDao = null;
    }

}
