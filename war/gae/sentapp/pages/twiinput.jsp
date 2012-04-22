<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
	<script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script>
	<script src="/gae/sentapp/scripts/sentapp.js" type="text/javascript"></script>
	<link rel="stylesheet" href="/gae/sentapp/styles/sentapp.css" type="text/css" />
<body>
	<div class="inputdiv">
		<span>Введите ключевое слово для анализатора:</span>
		<input id="searchterm"   type="text"/>
		<input id="searchsubmit" type="button" value="Поехали!"/>
	</div>
	<div class="results"></div>
</body>
</html>