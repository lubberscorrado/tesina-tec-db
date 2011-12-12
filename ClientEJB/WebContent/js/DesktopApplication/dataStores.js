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
	}
}