/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.training.gradebook.service.http;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.auth.HttpPrincipal;
import com.liferay.portal.kernel.service.http.TunnelUtil;
import com.liferay.portal.kernel.util.MethodHandler;
import com.liferay.portal.kernel.util.MethodKey;
import com.liferay.training.gradebook.service.AssignmentServiceUtil;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Provides the HTTP utility for the
 * <code>AssignmentServiceUtil</code> service
 * utility. The
 * static methods of this class calls the same methods of the service utility.
 * However, the signatures are different because it requires an additional
 * <code>HttpPrincipal</code> parameter.
 *
 * <p>
 * The benefits of using the HTTP utility is that it is fast and allows for
 * tunneling without the cost of serializing to text. The drawback is that it
 * only works with Java.
 * </p>
 *
 * <p>
 * Set the property <b>tunnel.servlet.hosts.allowed</b> in portal.properties to
 * configure security.
 * </p>
 *
 * <p>
 * The HTTP utility is only generated for remote services.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see AssignmentServiceSoap
 * @generated
 */
@ProviderType
public class AssignmentServiceHttp {

	public static com.liferay.training.gradebook.model.Assignment addAssignment(
			HttpPrincipal httpPrincipal,
			com.liferay.training.gradebook.model.Assignment assignment,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		try {
			MethodKey methodKey = new MethodKey(
				AssignmentServiceUtil.class, "addAssignment",
				_addAssignmentParameterTypes0);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, assignment, serviceContext);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof
						com.liferay.portal.kernel.exception.PortalException) {

					throw (com.liferay.portal.kernel.exception.PortalException)
						e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(
					e);
			}

			return (com.liferay.training.gradebook.model.Assignment)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static com.liferay.training.gradebook.model.Assignment
		createAssignment(HttpPrincipal httpPrincipal) {

		try {
			MethodKey methodKey = new MethodKey(
				AssignmentServiceUtil.class, "createAssignment",
				_createAssignmentParameterTypes1);

			MethodHandler methodHandler = new MethodHandler(methodKey);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				throw new com.liferay.portal.kernel.exception.SystemException(
					e);
			}

			return (com.liferay.training.gradebook.model.Assignment)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static com.liferay.training.gradebook.model.Assignment
			deleteAssignment(HttpPrincipal httpPrincipal, long assignmentId)
		throws com.liferay.portal.kernel.exception.PortalException {

		try {
			MethodKey methodKey = new MethodKey(
				AssignmentServiceUtil.class, "deleteAssignment",
				_deleteAssignmentParameterTypes2);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, assignmentId);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof
						com.liferay.portal.kernel.exception.PortalException) {

					throw (com.liferay.portal.kernel.exception.PortalException)
						e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(
					e);
			}

			return (com.liferay.training.gradebook.model.Assignment)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static com.liferay.training.gradebook.model.Assignment getAssignment(
			HttpPrincipal httpPrincipal, long assignmentId)
		throws com.liferay.portal.kernel.exception.PortalException {

		try {
			MethodKey methodKey = new MethodKey(
				AssignmentServiceUtil.class, "getAssignment",
				_getAssignmentParameterTypes3);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, assignmentId);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof
						com.liferay.portal.kernel.exception.PortalException) {

					throw (com.liferay.portal.kernel.exception.PortalException)
						e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(
					e);
			}

			return (com.liferay.training.gradebook.model.Assignment)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static java.util.List
		<com.liferay.training.gradebook.model.Assignment>
			getAssignmentsByKeywords(
				HttpPrincipal httpPrincipal, long groupId, String keywords,
				int start, int end, int status,
				com.liferay.portal.kernel.util.OrderByComparator
					<com.liferay.training.gradebook.model.Assignment>
						orderByComparator) {

		try {
			MethodKey methodKey = new MethodKey(
				AssignmentServiceUtil.class, "getAssignmentsByKeywords",
				_getAssignmentsByKeywordsParameterTypes4);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, groupId, keywords, start, end, status,
				orderByComparator);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				throw new com.liferay.portal.kernel.exception.SystemException(
					e);
			}

			return (java.util.List
				<com.liferay.training.gradebook.model.Assignment>)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static long getAssignmentsCountByKeywords(
		HttpPrincipal httpPrincipal, long groupId, String keywords,
		int status) {

		try {
			MethodKey methodKey = new MethodKey(
				AssignmentServiceUtil.class, "getAssignmentsCountByKeywords",
				_getAssignmentsCountByKeywordsParameterTypes5);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, groupId, keywords, status);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				throw new com.liferay.portal.kernel.exception.SystemException(
					e);
			}

			return ((Long)returnObj).longValue();
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static com.liferay.training.gradebook.model.Assignment
			updateAssignment(
				HttpPrincipal httpPrincipal,
				com.liferay.training.gradebook.model.Assignment assignment,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		try {
			MethodKey methodKey = new MethodKey(
				AssignmentServiceUtil.class, "updateAssignment",
				_updateAssignmentParameterTypes6);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, assignment, serviceContext);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof
						com.liferay.portal.kernel.exception.PortalException) {

					throw (com.liferay.portal.kernel.exception.PortalException)
						e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(
					e);
			}

			return (com.liferay.training.gradebook.model.Assignment)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	private static Log _log = LogFactoryUtil.getLog(
		AssignmentServiceHttp.class);

	private static final Class<?>[] _addAssignmentParameterTypes0 =
		new Class[] {
			com.liferay.training.gradebook.model.Assignment.class,
			com.liferay.portal.kernel.service.ServiceContext.class
		};
	private static final Class<?>[] _createAssignmentParameterTypes1 =
		new Class[] {};
	private static final Class<?>[] _deleteAssignmentParameterTypes2 =
		new Class[] {long.class};
	private static final Class<?>[] _getAssignmentParameterTypes3 =
		new Class[] {long.class};
	private static final Class<?>[] _getAssignmentsByKeywordsParameterTypes4 =
		new Class[] {
			long.class, String.class, int.class, int.class, int.class,
			com.liferay.portal.kernel.util.OrderByComparator.class
		};
	private static final Class<?>[]
		_getAssignmentsCountByKeywordsParameterTypes5 = new Class[] {
			long.class, String.class, int.class
		};
	private static final Class<?>[] _updateAssignmentParameterTypes6 =
		new Class[] {
			com.liferay.training.gradebook.model.Assignment.class,
			com.liferay.portal.kernel.service.ServiceContext.class
		};

}