package com.aizong.ishtirak.gui.form;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.aizong.ishtirak.bean.CurrencyManager.SupportedCurrency;
import com.aizong.ishtirak.common.form.BasicForm;
import com.aizong.ishtirak.common.misc.utils.ComponentUtils;
import com.aizong.ishtirak.common.misc.utils.MessageUtils;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;
import com.aizong.ishtirak.gui.table.service.RefreshTableInterface;
import com.aizong.ishtirak.model.Company;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;

public class CompanyForm extends BasicForm implements RefreshTableInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private JTextField txtName;
    private JTextArea txtAddress;
    private JTextField txtLandLine;
    private JTextField txtMainMobilePhone;
    private JTextField txtOtherMobilePhone;
    private JTextArea txtNote;
    private JComboBox<SupportedCurrency> supportedCurrencyCombobox;

    private Company company;

    public CompanyForm() {

	company = ServiceProvider.get().getSubscriberService().getCompany();
	initializePanel();
	fillData();
    }

    private void fillData() {

	if (company != null) {
	    txtName.setText(company.getName());
	    txtAddress.setText(company.getAddress());
	    txtLandLine.setText(company.getLandLine());
	    txtMainMobilePhone.setText(company.getMainMobilePhone());
	    txtNote.setText(company.getNote());
	    txtOtherMobilePhone.setText(company.getOtherMobilePhone());
	    supportedCurrencyCombobox.setSelectedItem(company.getSelectedCurrency());
	}
    }

    @Override
    protected void initComponents() {
	txtName = new JTextField();
	txtAddress = ComponentUtils.createTextArea();
	txtLandLine = new JTextField();
	txtMainMobilePhone = new JTextField();
	txtOtherMobilePhone = new JTextField();
	supportedCurrencyCombobox = new JComboBox<>(SupportedCurrency.values());
	txtNote = ComponentUtils.createTextArea(3, 1);
    }

    @Override
    protected Component buildPanel(DefaultFormBuilder builder) {
	builder.appendSeparator(message("company.form.seperator"));
	builder.setDefaultDialogBorder();
	builder.append(message("village.form.name"), txtName);
	builder.append(message("subsriber.form.address"), txtAddress);
	builder.append(message("subsriber.form.landLine"), txtLandLine);
	builder.append(message("subsriber.form.mainPhone"), txtMainMobilePhone);
	builder.append(message("subsriber.form.alternativePhone"), txtOtherMobilePhone);
	builder.append(message("subsriber.form.currency"), supportedCurrencyCombobox);
	builder.append(message("subsriber.form.note"), txtNote);

	builder.appendSeparator();

	JButton btnSave = btnSave();

	JButton btnClose = btnClose();

	builder.append(ButtonBarFactory.buildRightAlignedBar(btnClose, btnSave), builder.getColumnCount());
	return builder.getPanel();
    }

    @Override
    protected void save() {

	if (txtName.getText().isEmpty()) {
	    MessageUtils.showWarningMessage(getOwner(), errorPerfix("village.form.name"));
	    return;
	}

	Company newCompany = company == null ? new Company() : company;
	newCompany.setName(txtName.getText());
	newCompany.setAddress(txtAddress.getText());
	newCompany.setLandLine(txtLandLine.getText());
	newCompany.setMainMobilePhone(txtMainMobilePhone.getText());
	newCompany.setNote(txtNote.getText());
	newCompany.setOtherMobilePhone(txtOtherMobilePhone.getText());
	newCompany.setSelectedCurrency((SupportedCurrency) supportedCurrencyCombobox.getSelectedItem());
	ServiceProvider.get().getSubscriberService().saveCompany(newCompany);
	ServiceProvider.get().revalidate();
	closeWindow();

    }

    @Override
    protected Optional<List<String>> validateInputs() {

	List<String> errors = new ArrayList<>();
	if (txtName.getText().isEmpty()) {
	    errors.add(errorPerfix("subsriber.form.name"));
	}
	if (txtAddress.getText().isEmpty()) {
	    errors.add(errorPerfix("subsriber.form.address"));
	}
	if (txtMainMobilePhone.getText().isEmpty()) {
	    errors.add(errorPerfix("subsriber.form.mainPhone"));
	}
	return errors.isEmpty() ? Optional.empty() : Optional.of(errors);

    }

    @Override
    protected String getLayoutSpecs() {
	return "right:pref, 4dlu, fill:100dlu:grow";
    }

    @Override
    public void refreshTable() {
	fillData();

    }

}