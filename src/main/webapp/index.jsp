<html>
<head>
<title>Index Page for Traverse</title>
</head>

<body>
	<% // TODO: obviously these are just garbage controls to call the REST endpoints  %>
	
	<form action="./api/game/join" method="post">
		<input name="id" />
		<input type="submit" value="Join game" />
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
	</form>
	
	
	<form action="./api/game/status" method="post">
		<input name="id" />
		<input type="submit" value="Game status" />
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
	</form>
	
	
	<form action="./api/game/move" method="post">
		<input type="submit" value="Make move" />
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
	</form>
	
	<form action="./api/ping" methods="get">
		<input type="submit" value="Ping server" />
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
	</form>

	<form action="./logout" method="post">
		<input type="submit" value="Log out" />
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
	</form>
</body>
</html>