/**
 * Copyright 2010 BkitMobile
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package geomobility.servlet;

import geomobility.core.StackTraceUtil;
import geomobility.servlet.service.GatewayService;
import geomobility.servlet.service.GeocodeService;
import geomobility.servlet.service.HelloService;
import geomobility.servlet.service.POIService;
import geomobility.servlet.service.PresentationService;
import geomobility.servlet.service.ReGeocodeService;
import geomobility.servlet.service.SearchService;
import geomobility.servlet.service.exception.RestfulException;
import geomobility.servlet.service.exception.UndefinedServiceException;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class RestfulServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		process(req, resp, null);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		InputStream is = req.getInputStream();
		DataInputStream dis = new DataInputStream(is);
		String content = "";
		while (dis.available() > 0) {
			content += dis.readLine() + "\n";
		}
		process(req, resp, content);

		try {
			dis.close();
		} catch (Exception ex) {
		}
	}

	public void process(HttpServletRequest req, HttpServletResponse resp,
			String content) throws IOException {
		resp.setContentType("text/plain");
		resp.setCharacterEncoding("UTF-8");
		PrintWriter writer = resp.getWriter();

		try {
			String r;
			String serviceName = req.getParameter("service");
			Class<BaseRestfulService> service = services.get(serviceName);
			if (service == null)
				throw new UndefinedServiceException(serviceName);

			Map hashMap = new HashMap(req.getParameterMap());
			try {
				r = service.getConstructor(String.class).newInstance(serviceName)
				.process(hashMap, content);
			} catch (Exception e) {
				r = StackTraceUtil.getStackTrace(e);
			}

			// response
			writer.print(r);
		} catch (RestfulException ex) {
			writer.print(ex.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
			Arrays.toString(ex.getStackTrace());
			ex.printStackTrace(writer);
		}
	}

	public static Hashtable<String, Class> services = new Hashtable<String, Class>();
	static {
		services.put(ServiceNames.HELLO, HelloService.class);
		services.put(ServiceNames.GATEWAY, GatewayService.class);
		services.put(ServiceNames.POI, POIService.class);
		services.put(ServiceNames.GEOCODE, GeocodeService.class);
		services.put(ServiceNames.REGEOCODE, ReGeocodeService.class);
		services.put(ServiceNames.PRESENTATION, PresentationService.class);
		services.put(ServiceNames.SEARCH, SearchService.class);
	}
}
