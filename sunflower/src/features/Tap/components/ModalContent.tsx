import { useContext, useState } from "react";
import StandardButton from "../../../components/Button/StandardButton";
import { TapContext } from "../../../contexts/Context";
import { ModalProps, Tap } from "../types";
import styles from '../../../app.module.css';
import DropdownMenu from "../../../components/Dropdown/DropdownMenu";

export default function ModalContent (props: Readonly<ModalProps>) {
    const batchUnits = props.batchUnits;
    const tap = useContext(TapContext);
    // TODO: replace with id and search for tap
    const [activeItem, setActiveItem] = useState<Tap | { id: 0, active: false, batchUnit: undefined }>(tap);
    const [menuOpen, setMenuOpen] = useState<boolean>(false);
    return (
        <div className={styles.modal}>
          <div className={styles.modalcontent}>
            <DropdownMenu 
              item={activeItem} 
              items={batchUnits} 
              menuOpen={menuOpen} 
              setMenuOpen={setMenuOpen} 
              setItem={setActiveItem}
              handleUpdateTap={props.handleUpdateTap}
            />
            <div className={styles.last}>
            <StandardButton onClick={() => props.setOpen(false)} text="Save" />
            <StandardButton onClick={() => props.setOpen(false)} text="Close" />
            </div>
          </div>
        </div>
    );
}
