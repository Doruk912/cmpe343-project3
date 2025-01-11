package com.example.controller;

import com.example.database.DatabaseConnection;
import com.example.model.Movie;
import com.example.model.Product;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ShoppingCartController {

    public Label totalPriceLabel;
    @FXML
    private Label movieLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private Label sessionLabel;

    @FXML
    private Label usernameLabel;

    @FXML
    private TableView<CartItem> cartTableView;

    @FXML
    private TableColumn<CartItem, String> nameColumn;

    @FXML
    private TableColumn<CartItem, Integer> amountColumn;

    @FXML
    private TableColumn<CartItem, Double> priceColumn;

    @FXML
    private TableColumn<CartItem, Double> vatColumn;

    private List<Integer> selectedSeats;
    private List<Product> products;
    private Movie movie;
    private LocalDate date;
    private String session;
    private int ticketCount;
    private int discountedTicketCount;

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        amountColumn.setCellValueFactory(cellData -> cellData.getValue().amountProperty().asObject());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        vatColumn.setCellValueFactory(cellData -> cellData.getValue().vatProperty().asObject());
    }

    public void setSelectedSeats(List<Integer> selectedSeats) {
        this.selectedSeats = selectedSeats;
        updateCartTableView();
    }

    public void setProducts(List<Product> products) {
        this.products = products;
        updateCartTableView();
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
        movieLabel.setText(movie.getTitle());
    }

    public void setDate(LocalDate date) {
        this.date = date;
        dateLabel.setText(date.toString());
    }

    public void setSession(String session) {
        this.session = session;
        sessionLabel.setText(session);
    }

    public void setUsername(String username) {
        this.usernameLabel.setText(username);
    }

    public void setTicketCount(int ticketCount) {
        this.ticketCount = ticketCount;
        updateCartTableView();
    }

    public void setDiscountedTicketCount(int discountedTicketCount) {
        this.discountedTicketCount = discountedTicketCount;
        updateCartTableView();
    }

    private void updateCartTableView() {
        DatabaseConnection db = new DatabaseConnection();
        cartTableView.getItems().clear();
        BigDecimal regularTicketPrice = db.getTicketPrice();
        BigDecimal discountedTicketPrice = regularTicketPrice.multiply(BigDecimal.ONE.subtract(db.getDiscountPercentage().divide(BigDecimal.valueOf(100))));
        double ticketVatRate = 0.20;
        double productVatRate = 0.10;
        double totalPrice = 0;

        if (ticketCount > 0) {
            double totalPriceForTickets = regularTicketPrice.doubleValue() * ticketCount;
            double vat = totalPriceForTickets * ticketVatRate;
            totalPrice += totalPriceForTickets;
            cartTableView.getItems().add(new CartItem("Movie Ticket", ticketCount, totalPriceForTickets, vat));
        }
        if (discountedTicketCount > 0) {
            double totalPriceForDiscountedTickets = discountedTicketPrice.doubleValue() * discountedTicketCount;
            double vat = totalPriceForDiscountedTickets * ticketVatRate;
            totalPrice += totalPriceForDiscountedTickets;
            cartTableView.getItems().add(new CartItem("Discounted Movie Ticket", discountedTicketCount, totalPriceForDiscountedTickets, vat));
        }
        if (products != null) {
            for (Product product : products) {
                double totalPriceForProduct = product.getPrice().doubleValue();
                double vat = totalPriceForProduct * productVatRate;
                totalPrice += totalPriceForProduct;
                cartTableView.getItems().add(new CartItem(product.getName(), 1, totalPriceForProduct, vat));
            }
        }

        totalPriceLabel.setText(String.format("$%.2f", totalPrice));
    }

    public void onCheckout(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Invoice");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        Stage stage = (Stage) cartTableView.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                PdfWriter writer = new PdfWriter(file.getAbsolutePath());
                PdfDocument pdf = new PdfDocument(writer);
                Document document = new Document(pdf);

                // Set up fonts
                PdfFont boldFont = PdfFontFactory.createFont(com.itextpdf.io.font.constants.StandardFonts.HELVETICA_BOLD);
                PdfFont regularFont = PdfFontFactory.createFont(com.itextpdf.io.font.constants.StandardFonts.HELVETICA);

                // Add a title
                Paragraph title = new Paragraph("Invoice")
                        .setFont(boldFont)
                        .setFontSize(20)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setMarginBottom(20);
                document.add(title);

                // Add user details
                document.add(new Paragraph("Username: " + usernameLabel.getText()).setFont(regularFont).setFontSize(12));
                document.add(new Paragraph("Movie: " + movieLabel.getText()).setFont(regularFont).setFontSize(12));
                document.add(new Paragraph("Date: " + dateLabel.getText()).setFont(regularFont).setFontSize(12));
                document.add(new Paragraph("Session: " + sessionLabel.getText()).setFont(regularFont).setFontSize(12));

                // Create a table with 4 columns
                Table table = new Table(UnitValue.createPercentArray(new float[]{3, 1, 1, 1}))
                        .useAllAvailableWidth()
                        .setMarginTop(20);

                // Add table headers
                table.addHeaderCell(new Cell().add(new Paragraph("Name").setFont(boldFont).setFontSize(12)).setBackgroundColor(ColorConstants.LIGHT_GRAY));
                table.addHeaderCell(new Cell().add(new Paragraph("Amount").setFont(boldFont).setFontSize(12)).setBackgroundColor(ColorConstants.LIGHT_GRAY));
                table.addHeaderCell(new Cell().add(new Paragraph("Price").setFont(boldFont).setFontSize(12)).setBackgroundColor(ColorConstants.LIGHT_GRAY));
                table.addHeaderCell(new Cell().add(new Paragraph("VAT").setFont(boldFont).setFontSize(12)).setBackgroundColor(ColorConstants.LIGHT_GRAY));

                // Add table rows and calculate total price
                double totalPrice = 0;
                for (CartItem item : cartTableView.getItems()) {
                    table.addCell(new Cell().add(new Paragraph(item.getName()).setFont(regularFont).setFontSize(12)));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(item.getAmount())).setFont(regularFont).setFontSize(12)));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(item.getPrice())).setFont(regularFont).setFontSize(12)));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(item.getVat())).setFont(regularFont).setFontSize(12)));
                    totalPrice += item.getPrice();
                }

                document.add(table);

                // Add total price at the bottom
                Paragraph totalParagraph = new Paragraph("Total Price: $" + String.format("%.2f", totalPrice))
                        .setFont(boldFont)
                        .setFontSize(14)
                        .setTextAlignment(TextAlignment.RIGHT)
                        .setMarginTop(20);
                document.add(totalParagraph);

                document.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onBack(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/SeatSelection.fxml"));
            Parent root = loader.load();

            SeatSelectionController controller = loader.getController();
            controller.setMovie(movie);
            controller.setDate(date);
            controller.setSession(session);
            controller.setRegularTickets(ticketCount);
            controller.setDiscountedTickets(discountedTicketCount);
            controller.setUsername(usernameLabel.getText());
            controller.setSelectedProducts(products);

            Stage stage = (Stage) movieLabel.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class CartItem {
        private final SimpleStringProperty name;
        private final SimpleIntegerProperty amount;
        private final SimpleDoubleProperty price;
        private final SimpleDoubleProperty vat;

        public CartItem(String name, int amount, double price, double vat) {
            this.name = new SimpleStringProperty(name);
            this.amount = new SimpleIntegerProperty(amount);
            this.price = new SimpleDoubleProperty(price);
            this.vat = new SimpleDoubleProperty(vat);
        }

        public String getName() {
            return name.get();
        }

        public int getAmount() {
            return amount.get();
        }

        public double getPrice() {
            return price.get();
        }

        public double getVat() {
            return vat.get();
        }

        public SimpleStringProperty nameProperty() {
            return name;
        }

        public SimpleIntegerProperty amountProperty() {
            return amount;
        }

        public SimpleDoubleProperty priceProperty() {
            return price;
        }

        public SimpleDoubleProperty vatProperty() {
            return vat;
        }
    }
}