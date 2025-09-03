package com.yana.PrivateNetWorking.Node.localServer.router;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

import com.yana.PrivateNetWorking.Node.localServer.controller.AbstractController;
import com.yana.PrivateNetWorking.Node.localServer.httpServer.requet.RequestData;
import com.yana.PrivateNetWorking.Node.localServer.httpServer.requet.RequestDataDefnition.METHOD;
import com.yana.PrivateNetWorking.Node.localServer.httpServer.response.ResponseData;

public class Routing {
	private static final Pattern CSS_FILE_PATTERN = Pattern.compile("\\.css$");
	private static final Pattern JS_FILE_PATTERN = Pattern.compile("\\.js$");
	public static void setUp() {
		RoutingMap.init();
	}

	public static ResponseData invoke(RequestData requestData) {
		METHOD method = requestData.getRequestMethod();
		String requestPath = requestData.getRequestPath();
		String targetActionCode = getTargetActionCode(method, requestPath);
		if(targetActionCode == null) {
			// NG Action
			return null;
		}
		int separateMethod = targetActionCode.lastIndexOf(".");
		String targetClassName = targetActionCode.substring(0, separateMethod);
		String targetMethodName = targetActionCode.substring(separateMethod + 1);
		try {
			Class<?> clazz = Class.forName(targetClassName);
			Method targetMethod = null;
			for(Method m : clazz.getMethods()) {
				if(m.getName().equals(targetMethodName)) {
					targetMethod = m;
					break;
				}
			}
			if(targetMethod == null) {
				// NG Action
				return null;
			}
			Object tmp = clazz.newInstance();
			if(!(tmp instanceof AbstractController)) {
				// NG Action
				return null;
			}
			AbstractController controllerObj = (AbstractController)tmp;
			controllerObj.setRequestData(requestData);
			Object retObject = targetMethod.invoke(controllerObj);
			if(!(retObject instanceof ResponseData)) {
				return null;
			}
			return (ResponseData)retObject;
		} catch(ClassNotFoundException e) {
			// NG Action
			return null;
		} catch(InstantiationException e) {
			// NG Action
			return null;
		} catch(IllegalAccessException e) {
			// NG Action
			return null;
		} catch(InvocationTargetException e) {
			// NG Action
			return null;
		}
	}

	private static String getTargetActionCode(METHOD method, String requestPath) {
		if(CSS_FILE_PATTERN.matcher(requestPath).find()) {
			requestPath = "/*.css";
		}
		if(JS_FILE_PATTERN.matcher(requestPath).find()) {
			requestPath = "/*.js";
		}
		return RoutingMap.getValue(method, requestPath);
	}
}
