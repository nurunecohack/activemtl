package com.nurun.activemtl.data;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

public class QueryBuilder {
	boolean mDistinct;
	private String[] mColumns = null;
	private String mCondition;
	private ArrayList<String> mConditionArgs = new ArrayList<String>();
	private String[] mGroupBy;
	private String[] mOrderBy;
	private String mLimit;
	private String mTableName;

	public QueryBuilder(String tableName) {
		this.mTableName = tableName;
	}

	public QueryBuilder distinct(boolean d) {
		this.mDistinct = d;
		return this;
	}

	public QueryBuilder distinct() {
		this.mDistinct = true;
		return this;
	}

	public QueryBuilder limit(int limit) {
		this.mLimit = "" + limit;
		return this;
	}

	public QueryBuilder columns(String... column) {
		this.mColumns = column;
		return this;
	}

	public QueryBuilder where(String c, String conditionArgs) {
		if (this.mCondition == null) {
			this.mCondition = c + "=?";
		} else {
			this.mCondition += ", " + c + "=?";
		}

		if (conditionArgs != null) {
			this.mConditionArgs.add(conditionArgs);
		}
		return this;
	}

	public QueryBuilder where(String column, long rowId) {
		return this.where(column, "" + rowId);
	}

	public QueryBuilder where(long rowId) {
		return this.where(BaseColumns._ID, "" + rowId);
	}
	
	public QueryBuilder wherelike(String column, String value) {
		if (this.mCondition == null) {
			this.mCondition = column + " LIKE '%" + value + "%'";
		} else {
			this.mCondition += " " + column + " LIKE '%" + value + "%'";
		}
		return this;
	}

	public QueryBuilder and() {
		this.mCondition += " and ";
		return this;
	}

	public QueryBuilder or() {
		this.mCondition += " or ";
		return this;
	}

	public QueryBuilder groupeBy(String... gb) {
		this.mGroupBy = gb;
		return this;
	}

	public QueryBuilder orderBy(String... od) {
		this.mOrderBy = od;
		return this;
	}

	private String getmGroupBy() {
		if (mGroupBy != null) {
			return Arrays.toString(mGroupBy).replace("[", "").replace("]", "");
		}
		return null;
	}

	private String getmOrderBy() {
		if (mOrderBy != null) {
			return Arrays.toString(mOrderBy).replace("[", "").replace("]", "");
		}
		return null;
	}

	public Cursor request(SQLiteDatabase mDb) {
		String[] args = new String[mConditionArgs.size()];
		return mDb.query(mTableName, mColumns, mCondition,
				mConditionArgs.toArray(args), getmGroupBy(), null,
				getmOrderBy(), mLimit);
	}

	public int delete(SQLiteDatabase mDb) {
		String[] args = new String[mConditionArgs.size()];
		return mDb.delete(mTableName, mCondition, mConditionArgs.toArray(args));
	}

	public long insert(SQLiteDatabase mDb, ContentValues values) {
		try {
			long id = mDb.insertOrThrow(mTableName, null, values);
			Log.d(getClass().getSimpleName(), "Insertion du " + mTableName
					+ " " + id);
			return id;
		} catch (Exception e) {
			Log.w(getClass().getSimpleName(), e.getMessage(), e);
			return -1;
		}

	}

	public long replace(SQLiteDatabase mDb, ContentValues values) {
		try {
			Log.d(getClass().getSimpleName(), "Replace du " + mTableName + " "
					+ values.getAsLong(BaseColumns._ID));
			return mDb.replaceOrThrow(mTableName, null, values);
		} catch (Exception e) {
			Log.w(getClass().getSimpleName(), e.getMessage(), e);
			return -1;
		}
	}

	public Long exists(SQLiteDatabase mDb, String elementId, String column) {
		Cursor mCursor = null;
		try {
			QueryBuilder existsColumns = getExistsColumns(column);
			mCursor = existsColumns.where(column, elementId).request(mDb);
			if (mCursor != null && mCursor.moveToFirst()) {
				return CursorReader.getId(mCursor);
			}
		} catch (Exception e) {
			Log.e(getClass().getSimpleName(), e.getMessage(), e);
		} finally {
			if (mCursor != null) {
				mCursor.close();
			}
		}

		return null;
	}

	private QueryBuilder getExistsColumns(String column) {
		if (BaseColumns._ID == column) {
			return columns(BaseColumns._ID);
		}
		return columns(BaseColumns._ID, column);
	}

	public long update(SQLiteDatabase mDb, ContentValues values) {
		Long id = values.getAsLong(BaseColumns._ID);
		Log.d(getClass().getSimpleName(), "update du " + mTableName + " " + id);
		mDb.update(mTableName, values, BaseColumns._ID + " = ?",
				new String[] { id.toString() });
		return id;
	}

	
}