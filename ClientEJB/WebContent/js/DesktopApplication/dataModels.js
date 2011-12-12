/* DEFINIZIONE DI UN WRITER PER LA RICHIESTA CLASSICA DEI PARAMETRI */
Ext.define('Ext.data.writer.SinglePost', {
    extend: 'Ext.data.writer.Writer',
    alternateClassName: 'Ext.data.SinglePostWriter',
    alias: 'writer.singlepost',

    writeRecords: function(request, data) {
    	data[0].action = request.params.action;
        request.params = data[0];
        return request;
    }
});

Ext.define('Ext.data.writer.SinglePostNoAction', {
    extend: 'Ext.data.writer.Writer',
    alternateClassName: 'Ext.data.SinglePostNoActionWriter',
    alias: 'writer.singlepostnoaction',

    writeRecords: function(request, data) {
    	console.debug(request);
    	console.debug(data);
    	//data[0].action = request.params.action;
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
        url : 'statoTavolo',
        appendId: true,
        writer: {
            type: 'singlepost'
        },
		reader: {
	        type: 'json',
	        model: 'StatoTavolo',
	        idProperty: 'id_tavolo',
	        root: 'statoTavolo'
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
    fields: ['id','parentId','nome','descrizione','numeroPiano','tipo','enabled','numPosti','stato','text'],
    //store: Ext.getStore('datastore_gestione_tavolo'),
    proxy: {
        type: 'rest',
        url : 'gestioneTavolo',
        //url : 'services/gestioneTavolo',
        appendId : false,
        writer: {
        	//type: 'json'
        		type: 'singlepost'/*,
            	encode: false,
                writeAllFields: true,
                listful: true*/
        },
		reader: {
	        type: 'json',
	        idProperty: 'id',
	        root: 'data'
	    },
	    actionMethods : {
            create : 'POST',
            read   : 'GET',
            update : 'POST',
            destroy: 'POST'
        }
    }
});

Ext.define('nodoGestioneMenu', {
	extend: 'Ext.data.Model',
    fields: ['id','parentId','nome','descrizione','prezzo','tipo','text'],
    //store: Ext.getStore('datastore_gestione_menu'),
    proxy: {
        type: 'rest',
        url : 'gestioneMenu',
        appendId : false,
        writer: {
            type: 'singlepost'
            //type: 'json'
        },
		reader: {
	        type: 'json',
	        idProperty: 'id',
	        root: 'data'
	    },
	    actionMethods : {
            create : 'POST',
            read   : 'GET',
            update : 'POST',
            destroy: 'POST'
        },
        listeners: {
        	exception: function( thisProxy, response, operation, eOpts ){
        		console.debug('Proxy exception');
        		console.debug(eOpts);
        	}
        }
    }
});

Ext.define('variazioneVoceMenu', {
	extend: 'Ext.data.Model',
    fields: ['id','parentId','nome','descrizione','prezzo','tipo','text','categoriaDiAppartenenza','isEreditata','action','idCategoria'],
    phantom : true,
//    store: Ext.getStore('datastore_gestione_menu'),
    proxy: {
        type: 'rest',
        url : 'variazioneVoceMenu',
        appendId : false,
        writer: {
            type: 'singlepostnoaction'
            //type: 'json'
        },
		reader: {
	        type: 'json',
	        idProperty: 'id',
	        root: 'data'
	    },
	    actionMethods : {
            create : 'POST',
            read   : 'GET',
            update : 'POST',
            destroy: 'POST'
        }
    }
});