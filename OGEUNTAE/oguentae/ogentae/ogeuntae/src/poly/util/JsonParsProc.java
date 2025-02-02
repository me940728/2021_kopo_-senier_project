package poly.util;

// 정규표현식
/*
 * [] 안에 들어 간 특수문자는 or 처리한다.
 * .이나 , 같은 특수문자는 역슬레쉬 \ 해줘야 인식 한다.
 * \\s+ 는 공백을 의미
 * */
public class JsonParsProc {

	// JSON 받아서 처리하기 쉽게 가공해주는 메서드
	public static String[] JsonParsSplit(String str) {
		// System.out.println("str : " + str);
		// 정규식 사용해서 특수문자 처리 { or } or 공백 or " 제거
		String replaceJson = str.replaceAll("[{}\\s+\"]", "");
		// 키 : 값 형태로 된 문자열을 , 기준으로 다시 나워서 담음
		String[] empTime = replaceJson.split("\\,");
		
		for (int i = 0; i < empTime.length; i++) {
			System.out.println("empTime : " + empTime[i]);
		}
		
		return empTime;
	}
	// 지각 유무 확인 메서드 
	// 지금 시로만 지각 유무 정하는데 분까지 합쳐서 지각 유무 계산히기
	public static String lateCheck(String pEmpTime, String pLateTime) {
		
		String time = pEmpTime;
		String lateTime = pLateTime;
		String res;
		String[] resTime = time.split("\\_"); // 세번째 인자에 시간 들어 있음
		String [] resLateTime = lateTime.split("\\:"); // 세번째 인자에 시간이 들어 있음
		String temp = resTime[3]; // 세번째 인자 값 가져와서 임시 변수에 저장

		resTime = null;
		resTime = temp.split("\\:");
		// 종료 시간
		int hour = Integer.parseInt(resTime[0]);
		int min = Integer.parseInt(resTime[1]);
		
		System.out.println("resLateTime[0] : " + resLateTime[0]);
		
		if(hour <= Integer.parseInt(resLateTime[0])) {
			res = "정상출석";
		} else {
			res = "지각";
		}
		return res;
	}
}
