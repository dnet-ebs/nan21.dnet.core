/**
 * DNet eBusiness Suite. Copyright: Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
/**
 * Mixin which provides elements map based definition of items.
 */
Ext.define("dnet.core.base.AbstractDNetView", {

	// **************** Properties *****************

	_refs_ : null,

	/**
	 * Elements definition map
	 */
	_elems_ : null,

	/**
	 * Name of the component which is to be used as the root-item for this view.
	 * By default is `main`
	 */
	_mainViewName_ : "main",

	/**
	 * Translations for elements.
	 * 
	 */
	_trl_ : null,

	// **************** Public API *****************

	/**
	 * Get an element instance if it is found.
	 * 
	 * @param name
	 * @return {}
	 */
	_getElement_ : function(name) {
		if (!this._refs_) {
			this._refs_ = {};
		}
		if (!this._refs_[name]) {
			this._refs_[name] = Ext.getCmp(this._elems_.get(name).id);
		}
		return this._refs_[name];
	},

	/**
	 * Shorthand alias for _getElement_
	 * 
	 * @param name
	 * @return {}
	 */
	_get_ : function(name) {
		return this._getElement_(name);
	},

	/**
	 * Get an element's configuration object.
	 * 
	 * @param name
	 * @return {}
	 */
	_getElementConfig_ : function(name) {
		return this._elems_.get(name);
	},

	/**
	 * Shorthand alias for _getElementConfig_
	 * 
	 * @param name
	 * @return {}
	 */
	_getConfig_ : function(name) {
		return this._elems_.get(name);
	},

	/**
	 * Similar to getElement but used for windows. If there is already a window
	 * instance created will return it otherwise creates a new window instance
	 * based on its configuration. If the window is configured destroy on-close
	 * will recreate it each time.
	 * 
	 * @param {String}
	 *            name Window name
	 * @return {Ext.window.Window}
	 */
	_getWindow_ : function(name) {
		var cfg = this._elems_.get(name);
		var w = Ext.getCmp(cfg.id);
		if (!Ext.isEmpty(w)) {
			return w;
		} else {
			if (cfg._window_) {
				return new Ext.window.Window(cfg);
			}
		}
		alert (name + ' is not a window!');
	},

	/**
	 * Helper method to activate a certain container of a stacked view type.
	 * 
	 * @param svn
	 *            Stacked view name
	 * @param idx
	 *            Child view to activate, can be the index of the child or its
	 *            name as registered in the elements map.
	 */
	_showStackedViewElement_ : function(svn, idx) {
		var _cmp = null;
		if (Ext.isNumber(idx)) {
			this._getElement_(svn).getLayout().setActiveItem(idx);
		} else {
			var ct = this._getElement_(svn);
			var cmp = this._getElement_(idx);
			if (cmp) {
				_cmp = cmp;
				ct.getLayout().setActiveItem(_cmp);
			} else {
				var _cmp = ct.items.indexOfKey(idx);
				ct.getLayout().setActiveItem(_cmp);
			}
			//_cmp.down('#' + _cmp.id + ' textfield').focus();
		}

	},

	gotoFirstNavigationItem : function() {
		this.down(':focusable').focus();
	},

	firstNavigationItem : null,
	// **************** Protected API *****************

	/**
	 * First method invoked during the elements setup. Template method to
	 * override with custom logic.
	 */
	_startDefine_ : function() {

	},

	/**
	 * Last method invoked during the elements setup. Template method to
	 * override with custom logic.
	 */
	_endDefine_ : function() {

	},

	/**
	 * Factory method to provide the elements definition in each subclass.
	 * Invoked from the initComponent method.
	 */
	_defineElements_ : function() {
	},

	/**
	 * Template method checked during elements definition flow. Return false to
	 * skip the _defineElements_ call.
	 * 
	 * @return {Boolean}
	 */
	_beforeDefineElements_ : function() {
		return true;
	},

	/**
	 * Template method invoked after the elements definition flow. Used mainly
	 * to add overrides to existing components.
	 * 
	 * @return {Boolean}
	 */
	_afterDefineElements_ : function() {
	},

	/**
	 * Factory method to provide the elements linking in each subclass. Invoked
	 * from the initComponent method. Linking means to build up the components
	 * hierarchy. It can be specified in the elements definition also but this
	 * way makes life easier in the overrides to re-arrange the components.
	 */
	_linkElements_ : function() {
	},

	/**
	 * Template method checked during elements linking flow. Return false to
	 * skip the _linkElements_ call.
	 * 
	 * @return {Boolean}
	 */
	_beforeLinkElements_ : function() {
		return true;
	},

	/**
	 * Template method invoked after the elements linking. Used mainly to add
	 * overrides to existing components.
	 */
	_afterLinkElements_ : function() {
	},

	// **************** Private methods *****************

	_runElementBuilder_ : function() {

		this._elems_ = new Ext.util.MixedCollection();

		this._startDefine_();

		if (this._beforeDefineElements_() !== false) {
			this._defineElements_();
			this._afterDefineElements_();
		}
		this._elems_.each(this._postProcessElem_, this);

		if (this._beforeLinkElements_() !== false) {
			this._linkElements_();
			this._afterLinkElements_();
		}

		this._endDefine_();

		this.items = [ this._elems_.get(this._mainViewName_) ];

	},

	_beforeDestroyDNetView_ : function() {
		for ( var p in this._refs_) {
			delete this._refs_[p];
		}
	}

});