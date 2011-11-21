package geomobility.core.net;

import geomobility.core.utils.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/**
 * HTTP Request class
 * 
 * You can use this class and distribute it as long as you give proper credit
 * and place and leave this notice intact :). Check my blog for updated
 * version(s) of this class (http://moazzam-khan.com)
 * 
 * Usage Examples:
 * 
 * Get Request -------------------------------- HttpData data =
 * HttpRequest.get("http://example.com/index.php?user=hello");
 * System.out.println(data.content);
 * 
 * Post Request -------------------------------- HttpData data =
 * HttpRequest.post("http://xyz.com", "var1=val&var2=val2");
 * System.out.println(data.content); Enumeration<String> keys =
 * dat.cookies.keys(); // cookies while (keys.hasMoreElements()) {
 * System.out.println(keys.nextElement() + " = " +
 * data.cookies.get(keys.nextElement() + "rn"); } Enumeration<String> keys =
 * dat.headers.keys(); // headers while (keys.hasMoreElements()) {
 * System.out.println(keys.nextElement() + " = " +
 * data.headers.get(keys.nextElement() + "rn"); }
 * 
 * Upload a file -------------------------------- ArrayList<File> files = new
 * ArrayList(); files.add(new File("/etc/someFile")); files.add(new
 * File("/home/user/anotherFile"));
 * 
 * Hashtable<String, String> ht = new Hashtable<String, String>();
 * ht.put("var1", "val1");
 * 
 * HttpData data = HttpRequest.post("http://xyz.com", ht, files);
 * System.out.println(data.content);
 * 
 * @author Moazzam Khan
 * @author <a href="mailto:vo.mita.ov@gmail.com">VoMinhTam</a>
 * 
 *         Reference:
 *         http://www.hackdiary.com/2003/04/09/using-http-conditional-
 *         get-in-java-for-efficient-polling/
 */
public class HttpRequest {

	private static final String TAG = HttpRequest.class.getSimpleName();
	private static String etag = "";
	private static long lastModified = 0;

	/**
	 * HttpGet request
	 * 
	 * @param sUrl
	 * @return
	 * @throws IOException
	 */
	public static HttpData get(String sUrl, boolean isAlwaysGetNewData)
			throws IOException, ConnectException {
		HttpData ret = null;
		String str;
		StringBuffer buff = new StringBuffer();
		// try {
		sUrl = encodeString(sUrl);

		Log.d(TAG, "[URL] " + sUrl);

		URL url = new URL(sUrl);
		URLConnection con = url.openConnection();

		if (!isAlwaysGetNewData) {
			con.setRequestProperty("If-None-Match", etag);
			con.setIfModifiedSince(lastModified);
		}

		BufferedReader in = new BufferedReader(new

		InputStreamReader(con.getInputStream()));
		while ((str = in.readLine()) != null) {
			buff.append(str);
		}

		ret = new HttpData();
		ret.content = buff.toString();

		Log.d(TAG, "[Result] " + ret.content);

		if (con instanceof HttpURLConnection) {
			etag = con.getHeaderField("ETag");
			lastModified = con.getHeaderFieldDate("Last-Modified", 0);

			int responseCode = ((HttpURLConnection) con).getResponseCode();
			ret.responseCode = responseCode;
			if (responseCode == HttpURLConnection.HTTP_OK) {
				ret.isChange = true;
				Map<String, List<String>> headers = con.getHeaderFields();
				Set<Entry<String, List<String>>> hKeys = headers.entrySet();
				for (Iterator<Entry<String, List<String>>> i = hKeys.iterator(); i
						.hasNext();) {
					Entry<String, List<String>> m = i.next();

					if (m.getKey() == null)
						continue;

					ret.headers.put(m.getKey(), m.getValue().toString());
					if (m.getKey().equals("set-cookie"))
						ret.cookies.put(m.getKey(), m.getValue().toString());
				}
			} else {
				ret.isChange = false;
			}

			return ret;
		}
		return null;
	}

	/**
	 * HTTP post request
	 * 
	 * @param sUrl
	 * @param ht
	 * @return
	 * @throws Exception
	 */
	public static HttpData post(String sUrl, Hashtable<String, String> ht)
			throws Exception {
		String key;
		StringBuffer data = new StringBuffer();
		Enumeration<String> keys = ht.keys();
		while (keys.hasMoreElements()) {
			key = keys.nextElement();
			data.append(URLEncoder.encode(key, "UTF-8"));
			data.append("=");
			data.append(URLEncoder.encode(ht.get(key), "UTF-8"));
			data.append("&amp;");
		}
		return HttpRequest.post(sUrl, data.toString());
	}

