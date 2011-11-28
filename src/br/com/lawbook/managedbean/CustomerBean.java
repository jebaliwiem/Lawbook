package br.com.lawbook.managedbean;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.hibernate.HibernateException;

import br.com.lawbook.business.service.LocationService;
import br.com.lawbook.business.service.ProfileService;
import br.com.lawbook.model.Location;
import br.com.lawbook.model.Profile;
import br.com.lawbook.util.FacesUtil;

/**
 * @author Edilson Luiz Ales Junior
 * @version 28NOV2011-04
 *
 */
@ManagedBean
@ViewScoped
public class CustomerBean implements Serializable {

	private Date birth;
	private String rg;
	private String cpf;
	private String cnpj;
	private String phone;
	private Long profileId;
	private Location local;
	private Profile customer;
	private static final long serialVersionUID = 4793288994547648055L;
	private static final Logger LOG = Logger.getLogger("CustomerBean");
	private static final ProfileService PROFILE_SERVICE = ProfileService.getInstance();
	private static final LocationService LOCATION_SERVICE = LocationService.getInstance();

	public CustomerBean() {
		LOG.info("#### CustomerBean created");
		try {
			this.profileId = Long.valueOf(FacesUtil.getExternalContext().getRequestParameterMap().get("newUserProfileId"));
			this.customer = PROFILE_SERVICE.getProfileById(this.profileId);
			this.birth = new Date();
			this.local = new Location();
			
			if (this.customer.getBirth() != null) this.birth = this.customer.getBirth().getTime();
			if (this.customer.getRg() != null) this.rg = this.customer.getRg().toString();
			if (this.customer.getCpf() != null) this.cpf = this.customer.getCpf().toString();
			if (this.customer.getCnpj() != null) this.cnpj = this.customer.getCnpj().toString();
			if (this.customer.getPhone() != null) this.phone = this.customer.getPhone().toString();
			if (this.customer.getLocation() != null) this.local = this.customer.getLocation();

		} catch (final IllegalArgumentException e) {
			FacesUtil.warnMessage("=|", e.getMessage());
		} catch (final HibernateException e) {
			FacesUtil.errorMessage("=(", e.getMessage());
		}
	}

	public void updateProfile() {
		LOG.info("#### updateProfile()");
		try {
			if (!this.rg.isEmpty()) {
				this.customer.setRg(Long.valueOf(this.rg));
				LOG.info("#### updateProfile(): rg");
			}
			
			if (!this.phone.isEmpty()) {
				this.phone = this.phone.replaceAll("[^0-9]", "");
				this.customer.setPhone(Long.valueOf(this.phone));
				LOG.info("#### updateProfile(): phone");
			}
			
			if (!this.cpf.isEmpty()) {
				this.cpf = this.cpf.replaceAll("[^0-9]", "");
				if (!PROFILE_SERVICE.cpfValidation(this.cpf)) {
					FacesUtil.warnMessage("=|", "Invalid CPF");
					return;
				}
				this.customer.setCpf(Long.valueOf(this.cpf));
				LOG.info("#### updateProfile(): cpf");
			}
			if (!this.cnpj.isEmpty()) {
				this.cnpj = this.cnpj.replaceAll("[^0-9]", "");
				if (!PROFILE_SERVICE.cnpjValidation(this.cnpj)) {
					FacesUtil.warnMessage("=|", "Invalid CNPJ");
					return;
				}
				this.customer.setCnpj(Long.valueOf(this.cnpj));
				LOG.info("#### updateProfile(): cnpj");
			}
			
			if (this.local.getId() == null) {
				LOCATION_SERVICE.save(this.local);
				LOG.info("#### updateProfile(): Location saved successfully");
			} else {
				LOCATION_SERVICE.update(this.local);
				LOG.info("#### updateProfile(): Location updated successfully");
			}
			this.customer.setLocation(this.local);
			LOG.info("#### updateProfile(): local");
			
			final Calendar auxDate = Calendar.getInstance();
			auxDate.setTime(this.birth);
			this.customer.setBirth(auxDate);
			LOG.info("#### updateProfile(): birth");
			
			PROFILE_SERVICE.update(this.customer);

			this.customer = new Profile();
			this.local = new Location();
			this.birth = new Date();
			this.rg = "";
			this.cpf = "";
			this.cnpj = "";
			this.phone = "";

			LOG.info("#### updateProfile(): Profile updated successfully");
			FacesUtil.infoMessage("=)", "Profile updated successfully");
		} catch (final NumberFormatException e) {
			FacesUtil.warnMessage("=|", e.getMessage());
		} catch (final IllegalArgumentException e) {
			FacesUtil.warnMessage("=|", e.getMessage());
		} catch (final HibernateException e) {
			FacesUtil.errorMessage("=(", e.getMessage());
		}
	}

	public Long getProfileId() {
		return this.profileId;
	}

	public Date getBirth() {
		return this.birth;
	}

	public void setBirth(final Date birth) {
		this.birth = birth;
	}

	public Profile getCustomer() {
		return this.customer;
	}

	public void setCustomer(final Profile customer) {
		this.customer = customer;
	}

	public String getRg() {
		return this.rg;
	}

	public void setRg(final String rg) {
		this.rg = rg;
	}

	public String getCpf() {
		return this.cpf;
	}

	public void setCpf(final String cpf) {
		this.cpf = cpf;
	}

	public String getCnpj() {
		return this.cnpj;
	}

	public void setCnpj(final String cnpj) {
		this.cnpj = cnpj;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(final String phone) {
		this.phone = phone;
	}

	public Location getLocal() {
		return this.local;
	}

	public void setLocal(final Location local) {
		this.local = local;
	}

}
