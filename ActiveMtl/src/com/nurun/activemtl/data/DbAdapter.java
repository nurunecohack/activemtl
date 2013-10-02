package com.nurun.activemtl.data;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

public abstract class DbAdapter<E> {

	protected SQLiteDatabase mDb;
	public static final String DESC = "DESC";

	protected DbAdapter(Context context) {
		mDb = FacDatabaseHelper.getInstance(context).getWritableDatabase();
	}

	public void startTransaction() {
		mDb.beginTransaction();
	}

	public void stopTransaction() {
		if (mDb.inTransaction()) {
			mDb.endTransaction();
		}
	}

	public void commit() {
		if (mDb.inTransaction()) {
			mDb.setTransactionSuccessful();
		}
	}

	public long insert(E element) {
		long idInserted = new QueryBuilder(getTableName()).insert(mDb,
				getValues(element));
		return idInserted;
	}

	public void insert(List<E> elements) {
		for (E element : elements) {
			insert(element);
		}
	}

	/*
	 * public Long replace(E element) { return new QueryBuilder(getTableName())
	 * .replace(mDb, getValues(element)); }
	 */

	protected abstract ContentValues getValues(E element);

	protected abstract String getTableName();

	public boolean delete(long rowId) {
		Log.d(getClass().getSimpleName(), "Deleting " + getTableName()
				+ " with id=<" + rowId + ">");
		return new QueryBuilder(getTableName()).where(rowId).delete(mDb) == 1;
	}

	public long insertOrUpdate(E element) {
		try {
			ContentValues values = getValues(element);
			Long idDb = new QueryBuilder(getTableName()).exists(mDb,
					values.getAsString(getExistsColumn()), getExistsColumn());
			if (idDb == null) {
				return new QueryBuilder(getTableName()).insert(mDb, values);
			}
			values.put(BaseColumns._ID, idDb);
			return new QueryBuilder(getTableName()).update(mDb, values);
		} catch (Exception e) {
			Log.w(getClass().getSimpleName(), e.getMessage(), e);
			return -1;
		}
	}

	protected abstract String getExistsColumn();

	public int clear() {
		Log.d(getClass().getSimpleName(), "Deleting all " + getTableName());
		return new QueryBuilder(getTableName()).delete(mDb);
	}

	public Cursor list() {
		Cursor cursor = new QueryBuilder(getTableName()).request(mDb);
		Log.d(getClass().getSimpleName(), "list all " + getTableName()
				+ " received from the database : " + cursor.getCount());
		return cursor;
	}

	public Cursor list(String orderBy) {
		Cursor cursor = new QueryBuilder(getTableName()).orderBy(orderBy)
				.request(mDb);
		Log.d(getClass().getSimpleName(), "list all " + getTableName()
				+ " received from the database : " + cursor.getCount());
		return cursor;
	}

	public Cursor get(long rowId) throws SQLException {
		Cursor mCursor = new QueryBuilder(getTableName()).where(rowId).request(
				mDb);
		if (mCursor != null && mCursor.getCount() > 0) {
			mCursor.moveToFirst();
			return mCursor;
		}

		String message = "No " + getTableName()
				+ " was found in database for id " + rowId;
		Log.e(getClass().getSimpleName(), message);
		throw new SQLException(message);
	}

	public boolean isEmpty() {
		return new QueryBuilder(getTableName()).columns(BaseColumns._ID)
				.request(mDb).getCount() == 0;
	}

	/*
	 * public void replace(List<E> elements) { for (E element : elements) {
	 * replace(element); } }
	 */

	public void insertOrUpdate(List<E> elements) {
		if (elements != null) {
			for (E element : elements) {
				insertOrUpdate(element);
			}
		}
	}

}
