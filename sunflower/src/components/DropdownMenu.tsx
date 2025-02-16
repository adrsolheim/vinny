import { useState } from "react";
import DropdownButton from "./DropdownButton";
import DropdownContent from "./DropdownContent";
import styles from '../app.module.css';
import BatchUnit from "../types/batchUnit";

export default function DropdownMenu(props: DropdownProps) {
    const [menuOpen, setMenuOpen] = useState<boolean>(false);
    return(
        <div className={styles.dropdownMenu}>
            <DropdownButton menuOpen={menuOpen} setMenuOpen={setMenuOpen} buttonText={props.item}/>
            {menuOpen && <DropdownContent items={props.items} setItem={props.setItem} setMenuOpen={setMenuOpen}/>}
        </div>
    );
}


interface DropdownProps {
    menuOpen: boolean;
    setMenuOpen: Function;
    item: string;
    items: BatchUnit[];
    setItem: Function;
}
