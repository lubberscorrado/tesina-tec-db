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
    store: Ext.getStore('datastore_gestione_tavolo'),
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

Ext.define('nodoGestioneMenu', {
	extend: 'Ext.data.Model',
    fields: ['id','realId','parentId','realParentId','nome','descrizione','tipo','enabled','numPosti','stato','text'],
    store: Ext.getStore('datastore_gestione_menu'),
    proxy: {
        type: 'rest',
        url : 'gestioneMenu',
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