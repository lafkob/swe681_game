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
	<input type="button" id="startBtn" value="Start a game" />
	<input type="button" id="findBtn" value="Find Open Games" />
	<br/><br/>
	Game ID: <input id="gameId" /> 
	<br/><br/>
	<input type="button" id="joinBtn" value="Join game" />
	<input type="button" id="quitBtn" value="Quit game" />
	<input type="button" id="statusBtn" value="Game status" />
	<input type="button" id="moveBtn" value="Make move" />
	<br/><br/><br/><br/>
	<input type="button" id="pingBtn" value="Ping server" />
	<br/><br/><br/><br/>
	<form action="./logout" method="post">
		<input type="submit" id="logoutBtn" value="Log out" /> <input type="hidden"
			id="_csrf_token" name="${_csrf.parameterName}" value="${_csrf.token}" />
	</form>
	
	<div id="reqResult"></div>
</body>
</html>