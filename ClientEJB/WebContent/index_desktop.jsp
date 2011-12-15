<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
	
	<!-- 
		<script type="text/javascript" language="Javascript" SRC="./ExtJS/ext-all.js"></script>
		<link rel="stylesheet" type="text/css" href="./ExtJS/resources/css/ext-all-gray.css" />
	-->
	<script type="text/javascript" charset="utf-8" src="http://cdn.sencha.io/ext-4.0.7-gpl/ext-all.js"></script>
	<link rel="stylesheet" type="text/css" href="http://cdn.sencha.io/ext-4.0.7-gpl/resources/css/ext-all-gray.css" />
	
	<link rel="stylesheet" type="text/css" href="./Css/custom.css" />
	
	<script type="text/javascript" language="Javascript" SRC="./js/DesktopApplication/dataModels.js"></script>
	<script type="text/javascript" language="Javascript" SRC="./js/DesktopApplication/dataStores.js"></script>
	
	<script type="text/javascript" language="Javascript" SRC="./js/DesktopApplication/main.js"></script>
	<script type="text/javascript" language="Javascript" SRC="./js/DesktopApplication/tabPanel.js"></script>
	<script type="text/javascript" language="Javascript" SRC="./js/DesktopApplication/toolbar.js"></script>
	<script type="text/javascript" language="javascript">
		function DisabilitaIE4(){
		    if (event.button == 2){
		        return false;
		    }
		}

		function DisabilitaNS4(e){
		    if (document.layers || document.getElementById && !document.all){
		        if (e.which == 2 || e.which == 3) {
		            return false;
		        }
		    }
		}

		document.onload = function () {
		    if (document.layers){
		        document.captureEvents(Event.MOUSEDOWN);
		        document.onmousedown = DisabilitaNS4;
		    }	else if (document.all && !document.getElementById){
		        document.onmousedown = DisabilitaIE4;
		    }
		}
		
		document.oncontextmenu = function(){	return false;	}
	</script>

	<head>
		<%
			try{
				if( session == null || session.isNew() ){
					session.setAttribute("Logged", false);
					//Redirect alla pagina di login
					//out.print("location.replace('index_login.jsp');");
					%><meta http-equiv="Refresh" content="0;url=index_login.jsp" /><%
				}else if( session.getAttribute("Logged").equals(false) ){
					//Redirect alla pagina di login
					//out.print("location.replace('index_login.jsp');");
					%><meta http-equiv="Refresh" content="0;url=index_login.jsp" /><%
				}
				}catch(Exception e){
					//out.print("location.replace('index_login.jsp');");
					%><meta http-equiv="Refresh" content="0;url=index_login.jsp" /><%
				}
		%>	
		
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Restaurant Manager - Desktop</title>
	</head>
	
	<body>
		<div id='center'><H1>...wait...</H1></div>
		<script type="text/javascript">
			//Dopo aver caricato la libreria fa partire l'applicazione
			Ext.EventManager.onDocumentReady(main);
		</script>
		
	</body>
	
</html>