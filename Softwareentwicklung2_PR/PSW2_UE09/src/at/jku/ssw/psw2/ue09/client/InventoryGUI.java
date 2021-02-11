package at.jku.ssw.psw2.ue09.client;

import at.jku.ssw.psw2.ue09.model.InventoryException;
import at.jku.ssw.psw2.ue09.model.InventoryItem;
import at.jku.ssw.psw2.ue09.model.InventoryModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.stream.Stream;

public final class InventoryGUI {

    private final JFrame frame;
    private final JList<DisplayItem> stockListView;
    private final JTextArea itemName;
    private final JTextArea itemDescription;
    private final JTextArea itemQuantity;
    private final JButton addStockButton;
    private final JButton removeStockButton;
    private final JButton editDescriptionButton;

    private final InventoryModel inventoryModel;
    private final DefaultListModel<DisplayItem> listModel;

    private InventoryGUI(InventoryModel model) {
        inventoryModel = model;
        listModel = new DefaultListModel<>();

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

        final JPanel listPanel = new JPanel();
        listPanel.setLayout(new BorderLayout());
        splitPane.setLeftComponent(listPanel);
        stockListView = new JList<>(listModel);
        listPanel.add(stockListView, BorderLayout.CENTER);
        final JButton reloadButton = new JButton("Reload");
        listPanel.add(reloadButton, BorderLayout.SOUTH);

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

        reloadButton.addActionListener(e -> {
            try {
                updateList(model.getItems());
            } catch (InventoryException error) {
                showError(error.getMessage(), "Error during item access");
            }
        });

        addStockButton.addActionListener(e -> updateQuantity(stockListView.getSelectedValue(), (int) quantitySpinnerModel.getValue(), true));
        removeStockButton.addActionListener(e -> updateQuantity(stockListView.getSelectedValue(), (int) quantitySpinnerModel.getValue(), false));

        editDescriptionButton.addActionListener(e -> {
            final DisplayItem selectedItem = stockListView.getSelectedValue();
            if (selectedItem != null) {
                SwingUtilities.invokeLater(() -> showEditDescriptionDialog(selectedItem.getItem()));
            }
        });

        createItemButton.addActionListener(e -> SwingUtilities.invokeLater(() -> {
            final String newName = JOptionPane.showInputDialog(frame, "Please enter a unique name for the new item:", "Create Item", JOptionPane.PLAIN_MESSAGE);
            try {
                model.createItem(newName);
                updateList(model.getItems());
            } catch (InventoryException error) {
                showError(error.getMessage(), "Error while creating a new item");
            }
        }));

        deleteItemButton.addActionListener(e -> {
            final DisplayItem selectedItem = stockListView.getSelectedValue();
            if (selectedItem != null) {
                try {
                    model.deleteItem(selectedItem.getItem().getId());
                    updateList(model.getItems());
                } catch (InventoryException error) {
                    showError(error.getMessage(), "Error while deleting item");
                }
            }
        });

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                try {
                    model.close();
                } catch (InventoryException inventoryException) {
                    throw new AssertionError("Error during closing", inventoryException);
                }
            }
        });

        // initialize gui data
        try {
            updateList(model.getItems());
        } catch (InventoryException error) {
            throw new AssertionError("Error during initialization", error);
        }
    }

    public static void startGui(InventoryModel model) {
        new InventoryGUI(model).show();
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

    private void showError(String message, String title) {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(frame, message, title, JOptionPane.ERROR_MESSAGE));
    }

    private void updateQuantity(DisplayItem item, int change, boolean increase) {
        if (item != null) {
            try {
                inventoryModel.changeQuantity(item.getItem().getId(), increase ? change : -change);
                final InventoryItem newItem = inventoryModel.getItem(item.getItem().getId());
                updateItemInGui(new DisplayItem(newItem));
            } catch (InventoryException error) {
                showError(error.getMessage(), "Error while changing quantity");
            }
        }
    }

    private void updateItemInGui(DisplayItem newItem) {
        SwingUtilities.invokeLater(() -> {
            // items are compared by id
            final int index = listModel.indexOf(newItem);
            final boolean isSelected = newItem.equals(stockListView.getSelectedValue());
            listModel.setElementAt(newItem, index);
            if (isSelected) {
                setDetails(newItem.getItem());
            }
        });
    }

    private void updateList(List<InventoryItem> newItems) {
        SwingUtilities.invokeLater(() -> {
            stockListView.clearSelection();
            clearDetails();
            listModel.clear();
            newItems.stream().map(DisplayItem::new).forEach(listModel::addElement);
        });
    }

    private void updateDetailsView() {
        SwingUtilities.invokeLater(() -> {
            final DisplayItem selectedItem = stockListView.getSelectedValue();
            if (selectedItem != null) {
                setDetails(selectedItem.getItem());
            } else {
                clearDetails();
            }
        });
    }

    private void setDetails(InventoryItem item) {
        itemName.setText(item.getName());
        itemDescription.setText(item.getDescription());
        itemQuantity.setText(String.valueOf(item.getQuantity()));
        Stream.of(addStockButton, removeStockButton, editDescriptionButton).forEach(b -> b.setEnabled(true));
    }

    private void clearDetails() {
        Stream.of(itemName, itemDescription, itemQuantity).forEach(jta -> jta.setText(""));
        Stream.of(addStockButton, removeStockButton, editDescriptionButton).forEach(b -> b.setEnabled(false));
    }

    private void showEditDescriptionDialog(InventoryItem editedItem) {
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

        saveButton.addActionListener(e -> setDescription(editedItem, descriptionPane.getText()));
        reloadButton.addActionListener(e -> {
            try {
                final InventoryItem currentItem = inventoryModel.getItem(editedItem.getId());
                updateItemInGui(new DisplayItem(currentItem));
                SwingUtilities.invokeLater(() -> descriptionPane.setText(currentItem.getDescription()));
            } catch (InventoryException error) {
                showError(error.getMessage(), "Error while loading description");
            }
        });
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setDescription(editedItem, descriptionPane.getText());
            }
        });

        descriptionPane.setText(editedItem.getDescription());

        frame.setVisible(true);
        descriptionPane.requestFocus();
    }

    private void setDescription(InventoryItem editedItem, String newDescription) {
        try {
            inventoryModel.setDescription(editedItem.getId(), newDescription);
            final InventoryItem newItem = inventoryModel.getItem(editedItem.getId());
            updateItemInGui(new DisplayItem(newItem));
        } catch (InventoryException error) {
            showError(error.getMessage(), "Error while editing description");
        }
    }

    void show() {
        frame.setVisible(true);
    }

    private static final class DisplayItem {

        private final InventoryItem item;

        private DisplayItem(InventoryItem item) {
            assert item != null;
            this.item = item;
        }

        public InventoryItem getItem() {
            return item;
        }

        /**
         * This method is invoked to generate the string displayed in the list view.
         *
         * @return the description of the item
         */
        @Override
        public String toString() {
            return item.getName();
        }

        @Override
        public int hashCode() {
            return item.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof DisplayItem && item.getId() == ((DisplayItem) obj).getItem().getId();
        }
    }
}
