package com.test.dao;

public interface BaseDAOListener {
	abstract void onDataLoaded(BaseDAO dao);
	abstract void onDataFailed(BaseDAO dao);
}
