package poly.persistance.mongo;

import java.util.List;

import poly.dto.AttAnalysDTO;
import poly.dto.EmpAttDTO;

public interface IEmpAttMongoMapper {
	// 컬렉션 생성
	public boolean createCollection(String colNm) throws Exception;
	// 생성된 컬렉션에 데이터 삽입
	public int insertEmpATTforMongo(List<EmpAttDTO> pList, String colNm) throws Exception;
	// 분석을 위한 몽고 데이터 가져오기 리스트로 담아서 가져올 예정
	// 매개변수는 컬렉션 이름, 데이터 삽입 날짜와 지각 유무
	public List<AttAnalysDTO> selectAttInfoForMongo(String colNm) throws Exception;
	
	
	// 스케쥴러로 한 컬렉션에 한번에 때려 박는 배치 매서드 크론식으로 컨트롤함
	public void batSelectAttInfoForMongo() throws Exception;
	// 배치로 구동시킬 메서드
	public int batInsertEmpATTforMongo(List<AttAnalysDTO> pList) throws Exception;
}
