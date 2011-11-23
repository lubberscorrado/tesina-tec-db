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
Ext.define('StatoTavolo', {
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
        appendId: true,
        writer: {
            type: 'singlepost'
        },
		reader: {
	        type: 'json',
	        model: 'StatoTavolo',
	        idProperty: 'id_tavolo',
	        root: 'items'
	    }
    }
});

Ext.define('StatoCameriere', {
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
        appendId: true,
        writer: {
            type: 'singlepost'
        },
		reader: {
	        type: 'json',
	        model: 'StatoTavolo',
	        idProperty: 'id_tavolo',
	        root: 'items'
	    }
    }
});

Ext.define('StatoCuoco', {
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
        appendId: true,
        writer: {
            type: 'singlepost'
        },
		reader: {
	        type: 'json',
	        model: 'StatoTavolo',
	        idProperty: 'id_tavolo',
	        root: 'items'
	    }
    }
});

Ext.define('nodoGestioneTavolo', {
    /*extend: 'Ext.data.NodeInterface',*/
	extend: 'Ext.data.Model',
    fields: ['id','realId','parentId','realParentId','nome','descrizione','tipo','enabled','numPosti','stato','text'],
    proxy: {
        type: 'rest',
        url : 'gestioneTavolo',
        writer: {
            type: 'singlepost'
            //type: 'json'
        },
		reader: {
	        type: 'json',
	        idProperty: 'id',
	        root: 'data'
	    }
    }
});




/*Creo lo store*/
Ext.create('Ext.data.Store', {
	storeId: 'datastore_stato_tavolo',
	groupField: 'nomeArea',
	model: 'StatoTavolo',
	autoLoad: true,
	//autoSync: true,
	pageSize: 50
	
});

Ext.create('Ext.data.TreeStore', {
	storeId: 'datastore_gestione_tavolo',
	autoLoad: true,
	autoSync: true,
	model: 'nodoGestioneTavolo',
	/*proxy: {
        type: 'rest',
        url : 'gestioneTavolo',
        writer: {
            type: 'singlepost'
            //type: 'json'
        },
		reader: {
	        type: 'json',
	        idProperty: 'id',
	        root: 'data'
		}
    },*/
    root: {
        text: "Ristorante"
    },
listeners: {
	append: function( thisNode, newChildNode, index, eOpts ) {
            var depth = newChildNode.get("depth");
            
            if( depth == 1){
            	newChildNode.set('leaf', false);
            	//newChildNode.set('icon', newChildNode.get('profile_image_url'));
            }else if( depth == 2){
            	newChildNode.set('leaf', false);
            	//newChildNode.set('icon', newChildNode.get('profile_image_url'));
            }else if( depth == 3){
            	newChildNode.set('leaf', true);
            	//newChildNode.set('icon', newChildNode.get('profile_image_url'));
            }
            
        }
    }
});


