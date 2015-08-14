package com.ibm.docs.test.common;


import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolException;
import org.apache.http.client.protocol.RequestAcceptEncoding;
import org.apache.http.client.protocol.ResponseContentEncoding;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.protocol.HttpContext;

public class HttpClientFactory {
	
	static SSLSocketFactory sslSocketFactory = null;
	
	static {
		try {
			SSLContext ctx = SSLContext.getInstance("TLS");
			X509TrustManager tm = new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
				}
				public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
				}
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};
			ctx.init(null, new TrustManager[] { tm }, null);
			sslSocketFactory = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		} catch (Exception e) {

		}
	}

	public static DefaultHttpClient createTrustAllClient() {
		DefaultHttpClient client = new DefaultHttpClient();
		client.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, sslSocketFactory));
		client.addRequestInterceptor(new RequestAcceptEncoding());
		client.addResponseInterceptor(new ResponseContentEncoding());
		client.setRedirectStrategy(new DefaultRedirectStrategy() {
			@Override
			public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
				boolean isRedirected = super.isRedirected(request, response, context);
				if (!isRedirected) {
					int statusCode = response.getStatusLine().getStatusCode();
					isRedirected = statusCode == HttpStatus.SC_MOVED_TEMPORARILY || statusCode == HttpStatus.SC_MOVED_PERMANENTLY;
				}

				return isRedirected;
			}

		});
		return client;
	}
}
