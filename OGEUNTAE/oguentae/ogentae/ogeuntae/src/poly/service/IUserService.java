package poly.service;

import poly.dto.UserDTO;

public interface IUserService {
	// 유저 로그인 기능
	UserDTO getLoginInfo(UserDTO uDTO);
	// 회원 가입 
	int insertUserInfo(UserDTO pDTO) throws Exception;
	// 가입 후 정보 넘겨주는 DTO
	UserDTO getUserInfo(UserDTO rDTO) throws Exception;
	// 유저 정보를 찾기 위한 기능
	UserDTO getFindUserInfo(UserDTO userpDTO) throws Exception;
	// 관리자 정보 찾아서 비밀번호 변경하기
	int updateAdminInfo(UserDTO pDTO) throws Exception;
	
	
}
