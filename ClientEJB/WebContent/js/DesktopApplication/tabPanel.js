/* Creo l'oggetto rappresentante il Main Tab Panel */
var _mainTabPanel = {
		
		_panel: Ext.create('Ext.tab.Panel', {
			id: 'main_tabPanel',
			region: 'center',
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
		_tab_menu: null,
		_tab_camerieri: null,
		_tab_opzioni: null,
		_tab_gestione_tavolo: null,
			_albero_gestioneTavolo: null,
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

		        
			});
			
			this._tabella_stato_cameriere = Ext.create('Ext.grid.Panel', {
				id: 'tabella_statoCameriere',
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

		        
			});
			
			this._tabella_stato_cuoco = Ext.create('Ext.grid.Panel', {
				id: 'tabella_statoCuoco',
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
				id:	'main_tabPanel_stato',
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
			this._albero_gestioneTavolo = Ext.create('Ext.tree.Panel', {
				id: 'albero_gestioneTavolo',
			    title: 'Gestione ristorante: piani, aree, tavoli',
			    width: 500,
			    height: 500,
			    rootVisible: true,
			    useArrows: true,
			    store: Ext.getStore('datastore_gestione_tavolo'),
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
		            		var lastSelected = Ext.getCmp('albero_gestioneTavolo').getSelectionModel().getLastSelected();
		            		if(lastSelected.get('tipo')==3)
		            			_mainTabPanel.modifyNodeGestioneTavolo(lastSelected);
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
				                        			var lastSelected = Ext.getCmp('albero_gestioneTavolo').getSelectionModel().getLastSelected();
				                        			_mainTabPanel.addNewNodeGestioneTavolo(lastSelected);
				                        		}
			                            	}
			                        ]
			                    });
		                	}else if(depth == 1){
		                		contextMenu = Ext.create('Ext.menu.Menu', {
			                        items: [
			                            {text: 'Aggiungi area',
			                        		handler: function(){
			                        			var lastSelected = Ext.getCmp('albero_gestioneTavolo').getSelectionModel().getLastSelected();
			                        			_mainTabPanel.addNewNodeGestioneTavolo(lastSelected);
			                        		}
			                            },{
			                            	text: 'Modifica piano',
			                            	handler: function(){
			                        			var lastSelected = Ext.getCmp('albero_gestioneTavolo').getSelectionModel().getLastSelected();
			                        			_mainTabPanel.modifyNodeGestioneTavolo(lastSelected);
			                            	}
			                            },
			                            {
			                            	text: 'Rimuovi piano',
			                            	handler: function(){
			                            		var lastSelected = Ext.getCmp('albero_gestioneTavolo').getSelectionModel().getLastSelected();
			                            		lastSelected.destroy({
			        			                    params : {
			        			                    	action : 'delete'
			        			                    },
			        			                    success : function(record, action) {
			        			                    	Ext.Msg.alert('Success', action.result.message);
			        			                    },
			        			                    failure: function(form, action) {
			        			                        Ext.Msg.alert('Failed', action.result.message);
			        			                    }
			        			                });
			                            	}
			                            }
			                        ]
			                    });
		                	}else if(depth == 2){
		                		contextMenu = Ext.create('Ext.menu.Menu', {
			                        items: [
			                            {text: 'Aggiungi tavolo',
			                        		handler: function(){
			                        			var lastSelected = Ext.getCmp('albero_gestioneTavolo').getSelectionModel().getLastSelected();
			                        			_mainTabPanel.addNewNodeGestioneTavolo(lastSelected);
			                        		}
			                            },{
			                            	text: 'Modifica area',
			                            	handler: function(){
			                        			var lastSelected = Ext.getCmp('albero_gestioneTavolo').getSelectionModel().getLastSelected();
			                        			_mainTabPanel.modifyNodeGestioneTavolo(lastSelected);
			                            	}
			                            },
			                            {text: 'Rimuovi area'}
			                        ]
			                    });
		                	}else if(depth == 3){
		                		contextMenu = Ext.create('Ext.menu.Menu', {
			                        items: [
			                            {
			                            	text: 'Modifica tavolo',
			                            	handler: function(){
			                        			var lastSelected = Ext.getCmp('albero_gestioneTavolo').getSelectionModel().getLastSelected();
			                        			_mainTabPanel.modifyNodeGestioneTavolo(lastSelected);
			                            	}
			                            },
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
				id:	'main_tabPanel_gestioneTavolo',
				layout: 'vbox',
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
			var askWindow = Ext.create('Ext.window.Window', {
				id: 'window_inserimentoNodoGestioneTavolo',
        	    title: 'Hello',
        	    height: 400,
        	    width: 400,
        	    layout: 'fit'
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
				                    	tipo: 			action.result.data[0].tipo
			                    	});
			                    	
			                    	
			                    	
			                    	
			                    	
			                    	switch(action.result.data[0].tipo){
			                    		case 1: {
						                    		nuovo_nodo.set('parentId','root');
						                    		parentId = 'root';
				                    				break;
			                    		}
			                    		case 2: {
						                    		parentId = 'P'+action.result.data[0].parentId;
						                    		break;
					                    }
			                    		case 3: {
						                    		parentId = 'A'+action.result.data[0].parentId;
						                    		break;
					                    }
			                    		default: {parentId = 'root';	break;}
			                    	}
			                    	console.debug("PARENT ID NUOVO NODO: "+parentId);
			                    	console.debug('Nuovo nodo creato: '+nuovo_nodo.get('id')+' - '+nuovo_nodo.get('parentId')+' - '+nuovo_nodo.get('text')+' - '+nuovo_nodo.get('tipo'));
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
			form.getForm().findField('enabled').setValue('on');
			
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
		
		modifyNodeGestioneTavolo : function(selectedNode){
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
			                /*form.submit({
			                    success: function(form, action) {
			                       Ext.Msg.alert('Success', action.result.msg);
			                    },
			                    failure: function(form, action) {
			                        Ext.Msg.alert('Failed', action.result.msg);
			                    }
			                });*/
			                
			                //selectedNode.save({extraParams : { azione: 'update'}}); 
			                selectedNode.save({
			                    params : {
			                    	action : 'update'
			                    },
			                    success : function(record) {
			                    	Ext.Msg.alert('Success', action.result.message);
			                        form.updateRecord(record);// selectedNode.commit();
			                        console.debug('LOVE ME LOVE YOU'+record.result.message+' - '+action.result.message);
			                    },
			                    failure: function(record) {
			                        Ext.Msg.alert('Failed', action.result.message);
			                    }
			                });
			            }
			        }
			    }]
			});
			
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
			Ext.getCmp('viewport_east').removeAll();
			Ext.getCmp('viewport_east').expand();
			Ext.getCmp('viewport_east').add(Ext.getCmp('form_gestioneTavolo_updateNode'));
			
		},
		
		createTabGestioneMenu : function(){
			var tree = Ext.create('Ext.tree.Panel', {
				id: 'albero_gestioneMenu',
		        title: 'Gestione menù',
		        width: 500,
		        height: 500,
		        //collapsible: true,
		        useArrows: true,
		        rootVisible: true,
		        store: Ext.getStore('datastore_gestione_menu'),
		        //multiSelect: true,
		        singleExpand: true,
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
				layout: 'vbox',
				title: 'Gestione menù',
				width: '100%',
			    height: '100%',
			    autoScroll : true,
			    items: [
			            Ext.getCmp('albero_gestioneMenu')
			    ]
			});
		
			Ext.getCmp('main_tabPanel').add( Ext.getCmp('main_tabPanel_gestioneMenu') );
		}
		
};