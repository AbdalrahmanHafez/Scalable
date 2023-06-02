package app.media.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import app.media.controllers.MediaApplicationController;

@Component
public class CustomInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// Pre-processing logic
		// For example, you can perform authentication or modify the request
		System.out.println("[INFO] Prehandle");

		if (request.getRequestURI().equals("/continue")) {
			System.out.println("[INFO] Prehandle Continue request");
			return true;
		}

		return !MediaApplicationController.isPaused;
		// return true; // Proceed with the request handling
	}

	// @Override
	// public void postHandle(HttpServletRequest request, HttpServletResponse
	// response, Object handler,
	// ModelAndView modelAndView) throws Exception {
	// Post-processing logic
	// For example, you can modify the response or perform logging
	// System.out.println("[INFO] Posthandle");
	// }

}
