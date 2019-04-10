package recipesearch;

import se.chalmers.ait.dat215.lab2.Recipe;
import se.chalmers.ait.dat215.lab2.RecipeDatabase;
import se.chalmers.ait.dat215.lab2.SearchFilter;

import java.util.List;

public class RecipeBackendController {
    private String difficulty;
    private int maxTime;
    private String cuisine;
    private int maxPrice;
    private String mainIngredient;
    private RecipeDatabase db = RecipeDatabase.getSharedInstance();

    public List<Recipe> getRecipes(){
        List<Recipe> recipes = db.search(new SearchFilter(difficulty, maxTime, cuisine, maxPrice, mainIngredient));
        return recipes;
    }

    public void setCuisine(String cuisine){
        if(cuisine.equals("Sverige")||cuisine.equals("Grekland")||cuisine.equals("Indien")||cuisine.equals("Asien")||cuisine.equals("Afrika")||cuisine.equals("Frankrike")){
            this.cuisine = cuisine;
        }

    }

    public void setMainIngredient(String mainIngredient){
        if(mainIngredient.equals("Kött")||mainIngredient.equals("Fisk")||mainIngredient.equals("Kyckling")||mainIngredient.equals("Vegetarisk")){
            this.mainIngredient=mainIngredient;
        }
    }

    public void setDifficulty(String difficulty){
        if(difficulty.equals("Lätt")||difficulty.equals("Mellan")||difficulty.equals("Svår")){
            this.difficulty = difficulty;
        }
    }

    public void setMaxPrice(int maxPrice){
        if(maxPrice>0){
            this.maxPrice = maxPrice;
        }

    }

    public void setMaxTime(int maxTime){
        for(int i = 1; i <= 15; i++){
            if(maxTime == i * 10){
                this.maxTime = maxTime;
                return;
            }
        }
        this.maxTime = 0;
    }
}
