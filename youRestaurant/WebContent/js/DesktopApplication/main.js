/*VARIABILI GLOBALI*/
var _toolbar;
var _viewPort;
var _viewPort_panel_east;
var _viewPort_panel_west;


/* Funzione main */
var desktop_main = function desktop_main(){
	
	/* Genero la Toolbar */
	_toolbar = get_main_toolbar();
	
	/* Genero il tab panel*/
	//_tabPanel = get_main_tabPanel();
	
	/* Genero i pannelli laterali per permettere le modifiche successive: ViewPort una volta creato non può esser modificato*/
	_viewPort_panel_east = Ext.create('Ext.panel.Panel', {
		id: 'viewport_east',
		region: 'east',
		layout: 'fit',
		collapsible: true,
        split: true,
        width: 200,
        hidden: false,
        collapsed: true
	});
	
	_viewPort_panel_west = Ext.create('Ext.panel.Panel', {
		id: 'viewport_west',
		region: 'west',
		layout: 'fit',
		collapsible: true,
        split: true,
        width: 200,
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
	            _viewPort_panel_west
	   ]
	});
	
	_mainTabPanel.addTabStato();
	_mainTabPanel.addTabGestioneTavolo();
};