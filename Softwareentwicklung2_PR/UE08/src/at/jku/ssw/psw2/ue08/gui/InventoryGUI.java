package at.jku.ssw.psw2.ue08.gui;

import at.jku.ssw.psw2.ue08.model.InventoryChangeListener;
import at.jku.ssw.psw2.ue08.model.InventoryItem;
import at.jku.ssw.psw2.ue08.model.InventoryModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public final class InventoryGUI<ItemClass extends InventoryItem> {

    private static final class DisplayItem<ItemClass extends InventoryItem> {

        private final ItemClass item;

        private DisplayItem(ItemClass item) {
            assert item != null;
            this.item = item;
        }

        public ItemClass getItem() {
            return item;
        }

        /**
         * This method is invoked to generate the string displayed in the list view.
         *
         * @return the description of the item
         */
        @Override
        public String toString() {
            try {
                return item.getName();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public int hashCode() {
            return item.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof DisplayItem && item.equals(((DisplayItem) obj).getItem());
        }
    }

    private final JFrame frame;
    private final JList<DisplayItem<ItemClass>> stockListView;
    private final JTextArea itemName;
    private final JTextArea itemDescription;
    private final JTextArea itemQuantity;
    private final JButton addStockButton;
    private final JButton removeStockButton;
    private final JButton editDescriptionButton;
    private InventoryGUI(InventoryModel<ItemClass> model) {
        frame = new JFrame();
        frame.setTitle("Inventory Management");
        frame.setSize(800, 600);
        frame.setResizable(true);
        frame.setMinimumSize(new Dimension(800, 600));
        frame.setLocation(50, 50);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final JPanel mainPanel = new JPanel();
        frame.getContentPane().add(mainPanel);
        mainPanel.setLayout(new BorderLayout());
        final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(250);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        final DefaultListModel<DisplayItem<ItemClass>> stockListModel = new DefaultListModel<>();
        stockListView = new JList<>(stockListModel);
        splitPane.setLeftComponent(stockListView);

        final JPanel detailPanel = new JPanel();
        detailPanel.setLayout(new BorderLayout());
        splitPane.setRightComponent(detailPanel);

        final JPanel detailNamePanel = new JPanel();
        detailNamePanel.setLayout(new BoxLayout(detailNamePanel, BoxLayout.Y_AXIS));
        detailNamePanel.add(createDetailLabel("Item Name:"));
        detailNamePanel.add(wrapTextArea(itemName = createTextField()));
        detailPanel.add(detailNamePanel, BorderLayout.NORTH);

        final JPanel detailDescriptionPanel = new JPanel();
        detailDescriptionPanel.setLayout(new BoxLayout(detailDescriptionPanel, BoxLayout.Y_AXIS));
        detailDescriptionPanel.add(new JSeparator(JSeparator.HORIZONTAL));
        detailDescriptionPanel.add(createDetailLabel("Item Description:"));
        detailDescriptionPanel.add(wrapTextArea(itemDescription = createTextField()));
        detailDescriptionPanel.add(new JSeparator(JSeparator.HORIZONTAL));
        detailDescriptionPanel.add(createDetailLabel("Item Quantity:"));
        detailDescriptionPanel.add(wrapTextArea(itemQuantity = createTextField()));
        detailPanel.add(detailDescriptionPanel, BorderLayout.CENTER);

        final JPanel editPanel = new JPanel();
        editPanel.setLayout(new BoxLayout(editPanel, BoxLayout.Y_AXIS));
        detailPanel.add(editPanel, BorderLayout.SOUTH);

        final JPanel changeItemPanel = new JPanel();
        final SpinnerModel quantitySpinnerModel = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);
        changeItemPanel.add(new JSpinner(quantitySpinnerModel));
        changeItemPanel.setLayout(new BoxLayout(changeItemPanel, BoxLayout.X_AXIS));
        changeItemPanel.add(addStockButton = createQuantityButton("Increase Qty."));
        changeItemPanel.add(removeStockButton = createQuantityButton("Decrease Qty."));
        editPanel.add(changeItemPanel);
        changeItemPanel.add(editDescriptionButton = new JButton("Edit Desc."));

        final JPanel editListPanel = new JPanel();
        editListPanel.setLayout(new BoxLayout(editListPanel, BoxLayout.X_AXIS));
        JButton createItemButton;
        editListPanel.add(createItemButton = new JButton("Create Item"));
        JButton deleteItemButton;
        editListPanel.add(deleteItemButton = new JButton("Delete Item"));
        editPanel.add(editListPanel);

        stockListView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        stockListView.addListSelectionListener(listSelectionEvent -> updateDetailsView());

        addStockButton.addActionListener(e -> {
            final DisplayItem<ItemClass> selectedItem = stockListView.getSelectedValue();
            if (selectedItem != null) {
                try {
                    model.increaseQuantity(selectedItem.getItem().getName(), (int) quantitySpinnerModel.getValue());
                } catch (IllegalArgumentException | RemoteException error) {
                    JOptionPane.showMessageDialog(frame, error.getMessage(), "Error while changing quantity", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        removeStockButton.addActionListener(e -> {
            final DisplayItem<ItemClass> selectedItem = stockListView.getSelectedValue();
            if (selectedItem != null) {
                try {
                    model.decreaseQuantity(selectedItem.getItem().getName(), (int) quantitySpinnerModel.getValue());
                } catch (IllegalArgumentException | RemoteException error) {
                    JOptionPane.showMessageDialog(frame, error.getMessage(), "Error while changing quantity", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        editDescriptionButton.addActionListener(e -> {
            final DisplayItem<ItemClass> selectedItem = stockListView.getSelectedValue();
            if (selectedItem != null) {
                SwingUtilities.invokeLater(() -> showEditDescriptionDialog(model, selectedItem.getItem()));
            }
        });

        createItemButton.addActionListener(e -> {
            final String newName = JOptionPane.showInputDialog(frame, "Please enter a unique name for the new item:", "Create Item", JOptionPane.PLAIN_MESSAGE);
            if (newName != null) {
                try {
                    model.createItem(newName);
                } catch (IllegalArgumentException | RemoteException error) {
                    JOptionPane.showMessageDialog(frame, error.getMessage(), "Error while creating a new item", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        deleteItemButton.addActionListener(e -> {
            final DisplayItem<ItemClass> selectedItem = stockListView.getSelectedValue();
            if (selectedItem == null) {
                return;
            }
            try {
                model.deleteItem(selectedItem.getItem().getName());
            } catch (IllegalArgumentException | RemoteException error) {
                JOptionPane.showMessageDialog(frame, error.getMessage(), "Error while deleting item", JOptionPane.ERROR_MESSAGE);
            }
        });

        try {
            InventoryChangeListener<ItemClass> listener = new InventoryListener(stockListModel);
            model.addListener(listener);
            model.getItems().forEach(element -> stockListModel.addElement(new DisplayItem<>(element)));
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    try {
                        model.removeListener(listener);
                    } catch (IllegalArgumentException | RemoteException error) {
                        // the application will now terminate anyways
                        throw new AssertionError("Error during closing", error);
                    }
                }
            });
        } catch (IllegalArgumentException | RemoteException error) {
            throw new AssertionError("Error during initialization", error);
        }

        // initialize the details view
        updateDetailsView();
    }

    public static <ItemClass extends InventoryItem> void startGui(InventoryModel<ItemClass> model) {
        new InventoryGUI<>(model).show();
    }

    private static JButton createQuantityButton(String label) {
        return new JButton(label);
    }

    private static JLabel createDetailLabel(String labelText) {
        return new JLabel(labelText, JLabel.CENTER);
    }

    private static JScrollPane wrapTextArea(JTextArea textArea) {
        final JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        return scrollPane;
    }

    private static JTextArea createTextField() {
        final JTextArea textPane = new JTextArea();
        textPane.setWrapStyleWord(true);
        textPane.setEditable(true);
        textPane.setEditable(false);
        return textPane;
    }

    private void updateDetailsView() {
        SwingUtilities.invokeLater(() -> {
            final DisplayItem<ItemClass> selectedItem = stockListView.getSelectedValue();
            if (selectedItem != null) {
                setDetails(selectedItem.getItem());
            } else {
                clearDetails();
            }

            addStockButton.setEnabled(selectedItem != null);
            removeStockButton.setEnabled(selectedItem != null);
            editDescriptionButton.setEnabled(selectedItem != null);
        });
    }

    private void setDetails(InventoryItem item) {
        try {
            itemName.setText(item.getName());
            itemDescription.setText(item.getDescription());
            itemQuantity.setText(String.valueOf(item.getQuantity()));
        } catch (IllegalArgumentException | RemoteException error) {
            clearDetails();
            JOptionPane.showMessageDialog(frame, error.getMessage(), "Error while retrieving item details", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearDetails() {
        itemName.setText("");
        itemDescription.setText("");
        itemQuantity.setText("");
    }

    private void showEditDescriptionDialog(InventoryModel<ItemClass> model, ItemClass editedItem) {
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setTitle("Edit Description");
        frame.setMinimumSize(new Dimension(400, 400));
        frame.setResizable(true);

        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        frame.getContentPane().add(mainPanel);

        final JPanel descriptionPanel = new JPanel();
        descriptionPanel.setLayout(new BoxLayout(descriptionPanel, BoxLayout.X_AXIS));
        final JTextArea descriptionPane = new JTextArea(50, 50);
        descriptionPane.setWrapStyleWord(true);
        descriptionPane.setEnabled(true);
        descriptionPane.setEditable(true);
        JScrollPane scrollPane = new JScrollPane(descriptionPane);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        descriptionPanel.add(scrollPane);
        mainPanel.add(descriptionPanel, BorderLayout.CENTER);

        final JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout());
        final JButton saveButton = new JButton("Save");
        final JButton reloadButton = new JButton("Reload");
        buttonsPanel.add(saveButton);
        buttonsPanel.add(reloadButton);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        saveButton.addActionListener(e -> editDescription(model, editedItem, descriptionPane.getText()));
        reloadButton.addActionListener(e -> SwingUtilities.invokeLater(() -> {
            try {
                descriptionPane.setText(editedItem.getDescription());
            } catch (IllegalArgumentException | RemoteException error) {
                descriptionPane.setText("");
                JOptionPane.showMessageDialog(frame, error.getMessage(), "Error while loading description", JOptionPane.ERROR_MESSAGE);
            }
        }));
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                editDescription(model, editedItem, descriptionPane.getText());
            }
        });

        try {
            descriptionPane.setText(editedItem.getDescription());
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        frame.setVisible(true);
        descriptionPane.requestFocus();
    }

    private void editDescription(InventoryModel<ItemClass> model, ItemClass editedItem, String newDescription) {
        SwingUtilities.invokeLater(() -> {
            try {
                model.setDescription(editedItem.getName(), newDescription);
            } catch (IllegalArgumentException | RemoteException | NullPointerException error) {
                JOptionPane.showMessageDialog(frame, error.getMessage(), "Error while editing description", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    void show() {
        frame.setVisible(true);
    }

    private class InventoryListener extends UnicastRemoteObject implements InventoryChangeListener<ItemClass> {
        private final DefaultListModel<DisplayItem<ItemClass>> stockListModel;

        protected InventoryListener(DefaultListModel<DisplayItem<ItemClass>> stockListModel) throws RemoteException {
            this.stockListModel = stockListModel;
        }

        @Override
        public void onItemAdded(ItemClass addedItem) {
            stockListModel.addElement(new DisplayItem<>(addedItem));
        }

        @Override
        public void onItemChanged(String itemName) {
            updateDetailsView();
        }

        @Override
        public void onItemRemoved(ItemClass removedItem) {
            stockListModel.removeElement(new DisplayItem<>(removedItem));
        }
    };
}
