<html>
<head>
<title>Index Page for Traverse</title>
<script type="text/javascript" src="js/lib/jquery-2.2.2.min.js"></script>
<script type="text/javascript" src="js/index.js"></script>
</head>

<body>
	<%
		// TODO: obviously these are just garbage controls to call the REST endpoints
	%>

	Game ID: <input id="gameId" /> 
	<br/>
	<input type="button" id="joinBtn" value="Join game" />
	<input type="button" id="statusBtn" value="Game status" /></form>
	<input type="button" id="moveBtn" value="Make move" />
	<br/><br/>
	<input type="button" id="pingBtn" value="Ping server" />
	<br/><br/>
	<form action="./logout" method="post">
		<input type="submit" id="logoutBtn" value="Log out" /> <input type="hidden"
			id="_csrf_token" name="${_csrf.parameterName}" value="${_csrf.token}" />
	</form>
	
	<div id="reqResult"></div>
</body>
</html>