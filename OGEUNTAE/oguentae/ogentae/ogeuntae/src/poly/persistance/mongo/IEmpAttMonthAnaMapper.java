package poly.persistance.mongo;

import java.util.List;
import poly.dto.AttMonthAnaDTO;

public interface IEmpAttMonthAnaMapper {
	// 년도 별 월 지각자수 분석 
	public List<AttMonthAnaDTO> getAttMonthAna(String colNm) throws Exception;

}
