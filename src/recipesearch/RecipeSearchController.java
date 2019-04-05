
package recipesearch;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.fxml.LoadException;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import se.chalmers.ait.dat215.lab2.Recipe;
import se.chalmers.ait.dat215.lab2.RecipeDatabase;


public class RecipeSearchController implements Initializable {
    @FXML private ComboBox mainIngredientBox;
    @FXML private ComboBox cuisineBox;
    @FXML private Spinner MaxpriceSpinner;
    @FXML private Slider MaxtimeSlider;
    @FXML private FlowPane flowPane;
    @FXML private AnchorPane recipeDetailPane;
    @FXML private SplitPane searchPane;
    @FXML private RadioButton allDiff;
    @FXML private RadioButton easyDiff;
    @FXML private RadioButton medDiff;
    @FXML private RadioButton hardDiff;
    @FXML private Label MaxpriceLabel;
    @FXML private ImageView currentRecipeImage;
    @FXML private Button closeRecipeButton;
    @FXML private Label currentRecipeLabel;
    private Map<String, RecipeListItem> recipeListItemMap = new HashMap<String, RecipeListItem>();


    private RecipeBackendController recipeBackendController;
    private List <Recipe> recipes;

    private void updateRecipeList(){
        flowPane.getChildren().clear();
        recipes = recipeBackendController.getRecipes();


        for(int i = 0; i<recipes.size(); i++){
            flowPane.getChildren().add(recipeListItemMap.get(recipes.get(i).getName()));
        }
    }

    private void populateRecipeDetailView (Recipe recipe){

        try{
            currentRecipeImage.setImage(recipe.getFXImage());
            currentRecipeLabel.setText(recipe.getName());
        } catch (Exception exc){
            throw new RuntimeException();
        }

    }

    @FXML
    public void closeRecipeView(){
        searchPane.toFront();
    }

    public void openRecipeView(Recipe recipe){
        populateRecipeDetailView(recipe);
        recipeDetailPane.toFront();
    }




    RecipeDatabase db = RecipeDatabase.getSharedInstance();

    public void initComboBox(){

        mainIngredientBox.getItems().addAll("Visa alla", "Kött", "Fisk", "Kyckling", "Vegetarisk");
        mainIngredientBox.getSelectionModel().select("Visa alla");

        mainIngredientBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                recipeBackendController.setMainIngredient(newValue);
                updateRecipeList();
            }
        });

        cuisineBox.getItems().addAll("Visa alla", "Sverige", "Grekland", "Indien", "Asien", "Afrika", "Frankrike");
        cuisineBox.getSelectionModel().select("Visa alla");

        cuisineBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                recipeBackendController.setCuisine(newValue);
                updateRecipeList();
            }
        });
    }

    private void initRadio(){
        ToggleGroup difficultyToggleGroup = new ToggleGroup();

        allDiff.setToggleGroup(difficultyToggleGroup);
        easyDiff.setToggleGroup(difficultyToggleGroup);
        medDiff.setToggleGroup(difficultyToggleGroup);
        hardDiff.setToggleGroup(difficultyToggleGroup);
        allDiff.setSelected(true);

        difficultyToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {

                if (difficultyToggleGroup.getSelectedToggle() != null) {
                    RadioButton selected = (RadioButton) difficultyToggleGroup.getSelectedToggle();
                    recipeBackendController.setDifficulty(selected.getText());
                    updateRecipeList();
                }
            }
        });

    }

    private void initSpinner(){
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 0, 10);
        MaxpriceSpinner.setValueFactory(valueFactory);
        MaxpriceSpinner.valueProperty().addListener(new ChangeListener<Integer>() {

            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {

                //throw new RuntimeException();

                if(!oldValue.equals(newValue)){
                    Integer value = Integer.valueOf(MaxpriceSpinner.getEditor().getText());
                    recipeBackendController.setMaxPrice(value);
                    updateRecipeList();
                }
                else{
                    //do nothing
                }

            }
        });
        MaxpriceSpinner.focusedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

                if(newValue){
                    //focusgained - do nothing
                }
                else{
                    Integer value = Integer.valueOf(MaxpriceSpinner.getEditor().getText());
                    recipeBackendController.setMaxPrice(value);
                    updateRecipeList();
                }
            }
        });
    }

    private void initSlider(){
        MaxtimeSlider.valueProperty().setValue(50);
        MaxtimeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                recipeBackendController.setMaxTime((int)MaxtimeSlider.getValue());
                int d = (int)MaxtimeSlider.getValue();
                String s = String.valueOf(d);
                MaxpriceLabel.setText(s);
                updateRecipeList();
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        recipeBackendController = new RecipeBackendController();

        initComboBox();
        initRadio();
        initSpinner();
        initSlider();

        for (Recipe recipe : recipeBackendController.getRecipes()) {
            RecipeListItem recipeListItem = new RecipeListItem(recipe, this);
            recipeListItemMap.put(recipe.getName(), recipeListItem);
        }





        updateRecipeList();
    }
}
