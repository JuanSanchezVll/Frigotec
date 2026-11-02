import { motion, useReducedMotion } from 'motion/react';
import { subir } from './motion';
/** Entrada de seção: sobe 24 px e aparece uma única vez. `ordem` cria a sequência (80 ms por item). */
export function Reveal({ children, className = '', delay = 0, ordem }) {
    const r = useReducedMotion();
    const atraso = delay / 1000 + (ordem ?? 0) * 0.08;
    if (r)
        return <div className={className}>{children}</div>;
    return <motion.div className={className} {...subir(atraso)}>{children}</motion.div>;
}
