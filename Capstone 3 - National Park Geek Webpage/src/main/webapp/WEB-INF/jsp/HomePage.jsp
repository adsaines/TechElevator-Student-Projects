<!-- 
The HomePage must include:

1: an alphabetical listing of the parks in the DB
2: each park must have: picture, name, location, summary
3: use the NPgeek header
4: picture will be a link to a detailed description of the park, will also include the weather forecast

 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>

<c:url value="/CSS/HomePageStyle.css" var="homePageStyle"/>
<c:url value="/img/parks/HomePageBackground.jpg" var="parkPicture"/>
 
<c:import url="Header.jsp"> </c:import>
<link rel="stylesheet" href="${homePageStyle}"/>

<style>
body {
	background-image: url("${parkPicture}");
	background-repeat: no-repeat;
	background-attachment: fixed;
}
</style>

<div class="mainDiv">
	<c:forEach var="park" items="${parkList}">
		<div class="container alert-light">
		<div class="eachPark">
			<div class="picture">
				<c:url value="/SiteDetails?parkCode=${park.parkCode}" var="detailsPage"/>
				<a href="${detailsPage}">
					<c:url value="/img/parks/${fn:toLowerCase(park.parkCode)}.jpg" var="parkPicture"/>
					<img src="${parkPicture}" />
				</a>
			</div>
			
			<div class="name container" >
				<h2 class="name">${park.parkName} </h2>
			</div> 
		
			<div class="description container">
				<p class="summary">${park.parkDescription}</p>
			</div> 
		</div>
		</div>
	</c:forEach>
</div>


<c:import url="Footer.jsp"> </c:import>
        