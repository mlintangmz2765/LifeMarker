package com.lifemarker.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.LongSparseArray;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.RelationUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.lifemarker.data.local.entity.CategoryEntity;
import com.lifemarker.data.local.entity.MarkerEntity;
import com.lifemarker.data.local.entity.MarkerWithCategory;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class MarkerDao_Impl implements MarkerDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MarkerEntity> __insertionAdapterOfMarkerEntity;

  private final EntityDeletionOrUpdateAdapter<MarkerEntity> __deletionAdapterOfMarkerEntity;

  private final EntityDeletionOrUpdateAdapter<MarkerEntity> __updateAdapterOfMarkerEntity;

  public MarkerDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMarkerEntity = new EntityInsertionAdapter<MarkerEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `markers` (`id`,`categoryId`,`latitude`,`longitude`,`timestamp`,`note`,`photoUri`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MarkerEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getCategoryId());
        statement.bindDouble(3, entity.getLatitude());
        statement.bindDouble(4, entity.getLongitude());
        statement.bindLong(5, entity.getTimestamp());
        if (entity.getNote() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getNote());
        }
        if (entity.getPhotoUri() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getPhotoUri());
        }
      }
    };
    this.__deletionAdapterOfMarkerEntity = new EntityDeletionOrUpdateAdapter<MarkerEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `markers` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MarkerEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfMarkerEntity = new EntityDeletionOrUpdateAdapter<MarkerEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `markers` SET `id` = ?,`categoryId` = ?,`latitude` = ?,`longitude` = ?,`timestamp` = ?,`note` = ?,`photoUri` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MarkerEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getCategoryId());
        statement.bindDouble(3, entity.getLatitude());
        statement.bindDouble(4, entity.getLongitude());
        statement.bindLong(5, entity.getTimestamp());
        if (entity.getNote() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getNote());
        }
        if (entity.getPhotoUri() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getPhotoUri());
        }
        statement.bindLong(8, entity.getId());
      }
    };
  }

  @Override
  public Object insertMarker(final MarkerEntity marker,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfMarkerEntity.insertAndReturnId(marker);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteMarker(final MarkerEntity marker,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfMarkerEntity.handle(marker);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateMarker(final MarkerEntity marker,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfMarkerEntity.handle(marker);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<MarkerWithCategory>> getAllMarkers() {
    final String _sql = "SELECT * FROM markers ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"categories",
        "markers"}, new Callable<List<MarkerWithCategory>>() {
      @Override
      @NonNull
      public List<MarkerWithCategory> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
            final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
            final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
            final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
            final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
            final int _cursorIndexOfPhotoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "photoUri");
            final LongSparseArray<CategoryEntity> _collectionCategory = new LongSparseArray<CategoryEntity>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfCategoryId);
              _collectionCategory.put(_tmpKey, null);
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipcategoriesAscomLifemarkerDataLocalEntityCategoryEntity(_collectionCategory);
            final List<MarkerWithCategory> _result = new ArrayList<MarkerWithCategory>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final MarkerWithCategory _item;
              final MarkerEntity _tmpMarker;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final long _tmpCategoryId;
              _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
              final double _tmpLatitude;
              _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
              final double _tmpLongitude;
              _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
              final long _tmpTimestamp;
              _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
              final String _tmpNote;
              if (_cursor.isNull(_cursorIndexOfNote)) {
                _tmpNote = null;
              } else {
                _tmpNote = _cursor.getString(_cursorIndexOfNote);
              }
              final String _tmpPhotoUri;
              if (_cursor.isNull(_cursorIndexOfPhotoUri)) {
                _tmpPhotoUri = null;
              } else {
                _tmpPhotoUri = _cursor.getString(_cursorIndexOfPhotoUri);
              }
              _tmpMarker = new MarkerEntity(_tmpId,_tmpCategoryId,_tmpLatitude,_tmpLongitude,_tmpTimestamp,_tmpNote,_tmpPhotoUri);
              final CategoryEntity _tmpCategory;
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfCategoryId);
              _tmpCategory = _collectionCategory.get(_tmpKey_1);
              _item = new MarkerWithCategory(_tmpMarker,_tmpCategory);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<MarkerWithCategory>> getMarkersByCategory(final long categoryId) {
    final String _sql = "SELECT * FROM markers WHERE categoryId = ? ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, categoryId);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"categories",
        "markers"}, new Callable<List<MarkerWithCategory>>() {
      @Override
      @NonNull
      public List<MarkerWithCategory> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
            final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
            final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
            final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
            final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
            final int _cursorIndexOfPhotoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "photoUri");
            final LongSparseArray<CategoryEntity> _collectionCategory = new LongSparseArray<CategoryEntity>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfCategoryId);
              _collectionCategory.put(_tmpKey, null);
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipcategoriesAscomLifemarkerDataLocalEntityCategoryEntity(_collectionCategory);
            final List<MarkerWithCategory> _result = new ArrayList<MarkerWithCategory>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final MarkerWithCategory _item;
              final MarkerEntity _tmpMarker;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final long _tmpCategoryId;
              _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
              final double _tmpLatitude;
              _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
              final double _tmpLongitude;
              _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
              final long _tmpTimestamp;
              _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
              final String _tmpNote;
              if (_cursor.isNull(_cursorIndexOfNote)) {
                _tmpNote = null;
              } else {
                _tmpNote = _cursor.getString(_cursorIndexOfNote);
              }
              final String _tmpPhotoUri;
              if (_cursor.isNull(_cursorIndexOfPhotoUri)) {
                _tmpPhotoUri = null;
              } else {
                _tmpPhotoUri = _cursor.getString(_cursorIndexOfPhotoUri);
              }
              _tmpMarker = new MarkerEntity(_tmpId,_tmpCategoryId,_tmpLatitude,_tmpLongitude,_tmpTimestamp,_tmpNote,_tmpPhotoUri);
              final CategoryEntity _tmpCategory;
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfCategoryId);
              _tmpCategory = _collectionCategory.get(_tmpKey_1);
              _item = new MarkerWithCategory(_tmpMarker,_tmpCategory);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getMarkerById(final long id, final Continuation<? super MarkerEntity> $completion) {
    final String _sql = "SELECT * FROM markers WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<MarkerEntity>() {
      @Override
      @Nullable
      public MarkerEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
          final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfPhotoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "photoUri");
          final MarkerEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpCategoryId;
            _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
            final double _tmpLatitude;
            _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
            final double _tmpLongitude;
            _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            final String _tmpPhotoUri;
            if (_cursor.isNull(_cursorIndexOfPhotoUri)) {
              _tmpPhotoUri = null;
            } else {
              _tmpPhotoUri = _cursor.getString(_cursorIndexOfPhotoUri);
            }
            _result = new MarkerEntity(_tmpId,_tmpCategoryId,_tmpLatitude,_tmpLongitude,_tmpTimestamp,_tmpNote,_tmpPhotoUri);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }

  private void __fetchRelationshipcategoriesAscomLifemarkerDataLocalEntityCategoryEntity(
      @NonNull final LongSparseArray<CategoryEntity> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, false, (map) -> {
        __fetchRelationshipcategoriesAscomLifemarkerDataLocalEntityCategoryEntity(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `id`,`isSystemGenerated`,`systemNameKey`,`customName`,`colorHex`,`iconName` FROM `categories` WHERE `id` IN (");
    final int _inputSize = _map.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int i = 0; i < _map.size(); i++) {
      final long _item = _map.keyAt(i);
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    final Cursor _cursor = DBUtil.query(__db, _stmt, false, null);
    try {
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "id");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfIsSystemGenerated = 1;
      final int _cursorIndexOfSystemNameKey = 2;
      final int _cursorIndexOfCustomName = 3;
      final int _cursorIndexOfColorHex = 4;
      final int _cursorIndexOfIconName = 5;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        if (_map.containsKey(_tmpKey)) {
          final CategoryEntity _item_1;
          final long _tmpId;
          _tmpId = _cursor.getLong(_cursorIndexOfId);
          final boolean _tmpIsSystemGenerated;
          final int _tmp;
          _tmp = _cursor.getInt(_cursorIndexOfIsSystemGenerated);
          _tmpIsSystemGenerated = _tmp != 0;
          final String _tmpSystemNameKey;
          if (_cursor.isNull(_cursorIndexOfSystemNameKey)) {
            _tmpSystemNameKey = null;
          } else {
            _tmpSystemNameKey = _cursor.getString(_cursorIndexOfSystemNameKey);
          }
          final String _tmpCustomName;
          if (_cursor.isNull(_cursorIndexOfCustomName)) {
            _tmpCustomName = null;
          } else {
            _tmpCustomName = _cursor.getString(_cursorIndexOfCustomName);
          }
          final int _tmpColorHex;
          _tmpColorHex = _cursor.getInt(_cursorIndexOfColorHex);
          final String _tmpIconName;
          _tmpIconName = _cursor.getString(_cursorIndexOfIconName);
          _item_1 = new CategoryEntity(_tmpId,_tmpIsSystemGenerated,_tmpSystemNameKey,_tmpCustomName,_tmpColorHex,_tmpIconName);
          _map.put(_tmpKey, _item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }
}
