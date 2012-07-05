package com.zrd.zr.gwt.tradeblotter.client;


import com.zrd.zr.gwt.tradeblotter.client.TradeBlotterService;
import com.zrd.zr.gwt.tradeblotter.client.TradeBlotterServiceAsync;
import com.zrd.zr.gwt.tradeblotter.shared.MatrixStruc;

import java.util.LinkedHashMap;
import java.util.Map;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TradeBlotter implements EntryPoint {
	/**
	 * Create a instance of TriTradeService to talk to
	 */
	private final TradeBlotterServiceAsync mTradeBlotterService = GWT.create(
		TradeBlotterService.class
	);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		/*
		 * The Tri-trade GUI codes actually below!
		 */
		/*
		 * the components of the GUI
		 */
		final TextBox usernameField = new TextBox();
		usernameField.addStyleName("fullWidthField");
		RootPanel.get("usernameFieldContainer").add(usernameField);
		final TextBox passwordField = new PasswordTextBox();
		passwordField.addStyleName("fullWidthField");
		RootPanel.get("passwordFieldContainer").add(passwordField);
		final Button loginButton = new Button("Login");
		loginButton.addStyleName("linkStyleButton");
		RootPanel.get("loginButtonContainer").add(loginButton);
		final HTML loginErrorLabel = new HTML();
		RootPanel.get("loginErrorLabelContainer").add(loginErrorLabel);

		final Button logoutButton = new Button("Logout");
		logoutButton.addStyleName("linkStyleButton");
		RootPanel.get("logoutButtonContainer").add(logoutButton);
		final Button connDisconnButton = new Button("Connect");
		connDisconnButton.addStyleName("linkStyleButton");
		RootPanel.get("connDisconnButtonContainer").add(connDisconnButton);
		
		final Anchor totalAnchor = new Anchor("--");
		RootPanel.get("totalAnchorContainer").add(totalAnchor);
		final Anchor successfulAnchor = new Anchor("--");
		RootPanel.get("successfulAnchorContainer").add(successfulAnchor);
		final Anchor fnsAnchor = new Anchor("--");
		RootPanel.get("fnsAnchorContainer").add(fnsAnchor);
		final Anchor pnlAnchor = new Anchor("--");
		RootPanel.get("pnlAnchorContainer").add(pnlAnchor);
		
		final Button readIniButton = new Button("Read Ini");
		readIniButton.addStyleName("fullWidthButton");
		RootPanel.get("readIniButtonContainer").add(readIniButton);
		final Button runPauseButton = new Button("Run/Pause");
		runPauseButton.addStyleName("fullWidthButton");
		RootPanel.get("runPauseButtonContainer").add(runPauseButton);
		final Button inputOrderButton = new Button("Input Order");
		inputOrderButton.addStyleName("fullWidthButton");
		RootPanel.get("inputOrderButtonContainer").add(inputOrderButton);
		final Button editOrderButton = new Button("Edit Order");
		editOrderButton.addStyleName("fullWidthButton");
		RootPanel.get("editOrderButtonContainer").add(editOrderButton);
		final Button traderOrderBookButton = new Button("Trader OrderBook");
		traderOrderBookButton.addStyleName("fullWidthButton");
		RootPanel.get("traderOrderBookButtonContainer").add(traderOrderBookButton);
		final Button orderHistoryButton = new Button("Order History");
		orderHistoryButton.addStyleName("fullWidthButton");
		RootPanel.get("orderHistoryButtonContainer").add(orderHistoryButton);
		final Button tradeStatisticsButton = new Button("Trade Statistics");
		tradeStatisticsButton.addStyleName("fullWidthButton");
		RootPanel.get("tradeStatisticsButtonContainer").add(tradeStatisticsButton);
		final Button accountSummaryButton = new Button("Account Summary");
		accountSummaryButton.addStyleName("fullWidthButton");
		RootPanel.get("accountSummaryButtonContainer").add(accountSummaryButton);
		
		final TextBox stateField = new TextBox();
		stateField.addStyleName("fullWidthField");
		stateField.setReadOnly(true);
		RootPanel.get("stateFieldContainer").add(stateField);
		final TextBox loginField = new TextBox();
		loginField.addStyleName("fullWidthField");
		loginField.setReadOnly(true);
		RootPanel.get("loginFieldContainer").add(loginField);
		final TextBox lastUpdateField = new TextBox();
		lastUpdateField.addStyleName("fullWidthField");
		lastUpdateField.setReadOnly(true);
		RootPanel.get("lastUpdateFieldContainer").add(lastUpdateField);
		final CheckBox resendDataCheck = new CheckBox("Resend Data");
		RootPanel.get("resendDataCheckContainer").add(resendDataCheck);
		final TextBox availableCashField = new TextBox();
		availableCashField.addStyleName("fullWidthField");
		availableCashField.setReadOnly(true);
		RootPanel.get("availableCashFieldContainer").add(availableCashField);
		final TextBox runStateField = new TextBox();
		runStateField.addStyleName("fullWidthField");
		runStateField.setReadOnly(true);
		RootPanel.get("runStateFieldContainer").add(runStateField);
		final TextBox dataCollectorField = new TextBox();
		dataCollectorField.addStyleName("fullWidthField");
		dataCollectorField.setReadOnly(true);
		RootPanel.get("dataCollectorFieldContainer").add(dataCollectorField);
		
		final HTML statusHTML = new HTML();
		RootPanel.get("statusHTMLContainer").add(statusHTML);
		
		/*
		 * the dialogs that required to show the status when 
		 * the client trying to deal with the server
		 */
		final DialogBox msgDlg = new DialogBox();
		msgDlg.setText("Message Box");
		msgDlg.setAnimationEnabled(true);
		VerticalPanel msgPanel = new VerticalPanel();
		msgPanel.addStyleName("dialogVPanel");
		final Button msgOKButton = new Button("OK");
		msgPanel.add(new HTML("<font color='green' size='3' style='weight:bold'>Sending command:</font><br/>"));
		final HTML msgCmdHTML = new HTML();
		msgPanel.add(msgCmdHTML);
		msgPanel.add(new HTML("<font color='blue' size='3' style='weight:bold'>The response:</font><br/>"));
		final HTML msgInfoHTML = new HTML();
		msgPanel.add(msgInfoHTML);
		msgPanel.add(new HTML("<br/><br/>"));
		msgPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		msgPanel.add(msgOKButton);
		msgDlg.setWidget(msgPanel);
		msgOKButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				logoutButton.setEnabled(true);
				msgDlg.hide();
				
				RootPanel.get("loginPanel").setVisible(true);
				RootPanel.get("mainPanelAccess").setVisible(false);
				RootPanel.get("mainPanelCmds").setVisible(false);
				/*
				 * then we cancel the sending command
				 */
			}
			
		});
		
		/**
		 * the "Account Summary" dialog
		 */
		final DialogBox accountSummaryDlg = new DialogBox();
		accountSummaryDlg.setText("Account Summary");
		accountSummaryDlg.setAnimationEnabled(true);
		VerticalPanel accountSummaryPanel = new VerticalPanel();
		accountSummaryPanel.addStyleName("dialogVPanel");
		final FlexTable accountSummaryTable = new FlexTable();
		Map<String, Float> asData = new LinkedHashMap<String, Float>(6);
		asData.put("Account Balance", (float)0.00);
		asData.put("Deposit/Withdrawal", (float)0.00);
		asData.put("Margin Requirement", (float)0.00);
		asData.put("Profit/Loss", (float)0.00);
		asData.put("Commission/Levy", (float)0.00);
		asData.put("Current Balance", (float)0.00);
		fillAccountSummaryTable(accountSummaryTable, asData);
		accountSummaryTable.getColumnFormatter().setWidth(0, "200px");
		accountSummaryTable.getColumnFormatter().setWidth(1, "120px");
		accountSummaryPanel.add(accountSummaryTable);
		accountSummaryPanel.add(new HTML("<br/>"));
		final Button accountSummaryCloseButton = new Button("Close");
		accountSummaryPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		accountSummaryPanel.add(accountSummaryCloseButton);
		accountSummaryDlg.setWidget(accountSummaryPanel);
		accountSummaryCloseButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				accountSummaryDlg.hide();
				
			}
			
		});
		
		/**
		 * the "Input Order" dialog
		 */
		final DialogBox inputOrderDlg = new DialogBox();
		inputOrderDlg.setText("Input Order");
		inputOrderDlg.setAnimationEnabled(true);
		VerticalPanel inputOrderPanel = new VerticalPanel();
		inputOrderPanel.addStyleName("dialogVPanel");
		
		final FlexTable inputOrderTable = new FlexTable();
		inputOrderTable.setText(0, 0, "Procuct ID");
		inputOrderTable.setWidget(0, 1, new ListBox());
		((ListBox)inputOrderTable.getWidget(0, 1)).addItem("TXFG2", "000001");
		((ListBox)inputOrderTable.getWidget(0, 1)).addItem("000002", "000002");
		((ListBox)inputOrderTable.getWidget(0, 1)).addItem("000003", "000003");
		inputOrderTable.setText(1, 0, "Price");
		inputOrderTable.setWidget(1, 1, new TextBox());
		inputOrderTable.getWidget(1, 1).setWidth("60pt");
		inputOrderTable.setWidget(1, 2, new ListBox());
		((ListBox)inputOrderTable.getWidget(1, 2)).addItem("Fixed", "000001");
		((ListBox)inputOrderTable.getWidget(1, 2)).addItem("000002", "000002");
		((ListBox)inputOrderTable.getWidget(1, 2)).addItem("000003", "000003");
		inputOrderTable.setText(2, 0, "Quantity");
		inputOrderTable.setWidget(2, 1, new TextBox());
		inputOrderTable.getWidget(2, 1).setWidth("60pt");
		inputOrderTable.setWidget(3, 0, new RadioButton("inputOrder", "FOK"));
		((RadioButton)inputOrderTable.getWidget(3, 0)).setValue(true);
		inputOrderTable.setWidget(3, 1, new RadioButton("inputOrder", "IOC"));
		inputOrderTable.setWidget(3, 2, new RadioButton("inputOrder", "ROD"));
		inputOrderTable.setWidget(4, 0, new Button("Buy"));
		inputOrderTable.getWidget(4, 0).setWidth("100%");
		inputOrderTable.setWidget(4, 2, new Button("Sell"));
		inputOrderTable.getWidget(4, 2).setWidth("100%");
		inputOrderTable.setWidget(5, 0, new Button("Buy @ A1"));
		inputOrderTable.getWidget(5, 0).setWidth("100%");
		inputOrderTable.setWidget(5, 2, new Button("Sell @ A1"));
		inputOrderTable.getWidget(5, 2).setWidth("100%");
		inputOrderTable.setWidget(6, 0, new Button("Delete All Outstanding Orders"));
		inputOrderTable.getWidget(6, 0).setWidth("100%");
		inputOrderTable.getFlexCellFormatter().setColSpan(6, 0, 3);
		inputOrderPanel.add(inputOrderTable);
		
		inputOrderPanel.add(new HTML("<br/>"));
		final Button inputOrderCloseButton = new Button("Close");
		inputOrderPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		inputOrderPanel.add(inputOrderCloseButton);
		inputOrderDlg.setWidget(inputOrderPanel);
		inputOrderCloseButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				inputOrderDlg.hide();
				
			}
			
		});

		/*
		 * handle the events for the components in GUI
		 */
		loginButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				/*
				 * disable the button
				 */
				loginButton.setEnabled(false);
				
				/*
				 * validate the texts for the pair of username/password
				 */
				
				/*
				 * send the username/password to the server 
				 */
				loginErrorLabel.setHTML("");
				mTradeBlotterService.loginServer(
					usernameField.getText(),
					passwordField.getText(),
					new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							loginErrorLabel.setHTML(
								"<font color='red'>"
									+ caught.toString()
									+ "</font>"
							);
							loginButton.setEnabled(true);
						}

						@Override
						public void onSuccess(String result) {
							// TODO Auto-generated method stub
							if (result.equals("logged in")) {
								RootPanel.get("loginPanel").setVisible(false);
								RootPanel.get("mainPanelAccess").setVisible(true);
								loginErrorLabel.setHTML("");
								connDisconnButton.click();
							} else {
								loginErrorLabel.setHTML(
									"<font color='red'>"
										+ result
										+ "</font>"
								);
							}
							loginButton.setEnabled(true);
						}
						
					}
				);
			}
			
		});
		
		connDisconnButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if (connDisconnButton.isEnabled()) {
					if (connDisconnButton.getText().equals("Connect")) {
						connDisconnButton.setText("Connecting...");
						connDisconnButton.setEnabled(false);
						RootPanel.get("mainPanelCmds").setVisible(false);
						mTradeBlotterService.matrixServer(
							"connect",
							new AsyncCallback<MatrixStruc>() {

								@Override
								public void onFailure(Throwable caught) {
									// TODO Auto-generated method stub
									connDisconnButton.setText("Connect");
									connDisconnButton.setEnabled(true);
									statusHTML.setHTML(caught.toString());
									RootPanel.get("mainPanelCmds").setVisible(false);
								}

								@Override
								public void onSuccess(MatrixStruc result) {
									// TODO Auto-generated method stub
									connDisconnButton.setText("Disconnect");
									connDisconnButton.setEnabled(true);
									statusHTML.setHTML("<font color='green'>connected!</font>");
									RootPanel.get("mainPanelCmds").setVisible(true);
								}
								
							}
						);
					} else if (connDisconnButton.getText().equals("Disconnect")) {
						connDisconnButton.setText("Disconnecting...");
						connDisconnButton.setEnabled(false);
						RootPanel.get("mainPanelCmds").setVisible(true);
						mTradeBlotterService.matrixServer(
							"disconnect",
							new AsyncCallback<MatrixStruc>() {

								@Override
								public void onFailure(Throwable caught) {
									// TODO Auto-generated method stub
									connDisconnButton.setText("Disconnect");
									connDisconnButton.setEnabled(true);
									statusHTML.setHTML(caught.toString());
									RootPanel.get("mainPanelCmds").setVisible(true);
								}

								@Override
								public void onSuccess(MatrixStruc result) {
									// TODO Auto-generated method stub
									connDisconnButton.setText("Connect");
									connDisconnButton.setEnabled(true);
									statusHTML.setHTML("<font color='red'>disconnected!</font>");
									RootPanel.get("mainPanelCmds").setVisible(false);
								}
								
							}
						);
					}
				} else {
					
				}
			}

		});
		
		logoutButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				/*
				 * get the text from the runField
				 */
				logoutButton.setEnabled(false);
				msgCmdHTML.setHTML("logout<br/><br/>");
				msgInfoHTML.setHTML("");
				mTradeBlotterService.tradeBlotterServer(
					"logout",
					new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							msgInfoHTML.setHTML(
								"<font color='red'>"
								+ caught.toString()
								+ "</font>"
							);
							msgDlg.center();
							msgOKButton.setFocus(true);
						}

						@Override
						public void onSuccess(String result) {
							// TODO Auto-generated method stub
							msgInfoHTML.setHTML(result);
							msgDlg.center();
							msgOKButton.setFocus(true);
							connDisconnButton.setText("Connect");
							connDisconnButton.setEnabled(true);
						}
						
					}
				);
			}
			
		});
		
		runPauseButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				runPauseButton.setEnabled(false);
				statusHTML.setHTML("Processing ping...");
				mTradeBlotterService.matrixServer(
					"ping",
					new AsyncCallback<MatrixStruc>() {

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							runPauseButton.setEnabled(true);
							statusHTML.setHTML("<b>Failed~~~</b><br/>" + caught.toString());
						}

						@Override
						public void onSuccess(MatrixStruc result) {
							// TODO Auto-generated method stub
							runPauseButton.setEnabled(true);
							statusHTML.setHTML("<b>Succeed~~~</b><br/>" + result.message);
						}
						
					}
				);
			}
			
		});
		
		inputOrderButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				inputOrderButton.setEnabled(false);
				statusHTML.setHTML("Processing input...");
				mTradeBlotterService.matrixServer(
					"inputOrder",
					new AsyncCallback<MatrixStruc>() {

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							inputOrderButton.setEnabled(true);
							statusHTML.setHTML("<b>Failed~~~</b><br/>" + caught.toString());
						}

						@Override
						public void onSuccess(MatrixStruc result) {
							// TODO Auto-generated method stub
							inputOrderButton.setEnabled(true);
							statusHTML.setHTML("<b>Succeed~~~</b><br/>");
							inputOrderDlg.center();
							inputOrderCloseButton.setFocus(true);
						}
						
					}
				);
			}
			
		});
		
		editOrderButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				editOrderButton.setEnabled(false);
				statusHTML.setHTML("Processing edit...");
				mTradeBlotterService.testServer(
					100, "name_test", "blurb_test",
					new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							editOrderButton.setEnabled(true);
							statusHTML.setHTML("<b>Failed~~~</b><br/>" + caught.toString());
						}

						@Override
						public void onSuccess(String result) {
							// TODO Auto-generated method stub
							editOrderButton.setEnabled(true);
							statusHTML.setHTML("<b>Succeed~~~</b><br/>" + result);
						}
						
					}
				);
			}
			
		});

		accountSummaryButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				mTradeBlotterService.matrixServer(
					"accountSummary",
					new AsyncCallback<MatrixStruc>() {

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							statusHTML.setHTML(
								"<font color='red'>"
								+ caught.toString()
								+ "</font>"
							);
						}

						@Override
						public void onSuccess(MatrixStruc result) {
							// TODO Auto-generated method stub
							fillAccountSummaryTable(accountSummaryTable, result.accountSummaryData);
						}
						
					}
				);
				
				accountSummaryDlg.center();
				accountSummaryCloseButton.setFocus(true);
			}
			
		});
	}

	private void fillAccountSummaryTable(FlexTable table, Map<String, Float> data) {
		// TODO Auto-generated method stub
		Object[] keys = (Object[]) data.keySet().toArray();
		int i = 0;
		for (Object key : keys) {
			table.setWidget(i, 0, new Button((String) key));
			table.getWidget(i, 0).setWidth("100%");
			table.setText(i, 1, data.get(key).toString());
			i++;
		}
	}
}
