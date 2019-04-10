package recipesearch;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.ait.dat215.lab2.Recipe;

import java.io.IOException;

public class RecipeListItem extends AnchorPane {
    private RecipeSearchController parentController;
    private Recipe recipe;

    @FXML private AnchorPane anchorPane;
    @FXML private ImageView recipeImage; //stekt ägg
    @FXML private ImageView flagImage;
    @FXML private ImageView difficultyImage;
    @FXML private ImageView ingredientImage;
    @FXML private Label recipeName;
    @FXML private Label recipeTime;
    @FXML private Label recipePrice;
    @FXML private Label recipeDescription;


    public RecipeListItem(Recipe recipe, RecipeSearchController recipeSearchController){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("recipe_listitem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException();
        }

        this.recipe = recipe;
        this.parentController = recipeSearchController;


        try{
            this.recipeImage.setImage(recipe.getFXImage());
        }catch (Exception exc){
            throw new RuntimeException();
        }

        try{
            this.flagImage.setImage(parentController.getCuisineImage(recipe.getCuisine()));
        }catch (Exception exc){
            throw new RuntimeException();
        }
        try{
            this.difficultyImage.setImage(parentController.getDifficultyImage(recipe.getDifficulty()));
        }catch (Exception exc){
            throw new RuntimeException();
        }
        try{
            this.ingredientImage.setImage(parentController.getMainIngrediantImage(recipe.getMainIngredient()));
        }catch (Exception exc){
            throw new RuntimeException();
        }

        recipeTime.setText(String.valueOf(recipe.getTime()) + " minuter");
        recipePrice.setText(String.valueOf(recipe.getPrice()) + " kr");
        recipeDescription.setText(recipe.getDescription());

        this.recipeName.setText(recipe.getName());
    }

    @FXML
    protected void onClick(Event event){
        parentController.openRecipeView(recipe);
    }
}
