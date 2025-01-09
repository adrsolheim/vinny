import logo from '../assets/light_beer_mug.jpg';
import styles from '../app.module.css';
import { Recipe } from '../types/recipe';

export default function recipeCard(props: { recipe: Recipe; }) {
    const recipe: Recipe = props.recipe;
    return (
        <div className={styles.card}>
            <img className={styles.cardimage} src={logo} alt="recipe logo" />
            <h2 className={styles.cardtitle}>{recipe.name}</h2>
            <p className={styles.cardtext}>Recipe {recipe.name} has id {recipe.id} and brewfather id {recipe.brewfatherId}</p>
        </div>
    );
}