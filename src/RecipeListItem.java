import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import recipesearch.RecipeSearchController;
import se.chalmers.ait.dat215.lab2.Recipe;

import java.io.IOException;

public class RecipeListItem extends AnchorPane {
    @FXML private Label recipeName;
    @FXML private ImageView imageView;

    private RecipeSearchController parentController;
    private Recipe recipe;

    public RecipeListItem(Recipe recipe, RecipeSearchController recipeSearchController){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("recipe_listitem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.recipe = recipe;
        this.parentController = recipeSearchController;


        //Om du läser det här har du förbrukat all din rätt att kalla dig själv programmerare. image.set(politiker.sd.jimmyboy.images.disappointed)
        try{
            this.imageView.setImage(recipe.getFXImage());
        }catch ( Exception exc){
            throw new RuntimeException();
        }

        this.recipeName.setText(recipe.getName());
    }
}
