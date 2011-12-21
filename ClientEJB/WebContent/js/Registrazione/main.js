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
	        allowBlank: false,
	        value: 'Ristorante Pesce moscio'
	    },{
	        fieldLabel: 'P.IVA/C.Fisc.',
	        name: 'piva',
	        allowBlank: false,
	        value: '1234324234'
	    },{
	        fieldLabel: 'Indirizzo',
	        name: 'indirizzo',
	        allowBlank: false,
	        value: 'Via caduti all\'unimore'
	    },{
	        fieldLabel: 'N°Civico',
	        name: 'numCivico',
	        allowBlank: false,
	        value: '654'
	    },{
	        fieldLabel: 'CAP',
	        name: 'cap',
	        allowBlank: false,
	        value: '41100'
	    },{
	        fieldLabel: 'Città',
	        name: 'citta',
	        allowBlank: false,
	        value: 'Modena'
	    },{
	        fieldLabel: 'Provincia',
	        name: 'provincia',
	        allowBlank: false,
	        value: 'Modena'
	    },{
	        fieldLabel: 'Nazione',
	        name: 'nazione',
	        allowBlank: false,
	        value: 'Italia'
	    },{
	        fieldLabel: 'Telefono',
	        name: 'telefono',
	        allowBlank: false,
	        value: '059326598'
	    },{
	        fieldLabel: 'Fax',
	        name: 'fax',
	        allowBlank: false,
	        value: '059326598'
	    },{
	        fieldLabel: 'eMail',
	        name: 'email',
	        vtype: 'email',
	        allowBlank: false,
	        value: 'kastknocker@gmail.com'
	    },{
	        fieldLabel: 'Sito Web',
	        name: 'sitoWeb',
	        allowBlank: false,
	        value: 'www.forzagnocca.it'
	    },{
	        fieldLabel: 'Nome utente',
	        name: 'username',
	        allowBlank: false,
	        value: 'kastknocker'
	    },{
            xtype: 'displayfield',
            //name: 'displayfield1',
            //fieldLabel: 'Registrati',
            value: 'Se sei già registrato e vuoi effettuare l\'accesso clicca <a href="index_login.jsp">qui</a>!'
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