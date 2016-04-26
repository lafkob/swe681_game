<html>
<head>
<title>Index Page for Traverse</title>
<script type="text/javascript" src="js/lib/jquery-2.2.2.min.js"></script>
<script type="text/javascript" src="js/index.js"></script>
</head>

<body>
	<h4>Main Menu Options:</h4>
	<form action="./logout" method="post">
		<input type="button" id="startBtn" value="Start a game" />
		<input type="button" id="openGamesBtn" value="Find Open Games" />
		<input type="button" id="finishedGamesBtn" value="Find Finished Games" />
		<input type="button" id="pingBtn" value="Ping server" />
		<input type="submit" id="logoutBtn" value="Log out" /> <input type="hidden"
			id="_csrf_token" name="${_csrf.parameterName}" value="${_csrf.token}" />
		<br/>
		<input type="button" id="userInfoBtn" value="Get User Stats" />
	</form>
	<hr>
	<h4>Game Options:</h4>
	Game ID: <input id="gameId" /><br/><br/>
	<input type="button" id="joinBtn" value="Join game" />
	<input type="button" id="quitBtn" value="Quit game" />
	<input type="button" id="statusBtn" value="Game status" />
	<input type="button" id="moveBtn" value="Make move" />
	<hr>
	<h4>Result:</h4>
	<div id="reqResult"></div>
</body>
</html>