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

package com.liferay.training.gradebook.web.view;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import javax.inject.Inject;
import javax.inject.Named;

import javax.servlet.ServletContext;

/**
 * This producer makes it possible to access the {@link ServletContext} via the "application" EL keyword.
 *
 * @author Neil Griffin
 */
@ApplicationScoped
public class ApplicationProducer {

	@ApplicationScoped
	@Named
	@Produces
	public ServletContext getApplication() {
		return _servletContext;
	}

	@Inject
	private ServletContext _servletContext;

}