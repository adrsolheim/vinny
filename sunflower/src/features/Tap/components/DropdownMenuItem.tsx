
export default function DropdownItem(props : Readonly<DropdownItemProps>) {
  return (
    <li>
      <a href="#">{props.icon} {props.operation}</a>
    </li>
  );
}

export interface DropdownItemProps {
  icon: JSX.Element;
  operation: string;
}