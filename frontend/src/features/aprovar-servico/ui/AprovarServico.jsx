import { ThumbsUp } from 'lucide-react';
import { useApp } from '../../../entities/estado';
import { Button } from '../../../shared/ui';

/** O cliente aprova o serviço proposto (CONTATO_DO_TECNICO -> APROVACAO_DO_SERVICO, SDD RN-CHM-11). */
export function AprovarServico({ id }) {
    const { aprovarServico } = useApp();
    return <Button onClick={() => aprovarServico(id)}><ThumbsUp size={18}/>Aprovar serviço</Button>;
}
