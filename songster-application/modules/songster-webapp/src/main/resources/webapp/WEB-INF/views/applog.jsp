<%@ page session="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="hu">
<head>
<meta charset="UTF-8">
<meta name="_csrf" content="${_csrf.token}"/>
<meta name="_csrf_header" content="${_csrf.headerName}"/>
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
<script src="/resources/js/external/jquery-3.4.1.js"></script>
<script src="/resources/js/external/bootstrap-4.3.1.min.js"></script>
<script src="/resources/js/common.js"></script>
<script src="/resources/js/applog.js" nonce></script>
<link href="/resources/css/external/bootstrap-4.3.1.min.css" rel="stylesheet" media="screen">
<link href="/resources/css/menu.css" rel="stylesheet" media="screen">
<link id="favicon" rel="shortcut icon" type="image/png" href="/resources/img/favicon.png" />
</head>
<body class="body">
  <div class="container">
    <%@include file="../include/navbar.html" %>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

  </div>
  <div class="container">
    <fieldset class="form-horizontal">
    <legend class="message-legend">Válassz az alábbi lehetőségek közül...</legend>
    <div class="control-group">
    <a id="social-button" class="btn btn-primary" href="/songsterSecure/social">Azonosítások</a>
    <a id="audit-button" class="btn btn-primary" href="/songsterSecure/audit">Audit Log</a>
    </div>
    </fieldset>

    <fieldset class="form-horizontal">
        <legend>Logok</legend>
        <div class="control-group">
            <span class="help-block">Válassz az alább elérhető log file-ok közül.</span>
        </div>
        <div class="control-group">
            <ol id="div-log-files"></ol>
        </div>
    </fieldset>

  </div>
</body>
</html>
