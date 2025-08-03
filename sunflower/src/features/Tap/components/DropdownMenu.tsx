import styles from '../../../app.module.css';
import { BatchUnit } from "../../Batch/types";
import { Tap } from "../types";
import DropdownItem from './DropdownMenuItem';

export default function DropdownMenu(props : Readonly<DropdownProps>) {
  return (
    <div className={styles.dropdownmenu}>
      {props.items.map((item, i) => <DropdownItem key={i} icon={item.icon} operation={item.operation} />)}
    </div>
  );
}

export interface DropdownProps {
    menuOpen: boolean;
    setMenuOpen: Function;
    item: Tap;
    items: BatchUnit[];
    setItem: Function;
}