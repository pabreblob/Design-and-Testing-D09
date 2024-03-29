<%--
 * header.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<style type="text/css">
	.headerImg{
	height: auto;
	width: 100%;
	max-width: 955px; 
	}
	
	
	.divHeader{
	
	text-align: center;
	}
</style>
<div class="divHeader">
	<a class="headerEnlace" href="welcome/index.do"><img class="headerImg" src='<jstl:out value="${banner}"/>' alt='<jstl:out value="${footer}"/>' /></a>
</div>

<div id="menuP">
	<ul id="jMenu">
		<!-- Do not forget the "fNiv" class for the first level links !! -->
		
		
		<security:authorize access="hasRole('ADMIN')">
		
		<!-- Acciones de Administrador -->
			<li><a class="fNiv"><spring:message	code="master.page.administrator" /></a>
				<ul>
					<li class="arrow"/>
					<li><a href="admin/dashboard.do"><spring:message code="master.page.administrator.dashboard" /></a></li>
					<li><a href="rendezvous/admin/list.do"><spring:message code="master.page.administrator.rendezvous.list" /></a></li>
					<li><a href="configuration/admin/list.do"><spring:message code="master.page.administrator.configuration"/></a></li>
					<li><a href="service/admin/list.do"><spring:message code="master.page.administrator.services"/></a></li>
					<li><a href="category/list.do"><spring:message code="master.page.administrator.category.list"/></a></li>
				</ul>
			</li>
		</security:authorize>
		
		<security:authorize access="hasRole('USER')">
		
		<!-- Acciones de User -->
			<li><a class="fNiv"><spring:message	code="master.page.rendezvous" /></a>
				<ul>
					<li class="arrow"/>
					<li><a href="rendezvous/list.do"><spring:message code="master.page.user.rendezvous.list"/></a></li>
					<li><a href="rendezvous/user/list-joined.do"><spring:message code="master.page.user.rendezvous.rsvpd" /></a></li>
					<li><a href="rendezvous/user/list.do"><spring:message code="master.page.user.rendezvous.created" /></a></li>
					<li><a href="rendezvous/user/create.do"><spring:message code="master.page.user.rendezvous.create" /></a></li>			
				</ul>
			</li>
			
			<li><a class="fNiv"> <spring:message code="master.page.services" /></a>
				<ul>	
					<li><a href="category/list.do"><spring:message code="master.page.category.list"/></a></li>
					<li><a href="service/user/list.do"><spring:message code="master.page.user.service.list"/></a></li>		
				</ul>
			</li>	
		</security:authorize>
		
		<security:authorize access="hasRole('MANAGER')">
		
		<!-- Acciones de Manager -->
			<li><a class="fNiv"><spring:message code="master.page.rendezvous"/></a>
				<ul>
					<li class="arrow"/>
					<li><a href="rendezvous/list.do"><spring:message code="master.page.rendezvous.list"/></a>
					<li><a href="category/list.do"><spring:message code="master.page.category.list"/></a></li>
				</ul>
			</li>
			<li><a class="fNiv"><spring:message code="master.page.services"/></a>
				<ul>
					<li class="arrow"/>
					<li><a href="service/manager/list.do"><spring:message code="master.page.manager.service.list"/></a></li>
					<li><a href="service/manager/list-created.do"><spring:message code="master.page.manager.service.created"/></a></li>
					<li><a href="service/manager/create.do"><spring:message code="master.page.manager.service.create"/></a></li>
				</ul>
			</li>
		</security:authorize>
		
		<security:authorize access="isAnonymous()">
		
		<!-- Acciones de usuarios sin autenticar -->
			<li><a class="fNiv" href="security/login.do"><spring:message code="master.page.login" /></a></li>
			<li><a class="fNiv"><spring:message code="master.page.register"/></a>
				<ul>
					<li class="arrow"/>
					<li><a href="user/create.do"><spring:message code="master.page.register.user" /></a></li>
					<li><a href="manager/create.do"><spring:message code="master.page.register.manager" /></a></li>
				</ul>
			</li>
			<li><a class="fNiv"><spring:message code="master.page.allUsers"/></a>
				<ul>
					<li class="arrow"/>
					<li><a href="user/list-all.do"><spring:message code="master.page.user.list"/></a></li>
					<li><a href="manager/list-all.do"><spring:message code="master.page.manager.list"/></a></li>
					<li><a href="admin/list-all.do"><spring:message code="master.page.admin.list"/></a></li>
				</ul>
			</li>
			<li><a class="fNiv"><spring:message code="master.page.rendezvous"/></a>
				<ul>
					<li class="arrow"/>
					<li><a href="rendezvous/list.do"><spring:message code="master.page.rendezvous.list"/></a></li>
					<li><a href="category/list.do"><spring:message code="master.page.category.list"/></a></li>
				</ul>
			</li>
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
					
					<li><a href="j_spring_security_logout" onclick="deleteCreditCard()"><spring:message code="master.page.logout" /> </a></li>
				</ul>
			</li>
			<li><a class="fNiv"><spring:message code="master.page.allUsers"/></a>
				<ul>
					<li class="arrow"/>
					<li><a href="user/list-all.do"><spring:message code="master.page.user.list"/></a></li>
					<li><a href="manager/list-all.do"><spring:message code="master.page.manager.list"/></a></li>
					<li><a href="admin/list-all.do"><spring:message code="master.page.admin.list"/></a></li>
				</ul>
			</li>
		</security:authorize>
		<li id="rightB">
			<a class="fNiv" href="<spring:message code="master.page.language.url"/>"><spring:message code="master.page.language"/></a>
		</li>
	</ul>
</div>

<script type="text/javascript">
	function deleteCreditCard() {
		if (getCookie("CCHolderName") != '') {
			delete_cookie("CCHolderName"); 
			delete_cookie("CCBrandName");
			delete_cookie("CCNumber");
			delete_cookie("CCExpMonth");
			delete_cookie("CCExpYear");
			delete_cookie("CCCVV");
		}
		
	}
	
	var delete_cookie = function(name) {
	    document.cookie = name + '=;expires=Thu, 01 Jan 1970 00:00:01 GMT;path=/;';
	};
</script>
