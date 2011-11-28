function get_main_toolbar(){
	tmp = Ext.create('Ext.toolbar.Toolbar', {
		id: 'main_toolbar',
		region: 'north',
		items: [
		               {
		                   // xtype: 'button', // default for Toolbars
		                   text: 'Button'
		               },
		               {
		                   xtype: 'splitbutton',
		                   text : 'Split Button',
		                   menu: new Ext.menu.Menu({
		                       items: [
		                           // these will render as dropdown menu items when the arrow is clicked:
		                           {text: 'Item 1', handler: function(){
		                        	   alert("Item 1 clicked");
		                        	   var new_menu_node = new nodoGestioneTavolo();//Ext.ModelManager.getModel('nodoGestioneTavolo');
		                        	   new_menu_node.set('tipo','1');
		                        	   new_menu_node.set('nome','troiette di altri tempi');
		                        	   new_menu_node.save();
		                        	   }
		                           },
		                           {text: 'Item 2', handler: function(){
		                        	   alert("Item 2 clicked");
		                        	   var new_menu_node = new nodoGestioneMenu();
		                        	   new_menu_node.save();
		                        	   }
		                           }
		                       ]
		                   })
		               },
		               // begin using the right-justified button container
		               '->', // same as { xtype: 'tbfill' }
		               {
		                   xtype    : 'textfield',
		                   name     : 'field1',
		                   emptyText: 'enter search term'
		               },
		               // add a vertical separator bar between toolbar items
		               '-', // same as {xtype: 'tbseparator'} to create Ext.toolbar.Separator
		               'text 1', // same as {xtype: 'tbtext', text: 'text1'} to create Ext.toolbar.TextItem
		               { xtype: 'tbspacer' },// same as ' ' to create Ext.toolbar.Spacer
		               'text 2',
		               { xtype: 'tbspacer', width: 50 }, // add a 50px space
		               'text 3',{
		                   xtype: 'button',
		                   text: 'LogOut'
		               }
		]/*,
		renderTo: Ext.getBody()*/
	});
	return tmp;
}