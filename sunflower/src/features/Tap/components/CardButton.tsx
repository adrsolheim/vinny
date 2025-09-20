import { useState } from "react";
import styles from '../../../app.module.css';

export default function CardButton(props : Readonly<CardButtonProps>) {
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

export interface CardButtonProps {
  // this should be React.ReactElement?
  icon: JSX.Element;
  children?: React.ReactNode;
  handleUpdateTap: Function;
}