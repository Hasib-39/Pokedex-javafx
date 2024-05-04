package org.example.pokedex;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class AppData implements Initializable {

    @FXML
    private Button favBtn;
    @FXML
    private TextField searchBar;
    @FXML
    private Button searchBtn;
    @FXML
    private GridPane gridpane;

    @FXML
    private ScrollPane scrollpane;
    @FXML
    private Button details;
    private List<PokemonModel> pokemons = new ArrayList<>();
    private Map<String, String> color_map = new HashMap<>();

    public List<PokemonModel> getPokemon(String sql, int value) {
        List<PokemonModel> pokemons = new ArrayList<>();
        PokemonModel pokemon;
        Connection connection = DbController.getInstance();
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            if (value == 2) {
                String keyword = searchBar.getText();
                statement.setString(1, "%" + keyword + "%");
                statement.setString(2, "%" + keyword + "%");
                statement.setString(3, "%" + keyword + "%");
            } else if (value == 3) {
                statement.setString(1, "True");
            }
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int pokID = resultSet.getInt("ID");
                String pokName = resultSet.getString("Name");
                String type1 = resultSet.getString("Type1");
                String type2 = resultSet.getString("Type2");
                int total = resultSet.getInt("Total");
                int hp = resultSet.getInt("HP");
                int attack = resultSet.getInt("Attack");
                int defense = resultSet.getInt("Defense");
                int spAttack = resultSet.getInt("Sp.Atk");
                int spDefense = resultSet.getInt("Sp.Def");
                int speed = resultSet.getInt("Speed");
                int generation = resultSet.getInt("Generation");
                String legend = resultSet.getString("Legendary");
                String fav = resultSet.getString("Favourite");

                boolean legendary;
                legendary = !legend.equals("False");
                boolean favourite;
                favourite = !fav.equals("False");
                pokemon = new PokemonModel(pokID, pokName, type1, type2, total, hp, attack, defense, spAttack, spDefense, speed, generation, legendary, favourite);
                pokemons.add(pokemon);
            }
            resultSet.close();
        } catch (Exception e) {
            System.out.println("App data: " + e);
        }

        return pokemons;
    }

    public void Search() {
        String keyword = searchBar.getText();
        if (Objects.equals(keyword, "")) return;
        List<PokemonModel> pokemons_search = new ArrayList<>(getPokemon("SELECT * FROM POKEMON WHERE NAME LIKE ? OR TYPE1 LIKE ? OR TYPE2 LIKE ?", 2));
        gridpane.getChildren().clear();
        int column = 0;
        int row = 1;
        try {
            for (int i = 0; i < pokemons_search.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("pokemonCard.fxml"));
                AnchorPane anchorpane = fxmlLoader.load();
                pkController pokcontroller = fxmlLoader.getController();
                pokcontroller.setData(pokemons_search.get(i));
                if (column == 3) {
                    column = 0;
                    row++;
                }
                anchorpane.setStyle("-fx-background-color: " + color_map.get(pokemons.get(i).type1) + ";");
                gridpane.add(anchorpane, column++, row);
                GridPane.setMargin(anchorpane, new Insets(10));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void FavPokemons() {
        List<PokemonModel> pokemons_fav = new ArrayList<>(getPokemon("SELECT * FROM POKEMON WHERE FAVOURITE = ?", 3));
        gridpane.getChildren().clear();
        int column = 0;
        int row = 1;
        try {
            for (int i = 0; i < pokemons_fav.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("pokemonCard.fxml"));
                AnchorPane anchorpane = fxmlLoader.load();
                pkController pokcontroller = fxmlLoader.getController();
                pokcontroller.setData(pokemons_fav.get(i));
                if (column == 3) {
                    column = 0;
                    row++;
                }
                anchorpane.setStyle("-fx-background-color: " + color_map.get(pokemons.get(i).type1) + ";");
                gridpane.add(anchorpane, column++, row);
                GridPane.setMargin(anchorpane, new Insets(10));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void print(PokemonModel pokemon) {
        System.out.println(pokemon.pokID);
        System.out.println(pokemon.pokName);
        System.out.println(pokemon.attack);
        System.out.println();
    }

    public void home() {
        gridpane.getChildren().clear();
        pokemons.addAll(getPokemon("SELECT * FROM POKEMON", 1));
//        print(pokemons.get(5));
        int column = 0;
        int row = 1;
        try {
            for (int i = 0; i < pokemons.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("pokemonCard.fxml"));
                AnchorPane anchorpane = fxmlLoader.load();
                pkController pokcontroller = fxmlLoader.getController();
                pokcontroller.setData(pokemons.get(i));
                if (column == 3) {
                    column = 0;
                    row++;
                }
                anchorpane.setStyle("-fx-background-color: " + color_map.get(pokemons.get(i).type1) + ";");
                gridpane.add(anchorpane, column++, row);
                GridPane.setMargin(anchorpane, new Insets(10));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initColor() {
        color_map.put("Normal", "#A8A77A");
        color_map.put("Fire", "#EE8130");
        color_map.put("Water", "#6390F0");
        color_map.put("Electric", "#F7D02C");
        color_map.put("Grass", "#7AC74C");
        color_map.put("Ice", "#96D9D6");
        color_map.put("Fighting", "#C22E28");
        color_map.put("Poison", "#A33EA1");
        color_map.put("Ground", "#E2BF65");
        color_map.put("Flying", "#A98FF3");
        color_map.put("Psychic", "#F95587");
        color_map.put("Bug", "#A6B91A");
        color_map.put("Rock", "#B6A136");
        color_map.put("Ghost", "#735797");
        color_map.put("Dragon", "#6F35FC");
        color_map.put("Dark", "#705746");
        color_map.put("Steel", "#B7B7CE");
        color_map.put("Fairy", "#D685AD");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pokemons.addAll(getPokemon("SELECT * FROM POKEMON", 1));
//        print(pokemons.get(5));
        initColor();
        int column = 0;
        int row = 1;
        try {
            for (int i = 0; i < pokemons.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("pokemonCard.fxml"));
                AnchorPane anchorpane = fxmlLoader.load();
                pkController pokcontroller = fxmlLoader.getController();
                pokcontroller.setData(pokemons.get(i));
                if (column == 3) {
                    column = 0;
                    row++;
                }
                anchorpane.setStyle("-fx-background-color: " + color_map.get(pokemons.get(i).type1) + ";");
                gridpane.add(anchorpane, column++, row);
                GridPane.setMargin(anchorpane, new Insets(10));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
