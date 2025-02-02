package poly.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import poly.service.IMongoTestService;

// 어노테이션 컨트롤러 선언해야지 spring에서 컨트롤러로 자동인식함 자바 서블릿 역할
@Controller
public class mongoTestContoller {
	private Logger log = Logger.getLogger(this.getClass());
	
	@Resource(name = "MongoTestService")
	private IMongoTestService mongoTestService;
	
	@RequestMapping(value = "mongo/test")
	@ResponseBody
	public String test(HttpServletRequest request, HttpServletResponse response) throws Exception{
		log.info(this.getClass().getName() + ".test Start!!");
		mongoTestService.createColection();
		log.info(this.getClass().getName() + ".test END!!");
		return "succes";
	}
	
}
