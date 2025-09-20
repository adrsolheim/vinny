import { useState, useEffect } from "react";
import { BatchUnit } from "../../Batch/types";
import { CardButtonProps } from "./CardButton";
import TapModal from "./TapModal";
import styles from '../../../app.module.css';
import { fetchAvailableBatchUnits } from "../../Batch/api";

export default function ModalButton(props : Readonly<CardButtonProps>) {
  const [open, setOpen] = useState<boolean>(false);
  const [batchUnits, setBatchUnits] = useState<BatchUnit[]>([]); 
  useEffect(() => {
    const getBatchUnits = async () =>{
      const units = await fetchAvailableBatchUnits();
      setBatchUnits(units);
    };
    getBatchUnits();
  }, []);
  return (
    <li className={styles.cardbutton}>
      <a className={styles.iconbutton} href="#" onClick={() => setOpen(!open)}>
        {props.icon}
      </a>
      {open && <TapModal handleUpdateTap={props.handleUpdateTap} batchUnits={batchUnits} open={open} setOpen={setOpen} />}
    </li>
  );
}