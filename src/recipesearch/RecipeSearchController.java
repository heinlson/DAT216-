
package recipesearch;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.util.Callback;
import se.chalmers.ait.dat215.lab2.Recipe;
import se.chalmers.ait.dat215.lab2.RecipeDatabase;


public class RecipeSearchController implements Initializable {
    RecipeDatabase db = RecipeDatabase.getSharedInstance();
    private Map<String, RecipeListItem> recipeListItemMap = new HashMap<String, RecipeListItem>();
    private RecipeBackendController recipeBackendController;
    private List<Recipe> recipes;

    //fx:id stuff
    @FXML
    private AnchorPane recipeDetailPane;
    @FXML
    private SplitPane searchPane;
    @FXML
    private ComboBox mainIngredientBox;
    @FXML
    private ComboBox cuisineBox;
    @FXML
    private RadioButton allDiff;
    @FXML
    private RadioButton easyDiff;
    @FXML
    private RadioButton medDiff;
    @FXML
    private RadioButton hardDiff;
    @FXML
    private Spinner maxPriceSpinner;
    @FXML
    private Slider maxTimeSlider;
    @FXML
    private Label maxTimeLabel;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private FlowPane flowPane;
    @FXML
    private ToggleGroup difficultyToggleGroup;
    @FXML
    private Label recipeName;
    @FXML
    private ImageView recipeImage;
    @FXML
    private Button closeRecipeDetailButton;

    //fx:id for logo
    @FXML
    private ImageView logoImage;
    @FXML
    private Label logoMainText; //"Recept"
    @FXML
    private Label logoSubText;  //"Sök"


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        recipeBackendController = new RecipeBackendController();
        initMainIngredientBox();
        populateMainIngredientComboBox();
        initCuisineBox();
        populateCuisineComboBox();
        initDifficultyToggleGroup();
        initMaxPriceSpinner();
        initSlider();


        for (Recipe recipe : recipeBackendController.getRecipes()) {
            RecipeListItem recipeListItem = new RecipeListItem(recipe, this);
            recipeListItemMap.put(recipe.getName(), recipeListItem);
        }

