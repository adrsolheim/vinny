
import logo from '../../../assets/tap_handle.jpg';
import styles from '../../../app.module.css';
import { Tap } from '../types';
import { BaselineAddCircleOutline } from '../../../assets/BaselineAddCircleOutline';
import { TapContext } from '../../../contexts/Context';
import CardButton from './CardButton';
import CardButtonRow from './CardButtonRow';
import DropdownMenu from './DropdownMenu';
import ModalButton from './ModalButton';

// TODO: All buttons hidden by default, show on hover

export default function TapCard(props: Readonly<{ tap: Tap; }>) {
    const tap: Tap = props.tap;
    const dropdownItems = [
      { icon: <BaselineAddCircleOutline color='white'/>, operation: 'Settings' },
      { icon: <BaselineAddCircleOutline color='white'/>, operation: 'Activate' },
    ];
    return (
      <TapContext.Provider value={tap}>        
        <div className={`${styles.card} ${tap.active ? styles.cardhighlight : ''}`}>
            <div ><img className={styles.cardimage} src={logo} alt="tap handle logo" /></div>
            <div className={`${styles.cardtitle} ${tap.active ? styles.cardtexthighlight : ''}`}><p>{tap.batchUnit?.name ?? " "}</p></div>
            <div className={styles.cardnumber}><p>{tap.id}</p></div>
            <div className={styles.cardtail }>
                <CardButtonRow>
                    <ModalButton icon={<BaselineAddCircleOutline color='white'/>} />
                    <CardButton icon={<BaselineAddCircleOutline color='white'/>}>
                      <DropdownMenu items={dropdownItems} />
                    </CardButton>
                    <CardButton icon={<BaselineAddCircleOutline color='white'/>}>
                      <DropdownMenu items={dropdownItems} />
                    </CardButton>
                </CardButtonRow>
            </div>
        </div>
      </TapContext.Provider>

    );
}


