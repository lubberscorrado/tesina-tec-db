/*VARIABILI GLOBALI*/
var _toolbar;
var _viewPort;
var _viewPort_panel_east;
var _viewPort_panel_west;


/* Funzione main */
var registration_main = function registration_main(){
	
	var form_registrazione = Ext.create('Ext.form.Panel', {
	    title: 'Registrazione nuovo ristorante',
	    bodyPadding: 5,
	    width: 350,
	    //height: 600,

	    // The form will submit an AJAX request to this URL when submitted
	    url: 'registrazione',
/*
	    // Fields will be arranged vertically, stretched to full width
	    layout: 'anchor',
	    defaults: {
	        anchor: '100%'
	    },
	    */

	    // The fields
	    defaultType: 'textfield',
	    items: [{
	        fieldLabel: 'Ragione Sociale',
	        name: 'ragioneSociale',
	        allowBlank: false
	    },{
	        fieldLabel: 'P.IVA/C.Fisc.',
	        name: 'piva',
	        allowBlank: false
	    },{
	        fieldLabel: 'Indirizzo',
	        name: 'indirizzo',
	        allowBlank: false
	    },{
	        fieldLabel: 'N°Civico',
	        name: 'numCivico',
	        allowBlank: false
	    },{
	        fieldLabel: 'CAP',
	        name: 'cap',
	        allowBlank: false
	    },{
	        fieldLabel: 'Città',
	        name: 'citta',
	        allowBlank: false
	    },{
	        fieldLabel: 'Provincia',
	        name: 'provincia',
	        allowBlank: false
	    },{
	        fieldLabel: 'Nazione',
	        name: 'nazione',
	        allowBlank: false
	    },{
	        fieldLabel: 'Telefono',
	        name: 'telefono',
	        allowBlank: false
	    },{
	        fieldLabel: 'Fax',
	        name: 'fax',
	        allowBlank: false
	    },{
	        fieldLabel: 'eMail',
	        name: 'email',
	        vtype: 'email',
	        allowBlank: false
	    },{
	        fieldLabel: 'Sito Web',
	        name: 'sitoWeb',
	        allowBlank: false
	    },{
	        fieldLabel: 'Nome utente',
	        name: 'username',
	        allowBlank: false
	    }],

	    // Reset and Submit buttons
	    buttons: [{
	        text: 'Reset',
	        handler: function() {
	            this.up('form').getForm().reset();
	        }
	    }, {
	        text: 'Submit',
	        formBind: true, //only enabled once the form is valid
	        disabled: true,
	        handler: function() {
	            var form = this.up('form').getForm();
	            if (form.isValid()) {
	                form.submit({
	                    success: function(form, action) {
	                    	Ext.Msg.alert('Info:', action.result.message);
	                    	setTimeout("javascript:location.replace('index_login.jsp')",5000);
	                    },
	                    failure: function(form, action) {
	                    	Ext.Msg.alert('Failed', action.result.message);
	                    }
	                });
	            }
	        }
	    }],
	    renderTo: 'div_centrale'
	});


};