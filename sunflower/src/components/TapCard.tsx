
import logo from '../assets/tap_handle.jpg';
import styles from '../app.module.css';
import Tap from '../types/tap';

export default function RecipeCard(props: Readonly<{ tap: Tap; }>) {
    const tap: Tap = props.tap;
    console.log(tap)
    return (
        <div className={`${styles.card} ${tap.active ? styles.cardhighlight : ''}`}>
            <img className={styles.cardimage} src={logo} alt="tap handle logo" />
            <h2 className={`${styles.cardtitle} ${tap.active ? styles.cardtexthighlight : ''}`}>{tap.batch?.name ?? " "}</h2>
            <h4 className={styles.cardnumber}>{tap.id}</h4>
            <input type="button" className={styles.btn} value="+"/>
        </div>
    );
}