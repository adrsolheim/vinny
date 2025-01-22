
import logo from '../assets/tap_handle.jpg';
import styles from '../app.module.css';
import Tap from '../types/tap';
import { BaselineAddCircleOutline } from '../assets/BaselineAddCircleOutline';

export default function RecipeCard(props: Readonly<{ tap: Tap; }>) {
    const tap: Tap = props.tap;
    return (
        <div className={`${styles.card} ${tap.active ? styles.cardhighlight : ''}`}>
            <div className={styles.cardcontent}>
                <img className={styles.cardimage} src={logo} alt="tap handle logo" />
                <h2 className={`${styles.cardtitle} ${tap.active ? styles.cardtexthighlight : ''}`}>{tap.batch?.name ?? " "}</h2>
                <h4 className={styles.cardnumber}>{tap.id}</h4>
            </div>
            <div className={styles.cardtail }>
                <BaselineAddCircleOutline color='white'/>
            </div>
        </div>
    );
}