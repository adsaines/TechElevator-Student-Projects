<!-- 

includes a button for each of the two common pages: homepage & survey
 -->
  <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
 
 <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
 <c:url value="/CSS/HeaderStyle.css" var="StylePage"/>
 <link rel="stylesheet" href="${StylePage}"/>
 
<html>

<header>
	     
</header>

<body>
	<nav class="navbar navbar-expand-lg navbar-light bg-light">
		<a class="navbar-brand" href="#">
			<img src="img/logo.png" width="350" height="150" class="d-inline-block align-top" alt="">
		</a>
		<div class="collapse navbar-collapse" id="navbarNav">
			<ul class="navbar-nav">
			    <li class="btn btn-outline-success">
			    	<a class="nav-link" href="/m3-java-capstone/HomePage">Homepage <span class="sr-only">(current)</span></a>
			    </li>
			    <li class="btn btn-outline-success">
			    	<a class="nav-link" href="/m3-java-capstone/DailySurvey">Survey</a>
			    </li>
			    
			    <li class="btn btn-outline-success">
			    	<a class="nav-link" href="/m3-java-capstone/SurveyResults">Survey Results</a>
			    </li>
			    
			    <li class="btn btn-outline-success nav-item dropdown">
			    	<a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
          				Park Details
          			</a>
          			
          			<div class="dropdown-menu" aria-labelledby="navbarDropdown">
          				<c:forEach var="code" items="${ parkCodeList }">
          					<c:url value="/SiteDetails?parkCode=${code.key}" var="sitelocation"/>
          					<a class="dropdown-item" href="${sitelocation}">${code.value}</a>
          				</c:forEach>
          			</div>
			    </li>
		    </ul>
		</div>
	</nav>