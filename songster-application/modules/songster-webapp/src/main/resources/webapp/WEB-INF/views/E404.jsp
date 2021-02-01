<%@ page session="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="hu">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">
<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=3.0, user-scalable=yes" />
<meta name="HandheldFriendly" content="true" />
<meta name="apple-mobile-web-app-capable" content="YES" />
<meta name="author" content="Tamas Kohegyi" />
<meta name="Description" content="Application to support online glorification for Hungarian language speakers" />
<meta name="Keywords" content="glorification, dicsőítés, dalok" />
<title>Dalos Könyv</title>
<link href="/resources/css/external/bootstrap-4.3.1.min.css" rel="stylesheet" media="screen">
<link href="/resources/css/menu.css" rel="stylesheet" media="screen">
<link id="favicon" rel="shortcut icon" type="image/png" href="/resources/img/favicon.png" />
</head>
<body class="body">
  <div class="container">
    <%@include file="../include/navbar.html" %>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    <div class="centerwidediv textWebkitCenter">
		<br />
		Oppá, hiba történt.
		<br />
    </div>
    <div class="centerwidediv">
		Egy olyan kérés érkezett a weboldalra, amely sajnos nem teljesíthető.
		Az oldal üzemeltetői kaptak egy emlékeztetőt erről a hibáról, és már dolgoznak is azon, hogy ez máskor ne fordulhasson elő.
		Addig is türelmét kérjuk, mert a hiba kijavítása akár több napot is igénybe vehet.
		<br/>
    <hr />
		<br/>
		<div class="textWebkitCenter">A fenti menüsort használva folytassa a tevékenységét, vagy várjon 10 másodpercet, és az oldal újratöltődik a weblap fő oldalával.</div>
		<br />
	</div>
  </div>
  <script src="/resources/js/external/jquery-3.4.1.js"></script>
  <script src="/resources/js/external/bootstrap-4.3.1.min.js"></script>
  <script src="/resources/js/common.js"></script>
  <script src="/resources/js/E404.js"></script>
</body>
</html>
