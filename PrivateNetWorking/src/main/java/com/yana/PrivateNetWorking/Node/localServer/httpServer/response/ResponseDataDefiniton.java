package com.yana.PrivateNetWorking.Node.localServer.httpServer.response;

public class ResponseDataDefiniton {
	public enum CONTENT_TYPE {
		TEXT {
			@Override
			public String getContentType() {
				return "text/plain; charset=UTF-8";
			}
		},
		HTML {
			@Override
			public String getContentType() {
				return "text/html; charset=UTF-8";
			}
		},
		JAVASCRIPT {
			@Override
			public String getContentType() {
				return "text/javascript; charset=UTF-8";
			}
		},
		CSS {
			@Override
			public String getContentType() {
				return "text/css; charset=UTF-8";
			}
		},
		JSON {
			@Override
			public String getContentType() {
				//default is utf8
				return "application/json";
			}
		};
		abstract String getContentType();
	}
}
