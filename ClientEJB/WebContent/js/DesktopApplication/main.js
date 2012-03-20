/*VARIABILI GLOBALI*/
var _toolbar;
var _viewPort;
var _viewPort_panel_east;
var _viewPort_panel_west;

var sessionFunction;
var scheduledUpdate;

var _global = {
		_restaurant : 'BOH',
		_user : 'Gennaro',
		_isCameriere : false,
		_isCuoco : false,
		_isCassiere : false,
		_isAdministrator : false
};


function main(){
	Ext.Ajax.request({
	    url: 'login',
	    method: 'POST',
	    params: {
	        action: 'login_info'
	    },
	    success: function(response){
	    	var json_response = Ext.JSON.decode(response.responseText);
	    	//console.debug('Logged '+json_response.logged);

	    	if( json_response.logged == true){
	    		_global._restaurant = 		json_response.restaurant;
		    	_global._user = 			json_response.user;
		    	_global._isCameriere = 		json_response.isCameriere;
		    	_global._isCuoco = 			json_response.isCuoco;
		    	_global._isCassiere = 		json_response.isCassiere;
		    	_global._isAdministrator = 	json_response.isAdministrator;
		    	if( _global._isCassiere == true){
		    		desktop_main();
		    		sessionFunction=setTimeout("startScheduledActions()",10000);
		    	}
	    	}else{
	    		location.replace('index_login.jsp');
	    	}
	    	
	    }
	});
};


/* Funzione main */
function desktop_main(){
	
	document.getElementById('center').innerHTML = '';
	
	/* Genero la Toolbar */
	_toolbar = get_main_toolbar();
	
	/* Genero il tab panel*/
	//_tabPanel = get_main_tabPanel();
	
	/* Genero i pannelli laterali per permettere le modifiche successive: ViewPort una volta creato non pu� esser modificato*/
	_viewPort_panel_east = Ext.create('Ext.panel.Panel', {
		id: 'viewport_east',
		region: 'east',
		layout: 'fit',
		collapsible: true,
        split: true,
        width: 600,
        hidden: false,
        collapsed: true
	});
	
	_viewPort_panel_west = Ext.create('Ext.panel.Panel', {
		id: 'viewport_west',
		region: 'west',
		layout: 'fit',
		collapsible: true,
        split: true,
        width: 250,
        hidden: false,
        collapsed: true
	});
	
	/* Genero il border layout panel generale dell'applicazione */
	_viewPort = Ext.create('Ext.container.Viewport', {
	    layout: 'border',
	    items: [
	            _toolbar,
	            //_tabPanel,
	            _mainTabPanel.getPanel(),
	            _viewPort_panel_east,
	            //_viewPort_panel_west
	   ]
	});
	
	initStores();
	
	_mainTabPanel.addTabStato();
	
	if(_global._isAdministrator == true){
		_mainTabPanel.addTabGestioneTavolo();
		_mainTabPanel.addTabGestioneMenu();
		_mainTabPanel.addTabGestionePersonale();
		_mainTabPanel.addTabStoricoConti();
	}
};

function startScheduledActions(){
	aggiornamentoAutomaticoDati();
}

function aggiornamentoAutomaticoDati(){
	Ext.getStore('datastore_stato_tavolo').load();
	Ext.getStore('datastore_stato_cameriere').load();
	Ext.getStore('datastore_stato_cucina').load();
	Ext.getStore('datastore_elenco_conti_tavolo').load();
	
	scheduledUpdate=setTimeout("aggiornamentoAutomaticoDati()",30000);
};