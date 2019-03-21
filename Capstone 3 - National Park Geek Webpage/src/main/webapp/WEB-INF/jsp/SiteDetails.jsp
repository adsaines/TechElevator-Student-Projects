<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="f" %>
  
<c:import url="Header.jsp" />

<c:url value="/CSS/SiteDetailsStyle.css" var="StylePage"/>
<link rel="stylesheet" href="${StylePage}"/>
<c:url value="/img/parks/${fn:toLowerCase(park.parkCode)}-details.jpg" var="parkPicture"/>

<style>
body {
	background-image: url("${parkPicture}");
}
</style>


<div class="container alert-light">
	<!-- 
	<img src="${parkPicture}" class="img-fluid" alt="Responsive Image"/>
	 -->
	
	<div class="park-title">
		<ul>
			<li> <h1 class="display-4">${park.parkName}</h1> </li>
			<li id="quote"> <h4>${park.inspirationalQuote}</h4></li>
			<li id="quoteSource"><h5>-${park.inspirationalQuoteSource}</h5></li>
		</ul>
	</div>


 <div id="park-information">
	
	<div id="basicParkInfo">
	
	<f:formatNumber type="number" value="${park.annualVisitorCount}" var="visitors"/>
	<f:formatNumber type="number" value="${park.acreage}" var="acreage"/>
	<f:formatNumber type="number" value="${park.elevation}" var="elevation"/>
	<f:formatNumber type="number" value="${park.milesOfTrail}" var="milesOfTrail"/>
	<f:formatNumber type="number" value="${park.numberOfAnimalSpecies}" var="species"/>
	<f:formatNumber type="currency" value="${park.entryFee}" var="entry" />
	
	<p id="entryFee">Park Entry Fee: <span class="make-me-bold">${entry} </span></p>
	
	<p>
		${park.parkName} is located in ${park.state} and has an average annual visitor count of ${visitors}.
		It boasts a total acreage of ${acreage} with a peak elevation of ${elevation} feet. In this rugged section of our world we have layed down ${milesOfTrail} miles of trail.
		The park has a ${park.climate} climate and harbors at least ${species} species of animals. 
	</p>
	
	<p id="description">${park.parkDescription}</p>
	
	</div>
</div>

<!-- national park weather forecast -->
<div class="alert" role="alert">

	<table id="five-day-forecast" class="table">
	<tr>
	 	<td class="make-me-bold center-text"> Today </td>
	 	<td colspan="4"></td>
	 	
	</tr>
	
	<tr>
		<c:forEach var="day" items="${fiveDayForecast}">
			<td> <img class="weatherPic" src="img/weather/${day.forecast}.png"> </td>
		</c:forEach>
	</tr>

	<tr>
		<c:forEach var="day" items="${fiveDayForecast}">
			<td class="center-text">Low ${day.tempLow} / High ${day.tempHigh} (deg ${fn:toUpperCase(tempScale)})</td>
		</c:forEach>
	</tr>
	
	<tr>
		<c:forEach var="suggestion" items="${suggestionList}">
			<td class="center-text"> ${suggestion} </td>
		</c:forEach>
	  	<td colspan="4"></td>
 	</tr>
	</table>
	
	<c:url var="tempScaleChange" value="/changeTempScale?parkCode=${park.parkCode}"></c:url>
	<a class="btn btn-primary" href="${tempScaleChange}">
		Switch from ${tempScale == 'f'? 'Fahrenheit' : 'Celsius'} to ${tempScale != 'f'? 'Fahrenheit' : 'Celsius'}
	</a>
</div>

</div>


<c:import url="Footer.jsp"> </c:import>