package poly.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import poly.dto.EmpDTO;
import poly.service.IEmpService;
import poly.util.CmmUtil;
import poly.util.DateUtill;
import poly.util.FileUtil;

@Controller
public class EmpController {
	  // 업로드되는 파일이 저장되는 기본 폴더 설정
    // final private String FILE_UPLOAD_SAVE_PATH = "C:/PersonalProject/WebContent/resource//images"; // 윈도우 경로
	final private String FILE_UPLOAD_SAVE_PATH = "/ogeuntae/WebContent/resource//images"; // 리눅스에 적용하면  home 디렉터리로 이동
	
	// 로거
	Logger log = Logger.getLogger(this.getClass());
	
	@Resource(name = "EmpService")
	IEmpService empService;
	
	
	// 학습용 이미지 저장을 위한 매핑
	@RequestMapping(value="imgStorage")
	public String imgStorage(HttpServletRequest reuest, ModelMap model) {
		log.info("img Storage start!!");
		
		String empno = reuest.getParameter("empno");
		
		model.addAttribute("empno", empno);
		
		log.info("img Storage end!!");
		
		return "/opencv/imgStorage";
	}
	
	
	// 이미지 저장 프로세스 시작
	// 직원 등록할 때 넘겨준 파라미터값을 같이 보냄
	@RequestMapping(value="imgStorageProc", method=RequestMethod.GET)
	public String imgStorageProc(HttpServletRequest request, HttpServletResponse response, ModelMap model, HttpSession session) {
		session.getAttribute("aename"); // 세션 받기
		log.info("img Storage Process start!!");
		// API 서버측  IP로 수정 로컬에서 돌리면 127.0.0.1
		// String url = "http://15.164.139.197:5002";
		// String url = "http://127.0.0.1:5001"; // 로컬 
		String url = "http://192.168.0.84:5001";
		String api = "/imgStorageDevice?";
		String empnoCheck = "empno=";
		String empno = request.getParameter("empno");
		String addres = url + api + empnoCheck + empno;
		
		model.addAttribute("apiURL",addres);
		
		log.info("img Storage Process end!!");
		
		return "/opencv/imgStorageProc";
	}
	
	// 얼굴 검색 페이지 이동

	@RequestMapping(value="imgCheckPage.do")
	public String imgCheckPage() {
		log.info("img Check  start!!");	
		log.info("img Check end!!");
		
		return "/opencv/imgCheckPage";
	}
	
	
	// 직원 관리 매핑
	@RequestMapping(value="empMange") // .do 입력
	public String Empmange(HttpServletRequest request, 
			HttpServletResponse response, ModelMap model, HttpSession session) throws Exception{
		log.info(this.getClass() + "emp manage Page Start !!");
		
		/*
		 * String admin = "CHOI"; //(String) session.getAttribute("aname"); 작업을 위한 변수 고정
		 * String msg = ""; String url = "";
		 */
		// 사원 정보 게시판에 띄우기 위한 코드
		// 설명 : 
		//
		//
		// 첫 번째 호출
		List<EmpDTO> rList = new ArrayList<EmpDTO>();
		rList = empService.getEmpList(); 

		/*
		 * if (admin.equals(null)) { log.info(this.getClass() + "로그인 하지 않았음!!!"); msg =
		 * "관리자 로그인을 해주세요"; url = "/user/login.do"; } else {
		 * 
		 * }
		 */
		// 로그 찍기
		for(EmpDTO e : rList) {			
			log.info("############################");
			log.info("EMPNO : " + e.getEmpno());
			log.info("ENAME : " + e.getEname());
			log.info("SEX : " + e.getSex());
			log.info("ATT_DATE : " + e.getAttDate());
			log.info("############################");
		}
		// 리스트 데이터 JSP로 넘겨주기
		model.addAttribute("rList", rList);
		log.info(this.getClass() + "emp manage Page End !!");
		
		return "/mainPage/empMange";
	}
	
	// 직원 추가 매핑
	@RequestMapping(value="addEmp") // .do 입력
	public String AddEmp() {
		log.info(this.getClass());
		
		return "/mainPage/addEmp";
	}
	
