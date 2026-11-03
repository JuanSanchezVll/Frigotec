import { useApp } from '../../../entities/estado';
import { cliente, tecnico } from '../../../entities/usuario';
import { AppHeader } from './AppHeader';
export const HeaderCliente = ({ onMenu }) => {
    const { usuario, sair } = useApp();
    return <AppHeader logoTo="/home" nome={usuario?.nomeCompleto ?? cliente.nome} onMenu={onMenu} onSair={sair}/>;
};
export const HeaderTecnico = ({ onMenu }) => {
    const { usuario, sair } = useApp();
    return <AppHeader logoTo="/tecnico" nome={usuario?.nomeCompleto ?? tecnico.nome} papel="Técnico" onMenu={onMenu} onSair={sair}/>;
};
export const HeaderAdmin = ({ onMenu }) => {
    const { usuario, sair } = useApp();
    return <AppHeader logoTo="/admin" nome={usuario?.nomeCompleto ?? 'Administrador'} papel="Administrador" onMenu={onMenu} onSair={sair}/>;
};
