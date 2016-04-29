<html>
<head>
<title>Register Page for Traverse</title>
<script type="text/javascript" src="js/lib/jquery-2.2.2.min.js"></script>
<script type="text/javascript" src="js/register.js"></script>
</head>

<body>
	Registration<br/><br/>
	Username: <input id="username" /><br/><br/>
	Password: <input type="password" id="password" /><br/><br/>
	Confirm Password: <input type="password" id="passwordConfirm" /><br/><br/>
	<input type="hidden" id="_csrf_token" name="${_csrf.parameterName}" value="${_csrf.token}" />
	
	<input type="button" id="registerBtn" value="Register" />
	&nbsp;&nbsp;.....Or already registered? <a href="./login">Click here to login</a>
	<br/><br/>

	<div id="reqResult"></div>
</body>
</html>