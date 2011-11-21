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

import geomobility.servlet.service.exception.RestfulException;

import java.util.Map;


/**
 * @author H&#7912;A PHAN Minh Hi&#7871;u (rockerhieu@gmail.com)
 */
public interface RestfulService {

    /**
     * Process a service request.
     *
     * @param params
     *            parameters passed in query string of the URL.
     * @param content
     *            body content of the request.
     * @return the response in textual content.
     *
     * @throws RestfulException
     */
    public String process(Map<String, String[]> params, String content)
            throws Exception, RestfulException;
}
