
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:url value="/CSS/SurveyResultsStyle.css" var="StylePage"/>
<c:url value="/img/parks/HomePageBackground.jpg" var="parkPicture"/>
 
<c:import url="Header.jsp"> </c:import>
<link rel="stylesheet" href="${StylePage}"/>

<style>
body {
	background-image: url("${parkPicture}");
}
</style>

<div class="results-table">
<table class="table table-sm table-dark">
	<thead>
 	<tr>
 		<th scope="col"> Park Name </th>
 		<th scope="col"> Number Of Votes </th>
 	</tr>
 	</thead>
 	
 	<tbody>
 	<c:forEach var="parkReviews" items="${results}">
	 	<tr>
		 	<td scope="row">${parkReviews.key}</td>
		 	<td >${parkReviews.value}</td>
	 	</tr>
 	</c:forEach>
 	</tbody>
</table>
</div>


<c:import url="Footer.jsp"> </c:import>