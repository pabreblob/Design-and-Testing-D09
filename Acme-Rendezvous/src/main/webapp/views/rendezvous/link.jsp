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

<form:form action="rendezvous/user/link.do" modelAttribute="rendezvous">
	<form:hidden path="id" />
	<form:hidden path="version" />


	<%-- <form:hidden path="creator" />
	<form:hidden path="attendants" />
	<form:hidden path="announcements" />
	<form:hidden path="questions" />
	<form:hidden path="comments" />
	<form:hidden path="deleted" />
	<form:hidden path="adultContent" />
	<form:hidden path="name" />
	<form:hidden path="description" />
	<form:hidden path="moment" />
	<form:hidden path="pictureURL" />
	<form:hidden path="gpsCoordinates.latitude" />
	<form:hidden path="gpsCoordinates.longitude" />
	<form:hidden path="finalMode" /> --%>

	<acme:select items="${rendezvouses }" itemLabel="name" code="rendez.linked" path="linkedRendezvous"/><br />

	<acme:submit name="save" code="rendez.save"/>
	<acme:cancel url="rendezvous/user/list.do" code="rendez.cancel"/>
</form:form>



