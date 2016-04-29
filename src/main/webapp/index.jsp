<html>
<head>
<title>Index Page for Traverse</title>
<script type="text/javascript" src="js/lib/jquery-2.2.2.min.js"></script>
<script type="text/javascript" src="js/index.js"></script>
<link rel="stylesheet" type="text/css" href="css/index.css">
</head>

<body>
	<h4>Main Menu Options</h4>
	<form action="./logout" method="post">
		<input type="button" id="startBtn" value="Start a game" />
		<input type="button" id="openGamesBtn" value="See open games" />
		<input type="button" id="finishedGamesBtn" value="See finished games" />
		<input type="button" id="userInfoBtn" value="Get user stats" />
		<input type="button" id="pingBtn" value="Ping server" />
		<input type="submit" id="logoutBtn" value="Log out" /> <input type="hidden"
			id="_csrf_token" name="${_csrf.parameterName}" value="${_csrf.token}" />
		<br/><br/>
		Game History:&nbsp;
		<input id="auditGameId" placeholder="Game ID"/>
		<input type="button" id="auditBtn" value="Fetch" />
	</form>
	
	<hr>
	
	<h4>Game Options</h4>
	Join, Quit, Status:&nbsp;
	<input id="gameId" placeholder="Game ID"/>
	<input type="button" id="joinBtn" value="Join game" />
	<input type="button" id="quitBtn" value="Quit game" />
	<input type="button" id="statusBtn" value="Game status" />
	<br/><br/>
	Move Piece:&nbsp;
	<input id="moveGameId" placeholder="Game ID"/>
	<input id="pieceId" placeholder="Piece ID"/>
	<input id="move" placeholder="x1,y1|x2,y2|x3,y3...."/>
	<input type="button" id="moveBtn" value="Move" />
	
	<hr>
	
	<h4>Result</h4>
	<div id="reqResult"></div>
</body>
</html>