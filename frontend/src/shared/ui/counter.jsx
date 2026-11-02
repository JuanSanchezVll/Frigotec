import { animate, useInView, useReducedMotion } from 'motion/react';
import { useEffect, useRef, useState } from 'react';
import { DUR } from '../lib/motion';
/** Número que sobe de 0 até o valor (900 ms). */
export function Counter({ valor }) {
    const ref = useRef(null);
    const vis = useInView(ref, { once: true });
    const r = useReducedMotion();
    const [n, setN] = useState(r ? valor : 0);
    useEffect(() => {
        if (!vis)
            return;
        if (r) {
            setN(valor);
            return;
        }
        const c = animate(0, valor, { duration: DUR.contador, ease: 'easeOut', onUpdate: (v) => setN(Math.round(v)) });
        return () => c.stop();
    }, [vis, valor, r]);
    return <span ref={ref} className="num">{n}</span>;
}
