<html>
<head>
<title>Register Page for Traverse</title>
<script type="text/javascript" src="js/lib/jquery-2.2.2.min.js"></script>
</head>

<body>
	<form action="" method="post">
		Registration<br/><br/>
		Username: <input type="password" id="username" /><br/><br/>
		Password: <input type="password" id="password" /><br/><br/>
		Confirm Password: <input id="passwordConfirm" /><br/><br/>
		
		<input type="hidden" id="_csrf_token" name="${_csrf.parameterName}" value="${_csrf.token}" />
		
		<input type="submit" id="register" value="Register"/> 
	</form>
</body>
</html>