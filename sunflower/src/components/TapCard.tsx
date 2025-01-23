
import logo from '../assets/tap_handle.jpg';
import styles from '../app.module.css';
import Tap from '../types/tap';
import { BaselineAddCircleOutline } from '../assets/BaselineAddCircleOutline';

export default function TapCard(props: Readonly<{ tap: Tap; }>) {
    const tap: Tap = props.tap;
    return (
        <div className={`${styles.card} ${tap.active ? styles.cardhighlight : ''}`}>
            <div ><img className={styles.cardimage} src={logo} alt="tap handle logo" /></div>
            <div className={`${styles.cardtitle} ${tap.active ? styles.cardtexthighlight : ''}`}><p>{tap.batch?.name ?? " "}</p></div>
            <div className={styles.cardnumber}><p>{tap.id}</p></div>
            <div className={styles.cardtail }>
                <BaselineAddCircleOutline color='white'/>
            </div>
        </div>
    );
}