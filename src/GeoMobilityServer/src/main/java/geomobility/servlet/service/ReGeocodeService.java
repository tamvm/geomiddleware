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
package geomobility.servlet.service;

import geomobility.core.locutility.regeo.ReGeoController;
import geomobility.core.utils.StaticFunc;
import geomobility.servlet.BaseRestfulService;
import geomobility.servlet.ServiceNames;
import geomobility.servlet.service.exception.InvalidParameterException;

import java.util.Map;

/**
 * @author <a href="mailto:vo.mita.ov@gmail.com">VoMinhTam</a>
 */
public class ReGeocodeService extends BaseRestfulService {

	public ReGeocodeService(String name) {
		super(name);
		// mRequiredParameters = new String[] {"name"};
	}

	@Override
	public String process(Map<String, String[]> params, String content)
			throws Exception {
		if (StaticFunc.isNOE(content))
			throw new InvalidParameterException(ServiceNames.GATEWAY);
		String result = "";
		result = ReGeoController.doController(content);
		return result;
	}
}
