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
	root: {
        text: "Ristorante"
    },
    listeners: {
		beforeappend: function( thisNode, newChildNode, eOpts ){
			var tipo = newChildNode.get("tipo");
			if( tipo == 1){
            	newChildNode.set('leaf', false);
            	newChildNode.set('id', 'P'+newChildNode.get('realId'));
            	newChildNode.set('parentId', newChildNode.get('parentId'));
            	//newChildNode.set('icon', newChildNode.get('profile_image_url'));
            }else if( tipo == 2){
            	newChildNode.set('leaf', false);
            	newChildNode.set('id', 'A'+newChildNode.get('realId'));
            	newChildNode.set('parentId', 'P'+newChildNode.get('parentId'));
            	//newChildNode.set('icon', newChildNode.get('profile_image_url'));
            }else if( tipo == 3){
            	newChildNode.set('leaf', true);
            	newChildNode.set('id', 'T'+newChildNode.get('realId'));
            	newChildNode.set('parentId', 'A'+newChildNode.get('parentId'));
            	//newChildNode.set('icon', newChildNode.get('profile_image_url'));
            }
		},
		append: function( thisNode, newChildNode, index, eOpts ) {
			
	    }
    }
});