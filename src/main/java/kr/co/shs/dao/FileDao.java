package kr.co.shs.dao;

import java.util.HashMap;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

@Repository
public class FileDao implements FileDaoInterface {

	@Resource(name="sqlSession")
	SqlSession session;
	
	@Override
	public int insert(HashMap<String, Object> param) {
		
		return session.insert("file.insert",param);
	}

}
