<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
	<script type="text/javascript" language="Javascript" SRC="./ExtJS/ext-all.js"></script>
	<link rel="stylesheet" type="text/css" href="./ExtJS/ext-all-gray.css" />
	
	<script type="text/javascript" language="Javascript" SRC="./js/DesktopApplication/main.js"></script>
	<script type="text/javascript" language="Javascript" SRC="./js/DesktopApplication/tabPanel.js"></script>
	<script type="text/javascript" language="Javascript" SRC="./js/DesktopApplication/toolbar.js"></script>
	<script type="text/javascript" language="javascript">
		var messaggio = "Click destro disabilitato";

		function DisabilitaIE4(){
		    if (event.button == 2){
		        alert(messaggio);
		        return false;
		    }
		}

		function DisabilitaNS4(e){
		    if (document.layers || document.getElementById && !document.all){
		        if (e.which == 2 || e.which == 3) {
		            alert(messaggio);
		            return false;
		        }
		    }
		}

		document.onload = function () {
		    if (document.layers){
		        document.captureEvents(Event.MOUSEDOWN);
		        document.onmousedown = DisabilitaNS4;
		    }
		    else if (document.all && !document.getElementById){
		        document.onmousedown = DisabilitaIE4;
		    }
		}
		
		document.oncontextmenu = function(){ 
		    return false;
		}
	</script>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Restaurant Manager - Desktop</title>
	</head>
	
	<body>
		<div id='center'></div>
		
		<script type="text/javascript">
			/*Quando il documento è caricato eseguo l'applicazione*/
			Ext.EventManager.onDocumentReady(desktop_main);
		</script>
		
	</body>
	
</html>