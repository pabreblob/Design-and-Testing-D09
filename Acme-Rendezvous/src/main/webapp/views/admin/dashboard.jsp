<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<spring:message code="admin.dateFormat" var="dateFormat" />
<table>
  <tr>
  	<th></th>
    <th><spring:message code="admin.average"/></th>
    <th><spring:message code="admin.standardesv"/></th>
  </tr>
  <tr>
    <td><spring:message code="admin.rendCreated"/></td>
    <td><jstl:out value="${averageRendezvousCreatedPerUser}"/></td> 
    <td><jstl:out value="${standarDesvRendezvousCreatedPerUser}"/></td>
  </tr>
  <tr>
    <td><spring:message code="admin.rendReserved"/></td>
    <td><jstl:out value="${averageRendezvousReservedPerUser}"/></td> 
    <td><jstl:out value="${standarDesvRendezvousReservedPerUser}"/></td>
  </tr>
    <tr>
    <td><spring:message code="admin.rendAttendants"/></td>
    <td><jstl:out value="${averageAttendantsPerRendezvous}"/></td> 
    <td><jstl:out value="${standarDesvAttendantsPerRendezvous}"/></td>
  </tr>
  <tr>
    <td><spring:message code="admin.rendAnnouncements"/></td>
    <td><jstl:out value="${averageAnnouncementsPerRendezvous}"/></td> 
    <td><jstl:out value="${standarDesvAnnouncementsPerRendezvous}"/></td>
  </tr>
  <tr>
    <td><spring:message code="admin.rendQuestions"/></td>
    <td><jstl:out value="${averageQuestionsPerRendezvous}"/></td> 
    <td><jstl:out value="${standarDesvQuestionsPerRendezvous}"/></td>
  </tr>
  <tr>
    <td><spring:message code="admin.rendAnswers"/></td>
    <td><jstl:out value="${averageAnswersPerRendezvous}"/></td> 
    <td><jstl:out value="${standarDesvAnswersPerRendezvous}"/></td>
  </tr>
  <tr>
    <td><spring:message code="admin.repliesComment"/></td>
    <td><jstl:out value="${averageRepliesPerComment}"/></td> 
    <td><jstl:out value="${standarDesvRepliesPerComment}"/></td>
  </tr>
  
  </table>
  
  
  
  <div>
  <p><spring:message code="admin.ratioUsersCreatedRend"/>: <jstl:out value="${ratioUsersWithCreatedRendezvous}"/></p>
  </div>
  

  
  	<p><spring:message code="admin.rendezvousMostReserved"/></p>
  <display:table pagesize="5" class="displaytag" keepStatus="true"
	name="${mostReserved}"  id="row" requestURI="${requestURI}">
 <spring:message code="admin.rendezvous.name" var="nameHeader" />
		<display:column property="name" title="${nameHeader}" />
	<spring:message code="admin.rendezvous.creator" var="creatorHeader" />
		<display:column property="creator.userAccount.username" title="${creatorHeader}" />		
	<spring:message code="admin.rendezvous.moment" var="momentHeader" />
		<display:column title="${momentHeader}" >	
	<jstl:if test ="${row.moment.before(timestamp) }">
		
		<div class = "EXPIRADO">
			<fmt:formatDate value="${row.moment}" pattern="${dateFormat}" />
		</div>
		</jstl:if>
		<jstl:if test ="${row.moment.after(timestamp)}">
		<div class = "NOEXPIRADO">
			<fmt:formatDate value="${row.moment}" pattern="${dateFormat}" />
		</div>
		</jstl:if>
	</display:column>
	<spring:message code="admin.rendez.deleted" var="deletedHeader" />
	<display:column  title="${deletedHeader}" >
		<jstl:if test ="${row.deleted == false }">
		<div class = "NOBORRADO">
			<jstl:out value="${row.deleted}"></jstl:out>
		</div>
		</jstl:if>
		<jstl:if test ="${row.deleted == true }">
		<div class = "BORRADO">
			<jstl:out value="${row.deleted}"></jstl:out>
		</div>
		</jstl:if>
	</display:column>
	<display:column>
			<jstl:if test="${row.deleted==false}">
			<a href="rendezvous/display.do?rendezvousId=${row.id}">
				<spring:message	code="admin.rendezvous.view" />
			</a>
		</jstl:if>
		</display:column>
	</display:table>
	<p><spring:message code="admin.rendezvousannouncementsOver75"/></p>
	<display:table pagesize="5" class="displaytag" keepStatus="true"
	name="${announcementsOver75}"  id="row" requestURI="${requestURI}">
 <spring:message code="admin.rendezvous.name" var="nameHeader" />
		<display:column property="name" title="${nameHeader}" />
	<spring:message code="admin.rendezvous.creator" var="creatorHeader" />
		<display:column property="creator.userAccount.username" title="${creatorHeader}" />		
	<spring:message code="admin.rendezvous.moment" var="momentHeader" />
	<display:column title="${momentHeader}" >	
	<jstl:if test ="${row.moment.before(timestamp) }">
		
		<div class = "EXPIRADO">
			<fmt:formatDate value="${row.moment}" pattern="${dateFormat}" />
		</div>
		</jstl:if>
		<jstl:if test ="${row.moment.after(timestamp)}">
		<div class = "NOEXPIRADO">
			<fmt:formatDate value="${row.moment}" pattern="${dateFormat}" />
		</div>
		</jstl:if>
	</display:column>
	<spring:message code="admin.rendez.deleted" var="deletedHeader" />
	<display:column  title="${deletedHeader}" >
		<jstl:if test ="${row.deleted == false }">
		<div class = "NOBORRADO">
			<jstl:out value="${row.deleted}"></jstl:out>
		</div>
		</jstl:if>
		<jstl:if test ="${row.deleted == true }">
		<div class = "BORRADO">
			<jstl:out value="${row.deleted}"></jstl:out>
		</div>
		</jstl:if>
	</display:column>
	<display:column>
			<jstl:if test="${row.deleted==false}">
			<a href="rendezvous/display.do?rendezvousId=${row.id}">
				<spring:message	code="admin.rendezvous.view" />
			</a>
		</jstl:if>
		</display:column>
	</display:table>
	<p><spring:message code="admin.rendezvouslinkedPlus10"/></p>
	<display:table pagesize="5" class="displaytag" keepStatus="true"
	name="${linkedPlus10}"  id="row" requestURI="${requestURI}">
 <spring:message code="admin.rendezvous.name" var="nameHeader" />
		<display:column property="name" title="${nameHeader}" />
	<spring:message code="admin.rendezvous.creator" var="creatorHeader" />
		<display:column property="creator.userAccount.username" title="${creatorHeader}" />		
	<spring:message code="admin.rendezvous.moment" var="momentHeader" />
	<display:column title="${momentHeader}" >	
	<jstl:if test ="${row.moment.before(timestamp) }">
		
		<div class = "EXPIRADO">
			<fmt:formatDate value="${row.moment}" pattern="${dateFormat}" />
		</div>
		</jstl:if>
		<jstl:if test ="${row.moment.after(timestamp)}">
		<div class = "NOEXPIRADO">
			<fmt:formatDate value="${row.moment}" pattern="${dateFormat}" />
		</div>
		</jstl:if>
	</display:column>
	<spring:message code="admin.rendez.deleted" var="deletedHeader" />
	<display:column  title="${deletedHeader}" >
		<jstl:if test ="${row.deleted == false }">
		<div class = "NOBORRADO">
			<jstl:out value="${row.deleted}"></jstl:out>
		</div>
		</jstl:if>
		<jstl:if test ="${row.deleted == true }">
		<div class = "BORRADO">
			<jstl:out value="${row.deleted}"></jstl:out>
		</div>
		</jstl:if>
	</display:column>
	<display:column>
	<jstl:if test="${row.deleted==false}">
			<a href="rendezvous/display.do?rendezvousId=${row.id}">
				<spring:message	code="admin.rendezvous.view" />
			</a>
		</jstl:if>
		</display:column>
	
	</display:table>

	