        updateRecipeList();
    }


    private void updateRecipeList() {
        flowPane.getChildren().clear();
        recipes = recipeBackendController.getRecipes();

        for (int i = 0; i < recipes.size(); i++) {
            flowPane.getChildren().add(recipeListItemMap.get(recipes.get(i).getName()));
        }
    }


    private void initMainIngredientBox() {
        mainIngredientBox.getItems().addAll("Visa alla", "Kött", "Fisk", "Kyckling", "Vegetarisk");
        mainIngredientBox.getSelectionModel().select("Visa alla");

        mainIngredientBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                recipeBackendController.setMainIngredient(newValue);
                updateRecipeList();
            }
        });
    }

    private void initCuisineBox() {
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

    private void initDifficultyToggleGroup() {
        difficultyToggleGroup = new ToggleGroup();
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


    private void initMaxPriceSpinner() {
        initMaxPriceSpinnerValueFactory();
        initMaxPriceSpinnerTextInput();
    }

    private void initMaxPriceSpinnerValueFactory() {
        //For maxPriceSpinner
        int priceMinValue = 0;
        int priceMaxValue = 500;
        int priceInitValue = 0;
        int priceStepValue = 10;
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(priceMinValue, priceMaxValue, priceInitValue, priceStepValue);
        maxPriceSpinner.setValueFactory(valueFactory);

        maxPriceSpinner.valueProperty().addListener(new ChangeListener<Integer>() {

            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {

                //throw new RuntimeException();

                if (!oldValue.equals(newValue)) {
                    Integer value = Integer.valueOf(maxPriceSpinner.getEditor().getText());
                    recipeBackendController.setMaxPrice(value);
                    updateRecipeList();
                } else {
                    //do nothing
                }
            }
        });
    }


    private void initMaxPriceSpinnerTextInput() {
        maxPriceSpinner.focusedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

                if (newValue) {
                    //focusgained - do nothing
                } else {
                    Integer value = Integer.valueOf(maxPriceSpinner.getEditor().getText());
                    recipeBackendController.setMaxPrice(value);
                    updateRecipeList();
                }

            }
        });
    }

    private void initSlider() {
        maxTimeSlider.valueProperty().setValue(50);
        maxTimeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldVal, Number newVal) {
                if (newVal != null && !newVal.equals(oldVal) && !maxTimeSlider.isValueChanging()) {
                    recipeBackendController.setMaxTime((int) maxTimeSlider.getValue());
                    int d = (int) maxTimeSlider.getValue();
                    String s = String.valueOf(d);
                    maxTimeLabel.setText("Vald tid: " + s + " minuter");
                    updateRecipeList();
                }
            }
        });
    }


    private void populateRecipeDetailView(Recipe recipe) {

        try {
            recipeImage.setImage(recipe.getFXImage());
            recipeName.setText(recipe.getName());
        } catch (Exception exc) {
            throw new RuntimeException();
        }

    }

    @FXML
    public void closeRecipeView() {
        searchPane.toFront();
    }

    protected void openRecipeView(Recipe recipe) {
        populateRecipeDetailView(recipe);
        recipeDetailPane.toFront();
    }


    private void populateMainIngredientComboBox() {
        Callback<ListView<String>, ListCell<String>> cellFactory = new Callback<ListView<String>, ListCell<String>>() {

            @Override
            public ListCell<String> call(ListView<String> p) {

                return new ListCell<String>() {

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        setText(item);

                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            Image icon = null;
                            String iconPath;
                            try {
                                switch (item) {

                                    case "Kött":
                                        iconPath = "RecipeSearch/resources/icon_main_meat.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Fisk":
                                        iconPath = "RecipeSearch/resources/icon_main_fish.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Kyckling":
                                        iconPath = "RecipeSearch/resources/icon_main_chicken.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Vegetarisk":
                                        iconPath = "RecipeSearch/resources/icon_main_veg.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                }
                            } catch (NullPointerException ex) {
                                //This should never happen in this lab but could load a default image in case of a NullPointer
                            }
                            ImageView iconImageView = new ImageView(icon);
                            iconImageView.setFitHeight(12);
                            iconImageView.setPreserveRatio(true);
                            setGraphic(iconImageView);
                        }
                    }
                };
            }
        };
        mainIngredientBox.setButtonCell(cellFactory.call(null));
        mainIngredientBox.setCellFactory(cellFactory);
    }



    private void populateCuisineComboBox() {
        Callback<ListView<String>, ListCell<String>> cellFactory = new Callback<ListView<String>, ListCell<String>>() {

            @Override
            public ListCell<String> call(ListView<String> p) {

                return new ListCell<String>() {

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        setText(item);

                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            Image icon = null;
                            String iconPath;
                            try {
                                switch (item) {

                                    case "Sverige":
                                        iconPath = "RecipeSearch/resources/icon_flag_sweden.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Frankrike":
                                        iconPath = "RecipeSearch/resources/icon_flag_france.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Asien":
                                        iconPath = "RecipeSearch/resources/icon_flag_asia.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Grekland":
                                        iconPath = "RecipeSearch/resources/icon_flag_greece.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Afrika":
                                        iconPath = "RecipeSearch/resources/icon_flag_africa.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Indien":
                                        iconPath = "RecipeSearch/resources/icon_flag_india.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                }
                            } catch (NullPointerException ex) {
                                //This should never happen in this lab but could load a default image in case of a NullPointer
                            }
                            ImageView iconImageView = new ImageView(icon);
                            iconImageView.setFitHeight(12);
                            iconImageView.setPreserveRatio(true);
                            setGraphic(iconImageView);
                        }
                    }
                };
            }
        };
        cuisineBox.setButtonCell(cellFactory.call(null));
        cuisineBox.setCellFactory(cellFactory);
    }


    protected Image getCuisineImage(String cuisine) {
        String iconPath;
        switch (cuisine) {
            case "Sverige":
                iconPath = "RecipeSearch/resources/icon_flag_sweden.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Frankrike":
                iconPath = "RecipeSearch/resources/icon_flag_france.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Asien":
                iconPath = "RecipeSearch/resources/icon_flag_asia.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Grekland":
                iconPath = "RecipeSearch/resources/icon_flag_greece.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Afrika":
                iconPath = "RecipeSearch/resources/icon_flag_africa.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Indien":
                iconPath = "RecipeSearch/resources/icon_flag_india.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            default: return null;
        }
    }

    protected Image getMainIngrediantImage(String main){
        String iconPath;
        switch (main) {
            case "Kött":
                iconPath = "RecipeSearch/resources/icon_main_meat.png";
                return getSquareImage(new Image(getClass().getClassLoader().getResourceAsStream(iconPath)));

            case "Fisk":
                iconPath = "RecipeSearch/resources/icon_main_fish.png";
                return getSquareImage(new Image(getClass().getClassLoader().getResourceAsStream(iconPath)));
            case "Kyckling":
                iconPath = "RecipeSearch/resources/icon_main_chicken.png";
                return getSquareImage(new Image(getClass().getClassLoader().getResourceAsStream(iconPath)));
            case "Vegetarisk":
                iconPath = "RecipeSearch/resources/icon_main_veg.png";
                return getSquareImage(new Image(getClass().getClassLoader().getResourceAsStream(iconPath)));
            default:  return null;
        }

    }

    protected Image getDifficultyImage(String diff){
        String iconPath;
        switch (diff) {
            case "Lätt":
                iconPath = "RecipeSearch/resources/icon_difficulty_easy.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));

            case "Mellan":
                iconPath = "RecipeSearch/resources/icon_difficulty_medium.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Svår":
                iconPath = "RecipeSearch/resources/icon_difficulty_hard.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            default:  return null;
        }
    }

    private Image getSquareImage(Image image){

        int x = 0;
        int y = 0;
        int width = 0;
        int height = 0;

        if(image.getWidth() > image.getHeight()){
            width = (int) image.getHeight();
            height = (int) image.getHeight();
            x = (int)(image.getWidth() - width)/2;
            y = 0;
        }

        else if(image.getHeight() > image.getWidth()){
            width = (int) image.getWidth();
            height = (int) image.getWidth();
            x = 0;
            y = (int) (image.getHeight() - height)/2;
        }

        else{
            //Width equals Height, return original image
            return image;
        }
        return new WritableImage(image.getPixelReader(), x, y, width, height);
    }
}