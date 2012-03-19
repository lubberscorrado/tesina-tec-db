///////////////////////////////////////////////////////////////////////
// DEFINIZIONE DI UN WRITER PER LA RICHIESTA CLASSICA DEI PARAMETRI //
/////////////////////////////////////////////////////////////////////
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
        request.params = data[0];
        return request;
    }
});

///////////////////////////////////////
// DEFINIZIONE DEI MODELLI DEI DATI //
/////////////////////////////////////

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

/*
 * Ext.define('variazioneVoceMenu', {
	extend: 'Ext.data.Model',
    fields: ['id','parentId','nome','descrizione','prezzo','tipo','text','categoriaDiAppartenenza','isEreditata','idCategoria'],
    phantom : true,
//    store: Ext.getStore('datastore_gestione_menu'),
    proxy: {
        type: 'rest',
//        url : 'variazioneVoceMenu',
        appendId : false,
        api: {
            create: 	'variazioneVoceMenu?action=create',
            read: 		'variazioneVoceMenu?action=read',
            update: 	'variazioneVoceMenu?action=update',
            destroy: 	'variazioneVoceMenu?action=delete',
        },
        writer: {
            type: 'singlepostnoaction'
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
 */

Ext.define('Comanda', {
    extend: 'Ext.data.Model',
    /*comande":[{"stato":INVIATA,"variazioni":[],"nomeVoceMenu":"Bucatini all'amatriciana","prezzoVoceMenu":10,"idComanda":1,"quantita":3,"note":""},
     * {"stato":INVIATA,"variazioni":[],"nomeVoceMenu":"Tortellini alla panna","prezzoVoceMenu":11,"idComanda":2,"quantita":3,"note":"\n\n"},{"stato":INVIATA,"variazioni"*/
    fields: [
             {name: 'idComanda', 		type: 'string',		useNull: true},
            // {name: 'idTavolo',  		type: 'int'},
             //{name: 'idRemoto',  		type: 'int'},
             {name: 'stato', 			type: 'string'},
             {name: 'nomeVoceMenu', 	type: 'string'},
             {name: 'quantita', 		type: 'string'},
             {name: 'prezzoVoceMenu',	type: 'string'},
             {name: 'variazioni',    	type: 'text'},
             {name: 'note',    			type: 'string'}
    ],
    proxy: {
        type: 'rest',
        appendId: false,
	    api: {
	    	read: 		'gestioneConti?action=VISUALIZZA_CONTO',
            create: 	'gestioneConti?action=create',
            update: 	'gestioneConti?action=update',
            destroy: 	'gestioneConti'
        },
        writer: {
            type: 'singlepost'
        },
		reader: {
	        type: 'json',
	        model: 'Comanda',
	        idProperty: 'idComanda',
	        root: 'comande'
	    },
	    actionMethods : {
            create : 'POST',
            read   : 'POST',
            update : 'POST',
            destroy: 'POST'
        }
    }
});

Ext.define('Conto', {
    extend: 'Ext.data.Model',
    fields: [
             {name: 'idConto', 				type: 'int',		useNull: true},
             {name: 'timestampApertura', 	type: 'string'},
             {name: 'timestampChiusura', 	type: 'string'},
             {name: 'stato', 				type: 'string'},
             {name: 'prezzo',				type: 'string'}
    ],
    proxy: {
        type: 'rest',
        appendId: false,
	    api: {
	    	read: 		'gestioneConti?action=GET_ELENCO_CONTI',
            create: 	'gestioneConti?action=create',
            update: 	'gestioneConti?action=update',
            destroy: 	'gestioneConti'
        },
        writer: {
            type: 'singlepost'
        },
		reader: {
	        type: 'json',
	        model: 'Conto',
	        idProperty: 'idConto',
	        root: 'conti'
	    },
	    actionMethods : {
            create : 'POST',
            read   : 'POST',
            update : 'POST',
            destroy: 'POST'
        }
    }
});

