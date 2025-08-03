import logo from '../../../assets/light_beer_mug.jpg';
import styles from '../../../app.module.css';
import { Recipe } from '../types';

export default function RecipeCard(props: Readonly<{ recipe: Recipe; }>) {
    const recipe: Recipe = props.recipe;
    return (
        <div className={styles.recipecard}>
            <img src={logo} alt="recipe logo" />
            <h2>{recipe.name}</h2>
            <p>Recipe {recipe.name} has id {recipe.id} and brewfather id {recipe.brewfatherId}</p>
        </div>
    );
}