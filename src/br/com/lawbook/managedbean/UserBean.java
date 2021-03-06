package br.com.lawbook.managedbean;

import java.util.ResourceBundle;

import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionListener;

import org.hibernate.HibernateException;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.component.submenu.Submenu;
import org.primefaces.model.DefaultMenuModel;
import org.primefaces.model.MenuModel;

import br.com.lawbook.business.UserService;
import br.com.lawbook.model.Authority;
import br.com.lawbook.model.Profile;
import br.com.lawbook.model.User;
import br.com.lawbook.util.FacesUtil;

import com.sun.faces.taglib.jsf_core.SetPropertyActionListenerImpl;

/**
 * @author Edilson Luiz Ales Junior
 * @version 18NOV2011-07
 *  
 */
@ManagedBean
@RequestScoped
public class UserBean {

	private MenuModel menu;
	private UserService userService;

	public UserBean() {
		this.userService = UserService.getInstance();
		this.menu = new DefaultMenuModel();
		
		FacesContext context = FacesContext.getCurrentInstance();
		ExpressionFactory expressionFactory = context.getApplication().getExpressionFactory();
		ResourceBundle rs = ResourceBundle.getBundle("br.com.lawbook.util.messages", context.getViewRoot().getLocale());
		
		MenuItem item = new MenuItem();
		item.setId("menuItemHome");
		item.setValue(rs.getString("template_menu_home"));
		item.setUrl("/pages/home.jsf");
		this.menu.addMenuItem(item);
		
		// TODO Profile menu option doesn't fire in administration pages
		item = new MenuItem();
		item.setId("menuItemProfile");
		item.setValue(rs.getString("template_menu_profile"));
		ValueExpression target = expressionFactory.createValueExpression(context.getELContext(), "#{profileBean.profileOwner}", Profile.class);
		ValueExpression propertyValue = expressionFactory.createValueExpression(context.getELContext(),"#{profileBean.authProfile}", Profile.class);
		MethodExpression action = expressionFactory.createMethodExpression(context.getELContext(), "#{userBean.profileOutcome}", String.class, new Class[]{});
		ActionListener handler = new SetPropertyActionListenerImpl(target, propertyValue);
		item.setActionExpression(action);
        item.addActionListener(handler);
        item.setAjax(false);
		this.menu.addMenuItem(item);
		
		Submenu sub = new Submenu();
		sub.setId("subMenuAccount");
		sub.setLabel(rs.getString("template_menu_account"));
		
		try {
			User user = userService.getAuthorizedUser();
			for (Authority auth: user.getAuthority()) {
				if (auth.getName().equals("ADMIN")) {
					item = new MenuItem();
					item.setId("menuItemAdmin");
					item.setValue(rs.getString("template_menu_account_admin"));
					item.setUrl("/pages/admin/administration.jsf");
					sub.getChildren().add(item);
				}
			}
		} catch (IllegalArgumentException e) {
			FacesUtil.warnMessage("=|", e.getMessage());
		} catch (HibernateException e) {
			FacesUtil.errorMessage("=(", e.getMessage());
		} catch (Exception e) {
			FacesUtil.errorMessage("=(", e.getMessage());
		}
		
		item = new MenuItem();
		item.setId("menuItemSettings");
		item.setValue(rs.getString("template_menu_account_settings"));
		item.setUrl("/pages/settings.jsf");
		sub.getChildren().add(item);
		
		item = new MenuItem();
		item.setId("menuItemLogout");
		item.setValue(rs.getString("template_menu_account_logout"));
		item.setUrl("/j_spring_security_logout");
		sub.getChildren().add(item);
		
		this.menu.addSubmenu(sub);
	}

	public MenuModel getMenu() {
		return menu;
	}
	
	public String profileOutcome() {
		return "profile?faces-redirect=true";
	}
	
}
