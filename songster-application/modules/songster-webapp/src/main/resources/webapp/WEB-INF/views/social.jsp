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
<script src="/resources/js/external/dataTables/datatables.min.js"></script>
<script src="/resources/js/commonDataTable.js"></script>
<script src="/resources/js/common.js"></script>
<script src="/resources/js/social.js" nonce></script>
<link href="/resources/css/external/bootstrap-4.3.1.min.css" rel="stylesheet" media="screen">
<link href="/resources/js/external/dataTables/datatables.min.css" rel="stylesheet" type="text/css"/>
<link href="/resources/css/menu.css" rel="stylesheet" media="screen">
<link id="favicon" rel="shortcut icon" type="image/png" href="/resources/img/favicon.png" />
</head>
<body class="body">
  <div class="container">
    <%@include file="../include/navbar.html" %>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    <fieldset class="form-horizontal" id="socialList">
        <legend>Közösségi portálokon bejelentkezettek listája</legend>
        <div class="container textWebkitRight" style="padding:5px"><button id="refreshAll-button" type="button" class="btn btn-secondary" onclick="processEntityUpdated()">Frissítés</button></div>
        <div class="control-group">
            <table id="social" class="table table-striped table-bordered table-hover compact cell-border" style="width:100%" aria-describedby="socialList">
                    <thead>
                        <tr>
                            <th scope="col">ID</th>
                            <th scope="col">Státusz</th>
                            <th scope="col">Google - username</th>
                            <th scope="col">Google - picture</th>
                            <th scope="col">Google - e-mail</th>
                            <th scope="col">Google - UID</th>
                            <th scope="col">Facebook - username</th>
                            <th scope="col">Facebook - firstname</th>
                            <th scope="col">Facebook - e-mail</th>
                            <th scope="col">Facebook - UID</th>
                            <th scope="col">Megjegyzés</th>
                        </tr>
                    </thead>
                    <tfoot>
                        <tr>
                            <th scope="col">ID</th>
                            <th scope="col">Státusz</th>
                            <th scope="col">Google - username</th>
                            <th scope="col">Google - picture</th>
                            <th scope="col">Google - e-mail</th>
                            <th scope="col">Google - UID</th>
                            <th scope="col">Facebook - username</th>
                            <th scope="col">Facebook - firstname</th>
                            <th scope="col">Facebook - e-mail</th>
                            <th scope="col">Facebook - UID</th>
                            <th scope="col">Megjegyzés</th>
                        </tr>
                    </tfoot>
                </table>
            </div>
    </fieldset>

  </div>

    <!-- Modal Edit Social -->
    <div class="modal fade" id="editModal" tabindex="-1" role="dialog" aria-labelledby="editCenterTitle" aria-hidden="true">
      <div class="modal-dialog modal-dialog-centered modal-xl" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="editCenterTitle">Módosítás</h5>
            <button type="button" class="close" data-dismiss="modal" aria-label="Cancel">
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          <div class="modal-body">
              <form>
                  <table id="editTable" class="table table-hover table-bordered" aria-describedby="editCenterTitle">
                      <thead>
                          <tr>
                              <th scope="col">Oszlop név</th>
                              <th style="width:40%" scope="col">Tartalom</th>
                              <th scope="col">Segítség</th>
                          </tr>
                      </thead>
                      <tbody id="editContent"/>
                  </table>
                  <input id="editId" type="hidden" value="">
               </form>
          </div>
          <div class="modal-footer">
            <table class="fullWidth" role="presentation"><tr>
                <td class="textLeft"><button id="deleteButton" type="button" class="btn btn-danger" onclick="deleteSocial()">Social törlése</button></td>
                <td class="textRight">
                    <button id="resetChangesButton" type="button" class="btn btn-secondary" onclick="reBuildModal()">Eredeti adatok visszanyerése</button>
                    <button id="cancelButton" type="button" class="btn btn-info" data-dismiss="modal">Mégsem</button>
                    <button id="saveChangesButton" type="button" class="btn btn-success" onclick="saveChanges()">Mentés</button>
                </td>
            </tr></table>
          </div>
        </div>
      </div>
    </div>

    <!-- Modal Log/History -->
    <div class="modal fade" id="historyModal" tabindex="-1" role="dialog" aria-labelledby="historyCenterTitle" aria-hidden="true">
      <div class="modal-dialog modal-dialog-centered modal-xl" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="historyCenterTitle">Log</h5>
            <button type="button" class="close" data-dismiss="modal" aria-label="Cancel">
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          <div class="modal-body">
              <form>
                  <table id="historyTable" class="table table-hover table-bordered" aria-describedby="historyCenterTitle">
                      <thead>
                          <tr>
                              <th scope="col">Típus</th>
                              <th scope="col">Időpont</th>
                              <th scope="col">Végrehajtó</th>
                              <th style="width:40%" scope="col">Leírás</th>
                              <th scope="col">Egyéb</th>
                          </tr>
                      </thead>
                      <tbody id="historyContent"/>
                  </table>
               </form>
          </div>
          <div class="modal-footer">
            <button id="cancelButton" type="button" class="btn btn-info" data-dismiss="modal">Mégsem</button>
          </div>
        </div>
      </div>
    </div>
    <%@include file="../include/commonAlert.html" %>
    <%@include file="../include/commonConfirm.html" %>
</body>
</html>
