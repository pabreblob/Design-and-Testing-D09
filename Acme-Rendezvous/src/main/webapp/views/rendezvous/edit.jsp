<%--
 * action-2.jsp
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
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="rendezvous/user/edit.do" modelAttribute="rendezvous">
	<form:hidden path="id" />
	<form:hidden path="version" />


	<%-- <form:hidden path="creator" />
	<form:hidden path="attendants" />
	<form:hidden path="linkedRendezvous" />
	<form:hidden path="announcements" />
	<form:hidden path="questions" />
	<form:hidden path="comments" />
	<form:hidden path="deleted" /> --%>
	
	<jstl:if test="${not adult }">
	<form:hidden path="adultContent" />
	</jstl:if>


	<acme:textbox code="rendez.name" path="name" /><br />
	<acme:textarea code="rendez.description" path="description" /><br />
	<spring:message code="rendez.date.placeholder" var="dateplaceholder" />
	<acme:textbox code="rendez.date" path="moment" placeholder='${dateplaceholder}' /><br />
	<acme:textbox code="rendez.picture" path="pictureURL" placeholder ='http://www.image.com/imageId=25'/><br />
	<acme:textbox code="rendez.latitude" path="gpsCoordinates.latitude" /><br />
	<acme:textbox code="rendez.longitude" path="gpsCoordinates.longitude" /><br />
	<acme:checkbox code="rendez.isFinalVer.status" path="finalMode"/><br />
	
	<jstl:if test = "${adult }">
	<acme:checkbox code="rendez.adultContent" path="adultContent"/><br/>
	</jstl:if>

	<jstl:if test="${rendezvous.deleted == false }">
	<acme:submit name="save" code="rendez.save"  />
	</jstl:if>

	<jstl:if test="${rendezvous.id!= 0 }">
	<acme:submit name="cancel" code="rendez.delete"/>
	</jstl:if>
	<acme:cancel code="rendez.cancel" url="rendezvous/user/list.do" /><br />	
</form:form>



