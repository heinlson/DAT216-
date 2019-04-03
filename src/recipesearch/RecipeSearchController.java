
package recipesearch;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import se.chalmers.ait.dat215.lab2.RecipeDatabase;


public class RecipeSearchController implements Initializable {
    @FXML private  ComboBox mainIngredientBox;
    @FXML private  ComboBox cuisineBox;
    @FXML private  ToggleGroup DifficultyGroup;
    @FXML private  Spinner MaxpriceSpinner;
    @FXML private  Slider MaxtimeSlider;
    @FXML private  FlowPane flowPane;

    public  FlowPane getFlowPane(){
        return flowPane;
    }

    RecipeDatabase db = RecipeDatabase.getSharedInstance();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}
