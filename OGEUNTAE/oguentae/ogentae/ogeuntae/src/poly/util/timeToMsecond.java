package poly.util;

import java.text.SimpleDateFormat;
import java.util.Date;


// 밀리 세컨드 구하는 메서드 시간 분 나눠서 구함
public class timeToMsecond {
	
	public static int timeToMilSecond(String checkTime) {
		//날짜 객체 생성
		Date today = new Date();
		// 시간만 가져오기 위한 포맷팅 객체 생성
		SimpleDateFormat date = new SimpleDateFormat("HH:mm");
		String [] tempStr = null; // 문자열 갈라서 임시 저장할 변수
		// 현재 시간 포맷팅
		String nowTime = date.format(today);
		// 시와 분을 나눔
		tempStr = nowTime.split("\\:");
		// 문자열에서 정수형 변환
		int nowHour = Integer.parseInt(tempStr[0]);
		int nowMin = Integer.parseInt(tempStr[1]);
		//초로 변환
		int nowSeconds = ((nowHour * 60 * 60) + (nowMin * 60));
		
		tempStr = null;
		
		tempStr = checkTime.split("\\:", 2);
		int fuHour = Integer.parseInt(tempStr[0]);
		int fuMin = Integer.parseInt(tempStr[1]);
		int fuSeconds = ((fuHour * 60 * 60) + (fuMin * 60));
		
		tempStr = null;
		
		int milSeconds = ((fuSeconds - nowSeconds) *1000); 
		// int milSeconds = ((fuSeconds - nowSeconds) / 60); 
		
		System.out.println("ms : " + milSeconds);
		
		return milSeconds;
	}

}
