package poly.persistance.mongo.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mongodb.BasicDBObject;
import com.mongodb.Cursor;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import poly.dto.AttAnalysDTO;
import poly.dto.EmpAttDTO;
import poly.persistance.mongo.IEmpAttMongoMapper;
import poly.util.CmmUtil;
import poly.util.DateUtill;
@EnableScheduling
@Configuration
@Component("EmpAttMongoMapper")
public class EmpAttMongoMapper implements IEmpAttMongoMapper{
	
	@Autowired
	private MongoTemplate mongodb;
	
	private Logger log = Logger.getLogger(this.getClass());

	@Override
	public boolean createCollection(String colNm) throws Exception {
		log.info(this.getClass().getName() + ".createCollection Start!!");
		
		boolean res = false;
		
		if(mongodb.collectionExists(colNm)) { // 만약 몽고에 해당 컬렉션이 있으면
			mongodb.dropCollection(colNm); // 기존에 있던 컬렉션 삭제하기
		}
		// 컬렉션과 인덱스 생성(빠른 조회 성능을 위해 인덱스르 사용함)
		mongodb.createCollection(colNm).createIndex(new BasicDBObject("reg_dt", 1).append("empno", 1), "attTime");
		
		res = true;
		
		log.info(this.getClass().getName() + ".createCollection END!!");
		return res;
	}
	// 직원 정보 몽고에 삽입
	@Override
	public int insertEmpATTforMongo(List<EmpAttDTO> pList, String colNm) throws Exception {
		log.info(this.getClass().getName() + ".insertRank Start!");

		int res = 0;

		if (pList == null) {
			pList = new ArrayList<EmpAttDTO>();
		}

		Iterator<EmpAttDTO> it = pList.iterator();

		while (it.hasNext()) {
			EmpAttDTO pDTO = (EmpAttDTO) it.next();

			if (pDTO == null) {
				pDTO = new EmpAttDTO();
			}

			mongodb.insert(pDTO, colNm);

		}

		res = 1;

		log.info(this.getClass().getName() + ".insertRank End!");
		return res;
	}
	
	// 메인 화면에 월 별 지각자 현황을 분석하기 위한 데이터 뽑아오기 
	// 몽고에서 뽑아옴
	@Override
	public List<AttAnalysDTO> selectAttInfoForMongo(String colNm) throws Exception {
		
		log.info(this.getClass().getName() + ".selectAttInfoForMongo start!");
		
		String late_checkCount = null;
		
		DBCollection rCollection = mongodb.getCollection(colNm); // colNm 매개변수와 같은 컬렉션을 선택
		
		//BasicDBObject query = new BasicDBObject(); // where 쿼리 만들기 위한 객체 생성
		//query.put("empno", empno); // where 조건 절, empno를 기준으로 쿼리를 가져온다.
		
		Cursor cursor = rCollection.find(); // 쿼리 실행
		
		List<AttAnalysDTO> rList = new ArrayList<>();
		
		AttAnalysDTO rDTO = null; // 몽고 도큐먼트를 DTO에 매칭해서 저장시키기 위한 클래스 객체 선언
		
		// 레코드 단위로 읽음 한마디로 한 줄씩 컬럼과 일치되는 데이터 뽑아옴 
		while(cursor.hasNext()) {
			
			rDTO = new AttAnalysDTO(); // 객체 생성
			
			final DBObject current = cursor.next(); 
			
			late_checkCount = CmmUtil.nvl((String) current.get("late_check"));
			log.info("late_check : " + late_checkCount);
			
			rDTO.setMongo_late_checkCount(late_checkCount);
			
			rList.add(rDTO);
			
			rDTO = null;
		}
		
		log.info(this.getClass().getName() + ".selectAttInfoForMongo END!!!");
		return rList;
	}
	
