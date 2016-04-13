package cn.it.utils;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * 基础的MongoDB操作类
 */
public class BaseMongoDB {

	private static Log logger = LogFactory.getLog(BaseMongoDB.class); 
	
	/**
	 * 获得Collection (表名)
	 * 
	 * @param collectionName	Collection名称(表名称)
	 * @return 
	 */
	public DBCollection getDBCollection(String collectionName) {	
		logger.debug((DBCollection) DBUtils.getInstance().getDb().getCollection(collectionName));
		return (DBCollection) DBUtils.getInstance().getDb().getCollection(collectionName);
	}

	/**
	 * 添加对象到相应的Collection
	 * 
	 * @param collectionName	Collection名称(表名称)
	 * @param obj				存储的数据对象
	 * @return
	 */
	public DBObject insert(String collectionName, DBObject obj) {
		getDBCollection(collectionName).insert(obj);
		return obj;
	}

	/**
	 * 批量添加数据到Collection中
	 * 
	 * @param collectionName	Collection的名称(集合名称)
	 * @param objList			数据的列表集合
	 */
	public void insertBatch(String collectionName, List<DBObject> objList) {
		if (null == objList || objList.isEmpty()) {
			return;
		}
		
		getDBCollection(collectionName).insert(objList);
	}

	/**
	 * 删除实体
	 * 
	 * @param collectionName	Collection名称(表名)
	 * @param obj				对象
	 */
	public void delete(String collectionName, DBObject obj) {
		getDBCollection(collectionName).remove(obj);
	}

	/**
	 * 查询
	 * @param collectionName	Collection名称(表名)
	 * @param obj				对象
	 * @return
	 */
	public List<DBObject> find(String collectionName, DBObject obj) {
		logger.debug("FIND-->>>>>" + collectionName);
		DBCursor dbCursor = getDBCollection(collectionName).find(obj);
		return DBCursor2List(dbCursor);
	}
	
	/**
	 * 查询并排序
	 * @param collectionName	Collection名称(表名)
	 * @param query				查询条件
	 * @param sort				排序方式
	 * @return
	 */
	public List<DBObject> find(String collectionName,DBObject query, DBObject sort) {
		DBCursor cur;
		if (query != null) {
			cur = getDBCollection(collectionName).find(query);
		} else {
			cur = getDBCollection(collectionName).find();
		}
		if (sort != null) {
			cur.sort(sort);
		}
		return DBCursor2List(cur);
	}
	
	/**
	 * 查询
	 * @param collectionName	集合名称
	 * @param query				查询条件
	 * @param sort				排序
	 * @param start				页码
	 * @param limit				分页大小
	 * @return
	 */
	public List<DBObject> find(String collectionName,DBObject query, DBObject sort, int start,int limit) {
		DBCursor cur = null;
		if (query != null) {
			cur = getDBCollection(collectionName).find(query);
		} else {

		}
		if (sort != null) {
			cur.sort(sort);
		}
		if (start == 0) {
			cur.batchSize(limit);
		} else {
			cur.skip(start).limit(limit);
		}
		return DBCursor2List(cur);
	}
	
	/**
	 * 将DB游标转换为List
	 * @param cur	游标
	 * @return
	 */
	private List<DBObject> DBCursor2List(DBCursor cur) {
		List<DBObject> list = new ArrayList<DBObject>();
		if (cur != null) {
			list = cur.toArray();
		}
		return list;
	}

}
