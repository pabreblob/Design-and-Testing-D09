<%--
 * header.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<div>
	<a href="welcome/index.do"><img src="images/logo.png" alt="Acme-Rendezvous, Inc." /></a>
</div>

<div>
	<ul id="jMenu">
		<!-- Do not forget the "fNiv" class for the first level links !! -->
		
		
		<security:authorize access="hasRole('ADMIN')">
		
		<!-- Acciones de Administrator -->
			<li><a class="fNiv"><spring:message	code="master.page.administrator" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="admin/dashboard.do"><spring:message code="master.page.administrator.dashboard" /></a></li>
					<li><a href="rendezvous/administrator/list.do"><spring:message code="master.page.administrator.rendezvous.list" /></a></li>					
				</ul>
			</li>
		</security:authorize>
		
		<security:authorize access="hasRole('USER')">
		
		<!-- Acciones de User -->
			<li><a class="fNiv"><spring:message	code="master.page.rendezvous" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="rendezvous/list.do"><spring:message code="master.page.user.rendezvous.list"/></a></li>
					<li><a href="rendezvous/user/list-joined.do"><spring:message code="master.page.user.rendezvous.rsvpd" /></a></li>
					<li><a href="rendezvous/user/list.do"><spring:message code="master.page.user.rendezvous.created" /></a></li>
					<li><a href="rendezvous/user/create.do"><spring:message code="master.page.user.rendezvous.create" /></a></li>					
				</ul>
			</li>
		</security:authorize>
		
		<security:authorize access="isAnonymous()">
		
		<!-- Acciones de usuarios sin autenticar -->
			<li><a class="fNiv" href="security/login.do"><spring:message code="master.page.login" /></a></li>
			<li><a class="fNiv" href="user/create.do"><spring:message code="master.page.register" /></a></li>
			<li><a class="fNiv" href="user/list-all.do"><spring:message code="master.page.user.list"/></a></li>
			<li><a class="fNiv" href="rendezvous/list.do"><spring:message code="master.page.rendezvous.list"/></a></li>
		</security:authorize>
		
		<security:authorize access="isAuthenticated()">
		
		<!-- Acciones de usuarios autenticados -->
			<li>
				<a class="fNiv"> 
					<spring:message code="master.page.profile" /> 
			        (<security:authentication property="principal.username" />)
				</a>
				<ul>
				
				<security:authorize access="hasRole('USER')">
					<li><a href="user/user/display.do"><spring:message code="master.page.profile.display" /></a></li>					
				</security:authorize>
					
					<li><a href="j_spring_security_logout"><spring:message code="master.page.logout" /> </a></li>
				</ul>
			</li>
			<li><a class="fNiv" href="user/list-all.do"><spring:message code="master.page.user.list"/></a></li>
		</security:authorize>
		<li>
			<a class="fNiv" href="<spring:message code="master.page.language.url"/>"><spring:message code="master.page.language"/></a>
		</li>
	</ul>
</div>
