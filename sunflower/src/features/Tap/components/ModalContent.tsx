import { useContext, useState } from "react";
import StandardButton from "../../../components/Button/StandardButton";
import { TapContext } from "../../../contexts/Context";
import { BatchUnit } from "../../Batch/types";
import { Tap } from "../types";
import DropdownMenu from "./DropdownMenu";
import styles from '../../../app.module.css';

export default function ModalContent (props: Readonly<ModalProps>) {
    const batchUnits = props.batchUnits;
    const tap = useContext(TapContext);
    // TODO: replace with id and search for tap
    const [activeItem, setActiveItem] = useState<Tap | null>(tap);
    const [menuOpen, setMenuOpen] = useState<boolean>(false);
    const update = () => {
      props.setOpen(false);
    };
    return (
        <div className={styles.modal}>
          <div className={styles.modalcontent}>
            <DropdownMenu item={activeItem} items={batchUnits} menuOpen={menuOpen} setMenuOpen={setMenuOpen} setItem={setActiveItem}/>
            <div className={styles.last}>
            <StandardButton onClick={() => props.setOpen(false)} text="Save" />
            <StandardButton onClick={() => props.setOpen(false)} text="Close" />
            </div>
          </div>
        </div>
    );
}

export interface ModalProps {
  open: boolean;
  setOpen: Function;
  batchUnits: BatchUnit[];
}