	// 직원 수정 매핑
	@RequestMapping(value="editEmp") // .do 입력
	public String EditEmp(HttpServletRequest request, Model model) {
		log.info(this.getClass());
		// empno를 받아와서
		String empno = CmmUtil.nvl(request.getParameter("empno"));
		// DTO 메모리 올려서 별도 공간 생성
		EmpDTO pDTO = new EmpDTO();
		// 만들어진 DTO에 empno 저장
		pDTO.setEmpno(empno);
		// 리스트에 값 담기 
		List<EmpDTO> rList = new ArrayList<EmpDTO>();
		
		rList = empService.getEmpDetail(pDTO);
		
		pDTO = null;
				
		for(EmpDTO e : rList) {
		
		log.info("empno : " + e.getEmpno());
		log.info("name : " + e.getEname());
		log.info("addres : " + e.getAddrs());
		log.info("sex : " + e.getSex());
		log.info("bday : " + e.getBday());
		log.info("email : " + e.getEemail());
		log.info("imgUrl : " + e.getProImage());
		
		}
		
		model.addAttribute("rList", rList);

		return "/mainPage/editEmp";
	}
	
	
	// 직원 등록 처리 매핑
	@RequestMapping(value="user/addEmpProc", method = RequestMethod.POST)
	public String UseraddEmpProc(HttpServletRequest request, HttpServletResponse response, ModelMap model, HttpSession session,
			@RequestParam(value="fileUpload") MultipartFile mf, MultipartHttpServletRequest iRequest) throws Exception {
		
		log.info("user Add  Process start!!");
		
		// API 접속을 위한 주소
		String empno = CmmUtil.nvl(request.getParameter("empno")); // 사원번호
		String ename = CmmUtil.nvl(request.getParameter("ename")); // 사원이름
		String bday = CmmUtil.nvl(request.getParameter("bday")); // 생년월일
		String sex = CmmUtil.nvl(request.getParameter("sex")); // 성별 1이면 남자 2면 여자
		sex = (sex.equals("1")) ? "남자" : "여자"; // 삼항연산자 사용
		String eemail= CmmUtil.nvl(request.getParameter("eemail")); // 이메일
		String proimage = ""; // 대표사진
		String addrs = CmmUtil.nvl(request.getParameter("addrs"));// 주소
		String inadmin = (String) session.getAttribute("aname"); // 세션에 등록한 관리자 이름 
		String msg = "";
		String url = "";
		// ##################이미지 저장 변수
		String save_file_name = "";
		String save_file_path = "";
		String save_folder_name = "";
		String full_img = "";
		
		// #######################이미지 저장 프로세스
		try {
			// 업로드하는 실제 파일명
			String org_file_name = mf.getOriginalFilename();

			// 파일의 확장자를 가져와, 이미지 파일만 실행되도록 함
			String ext = org_file_name.substring(org_file_name.lastIndexOf(".") + 1, org_file_name.length())
					.toLowerCase();


			if (ext.equals("jpeg") || ext.equals("jpg") || ext.equals("gif") || ext.equals("png")) {

				save_file_name = DateUtill.getDateTime("24hhmmss") + "." + ext;

				// 웹서버에 업로드한 파일 저장하는 물리적 경로
				save_file_path = FileUtil.mkdirForDate(FILE_UPLOAD_SAVE_PATH);
				// 이미지가 저장되는 폴더경로
				save_folder_name = save_file_path.substring(save_file_path.length() - 10);

				log.info("폴더경로 제대로 되었는지 확인 : " + save_folder_name);

				full_img = save_folder_name + "/" + save_file_name;
				log.info("이미지 풀 경로 : " + full_img);

				// 업로드되는 파일을 서버에 저장(전체경로+파일명.확장자 형태로 저장)
				mf.transferTo(new File(save_file_path + "/" + save_file_name));

			}
		} catch (Exception e) {
			log.info("이미지 저장 실패");
		}

		int res = 0; 
		
		log.info("empno : " + empno);
		log.info("ename : " + ename);
		log.info("ebday : " + bday);
		log.info("sex : " + sex);
		log.info("eemail : " + eemail);
		log.info("proimage : " + proimage);
		log.info("addrs : " + addrs);
		log.info("inadmin : " + inadmin);
		log.info("@@@@@@@@@@@@@@@@@JSP에서 받아옴 완료!!!!@@@@@@@@@@@@@@@@@@");
		
		if (inadmin == null) {
			
			msg = "관리자로 로그인 해주세요";
			url = "/user/login.do";
			
		} else {
			// 데이터 저장을 위한 DTO 선언
			EmpDTO pDTO = new EmpDTO();

			pDTO.setEmpno(empno);
			pDTO.setEname(ename);
			pDTO.setBday(bday);
			pDTO.setSex(sex);
			pDTO.setEemail(eemail);
			pDTO.setProImage(proimage);
			pDTO.setAddrs(addrs);
			pDTO.setInAdmin(inadmin);
            pDTO.setProImage(full_img);
            log.info("img(폴더 경로) : " + pDTO.getProImage());

			res = empService.insertEmpInfo(pDTO);

			
			if (res == 1) {
				log.info("직원 등록 성공 1 == " + res);
				msg = "직원 등록이 완료되었습니다.";
				url = "/empMange.do";
			} else {
				log.info("직원 등록 실패 1 != " + res);
				msg = "직원이 이미 등록되어 있습니다.";
				url = "/addEmp.do";
			}
		}

		model.addAttribute("msg", msg);
		model.addAttribute("url", url);
		

		
		log.info("user Add  Process end!!");
		
		return "/user/redirect";
	}
	
