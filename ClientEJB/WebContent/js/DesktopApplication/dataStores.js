function initStores(){

	/*Creo lo store*/
	Ext.create('Ext.data.Store', {
		storeId: 'datastore_stato_tavolo',
		groupField: 'nomeArea',
		model: 'StatoTavolo',
		autoLoad: true,
		//autoSync: true,
		//pageSize: 50
	});
	
	Ext.create('Ext.data.Store', {
		storeId: 'datastore_stato_cameriere',
		model: 'StatoCameriere',
		autoLoad: true,
		//autoSync: true,
		//pageSize: 50
	});
	
	Ext.create('Ext.data.Store', {
		storeId: 'datastore_stato_cucina',
		model: 'StatoCuoco',
		autoLoad: true,
		//autoSync: true,
		//pageSize: 50
	});
	
	Ext.create('Ext.data.Store', {
		storeId: 'datastore_conto',
		model: 'Comanda',
		//autoLoad: true,
		//autoSync: true,
		pageSize: 50,
		listeners: {
			beforeload: function( store, operation, eOpts ){
				store.proxy.extraParams.idTavolo=this.idTavolo;
				store.proxy.extraParams.idConto=this.idConto;
			}
		}
	});
	
	Ext.create('Ext.data.Store', {
		storeId: 'datastore_elenco_conti_tavolo',
		model: 'Conto',
		//autoLoad: true,
		//autoSync: true,
		pageSize: 50,
		listeners: {
			beforeload: function( store, operation, eOpts ){
				store.proxy.extraParams.idTavolo=this.idTavolo;
			}
		}
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
//					if( newChildNode.get('enabled') == 'on' || newChildNode.get('enabled') == 'true' || newChildNode.get('enabled') == true ){
//						newChildNode.set('enabled','on');
//						console.debug('set true');
//					} else {
//						newChildNode.set('enabled','off');
//						console.debug('set false');
//					}
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
					//console.debug('BEFORE LOAD');
				},
				beforesync: function( options, eOpts ){
				},
				beforeprefetch: function( thisStore, operation, eOpts ){
					//console.debug('BEFORE PREFETCH');
				},
				write: function(store, operation, eOpts ){
//					console.debug('writerello');
//					console.debug(eOpts);
//					console.debug(operation);
//					console.debug(store);
//					
//					operation.request.params.action = 'Cicisbeo';
//					
					//console.debug('WRITE');
				},
				update: function( thisStore, record, operation, eOpts ){
//					console.debug('updaterello');
//					console.debug(eOpts);
//					console.debug(operation);
//					console.debug(store);
//		//			Ext.data.Model.EDIT
//		//			Ext.data.Model.REJECT
//		//			Ext.data.Model.COMMIT
					//console.debug('UPDATE');
					var r = thisStore.getProxy().getReader().jsonData;
			    	if(!r.success){
//			    		alert(r.message);
			    	}
				},
				remove: function( store, record, index, eOpts ){
//					console.debug('removerello');
//					console.debug(eOpts);
//					console.debug(index);
//					console.debug(record);
//					console.debug(store);
					//console.debug('REMOVE');
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
					//console.debug('BEFORE APPEND');
				},
				append: function( thisNode, newChildNode, index, eOpts ) {
					//console.debug('APPEND');
			    },
			    datachanged: function( thisStore, eOpts ){
			    	//console.debug('DATACHANGED');
//			    	console.debug(thisStore);
//			    	console.debug(eOpts);
			    },
			    load: function( thisStore, records, successful, operation, eOpts ){
			    	//console.debug('PROVIAMOLOO');
			    	//console.debug(thisStore);
			    	var r = thisStore.getProxy().getReader().jsonData;
			    	if(!r.success){
//			    		alert(r.message);
			    	}
			    }
		    }
		});
		
		
//	    var store = Ext.create('Ext.data.Store', {
//        pageSize: 50,
//        model: 'ForumThread',
//        remoteSort: true,
//        proxy: {
//            // load using script tags for cross domain, if the data in on the same domain as
//            // this page, an HttpProxy would be better
//            type: 'jsonp',
//            url: 'http://www.sencha.com/forum/topics-browse-remote.php',
//            reader: {
//                root: 'topics',
//                totalProperty: 'totalCount'
//            },
//            // sends single sort as multi parameter
//            simpleSortMode: true
//        },
//        sorters: [{
//            property: 'lastpost',
//            direction: 'DESC'
//        }]
//    });
		
		Ext.create('Ext.data.Store', {
			storeId: 'datastore_storico_conti',
			model: 'StoricoConto',
			autoLoad: true,
			//autoSync: true,
			pageSize: 30,
			remoteSort: true,
			
//			proxy: {
//	            // load using script tags for cross domain, if the data in on the same domain as
//	            // this page, an HttpProxy would be better
//	            type: 'jsonp',
//	            url: 'gestioneConti?action=GET_STORICO_CONTI',
//	            
//	            writer: {
//	                type: 'singlepost'
//	            },
//	            reader: {
//	                root: 'topics',
//	                totalProperty: 'totalCount'
//	            },
//	            // sends single sort as multi parameter
//	            simpleSortMode: true,
//	            actionMethods : {
//	                create : 'POST',
//	                read   : 'POST',
//	                update : 'POST',
//	                destroy: 'POST'
//	            }
//	        },
//	        sorters: [{
//	            property: 'lastpost',
//	            direction: 'DESC'
//	        }]
		});
	}
}