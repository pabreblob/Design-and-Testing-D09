<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<jstl:if test="${rendezvous.finalMode == false and rendezvous.deleted == false}">
	<a href="question/user/create.do?rendezvousId=${rendezvous.id}"><spring:message code="question.new"/></a><br>
</jstl:if>

<display:table class="displaytag" name="rendezvous.questions" id="row" requestURI="question/user/list.do?rendezvousId=${rendezvous.id}" pagesize="5">	
	<spring:message code="question.question" var="questionHeader" />
	<display:column property="text" title="${questionHeader}" sortable="false"/>

	<jstl:if test="${rendezvous.finalMode == false and rendezvous.deleted == false}">	
		<display:column>
			<a href="question/user/edit.do?questionId=${row.id}&rendezvousId=${rendezvous.id}"><spring:message code="question.edit"/></a>
		</display:column>
	
		<display:column>
			<a href="question/user/delete.do?questionId=${row.id}&rendezvousId=${rendezvous.id}"><spring:message code="question.delete"/></a>
		</display:column>
	</jstl:if>
		
</display:table>