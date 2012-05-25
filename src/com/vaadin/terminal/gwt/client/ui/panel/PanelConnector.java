/*
@VaadinApache2LicenseForJavaFiles@
 */
package com.vaadin.terminal.gwt.client.ui.panel;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.ComponentConnector;
import com.vaadin.terminal.gwt.client.ConnectorHierarchyChangeEvent;
import com.vaadin.terminal.gwt.client.LayoutManager;
import com.vaadin.terminal.gwt.client.MouseEventDetails;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.communication.RpcProxy;
import com.vaadin.terminal.gwt.client.ui.AbstractComponentContainerConnector;
import com.vaadin.terminal.gwt.client.ui.ClickEventHandler;
import com.vaadin.terminal.gwt.client.ui.Connect;
import com.vaadin.terminal.gwt.client.ui.PostLayoutListener;
import com.vaadin.terminal.gwt.client.ui.ShortcutActionHandler;
import com.vaadin.terminal.gwt.client.ui.SimpleManagedLayout;
import com.vaadin.terminal.gwt.client.ui.layout.MayScrollChildren;
import com.vaadin.ui.Panel;

@Connect(Panel.class)
public class PanelConnector extends AbstractComponentContainerConnector
        implements Paintable, SimpleManagedLayout, PostLayoutListener,
        MayScrollChildren {

    private Integer uidlScrollTop;

    private ClickEventHandler clickEventHandler = new ClickEventHandler(this) {

        @Override
        protected void fireClick(NativeEvent event,
                MouseEventDetails mouseDetails) {
            rpc.click(mouseDetails);
        }
    };

    private Integer uidlScrollLeft;

    private PanelServerRpc rpc;

    @Override
    public void init() {
        rpc = RpcProxy.create(PanelServerRpc.class, this);
        VPanel panel = getWidget();
        LayoutManager layoutManager = getLayoutManager();

        layoutManager.registerDependency(this, panel.captionNode);
        layoutManager.registerDependency(this, panel.bottomDecoration);
        layoutManager.registerDependency(this, panel.contentNode);
    }

    @Override
    public void onUnregister() {
        VPanel panel = getWidget();
        LayoutManager layoutManager = getLayoutManager();

        layoutManager.unregisterDependency(this, panel.captionNode);
        layoutManager.unregisterDependency(this, panel.bottomDecoration);
        layoutManager.unregisterDependency(this, panel.contentNode);
    }

    @Override
    public boolean delegateCaptionHandling() {
        return false;
    }

    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        if (isRealUpdate(uidl)) {

            // Handle caption displaying and style names, prior generics.
            // Affects size calculations

            // Restore default stylenames
            getWidget().contentNode.setClassName(VPanel.CLASSNAME + "-content");
            getWidget().bottomDecoration.setClassName(VPanel.CLASSNAME
                    + "-deco");
            getWidget().captionNode.setClassName(VPanel.CLASSNAME + "-caption");
            boolean hasCaption = false;
            if (getState().getCaption() != null
                    && !"".equals(getState().getCaption())) {
                getWidget().setCaption(getState().getCaption());
                hasCaption = true;
            } else {
                getWidget().setCaption("");
                getWidget().captionNode.setClassName(VPanel.CLASSNAME
                        + "-nocaption");
            }

            // Add proper stylenames for all elements. This way we can prevent
            // unwanted CSS selector inheritance.
            final String captionBaseClass = VPanel.CLASSNAME
                    + (hasCaption ? "-caption" : "-nocaption");
            final String contentBaseClass = VPanel.CLASSNAME + "-content";
            final String decoBaseClass = VPanel.CLASSNAME + "-deco";
            String captionClass = captionBaseClass;
            String contentClass = contentBaseClass;
            String decoClass = decoBaseClass;
            if (getState().hasStyles()) {
                for (String style : getState().getStyles()) {
                    captionClass += " " + captionBaseClass + "-" + style;
                    contentClass += " " + contentBaseClass + "-" + style;
                    decoClass += " " + decoBaseClass + "-" + style;
                }
            }
            getWidget().captionNode.setClassName(captionClass);
            getWidget().contentNode.setClassName(contentClass);
            getWidget().bottomDecoration.setClassName(decoClass);
        }

        if (!isRealUpdate(uidl)) {
            return;
        }

        clickEventHandler.handleEventHandlerRegistration();

        getWidget().client = client;
        getWidget().id = uidl.getId();

        if (getState().getIcon() != null) {
            getWidget().setIconUri(getState().getIcon().getURL(), client);
        } else {
            getWidget().setIconUri(null, client);
        }

        getWidget().setErrorIndicatorVisible(
                null != getState().getErrorMessage());

        // We may have actions attached to this panel
        if (uidl.getChildCount() > 0) {
            final int cnt = uidl.getChildCount();
            for (int i = 0; i < cnt; i++) {
                UIDL childUidl = uidl.getChildUIDL(i);
                if (childUidl.getTag().equals("actions")) {
                    if (getWidget().shortcutHandler == null) {
                        getWidget().shortcutHandler = new ShortcutActionHandler(
                                getConnectorId(), client);
                    }
                    getWidget().shortcutHandler.updateActionMap(childUidl);
                }
            }
        }

        if (getState().getScrollTop() != getWidget().scrollTop) {
            // Sizes are not yet up to date, so changing the scroll position
            // is deferred to after the layout phase
            uidlScrollTop = getState().getScrollTop();
        }

        if (getState().getScrollLeft() != getWidget().scrollLeft) {
            // Sizes are not yet up to date, so changing the scroll position
            // is deferred to after the layout phase
            uidlScrollLeft = getState().getScrollLeft();
        }

        // And apply tab index
        getWidget().contentNode.setTabIndex(getState().getTabIndex());
    }

    public void updateCaption(ComponentConnector component) {
        // NOP: layouts caption, errors etc not rendered in Panel
    }

    @Override
    public VPanel getWidget() {
        return (VPanel) super.getWidget();
    }

    public void layout() {
        updateSizes();
    }

    void updateSizes() {
        VPanel panel = getWidget();

        LayoutManager layoutManager = getLayoutManager();
        int top = layoutManager.getOuterHeight(panel.captionNode);
        int bottom = layoutManager.getInnerHeight(panel.bottomDecoration);

        Style style = panel.getElement().getStyle();
        panel.captionNode.getParentElement().getStyle()
                .setMarginTop(-top, Unit.PX);
        panel.bottomDecoration.getStyle().setMarginBottom(-bottom, Unit.PX);
        style.setPaddingTop(top, Unit.PX);
        style.setPaddingBottom(bottom, Unit.PX);

        // Update scroll positions
        panel.contentNode.setScrollTop(panel.scrollTop);
        panel.contentNode.setScrollLeft(panel.scrollLeft);
        // Read actual value back to ensure update logic is correct
        panel.scrollTop = panel.contentNode.getScrollTop();
        panel.scrollLeft = panel.contentNode.getScrollLeft();
    }

    public void postLayout() {
        VPanel panel = getWidget();
        if (uidlScrollTop != null) {
            panel.contentNode.setScrollTop(uidlScrollTop.intValue());
            // Read actual value back to ensure update logic is correct
            // TODO Does this trigger reflows?
            panel.scrollTop = panel.contentNode.getScrollTop();
            uidlScrollTop = null;
        }

        if (uidlScrollLeft != null) {
            panel.contentNode.setScrollLeft(uidlScrollLeft.intValue());
            // Read actual value back to ensure update logic is correct
            // TODO Does this trigger reflows?
            panel.scrollLeft = panel.contentNode.getScrollLeft();
            uidlScrollLeft = null;
        }
    }

    @Override
    public PanelState getState() {
        return (PanelState) super.getState();
    }

    @Override
    public void onConnectorHierarchyChange(ConnectorHierarchyChangeEvent event) {
        super.onConnectorHierarchyChange(event);
        // We always have 1 child, unless the child is hidden
        Widget newChildWidget = null;
        if (getChildComponents().size() == 1) {
            ComponentConnector newChild = getChildComponents().get(0);
            newChildWidget = newChild.getWidget();
        }

        getWidget().setWidget(newChildWidget);
    }

}
