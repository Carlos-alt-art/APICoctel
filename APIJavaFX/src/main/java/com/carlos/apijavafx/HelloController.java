package com.carlos.apijavafx;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML
    private ListView<Coctel> mainListView;

    @FXML
    private Button btnAleatorio;

    @FXML
    private Button btnCategoria;

    @FXML
    private ImageView viewImage;

    @FXML
    private ChoiceBox<Categoria> mainChoiceBox;

    @FXML
    private TextField textFieldBuscar;

    private ObservableList<Categoria> listaCategorias;
    private ObservableList<Coctel> listaCoctel;

    public void onKeyFiltar(KeyEvent event) {
        String filtroID = this.textFieldBuscar.getText();
        if (filtroID.isEmpty()) {
            this.mainListView.setItems(listaCoctel);
        } else {
            ObservableList<Coctel> filteredCocktails = FXCollections.observableArrayList();
            for (Coctel coctel : this.listaCoctel) {
                if (coctel.getName().toLowerCase().contains(filtroID.toLowerCase())) {
                    filteredCocktails.add(coctel);
                }
            }
            this.mainListView.setItems(filteredCocktails);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listaCategorias = FXCollections.observableArrayList();
        listaCoctel = FXCollections.observableArrayList();
        try {
            jsonCategorias();
        } catch (IOException e) {
            e.printStackTrace();
        }
        asociarElementos();

        mainListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Coctel>() {
            @Override
            public void changed(ObservableValue<? extends Coctel> observable, Coctel oldValue, Coctel newValue) {
                if (newValue != null) {
                    Image image = new Image(newValue.getImage());
                    viewImage.setImage(image);
                }
            }
        });
    }

    public void generarPorCategoria(ActionEvent actionEvent) {
        listaCoctel.clear();

        if (mainChoiceBox.getSelectionModel().getSelectedIndex() > -1) {
            String url = "https://www.thecocktaildb.com/api/json/v1/1/filter.php?a=" +
                    mainChoiceBox.getSelectionModel().getSelectedItem().getNombre();
            try (InputStream inputStream = new URL(url).openStream();
                 BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
                String response = bufferedReader.readLine();
                JSONObject jsonObject = new JSONObject(response);

                JSONArray drinksArray = jsonObject.getJSONArray("drinks");
                for (int i = 0; i < drinksArray.length(); i++) {
                    JSONObject drinkObject = drinksArray.getJSONObject(i);
                    String id = drinkObject.optString("idDrink");
                    String name = drinkObject.optString("strDrink");

                    String alcoholic = drinkObject.optString("strAlcoholic", "");
                    String image = drinkObject.optString("strDrinkThumb", "");

                    listaCoctel.add(new Coctel(id, name, alcoholic, image));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                System.out.println("Error al procesar el JSON: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            mostrarAlerta("Escoge categoría");
        }
    }

    public static void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Error");
        alerta.setHeaderText("No has seleccionado categoría");
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    public void generarCoctelAleatorio(ActionEvent actionEvent) {
        String url = "https://www.thecocktaildb.com/api/json/v1/1/random.php";
        try (InputStream inputStream = new URL(url).openStream();
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {

            String response = bufferedReader.readLine();
            JSONObject jsonObject = new JSONObject(response);
            JSONArray drinksArray = jsonObject.getJSONArray("drinks");

            if (drinksArray.length() > 0) {
                JSONObject drinkObject = drinksArray.getJSONObject(0);
                String id = drinkObject.getString("idDrink");
                String name = drinkObject.getString("strDrink");
                String alcoholic = drinkObject.getString("strAlcoholic");
                String image = drinkObject.getString("strDrinkThumb");

                listaCoctel.add(new Coctel(id, name, alcoholic, image));
                viewImage.setImage(new Image(image));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void jsonCategorias() throws IOException {
        String url = "https://www.thecocktaildb.com/api/json/v1/1/list.php?a=list";
        InputStream inputStream;
        inputStream = new URL(url).openStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder responseStringBuilder = new StringBuilder();
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            responseStringBuilder.append(line);
        }

        String response = responseStringBuilder.toString();
        JSONObject jsonObject = new JSONObject(response);

        JSONArray jsonArray = jsonObject.getJSONArray("drinks");

        String categoria;

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject categoryObject = jsonArray.getJSONObject(i);
            categoria = categoryObject.getString("strAlcoholic");
            listaCategorias.add(new Categoria(categoria));
        }
    }

    private void asociarElementos() {
        mainChoiceBox.setItems(listaCategorias);
        mainListView.setItems(listaCoctel);
    }
}
