package poly.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import poly.dto.AttAnalysDTO;
import poly.dto.AttMonthAnaDTO;
import poly.service.IAttAnalysService;
import poly.util.DateUtill;


@Controller
public class MainController {
	
	private Logger log = Logger.getLogger(this.getClass());
	
	// 대쉬보드에서 분석을 위한 리소스 연결
	@Resource(name="AttAnalysService")
	IAttAnalysService attAnalysService;
	
	// 메인 페이지 매핑
	@RequestMapping(value="opencvIndex") // .do 입력
	public String OpencvIndex(ModelMap model, HttpSession session) throws Exception {
		log.info(this.getClass());
		session.getAttribute("aemail");
		// 출근 정보 불러오기 위한 리스트 구현
		List<AttAnalysDTO> rList = attAnalysService.getAttInfo();
		// 총인원 / 출석 인원 총인원 가져오는 출근자 리스트
		AttAnalysDTO rDTO= attAnalysService.getEmpCount();
		
		String empCount = rDTO.getCnt();
		int empAttCount = rList.size();
		
		String toDay = DateUtill.getDateTime("yyyy-MM-dd");
		
		for(AttAnalysDTO e : rList) {
			log.info("#######################################");
			log.info("empno: " + e.getEmpno());
			log.info("ename: " + e.getEname());
			log.info("sex: " + e.getSex());
			log.info("addrs: " + e.getAddrs());
			log.info("birthDay: " + e.getBday());
			log.info("attDate: " + e.getAtt_date());
			log.info("attCount : " + e.getAtt_date());
			log.info("late_check : " + e.getLate_Check());			
			log.info("#######################################");
		}
		
		log.info("empCountAll : " + empCount);

		// 분석한 결과 서비스에서 가져오기
		List<AttMonthAnaDTO> mongoList = attAnalysService.getAttMonthAna();
	    int jan = 0, feb = 0, mar = 0, apr = 0, may = 0, jun = 0, jul = 0, aug = 0, sep = 0, oct = 0, nov = 0, dec = 0;
		
		if(mongoList == null) {
			log.info("서비스에서 안넘어 왔음");
			mongoList = new ArrayList<AttMonthAnaDTO>();
			
		}else {
			log.info("분석결과 도착!!");
			log.info(mongoList);
			
		    for(AttMonthAnaDTO res : mongoList){
		    	log.info("분석결과 시각화 작업 start!!");
		    	
		    	int month = Integer.parseInt(res.getMonth());
		    	log.info("month : " + month);
		    	
		    	switch(month){
		    		case 1: jan = Integer.parseInt(res.getLate_CheckCount()); log.info("jan : " + jan); break;
		    		
		    		case 2: feb = Integer.parseInt(res.getLate_CheckCount()); log.info("feb : " + feb); break;
		    		
		    		case 3: mar = Integer.parseInt(res.getLate_CheckCount()); log.info("mar : " + mar); break;
		    		
		    		case 4: apr = Integer.parseInt(res.getLate_CheckCount()); log.info("apr : " + apr); break;
		    		
		    		case 5: may = Integer.parseInt(res.getLate_CheckCount()); log.info("may : " + may); break;
		    		
		    		case 6: jun = Integer.parseInt(res.getLate_CheckCount()); log.info("jun : " + jun); break;
		    		
		    		case 7: jul = Integer.parseInt(res.getLate_CheckCount()); log.info("jul : " + jul); break;
		    		
		    		case 8: aug = Integer.parseInt(res.getLate_CheckCount()); log.info("aug : " + aug); break;
		    		
		    		case 9: sep = Integer.parseInt(res.getLate_CheckCount()); log.info("sep : " + sep); break;
		    		
		    		case 10: oct = Integer.parseInt(res.getLate_CheckCount()); log.info("oct : " + oct); break;
		    		
		    		case 11: nov = Integer.parseInt(res.getLate_CheckCount()); log.info("nov : " + nov); break;
		    		
		    		case 12: dec = Integer.parseInt(res.getLate_CheckCount()); log.info("dec : " + dec); break;
		    	}
		    	log.info("분석결과 시각화 작업 END!!");
		    }
		    
		}
		
		model.addAttribute("rList", rList);
		model.addAttribute("empCount", empCount);
		model.addAttribute("empAttCount", empAttCount);
		model.addAttribute("toDay", toDay);
		// 월 별 분석
		model.addAttribute("jan", jan);
		model.addAttribute("feb", feb);
		model.addAttribute("mar", mar);
		model.addAttribute("apr", apr);
		model.addAttribute("may", may);
		model.addAttribute("jun", jun);
		model.addAttribute("jul", jul);
		model.addAttribute("aug", aug);
		model.addAttribute("sep", sep);
		model.addAttribute("oct", oct);
		model.addAttribute("nov", nov);
		model.addAttribute("dec", dec);
		
		rList = null;
		mongoList = null;
		log.info(this.getClass());
		
		return "/mainPage/index";
	}
	
	// 몽고에서 데이터 가져오는지 체크 테스트용 
	// 결과 : OK 연결 이상무
	@RequestMapping(value = "mongo/AttListSelect")
	@ResponseBody
	public List<AttAnalysDTO> mongoSelectAttInfo(HttpServletRequest request, HttpServletResponse responce) throws Exception{
		
		log.info(this.getClass().getName() + "mongoSelectAttInfo Start!!");
		
		List<AttAnalysDTO> rList = attAnalysService.selectAttInfoForMongo();
		
		if(rList == null) {
			rList = new ArrayList<AttAnalysDTO>();
		}
		
		log.info(this.getClass().getName() + "mongoSelectAttInfo ENd!!");
		
		return rList;
	}

	
	
			
}
