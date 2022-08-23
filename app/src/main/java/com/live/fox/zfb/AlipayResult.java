package com.live.fox.zfb;import android.text.TextUtils;public class AlipayResult {	private String userCd;	private String requestId;	private String resultStatus;	private AlipayResponse result;	private String memo;	public AlipayResult(String rawResult) {		if (TextUtils.isEmpty(rawResult))			return;		String[] resultParams = rawResult.split(";");		for (String resultParam : resultParams) {			if (resultParam.startsWith("resultStatus")) {				resultStatus = gatValue(resultParam, "resultStatus");			}			if (resultParam.startsWith("result")) {				result = new AlipayResponse(gatValue(resultParam, "result"));			}			if (resultParam.startsWith("memo")) {				memo = gatValue(resultParam, "memo");			}		}	}	public String getUserCd() {		return userCd;	}	public String getRequestId() {		return requestId;	}	public void setResultStatus(String resultStatus) {		this.resultStatus = resultStatus;	}	public void setResult(AlipayResponse result) {		this.result = result;	}	public void setMemo(String memo) {		this.memo = memo;	}	public void setUserCd(String userCd) {		this.userCd = userCd;	}	public void setRequestId(String requestId) {		this.requestId = requestId;	}	@Override	public String toString() {		return "resultStatus={" + resultStatus + "};memo={" + memo				+ "};result={" + result + "}";	}	private String gatValue(String content, String key) {		String prefix = key + "={";		return content.substring(content.indexOf(prefix) + prefix.length(),				content.lastIndexOf("}"));	}	/**	 * @return the resultStatus	 */	public String getResultStatus() {		return resultStatus;	}	/**	 * @return the memo	 */	public String getMemo() {		return memo;	}	/**	 * @return the result	 */	public AlipayResponse getResult() {		return result;	}}