	/**
	 * HTTP post request
	 * 
	 * @param sUrl
	 * @param data
	 * @return
	 */
	public static HttpData post(String sUrl, String data) {
		StringBuffer ret = new StringBuffer();
		HttpData dat = new HttpData();
		String header;
		try {
			// Send data
			URL url = new URL(sUrl);
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(
					conn.getOutputStream());
			wr.write(data);
			wr.flush();

			// Get the response
			// Log.d(HttpRequest.class.toString() + " - URLConnection",
			// conn.getInputStream());
			Map<String, List<String>> headers = conn.getHeaderFields();
			Set<Entry<String, List<String>>> hKeys = headers.entrySet();
			for (Iterator<Entry<String, List<String>>> i = hKeys.iterator(); i
					.hasNext();) {
				Entry<String, List<String>> m = i.next();

				dat.headers.put(m.getKey(), m.getValue().toString());
				if (m.getKey().equals("set-cookie"))
					dat.cookies.put(m.getKey(), m.getValue().toString());
			}
			BufferedReader rd = new BufferedReader(new

			InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				ret.append(line);
			}
			System.out.println("Line: " + line);
			wr.close();
			rd.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		dat.content = ret.toString();
		return dat;
	}

	// For Communicate with GeoServer
	public static HttpData postXML(String sUrl, String data) throws IOException, ConnectException{
		StringBuffer ret = new StringBuffer();
		HttpData dat = new HttpData();
		String header;
		try {
			// Send data
			URL url = new URL(sUrl);
			URLConnection conn = url.openConnection();
			((HttpURLConnection) conn).setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestProperty("Content-Type", "text/xml");
			conn.setRequestProperty("Content-Length", "" + data.length());
			OutputStreamWriter wr = new OutputStreamWriter(
					conn.getOutputStream());
			wr.write(data);
			wr.flush();

			BufferedReader rd = new BufferedReader(new

			InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				ret.append(line);
				ret.append("\n");
			}
			ret.deleteCharAt(ret.length()-1);

			Log.d(HttpRequest.class.toString() + " - URLConnection",
					conn.getInputStream() + " content:"
							+ conn.getContent().toString() + " headerFields:"
							+ conn.getHeaderFields().toString());

			// Get the response

			Map<String, List<String>> headers = conn.getHeaderFields();
			Set<Entry<String, List<String>>> hKeys = headers.entrySet();
			for (Iterator<Entry<String, List<String>>> i = hKeys.iterator(); i
					.hasNext();) {
				Entry<String, List<String>> m = i.next();
				
				if(m.getKey()!= null)
					dat.headers.put(m.getKey(), m.getValue().toString());
				else
					dat.headers.put("Status", m.getValue().toString());
//				if (m.getKey().equals("set-cookie"))
//					dat.cookies.put(m.getKey(), m.getValue().toString());

			}

			wr.close();
			rd.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		dat.content = ret.toString();
		return dat;
	}

	public static String postString(String urlStr, String content) {
		try {
			// Send data
			Log.d(TAG, "[URL] " + urlStr);
			URL url = new URL(urlStr);
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(
					conn.getOutputStream());
			wr.write(content);
			wr.flush();

			// Get the response
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String line;
			StringBuilder strBuilder = new StringBuilder();
			while ((line = rd.readLine()) != null) {
				strBuilder.append(line);
			}

			wr.close();
			rd.close();

			return strBuilder.toString();
		} catch (Exception e) {
		}

		return null;
	}

	// public static String postXML(String urlStr, String content) {
	// try {
	// // Send data
	// Log.d(TAG, "[URL] " + urlStr);
	// URL url = new URL(urlStr);
	// URLConnection conn = url.openConnection();
	// conn = url.openConnection();
	// ((HttpURLConnection)conn).setRequestMethod("POST");
	// conn.setDoInput(true);
	// conn.setDoOutput(true);
	// conn.setUseCaches(false);
	// conn.setRequestProperty("Content-Type", "text/xml");
	// conn.setRequestProperty("Content-Length", ""+ content.length());
	// OutputStreamWriter wr = new OutputStreamWriter(conn
	// .getOutputStream());
	// wr.write(content);
	// wr.flush();
	//
	// // Get the response
	// BufferedReader rd = new BufferedReader(new InputStreamReader(conn
	// .getInputStream()));
	// String line;
	// StringBuilder strBuilder = new StringBuilder();
	// while ((line = rd.readLine()) != null) {
	// strBuilder.append(line);
	// }
	//
	// wr.close();
	// rd.close();
	//
	// return strBuilder.toString();
	// } catch (Exception e) {
	// }
	//
	// return null;
	// }

	public static String encodeString(String str) {
		return str.replaceAll(" ", "+");
	}

	/**
	 * Post request (upload files)
	 * 
	 * @param sUrl
	 * @param files
	 * @return HttpData
	 */
	/*
	 * public static HttpData post(String sUrl, ArrayList<File> files) {
	 * Hashtable<String, String> ht = new Hashtable<String, String>(); return
	 * HttpRequest.post(sUrl, ht, files); }
	 */
	/**
	 * Post request (upload files)
	 * 
	 * @param sUrl
	 * @param params
	 *            Form data
	 * @param files
	 * @return
	 */
	/*
	 * public static HttpData post(String sUrl, Hashtable<String, String>
	 * params, ArrayList<File>
	 * 
	 * files) { HttpData ret = new HttpData(); try { String boundary =
	 * "*****************************************"; String newLine = "rn"; int
	 * bytesAvailable; int bufferSize; int maxBufferSize = 4096; int bytesRead;
	 * 
	 * URL url = new URL(sUrl); HttpURLConnection con = (HttpURLConnection)
	 * url.openConnection(); con.setDoInput(true); con.setDoOutput(true);
	 * con.setUseCaches(false); con.setRequestMethod("POST");
	 * con.setRequestProperty("Connection", "Keep-Alive");
	 * con.setRequestProperty("Content-Type",
	 * 
	 * "multipart/form-data;boundary="+boundary); DataOutputStream dos = new
	 * DataOutputStream(con.getOutputStream());
	 * 
	 * //dos.writeChars(params);
	 * 
	 * //upload files for (int i=0; i<files.size(); i++) { Log.i("HREQ", i+"");
	 * FileInputStream fis = new FileInputStream(files.get(i));
	 * dos.writeBytes("--" + boundary + newLine);
	 * dos.writeBytes("Content-Disposition: form-data; " +
	 * "name="file_"+i+"";filename="" + files.get(i).getPath() +""" + newLine +
	 * newLine); bytesAvailable = fis.available(); bufferSize =
	 * Math.min(bytesAvailable, maxBufferSize); byte[] buffer = new
	 * byte[bufferSize]; bytesRead = fis.read(buffer, 0, bufferSize); while
	 * (bytesRead > 0) { dos.write(buffer, 0, bufferSize); bytesAvailable =
	 * fis.available(); bufferSize = Math.min(bytesAvailable, maxBufferSize);
	 * bytesRead = fis.read(buffer, 0, bufferSize); } dos.writeBytes(newLine);
	 * dos.writeBytes("--" + boundary + "--" + newLine); fis.close(); } // Now
	 * write the data
	 * 
	 * Enumeration keys = params.keys(); String key, val; while
	 * (keys.hasMoreElements()) { key = keys.nextElement().toString(); val =
	 * params.get(key); dos.writeBytes("--" + boundary + newLine);
	 * dos.writeBytes("Content-Disposition: form-data;name="" + key+""" +
	 * newLine + newLine + val); dos.writeBytes(newLine); dos.writeBytes("--" +
	 * boundary + "--" + newLine);
	 * 
	 * } dos.flush();
	 * 
	 * BufferedReader rd = new BufferedReader( new
	 * InputStreamReader(con.getInputStream())); String line; while ((line =
	 * rd.readLine()) != null) { ret.content += line + "rn"; } //get headers
	 * Map<String, List<String>> headers = con.getHeaderFields();
	 * Set<Entry<String, List<String>>> hKeys = headers.entrySet(); for
	 * (Iterator<Entry<String, List<String>>> i = hKeys.iterator();
	 * i.hasNext();) { Entry<String, List<String>> m = i.next();
	 * 
	 * Log.w("HEADER_KEY", m.getKey() + ""); ret.headers.put(m.getKey(),
	 * m.getValue().toString()); if (m.getKey().equals("set-cookie"))
	 * ret.cookies.put(m.getKey(), m.getValue().toString()); } dos.close();
	 * rd.close(); } catch (MalformedURLException me) {
	 * 
	 * } catch (IOException ie) {
	 * 
	 * } catch (Exception e) { Log.e("HREQ", "Exception: "+e.toString()); }
	 * return ret; }
	 */
}