	/* cron 식으로 스케쥴러 동작을 위한 어노테이션 추가(쿼프 방법도 있으나 구글링 해서 알아볼 것 비슷함)
	* cron 식이란? 초 분 시 일 월 ? 년도 순이 기본 포맷으로 
	* 2020년 5월 8일 15시 16분 30초에 동작을 걸려면
	* ex : 30 16 15 8 5 ? 2020 으로 입력 하면 된다. 또한 년도는 생략가능하다. 30 16 15 8 5 ? or 30 16 15 8 5 ? * 가능
	* 그렇다면 매일 오후 14시 15분 10초에 실행시키고자 하면 일 월에 *를 추가해주면 된다.
	* ex : 10 15 14 * * ?
	* 매일 새벽 4시면?
	* ex : 0 0 4 * * ?
	* 매 시간 10분 마다 실행하는 크론식은?
	* ex : 0 0/10 0 * * * ? => 그러면 0분0초->10분 0초 -> 20분 0초 이런식으로 매 시간 10분 간격으로 실행된다.
	*/
	//                초 분  시 일 월 ? 년
	@Scheduled(cron = "0 55 23 * * ?")
	@Override
	public void batSelectAttInfoForMongo() throws Exception {
		
		log.info(this.getClass().getName() + ".batselectAttInfoForMongo start!");
		// 정식 배치
		String colFirstNm = "AttInfoInsert_";
		String years = DateUtill.getDateTime("yyyy_MM_dd");
		String colNm = colFirstNm + years;
		DBCollection rCollection = mongodb.getCollection(colNm); // colNm 매개변수와 같은 컬렉션을 선택
		// 정식 배치
		
		//####################### 배치 수동 입력#########################################
		//String testcolNm = "AttInfoInsert_2021_06_06"; // 테스트용
		//DBCollection rCollection = mongodb.getCollection(testcolNm); // test용 배치
		//#############################################################################


		
		//BasicDBObject query = new BasicDBObject(); // where 쿼리 만들기 위한 객체 생성
		//query.put("empno", empno); // where 조건 절, empno를 기준으로 쿼리를 가져온다.
		
		Cursor cursor = rCollection.find(); // 쿼리 실행
		
		List<AttAnalysDTO> rList = new ArrayList<>();
		
		AttAnalysDTO rDTO = null; // 몽고 도큐먼트를 DTO에 매칭해서 저장시키기 위한 클래스 객체 선언
		
		// 레코드 단위로 읽음 한마디로 한 줄씩 컬럼과 일치되는 데이터 뽑아옴 
		while(cursor.hasNext()) {
			
			rDTO = new AttAnalysDTO(); // 객체 생성
			
			final DBObject current = cursor.next(); 
			
			// 몽고에서 가져온 데이터를 DTO에 담는데 오브젝트 형식이라서 문자열로 형변환 해준다.
			rDTO.setReg_dt(current.get("reg_dt").toString());
			log.info("reg_dt for mongo : " + rDTO.getReg_dt());
			String regDT = rDTO.getReg_dt(); // 
			
			rDTO.setEmpno(current.get("empno").toString());
			log.info("empno for mongo : " + rDTO.getEmpno());
			
			rDTO.setAtt_date(current.get("att_date").toString());
			log.info("att_date for mongo : " + rDTO.getAtt_date());

			rDTO.setLate_Check(current.get("late_check").toString());
			log.info("late_check for mongo : " + rDTO.getLate_Check());
			
			// 월 입력을 위한 스위치 문 사용
			String[] res = regDT.split("\\-");
			String cnt = "";
			
			switch (res[1]) {
				case "01": cnt = "1"; break;
				case "02": cnt = "2"; break;
				case "03": cnt = "3"; break;
				case "04": cnt = "4"; break;
				case "05": cnt = "5"; break;
				case "06": cnt = "6"; break;
				case "07": cnt = "7"; break;
				case "08": cnt = "8"; break;
				case "09": cnt = "9"; break;
				case "10": cnt = "10"; break;
				case "11": cnt = "11"; break;
				case "12": cnt = "12"; break;
			}
			
			rDTO.setCnt(cnt);
			log.info("Cnt for mongo : " + rDTO.getCnt());
			
			rList.add(rDTO);
			
			rDTO = null;
		}
		// 배치를 통해 인서트 하는 몽고 
		log.info("bat Insert Start!!!");
		// 분석용 컬렉션에 한번에 담기
		int barRes = batInsertEmpATTforMongo(rList);
		
		if(barRes == 1) {
			log.info("배치 후 분석용 컬렉션 삽입 성공");
		} else {
			log.info("배치 후 분석용 컬렉션 삽입 실패");
		}
		
		log.info("bat Insert End!!!");
		log.info(this.getClass().getName() + ".batselectAttInfoForMongo END!!!");

		

	}
	
	// 배치와 함께 작동할 분석용 컬렉션에 담는 작업
	@Override
	public int batInsertEmpATTforMongo(List<AttAnalysDTO> pList) throws Exception {
		log.info(this.getClass().getName() + ".insertRank Start!");
		
		String colFirstNm = "AttInfo_";
		String years = DateUtill.getDateTime("yyyy");
		// 컬렉션 이름 만들기
		String colNm = colFirstNm + years;
		int res = 0; // 결과 상태 반환용 변수
		
		// 기존에 등록된 컬렉션 이름이 존재하는지 체크하고 
		// ! 반전을 주어 중복이 있으면 True -> False 로 컬렉션 생성
		if (!mongodb.collectionExists(colNm)) {
			// 컬렉션이 없으면 생성해라
			mongodb.createCollection(colNm).createIndex(new BasicDBObject("user_id", 1));
		} 
		
		if (pList == null) {
			pList = new ArrayList<AttAnalysDTO>();
		}

		Iterator<AttAnalysDTO> it = pList.iterator();

		while (it.hasNext()) {
			AttAnalysDTO pDTO = (AttAnalysDTO) it.next();

			if (pDTO == null) {
				pDTO = new AttAnalysDTO();
			}

			mongodb.insert(pDTO, colNm);
		}

		res = 1;

		log.info(this.getClass().getName() + ".insertRank End!");
		return res;
	}

	

}
