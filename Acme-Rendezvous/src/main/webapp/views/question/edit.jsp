<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="question/user/edit.do?rendezvousId=${rendezvous.id}" modelAttribute="question">
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<acme:textbox code="question.question" path="text"/>
	<acme:submit name="submit" code="question.submit"/>
	<acme:cancel url="question/user/list.do?rendezvousId=${rendezvous.id}" code="question.cancel"/>
</form:form>