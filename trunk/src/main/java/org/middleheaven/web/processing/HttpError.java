package org.middleheaven.web.processing;

/**
 * Enum for HTTP 1.1 protocol commom response error codes (4xx and 5xx)
 * 
 * @see http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html
 */
public enum HttpError {
	
	/**
	 * The server has not found anything matching the Request-URI.
	 *  No indication is given of whether the condition is temporary or permanent. 
	 *  The 410 (Gone) status code SHOULD be used if the server knows, 
	 *  through some internally configurable mechanism, that an old resource is permanently 
	 *  unavailable and has no forwarding address. 
	 *  This status code is commonly used when the server does not wish to reveal exactly 
	 *  why the request has been refused, or when no other response is applicable. 
	 */
	NOT_FOUND(404), 
	
	/**
	* The server understood the request, but is refusing to fulfill it. 
	* Authorization will not help and the request SHOULD NOT be repeated. 
	* If the request method was not HEAD and the server wishes to make public why the request has not been fulfilled, 
	* it SHOULD describe the reason for the refusal in the entity. 
	* If the server does not wish to make this information available to the client, 
	* the status code 404 (Not Found) can be used instead.
	* */ 
	FORBIDDEN(403),
	
	/**
	 *  The requested resource is no longer available at the server and no forwarding address is known. 
	 *  This condition is expected to be considered permanent. 
	 *  Clients with link editing capabilities SHOULD delete references to the Request-URI after user approval. 
	 *  If the server does not know, or has no facility to determine, whether or not the condition is permanent, the status code 404 (Not Found) SHOULD be used instead. 
	 *  This response is cacheable unless indicated otherwise.
	 *  The 410 response is primarily intended to assist the task of web maintenance by notifying the recipient 
	 *  that the resource is intentionally unavailable and that the server owners desire that remote links to that
	 *   resource be removed. Such an event is common for limited-time, promotional services and for 
	 *   resources belonging to individuals no longer working at the server's site. 
	 *   It is not necessary to mark all permanently unavailable resources as "gone" or to keep the mark for any 
	 *   length of time -- that is left to the discretion of the server owner. 
	 */
	GONE(410),
	
	/**
	 * The server encountered an unexpected condition which prevented it from fulfilling the request. 
	 */
	INTERNAL_SERVER_ERROR(500),
	
	/**
	 * The server does not support the functionality required to fulfill the request. 
	 * This is the appropriate response when the server does not recognize the request 
	 * method and is not capable of supporting it for any resource. 
	 */
	NOT_IMPLEMENTED(501),
	
	
	/**
	 * The server, while acting as a gateway or proxy, 
	 * received an invalid response from the upstream server it accessed in attempting to fulfill the request. 
	 */
	BAD_GATEWAY(502),
	
	/**
	 * The server is currently unable to handle the request due to a temporary overloading or maintenance 
	 * of the server. 
	 * The implication is that this is a temporary condition which will be alleviated after some delay. 
	 * If known, the length of the delay MAY be indicated in a Retry-After header. 
	 * If no Retry-After is given, the client SHOULD handle the response as it would for a INTERNAL_SERVER_ERROR (500) response. 
	 */
	SERVICE_UNAVAILABLE(503);

	
	public static HttpError valueOf(int errorCode) {
		for (HttpError error : HttpError.values()){
			if (error.errorCode == errorCode){
				return error;
			}
		}
		return null;
	}
	
	
	private int errorCode;

	private HttpError(int errorCode){
		this.errorCode = errorCode;
	}
	
	public int errorCode(){
		return errorCode;
	}


}
