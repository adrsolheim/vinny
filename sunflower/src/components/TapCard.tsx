
import logo from '../assets/tap_handle.jpg';
import styles from '../app.module.css';
import Tap from '../types/tap';
import { BaselineAddCircleOutline } from '../assets/BaselineAddCircleOutline';
import { useState } from 'react';

// TODO: All buttons hidden by default, show on hover

export default function TapCard(props: Readonly<{ tap: Tap; }>) {
    const tap: Tap = props.tap;
    const dropdownItems = [
      { icon: <BaselineAddCircleOutline color='white'/>, operation: 'Settings' },
      { icon: <BaselineAddCircleOutline color='white'/>, operation: 'Activate' },
    ];
    return (
        <div className={`${styles.card} ${tap.active ? styles.cardhighlight : ''}`}>
            <div ><img className={styles.cardimage} src={logo} alt="tap handle logo" /></div>
            <div className={`${styles.cardtitle} ${tap.active ? styles.cardtexthighlight : ''}`}><p>{tap.batch?.name ?? " "}</p></div>
            <div className={styles.cardnumber}><p>{tap.id}</p></div>
            <div className={styles.cardtail }>
                <CardButtonRow>
                    <CardButton icon={<BaselineAddCircleOutline color='white'/>}>
                      <DropdownMenu items={dropdownItems} />
                    </CardButton>
                    <CardButton icon={<BaselineAddCircleOutline color='white'/>}>
                      <DropdownMenu items={dropdownItems} />
                    </CardButton>
                </CardButtonRow>
            </div>
        </div>
    );
}

interface CardButtonRowProps {
  // this should be React.ReactElement?
  children?: React.ReactNode;
}
interface CardButtonProps {
  // this should be React.ReactElement?
  icon: JSX.Element;
  children?: React.ReactNode;
}
interface DropdownProps {
  items: DropdownItemProps[];
}
interface DropdownItemProps {
  icon: JSX.Element;
  operation: string;
}

function CardButtonRow(props : CardButtonRowProps) {
  return (
    <nav>    
      <ul>
        {props.children}
      </ul>
    </nav>

  );
}

function CardButton(props : CardButtonProps) {
  const [open, setOpen] = useState<boolean>(false);
  return (
    <li className={styles.cardbutton}>
      <a className={styles.iconbutton} href="#" onClick={() => setOpen(!open)}>
        {props.icon}
      </a>
      {open && props.children}
    </li>
  );
}

function DropdownMenu(props : DropdownProps) {
  function DropdownItem(props : DropdownItemProps) {
    return (
      <li>
        <a href="#">{props.icon} {props.operation}</a>
      </li>
    );
  }
  return (
    <div className={styles.dropdownmenu}>
      {props.items.map((item, i) => <DropdownItem key={i} icon={item.icon} operation={item.operation} />)}
    </div>
  );
}