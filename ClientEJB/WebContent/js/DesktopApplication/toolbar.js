function get_main_toolbar(){
	tmp = Ext.create('Ext.toolbar.Toolbar', {
		id: 'main_toolbar',
		region: 'north',
		items: [	'Utente: '+_global._user+' @ '+_global._restaurant,
		        	'->',{
		                   xtype: 'button',
		                   text: 'LogOut',
		                   handler: function(){
		                	   Ext.Ajax.request({
		                		    url: 'login',
		                		    method: 'POST',
		                		    params: {
		                		        action: 'logout'
		                		    },
		                		    success: function(response){
		                		    	location.replace('index_login.jsp');
		                		    }
		                		});
		                   }
		               }
		]
	});
	return tmp;
}