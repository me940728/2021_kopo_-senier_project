package poly.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import poly.dto.EmpAttDTO;
import poly.persistance.mapper.IEmpAttMapper;
import poly.persistance.mongo.IEmpAttMongoMapper;
import poly.service.IEmpAttService;
import poly.util.CmmUtil;
import poly.util.DateUtill;
import poly.util.JsonParsProc;
import poly.util.UrlUtil;

// 출근 정보 기입을 위한 로직
@Service("EmpAttService")
public class EmpAttService implements IEmpAttService {

	private Logger log = Logger.getLogger(this.getClass());

	@Resource(name = "EmpAttMapper")
	IEmpAttMapper empAttMapper;
	
	@Resource(name = "EmpAttMongoMapper")
	IEmpAttMongoMapper empMongoMapper;
	

	// parameter 1 API로 가는 주소, 두번째는 타이머, 세번쨰는 강제 정료 인터럽트
	@SuppressWarnings("null")
	@Override
	public int empAttProc(String addres, String checkTime, String ascTime, String lateTime) 
			throws IOException, Exception {
		
		log.info("empAttCheck Start!!!!");
		log.info("addres : " + addres);
		log.info("checkTime : " + checkTime);
		log.info("ascTime : " + ascTime);
		
		String res = "";
		String empno = "";
		String[] empTime = null;
		String[] pEmpTime = null;
		String insertDate = null; // 출근시간이 입력되는 날짜를 넣기 위한 객체 호출(복합키로 작용할 것임)
		String pLateTime = lateTime;
		
		// ################################################################################
		
		int num = 0;
		int check = 0;
		int upsert = 0;
		
		// ################################################################################
		
		UrlUtil uu = new UrlUtil(); // API 서버에 보내기 위한 매서드 호출
		EmpAttDTO pDTO = null;
		JSONParser parser = null;
		JSONObject json = null;
		List<EmpAttDTO> pList = empAttMapper.getEmpno(); // 직원 정보 한번에 가져오기
		
		// ################################################################################
		
		List<EmpAttDTO> mongoList = new ArrayList<>();
		
		log.info("::" + pList);

		// 파이선 서버로 보내기 위해 문자열로 합침
		for (EmpAttDTO e : pList) {
			
			empno += e.getEmpno() + ",";
			
		}
		
		log.info("String empno :: " + empno);
		
		String [] resTime = null; // JSP에서 받은 종료 시간
		int hour; // 종료 시간에서 시를 나눔
		int min; // 종료 시간에서 분을 나눔
		int intAscTime; // 강제 종료를 위한 매개 변수
		
		// 만약 강제 종료를 하면 타이머 데이터가 오질 않으니 시간 값이 안들어 왔을 경우
		if(checkTime == null) {
			log.info("강제 종료 프로세스 실행");
			hour = 0;
			min = 0;
			intAscTime = Integer.parseInt(ascTime); // 종료를 위한 취소 인터럽트
			
		// 제한 시간을 입력해서 else 문이 시작됨	
		} else {
			log.info("정상 종료 시간 프로세스 실행");
			resTime = checkTime.split("\\:");
			hour = Integer.parseInt(resTime[0]); // 종료를 위한 시
			min = Integer.parseInt(resTime[1]); // 종료를 위한 분
			intAscTime = 0;
		}
		
		// ##############          API서버로 전송         ########################################
		res = uu.urlReadforString(addres + empno + "&hour=" + hour + "&min=" + min + "&ascTime=" + intAscTime); // 합쳐진 문자열을 주소와 함께 붙혀서 보내기
		log.info("res : " + res);
		
		pList = null; // 메모리 관리를 위해 항상 객체 사용 후 쓸모 없으면 초기화 
		uu = null;
		
		// #################### API 서버로부터 JSON 형식으로 받았음 ####################################
		
		parser = new JSONParser(); // 서버로부터 데이터를 JSON형식으로 받음
		json = (JSONObject) parser.parse(res);
		log.info("json :: " + json);
		
		// 유효성 검사 만약 null 값이 들어오면 객체를 올려서 프로그램 동작 계속 시킴
		if (json == null) {
			
			log.info("json is null ");
			json = new JSONObject();
			
		} else {
			
			num = 1;
			String time = json.get("empno").toString(); // JSON key값(empno)에 담긴 내용을 문자열로 변환해서 받음
			log.info("json : " + time);
			
			empTime = JsonParsProc.JsonParsSplit(time); // json 가공을 위해 만든 메서드를 호출하여 리턴 받음
			//log.info("::::" + empTime[0] + empTime[1]);
			
			// 디비에 들어갈 날짜 지정
			insertDate = DateUtill.getDateTime("yyyy-MM-dd"); 
			
			for(int i = 0; i < empTime.length; i++) {
				
				pDTO = new EmpAttDTO(); // empno:time 형식이 담긴 배열의 크기 만큼 반복문을 돌려줌
				// :을 기준으로 2개 배열로 나눠 임시 저장시킴 왜냐면 시간도 : 로 구분되기 때문에
				pEmpTime = empTime[i].split("\\:", 2);
				
				pDTO.setEmpno(pEmpTime[0]); // 홀수는 항상  empno
				log.info("setEmpno : " + pEmpTime[0]);
				
				pDTO.setAtt_date(pEmpTime[1]); // 짝수는 항상 time이 담김
				log.info("setAtt_date : " + pEmpTime[1]);
				
				pDTO.setReg_dt(insertDate);
				log.info("setReg_dt : " + insertDate);
				
				pDTO.setLate_check(JsonParsProc.lateCheck(pEmpTime[1], pLateTime)); // 지각? 정상출석? 문자열로 반환
				log.info("pLateTime : " + pLateTime);
				log.info("setLate_check : " + pEmpTime[1]);
				log.info("getLate_check : " + pDTO.getLate_check());
				
				// 같은 날짜 같은 회원 정보 있는지 확인용
				EmpAttDTO checkDTO = empAttMapper.getEmpAttExists(pDTO);
				
				// #####################################################
				if(CmmUtil.nvl(checkDTO.getExists_yn()).equals("Y")) {					
					log.info("get empno : " + pDTO.getEmpno());
					log.info("이미 출근 등록이 완료된 회원");
					// checkDTO = new EmpAttDTO(); // 에러 발생되서 멈추면 안되기 떄문에 메모리로 올림
				} else {
					log.info("출근 등록 삽입 작업 시작");
					// 디비 인서트 실행 객체를 실어서 생성해줘야 작업이 실행됨 
					// DTO에 단순히 값을 넣어서는 실행 안댐
					check = empAttMapper.insertEmpAttTime(pDTO);
					// 최근 출근 시간 저장하는 쿼리
					upsert = empAttMapper.upSertAttTime(pDTO);
					// 몽고에 넣기 위해 리스트에다가 객체 담기 몽고에는 리스트 형태로 담아야 함
					// ?? null 에러 발생
					mongoList.add(pDTO);
					// 객체 초기화 다음 값 담기 위함
					pDTO= null;
					log.info("출근 등록 삽입 작업 종료");
					log.info("mongoList add : " + mongoList.get(i));
				}
					
				// 유효성 검사
				if(check == 0 && upsert ==0) {
					log.info("ATT_DATE insert Fail...");
					pDTO = new EmpAttDTO(); // 만약 DTO에 아무 값도 안들어가면 강제로 객체를 오렬서 프로그램 정지 방지
				}else {
					log.info("success!!");
				}
			}
		}
		// 컬렉션 이름 날짜별로 컬렉션 삽입 할 것임
		String colNm = "AttInfoInsert_" + DateUtill.getDateTime("yyyy_MM_dd");
		// 컬렉션 생성
		empMongoMapper.createCollection(colNm);
		// 컬렉션에 데이터 삽입하기
		empMongoMapper.insertEmpATTforMongo(mongoList, colNm);
		
		log.info("img Check Process end!!");
		
		return num;
	}


}