Ext.define('nodoGestioneTavolo', {
    /*extend: 'Ext.data.NodeInterface',*/
	extend: 'Ext.data.Model',
    fields: ['id','parentId','nome','descrizione','numeroPiano','tipo','numPosti','stato','text',
             {name: 'enabled', 	type: 'bool'}
    ],
    //store: Ext.getStore('datastore_gestione_tavolo'),
    proxy: {
        type: 'rest',
//        url : 'gestioneTavolo',
        //url : 'services/gestioneTavolo',
        appendId : false,
        api: {
            create: 	'gestioneTavolo?action=create',
            read: 		'gestioneTavolo?action=read',
            update: 	'gestioneTavolo?action=update',
            destroy: 	'gestioneTavolo',
        },
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
//        url : 'gestioneMenu',
        appendId : false,
        api: {
            create: 	'gestioneMenu?action=create',
            read: 		'gestioneMenu?action=read',
            update: 	'gestioneMenu?action=update',
            destroy: 	'gestioneMenu',
        },
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
    fields: ['id','parentId','nome','descrizione','prezzo','tipo','text','categoriaDiAppartenenza','isEreditata',/*'action',*/'idCategoria'],
    phantom : true,
//    store: Ext.getStore('datastore_gestione_menu'),
    proxy: {
        type: 'rest',
//        url : 'variazioneVoceMenu',
        appendId : false,
        api: {
            create: 	'variazioneVoceMenu?action=create',
            read: 		'variazioneVoceMenu?action=read',
            update: 	'variazioneVoceMenu?action=update',
            destroy: 	'variazioneVoceMenu?action=delete',
        },
        writer: {
            type: 'singlepostnoaction'
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

Ext.define('personale', {
	extend: 'Ext.data.Model',
    fields: ['id','username','passwd','passwd2','nome','cognome',
             {name: 'isCameriere', 	type: 'bool'},
             {name: 'isCassiere', 	type: 'bool'},
             {name: 'isCucina', 	type: 'bool'},
             {name: 'isAdmin', 		type: 'bool'}
    ],
    phantom : true,
//    store: Ext.getStore('datastore_gestione_menu'),
    proxy: {
        type: 'rest',
//        url : 'gestionePersonale',
        appendId : false,
        api: {
            create: 	'gestionePersonale?action=create',
            read: 		'gestionePersonale?action=read',
            update: 	'gestionePersonale?action=update',
            destroy: 	'gestionePersonale',
        },
        writer: {
            type: 'singlepost'
        },
		reader: {
	        type: 'json',
	        idProperty: 'id',
	        root: 'data',
	        messageProperty : 'message',
//	        failure: function(form, action) {
//                Ext.Msg.alert('Errore: ', action.result.message);
//            }
	    },
	    actionMethods : {
            create : 'POST',
            read   : 'GET',
            update : 'POST',
            destroy: 'POST'
        },
//        afterRequest:function(request,success){
////        	console.debug('inizio AFTEREQUEST');
////        	console.debug(request);
////        	console.debug(success);
////        	console.debug('fine AFTEREQUEST');
//            
//        },
        listeners: {
//        	write: function (proxy, action) {
//        	    console.log('proxy-write');
//        	},
//        	afterRequest:function(request,success){
//                if(request.method = 'PUT'){
//                     // your code
//                }
//                console.debug('PROXY AFTER REQUEST');
//                console.debug(success);
//            },
        	exception: function (proxy, response, operation) {
        		console.debug('EXCEPTION');
                var json = Ext.decode(response.responseText);
                console.debug(json);
                if(json.success){//Success
                	console.debug('SUCCESS');
                }else{//Failure
                	console.debug('FAILURE');
//                	new Ext.ux.Notification({
//                		iconCls:	'x-icon-information',
//	        			title:	  'Errore',
//	        			html:		json.message,
//	        			autoDestroy: true,
//	        			hideDelay:  5000,
//        			}).show(document);
                	Ext.MessageBox.show({
		                title: 'EXCEPTION',
		                msg: operation.getError(),
		                icon: Ext.MessageBox.ERROR,
		                autoDestroy: true,
		      			hideDelay:  2000,
		                buttons: Ext.Msg.OK
                  });
                }
//                if (json) {
//                    detl.getForm().markInvalid(json.errors);
//                    Ext.MessageBox.show({
//                        title: 'Save Failed',
//                        msg: json.message,
//                        icon: Ext.MessageBox.ERROR,
//                        buttons: Ext.Msg.OK
//                    });
//                } else
//                Ext.MessageBox.show({
//                    title: 'EXCEPTION',
//                    msg: operation.getError(),
//                    icon: Ext.MessageBox.ERROR,
//                    buttons: Ext.Msg.OK
//                });
            }
        }
    }
});

//var originalModelSave = Ext.data.Model.prototype.save;
//Ext.override(Ext.data.Model, {
//    save: function() {
//    	//alert('KAsdasd');
//    	console.debug('TANTE TROIE DI ALTRI TEMPII DAI CAZZOOO');
//    	//originalModelSave.apply(this, arguments);
//    	 this.callOverridden({
//    		success: function(rec, op) {
//    			Ext.Msg.alert("Failed",'Successo lento');
//    		},
//    		failure: function(rec, op) {
//    			Ext.Msg.alert("Failed",'Fallimento lento');
//    		}
//    	 });
//    }
//});


Ext.define('StoricoConto', {
    extend: 'Ext.data.Model',
    fields: [
             {name: 'idConto', 				type: 'int',		useNull: true},
             {name: 'timestampApertura', 	type: 'string'},
             {name: 'timestampChiusura', 	type: 'string'},
             {name: 'stato', 				type: 'string'},
             {name: 'prezzo',				type: 'string'}
    ],
    idProperty: 'threadid',
    proxy: {
        type: 'rest',
        appendId: false,
	    api: {
	    	read: 		'gestioneConti?action=GET_STORICO_CONTI',
            create: 	'gestioneConti?action=create',
            update: 	'gestioneConti?action=update',
            destroy: 	'gestioneConti'
        },
        writer: {
            type: 'singlepost'
        },
		reader: {
	        type: 'json',
	        model: 'Conto',
	        idProperty: 'idConto',
	        root: 'conti',
	        totalProperty: 'results'
	    },
	    actionMethods : {
            create : 'POST',
            read   : 'POST',
            update : 'POST',
            destroy: 'POST'
        }
    }
});

