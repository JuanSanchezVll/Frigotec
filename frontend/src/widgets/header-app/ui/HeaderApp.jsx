import { cliente, tecnico } from '../../../entities/usuario';
import { AppHeader } from './AppHeader';
export const HeaderCliente = ({ onMenu }) => <AppHeader logoTo="/home" nome={cliente.nome} onMenu={onMenu}/>;
export const HeaderTecnico = ({ onMenu }) => <AppHeader logoTo="/tecnico" nome={tecnico.nome} papel="Técnico" onMenu={onMenu}/>;
