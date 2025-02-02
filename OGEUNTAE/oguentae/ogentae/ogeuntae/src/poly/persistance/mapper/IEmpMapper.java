package poly.persistance.mapper;

import java.util.List;

import config.Mapper;
import poly.dto.EmpDTO;

@Mapper("EmpMapper")
public interface IEmpMapper {
	// 직원 등록용 
	int insertEmpInfo(EmpDTO pDTO) throws Exception; 
	// 직원 등록 중복 체크용
	EmpDTO getEmployExists(EmpDTO pDTO) throws Exception;
	// 직원 정보 조회 세번째 호출
	List<EmpDTO> getEmpList();
	// 유저 정보 상세보기(수정을 위함)
	List<EmpDTO> getEmpDetail(EmpDTO pDTO);
	// 유저 정보 수정
	int updateEmpInfo(EmpDTO pDTO);
	// emp 정보 삭제
	int deleteEmpInfo(EmpDTO pDTO);
	// 프로필 사진 저장
	int InsertImgStorage(EmpDTO pDTO);



}
