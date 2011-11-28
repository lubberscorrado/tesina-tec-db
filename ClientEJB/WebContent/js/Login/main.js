/*VARIABILI GLOBALI*/
var _toolbar;
var _viewPort;
var _viewPort_panel_east;
var _viewPort_panel_west;


/* Funzione main */
var login_main = function registration_main(){
	
	var form_login = Ext.create('Ext.form.Panel', {
	    title: 'Login',
	    bodyPadding: 5,
	    width: 350,
	    //height: 600,

	    // The form will submit an AJAX request to this URL when submitted
	    url: 'login',
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
	        fieldLabel: 'Ristorante',
	        name: 'ristorante',
	        allowBlank: false
	    },{
	        fieldLabel: 'Username',
	        name: 'username',
	        allowBlank: false
	    },{
	        fieldLabel: 'Password',
	        name: 'password',
	        inputType: 'password',
	        allowBlank: false
	    }],

	    // Reset and Submit buttons
	    buttons: [{
	        text: 'Reset',
	        handler: function() {
	            this.up('form').getForm().reset();
	        }
	    }, {
	        text: 'Login',
	        formBind: true, //only enabled once the form is valid
	        disabled: true,
	        handler: function() {
	            var form = this.up('form').getForm();
	            if (form.isValid()) {
	                form.submit({
	                    success: function(form, action) {
	                    	Ext.Msg.alert('Info:', action.result.message);
	                    	location.replace('index_desktop.jsp');
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