	// 직원 정보 수정
	@RequestMapping(value="user/updateEmpProc")
	public String updateEmpProc(HttpServletRequest request, Model model,HttpSession session) throws Exception {
		log.info("update Emp Proc start!!");
		
		String empno = CmmUtil.nvl(request.getParameter("empno")); // 사원번호
		String ename = CmmUtil.nvl(request.getParameter("ename")); // 사원이름
		String bday = CmmUtil.nvl(request.getParameter("bday")); // 생년월일
		String sex = CmmUtil.nvl(request.getParameter("sex")); // 성별 1이면 남자 2면 여자
		sex = (sex.equals("1")) ? "남자" : "여자"; // 삼항연산자 사용
		String eemail= CmmUtil.nvl(request.getParameter("eemail")); // 이메일
		String proimage = ""; // 대표사진
		String addrs = CmmUtil.nvl(request.getParameter("addrs"));// 주소
		String upadmin = "admin";//(String) session.getAttribute("aname"); // 세션에 등록한 관리자 이름 
		String msg = "";
		String url = "";
		
		int res = 0; 
		
		log.info("empno : " + empno);
		log.info("ename : " + ename);
		log.info("ebday : " + bday);
		log.info("sex : " + sex);
		log.info("eemail : " + eemail);
		log.info("proimage : " + proimage);
		log.info("addrs : " + addrs);
		log.info("upadmin : " + upadmin);
		log.info("@@@@@@@@@@@@@@@@@JSP에서 받아옴 완료!!!!@@@@@@@@@@@@@@@@@@");
		
		EmpDTO pDTO = new EmpDTO();
		
		pDTO.setEmpno(empno);
		pDTO.setEname(ename);
		pDTO.setBday(bday);
		pDTO.setSex(sex);
		pDTO.setEemail(eemail);
		pDTO.setProImage(proimage);
		pDTO.setAddrs(addrs);
		pDTO.setUpAdmin(upadmin);
		
		res = empService.UpdateEmpInfo(pDTO);
		
		if(res == 1) {
			log.info("직원 수정 성공 1 == " + res);
			msg = "직원 수정이 완료되었습니다.";
			url = "/empMange.do";
		} else {
			log.info("직원 수정 실패 1 != " + res);
			msg = "사원번호가 중복 됩니다.";
			url = "/empMange.do";
		}
		
		model.addAttribute("msg", msg);
		model.addAttribute("url", url);
		
		log.info("update Emp Proc end!!");
		
		return "/user/redirect";
	}

	@RequestMapping(value="emp/empDelete")
	public String empDelete(HttpServletRequest request, HttpServletResponse response, Model model, HttpSession session) 
			throws Exception{
		log.info(this.getClass().getName() + "empDelete Start!");
		// 아무나 접근해서 수정하지 못하도록 임시로 만든 세션######
		String admin = (String) session.getAttribute("aemail");
		// 세션#################################
		String empno = CmmUtil.nvl(request.getParameter("empno"));
		String msg = "";
		String url = "";
		int res = 0;
		log.info("empno : " + empno);
		// 세션 확인
		if(admin != null) {
			res = empService.empDeleteInfo(empno);
		}

		if (res == 1) {
			log.info("삭제 성공");
			msg = "유저 정보가 성공적으로 삭제 되었습니다.";
			url = "/empMange.do";
		} else {
			log.info("삭제 실패");
			msg = "유저 삭제 실패";
			url = "/empMange.do";
		}
		
		
		model.addAttribute("url", url);
		model.addAttribute("msg", msg);
		log.info(this.getClass().getName() + "empDelete END!");
		return "/user/redirect";
	}
	

	
	
}
