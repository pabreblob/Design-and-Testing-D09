<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form action="answer/user/answers.do?rendezvousId=${rendezId}" method="post">
	<jstl:forEach var="row" items="${questions}">
		<h3><jstl:out value="${row.text}"/></h3>
		<input name="${row.id}" required/><br>
	</jstl:forEach>
	<input type="submit" value="<spring:message code="answer.submit"/>" name="submit"/>
	<acme:cancel url="rendezvous/list.do" code="answer.cancel"/>	
</form>

<jstl:if test="${messageError}"> 
	<p style="color:red;"><spring:message code="answer.error"/></p>
</jstl:if>