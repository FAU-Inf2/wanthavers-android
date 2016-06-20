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

import de.fau.cs.mad.wanthavers.common.AppChatLastSeen;

public class AppChatLastSeenDatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "wanthavers_chatlastseen.db";

    private static final int DATABASE_VERSION = 1;

    private static AppChatLastSeenDatabaseHelper INSTANCE;

    private RuntimeExceptionDao<AppChatLastSeen, String> chatLastSeenRuntimeDao;

    private AppChatLastSeenDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        chatLastSeenRuntimeDao = getRuntimeExceptionDao(AppChatLastSeen.class);
    }

    public static AppChatLastSeenDatabaseHelper getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new AppChatLastSeenDatabaseHelper(context);
        }
        return INSTANCE;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            Log.i(AppChatLastSeenDatabaseHelper.class.getName(), "onCreate()");
            TableUtils.createTable(connectionSource, AppChatLastSeen.class);
        } catch (SQLException e) {
            Log.e(AppChatLastSeenDatabaseHelper.class.getName(), "Can't create Database", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

    public AppChatLastSeen createOrUpdate(AppChatLastSeen appChatLastSeen) {
        chatLastSeenRuntimeDao.createOrUpdate(appChatLastSeen);
        return appChatLastSeen;
    }

    public int delete(AppChatLastSeen appChatLastSeen) {
        return chatLastSeenRuntimeDao.delete(appChatLastSeen);
    }

    public int deleteById(String chatId) {
        return chatLastSeenRuntimeDao.deleteById(chatId);
    }

    public AppChatLastSeen getById(String chatId) {
        return chatLastSeenRuntimeDao.queryForId(chatId);
    }
}
