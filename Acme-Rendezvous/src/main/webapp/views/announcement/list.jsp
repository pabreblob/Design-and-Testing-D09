<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<display:table class="displaytag" name="announcements" id="row" requestURI="announcement/user/list.do" pagesize="5">	
	<spring:message code="announcement.title" var="titleHeader" />
	<display:column property="title" title="${titleHeader}"/>
	<spring:message code="announcement.description" var="descriptionHeader" />
	<display:column property="description" title="${descriptionHeader}"/>
	<spring:message code="announcement.moment" var="momentHeader" />
	<spring:message code="announcement.date.format" var="dateFormat" />
	<display:column title="${momentHeader}">
		<fmt:formatDate value="${row.moment}" pattern="${dateFormat}"/>
	</display:column>

	<security:authorize access="hasRole('ADMIN')">	
		<display:column>
			<a href="announcement/admin/delete.do?announcementId=${row.id}"><spring:message code="announcement.delete"/></a>
		</display:column>
	</security:authorize>
		
</display:table>