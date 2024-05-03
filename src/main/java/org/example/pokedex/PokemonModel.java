package org.example.pokedex;

import javafx.scene.layout.GridPane;

public class PokemonModel {
    public int pokID;
    public String pokName;
    public String type1;
    public String type2;
    public int total;
    public int hp;
    public int attack;
    public int defense;
    public int spAttack;
    public int spDefense;
    public int speed;
    public int generation;
    public boolean legendary;


    public PokemonModel(int pokID, String pokName, String type1, String type2, int total, int hp, int attack, int defense, int spAttack, int spDefense, int speed, int generation, boolean legendary) {
        this.pokID = pokID;
        this.pokName = pokName;
        this.type1 = type1;
        this.type2 = type2;
        this.total = total;
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
        this.spAttack = spAttack;
        this.spDefense = spDefense;
        this.speed = speed;
        this.generation = generation;
        this.legendary = legendary;
    }

    public String getImgSrc() {
//        System.out.println("/img/all/" + this.pokName.toLowerCase() + ".png");
        return this.pokName.toLowerCase() + ".png";
    }
}
