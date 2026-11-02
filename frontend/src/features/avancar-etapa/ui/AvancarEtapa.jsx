import { ChevronsRight } from 'lucide-react';
import { useApp } from '../../../entities/estado';
import { Button } from '../../../shared/ui';
/** Leva a linha do tempo para a próxima etapa (a animação roda na própria linha do tempo). */
export function AvancarEtapa({ id }) {
    const { avancar } = useApp();
    return <Button variante="contorno" onClick={() => avancar(id)}><ChevronsRight size={18}/>Avançar etapa</Button>;
}
