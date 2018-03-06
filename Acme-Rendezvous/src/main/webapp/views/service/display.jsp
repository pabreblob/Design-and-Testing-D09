<%--
 * action-1.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

	
	<div>
	<p>
	<spring:message code="service.name"/>: <jstl:out value="${service.name}"/>
	</p>
	</div>
	
	<div>
	<p>
	<spring:message code="service.description"/>: <jstl:out value="${service.description}"/>
	</p>
	</div>
		
	<div>
	<p>
	<spring:message code="service.price"/>: <jstl:out value="${service.price}"/>
	</p>
	</div>
	
	
	<jstl:if test="${pictureSize > 0}">
	<div>
	<img alt="service.pictureUrl" src="<jstl:out value="${service.pictureUrl}"/>">
	</div>
	</jstl:if>
	
	
	
	
