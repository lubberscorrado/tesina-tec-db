/* DEFINIZIONE DI UN WRITER PER LA RICHIESTA CLASSICA DEI PARAMETRI */
Ext.define('Ext.data.writer.SinglePost', {
    extend: 'Ext.data.writer.Writer',
    alternateClassName: 'Ext.data.SinglePostWriter',
    alias: 'writer.singlepost',

    writeRecords: function(request, data) {
        request.params = data[0];
        return request;
    }
});


/*Definisco il modello dei dati*/
Ext.define('StatoTavoli', {
    extend: 'Ext.data.Model',
    fields: [
             {name: 'idTavolo', 		type: 'int',		useNull: true},
             {name: 'nomeTavolo', 		type: 'string'},
             {name: 'numeroPiano', 		type: 'int'},
             {name: 'nomePiano', 		type: 'string'},
             {name: 'nomeArea',  		type: 'string'},
             {name: 'numPosti',    		type: 'int'},
             {name: 'statoTavolo',  	type: 'string'},
             {name: 'cameriere',  		type: 'string'}
    ],
    proxy: {
        type: 'rest',
        url : 'stato',
        /*actionMethods: {
            create : 'POST',
            read   : 'GET',
            update : 'PUT',
            destroy: 'DELETE'
        },*/
        
        //format: 'json',
        appendId: true, //default
        /*headers: {
        	create  : 'shemale',
            read    : 'prendi',
            update  : 'aggiorna',
            destroy : 'destriy'
        },*/
        /*
        api: {
            create  : undefined,
            read    : undefined,
            update  : undefined,
            destroy : undefined
        },*/
        writer: {
            type: 'singlepost'
        },
		reader: {
	        type: 'json',
	        model: 'StatoTavoli',
	        idProperty: 'id_tavolo',
	        root: 'items'
	    }
    }
});


/*Creo lo store*/
var store = Ext.create('Ext.data.Store', {
	storeId: 'datasource_stato_tavoli',
	groupField: 'nomeArea',
	model: 'StatoTavoli',
	autoLoad: true,
	autoSync: true,
	pageSize: 50
	
});

/* Creo l'oggetto rappresentante il Main Tab Panel */
var _mainTabPanel = {
		
		_panel: Ext.create('Ext.tab.Panel', {
			region: 'center',
			width: '100%',
		    height: '100%'
		}),
		
		
		//Start Tab Stato
		_tab_stato: Ext.create('Ext.grid.Panel', {
			title: 'Stato: tavoli',
	        store: Ext.data.StoreManager.lookup('datasource_stato_tavoli'),
	        columns: [
	            { header: 'IdTavolo',  	dataIndex: 'idTavolo', hidden: true },
	            { header: 'Tavolo',  	dataIndex: 'nomeTavolo' },
	            { header: 'Piano',  	dataIndex: 'numeroPiano' },
	            { header: 'Area',  		dataIndex: 'nomeArea' },
	            { header: 'N°Posti', 	dataIndex: 'numPosti' },
	            { header: 'Cameriere', 	dataIndex: 'cameriere' },
	            { header: 'Stato', 		dataIndex: 'statoTavolo'}
	        ],
	        features: [{ftype:'grouping'}],
	        listeners:{
	        	itemdblclick: function(view, record, item, index, e, eOpts){
		        	_viewPort_panel_east.removeAll();
		        	_viewPort_panel_east.setTitle(index);
		        	_viewPort_panel_east.add({
		        		xtype: 'label',
		        		text: '<h1>Tavolo:</h1> '+record.get('Tavolo')
		        	});
		        	_viewPort_panel_east.expand(true);
		        }
	        },
		    dockedItems: [{
	            xtype: 'toolbar',
	            dock: 'bottom',
	            //height: 100,
	            items: ['Raggruppamenti: ',{
	                tooltip: 'Toggle the visibility of the summary row',
	                text: 'None',
	                handler: function(){
	                	_mainTabPanel._tab_stato.getStore().clearGrouping();
	                }
	            },{
	                tooltip: 'Toggle the visibility of the summary row',
	                text: 'Piano',
	                handler: function(){
	                	_mainTabPanel._tab_stato.getStore().group('nomePiano');
	                }
	            },{
	                tooltip: 'Toggle the visibility of the summary row',
	                text: 'Area',
	                handler: function(){
	                	_mainTabPanel._tab_stato.getStore().group('nomeArea');
	                }
	            },{
	                tooltip: 'Toggle the visibility of the summary row',
	                text: 'Stato',
	                handler: function(){
	                	 _mainTabPanel._tab_stato.getStore().group('statoTavolo');
	                }
	            },'->',{
	                text: 'Aggiorna',
	                iconCls: 'icon-add',
	                handler: function(){
	                    // empty record
	                    store.load();
	                }
	            },{
	                text: 'Add',
	                iconCls: 'icon-add',
	                handler: function(){
	                    // empty record
	                    store.insert(0, new StatoTavoli());
	                    //rowEditing.startEdit(0, 0);
	                }
	            }, '-', {
	                text: 'Delete',
	                iconCls: 'icon-delete',
	                handler: function(){
	                    var selection = store.remove(store.getAt(0));
	                 store.sync();
	                }
	            }]
	        }]	//Fine dockeditems

	        
		}),
		//End Tab Stato
		
		_tab_menu: null,
		_tab_camerieri: null,
		_tab_opzioni: null,
		_tabella_stato: null,
		
		
		getPanel 	: function(){	return this._panel;},
		addTabStato : function(){	this._panel.add(this._tab_stato);}

		
};






