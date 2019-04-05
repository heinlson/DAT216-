package recipesearch;

import recipesearch.RecipeSearchController;
import se.chalmers.ait.dat215.lab2.Recipe;
import se.chalmers.ait.dat215.lab2.RecipeDatabase;
import se.chalmers.ait.dat215.lab2.SearchFilter;

import java.util.*;

public class RecipeBackendController {
    private String difficulty;
    private int time;
    private String cuisine;
    private int price;
    private String mainIngredient;




    public List<Recipe> getRecipes(){
        RecipeDatabase db = RecipeDatabase.getSharedInstance();
        List<Recipe> recipes = db.search(new SearchFilter(difficulty, time, cuisine, price, mainIngredient));
        return  recipes;
    }

    public void setCuisine(String cuisine){
        if(cuisine.equals("Sverige")||cuisine.equals("Grekland")||cuisine.equals("Indien")||cuisine.equals("Asien")||cuisine.equals("Afrika")||cuisine.equals("Frankrike")){
            this.cuisine=cuisine;
        }

    }

    public void setMainIngredient(String mainIngredient){
        if(mainIngredient.equals("Kött")||mainIngredient.equals("Fisk")||mainIngredient.equals("Kyckling")||mainIngredient.equals("Vegetarisk")){
                this.mainIngredient=mainIngredient;
        }
    }

    public void setDifficulty(String difficulty){
        if(difficulty.equals("Lätt")||difficulty.equals("Mellan")||difficulty.equals("Svår")){
            this.difficulty=difficulty;
        }
    }

    public void setMaxPrice(int maxPrice){
        if(maxPrice>0){
            this.price=maxPrice;
        }

    }
    public void setMaxTime(int maxTime){
        if(maxTime>9&&maxTime<151&& (maxTime % 10)==0){
            time=maxTime;
        }
    }

}
