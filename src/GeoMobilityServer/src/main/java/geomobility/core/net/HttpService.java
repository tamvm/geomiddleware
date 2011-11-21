/**
 * Copyright 2010 Hieu Rocker & Tien Trum
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package geomobility.core.net;

import geomobility.core.utils.Log;

/**
 * Contains some stuffs for sending <code>HTTP</code> request.
 *
 * @author H&#7912;A PHAN Minh Hi&#7871;u (rockerhieu@gmail.com)
 */
public class HttpService {

    /**
     * Constructs a {@link HttpService} instance.
     */
    public HttpService() {
    }

    /**
     * Send a <code>HTTP GET</code> to <code>url</code> and retrieve the string
     * content.
     *
     * @param url
     *            URL of the webpage you want to retrieve content.
     * @param blocking
     *            Indicate <code>HTTP GET</code> will be sent synchronous or
     *            asynchronous. Pass <code>true</code> if want to send
     *            <code>HTTP GET</code> in the same thread of caller, otherwise
     *            pass <code>false</code>.
     * @param callback
     *            A callback instance that is being called as soon as the remote
     *            service returns the result (a String object contains textual
     *            content) of this method invocation.
     */
    public static void getResource(String url, final boolean blocking,final boolean isAlwaysGetNewData, 
            final ServiceCallback<HttpData> callback) {
//    	final String sUrl = Global.encodeString(url);
    	final String sUrl = url;
        Log.d("URL",sUrl);
        try {
            if (blocking) {
                callback.onSuccess(HttpRequest.get(sUrl, isAlwaysGetNewData));
            } else {
                ThreadPool.getInstance().execute(new TaskPool() {
                    public void run() {
                        try {
                            callback.onSuccess(HttpRequest.get(sUrl, isAlwaysGetNewData));
                        } catch (Exception ex) {
                            callback.onFailure(ex);
                        }
                    };
                }, null);
            }
        } catch (Exception ex) {
            callback.onFailure(ex);
        }
    }

    /**
     * Get the textual content of a webpage through <code>HTTP GET</code>.
     * @deprecated
     */
//    private static String getContent(String urlResource) {
//        String result = "";
//        try {
//            URL url = new URL(urlResource);
//            URLConnection con = url.openConnection();
//            BufferedReader rd = new BufferedReader(new InputStreamReader(con
//                    .getInputStream()));
//            String line;
//            while ((line = rd.readLine()) != null) {
//                result += line + "\n";
//            }
//            rd.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result.trim();
//    }
}
