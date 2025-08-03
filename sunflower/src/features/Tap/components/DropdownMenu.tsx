import DropdownItem from "./DropdownMenuItem";
import styles from '../../../app.module.css';

export default function DropdownMenu(props : Readonly<DropdownProps>) {
  return (
    <div className={styles.dropdownmenu}>
      {props.items.map((item, i) => <DropdownItem key={i} icon={item.icon} operation={item.operation} />)}
    </div>
  );
}

export interface DropdownProps {
  items: DropdownItemProps[];
}

export interface DropdownItemProps {
  icon: JSX.Element;
  operation: string;
}
