package poly.persistance.mongo.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.mongodb.AggregationOptions;
import com.mongodb.BasicDBObject;
import com.mongodb.Cursor;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import poly.dto.AttMonthAnaDTO;
import poly.persistance.mongo.IEmpAttMonthAnaMapper;
import poly.util.CmmUtil;

@Component("EmpAttMonthAnaMapper")
public class EmpAttMonthAnaMapper implements IEmpAttMonthAnaMapper{
	private Logger log = Logger.getLogger(getClass());
	
	@Autowired
	private MongoTemplate mongodb;
	
	// 년도 별 지각자 수 분석
	@Override
	public List<AttMonthAnaDTO> getAttMonthAna(String colNm) throws Exception {
		log.info(this.getClass().getName() + "분석 시작!!!");
		// 데이터를 가져올 컬렉션 선택
		DBCollection rCol = mongodb.getCollection(colNm);
		//################어그리게이션###################
        List<DBObject> pipeline = Arrays.asList(
                new BasicDBObject()
                        .append("$match", new BasicDBObject()
                                .append("late_Check", "\uC9C0\uAC01")
                        ), 
                new BasicDBObject()
                        .append("$group", new BasicDBObject()
                                .append("_id", new BasicDBObject()
                                        .append("cnt", "$cnt")
                                )
                                .append("COUNT(late_Check)", new BasicDBObject()
                                        .append("$sum", 1)
                                )
                        ), 
                new BasicDBObject()
                        .append("$project", new BasicDBObject()
                                .append("cnt", "$_id.cnt")
                                //아래 원본 .append("COUNT(late_Check)", "$COUNT(late_Check)")
                                .append("late_CheckCount", "$COUNT(late_Check)")
                                .append("_id", 0)
                        ),
                new BasicDBObject() // 정렬
                        .append("$sort", new BasicDBObject()
                        .append("cnt", 1)
                        )
        );
        
        AggregationOptions options = AggregationOptions.builder().allowDiskUse(true).build();
                        
        Cursor cursor = rCol.aggregate(pipeline, options);
        
        
        List<AttMonthAnaDTO> rList = new ArrayList<AttMonthAnaDTO>();
        
        AttMonthAnaDTO rDTO = null;
        
        while(cursor.hasNext()) {
        	
        	rDTO = new AttMonthAnaDTO();
        	
        	final DBObject current = cursor.next();
        	String month = CmmUtil.nvl(current.get("cnt").toString());
        	String late_CheckCount = CmmUtil.nvl(current.get("late_CheckCount").toString());
        	
        	log.info("month : " + month);
        	log.info("late_CheckCount : " + late_CheckCount);
        	
        	rDTO.setMonth(month);
        	rDTO.setLate_CheckCount(late_CheckCount);
        	rList.add(rDTO);
        	rDTO = null;

        }
        
        log.info(this.getClass().getName() + "END");
		return rList;
	}

}
