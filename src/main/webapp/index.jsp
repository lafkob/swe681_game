<html>
<head>
<title>Index Page for Traverse</title>
</head>

<body>
	<a href="./api/ping">Example Controller (Ping)</a>

	<form action="./logout" method="post">
		<input type="submit" value="Log out" />
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
	</form>
</body>
</html>