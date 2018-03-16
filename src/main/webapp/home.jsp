<%@ page import="tlc.project.service.AdvertisementService" %>
<%@ page import="tlc.project.model.Advertisement" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.fasterxml.jackson.databind.ObjectMapper" %>

<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<html>
	<head>
		<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
	</head>
	<body>
	<% SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd"); %>
	<%	AdvertisementService advService = AdvertisementService.getInstance(); %>
	<%	ObjectMapper mapper = new ObjectMapper(); %>
	<%	String searchByTitle = null; %>
	<%	Long searchByPriceMin = null; %>
	<%	Long searchByPriceMax = null; %>
	<%	Date searchByDateMin = null; %>
	<%	Date searchByDateMax = null; %>
	
	
	<% 	searchByTitle = request.getParameter("searchByTitle"); 
		pageContext.setAttribute("searchByTitle", searchByTitle);
	%>
	<%	
		String param = request.getParameter("searchByPriceMin");
		if(param != null && !param.isEmpty()){
			searchByPriceMin = Long.decode(param);
			pageContext.setAttribute("searchByPriceMin", searchByPriceMin);
		}
	%>
	<%	
		param = request.getParameter("searchByPriceMax");
		if(param != null && !param.isEmpty()){
			searchByPriceMax = Long.decode(param);
			pageContext.setAttribute("searchByPriceMax", searchByPriceMax);
		}
	%>
	<%	
		param = request.getParameter("searchByDateMin");
		if(param != null && !param.isEmpty()){
			searchByDateMin = dateFormater.parse(param);
			pageContext.setAttribute("searchByDateMin", searchByDateMin);
		}
	%>
	<%	
		param = request.getParameter("searchByDateMax");
		if(param != null && !param.isEmpty()){
			searchByDateMax = dateFormater.parse(param);
			pageContext.setAttribute("searchByDateMax", searchByDateMax);
		}
	%>
	
	
	<%  List<Advertisement> advertisements = advService.get(searchByTitle,searchByPriceMin,searchByPriceMax,searchByDateMin,searchByDateMax);
		pageContext.setAttribute("json_advs", mapper.writeValueAsString(advertisements) );
	%>

<!-- Content de la page -->

	<% if(!advertisements.isEmpty()) { %>
		<ul>
			<%	for(Advertisement adv : advertisements) { %>
			<%	pageContext.setAttribute("adv_resume", adv.toString()); %>
					<li>${fn:escapeXml(adv_resume)}</li>
			<%	}	%>
		</ul>
	<% } %>

	
<!-- Action sur la page -->
	<form action="/home.jsp" method="get">
    	<div>
    		<input type="text" name="searchByTitle" value="${fn:escapeXml(searchByTitle)}"/>
   		</div>
   		<div>
    		<input type="number" name="searchByPriceMin" value="${fn:escapeXml(searchByPriceMin)}"/>
   		</div>
   		<div>
    		<input type="number" name="searchByPriceMax" value="${fn:escapeXml(searchByPriceMax)}"/>
   		</div>
   		<div>
    		<input type="date" name="searchByDateMin" value="${fn:escapeXml(searchByDateMin)}"/>
   		</div>
   		<div>
    		<input type="date" name="searchByDateMax" value="${fn:escapeXml(searchByDateMax)}"/>
   		</div>
    	<div>
    		<input type="submit" value="Search Advertisement"/>
   		</div>
	</form>
	
	<form action="/api/advertisement" method="post">
		<div>
    		<textarea name="content" cols=40 rows=6></textarea>
   		</div>
    	<div>
    		<input type="submit" value="Add"/>
   		</div>
	</form>
	
	<form action="/api/advertisement" method="post">
		<div>
    		<input type="hidden" name="content" value="${fn:escapeXml(json_advs)}"/>
   		</div>
    	<div>
    		<input type="submit" value="delete"/>
   		</div>
	</form>
	</body>
</html>