var _tabPanel;
var _tabPanel_stato;
var _tabPanel_tavoli;
var _tabPanel_menu;
var _tabPanel_camerieri;
var _tabPanel_opzioni;

function create_main_tabPanel(){
	_tabPanel = Ext.create('Ext.tab.Panel', {
		region: 'center',
		//layout: 'fit',
	    width: '100%',
	    height: '100%'/*,
	    activeTab: 0,
	    items: [ _tabPanel_stato,{
        	id: 'main_tab_tavoli',
            title: 'Tavoli',
            html: 'The second tab\'s content. Others may be added dynamically'
        },{
        	id: 'main_tab_menu',
            title: 'Menu',
            html: 'The second tab\'s content. Others may be added dynamically'
        },{
        	id: 'main_tab_camerieri',
            title: 'Camerieri',
            html: 'The second tab\'s content. Others may be added dynamically'
        },{
        	id: 'main_tab_opzioni',
            title: 'Opzioni',
            html: 'The second tab\'s content. Others may be added dynamically'
        }]*/
	});
	return _tabPanel;
}

function get_main_tabPanel(){
	if(_tabPanel != null)
		return _tabPanel;
	else
		return create_main_tabPanel();
}







/*
function getMainTabPanel(){
	_tabPanel_stato = getPanelTabStato();
	
	var tmp = 
	
	/* PER AGGIUNGERE NUOVE TABS
	new_tab = Ext.create('Ext.panel.Panel', {
		title: 'Tab asdasd1',
        html : 'A simple tab'
	});
	_tabPanel.add(new_tab); 
	return tmp;
}
*/


function getPanelTabStato(){
	
	var tabella = getPanelTabStatoTabella();
	

	
	/* CREO LA TABELLA */
	var tmp = Ext.create('Ext.panel.Panel', {
	    //width: 500,
	    //height: 400,
	    title: "Stato",
	    layout: {
	        type: 'vbox',
	        align: 'center'
	    },
	    items: [tabella
	    /*,
	    {
	        xtype: 'panel',
	        title: 'Inner Panel Two',
	        width: 250,
	        flex: 4
	    },
	    {
	        xtype: 'panel',
	        title: 'Inner Panel Three',
	        width: '50%',
	        flex: 4
	    }*/]
	});
	
	/*
	tmp.add([{"num_posti":0,"cameriere":0,"stato":0,"area":"A0","piano":0,"id_tavolo":10,"nome_tavolo":0}]);
	tmp.add([{"num_posti":0,"cameriere":0,"stato":0,"area":"A0","piano":0,"id_tavolo":11,"nome_tavolo":0}]);
	*/
	return tmp;
}

