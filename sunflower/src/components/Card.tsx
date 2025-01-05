import logo from '../assets/light_beer_mug.jpg';
import styles from '../app.module.css';

export default function Card() {
    return (
        <div className={styles.card}>
            <img className={styles.cardimage} src={logo} alt="recipe logo" />
            <h2 className={styles.cardtitle}>Beer Recipe</h2>
            <p className={styles.cardtext}>Beer description, tags and ingredients</p>
        </div>
    );
}