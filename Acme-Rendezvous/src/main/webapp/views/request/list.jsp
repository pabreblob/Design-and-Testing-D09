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
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<display:table name="requests" id="r" requestURI="${requestURI}"
	pagesize="5" class="displaytag">
	

	<spring:message code="request.rendezvous" var="rendezHeader" />
	<display:column property="rendezvous.name" title="${rendezHeader}" />
	<spring:message code="request.comment" var="commentHeader" />
	<display:column property="comment" title="${commentHeader}" />
	<spring:message code="request.user" var="userHeader" />
	<display:column property="rendezvous.creator.name" title="${userHeader}" />
	
</display:table>