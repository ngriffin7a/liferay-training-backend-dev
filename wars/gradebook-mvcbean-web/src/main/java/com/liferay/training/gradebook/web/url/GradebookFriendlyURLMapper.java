/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.liferay.training.gradebook.web.url;

import com.liferay.portal.kernel.portlet.DefaultFriendlyURLMapper;
import com.liferay.portal.kernel.portlet.FriendlyURLMapper;
import com.liferay.training.gradebook.web.annotation.ServiceFriendlyURLMapper;
import com.liferay.training.gradebook.web.annotation.ServicePortlet;
import com.liferay.training.gradebook.web.controller.GradebookPortlet;

import javax.enterprise.context.ApplicationScoped;

import org.osgi.service.cdi.annotations.Service;

/**
 * @author Neil Griffin
 */
@ApplicationScoped
@Service(FriendlyURLMapper.class)
@ServiceFriendlyURLMapper(
	friendly$_$url$_$routes = "/META-INF/friendly-url-routes.xml"
)
@ServicePortlet(name = GradebookPortlet.NAMESPACE)
public class GradebookFriendlyURLMapper extends DefaultFriendlyURLMapper {

	@Override
	public String getMapping() {
		return "gradebook";
	}

}