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
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

	<div>
	<spring:message code="rendez.creator"/>: <a href="user/display.do?userId=${rendezvous.creator.id }">
	<jstl:out value="${rendezvous.creator.name}"/> <jstl:out value="${rendezvous.creator.surname}"/>
	
	</a>
	</div>
	
	<div>
	<p>
	<spring:message code="rendez.name"/>: <jstl:out value="${rendezvous.name}"/>
	</p>
	</div>
	
	<div>
	<p>
	<spring:message code="rendez.description"/>: <jstl:out value="${rendezvous.description}"/>
	</p>
	</div>
	
	<div>
	<spring:message code="rendez.dateFormat2" var="dateFormat2" />
	<p>
	<spring:message code="rendez.date"/>: <fmt:formatDate value="${rendezvous.moment}" pattern="${dateFormat2}" />
	</p>
	</div>
	
	<jstl:if test="${rendezvous.gpsCoordinates.latitude != null}">
	<div>
	<p>
	<spring:message code="rendez.latitude"/>: <jstl:out value="${rendezvous.gpsCoordinates.latitude}"/>
	</p>
	</div>
	</jstl:if>
	
	<jstl:if test="${rendezvous.gpsCoordinates.longitude != null}">	
	<div>
	<p>
	<spring:message code="rendez.longitude"/>: <jstl:out value="${rendezvous.gpsCoordinates.longitude}"/>
	</p>
	</div>
	</jstl:if>
	
	<jstl:if test="${pictureSize > 0}">
	<div>
	<img alt="rendez.picture" src="<jstl:out value="${rendezvous.pictureURL}"/>">
	</div>
	</jstl:if>
		
<%-- 	<div>
	<p><spring:message code="rendez.isFinalVer.status" />:
	<jstl:if test="${rendezvous.finalMode != true}">
	<spring:message code="rendez.isFinalVer.false" var="finalVer"/>
	 <jstl:out value="${finalVer}"/></jstl:if>
	 <jstl:if test="${rendezvous.finalMode == true}">
	<spring:message code="rendez.isFinalVer.true" var="finalVer"/>
	 <jstl:out value="${finalVer}"/></jstl:if></p>
	</div> --%>
	
	<div>
	<p><spring:message code="rendez.adultContent" />:
	<jstl:if test="${rendezvous.adultContent != true}">
	<spring:message code="rendez.isFinalVer.false" var="finalVer"/>
	 <jstl:out value="${finalVer}"/></jstl:if>
	 <jstl:if test="${rendezvous.adultContent == true}">
	<spring:message code="rendez.isFinalVer.true" var="finalVer"/>
	 <jstl:out value="${finalVer}"/></jstl:if></p>
	</div>
	
	<%-- <div>
	<p><spring:message code="rendez.deleted" />:
	<jstl:if test="${rendezvous.deleted != true}">
	<spring:message code="rendez.isFinalVer.false" var="finalVer"/>
	 <jstl:out value="${finalVer}"/></jstl:if>
	 <jstl:if test="${rendezvous.deleted == true}">
	<spring:message code="rendez.isFinalVer.true" var="finalVer"/>
	 <jstl:out value="${finalVer}"/></jstl:if></p>
	</div> --%>
	
	<div>
	<a href="user/list-attendants.do?rendezvousId=${rendezvous.id }">
	<spring:message code="rendez.attendants"/>
	</a>
	</div>
	
	<jstl:if test="${requestURI == 'rendezvous/user/list.do' }">
	<jstl:if test = "${linkedSize > 0}">
	<div>
	<a href="rendezvous/user/list-linked.do?rendezvousId=${rendezvous.id }">
	<spring:message code="rendez.linked"/>
	</a>
	</div>
	</jstl:if>
	</jstl:if>
	
	<jstl:if test="${requestURI != 'rendezvous/user/list.do' }">
	<jstl:if test = "${linked2Size > 0}">
	<div>
	<a href="rendezvous/list-linked.do?rendezvousId=${rendezvous.id }">
	<spring:message code="rendez.linked"/>
	</a>
	</div>
	</jstl:if>
	</jstl:if>
	
	<jstl:if test = "${announcementSize > 0}">
	<div>
	<a href="announcement/list.do?rendezvousId=${rendezvous.id }">
	<spring:message code="rendez.announcements"/>
	</a>
	</div>
	</jstl:if>
	
<br/>
<h2>
<spring:message code="rendez.comments" var="commentHeader"/>
<jstl:out value="${commentHeader}"></jstl:out>
</h2>
<jstl:forEach var="comment" items="${comments}">
	
	<div class="COMMENT">
	
		<div class="COMMENT">
			<a href="user/display.do?userId=${comment.author.id}"><jstl:out value="${comment.author.name}"/></a>:
		</div>
		<div class="COMMENT">
		&emsp;
		<jstl:if test="${comment.pictureURL != ''}">	
			<spring:message code="comment.url" var="urlLink"/>
			<a href="${comment.pictureURL}"><img src="${comment.pictureURL}" alt="${urlLink}" height="150px" width="150px"></a>
		</jstl:if>
			<jstl:out value="${comment.text}"/>
		</div>
		<span class="MOMENT">
			<fmt:formatDate value="${comment.moment}" pattern="${dateFormat2}" />
		</span>
	</div>
	<div>			
		<security:authorize access="hasRole('ADMIN')">
			<a href="comment/admin/delete.do?commentId=${comment.id}"><spring:message code="comment.delete"/></a>
		</security:authorize>
	</div>	
	<jstl:forEach var="reply" items="${comment.replies}">
	<div class="REPLY">
		
			<div>
				&emsp;&emsp;<a href="user/display.do?userId=${reply.author.id}"><jstl:out value="${reply.author.name}"/></a>:	
			</div>
			<div>
				&emsp;&emsp;&emsp;<jstl:out value="${reply.text}"/>
			</div>
			
			<span class="MOMENT">
				<fmt:formatDate value="${reply.moment}" pattern="${dateFormat2}" />
			</span>
			<security:authorize access="hasRole('ADMIN')">
				<a href="reply/admin/delete.do?replyId=${reply.id}"><spring:message code="comment.reply.delete"/></a>
			</security:authorize>
			
	</div>
	
	</jstl:forEach>
	
	<security:authorize access="hasRole('USER')">
		<jstl:if test="${isFinal == true}">
		<jstl:if test="${rsvpd == true}">
			<div>
				<a href="reply/user/create.do?commentId=${comment.id}"><spring:message code="comment.reply.new"/></a>
			</div>
		</jstl:if>
		</jstl:if>
	</security:authorize>
	
	<br/>
	
</jstl:forEach>

<security:authorize access="hasRole('USER')">	
	<jstl:if test="${isFinal == true}">
	<jstl:if test="${rsvpd == true}">
		<a href="comment/user/create.do?rendezvousId=${rendezvousId}"><spring:message code="comment.new"/></a><br>
	</jstl:if>
	</jstl:if>
</security:authorize>
	

	
	
