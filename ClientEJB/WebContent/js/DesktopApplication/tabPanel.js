/* Creo l'oggetto rappresentante il Main Tab Panel */
var _mainTabPanel = {
		
		_panel: Ext.create('Ext.tab.Panel', {
			id: 'main_tabPanel',
			region: 'center',
			layout: 'fit',
			width: '100%',
		    height: '100%',
		    listeners: {
		    	tabchange: function( tabPanel, newCard, oldCard, eOpts ){
		    		Ext.getCmp('viewport_east').collapse( Ext.Component.DIRECTION_RIGHT, true ); 
		    	}
		    }
		}),
		

		//Tabs
		_tab_stato: null,
			_tabella_stato_tavolo: null,
			_tabella_stato_cameriere: null,
			_tabella_stato_cuoco: null,
			_tabella_gestione_personale: null,
		_tab_menu: null,
		_tab_camerieri: null,
		_tab_opzioni: null,
		_tab_gestione_tavolo: null,
			_albero_gestioneTavolo: null,
		_tab_gestione_personale: null,
		_tab_gestione_menu: null,
		
		_tab_storico_conti: null,
		
		_tabella_conto: null,
		_tabella_lista_conti_tavolo: null,

		
		getPanel 	: function(){	return this._panel;},
		createTabStato : function(){
			this._tabella_stato_tavolo = Ext.create('Ext.grid.Panel', {
				title: 'Stato: tavoli',
				flex: 3,
				align : 'stretch',
				id: 'tabella_stato_tavolo',
				margin: '2 2 2 2',
//				css:'background-color: #000000;border-style:solid;border-color:#0000ff;',
//				bodyStyle: {
//				    background: '#000000',
//				    padding: '10px'
//				},
				autoScroll: true,
		        store: Ext.getStore('datastore_stato_tavolo'),
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
		        		// Inizio definizione stato tavolo
			        	_mainTabPanel.visualizzaListaConti(record.get('idTavolo'));
			        	// Fine definizione stato tavolo
			        },
			        itemcontextmenu: function(view, rec, node, index, e) {
			        	if(rec.get('statoTavolo') == 'OCCUPATO'){
			        		
			        	}
			        	
	                	var contextMenu = null;
	                	console.debug('CONTEXT MENU');
	                	contextMenu = Ext.create('Ext.menu.Menu', {
	                		items: [{
			                			text: 'Modifica utente',
			                        	handler: function(){
			                        		_mainTabPanel.updateComponentePersonale(rec);
			                        	}
		                            }
		                        ]
		                });
	                	
	                    contextMenu.showAt(e.getXY());
//	                    return false;
	                }
			        
		        },
			    dockedItems: [{
		            xtype: 'toolbar',
		            dock: 'bottom',
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
		                iconCls: 'icon-refresh',
		                handler: function(){
		                	Ext.getStore('datastore_stato_tavolo').load();
		                }
		            }]
		        }]	//Fine dockeditems

		        
			});
			
			this._tabella_stato_cameriere = Ext.create('Ext.grid.Panel', {
				id: 'tabella_statoCameriere',
				flex: 1,
				title: 'Stato: camerieri',
				margin: '2 2 2 2',
		        store: Ext.getStore('datastore_stato_tavolo'),
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
			        	_viewPort_panel_east.removeAll(false);
			        	// Inizio definizione stato tavolo
			        	//console.debug(view);
			        	console.debug(record);
			        	console.debug(item);
			        	_viewPort_panel_east.setTitle(index);
			        	_viewPort_panel_east.add({
			        		xtype: 'label',
			        		text: '<h1>Tavolo:</h1> '+record.get('Tavolo')
			        	});
			        	// Fine definizione stato tavolo
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
		                iconCls: '/ExtJS/resources/themes/images/gray/tree/drop-add.gif',
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
			
			this._tabella_stato_cuoco = Ext.create('Ext.grid.Panel', {
				id: 'tabella_statoCuoco',
				flex: 1,
				title: 'Stato: cucina',
				margin: '2 2 2 2',
		        store: Ext.getStore('datastore_stato_tavolo'),
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
			        	_viewPort_panel_east.removeAll(false);
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
		                iconCls: 'icon-refresh',
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
				id:	'main_tabPanel_stato',
				
//				layout: {
//			        type: 'vbox',
//			        align: 'stretch'
//			    },
			    
			    layout: {
			        type: 'accordion',
			        align: 'stretch',
			        //itemCls: 'Css/custom.css'
			        //multi: true
			        //css:'background-color: #000000;border-style:solid;border-color:#0000ff;'
			    },
			    defaults: {
			        // applied to each contained panel
			        //bodyStyle: 'padding:15px'
//			    	bodyStyle: {
//					    background: '#000000',
//					    padding: '10px'
//					}
			    },
			    layoutConfig: {
			        // layout-specific configs go here
			        titleCollapse: false,
			        animate: true,
			        activeOnTop: true
			        
			    },
				
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
			this._albero_gestioneTavolo = Ext.create('Ext.tree.Panel', {
				id: 'albero_gestioneTavolo',
			    title: 'Gestione ristorante: piani, aree, tavoli',
			    //,
			    
			    //height: 500,
			    align : 'stretch',
			    flex: 1,
			    width: '75%',
			    rootVisible: true,
			    useArrows: true,
			    store: Ext.getStore('datastore_gestione_tavolo'),
			    dockedItems: [{
		            xtype: 'toolbar',
		            dock: 'bottom',
		            //height: 100,
		            items: ['->',{
		                text: 'Aggiorna',
		                iconCls: 'icon-refresh',
		                handler: function(){
		                	Ext.getStore('datastore_gestione_tavolo').load();
		                }
		            }/*,{
		                text: 'Add',
		                iconCls: 'icon-add',
		                handler: function(){
		                	
		                    
		                }
		            }, '-', {
		                text: 'Delete',
		                iconCls: 'icon-delete',
		                handler: function(){
		                	
		                }
		            }*/]
		        }],	//Fine dockeditems
		        
		        viewConfig: {
		        	//plugins: { ptype: 'treeviewdragdrop' },
		            stripeRows: true,
		            listeners: {
		            	itemdblclick: function( view, rec,item,index,e,eOpts ){
//		            		var lastSelected = Ext.getCmp('albero_gestioneTavolo').getSelectionModel().getLastSelected();
		            		if(rec.get('tipo')==3)
		            			_mainTabPanel.updateNodeGestioneTavolo(rec);
		            	},
		                itemcontextmenu: function(view, rec, node, index, e) {
		                	var depth = rec.get("depth");
		                	var contextMenu = null;
		                	//_mainTabPanel._albero_gestioneTavolo.
		                	
		                	if(depth == 0){
		                		contextMenu = Ext.create('Ext.menu.Menu', {
			                        items: [{
				                        		text: 'Aggiungi piano',
				                        		handler: function(){
//				                        			var lastSelected = Ext.getCmp('albero_gestioneTavolo').getSelectionModel().getLastSelected();
				                        			_mainTabPanel.addNewNodeGestioneTavolo(rec);
				                        		}
			                            	}
			                        ]
			                    });
		                	}else if(depth == 1){
		                		contextMenu = Ext.create('Ext.menu.Menu', {
			                        items: [
			                            {text: 'Aggiungi area',
			                        		handler: function(){
//			                        			var lastSelected = Ext.getCmp('albero_gestioneTavolo').getSelectionModel().getLastSelected();
			                        			_mainTabPanel.addNewNodeGestioneTavolo(rec);
			                        		}
			                            },{
			                            	text: 'Modifica piano',
			                            	handler: function(){
//			                        			var lastSelected = Ext.getCmp('albero_gestioneTavolo').getSelectionModel().getLastSelected();
			                        			_mainTabPanel.updateNodeGestioneTavolo(rec);
			                            	}
			                            },{
			                            	text: 'Rimuovi piano',
			                            	handler: function(){
//			                            		var lastSelected = Ext.getCmp('albero_gestioneTavolo').getSelectionModel().getLastSelected();
			                            		_mainTabPanel.deleteNodeGestioneTavolo(rec);
			                            	}
			                            }
			                        ]
			                    });
		                	}else if(depth == 2){
		                		contextMenu = Ext.create('Ext.menu.Menu', {
			                        items: [
			                            {text: 'Aggiungi tavolo',
			                        		handler: function(){
//			                        			var lastSelected = Ext.getCmp('albero_gestioneTavolo').getSelectionModel().getLastSelected();
			                        			_mainTabPanel.addNewNodeGestioneTavolo(rec);
			                        		}
			                            },{
			                            	text: 'Modifica area',
			                            	handler: function(){
//			                        			var lastSelected = Ext.getCmp('albero_gestioneTavolo').getSelectionModel().getLastSelected();
			                        			_mainTabPanel.updateNodeGestioneTavolo(rec);
			                            	}
			                            },{
			                            	text: 'Rimuovi area',
			                            	handler: function(){
//			                            		var lastSelected = Ext.getCmp('albero_gestioneTavolo').getSelectionModel().getLastSelected();
			                            		_mainTabPanel.deleteNodeGestioneTavolo(rec);
			                            	}
			                            }
			                        ]
			                    });
		                	}else if(depth == 3){
		                		contextMenu = Ext.create('Ext.menu.Menu', {
			                        items: [
			                            {
			                            	text: 'Modifica tavolo',
			                            	handler: function(){
//			                        			var lastSelected = Ext.getCmp('albero_gestioneTavolo').getSelectionModel().getLastSelected();
			                        			_mainTabPanel.updateNodeGestioneTavolo(rec);
			                            	}
			                            },{
			                            	text: 'Rimuovi tavolo',
			                            	handler: function(){
//			                            		var lastSelected = Ext.getCmp('albero_gestioneTavolo').getSelectionModel().getLastSelected();
			                            		_mainTabPanel.deleteNodeGestioneTavolo(rec);
			                            	}
			                            }
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
				id:	'main_tabPanel_gestioneTavolo',
				layout: {
			        type: 'vbox',
			        align: 'stretch'
			    },
				title: 'Gestione tavoli',
				width: '100%',
			    height: '100%',
			    autoScroll : true,
			    items: [
			            this._albero_gestioneTavolo
			    ]
			});
		},
		addTabGestioneTavolo : function(){
			this.createTabGestioneTavolo();
			this._panel.add(this._tab_gestione_tavolo);
		},
		addNewNodeGestioneTavolo : function(parentNode){
			var tmp = Ext.getCmp('window_inserimentoNodoGestioneTavolo');
			if(tmp) tmp.destroy();
			tmp = Ext.getCmp('form_gestioneTavolo');
			if(tmp) tmp.destroy();
			var askWindow = Ext.create('Ext.window.Window', {
				id: 'window_inserimentoNodoGestioneTavolo',
        	    title: 'Hello',
        	    modal: true,
//        	    height: 400,
//        	    width: 400,
//        	    layout: 'fit',
        	    layout: {
			        type: 'auto',
			        pack: 'center'
			    },
        	    listeners:{
        	    	afterrender: function( thisWindow, eOpts ){
        	    		var nuovaAltezza = Ext.getCmp('form_gestioneTavolo').getHeight()+37;
        	    		thisWindow.setHeight( nuovaAltezza );
        	    	}
        	    }
        	});
			
			var form = Ext.create('Ext.form.Panel', {
				id: 'form_gestioneTavolo',
				border: false,
    	        defaultType: 'textfield',
    	        url: 'gestioneTavolo',
    	        
    	        items: [{
			        fieldLabel: 'ID',
			        name: 'id'
			    },{
			        fieldLabel: 'ParentId',
			        name: 'parentId'
			    },{
			        fieldLabel: 'Depth',
			        name: 'tipo'
			    },{
			        fieldLabel: 'Nome',
			        name: 'nome',
			        allowBlank: false
			    },{
			    	xtype: 'numberfield',
			        fieldLabel: 'Numero piano',
			        name: 'numeroPiano'
			    },{
			    	xtype: 'numberfield',
			        fieldLabel: 'NumPosti',
			        name: 'numPosti'
			    },{
			        fieldLabel: 'Descrizione',
			        name: 'descrizione'
			    },{
			    	xtype: 'checkboxfield',
			        fieldLabel: 'Attivo',
			        name: 'enabled'
			    }],
			    
			    buttons: [{
    	            text: 'Reset',
    	            handler: function() {
    	                this.up('form').getForm().reset();
    	            }
    	        },{
			        text: 'Submit',
			        handler: function() {
			            // The getForm() method returns the Ext.form.Basic instance:
			            var form = this.up('form').getForm();
			            if (form.isValid()) {
			                // Submit the Ajax request and handle the response
			                form.submit({
			                	params : {
			                    	action : 'create'
			                    },
			                    success: function(form, action) {
			                    	Ext.getCmp('window_inserimentoNodoGestioneTavolo').destroy();
			                    	var parentId = null;
			                    	var nuovo_nodo = Ext.create('nodoGestioneTavolo', {
			                    		id: 			action.result.data[0].id,
			                    		parentId: 		action.result.data[0].parentId,
			                    		nome: 			action.result.data[0].nome,
				                    	tipo: 			action.result.data[0].tipo,
				                    	descrizione: 	action.result.data[0].descrizione,
				                    	enabled: 		action.result.data[0].enabled,
				                    	numeroPiano:	action.result.data[0].numeroPiano,
				                    	numPosti:		action.result.data[0].numPosti
			                    	});
			                    	
			                    	parentId = action.result.data[0].parentId;
//			                    	
//			                    	
//			                    	
//			                    	switch(action.result.data[0].tipo){
//			                    		case 1: {
//						                    		nuovo_nodo.set('parentId','root');
//						                    		parentId = 'root';
//				                    				break;
//			                    		}
//			                    		case 2: {
//						                    		parentId = 'P'+action.result.data[0].parentId;
//						                    		break;
//					                    }
//			                    		case 3: {
//						                    		parentId = 'A'+action.result.data[0].parentId;
//						                    		break;
//					                    }
//			                    		default: {parentId = 'root';	break;}
//			                    	}
////			                    	console.debug("PARENT ID NUOVO NODO: "+parentId);
//			                    	console.debug('Nuovo nodo creato: '+nuovo_nodo.get('id')+' - '+nuovo_nodo.get('parentId')+' - '+nuovo_nodo.get('text')+' - '+nuovo_nodo.get('tipo'));
			                    	var nodo_padre = Ext.getStore('datastore_gestione_tavolo').getNodeById(parentId);
			                    	nodo_padre.appendChild(nuovo_nodo);
			                    	
			                    	Ext.Msg.alert('Info: ', action.result.message);
			                    	
			                    },
			                    failure: function(form, action) {
			                        Ext.Msg.alert('Errore: ', action.result.message);
			                    }
			                });
			            }
			        }
			    }]
			});
			
			var parentDepth = parentNode.get('depth');
			
			form.getForm().findField('id').setValue(parentNode.get('id'));
			form.getForm().findField('id').hide();
			form.getForm().findField('parentId').setValue(parentNode.get('id'));
			form.getForm().findField('parentId').hide();
			form.getForm().findField('tipo').setValue(parentDepth+1);
			form.getForm().findField('tipo').hide();
			form.getForm().findField('enabled').setValue(true);
			
			askWindow.add(form);
			
			
			if(parentDepth == 0){
				askWindow.setTitle('Aggiungi nuovo piano');
				form.getForm().findField('numPosti').hide();
				askWindow.show();
			}else if(parentDepth == 1){
				askWindow.setTitle('Aggiungi nuova area in '+parentNode.get('text'));
				form.getForm().findField('numPosti').hide();
				form.getForm().findField('numeroPiano').hide();
				askWindow.show();
			}else if(parentDepth == 2){
				askWindow.setTitle('Aggiungi nuovo tavolo in '+parentNode.get('text'));
				form.getForm().findField('descrizione').hide();
				form.getForm().findField('numeroPiano').hide();
				askWindow.show();
			}else{
				return;
			}
			
		},
		
		updateNodeGestioneTavolo : function(selectedNode){
			var form_tmp = Ext.create('Ext.form.Panel', {
				id: 'form_gestioneTavolo_updateNode',
			    title: 'Simple Form',
			    bodyPadding: 5,
			    width: '100%',
			    height: '100%',

			    url: 'gestioneTavolo',

			    // Fields will be arranged vertically, stretched to full width
			    layout: 'anchor',
			    defaults: {
			        anchor: '100%'
			    },

			    // The fields
			    defaultType: 'textfield',
			    items: [{
			        fieldLabel: 'ID',
			        name: 'id',
			        hidden: true
			    },{
			        fieldLabel: 'ParentId',
			        name: 'parentId',
			        hidden: true
			    },{
			        fieldLabel: 'Depth',
			        name: 'tipo',
			        hidden: true
			    },{
			        fieldLabel: 'Nome',
			        name: 'nome',
			        allowBlank: false
			    },{
			    	xtype: 'numberfield',
			        fieldLabel: 'Numero piano',
			        name: 'numeroPiano',
			        hidden: true
			    },{
			    	xtype: 'numberfield',
			        fieldLabel: 'NumPosti',
			        name: 'numPosti',
			        hidden: true
			    },{
			        fieldLabel: 'Descrizione',
			        name: 'descrizione',
			        hidden: true
			    },{
			    	xtype: 'checkboxfield',
			        fieldLabel: 'Attivo',
			        name: 'enabled'
			    }],

			    // Reset and Submit buttons
			    buttons: [{
			        text: 'Reset',
			        handler: function() {
			            //Ricarica i valori del nodo selezionato
			        	this.up('form').getForm().loadRecord(selectedNode);
			        }
			    }, {
			        text: 'Submit',
			        formBind: true, //only enabled once the form is valid
			        disabled: true,
			        handler: function() {
			            var form = this.up('form').getForm();
			            if (form.isValid()) {
			                form.submit({
			                	params : {
			                    	action : 'update'
			                    },
			                    success: function(form, action) {
//			                       Ext.Msg.alert('Success', action.result.message);
			                    	new Ext.ux.Notification({
				        				iconCls:	'x-icon-information',
				        				title:	  'Successo',
				        				html:		action.result.message,
				        				autoDestroy: true,
				        				hideDelay:  2000,
				        			}).show(document);
			                       var updatedNode = Ext.getStore('datastore_gestione_tavolo').getNodeById( action.result.data[0].id );
			                       updatedNode.set('nome',action.result.data[0].nome);
			                       updatedNode.set('text',action.result.data[0].nome);
			                       updatedNode.set('descrizione',action.result.data[0].descrizione);
			                       updatedNode.set('tipo',action.result.data[0].tipo);
			                       updatedNode.set('numPosti',action.result.data[0].numPosti);
			                       updatedNode.set('numeroPiano',action.result.data[0].numeroPiano);
			                       updatedNode.set('enabled',action.result.data[0].enabled);
//			                       if(action.result.data[0].enabled == true){
//			                    	   updatedNode.set('enabled','on');
//			                       }else{
//			                    	   updatedNode.set('enabled','off');
//			                       }
			                       Ext.getCmp('viewport_east').collapse();
			                    },
			                    failure: function(form, action) {
			                        Ext.Msg.alert('Failed', action.result.message);
			                    }
			                });
			            }
			        }
			    }]
			});	//END FORM
			
			form_tmp.getForm().loadRecord(selectedNode);
			
			switch(selectedNode.get('tipo')){
			case 1: {
				form_tmp.setTitle('Modifica piano');
				form_tmp.getForm().findField('descrizione').setVisible(true);
				form_tmp.getForm().findField('numeroPiano').setVisible(true);
				break;
			}
			case 2: {
				form_tmp.setTitle('Modifica area');
				form_tmp.getForm().findField('descrizione').setVisible(true);
				break;
			}
			case 3: {
				form_tmp.setTitle('Modifica tavolo');
				form_tmp.getForm().findField('numPosti').setVisible(true);
				break;
			}
			}
			Ext.getCmp('viewport_east').removeAll(false);
			Ext.getCmp('viewport_east').expand();
			Ext.getCmp('viewport_east').add(Ext.getCmp('form_gestioneTavolo_updateNode'));
			
		},
		deleteNodeGestioneTavolo : function(selectedNode){
//			Ext.Msg.show({
//			     title:'Save Changes?',
//			     msg: 'You are closing a tab that has unsaved changes. Would you like to save your changes?',
//			     buttons: Ext.Msg.YESNO,
//			     icon: Ext.Msg.QUESTION
//			});
			Ext.MessageBox.confirm('Conferma', 'Sei sicuro di voler rimuovere '+selectedNode.get('nome')+'?', function(btn){
				if(btn == 'no') return;
				selectedNode.destroy({
					params : {
                    	action : 'delete'
                    },
	                success : function(record, action) {
//	                	Ext.Msg.alert('Success', action.result.message);
	                	new Ext.ux.Notification({
	        				iconCls:	'x-icon-information',
	        				title:	  'Successo',
	        				html:		action.result.message,
	        				autoDestroy: true,
	        				hideDelay:  2000,
	        			}).show(document);
	                },
	                failure: function(form, action) {
	                    Ext.Msg.alert('Failed', action.result.message);
	                }
	            });
			});
			
		},
		createTabGestioneMenu : function(){
			var tree = Ext.create('Ext.tree.Panel', {
				id: 'albero_gestioneMenu',
		        title: 'Gestione menù',
		        width: '100%',
		        flex: 1,
		        //height: 500,
		        //collapsible: true,
		        useArrows: true,
		        rootVisible: false,
		        store: Ext.getStore('datastore_gestione_menu'),
		        
		        dockedItems: [{
		            xtype: 'toolbar',
		            dock: 'bottom',
		            //height: 100,
		            items: ['->',{
		                text: 'Aggiorna',
		                iconCls: 'icon-refresh',
		                handler: function(){
		                	Ext.getStore('datastore_gestione_menu').load();
		                }
		            }],
		        }],
		        
		        viewConfig: {
		        	//plugins: { ptype: 'treeviewdragdrop' },
		            stripeRows: true,
		            listeners: {
		            	itemdblclick: function( view, rec,item,index,e,eOpts ){
//		            		var lastSelected = Ext.getCmp('albero_gestioneMenu').getSelectionModel().getLastSelected();
		            		if(rec.get('tipo')==3)
		            			_mainTabPanel.updateNodeGestioneTavolo(rec);
		            	},
		                itemcontextmenu: function(view, rec, node, index, e) {
		                	var depth = rec.get("depth");
		                	var contextMenu = null;
//		                	console.debug('PROFONDITA: '+depth);
		                	
		                	if(depth == 1){
		                		contextMenu = Ext.create('Ext.menu.Menu', {
			                        items: [
			                            {
			                            	text: 'Aggiungi voce di menù',
			                        		handler: function(){
			                        			//var lastSelected = Ext.getCmp('albero_gestioneTavolo').getSelectionModel().getLastSelected();
			                        			_mainTabPanel.addNewNodeGestioneMenu(rec,true);
			                        		}
			                            },{
			                            	text: 'Aggiungi categoria',
			                        		handler: function(){
			                        			//var lastSelected = Ext.getCmp('albero_gestioneTavolo').getSelectionModel().getLastSelected();
			                        			_mainTabPanel.addNewNodeGestioneMenu(rec,false);
			                        		}
			                            },{
			                            	text: 'Visualizza variazioni',
			                            	handler: function(){
			                        			//var lastSelected = Ext.getCmp('albero_gestioneTavolo').getSelectionModel().getLastSelected();
			                            		_mainTabPanel.showGestioneVariazioniMenu(rec.get('id').substring(1),rec.get('nome'));
			                            	}
			                            }/*,{
			                            	text: 'Modifica categoria',
			                            	handler: function(){
			                        			//var lastSelected = Ext.getCmp('albero_gestioneTavolo').getSelectionModel().getLastSelected();
			                            		_mainTabPanel.updateNodeGestioneMenu(rec);
			                            	}
			                            }*/
			                        ]
			                    });
		                	}else if(depth >= 2){
		                		if(	rec.isLeaf() ){
		                			contextMenu = Ext.create('Ext.menu.Menu', {
				                        items: [
				                            {text: 'Modifica voce menù',
				                        		handler: function(){
				                        			//var lastSelected = Ext.getCmp('albero_gestioneTavolo').getSelectionModel().getLastSelected();
				                        			_mainTabPanel.updateNodeGestioneMenu(rec);
				                        		}
				                            },{
				                            	text: 'Rimuovi voce menù',
				                            	handler: function(){
				                        			//var lastSelected = Ext.getCmp('albero_gestioneTavolo').getSelectionModel().getLastSelected();
				                            		_mainTabPanel.deleteNodeGestioneMenu(rec);
				                            	}
				                            }
				                        ]
				                    });
		                		}else{
		                			contextMenu = Ext.create('Ext.menu.Menu', {
				                        items: [
				                            {
				                            	text: 'Aggiungi voce di menù',
				                        		handler: function(){
				                        			//var lastSelected = Ext.getCmp('albero_gestioneTavolo').getSelectionModel().getLastSelected();
				                        			_mainTabPanel.addNewNodeGestioneMenu(rec,true);
				                        		}
				                            },{
				                            	text: 'Aggiungi categoria',
				                        		handler: function(){
				                        			//var lastSelected = Ext.getCmp('albero_gestioneTavolo').getSelectionModel().getLastSelected();
				                        			_mainTabPanel.addNewNodeGestioneMenu(rec,false);
				                        		}
				                            },{
				                            	text: 'Modifica categoria',
				                            	handler: function(){
				                        			//var lastSelected = Ext.getCmp('albero_gestioneTavolo').getSelectionModel().getLastSelected();
				                            		_mainTabPanel.updateNodeGestioneMenu(rec);
				                            	}
				                            },{
				                            	xtype: 'menuseparator'
				                            },{
				                            	text: 'Visualizza variazioni',
				                            	handler: function(){
				                        			//var lastSelected = Ext.getCmp('albero_gestioneTavolo').getSelectionModel().getLastSelected();
				                            		_mainTabPanel.showGestioneVariazioniMenu(rec.get('id').substring(1),rec.get('nome'));
				                            	}
				                            },{
				                            	xtype: 'menuseparator'
				                            },{
				                            	text: 'Rimuovi categoria',
				                            	handler: function(){
				                        			//var lastSelected = Ext.getCmp('albero_gestioneTavolo').getSelectionModel().getLastSelected();
				                            		_mainTabPanel.deleteNodeGestioneMenu(rec);
				                            	}
				                            }
				                        ]
				                    });
		                		}
		                	}
		                	/*
		                	else if(depth == 3){
		                		contextMenu = Ext.create('Ext.menu.Menu', {
			                        items: [
			                            {
			                            	text: 'Modifica tavolo',
			                            	handler: function(){
			                        			var lastSelected = Ext.getCmp('albero_gestioneTavolo').getSelectionModel().getLastSelected();
			                        			_mainTabPanel.updateNodeGestioneTavolo(lastSelected);
			                            	}
			                            },{
			                            	text: 'Rimuovi tavolo',
			                            	handler: function(){
			                            		var lastSelected = Ext.getCmp('albero_gestioneTavolo').getSelectionModel().getLastSelected();
			                            		_mainTabPanel.deleteNodeGestioneTavolo(lastSelected);
			                            	}
			                            }
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
		                	*/
		                	    
		                    contextMenu.showAt(e.getXY());
		                    return false;
		                }
		            },
		            

		        },
		        //multiSelect: true,
		        //singleExpand: true,
		        //the 'columns' property is now 'headers'
		        /*columns: [{
		            xtype: 'treecolumn', //this is so we know which column will show the tree
		            text: 'Task',
		            flex: 2,
		            sortable: true,
		            dataIndex: 'task'
		        },{
		            //we must use the templateheader component so we can use a custom tpl
		            xtype: 'templatecolumn',
		            text: 'Duration',
		            flex: 1,
		            sortable: true,
		            dataIndex: 'duration',
		            align: 'center',
		            //add in the custom tpl for the rows
		            tpl: Ext.create('Ext.XTemplate', '{duration:this.formatHours}', {
		                formatHours: function(v) {
		                    if (v < 1) {
		                        return Math.round(v * 60) + ' mins';
		                    } else if (Math.floor(v) !== v) {
		                        var min = v - Math.floor(v);
		                        return Math.floor(v) + 'h ' + Math.round(min * 60) + 'm';
		                    } else {
		                        return v + ' hour' + (v === 1 ? '' : 's');
		                    }
		                }
		            })
		        },{
		            text: 'Assigned To',
		            flex: 1,
		            dataIndex: 'user',
		            sortable: true
		        }]*/
		    });
		},
		
		addTabGestioneMenu : function(){
			this.createTabGestioneMenu();
			this._tab_menu = Ext.create('Ext.panel.Panel', {
				id:	'main_tabPanel_gestioneMenu',
				layout: {
			        type: 'vbox',
			        align: 'stretch'
			    },
				title: 'Gestione menù',
				width: '100%',
			    height: '100%',
			    autoScroll : true,
			    items: [
			            Ext.getCmp('albero_gestioneMenu')
			    ]
			});
		
			Ext.getCmp('main_tabPanel').add( Ext.getCmp('main_tabPanel_gestioneMenu') );
		},
		createFormGestioneMenu : function(){
			var tmp = Ext.getCmp('form_gestioneMenu');
			if(tmp) tmp.destroy();
			
			return Ext.create('Ext.form.Panel', {
					id: 'form_gestioneMenu',
					border: false,
	    	        defaultType: 'textfield',
	    	        url: 'gestioneMenu',
	    	        items: [{
				        fieldLabel: 'ID',
				        name: 'id'
				    },{
				        fieldLabel: 'ParentId',
				        name: 'parentId'
				    },{
				        fieldLabel: 'Depth',
				        name: 'depth'
				    },{
				        fieldLabel: 'Tipo',
				        name: 'tipo'
				    },{
				        fieldLabel: 'Nome',
				        name: 'nome',
				        allowBlank: false
				    },{
				        fieldLabel: 'Descrizione',
				        name: 'descrizione'
				    },{
				    	xtype: 'numberfield',
				    	step: 0.1,
				        fieldLabel: 'Prezzo',
				        name: 'prezzo'
				    }],
				    
				    buttons: [{
	    	            text: 'Reset',
	    	            handler: function() {
	    	            	this.up('form').getForm().findField('nome').setValue('');
	    	            	this.up('form').getForm().findField('descrizione').setValue('');
	    	            	this.up('form').getForm().findField('prezzo').setValue('');
	    	            	
	    	            }
	    	        },{
				        text: 'Submit',
				        handler: function() {
				            // The getForm() method returns the Ext.form.Basic instance:
				            var form = this.up('form').getForm();
				            if (form.isValid()) {
				                // Submit the Ajax request and handle the response
				                form.submit({
				                	params : {
				                    	action : this.up('form').action
				                    },
				                    success: function(form, action) {
//				                    	console.debug('OK');
				                    	if( action.result.action == 'create' ){
//				                    		console.debug('OK SUCCESSO CREATE');
				                    		var nuovo_nodo = Ext.create('nodoGestioneMenu', {
					                    		id: 			action.result.data[0].id,
					                    		parentId: 		action.result.data[0].parentId,
					                    		nome: 			action.result.data[0].nome,
					                    		tipo: 			action.result.data[0].tipo,
					                    		descrizione:	action.result.data[0].descrizione,
					                    		prezzo:			action.result.data[0].prezzo
					                    	});
				                    		Ext.getStore('datastore_gestione_menu').getNodeById(nuovo_nodo.get('parentId')).appendChild(nuovo_nodo);
				                    	
				                    	}else if ( action.result.action == 'update' ){
//				                    		console.debug('OK SUCCESSO UPDATE');
				                    		var updatedNode = Ext.getStore('datastore_gestione_menu').getNodeById(action.result.data[0].id);
					                    		//updatedNode.set('id',action.result.data[0].id);
					                    		//updatedNode.set('parentId',action.result.data[0].parentId);
					                    		updatedNode.set('nome',action.result.data[0].nome);
					                    		//updatedNode.set('tipo',action.result.data[0].tipo);
					                    		updatedNode.set('descrizione',action.result.data[0].descrizione);
					                    		updatedNode.set('prezzo',action.result.data[0].prezzo);
					                    		if(action.result.data[0].tipo == 1){
					                    			updatedNode.set('text',action.result.data[0].nome);
					                    		}else{
					                    			updatedNode.set('text',action.result.data[0].nome+' - ['+action.result.data[0].prezzo+'€]');
					                    		}
					                    	Ext.getCmp('viewport_east').collapse();
				                    	}
				                    	
				                    	
				                    	
				                    	
				                    	new Ext.ux.Notification({
					        				iconCls:	'x-icon-information',
					        				title:	  'Successo',
					        				html:		action.result.message,
					        				autoDestroy: true,
					        				hideDelay:  2000,
					        			}).show(document);
				                    	
//				                    	Ext.Msg.alert('Info: ', action.result.message);
				                    	Ext.getCmp('window_inserimentoNodoGestioneMenu').destroy();
//				                    	Ext.getCmp('form_gestioneMenu').destroy();
				                    	
				                    },
				                    failure: function(form, action) {
				                        Ext.Msg.alert('Errore: ', action.result.message);
				                    }
				                });
				            }
				        }
				    }]
				});
		},
		addNewNodeGestioneMenu : function(parentNode,isVoceMenu){

			var a = Ext.getCmp('window_inserimentoNodoGestioneMenu');
			var b = Ext.getCmp('form_gestioneMenu');
			if(a != undefined) a.destroy();
			if(b != undefined) b.destroy();
			var form = this.createFormGestioneMenu();
			form.action = 'create';
			form.isVoceMenu = isVoceMenu;
			var title = 'titolo';
			
			form.getForm().findField('id').hide();
			form.getForm().findField('parentId').hide();
			form.getForm().findField('parentId').setValue(parentNode.get('id'));
			form.getForm().findField('depth').hide();
			form.getForm().findField('tipo').hide();
			
			if(isVoceMenu == true){
				title = 'Aggiungi nuova voce di menù';
				form.getForm().findField('tipo').setValue(2);
			}else{
				title = 'Aggiungi nuova categoria';
				form.getForm().findField('prezzo').hide();
				form.getForm().findField('parentId').setValue(parentNode.get('id'));
				form.getForm().findField('tipo').setValue(1);
			}
			
			var askWindow = Ext.create('Ext.window.Window', {
				id: 'window_inserimentoNodoGestioneMenu',
        	    title: title,
        	    expandOnShow : true,
        	    modal: true,
        	    //height: 400,
        	    //width: 400,
//        	    layout: 'fit',
//        	    layout: 'auto',
        	    layout: {
			        type: 'auto',
			        pack: 'center'
			    },
        	    items:[form],
        	    listeners:{
        	    	afterrender: function( thisWindow, eOpts ){
        	    		console.debug('Altezzaaaa');
        	    		thisWindow.setHeight( Ext.getCmp('form_gestioneMenu').getHeight()+37 );
        	    	}
        	    }
        	});
			//askWindow.add(form);
			
			
			
			askWindow.show();
		},
		updateNodeGestioneMenu : function(selectedNode){
			var a = Ext.getCmp('form_gestioneMenu');
			if(a != undefined) a.destroy();
			var form = this.createFormGestioneMenu();
			form.action = 'update';
			form.selectedNode = selectedNode;
			form.getForm().findField('id').hide();
			form.getForm().findField('parentId').hide();
			form.getForm().findField('depth').hide();
			form.getForm().findField('tipo').hide();
			if(selectedNode.get('tipo') == 2){
				form.setTitle('Modifica voce di menù');
			}else{
				form.setTitle('Modifica categoria');
				form.getForm().findField('prezzo').hide();
			}
			
			form.getForm().loadRecord(selectedNode);
			
			Ext.getCmp('viewport_east').removeAll(false);
			Ext.getCmp('viewport_east').expand();
			Ext.getCmp('viewport_east').add(Ext.getCmp('form_gestioneMenu'));
		},
		deleteNodeGestioneMenu : function(selectedNode){
			Ext.MessageBox.confirm('Conferma', 'Sei sicuro di voler rimuovere '+selectedNode.get('nome')+'?', function(btn){
				if(btn == 'no') return;
				selectedNode.destroy({
					params : {
                    	action : 'delete'
                    },
	                success : function(record, action) {
//	                	Ext.Msg.alert('Success', action.result.message);
	                	new Ext.ux.Notification({
	        				iconCls:	'x-icon-information',
	        				title:	  'Successo',
	        				html:		action.result.message,
	        				autoDestroy: true,
	        				hideDelay:  2000,
	        			}).show(document);
	                },
	                failure: function(form, action) {
	                    Ext.Msg.alert('Failed', action.result.message);
	                }
	            });
			});
		},
		showGestioneVariazioniMenu : function(idCategoria,nomeCategoria){
//			console.debug('IdCategoria '+idCategoria);
			var store = Ext.getStore('datastore_variazione_voce_menu');
			if(store) store.destroy();
			store = Ext.create('Ext.data.Store', {
				storeId: 'datastore_variazione_voce_menu',
				//groupField: 'nomeArea',
				model: 'variazioneVoceMenu',
				autoLoad: true,
				//autoSync: true,
				pageSize: 50,
				remoteFilter : true,
			
				listeners: {
					beforeload: function( store, operation, eOpts ){
						store.proxy.extraParams.idCategoria=idCategoria;
					},
					beforesync: function( options, eOpts ){
////									console.debug('beforesync');
////									console.debug(options);
//						//			console.debug(eOpts);
//									if(options.update){
////										console.debug('Create OR Update');
//									}
					},
					write: function(store, operation, eOpts ){
//						console.debug('writerello');
//						console.debug(eOpts);
//						console.debug(operation);
//						console.debug(store);
//						
//						operation.request.params.action = 'Cicisbeo';
//						
					},
					update: function( store, record, operation, eOpts ){
//						console.debug('updaterello');
//						console.debug(eOpts);
//						console.debug(operation);
//						console.debug(store);
//			//			Ext.data.Model.EDIT
//			//			Ext.data.Model.REJECT
//			//			Ext.data.Model.COMMIT
//						
					},
					remove: function( store, record, index, eOpts ){
//						console.debug('removerello');
//						console.debug(eOpts);
//						console.debug(index);
//						console.debug(record);
//						console.debug(store);
					},
					
					beforeappend: function( thisNode, newChildNode, eOpts ){
						var tipo = newChildNode.get("tipo");
						if( tipo == 1){			//CATEGORIA
							newChildNode.set('leaf', false);
							newChildNode.set('text', newChildNode.get('nome'));
							newChildNode.set('qtip', newChildNode.get('descrizione'));
			            	//newChildNode.set('id', 'P'+newChildNode.get('realId'));
			            	//newChildNode.set('parentId', newChildNode.get('parentId'));
			            	//newChildNode.set('icon', newChildNode.get('profile_image_url'));
			            }else if( tipo == 2){	//VOCE MENU
			            	newChildNode.set('leaf', true);
			            	newChildNode.set('text', newChildNode.get('nome')+' - ['+newChildNode.get('prezzo')+'€]');
			            	newChildNode.set('qtip', newChildNode.get('descrizione'));
			            	//newChildNode.set('id', 'A'+newChildNode.get('realId'));
			            	//newChildNode.set('parentId', 'P'+newChildNode.get('parentId'));
			            	//newChildNode.set('icon', newChildNode.get('profile_image_url'));
			            }
					},
					append: function( thisNode, newChildNode, index, eOpts ) {
						
				    }
			    },
//			    proxy: {
//			        type: 'rest',
//			        url : 'variazioneVoceMenu',
//			        appendId : false,
//			        writer: {
//			            type: 'singlepostnoaction'
//			            //type: 'json'
//			        },
//					reader: {
//				        type: 'json',
//				        idProperty: 'id',
//				        root: 'data'
//				    },
//				    actionMethods : {
//			            create : 'POST',
//			            read   : 'GET',
//			            update : 'POST',
//			            destroy: 'POST'
//			        }
//			    }
				
			});
			
			var rowEditing = Ext.create('Ext.grid.plugin.RowEditing',{
				listeners: {
					beforeedit: function(editor, e, eOpts ){
						if (editor.record.get('isEreditata') == true) {
							Ext.Msg.alert('Info: ', 'Non è possibile modificare variazioni ereditate da altre categorie. Per modificarle, occorre visualizzare le variazione dell\'apposita categoria a cui sono associate.');
							return false;
					  	}
						return true;
				    },
				    canceledit: function( grid, eOpts ){
				    	
				    },
				    edit: function( editor, e, eOpts ){
//				    	console.debug('EDITED KISSES');
//				    	console.debug(editor);
//				    	editor.record.save();
				    	editor.store.sync();
				    	
				    },
				    validateedit: function( editor, e, eOpts ){
				    	
				    }
				},
				failure: function(form, action) {
                    Ext.Msg.alert('Errore: ', 'sadsasdasd');
                }
			});
			
		    var grid = Ext.create('Ext.grid.Panel', {
		    	id: 'tabella_gestioneVariazioni',
		        //renderTo: document.body,
		        plugins: [rowEditing],
		        //height: '100%',
//		        flex: 1,
        	    //width: '100%',
		        frame: true,
		        autoscroll: true,
		        //title: 'Users',
//		        store: Ext.getStore('datastore_variazione_voce_menu'),
		        iconCls: 'icon-user',
		        //fields: ['id','parentId','nome','descrizione','prezzo','tipo','text','isEreditata'],
		        columns: [{
		            text: 'ID',
		            width: 40,
		            sortable: true,
		            dataIndex: 'id',
		            hidden: true
		        }, {
		            text: 'Nome',
		            flex: 1,
		            sortable: true,
		            dataIndex: 'nome',
		            field: {
		                xtype: 'textfield'
		            }
		        }, {
		            header: 'Descrizione',
		            flex: 1,
		            sortable: true,
		            dataIndex: 'descrizione',
		            field: {
		                xtype: 'textfield'
		            }
		        }, {
		            text: 'Prezzo',
		            width: 60,
		            sortable: true,
		            dataIndex: 'prezzo',
		            field: {
		            	xtype: 'numberfield'
		            }
		        }, {
		            text: 'Categoria di appartenenza',
		            flex: 1,
		            sortable: true,
		            dataIndex: 'categoriaDiAppartenenza',
		            field: {
		            	xtype: 'displayfield'
		            }
		        }],
		        features: [
					Ext.create('Ext.grid.feature.Grouping', {
						groupHeaderTpl: '{[titoloRaggruppamentoVariazioni(values)]} ({rows.length})',
//						groupHeaderTpl: 'Group: {name} {[(name=="true")]} {[(name==="true") ? "Editabili" : "Ereditate"]} {[readOut(values)]} ({rows.length})',
					//	groupHeaderTpl: 'Group: {name} ({rows.length})',
					//    groupHeaderTpl: 'Group: {name} ({rows.length})', //print the number of items in the group
					    //startCollapsed: true // start all groups collapsed
					})
				],
		        dockedItems: [{
		            xtype: 'toolbar',
		            dock: 'bottom',
		            items: ['->',{
		                text: 'Aggiorna',
		                iconCls: 'icon-update',
		                handler: function(){
		                	Ext.getStore('datastore_variazione_voce_menu').load();
		                	Ext.getCmp('tabella_gestioneVariazioni').determineScrollbars();
		                	Ext.getCmp('tabella_gestioneVariazioni').forceComponentLayout();
		                }
		            },{
		                text: 'Add',
		                iconCls: 'icon-add',
		                handler: function(){
		                	var emptyRecord = Ext.create('variazioneVoceMenu',{
		                    	action: 'create',
		                    	isEreditata: false,
		                    	idCategoria: idCategoria
		                    });
		                	Ext.getStore('datastore_variazione_voce_menu').insert(0,emptyRecord);
		                    rowEditing.startEdit(0, 0);
		                    Ext.getCmp('tabella_gestioneVariazioni').determineScrollbars();
		                    Ext.getCmp('tabella_gestioneVariazioni').forceComponentLayout();
		                }
		            }, '-', {
		                itemId: 'delete',
		                text: 'Delete',
		                iconCls: 'icon-delete',
		                disabled: true,
		                handler: function(){
		                    var selection = grid.getView().getSelectionModel().getSelection()[0];
		                    if (selection) {
		                    	if(selection.get('isEreditata')){//Se è una variazione ereditata non la posso cancellare
		                    		Ext.Msg.alert('Info: ', 'Non è possibile modificare variazioni ereditate da altre categorie. Per modificarle, occorre visualizzare le variazione dell\'apposita categoria a cui sono associate.');
		                    		return;	
		                    	}
		                    	
		                    	Ext.getStore('datastore_variazione_voce_menu').remove(selection);
		                    	Ext.getStore('datastore_variazione_voce_menu').sync();
		                    	Ext.getCmp('tabella_gestioneVariazioni').forceComponentLayout();
//		                    	selection.set('action','delete');
//		                    	Ext.getStore('datastore_variazione_voce_menu').remove(selection);
//		                    	selection.destroy({
//		                			params: {
//		                				action: 'delete'
//		                			},
//		                			success: function(form, action){
//		                				Ext.getStore('datastore_variazione_voce_menu').remove(selection);
//		                			}
//		                		});
		                    	
		                    	//Ext.getStore('datastore_variazione_voce_menu').remove(selection);
//		                    	selection.destroy({
//		                			params: {
//		                				action: 'delete'
//		                			},
//		                			success: function(form, action){
//		                				Ext.getStore('datastore_variazione_voce_menu').remove(selection);
//		                			}
//		                		});
		                    }
		                }
		            }]
		        }],
		        viewConfig: {
		        	emptyText:'Non ci sono variazioni disponibili.',
				    forceFit: true,
		            showPreview: true, // custom property
		            enableRowBody: true,
		            getRowClass: function(record, rowIndex, rowParams, store){
		                return record.get('isEreditata') ? 'gray-row' : '';
		            }
		        },
		        
//		        view: new Ext.grid.GroupingView({
//		            forceFit:true,
//		            groupTextTpl: '{text} ({[values.rs.length]} {[values.rs.length > 1 ? "Items" : "Item"]})'
//		        }),
		        store: Ext.getStore('datastore_variazione_voce_menu')
		    });
		    grid.getSelectionModel().on('selectionchange', function(selModel, selections){
		        grid.down('#delete').setDisabled(selections.length === 0);
		    });
		    var askWindow = Ext.create('Ext.window.Window', {
				id: 'window_gestionevariazionivocemenu',
        	    title: 'Elenco variazioni: '+nomeCategoria,
        	    height: 300,
        	    width: 550,
        	    layout: 'fit',
        	    modal: true
        	});
		    askWindow.add(grid);
			askWindow.show();
			Ext.getStore('datastore_variazione_voce_menu').group('isEreditata');
		},


		createTabGestionePersonale : function(){
			
			var rowEditing = Ext.create('Ext.grid.plugin.RowEditing',{
				listeners: {
					beforeedit: function(editor, e, eOpts ){
//						if (editor.record.get('isEreditata') == true) {
//							Ext.Msg.alert('Info: ', 'Non è possibile modificare variazioni ereditate da altre categorie. Per modificarle, occorre visualizzare le variazione dell\'apposita categoria a cui sono associate.');
//							return false;
//					  	}
//						return true;
				    },
				    canceledit: function( grid, eOpts ){
				    	
				    },
				    edit: function( editor, e, eOpts ){
//				    	console.debug('EDITED KISSES');
//				    	console.debug(editor);
//				    	editor.record.save();
				    	editor.store.sync();
				    	
				    },
				    validateedit: function( editor, e, eOpts ){
//				    	console.debug('VALIDAMELOOO');
//				    	console.debug(editor);
//				    	console.debug(e);
//				    	console.debug(eOpts);
//				    	
				    }
				}
			});
			
			this._tabella_gestione_personale = Ext.create('Ext.grid.Panel', {
				title: 'Gestione personale',
				flex: 3,
				align : 'stretch',
				id: 'tabella_gestionePersonale',
				margin: '2 2 2 2',
				autoScroll: true,
				plugins: [rowEditing],
		        store: Ext.getStore('datastore_gestione_personale'),
		        columns: [

		            { text:	'Id',  			flex: 1,	dataIndex: 'id', hidden: true	},
		            { text:	'Username',  	flex: 1,	dataIndex: 'username',		/*field: {xtype: 'textfield'}*/},
		            { text:	'Nome',  		flex: 1,	dataIndex: 'nome',			field: {xtype: 'textfield'}	},
		            { text:	'Cognome', 		flex: 1,	dataIndex: 'cognome',		field: {xtype: 'textfield'}	},
		            { text:	'Cameriere', 	width: 80,	dataIndex: 'isCameriere',	field: {xtype: 'checkbox'}	},
		            { text:	'Cassiere', 	width: 80,	dataIndex: 'isCassiere',	field: {xtype: 'checkbox'}	},
		            { text:	'Cucina', 		width: 80,	dataIndex: 'isCucina',		field: {xtype: 'checkbox'}	},
		            { text:	'Admin', 		width: 80,	dataIndex: 'isAdmin',		field: {xtype: 'checkbox'}	}
		        ],
//		        features: [{ftype:'grouping'}],
		        listeners:{
		        	itemdblclick: function(view, record, item, index, e, eOpts){
//			        	_viewPort_panel_east.removeAll(false);
//			        	_viewPort_panel_east.setTitle(index);
//			        	_viewPort_panel_east.add({
//			        		xtype: 'label',
//			        		text: '<h1>Tavolo:</h1> '+record.get('Tavolo')
//			        	});
//			        	_viewPort_panel_east.expand(true);
			        },
//					selectionchange: function(view, records) {
//						grid.down('#delete_gestione_personale').setDisabled(!records.length);
//					}
			        statesave: function( thisStateful, state, eOpts ){
			        	console.debug('STATESAVE');
			        },
			        
			        itemcontextmenu: function(view, rec, node, index, e) {
	                	var contextMenu = null;
	                	console.debug('CONTEXT MENU');
	                	contextMenu = Ext.create('Ext.menu.Menu', {
	                		items: [{
			                			text: 'Modifica utente',
			                        	handler: function(){
			                        		_mainTabPanel.updateComponentePersonale(rec);
			                        	}
		                            }
		                        ]
		                });
	                	
	                    contextMenu.showAt(e.getXY());
//	                    return false;
	                }
		        },
		        
			    dockedItems: [{
		            xtype: 'toolbar',
		            dock: 'bottom',
		            items: ['->',{
		                text: 'Aggiorna',
		                iconCls: 'icon-refresh',
		                handler: function(){
		                	Ext.getStore('datastore_gestione_personale').load();
		                }
		            },{
		                text: 'Add',
		                iconCls: 'icon-add',
		                handler: function(){
		                	_mainTabPanel.addNewComponentePersonale();

//		                	var emptyRecord = Ext.create('personale',{
//		                    	action: 'create'
//		                    });
//		            		Ext.getStore('datastore_gestione_personale').insert(0,emptyRecord);
//		                    rowEditing.startEdit(0, 0);
//		                	_maintab addNewComponentePersonale();
		                }
		            }, '-', {
//		                itemId: 'delete',
		                text: 'Delete',
		                iconCls: 'icon-delete',
//		                disabled: true,
		                handler: function(){
		                    var selection = Ext.getCmp('tabella_gestionePersonale').getView().getSelectionModel().getSelection()[0];
		                    if (selection) {
		                    	Ext.MessageBox.confirm('Conferma', 'Sei sicuro di voler rimuovere l\'utente '+selection.get('cognome')+' '+selection.get('nome')+'?', function(btn){
		            				if(btn == 'no') return;
		            				
		            				if(selection.get('id') == ''){
			                    		Ext.getStore('datastore_gestione_personale').remove(selection);
			                    		return;
			                    	}
		            				
		            				selection.destroy({
		            					params: {
		            						action: 'delete'
		            					},
		            	                success : function(record, action) {
//		            	                	Ext.Msg.alert('Success', action.resultSet.message);
//		            	                	new Ext.ux.Notification({
//		            	        				iconCls:	'x-icon-information',
//		            	        				title:	  'Successo',
//		            	        				html:		action.result.message,
//		            	        				autoDestroy: true,
//		            	        				hideDelay:  2000,
//		            	        			}).show(document);
		            	                	Ext.getStore('datastore_gestione_personale').remove(selection);
		            	                },
		            	                failure: function(form, action) {
		            	                    Ext.Msg.alert('Failed', action.result.message);
		            	                }
		            	            });
		            			});
		                    	
//		                    	Ext.getStore('datastore_variazione_voce_menu').remove(selection);
//		                    	Ext.getStore('datastore_variazione_voce_menu').sync();
//		                    	Ext.getCmp('tabella_gestioneVariazioni').forceComponentLayout();

		                    }
		                }
		            }]
		        }]	//Fine dockeditems

		        
			});
		},
		addTabGestionePersonale : function(){
			this.createTabGestionePersonale();
			this._tab_gestione_personale = Ext.create('Ext.panel.Panel', {
				id:	'main_tabPanel_gestionePersonale',
				layout: {
			        type: 'vbox',
			        align: 'stretch'
			    },
				title: 'Gestione personale',
				width: '100%',
			    height: '100%',
			    autoScroll : true,
			    items: [
			            Ext.getCmp('tabella_gestionePersonale')
			    ]
			});
		
			Ext.getCmp('main_tabPanel').add( Ext.getCmp('main_tabPanel_gestionePersonale') );
		},

		createFormGestionePersonale : function(){
			var tmp = Ext.getCmp('form_gestionePersonale');
			if(tmp) tmp.destroy();
			
			return Ext.create('Ext.form.Panel', {
					id: 'form_gestionePersonale',
					border: false,
	    	        defaultType: 'textfield',
	    	        url: 'gestionePersonale',
	    	        items: [{
				        fieldLabel: 'ID',
				        name: 'id',
				        hidden: true
				    },{
				        fieldLabel: 'Username',
				        name: 'username',
				        allowBlank: false
				    },{
				        fieldLabel: 'Nome',
				        name: 'nome',
					    allowBlank: false
				    },{
				        fieldLabel: 'Cognome',
				        name: 'cognome',
				        allowBlank: false
				    },{
				    	inputType: 'password',
				        fieldLabel: 'Password',
				        name: 'passwd'
				    },{
				    	inputType: 'password',
				        fieldLabel: 'Password2',
				        name: 'passwd2'
				    },{
				    	xtype: 'checkboxfield',
				        fieldLabel: 'Cameriere',
				        name: 'isCameriere'
				    },{
				    	xtype: 'checkboxfield',
				        fieldLabel: 'Cassiere',
				        name: 'isCassiere'
				    },{
				    	xtype: 'checkboxfield',
				        fieldLabel: 'Cucina',
				        name: 'isCucina'
				    },{
				    	xtype: 'checkboxfield',
				        fieldLabel: 'Admin',
				        name: 'isAdmin'
				    }],
				    
				    buttons: [{
	    	            text: 'Reset',
	    	            handler: function() {
	    	            	if(this.up('form').action == 'create'){
	    	            		this.up('form').getForm().reset();
	    	            	}else{
//		    	            	this.up('form').getForm().findField('nome').setValue('');
//		    	            	this.up('form').getForm().findField('descrizione').setValue('');
//		    	            	this.up('form').getForm().findField('prezzo').setValue('');
	    	            		this.up('form').getForm().loadRecord( this.up('form').selectedNode );
	    	            	}
	    	            	
	    	            }
	    	        },{
				        text: 'Submit',
				        handler: function() {
				            // The getForm() method returns the Ext.form.Basic instance:
				            var form = this.up('form').getForm();
				            if (form.isValid()) {
				                // Submit the Ajax request and handle the response
				                if(this.up('form').action == 'create'){
				                	form.submit({
					                	params : {
					                    	action : this.up('form').action
					                    },
					                    success: function(form, action) {
					                    	console.debug('GUARDAAAAA');
					                    	console.debug(action);
					                    	if( action.params.action == 'create' ){
					                    		console.debug('OK SUCCESSO CREATE');
					                    		
					                    		var nuovo_nodo = Ext.create('personale', {
						                    		id: 			action.result.data[0].id,
						                    		username: 		action.result.data[0].username,
						                    		nome: 			action.result.data[0].nome,
						                    		cognome: 		action.result.data[0].cognome,
						                    		isCameriere:	action.result.data[0].isCameriere,
						                    		isCassiere:		action.result.data[0].isCassiere,
						                    		isCucina:		action.result.data[0].isCucina,
						                    		isAdmin:		action.result.data[0].isAdmin
						                    	});
					                    		Ext.getStore('datastore_gestione_personale').add(nuovo_nodo);
					                    		Ext.getCmp('window_inserimentoPersonale').destroy();
					                    	}else if ( action.params.action == 'update' ){
//					                    		console.debug('OK SUCCESSO UPDATE');
					                    		var updatedNode = Ext.getStore('datastore_gestione_personale').getById(action.result.data[0].id);
					                    		updatedNode.set('username', 	action.result.data[0].username);
					                    		updatedNode.set('nome',			action.result.data[0].nome);
					                    		updatedNode.set('cognome', 		action.result.data[0].cognome);
					                    		updatedNode.set('isCameriere',	action.result.data[0].isCameriere);
					                    		updatedNode.set('isCassiere',	action.result.data[0].isCassiere);
					                    		updatedNode.set('isCucina',		action.result.data[0].isCucina);
					                    		updatedNode.set('isAdmin',		action.result.data[0].isAdmin);
					                    		
					                    		
					                    		
					                    		
//					                    		//updatedNode.set('id',action.result.data[0].id);
//						                    		//updatedNode.set('parentId',action.result.data[0].parentId);
//						                    		updatedNode.set('nome',action.result.data[0].nome);
//						                    		//updatedNode.set('tipo',action.result.data[0].tipo);
//						                    		updatedNode.set('descrizione',action.result.data[0].descrizione);
//						                    		updatedNode.set('prezzo',action.result.data[0].prezzo);
//						                    		if(action.result.data[0].tipo == 1){
//						                    			updatedNode.set('text',action.result.data[0].nome);
//						                    		}else{
//						                    			updatedNode.set('text',action.result.data[0].nome+' - ['+action.result.data[0].prezzo+'€]');
//						                    		}
						                    	Ext.getCmp('viewport_east').collapse();
					                    	}
					                    	
					                    	
					                    	
//					                    	Ext.Msg.alert('Info: ', action.result.message);
//					                    	Ext.getCmp(window_inserimentoPersonale).destroy();
//					                    	Ext.getCmp('form_gestioneMenu').destroy();
					                    	
					                    },
					                    failure: function(form, action) {
					                        Ext.Msg.alert('Errore: ', action.result.message);
					                    }
					                });
				                }else if(this.up('form').action == 'update'){
				                	var updatedForm = Ext.getCmp('form_gestionePersonale');
				                	var updatedNode = updatedForm.selectedNode;
		                    		if(updatedNode){
				                	updatedNode.set('username', 	updatedForm.getForm().findField('username').getValue());
		                    		updatedNode.set('nome',			updatedForm.getForm().findField('nome').getValue());
		                    		updatedNode.set('cognome', 		updatedForm.getForm().findField('cognome').getValue());
		                    		updatedNode.set('isCameriere',	updatedForm.getForm().findField('isCameriere').getValue());
		                    		updatedNode.set('isCassiere',	updatedForm.getForm().findField('isCassiere').getValue());
		                    		updatedNode.set('isCucina',		updatedForm.getForm().findField('isCucina').getValue());
		                    		updatedNode.set('isAdmin',		updatedForm.getForm().findField('isAdmin').getValue());
		                    		Ext.getCmp('viewport_east').collapse();
		                    		Ext.getStore('datastore_gestione_personale').sync();
				                	}
				                }
				            	
				            	
				            }
				        }
				    }]
				});
		},
		addNewComponentePersonale : function(){
			var form = this.createFormGestionePersonale();
			form.action = 'create';
			var askWindow = Ext.create('Ext.window.Window', {
				id: 'window_inserimentoPersonale',
        	    title: 'Inserimento componente del personale',
        	    expandOnShow : true,
        	    modal: true,
        	    //height: 400,
        	    //width: 400,
//        	    layout: 'fit',
//        	    layout: 'auto',
        	    layout: {
			        type: 'auto',
			        pack: 'center'
			    },
        	    items:[form],
        	    listeners:{
        	    	afterrender: function( thisWindow, eOpts ){
        	    		console.debug('Altezzaaaa');
        	    		thisWindow.setHeight( Ext.getCmp('form_gestionePersonale').getHeight()+37 );
        	    	}
        	    }
        	});
			askWindow.show();
		},
		updateComponentePersonale : function(selectedNode){
			var form = this.createFormGestionePersonale();
			form.action = 'update';
			form.selectedNode = selectedNode;
//			form.getForm().findField('id').hide();
//			form.getForm().findField('parentId').hide();
//			form.getForm().findField('depth').hide();
//			form.getForm().findField('tipo').hide();
//			if(selectedNode.get('tipo') == 2){
//				form.setTitle('Modifica voce di menù');
//			}else{
//				form.setTitle('Modifica categoria');
//				form.getForm().findField('prezzo').hide();
//			}
			
			form.setTitle('Modifica utente');
			
			form.getForm().loadRecord(selectedNode);
			
			Ext.getCmp('viewport_east').removeAll(false);
			Ext.getCmp('viewport_east').expand();
			Ext.getCmp('viewport_east').add(form);
		},

		createTabStoricoConti : function(){
			///////INIZIO
			var pluginExpanded = true;
		    
			// pluggable renders
//		    function renderTopic(value, p, record) {
//		        return Ext.String.format(
//		            '<b><a href="http://sencha.com/forum/showthread.php?t={2}" target="_blank">{0}</a></b><a href="http://sencha.com/forum/forumdisplay.php?f={3}" target="_blank">{1} Forum</a>',
//		            value,
//		            record.data.forumtitle,
//		            record.getId(),
//		            record.data.forumid
//		        );
//		    }
//
//		    function renderLast(value, p, r) {
//		        return Ext.String.format('{0}<br/>by {1}', Ext.Date.dateFormat(value, 'M j, Y, g:i a'), r.get('lastposter'));
//		    }

		    
		    
		    var grid = Ext.create('Ext.grid.Panel', {
		    	id:	'tabella_storico_conti',
//		        width: 700,
//		        height: 500,
//		    	forceFit: true,
		        title: 'Storico conti',
		        store: Ext.getStore('datastore_storico_conti'),
//		        disableSelection: true,
//		        loadMask: true,
		        viewConfig: {
		            id: 'gv',
//		            emptyText:'Non ci sono conti da visualizzare.',
//		            forceFit: true,
//		            trackOver: false,
//		            stripeRows: false,
//		            plugins: [{
//		                ptype: 'preview',
//		                bodyField: 'excerpt',
//		                expanded: true,
//		                pluginId: 'preview'
//		            }]
		        },
		        
		        // grid columns
		        columns: [
				            { header: 'IdConto',  	dataIndex: 'idConto', 				flex:1 , 	hidden: true},
				            { header: 'Stato',  	dataIndex: 'stato', 				flex:1 },
				            { header: 'Totale €',  	dataIndex: 'prezzo', 				flex:1 },			          
				            { header: 'Creazione', 	dataIndex: 'timestampApertura', 	flex:2 },
				            { header: 'Chiusura', 	dataIndex: 'timestampChiusura',	 	flex:2 }
				        ],
		        // paging bar on the bottom
		        bbar: Ext.create('Ext.PagingToolbar', {
		            store: Ext.getStore('datastore_storico_conti'),
		            displayInfo: true,
		            displayMsg: 'Displaying topics {0} - {1} of {2}',
		            emptyMsg: "Non ci sono conti da visualizzare",
		            items:[
		                '-', 'Trololo'/*{
		                text: 'Show Preview',
		                pressed: pluginExpanded,
		                enableToggle: true,
		                toggleHandler: function(btn, pressed) {
		                    var preview = Ext.getCmp('gv').getPlugin('preview');
		                    preview.toggleExpanded(pressed);
		                }
		            }*/]
		        })
		    });

		    // trigger the data store load
		    //Ext.getStore('datastore_storico_conti').loadPage(1);
		    //Ext.getCmp('main_tabPanel').add( Ext.getCmp('tabella_storico_conti') );
			return grid;
		    ///////FINE
		},
		addTabStoricoConti : function(){
			this._tab_storico_conti = this.createTabStoricoConti();
			Ext.getCmp('main_tabPanel').add( Ext.getCmp('tabella_storico_conti') );
		},
		
		visualizzaListaConti : function(idTavolo){
			_viewPort_panel_east.removeAll(false);
			_viewPort_panel_east.expand(true);
			
			if(!_mainTabPanel._tabella_lista_conti_tavolo){
				_mainTabPanel._tabella_lista_conti_tavolo = Ext.create('Ext.grid.Panel', {
					id: 'tabella_lista_conti',
					title: 'Conti del tavolo',
					forceFit: true,
					autoScroll: true,
			        store: Ext.getStore('datastore_elenco_conti_tavolo'),
			        viewConfig: {
			            emptyText: 'Non sono presenti conti'        
			        },
			        columns: [
			            { header: 'IdConto',  	dataIndex: 'idConto', 				flex:1 , 	hidden: true},
			            { header: 'Stato',  	dataIndex: 'stato', 				flex:1 },
			            { header: 'Totale €',  	dataIndex: 'prezzo', 				flex:1 },			          
			            { header: 'Creazione', 	dataIndex: 'timestampApertura', 	flex:2 },
			            { header: 'Chiusura', 	dataIndex: 'timestampChiusura',	 	flex:2 }
			        ],
			        listeners:{
			        	itemdblclick: function(view, record, item, index, e, eOpts){
			        		console.debug('idConto: '+record.get('idConto'));
			        		_mainTabPanel.visualizzaConto(	record.get('idConto'), idTavolo	);
	//			        	_viewPort_panel_east.removeAll(false);
	//			        	// Inizio definizione stato tavolo
	//				        	console.debug(view);
	//				        	console.debug('Record: ');
	//				        	console.debug(record);
	//				        	console.debug(item);
	//				        	_viewPort_panel_east.setTitle('P:'+record.get('numeroPiano')+'\tT:'+record.get('idTavolo')+'\t'+record.get('nomeTavolo')+'['+record.get('numPosti')+']');
	//				        	_viewPort_panel_east.add({
	//				        		xtype: 'label',
	//				        		text: '<h1>Tavolo:</h1> '+record.get('idTavolo')
	//				        	});
	//			        	// Fine definizione stato tavolo
	//			        	_viewPort_panel_east.expand(true);
				        },
				        itemcontextmenu: function(view, rec, node, index, e) {}
				    },
				    dockedItems: [{
			            xtype: 'toolbar',
			            dock: 'bottom',
			            items: ['->',{
			                text: 'Aggiorna',
			                iconCls: 'icon-refresh',
			                handler: function(){
			                	Ext.getStore('datastore_elenco_conti_tavolo').load();
			                }
			            }]
			        }]	//Fine dockeditems
	
			        
				});
			}

			Ext.getStore('datastore_elenco_conti_tavolo').idTavolo=idTavolo;
			Ext.getStore('datastore_elenco_conti_tavolo').load();
			
			_viewPort_panel_east.add(_mainTabPanel._tabella_lista_conti_tavolo);
			
			
			//_viewPort_panel_east.setTitle('P:'+record.get('numeroPiano')+'\tT:'+record.get('idTavolo')+'\t'+record.get('nomeTavolo')+'['+record.get('numPosti')+']');
			
			
//			if( record.get('statoTavolo') == 'OCCUPATO' ){
//    			_viewPort_panel_east.removeAll(false);
//    			_viewPort_panel_east.expand(true);
//    			var idTavolo = record.get('idTavolo');
//    			//var tabella_conto = createTabellaConto( idTavolo );
//    			_viewPort_panel_east.add(_mainTabPanel._tabella_conto);
//    			_viewPort_panel_east.setTitle('P:'+record.get('numeroPiano')+'\tT:'+record.get('idTavolo')+'\t'+record.get('nomeTavolo')+'['+record.get('numPosti')+']');
//    						        			
//    			
//    			Ext.getStore('datastore_conto').idTavolo=idTavolo;
//    			Ext.getStore('datastore_conto').load();
//    		}else{
//    			_viewPort_panel_east.collapse();
//	        	_viewPort_panel_east.removeAll(false);
//	        	_viewPort_panel_east.setTitle('');
//    		}
			return;
		},

		visualizzaConto : function(idConto,idTavolo){
			
			var askWindow = Ext.get('window_visualizzazione_conto');
			if(askWindow == undefined){
				console.debug('CREO LA WINDOW LALALAL YAAAAAAAAAAAA');
				askWindow = Ext.create('Ext.window.Window', {
								id: 'window_visualizzazione_conto',
				        	    title: 'Visualizzazione Conto',
				        	    height: 350,
				        	    width: 650,
				        	    layout: 'fit',
				        	    modal: true
	        				});
			}
				
			_mainTabPanel._tabella_conto = Ext.get('tabella_conto');
			if(_mainTabPanel._tabella_conto == undefined){
				console.debug("INIZIALIZZAZIONE TABELLA CONTO");
				_mainTabPanel._tabella_conto = Ext.create('Ext.grid.Panel', {
					id: 'tabella_conto',
					title: 'Conto tavolo',
					forceFit: true,
					//flex: 1,
					//align : 'stretch',
					//margin: '2 2 2 2',
					autoScroll: true,
			        store: Ext.getStore('datastore_conto'),
			        viewConfig: {
			            emptyText: 'Non sono presenti comande'        
			        },
			        columns: [
			            { header: 'IdComanda',  	dataIndex: 'idComanda', 		flex:1 , 	hidden: true},
			            { header: 'Qnt',  			dataIndex: 'quantita', 			flex:2 },
			            { header: 'Nome',  			dataIndex: 'nomeVoceMenu', 		flex:8 },
			            { header: 'Stato',  		dataIndex: 'stato', 			flex:2 },
			            { header: 'Prezzo', 		dataIndex: 'prezzoVoceMenu', 	flex:2 },
			            { header: 'Variazioni', 	dataIndex: 'variazioni',	 	flex:8},
			            { header: 'Note', 			dataIndex: 'note', 				flex:2 }
			        ],
	//		        features: [{ftype:'grouping'}],
			        listeners:{
			        	itemdblclick: function(view, record, item, index, e, eOpts){
	//			        	_viewPort_panel_east.removeAll(false);
	//			        	// Inizio definizione stato tavolo
	//				        	console.debug(view);
	//				        	console.debug('Record: ');
	//				        	console.debug(record);
	//				        	console.debug(item);
	//				        	_viewPort_panel_east.setTitle('P:'+record.get('numeroPiano')+'\tT:'+record.get('idTavolo')+'\t'+record.get('nomeTavolo')+'['+record.get('numPosti')+']');
	//				        	_viewPort_panel_east.add({
	//				        		xtype: 'label',
	//				        		text: '<h1>Tavolo:</h1> '+record.get('idTavolo')
	//				        	});
	//			        	// Fine definizione stato tavolo
	//			        	_viewPort_panel_east.expand(true);
				        },
				        itemcontextmenu: function(view, rec, node, index, e) {
	//			        	if(rec.get('statoTavolo') == 'OCCUPATO'){
	//			        		
	//			        	}
	//			        	
	//		            	var contextMenu = null;
	//		            	console.debug('CONTEXT MENU');
	//		            	contextMenu = Ext.create('Ext.menu.Menu', {
	//		            		items: [{
	//			                			text: 'Modifica utente',
	//			                        	handler: function(){
	//			                        		_mainTabPanel.updateComponentePersonale(rec);
	//			                        	}
	//		                            }
	//		                        ]
	//		                });
	//		            	
	//		                contextMenu.showAt(e.getXY());
	////		                return false;
			            }
				    },
				    dockedItems: [{
			            xtype: 'toolbar',
			            dock: 'bottom',
			            items: [{
			            	id: 'totale_tabella_conto',
			            	text: '0.00'
			            },'->',{
			                text: 'Chiudi conto',
			                id: 'tabella_visualizza_conto_action_button',
			                iconCls: 'icon-close',
			                handler: function(){
			                	
			                	Ext.MessageBox.confirm('Conferma', 'Sei sicuro di voler chiudere il conto?', function(btn){
			        				if(btn == 'no') return;
			        				
			        				Ext.Ajax.request({
			        				    url: 'gestioneConti',
			        				    params: {
			        				    	idConto: idConto,
			        				        action: 'CHIUDI_CONTO'
			        				    },
			        				    success: function(response){
			        				    	Ext.getStore('datastore_conto').load();
			        				    	Ext.getStore('datastore_elenco_conti_tavolo').load();	//Aggiornamento per la visualizzazione delle modifiche
			        				    	Ext.getStore('datastore_stato_tavolo').load();			//Aggiornamento per la visualizzazione delle modifiche
			        				    	
						                	_mainTabPanel.updateVisualizzaConto(idConto,idTavolo);
						                	Ext.get('tabella_visualizza_conto_action_button').destroy();
			        				    }
			        				});
			        				
			        			});
			                	
			                }
			            },{
			                text: 'Aggiorna',
			                iconCls: 'icon-refresh',
			                handler: function(){
			                	_mainTabPanel.updateVisualizzaConto(idConto,idTavolo);
			                }
			            }]
			        }]	//Fine dockeditems
	
			        
				});
			}
			
			_mainTabPanel.updateVisualizzaConto(idConto,idTavolo);
			askWindow.add(_mainTabPanel._tabella_conto);
			askWindow.show();
		},
		
		updateVisualizzaConto : function(idConto,idTavolo){
			Ext.Ajax.request({
			    url: 'gestioneConti',
			    params: {
			    	idConto: idConto,
			        action: 'INFO_CONTO'
			    },
			    success: function(response){
			    	var text = response.responseText;
			    	var objConto = Ext.JSON.decode(text,true);
			    	console.debug(objConto.conto[0]);
			    	Ext.get('totale_tabella_conto').dom.textContent = 'Stato: ['+objConto.conto[0].stato+']    Totale conto: '+objConto.conto[0].prezzo+'€';
			    	Ext.getStore('datastore_conto').idTavolo=idTavolo;
			    	Ext.getStore('datastore_conto').idConto=idConto;
					Ext.getStore('datastore_conto').load();
					
					if(objConto.conto[0].stato == "APERTO" || objConto.conto[0].stato == "DAPAGARE"){
						//Ext.get('tabella_visualizza_conto_action_button').dom.textContent = 'Chiudi';
					}else{
						//Ext.get('tabella_visualizza_conto_action_button').destroy();
					}
			    }
			});
			
		}
		
};

//function createTabellaConto(idTavolo){
//	console.debug('IDTAVOLO: - '+idTavolo);
//	
//	//var tabella_conto = Ext.get('tabella_conto');
//	
//	if(_mainTabPanel._tabella_conto == undefined){
//		console.debug('CREO TABELLA');
//		_mainTabPanel.
//	}//fine if
//	
//	return tabella_conto;
//};
	

function titoloRaggruppamentoVariazioni(values) {
    //console.log(values);
    if(values.name){
    	return values.name = 'Variazioni ereditate';
    }else{
    	return values.name = 'Variazioni';
    }
};