function getPanelTabStatoTabella(){
	
	
	/*Creo la tabella*/
	var tabella = Ext.create('Ext.grid.Panel',{
		title: 'Stato: tavoli',
		layout: 'fit',
        store: Ext.data.StoreManager.lookup('datasource_stato_tavoli'),
        columns: [
            { header: 'IdTavolo',  	dataIndex: 'idTavolo', hidden: true },
            { header: 'Tavolo',  	dataIndex: 'nomeTavolo' },
            { header: 'Piano',  	dataIndex: 'numeroPiano' },
            { header: 'Area',  		dataIndex: 'nomeArea' },
            { header: 'N°Posti', 	dataIndex: 'numPosti' },
            { header: 'Cameriere', 	dataIndex: 'cameriere' },
            { header: 'Stato', 		dataIndex: 'statoTavolo'}
        ],
        features: [{ftype:'grouping'}],
       	//height: 400,
        height: '50%',
        width: '100%',
	    dockedItems: [{
            xtype: 'toolbar',
            dock: 'bottom',
            //height: 100,
            items: ['Raggruppamenti: ',{
                tooltip: 'Toggle the visibility of the summary row',
                text: 'Piano',
                handler: function(){
                    tabella.getStore().group('nomePiano');
                }
            },{
                tooltip: 'Toggle the visibility of the summary row',
                text: 'Area',
                handler: function(){
                	tabella.getStore().group('nomeArea');
                }
            },{
                tooltip: 'Toggle the visibility of the summary row',
                text: 'Stato',
                handler: function(){
                	tabella.getStore().group('statoTavolo');
                }
            },'->',{
            	text: 'Aggiorna',
                handler: function(){
                	_toolbar.add('lalaal');
                	_tabPanel.removeAll();
                	_tabPanel.add({
                		xtype: 'form',
                		layout: 'fit',
                		region: 'west',
                		title: 'Hello',
                		collapsible: true,
                	    split: true,
                	    width: 200,
                	    html: '<p>World!</p>'
                	});
                }
            }]
        }]	//Fine dockeditems
	});

        
	return tabella;
}


function getPanelInfoTavolo(){
	var tmp = Ext.create('Ext.form.Panel', {
	    title: 'Info tavolo:',
	    region: 'east',
	    bodyPadding: 5,
	    width: 350,
	    height: 350,
	    // The form will submit an AJAX request to this URL when submitted
	   // url: 'save-form.php',

	    // Fields will be arranged vertically, stretched to full width
	    //layout: 'fit',
	    /*
	    defaults: {
	        anchor: '100%'
	    },*/

	    // The fields
	    defaultType: 'textfield',
	    items: [{
	        fieldLabel: 'First Name',
	        name: 'first',
	        allowBlank: false
	    },{
	        fieldLabel: 'Last Name',
	        name: 'last',
	        allowBlank: false
	    }],

	    // Reset and Submit buttons
	    buttons: [{
	        text: 'Reset',
	        handler: function() {
	            this.up('form').getForm().reset();
	        }
	    }, {
	        text: 'Submit',
	        formBind: true, //only enabled once the form is valid
	        disabled: true,
	        handler: function() {
	            var form = this.up('form').getForm();
	            if (form.isValid()) {
	                form.submit({
	                    success: function(form, action) {
	                       Ext.Msg.alert('Success', action.result.msg);
	                    },
	                    failure: function(form, action) {
	                        Ext.Msg.alert('Failed', action.result.msg);
	                    }
	                });
	            }
	        }
	    }]
	});
	
	
	_viewPort.forceComponentLayout( );
	return tmp;
}