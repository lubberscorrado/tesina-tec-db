/*Creo lo store*/
Ext.create('Ext.data.Store', {
	storeId: 'datastore_stato_tavolo',
	groupField: 'nomeArea',
	model: 'StatoTavolo',
	autoLoad: true,
	//autoSync: true,
	pageSize: 50
	
});

Ext.create('Ext.data.Store', {
	storeId: 'datastore_variazione_voce_menu',
	//groupField: 'nomeArea',
	model: 'variazioneVoceMenu',
	autoLoad: true,
	autoSync: true,
	pageSize: 50,
	remoteFilter : true,
	listeners: {
		write: function(store, operation, eOpts ){
			console.debug('writerello');
			console.debug(eOpts);
			console.debug(operation);
			console.debug(store);
			
			operation.request.params.action = 'Cicisbeo';
			
		},
		update: function( store, record, operation, eOpts ){
			console.debug('updaterello');
			console.debug(eOpts);
			console.debug(operation);
			console.debug(store);
//			Ext.data.Model.EDIT
//			Ext.data.Model.REJECT
//			Ext.data.Model.COMMIT
			
		},
		remove: function( store, record, index, eOpts ){
			console.debug('removerello');
			console.debug(eOpts);
			console.debug(index);
			console.debug(record);
			console.debug(store);
		},
		beforesync: function( options, eOpts ){
//			console.debug('beforesync');
//			console.debug(options);
//			console.debug(eOpts);
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
    }
	
});

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
				console.debug('ENABLED ON');
			} else {
				newChildNode.set('enabled','off');
				console.debug('ENABLED OFF');
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
			console.debug('Append: depth='+newChildNode.get('depth')+' id='+newChildNode.get('id')+'\t\tparentId='+newChildNode.get('parentId')+'\t\ttext='+newChildNode.get('text')+'\t\tdescrizione='+newChildNode.get('descrizione'));
		},
		update: function( thisStore, record, operation, eOpts ){
			console.debug('UPDATE');
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
        text: "Menù"
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

