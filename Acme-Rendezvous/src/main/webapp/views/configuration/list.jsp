<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<display:table class="displaytag" name="config" id="row">	
	<spring:message code="configuration.businessName" var="businessName" />
	<display:column property="businessName" title="${businessName}"/>
	
	<spring:message code="configuration.banner" var="banner" />
	<display:column property="bannerUrl" title="${banner}"/>
	
	<spring:message code="configuration.welcomeEng" var="welcomeEng" />
	<display:column property="welcomeEng" title="${welcomeEng}"/>
	
	<spring:message code="configuration.welcomeEsp" var="welcomeEsp" />
	<display:column property="welcomeEsp" title="${welcomeEsp}"/>
	
</display:table>