/* Creo l'oggetto rappresentante il Main Tab Panel */
var _mainTabPanel = {
		
		_panel: Ext.create('Ext.tab.Panel', {
			region: 'center',
			width: '100%',
		    height: '100%'
		}),
		

		//Tabs
		_tab_stato: null,
			_tabella_stato_tavolo: null,
			_tabella_stato_cameriere: null,
			_tabella_stato_cuoco: null,
		_tab_menu: null,
		_tab_camerieri: null,
		_tab_opzioni: null,
		_tab_gestione_tavolo: null,
			_albero_gestione_tavolo: null,
		_tab_gestione_personale: null,
		_tab_gestione_menu: null,
		
		

		
		getPanel 	: function(){	return this._panel;},
		createTabStato : function(){
			
			this._tabella_stato_tavolo = Ext.create('Ext.grid.Panel', {
				title: 'Stato: tavoli',
				id: 'tabella_stato_tavolo',
				margin: '5 5 5 5',
				autoScroll: true,
		        store: Ext.data.StoreManager.lookup('datastore_stato_tavolo'),
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
		                	Ext.getStore('datastore_stato_tavolo').clearGrouping();
		                }
		            },{
		                tooltip: 'Toggle the visibility of the summary row',
		                text: 'Piano',
		                handler: function(){
		                	Ext.getStore('datastore_stato_tavolo').group('nomePiano');
		                }
		            },{
		                tooltip: 'Toggle the visibility of the summary row',
		                text: 'Area',
		                handler: function(){
		                	Ext.getStore('datastore_stato_tavolo').group('nomeArea');
		                }
		            },{
		                tooltip: 'Toggle the visibility of the summary row',
		                text: 'Stato',
		                handler: function(){
		                	Ext.getStore('datastore_stato_tavolo').group('statoTavolo');
		                }
		            },'->',{
		                text: 'Aggiorna',
		                iconCls: 'icon-add',
		                handler: function(){
		                	Ext.getStore('datastore_stato_tavolo').load();
		                }
		            },{
		                text: 'Add',
		                iconCls: 'icon-add',
		                handler: function(){
		                    // empty record
		                	Ext.getStore('datastore_stato_tavolo').insert(0, new StatoTavolo());
		                    //rowEditing.startEdit(0, 0);
		                }
		            }, '-', {
		                text: 'Delete',
		                iconCls: 'icon-delete',
		                handler: function(){
		                    var selection = Ext.getStore('datastore_stato_tavolo').remove(Ext.getStore('datastore_stato_tavolo').getAt(0));
		                    Ext.getStore('datastore_stato_tavolo').sync();
		                }
		            }]
		        }]	//Fine dockeditems

		        
			}),
			
			this._tabella_stato_cameriere = Ext.create('Ext.grid.Panel', {
				title: 'Stato: camerieri',
				margin: '5 5 5 5',
		        store: Ext.data.StoreManager.lookup('datastore_stato_tavoli'),
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
		                    store.insert(0, new StatoTavolo());
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
			
			this._tabella_stato_cuoco = Ext.create('Ext.grid.Panel', {
				title: 'Stato: cucina',
				margin: '5 5 5 5',
		        store: Ext.data.StoreManager.lookup('datastore_stato_tavoli'),
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
		                    store.insert(0, new StatoTavolo());
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

		        
			});
			
			
			this._tab_stato = Ext.create('Ext.panel.Panel', {
				layout: 'vbox',
				title: 'Stato locale',
				width: '100%',
			    height: '100%',
			    autoScroll : true,
			    items: [
			            this._tabella_stato_tavolo,
			            this._tabella_stato_cameriere,
			            this._tabella_stato_cuoco
			    ]
			});
		},
		addTabStato : function(){
			this.createTabStato();
			this._panel.add(this._tab_stato);
		},

		createTabGestioneTavolo : function(){
			this._albero_gestione_tavolo = Ext.create('Ext.tree.Panel', {
			    title: 'Simple Tree',
			    width: 500,
			    height: 500,
			    rootVisible: true,
			    store: Ext.data.StoreManager.lookup('datastore_gestione_tavolo'),
			    dockedItems: [{
		            xtype: 'toolbar',
		            dock: 'bottom',
		            //height: 100,
		            items: ['->',{
		                text: 'Aggiorna',
		                iconCls: 'icon-add',
		                handler: function(){
		                	Ext.getStore('datastore_gestione_tavolo').load();
		                }
		            },{
		                text: 'Add',
		                iconCls: 'icon-add',
		                handler: function(){
		                	alert("ADDDD");
		                	var selModel = _mainTabPanel._albero_gestione_tavolo.getSelectionModel();
		                    var node = selModel.getLastSelected();
		                    alert(node.get('id')+" - "+node.get('text'));
		                    node = Ext.getStore('datastore_gestione_tavolo').getNodeById( node.get('id') );
		                    var new_record = Ext.create('nodoGestioneTavolo', {
		                    	//id: 12345,
		                    	text: 'nuovo oggetto',
		                    	parentId: 123456,
		                    	name: 'Ed Spencer',
		                    	tipo: 'piano',
		                    	email: 'ed@sencha.com'
		                    });
		                    
		                    alert("Prima: "+new_record.get('id'));
		                    new_record.save();
		                    alert("Dopo: "+new_record.get('id'));
		                    
		                    node.appendChild(new_record);
		                    
		                    //Ext.getStore('datastore_gestione_tavolo').add(new_record);

		                    
		                }
		            }, '-', {
		                text: 'Delete',
		                iconCls: 'icon-delete',
		                handler: function(){
		                    var selection = Ext.getStore('datastore_gestione_tavolo').remove(Ext.getStore('datastore_gestione_tavolo').getAt(0));
		                    Ext.getStore('datastore_gestione_tavolo').sync();
		                }
		            }]
		        }],	//Fine dockeditems
		        
		        viewConfig: {
		        	//plugins: { ptype: 'treeviewdragdrop' },
		            stripeRows: true,
		            listeners: {
		                itemcontextmenu: function(view, rec, node, index, e) {
		                	var depth = rec.get("depth");
		                	var contextMenu = null;
		                	//_mainTabPanel._albero_gestione_tavolo.
		                	
		                	if(depth == 0){
		                		contextMenu = Ext.create('Ext.menu.Menu', {
			                        items: [{
				                        		text: 'Aggiungi piano',
				                        		handler: function(){
				                        			var tmp = new nodoGestioneTavolo();
				                        			
				        		                	view.add(tmp);
				                        		}
			                            	}
			                        ]
			                    });
		                	}else if(depth == 1){
		                		contextMenu = Ext.create('Ext.menu.Menu', {
			                        items: [
			                            {text: 'Aggiungi area'},
			                            {text: 'Rimuovi piano'}
			                        ]
			                    });
		                	}else if(depth == 2){
		                		contextMenu = Ext.create('Ext.menu.Menu', {
			                        items: [
			                            {text: 'Aggiungi tavolo'},
			                            {text: 'Rimuovi area'}
			                        ]
			                    });
		                	}else if(depth == 3){
		                		contextMenu = Ext.create('Ext.menu.Menu', {
			                        items: [
			                            {text: 'Modifica tavolo'},
			                            {text: 'Rimuovi tavolo'}
			                        ]
			                    });
		                	}else{
		                		contextMenu = Ext.create('Ext.menu.Menu', {
			                        items: [
			                            buyAction,
			                            sellAction
			                        ]
			                    });
		                	}
		                	var sellAction = Ext.create('Ext.Action', {
		                	        icon   : '../shared/icons/fam/delete.gif',  // Use a URL in the icon config
		                	        text: 'Sell stock',
		                	        disabled: true,
		                	        handler: function(widget, event) {
		                	            var rec = grid.getSelectionModel().getSelection()[0];
		                	            if (rec) {
		                	                alert("Sell " + rec.get('company'));
		                	            } else {
		                	                alert('Please select a company from the grid');
		                	            }
		                	        }
		                	    });
		                	    var buyAction = Ext.create('Ext.Action', {
		                	        iconCls: 'buy-button',
		                	        text: 'Buy stock',
		                	        disabled: true,
		                	        handler: function(widget, event) {
		                	            var rec = grid.getSelectionModel().getSelection()[0];
		                	            if (rec) {
		                	                alert("Buy " + rec.get('company'));
		                	            } else {
		                	                alert('Please select a company from the grid');
		                	            }
		                	        }
		                	    });
		                	    
		                    contextMenu.showAt(e.getXY());
		                    return false;
		                }
		            }
		        },
			});
			
			this._tab_gestione_tavolo = Ext.create('Ext.panel.Panel', {
				layout: 'vbox',
				title: 'Gestione tavoli',
				width: '100%',
			    height: '100%',
			    autoScroll : true,
			    items: [
			            this._albero_gestione_tavolo
			    ]
			});
		},
		addTabGestioneTavolo : function(){
			this.createTabGestioneTavolo();
			this._panel.add(this._tab_gestione_tavolo);
		}
		
};




/*

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
        }]
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




function getPanelTabStatoTabella(){
	
	
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
        minHeight: 100,
        width: '100%',
       	//height: 400,
        
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
	    },

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
*/