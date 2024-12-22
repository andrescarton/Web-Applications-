//Name: Andre Kim Scarton and Student Number:3150778

package application;

//Standard JavaFX imports
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import javafx.scene.Scene;
//File Reading
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
//Concurrency
import javafx.application.Platform;
import javafx.concurrent.Task;
//Geometry
import java.util.HashMap;
import java.util.Map;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
//Controls
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import java.util.ArrayList;
import java.util.List;

//Image for dialog
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
//Layouts
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PizzaVendingFX extends Application {
	// Declare components that require class scope
	Label lblOrderType, lblCustomerName, lblAddress, lblPizza, lblTotal, lblTotalPizza, lblPrice, lblBase, lblSize,
			lblSauce, lblToppings, lblStatus, lblYourOrder;
	ListView<String> lvPizzaMenu;
	ComboBox<String> bOrderType;
	Spinner<Integer> sTotalPizza;
	SpinnerValueFactory<Integer> valueFactory;
	Button btnAdd, btnRemove, btnPay, btnClear;
	TextField txtCustomerName, txtAddress, txtTotalPizzas;
	ToggleGroup tgSize, tgBase, tgSauce;
	CheckBox cbProsciutto, cbSoppressata, cbChicken, cbSausage, cbPepperoni, cbHam, cbMeatball;
	RadioButton rbRegular, rbLarge, rbPizzaBase, rbCalzone, rbGlutenFree, rbOriginalsauce, rbTomatosauce, rbBasilPesto,
			rbTeriyaki, rbTruffleCream, rbNoSauce, rbPizza, rbTeriyakiSauce;
	ProgressBar progBar;
	TextArea taOrderDetails;

	// One-off task
	Task<Void> task;

	// Keep track of order total
	double total = 0;

	// Image for dialog
	Image img, Logo;
	ImageView imv, imvLogo;

	// Map to hold pizza names and image paths
	private Map<String, String> pizzaImages;
	
	// Flag to check if customer details have been added
	private boolean customerDetailsAdded = false;

	// ObservableList to hold pizza names
	ObservableList<String> pizzaNames = FXCollections.observableArrayList();

	List<String> addedPizzas = new ArrayList<>();

	private List<PizzaOrder> pizzaOrders = new ArrayList<>();
	
	// Inner class to represent a pizza order
	class PizzaOrder {
		String type;
		String name;
		String address;
		String pizza;
		String size;
		String base;
		String sauce;
		String toppings;
		int quantity;
		double price;

		public PizzaOrder(String type, String name, String address, String pizza, String size, String base,
				String sauce, String toppings, int quantity, double price) {
			this.type = type;
			this.name = name;
			this.pizza = pizza;
			this.address = address;
			this.size = size;
			this.base = base;
			this.sauce = sauce;
			this.toppings = toppings;
			this.quantity = quantity;
			this.price = price;

		}

		/// Getters to access order details
		public String getType() {
			return type;
		}

		public String getName() {
			return name;
		}

		public String getAddress() {
			return address;
		}

		public String getSize() {
			return size;
		}

		public String getBase() {
			return base;
		}

		public String getSauce() {
			return sauce;
		}

		public String getToppings() {
			return toppings;
		}

		public String getPizza() {
			return pizza;
		}

		public int getQuantity() {
			return quantity;
		}

		public double getPrice() {
			return price;
		}
	}

	double totalOrderPrice = 0.0;

	String studentName = "Andre Kim Scarton";
	String studentNumber = "3150778";

	public PizzaVendingFX() {
		// Instantiate components as per specification
		lblOrderType = new Label("Select Order Type:");
		lblCustomerName = new Label("Customer Name:");
		lblAddress = new Label("Address:");
		lblPizza = new Label("Pizza:");
		lblTotal = new Label("Total: €0.00");
		lblTotalPizza = new Label("Quantity of Pizza:");
		lblBase = new Label("Base:");
		lblSize = new Label("Size:");
		lblSauce = new Label("Sauce:");
		lblToppings = new Label("Extra Topping:");
		lblStatus = new Label("");
		lblYourOrder = new Label("Orders");

		lvPizzaMenu = new ListView<>(pizzaNames);

		bOrderType = new ComboBox<>();
		bOrderType.getItems().addAll("Here", "ToGo", "Delivery");

		bOrderType.setValue("Here");

		sTotalPizza = new Spinner<>();

		valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1);

		sTotalPizza.setValueFactory(valueFactory);

		btnAdd = new Button("Add to order");
		btnRemove = new Button("Remove from order");
		btnPay = new Button("Pay");
		btnClear = new Button("Clear All");

		txtCustomerName = new TextField();
		txtCustomerName.setPromptText("Customer Name");

		txtAddress = new TextField();
		txtAddress.setPromptText("Address");

		progBar = new ProgressBar();
		progBar.setVisible(false);

		// Radio buttons and toggle groups
		tgSize = new ToggleGroup();
		rbRegular = new RadioButton("Regular");
		rbLarge = new RadioButton("Large");
		rbRegular.setToggleGroup(tgSize);
		rbLarge.setToggleGroup(tgSize);

		tgBase = new ToggleGroup();
		rbPizza = new RadioButton("Pizza");
		rbCalzone = new RadioButton("Calzone (Folded Pizza)");
		rbGlutenFree = new RadioButton("Gluten-Free Pizza");
		rbPizza.setToggleGroup(tgBase);
		rbCalzone.setToggleGroup(tgBase);
		rbGlutenFree.setToggleGroup(tgBase);

		tgSauce = new ToggleGroup();
		rbOriginalsauce = new RadioButton("Original Sauce");
		rbTomatosauce = new RadioButton("Tomato Sauce");
		rbBasilPesto = new RadioButton("Basil pesto");
		rbTeriyakiSauce = new RadioButton("Teriyaki sauce");
		rbTruffleCream = new RadioButton("Truffle Cream");
		rbNoSauce = new RadioButton("No Sauce");
		rbOriginalsauce.setToggleGroup(tgSauce);
		rbTomatosauce.setToggleGroup(tgSauce);
		rbBasilPesto.setToggleGroup(tgSauce);
		rbTeriyakiSauce.setToggleGroup(tgSauce);
		rbTruffleCream.setToggleGroup(tgSauce);
		rbNoSauce.setToggleGroup(tgSauce);

		rbRegular.setSelected(true);
		rbPizza.setSelected(true);
		rbOriginalsauce.setSelected(true);

		// Initialize CheckBox components for toppings
		cbProsciutto = new CheckBox("Prosciutto");
		cbSoppressata = new CheckBox("Soppressata");
		cbChicken = new CheckBox("Roasted Chicken");
		cbSausage = new CheckBox("Roasted Sausage");
		cbPepperoni = new CheckBox("Pepperoni");
		cbHam = new CheckBox("Ham");
		cbMeatball = new CheckBox("Meatball");

		// Initialize map of pizza images
		pizzaImages = new HashMap<>();

		taOrderDetails = new TextArea();
		taOrderDetails.setEditable(false);
		taOrderDetails.setWrapText(true);
		
		// Load logo image
		try {
			imvLogo = new ImageView(new Image("Assets/Image/Logo2.png"));
			imvLogo.setFitHeight(100);
			imvLogo.setFitWidth(800);
		} catch (Exception e) {
			System.err.println("Error loading Logo2.png: " + e.getMessage());
			imvLogo = new ImageView(); 
		}

		// Fix for imv
		try {
			imv = new ImageView(new Image("Assets/Image/image_placeholder.jpg"));
			imv.setFitWidth(200);
			imv.setFitHeight(200);
		} catch (Exception e) {
			System.err.println("Error loading image_placeholder.jpg: " + e.getMessage());
			imv = new ImageView(); 
		}
	}

	// Function to calculate pizza price from CSV
	private double getPizzaPriceFromCSV(String pizzaName, String size) {
		try (BufferedReader br = new BufferedReader(new FileReader("Assets/mypizzamenu.csv"))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] values = line.split(","); 
				if (values[0].equalsIgnoreCase(pizzaName) && values[1].equalsIgnoreCase(size)) {
					return Double.parseDouble(values[2]);
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading the CSV file: " + e.getMessage());
		}
		return 0.0; 
	}

	// Event handling
	@Override
	public void init() {

		// Disable the address field initially
		txtAddress.setDisable(true);

		// Add a ChangeListener to monitor the ComboBox selection
		bOrderType.valueProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				if (newValue.equals("Delivery")) {
					txtAddress.setDisable(false); 
				} else {
					txtAddress.setDisable(true); 
				}
			}
		});

		// handle events on add button
		btnAdd.setOnAction(e -> {
			// Capture the customer name and address
			String customerName = txtCustomerName.getText().trim();
			String customerAddress = txtAddress.getText().trim();

			// Check if the customer name has been filled in
			if (customerName.isEmpty()) {
				Alert alert = new Alert(Alert.AlertType.WARNING, "Please enter the customer's name before proceeding.");
				alert.showAndWait();
				return;
			}

			// Saves the customer name and disables the field to prevent changes later
			String savedCustomerName = customerName;
			txtCustomerName.setDisable(true);

			if (!customerDetailsAdded) {
				String customerDetails = String.format("Customer Name: %s\nAddress: %s\n\n", savedCustomerName,
						customerAddress);
				taOrderDetails.insertText(0, customerDetails); 
				customerDetailsAdded = true; 
			}

			// Capture the selected values ​​from the order
			String orderType = bOrderType.getSelectionModel().getSelectedItem();
			String pizza = lvPizzaMenu.getSelectionModel().getSelectedItem(); // Pizza selecionada

			// Check if the pizza was selected
			if (pizza == null || pizza.isEmpty()) {
				Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a pizza before proceeding.");
				alert.showAndWait();
				return;
			}

			int totalPizza = sTotalPizza.getValue(); 
			String size = ((RadioButton) tgSize.getSelectedToggle()).getText(); 
			String base = ((RadioButton) tgBase.getSelectedToggle()).getText(); 
			String sauce = ((RadioButton) tgSauce.getSelectedToggle()).getText(); 

			// Capture the selected extra covers
			StringBuilder toppings = new StringBuilder();
			if (cbProsciutto.isSelected())
				toppings.append("Prosciutto, ");
			if (cbSoppressata.isSelected())
				toppings.append("Soppressata, ");
			if (cbChicken.isSelected())
				toppings.append("Roasted Chicken, ");
			if (cbSausage.isSelected())
				toppings.append("Roasted Sausage, ");
			if (cbPepperoni.isSelected())
				toppings.append("Pepperoni, ");
			if (cbHam.isSelected())
				toppings.append("Ham, ");
			if (cbMeatball.isSelected())
				toppings.append("Meatball, ");

			// Removes the comma and extra space at the end or sets "No Toppings" if empty
			if (toppings.length() > 0) {
				toppings.setLength(toppings.length() - 2);
			} else {
				toppings.append("No Toppings");
			}

			// Calculate the price of the pizza based on the size and quantity
			double pizzaPrice = getPizzaPriceFromCSV(pizza, size); 
			double totalPizzaPrice = pizzaPrice * totalPizza;

			// Update the total order price
			totalOrderPrice += totalPizzaPrice;

			// Create the PizzaOrder object, passing all the necessary parameters
			PizzaOrder pizzaOrder = new PizzaOrder(orderType, savedCustomerName, customerAddress, pizza, size, base,
					sauce, toppings.toString(), totalPizza, totalPizzaPrice);

			// Add the order to the order list
			pizzaOrders.add(pizzaOrder);

			// Display the pizza details in the TextArea
			taOrderDetails.appendText(String.format(
					"Order Type: %s\nPizza: %s\nQuantity: %d\nSize: %s\nBase: %s\nSauce: %s\nToppings: %s\nPrice: €%.2f\n\n",
					orderType, pizza, totalPizza, size, base, sauce, toppings, totalPizzaPrice));

			// Updates the label with the total order price
			lblTotal.setText(String.format("Total: €%.2f", totalOrderPrice));

			// Restores default values ​​for the next request
			lvPizzaMenu.getSelectionModel().selectFirst(); 
			sTotalPizza.setValueFactory(valueFactory); 
			rbRegular.setSelected(true); 
			rbPizza.setSelected(true); 
			rbOriginalsauce.setSelected(true); 
			cbProsciutto.setSelected(false);
			cbSoppressata.setSelected(false);
			cbChicken.setSelected(false);
			cbSausage.setSelected(false);
			cbPepperoni.setSelected(false);
			cbHam.setSelected(false);
			cbMeatball.setSelected(false);
			lblStatus.setText("Added to order");
		});

		// "Clear" button event
		btnClear.setOnAction(e -> {
			// Restores to default values
			taOrderDetails.clear();
			pizzaOrders.clear();
			totalOrderPrice = 0.0;
			lblTotal.setText(String.format("Total: €%.2f", totalOrderPrice));	
			txtCustomerName.clear();
			txtCustomerName.setDisable(false);
			customerDetailsAdded = false;
			txtAddress.clear();
			lblStatus.setText("Order cleared.");
			lvPizzaMenu.getSelectionModel().selectFirst(); 
			sTotalPizza.setValueFactory(valueFactory); 
			rbRegular.setSelected(true); 
			rbPizza.setSelected(true); 
			rbOriginalsauce.setSelected(true); 
			cbProsciutto.setSelected(false);
			cbSoppressata.setSelected(false);
			cbChicken.setSelected(false);
			cbSausage.setSelected(false);
			cbPepperoni.setSelected(false);
			cbHam.setSelected(false);
			cbMeatball.setSelected(false);
		});

		// handle events on remove button
		btnRemove.setOnAction(e -> {
			System.out.println("Current PizzaOrders: " + pizzaOrders); 

			if (!pizzaOrders.isEmpty()) {
				// Remove the last pizza from the list
				PizzaOrder lastOrder = pizzaOrders.remove(pizzaOrders.size() - 1);
				totalOrderPrice -= lastOrder.price;

				// Clear the TextArea
				taOrderDetails.clear();

				// Add customer name and address only once
				if (customerDetailsAdded) {
					taOrderDetails.appendText(
							String.format("Customer Name: %s\nAddress: %s\n\n", lastOrder.name, lastOrder.address));
				}

				// Display remaining order details
				for (PizzaOrder order : pizzaOrders) {
					taOrderDetails.appendText(String.format(
							"Order Type: %s\nPizza: %s\nQuantity: %d\nSize: %s\nBase: %s\nSauce: %s\nToppings: %s\nPrice: €%.2f\n\n",
							order.type, order.pizza, order.quantity, order.size, order.base, order.sauce,
							order.toppings, order.price));
				}

				// Update the total price
				lblTotal.setText(String.format("Total: €%.2f", totalOrderPrice));
			} else {
				// If there are no pizzas in the list
				Alert alert = new Alert(Alert.AlertType.WARNING, "No pizzas in the order to remove!");
				alert.showAndWait();
			}
			lblStatus.setText("Last pizza removed from the order.");
			// Restores default values ​​for the next request
			lvPizzaMenu.getSelectionModel().selectFirst(); 
			sTotalPizza.setValueFactory(valueFactory); 
			rbRegular.setSelected(true); 
			rbPizza.setSelected(true); 
			rbOriginalsauce.setSelected(true); 
			cbProsciutto.setSelected(false);
			cbSoppressata.setSelected(false);
			cbChicken.setSelected(false);
			cbSausage.setSelected(false);
			cbPepperoni.setSelected(false);
			cbHam.setSelected(false);
			cbMeatball.setSelected(false);
		});

		// handle events on pay button
		btnPay.setOnAction(e -> {

			// Check if there are orders in the list
			if (pizzaOrders.isEmpty()) {
				Alert alert = new Alert(Alert.AlertType.WARNING,
						"Please add at least one item to the order before proceeding with payment.");
				alert.showAndWait();
				return;
			}

			// Creates the confirmation message with the order total
			String confirmationMessage = String
					.format("Your total order amount is €%.2f. Do you want to confirm the order?", totalOrderPrice);

			// Create the confirmation dialog
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION, confirmationMessage, ButtonType.CANCEL,
					ButtonType.OK);

			alert.setTitle("Confirm Order");
			alert.setHeaderText("Please confirm your order");

			// Wait for user response
			alert.showAndWait().ifPresent(response -> {
				if (response == ButtonType.OK) {
					// Update the status to "Preparing your order" and display the ProgressBar
					lblStatus.setText("Preparing your order");
					progBar.setProgress(0);
					progBar.setVisible(true); 
					txtCustomerName.setDisable(true);

					// Disables buttons while payment is in progress
					btnAdd.setDisable(true);
					btnPay.setDisable(true);
					btnRemove.setDisable(true);
					btnClear.setDisable(true);

					// Create a new thread to simulate the preparation progress
					Thread preparationThread = new Thread(() -> {
						for (double i = 0; i <= 1; i += 0.01) {
							try {
								Thread.sleep(100); 
							} catch (InterruptedException ex) {
								ex.printStackTrace();
							}

							// Update progress bar on main thread
							final double progress = i;
							javafx.application.Platform.runLater(() -> progBar.setProgress(progress));
						}

						// When the progress bar reaches 100%, update the status
						javafx.application.Platform.runLater(() -> {
							lblStatus.setText("Pizza is ready! \r" + "You can now place another order");
							progBar.setVisible(false);
							btnAdd.setDisable(false);
							btnPay.setDisable(false);
							btnRemove.setDisable(false);
							btnClear.setDisable(false);
							txtCustomerName.setDisable(false);

							// Reset the order list and total
							pizzaOrders.clear();
							totalOrderPrice = 0.0;
							lblTotal.setText(String.format("Total: €%.2f", totalOrderPrice));
							
							// Clear the TextArea and the order list
							taOrderDetails.clear();
							pizzaOrders.clear();

							// Restore the customer name field and enable it again
							txtCustomerName.clear();

							// Restore the control variable
							customerDetailsAdded = false;

							// Can also restore client address
							txtAddress.clear();

							lvPizzaMenu.getSelectionModel().selectFirst(); 
							sTotalPizza.setValueFactory(valueFactory); 
							rbRegular.setSelected(true); 
							rbPizza.setSelected(true); 
							rbOriginalsauce.setSelected(true); 

							// Uncheck all CheckBoxes for extra coverage
							cbProsciutto.setSelected(false);
							cbSoppressata.setSelected(false);
							cbChicken.setSelected(false);
							cbSausage.setSelected(false);
							cbPepperoni.setSelected(false);
							cbHam.setSelected(false);
							cbMeatball.setSelected(false);
						});
					});

					// Start the thread to simulate pizza preparation
					preparationThread.start();
				} else {
					
					lblStatus.setText("Order was canceled.");
				}
				
			});

		});

		// Initialize map of pizza images
		pizzaImages.put("Margherita", "Assets/Image/margherita.png");
		pizzaImages.put("Salami", "Assets/Image/salami.png");
		pizzaImages.put("Prosciutto", "Assets/Image/prosciutto.png");
		pizzaImages.put("Pepperoni", "Assets/Image/pepperoni.png");
		pizzaImages.put("Mixed Veggies", "Assets/Image/mixed_veggies.png");
		pizzaImages.put("Truffle Lover", "Assets/Image/truffle_lover.png");
		pizzaImages.put("Chicken Pesto", "Assets/Image/chicken_pesto.png");
		pizzaImages.put("House Special", "Assets/Image/house_special.png");
		pizzaImages.put("Emma Choice", "Assets/Image/emmas_choice.png");
		pizzaImages.put("Wisewguy", "Assets/Image/wiseguy.png");
		pizzaImages.put("Chicken Teriyaki", "Assets/Image/chicken_teriyaki.png");
		pizzaImages.put("Fire Roasted Garlic", "Assets/Image/fire_roasted_garlic.png");

		// Add listener to ListView to update the image
		lvPizzaMenu.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue != null) {
				updateImage(newValue);
			}
		});
		// Automatically select the first item in the list
		lvPizzaMenu.getSelectionModel().selectFirst();

		// Update the image corresponding to the first item
		updateImage(lvPizzaMenu.getSelectionModel().getSelectedItem());
	}

	// Method to update the image in the center
	private void updateImage(String pizzaName) {
		String imagePath = pizzaImages.get(pizzaName);
		if (imagePath != null) {
			try {
				imv.setImage(new Image(imagePath));
			} catch (Exception e) {
				System.err.println("Error loading image: " + e.getMessage());
			}
		}
	}

	private void loadUniqueNames(String arquivoCSV, ObservableList<String> pizzaNames) {
		Set<String> nomesUnicos = new HashSet<>();

		try (BufferedReader br = new BufferedReader(new FileReader(arquivoCSV))) {
			String linha;
			
			br.readLine();

			
			while ((linha = br.readLine()) != null) {
				String[] dados = linha.split(",");
				String pizzaName = dados[0]; 
				nomesUnicos.add(pizzaName);
			}
		} catch (IOException e) {
			System.err.println("Error reading CSV file: " + e.getMessage());
		}

		
		pizzaNames.setAll(nomesUnicos);
	}

	// Window setup and layouts
	@Override
	public void start(Stage primaryStage) throws Exception {
		// Set up your primary stage and layouts
		primaryStage.setTitle("PizzaVending - " + studentName + " " + studentNumber);

		// Load unique names from CSV file
		loadUniqueNames("Assets/mypizzamenu.csv", pizzaNames);

		lvPizzaMenu.getSelectionModel().selectFirst();

		// Layout principal (BorderPane)
		BorderPane root = new BorderPane();
		root.setPadding(new Insets(2));

		// Top: Title and logo
		HBox topBox = new HBox(2);
		topBox.setAlignment(Pos.CENTER);
		topBox.getChildren().addAll(imvLogo);

		// Top section (title and customer details)
		HBox topBox2 = new HBox(10);
		topBox2.setAlignment(Pos.CENTER);
		topBox2.setPadding(new Insets(10));
		topBox2.getChildren().addAll(lblOrderType, bOrderType, lblCustomerName, txtCustomerName, lblAddress,
				txtAddress);

		// Group the two HBoxes into a VBox and add it to the Top
		VBox topSection = new VBox(2, topBox, topBox2);
		root.setTop(topSection);

		// Left section (menu and export history)
		VBox leftBox = new VBox(2);
		leftBox.setAlignment(Pos.CENTER);
		leftBox.setPadding(new Insets(10));
		leftBox.getChildren().addAll(lvPizzaMenu);
		root.setLeft(leftBox);

		VBox leftBox2 = new VBox(2);
		leftBox2.setAlignment(Pos.CENTER);
		leftBox2.setPadding(new Insets(10));
		leftBox2.getChildren().addAll(lblStatus, progBar);
		root.setLeft(leftBox2);

		// Group the two HBoxes into a VBox and add it to the Bottom
		VBox leftSection = new VBox(2, leftBox, leftBox2);
		root.setLeft(leftSection);

		// Center section (image and order details)
		VBox centerBox = new VBox(2);
		centerBox.setAlignment(Pos.CENTER);
		centerBox.setPadding(new Insets(10));
		centerBox.getChildren().addAll(imv);
		root.setCenter(centerBox);

		// Center section (image and order details)
		VBox centerBox2 = new VBox(2);
		centerBox2.setAlignment(Pos.CENTER);
		centerBox2.setPadding(new Insets(10));
		centerBox2.getChildren().addAll(lblYourOrder, taOrderDetails);
		root.setCenter(centerBox2);

		// Group the two HBoxes into a VBox and add it to the Bottom
		VBox centerSection = new VBox(2, centerBox, centerBox2);
		root.setCenter(centerSection);

		// Right section (size, base, sauce, topping)
		VBox rightBox = new VBox(2);
		rightBox.setPadding(new Insets(10));
		rightBox.getChildren().addAll(lblTotalPizza, sTotalPizza, lblSize, rbRegular, rbLarge, lblBase, rbPizza,
				rbCalzone, rbGlutenFree, lblSauce, rbOriginalsauce, rbTomatosauce, rbBasilPesto, rbTeriyakiSauce,
				rbTruffleCream, rbNoSauce, lblToppings, cbProsciutto, cbSoppressata, cbChicken, cbSausage, cbPepperoni,
				cbHam, cbMeatball);
		root.setRight(rightBox);

		// Bottom section (buttons and total)
		HBox bottomBox = new HBox(5);
		bottomBox.setAlignment(Pos.CENTER);
		bottomBox.setPadding(new Insets(10));
		bottomBox.getChildren().addAll(btnAdd, btnRemove, btnClear, lblTotal, btnPay);
		root.setBottom(bottomBox);
		
		// Add the icon to the window
	    Image appIcon = new Image("Assets/Image/icon.png"); 
	    primaryStage.getIcons().add(appIcon);


		// show the main window
		Scene scene = new Scene(root, 800, 700);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		// launch the application
		launch(args);
	}
}