/**
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.vaadin.swingersclub;

import java.awt.BorderLayout;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import org.vaadin.domain.model.Person;

public class SwingApplication extends JFrame {

    Form form;
    JLabel countLabel = new JLabel();
    JButton newCustomer = new JButton("Add new");

    String[] columnNames = new String[]{"first name", "last name", "email"};
    private JTable table;

    private List<Person> customers;

    private JPAFacade customerFacade = new JPAFacade();

    public static void main(String args[]) {
        new SwingApplication().createUI();
        
    }

    void deselect() {
        table.getSelectionModel().clearSelection();
    }

    class CustomerTableModel extends AbstractTableModel {

        @Override
        public int getRowCount() {
            return customers.size();
        }

        @Override
        public int getColumnCount() {
            return 3;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (customers == null) {
                customers = customerFacade.findAll();
            }
            Person c = customers.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return c.getFirstName();
                case 1:
                    return c.getLastName();
                case 2:
                    return c.getEmail();
            }
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

    }

    private void createUI() {
        final BorderLayout borderLayout = new BorderLayout(10, 10);
        setLayout(borderLayout);

        newCustomer.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                form.clear();
            }
        });

        form = new Form(this);

        Box hbox = Box.createHorizontalBox();
        hbox.add(newCustomer);
        hbox.add(Box.createGlue());
        hbox.add(countLabel);
        add(hbox, BorderLayout.PAGE_START);

        table = new JTable();
        table.getSelectionModel().setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

                    @Override
                    public void valueChanged(ListSelectionEvent e) {
                        Person c = customers.get(e.getFirstIndex());
                        form.editCustomer(c);
                    }
                });
        add(new JScrollPane(table), BorderLayout.CENTER);

        add(form, BorderLayout.PAGE_END);

        refreshData();

        setSize(640, 400);

        setVisible(true);
    }

    protected void refreshData() {
        customers = getCustomerFacade().findAll();
        // Manual style, almost like IndexexCotainer or custom Container 
        // in vaadin, see impl.
        table.setModel(new CustomerTableModel());
        countLabel.setText("Rows in DB: " + customers.size());
    }

    public JPAFacade getCustomerFacade() {
        return customerFacade;
    }

}
