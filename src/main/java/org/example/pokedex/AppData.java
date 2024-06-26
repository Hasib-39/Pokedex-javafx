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
    private int cardno = 30;
    private int beginCardno = 0;
    private int endCardno = 30;
    private int fav_pokno;
    private int search_pokno;
    private int maxcardno = 801;
    private String page_info = "Home";
    private List<PokemonModel> pokemons = new ArrayList<>();
    private Map<String, String> color_map = new HashMap<>();

    public List<PokemonModel> getPokemon(String sql, int value) {
        List<PokemonModel> pokemons_all = new ArrayList<>();
        PokemonModel pokemon;
        Connection connection = DbController.getInstance();
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            if (value == 2) {
                String keyword = searchBar.getText();
                statement.setString(1, "%" + keyword + "%");
                statement.setString(2, "%" + keyword + "%");
                statement.setString(3, "%" + keyword + "%");
                statement.setString(4, keyword);

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
                pokemons_all.add(pokemon);
            }
            resultSet.close();
        } catch (Exception e) {
            System.out.println("App data: " + e);
        }
        pokemons.addAll(pokemons_all);
        return pokemons_all;
    }

    public void Search() {
        String keyword = searchBar.getText();
        page_info = "Search";
        if (Objects.equals(keyword, "")) return;
        List<PokemonModel> pokemons_search = new ArrayList<>(getPokemon("SELECT * FROM POKEMON WHERE NAME LIKE ? OR TYPE1 LIKE ? OR TYPE2 LIKE ? OR ID = ?", 2));
        if (pokemons_search.isEmpty()) return;
        gridpane.getChildren().clear();
        scrollpane.setVvalue(0);
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
                anchorpane.setStyle("-fx-background-color: " + color_map.get(pokemons_search.get(i).type1) + ";" + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 10, 0, 0, 7);");
                gridpane.add(anchorpane, column++, row);
                GridPane.setMargin(anchorpane, new Insets(10));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void FavPokemons() {
        List<PokemonModel> pokemons_fav = new ArrayList<>(getPokemon("SELECT * FROM POKEMON WHERE FAVOURITE = ?", 3));
        page_info = "Favourite";
        gridpane.getChildren().clear();
        scrollpane.setVvalue(0);
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
//                anchorpane.setStyle("-fx-background-color: " + color_map.get(pokemons_fav.get(i).type1) + ";");
                anchorpane.setStyle("-fx-background-color: " + color_map.get(pokemons_fav.get(i).type1) + "; "
                        + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 10, 0, 0, 7);");

                gridpane.add(anchorpane, column++, row);
                GridPane.setMargin(anchorpane, new Insets(10));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void nextprev_init() {
        beginCardno = 0;
        endCardno = 30;
    }


    public void nextClick() {
        if (Objects.equals(page_info, "Home")) {
            int newBeginCardno = Math.min(endCardno, maxcardno);
            endCardno = Math.min(endCardno + 30, maxcardno);
            if (newBeginCardno != beginCardno) {
                beginCardno = newBeginCardno;
                home(beginCardno, endCardno);
            }
        }
    }

    public void prevClick() {
        if (Objects.equals(page_info, "Home")) {
            beginCardno = Math.max(beginCardno - 30, 0);
            endCardno = beginCardno + 30;
            home(beginCardno, endCardno);
        } else if (Objects.equals(page_info, "Favourite")) {
            // Handle favourite page
        } else {
            // Handle search page
        }
    }


    public void homeClick() {
        home(0, 30);
    }

    public void home(int beginCardno, int endCardno) {
        page_info = "Home";
        gridpane.getChildren().clear();
        scrollpane.setVvalue(0);
//        pokemons.addAll(getPokemon("SELECT * FROM POKEMON", 1));
//        print(pokemons.get(5));
        int column = 0;
        int row = 1;
        try {
            for (int i = beginCardno; i < endCardno; i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("pokemonCard.fxml"));
                AnchorPane anchorpane = fxmlLoader.load();
                pkController pokcontroller = fxmlLoader.getController();
                pokcontroller.setData(pokemons.get(i));
                if (column == 3) {
                    column = 0;
                    row++;
                }
                anchorpane.setStyle("-fx-background-color: " + color_map.get(pokemons.get(i).type1) + ";" + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 10, 0, 0, 7);");
                gridpane.add(anchorpane, column++, row);
                GridPane.setMargin(anchorpane, new Insets(10));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initColor() {
        color_map.put("Normal", "#A7A877");
        color_map.put("Fire", "#FB6C6C");
        color_map.put("Water", "#77BDFE");
        color_map.put("Grass", "#48D0B0");
        color_map.put("Electric", "#FFCE4B");
        color_map.put("Ice", "#99D7D8");
        color_map.put("Fighting", "#C03128");
        color_map.put("Poison", "#9F419F");
        color_map.put("Ground", "#E1C068");
        color_map.put("Flying", "#A890F0");
        color_map.put("Psychic", "#F95887");
        color_map.put("Bug", "#A8B91F");
        color_map.put("Rock", "#B8A038");
        color_map.put("Ghost", "#705998");
        color_map.put("Dark", "#6F5848");
        color_map.put("Dragon", "#7138F8");
        color_map.put("Steel", "#B8B8D0");
        color_map.put("Fairy", "#A890F0");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pokemons.addAll(getPokemon("SELECT * FROM POKEMON", 1));
//        print(pokemons.get(5));
        initColor();
        int column = 0;
        int row = 1;
        try {
            for (int i = beginCardno; i < endCardno; i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("pokemonCard.fxml"));
                AnchorPane anchorpane = fxmlLoader.load();
                pkController pokcontroller = fxmlLoader.getController();
                pokcontroller.setData(pokemons.get(i));
                if (column == 3) {
                    column = 0;
                    row++;
                }
                anchorpane.setStyle("-fx-background-color: " + color_map.get(pokemons.get(i).type1) + ";" + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 10, 0, 0, 7);");
                gridpane.add(anchorpane, column++, row);
                GridPane.setMargin(anchorpane, new Insets(10));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
