package cn.com.unispark.task;

import java.io.IOException;

import javax.net.ssl.SSLHandshakeException;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

import android.text.format.DateUtils;

/**
 * Wrap of org.apache.http.impl.client.DefaultHttpClient
 * 
 * @author gaowen
 * 
 */
public class CustomHttpClient {

	private static DefaultHttpClient mClient;
	private static final int SECOND_IN_MILLIS = (int) DateUtils.SECOND_IN_MILLIS;
	public static final int RETRIED_TIME = 3;

	public CustomHttpClient() {
		prepareHttpClient();
	}

	/**
	 * 装配http的相关信息
	 * 
	 * @return
	 */

	public static synchronized HttpClient prepareHttpClient() {
		if (mClient == null) {
			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, "UTF-8");
			HttpProtocolParams.setUseExpectContinue(params, true);
			ConnManagerParams.setTimeout(params, 10 * SECOND_IN_MILLIS);
			HttpConnectionParams.setConnectionTimeout(params,
					20 * SECOND_IN_MILLIS);
			HttpConnectionParams.setSoTimeout(params, 20 * SECOND_IN_MILLIS);
			HttpConnectionParams.setSocketBufferSize(params, 8192);

			SchemeRegistry schemeRegistry = new SchemeRegistry();
			schemeRegistry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			schemeRegistry.register(new Scheme("https", SSLSocketFactory
					.getSocketFactory(), 443));
			ClientConnectionManager cm = new ThreadSafeClientConnManager(
					params, schemeRegistry);
			mClient = new DefaultHttpClient(cm, params);
		}
		return mClient;
	}

	public HttpResponse get(String url) throws HttpException {

		HttpResponse response = null;
		HttpGet method = new HttpGet(url);
		SetupHTTPConnectionParams(method);
		try {
			response = mClient.execute(method);
		} catch (ClientProtocolException e) {
			throw new HttpException(e.getMessage(), e);
		} catch (IOException e) {
			throw new HttpException(e.getMessage(), e);
		}

		return response;
	}

	/**
	 * Setup HTTPConncetionParams
	 * 
	 * @param method
	 */
	private void SetupHTTPConnectionParams(HttpUriRequest method) {
		HttpConnectionParams.setConnectionTimeout(method.getParams(),
				10 * SECOND_IN_MILLIS);
		HttpConnectionParams.setSoTimeout(method.getParams(),
				10 * SECOND_IN_MILLIS);
		mClient.setHttpRequestRetryHandler(requestRetryHandler);
	}

	/**
	 * 异常自动恢复处理, 使用HttpRequestRetryHandler接口实现请求的异常恢复
	 */
	private static HttpRequestRetryHandler requestRetryHandler = new HttpRequestRetryHandler() {
		// 自定义的恢复策略
		public boolean retryRequest(IOException exception, int executionCount,
				HttpContext context) {
			// 设置恢复策略，在发生异常时候将自动重试N次
			if (executionCount >= RETRIED_TIME) {
				// Do not retry if over max retry count
				return false;
			}
			if (exception instanceof NoHttpResponseException) {
				// Retry if the server dropped connection on us
				return true;
			}
			if (exception instanceof SSLHandshakeException) {
				// Do not retry on SSL handshake exception
				return false;
			}
			HttpRequest request = (HttpRequest) context
					.getAttribute(ExecutionContext.HTTP_REQUEST);
			boolean idempotent = (request instanceof HttpEntityEnclosingRequest);
			if (!idempotent) {
				// Retry if the request is considered idempotent
				return true;
			}
			return false;
		}
	};

}
