import { UserCheck } from 'lucide-react';
import { useApp } from '../../../entities/estado';
import { Button } from '../../../shared/ui';
export function AtribuirButton({ id }) {
    const { atribuir } = useApp();
    return <Button variante="contorno" tamanho="sm" onClick={() => atribuir(id)}><UserCheck size={16}/>Atribuir a mim</Button>;
}
