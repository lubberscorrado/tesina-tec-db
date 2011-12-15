function initStores(){

	/*Creo lo store*/
	Ext.create('Ext.data.Store', {
		storeId: 'datastore_stato_tavolo',
		groupField: 'nomeArea',
		model: 'StatoTavolo',
		autoLoad: true,
		//autoSync: true,
		pageSize: 50
		
	});
	
	if(_global._isAdministrator == true){
		
		Ext.create('Ext.data.TreeStore', {
			storeId: 'datastore_gestione_tavolo',
			autoLoad: true,
			autoSync: true,
			clearOnLoad: true,
			model: 'nodoGestioneTavolo',
			root: {
				expanded: true,
		        text: "Ristorante"
		    },
		    listeners: {
				beforeappend: function( thisNode, newChildNode, eOpts ){
					
					var tipo = newChildNode.get('tipo');
					if( newChildNode.get('enabled') == 'on' || newChildNode.get('enabled') == true ){
						newChildNode.set('enabled','on');
					} else {
						newChildNode.set('enabled','off');
					}
					if( tipo == 1){			//Piano
		            	newChildNode.set('leaf', false);
		            	newChildNode.set('text', 	newChildNode.get('nome'));
		            	//newChildNode.set('id', 'P'+newChildNode.get('realId'));
		            	//newChildNode.set('parentId', newChildNode.get('parentId'));
		            	//newChildNode.set('icon', newChildNode.get('profile_image_url'));
		            }else if( tipo == 2){	//Area
		            	newChildNode.set('leaf', false);
		            	newChildNode.set('text', 	newChildNode.get('nome'));
		            	//newChildNode.set('id', 'A'+newChildNode.get('realId'));
		            	//newChildNode.set('parentId', 'P'+newChildNode.get('parentId'));
		            	//newChildNode.set('icon', newChildNode.get('profile_image_url'));
		            }else if( tipo == 3){	//Tavolo
		            	newChildNode.set('leaf', true);
		            	newChildNode.set('text', 	newChildNode.get('nome'));
		            	//newChildNode.set('id', 'T'+newChildNode.get('realId'));
		            	//newChildNode.set('parentId', 'A'+newChildNode.get('parentId'));
		            	//newChildNode.set('icon', newChildNode.get('profile_image_url'));
		            }
				},
				append: function( thisNode, newChildNode, index, eOpts ) {
				},
				update: function( thisStore, record, operation, eOpts ){
				}
		    }
		});
		
		Ext.create('Ext.data.TreeStore', {
			storeId: 'datastore_gestione_menu',
			autoLoad: true,
			autoSync: true,
			model: 'nodoGestioneMenu',
			root: {
				expanded: true,
		        text: "Men�"
		    },
		    listeners: {
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
		    }
		});
		
		Ext.create('Ext.data.Store', {
			storeId: 'datastore_gestione_personale',
			//groupField: 'nomeArea',
			model: 'personale',
			autoLoad: true,
			//autoSync: true,
			pageSize: 50,
			remoteFilter : true,
		
			listeners: {
				beforeload: function( store, operation, eOpts ){
//					store.proxy.extraParams.idCategoria=idCategoria;
				},
				beforesync: function( options, eOpts ){
//								console.debug('beforesync');
//								console.debug(options);
//					//			console.debug(eOpts);
//								if(options.update){
//									console.debug('Create OR Update');
//								}
				},
				write: function(store, operation, eOpts ){
//					console.debug('writerello');
//					console.debug(eOpts);
//					console.debug(operation);
//					console.debug(store);
//					
//					operation.request.params.action = 'Cicisbeo';
//					
				},
				update: function( store, record, operation, eOpts ){
//					console.debug('updaterello');
//					console.debug(eOpts);
//					console.debug(operation);
//					console.debug(store);
//		//			Ext.data.Model.EDIT
//		//			Ext.data.Model.REJECT
//		//			Ext.data.Model.COMMIT
					
				},
				remove: function( store, record, index, eOpts ){
//					console.debug('removerello');
//					console.debug(eOpts);
//					console.debug(index);
//					console.debug(record);
//					console.debug(store);
				},
				
				beforeappend: function( thisNode, newChildNode, eOpts ){
//					var tipo = newChildNode.get("tipo");
//					if( tipo == 1){			//CATEGORIA
//						newChildNode.set('leaf', false);
//						newChildNode.set('text', newChildNode.get('nome'));
//						newChildNode.set('qtip', newChildNode.get('descrizione'));
//		            	//newChildNode.set('id', 'P'+newChildNode.get('realId'));
//		            	//newChildNode.set('parentId', newChildNode.get('parentId'));
//		            	//newChildNode.set('icon', newChildNode.get('profile_image_url'));
//		            }else if( tipo == 2){	//VOCE MENU
//		            	newChildNode.set('leaf', true);
//		            	newChildNode.set('text', newChildNode.get('nome')+' - ['+newChildNode.get('prezzo')+'€]');
//		            	newChildNode.set('qtip', newChildNode.get('descrizione'));
//		            	//newChildNode.set('id', 'A'+newChildNode.get('realId'));
//		            	//newChildNode.set('parentId', 'P'+newChildNode.get('parentId'));
//		            	//newChildNode.set('icon', newChildNode.get('profile_image_url'));
//		            }
				},
				append: function( thisNode, newChildNode, index, eOpts ) {
					
			    },
			    update: function( thisStore, record, operation, eOpts ){
//			    	if(operation == Ext.data.Model.EDIT){
//			    		console.debug('xedit');
//			    	}else if(operation == Ext.data.Model.REJECT){
//			    		console.debug('xreject');
//			    	}else if(operation == Ext.data.Model.COMMIT){
//			    		console.debug('xcommit');
//			    	}
			    },
			    datachanged: function( thisStore, eOpts ){
//			    	console.debug('DATACHANGED');
//			    	console.debug(thisStore);
//			    	console.debug(eOpts);
			    }
		    }
		});
	}
}