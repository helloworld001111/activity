package com.example.dubei.activity.base.jdbc;

import org.apache.ibatis.session.SqlSession;

public interface SqlSessionCallback {
	 Object doInSession(SqlSession session);
}
