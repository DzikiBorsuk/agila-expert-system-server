package uni.tukl.cs.cps.agilaserver.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import uni.tukl.cs.cps.agilaserver.models.login.LoginModel;

@Controller
public class AgilaController {

	@RequestMapping("/")
	public ModelAndView index() {
		ModelAndView mav = new ModelAndView("index");
		mav.addObject("index", new LoginModel());
		return mav;
	}

	@RequestMapping("/login")
	public ModelAndView login() {
		ModelAndView mav = new ModelAndView("login");
		mav.addObject("login", new LoginModel());
		return mav;
	}

	@RequestMapping("/register")
	public ModelAndView register() {
		ModelAndView mav = new ModelAndView("login");
		mav.addObject("register", new LoginModel());
		return mav;
	}

	@RequestMapping("/sign-up")
	public ModelAndView signUp() {
		ModelAndView mav = new ModelAndView("login");
		mav.addObject("sign-up", new LoginModel());
		return mav;
	}

}
