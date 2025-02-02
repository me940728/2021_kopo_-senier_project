package poly.service.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import poly.dto.MailDTO;
import poly.dto.UserDTO;
import poly.persistance.mapper.IUserMapper;
import poly.service.IMailService;
import poly.service.IUserService;
import poly.util.CmmUtil;

@Service("UserService")
public class UserService implements IUserService {

	private Logger log = Logger.getLogger(this.getClass());

	@Resource(name = "UserMapper")
	private IUserMapper userMapper;

	@Resource(name = "MailService")
	private IMailService mailService;

	// 로그인 확인용
	@Override
	public UserDTO getLoginInfo(UserDTO uDTO) {

		return userMapper.getLoginInfo(uDTO);
	}
	
	@Override
	public UserDTO getUserInfo(UserDTO rDTO) throws Exception {
		
		return userMapper.getUserInfo(rDTO);
	}

	// 회원 가입용 비즈니스 로직
	@Override
	public int insertUserInfo(UserDTO pDTO) throws Exception {
		log.info(this.getClass() + "회원가입 프로세스 시작");
		
		int res = 0;

		if (pDTO == null) {
			log.info(this.getClass() + "컨트롤러에서 넘어온 DTO null 값");
			pDTO = new UserDTO();
		}
		
		log.info("password : " + pDTO.getApw());
		log.info("email : " + pDTO.getAemail());
		log.info("name : " + pDTO.getAname());
		
		log.info(this.getClass() + "중복 가입 여부 확인");
		
		UserDTO rDTO = userMapper.getUserExists(pDTO);
		
		if (rDTO == null) { // 에러로 시슽켐 정지 방지용
			rDTO = new UserDTO();
		}

		if (CmmUtil.nvl(rDTO.getExists_yn()).equals("Y")) { // 만약 중복가입 되었으면 1이면 중복, 0이면 중복 아님
			log.info(this.getClass() + "해당 이메일 이미 가입되어 있음");
			res = 2;
			
		} else {
			log.info(this.getClass() + "중복 확인?? " + rDTO.getExists_yn());
			log.info(this.getClass() + "##############데이터 삽입 시도##############");

			int success = userMapper.insertUserInfo(pDTO); // 성공하면 1을 반환 실패하면 0 반환

			log.info(this.getClass() + "데이터 삽입 성공 success 1 == " + success); // 1이여야 성공

			if (success > 0) { // 성공했으면 
				
				res = 1; //  컨트롤러에서 작업을 위해 
				log.info(this.getClass() + "메일 발송 시작");
				
				MailDTO mDTO = new MailDTO();
				// 가입 후 유저 정보 가져오는 매퍼

				
				mDTO.setToMail(CmmUtil.nvl(pDTO.getAemail())); // 받을 사람
				mDTO.setTitle("오근태 운영자_STAR_CHOI 드림_"); // 제목
				mDTO.setContents(CmmUtil.nvl(pDTO.getAname()) + " 님의 오근태 관리자로 등록되셨음을 축하드립니다.\n - STAR CHOI 드림 -");

				mailService.doSendMail(mDTO);

			} else {
				// 만약 모두 실패하면
				res = 0;
			}
		}
		log.info(this.getClass() + "회원가입 종료");

		return res;
	}
	// 유저 정보 착지 위한 메서드
	@Override
	public UserDTO getFindUserInfo(UserDTO userpDTO) throws Exception {
		log.info("유저 이메일 찾기 프로세스 실행");
		UserDTO rDTO = userMapper.getFindUserInfo(userpDTO);
		// 널처리 안하면 어떻게 되는 지 보여줌 
		// 메모리 올리기 처리 예시 
		if(rDTO == null ) {
			rDTO = new UserDTO();
		} 
		
		log.info("유저 이메일 찾기 프로세스 종료");
		return rDTO;
	}
	// 유저 정보 찾아서 비밀번호 변경하기
	@Override
	public int updateAdminInfo(UserDTO pDTO) throws Exception {
		
		int success = userMapper.updateAdminInfo(pDTO);
		
		int res = 0;
		if(success == 1) {
			res = 1;
		}
		
		return res;
	}



}
