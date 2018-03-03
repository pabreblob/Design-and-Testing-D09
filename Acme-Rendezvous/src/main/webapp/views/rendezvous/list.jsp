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
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<display:table name="rendezvous" id="r" requestURI="${requestURI}"
	pagesize="5" class="displaytag">

	
	<jstl:if test="${requestURI == 'rendezvous/user/list.do' }">
	<display:column>
	<jstl:if test = "${r.deleted == false}">
	<jstl:if test = "${r.finalMode == false}">
	<a href="rendezvous/user/edit.do?rendezvousId=${r.id}"> <spring:message
					code="rendez.edit" />
			</a>
	</jstl:if>
	</jstl:if>
	</display:column>
	</jstl:if>
	
	<jstl:if test="${requestURI == 'rendezvous/user/list.do' }">
	<display:column>
	<jstl:if test = "${r.deleted == false}">
	<a href="question/user/list.do?rendezvousId=${r.id}"> <spring:message
					code="rendez.questions" />
			</a>
	</jstl:if>
	</display:column>
	</jstl:if>
	
	<jstl:if test="${requestURI == 'rendezvous/user/list.do' }">
	<display:column>
	<jstl:if test = "${r.deleted == false}">
	<a href="announcement/user/create.do?rendezvousId=${r.id}"> <spring:message
					code="rendez.announcement" />
			</a>
	</jstl:if>
	</display:column>
	</jstl:if>
	
	<jstl:if test="${requestURI == 'rendezvous/user/list.do' }">
	<display:column>
	<jstl:if test = "${r.deleted == false}">
	<jstl:if test = "${rendezSizeLink > 0 }">
	<a href="rendezvous/user/link.do?rendezvousId=${r.id}"> <spring:message
					code="rendez.linked" />
			</a>
	</jstl:if>
	</jstl:if>
	</display:column>
	</jstl:if>
	
	<security:authorize access="hasRole('USER')">
	<display:column>
	<jstl:if test="${requestURI == 'rendezvous/list.do' }">
	<jstl:if test="${r.moment > timestamp }">
	<jstl:set var="contains" value="true"/>
	<jstl:forEach var="item" items="${userLogged.createdRendezvous}">
		<jstl:if test="${item.id == r.id}">
			<jstl:set var="contains" value="false"/>
		</jstl:if>
	</jstl:forEach>
	<jstl:forEach var="item" items="${userLogged.reservedRendezvous}">
		<jstl:if test="${item.id == r.id}">
			<jstl:set var="contains" value="false"/>
		</jstl:if>
	</jstl:forEach>
	<jstl:if test="${contains}">
	<a href="rendezvous/user/joinQuestion.do?rendezvousId=${r.id}"> <spring:message
					code="rendez.join" />
			</a>
	</jstl:if>
	</jstl:if>
	</jstl:if>
	</display:column>
	</security:authorize>
	
	
	<security:authorize access="hasRole('USER')">
	<display:column>
	<jstl:if test="${requestURI == 'rendezvous/user/list-joined.do' }">
	<jstl:if test="${r.moment > timestamp }">
	<jstl:set var="contains" value="true"/>
	<jstl:forEach var="item" items="${userLogged.createdRendezvous}">
		<jstl:if test="${item.id == r.id}">
			<jstl:set var="contains" value="false"/>
		</jstl:if>
	</jstl:forEach>
	<jstl:if test="${contains}">
	<a href="rendezvous/user/unjoin.do?rendezvousId=${r.id}"> <spring:message
					code="rendez.unjoin" />
			</a>
	
	</jstl:if>
	</jstl:if>
	</jstl:if>
	</display:column>
	</security:authorize>
	
	<spring:message code="rendez.date" var="dateHeader"/>
	<spring:message code = "rendez.dateFormat2" var="formatHeader"/>
		<display:column title="${dateHeader}" >	
	<jstl:if test ="${r.moment.before(timestamp) }">
		
		<div class = "EXPIRADO">
			<fmt:formatDate value="${r.moment}" pattern="${formatHeader}" />
		</div>
		</jstl:if>
		<jstl:if test ="${r.moment.after(timestamp)}">
		<div class = "NOEXPIRADO">
			<fmt:formatDate value="${r.moment}" pattern="${formatHeader}" />
		</div>
		</jstl:if>
	</display:column>
	<spring:message code="rendez.name" var="titleHeader" />
	<display:column property="name" title="${titleHeader}"/>
	<spring:message code="rendez.creator" var="creatorHeader" />
	<display:column property="creator.name" title="${creatorHeader}" />
	
	<jstl:if test="${requestURI == 'rendezvous/user/list.do' }">
	<spring:message code="rendez.isFinalVer.status" var="finalHeader" />
	<display:column title="${finalHeader}" >
	<jstl:if test="${r.finalMode == true}">
	<spring:message code ="rendez.isFinalVer.true"/>
	</jstl:if>
	<jstl:if test="${r.finalMode == false}">
	<spring:message code ="rendez.isFinalVer.false"/>
	</jstl:if>
	</display:column>
	</jstl:if>
	
	<spring:message code="rendez.adultContent" var="adultHeader" />
	<display:column title="${adultHeader}" >
	<jstl:if test="${r.adultContent == true}">
	<spring:message code ="rendez.isFinalVer.true"/>
	</jstl:if>
	<jstl:if test="${r.adultContent == false}">
	<spring:message code ="rendez.isFinalVer.false"/>
	</jstl:if>
	</display:column>
	
	<jstl:if test="${requestURI == 'rendezvous/user/list.do' }">
	<spring:message code="rendez.deleted" var="deletedHeader" />
	<display:column  title="${deletedHeader}" >
		<jstl:if test ="${r.deleted == false }">
		<div class = "NOBORRADO">
				<spring:message code ="rendez.isFinalVer.false"/>
		</div>
		</jstl:if>
		<jstl:if test ="${r.deleted == true }">
		<div class = "BORRADO">
			<spring:message code ="rendez.isFinalVer.true"/>
		</div>
		</jstl:if>
	</display:column>
	</jstl:if>
	
	
	<security:authorize access="hasRole('ADMIN')">
	<display:column>
	<jstl:if test = "${r.deleted == false}">
	<a href="rendezvous/administrator/delete.do?rendezvousId=${r.id}"> <spring:message
					code="rendez.delete" />
			</a>
	</jstl:if>
	</display:column>
	</security:authorize>

	<display:column>
	<jstl:if test = "${r.deleted == false}">
			<a href="rendezvous/display.do?rendezvousId=${r.id}"> <spring:message
					code="rendez.display" />
			</a>
			</jstl:if>
		</display:column>
</display:table>

<jstl:if test="${requestURI == 'rendezvous/list.do' }">
<jstl:if test="${adult == false }">
<div>
<p><spring:message code="rendez.adultWarning"/></p>
</div>
</jstl:if>
</jstl:if>


<jstl:if test="${requestURI == 'rendezvous/user/list.do' }">
<div><a href="rendezvous/user/create.do"> <spring:message
					code="rendez.create" />
			</a></div>
			</jstl:if>