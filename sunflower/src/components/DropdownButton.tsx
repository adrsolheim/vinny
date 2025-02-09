import { BaselineAddCircleOutline } from '../assets/BaselineAddCircleOutline';
import { FaChevronDown, FaChevronUp } from 'react-icons/fa';
import styles from '../app.module.css';


export default function DropdownButton(props: DropdownButtonProps) {
    const setMenuOpen: Function = props.setMenuOpen;
    const menuOpen: boolean = props.menuOpen;
    return (
        <div className={styles.dropdownmenubutton} onClick={() => setMenuOpen(!menuOpen)}>
            <span>{props.buttonText}</span><span className={styles.dropmenuicon}>{menuOpen ?<FaChevronUp fill='white' /> : <FaChevronDown fill='white'/>}</span>
        </div>
    );
}

interface DropdownButtonProps {
    buttonText: string;
    setMenuOpen: Function;
    menuOpen: boolean;
}