/** Curvas e durações do sistema de animação. Usadas por todos os componentes. */
export const EASE = [0.22, 1, 0.36, 1];
export const DUR = { rapido: 0.15, base: 0.22, passo: 0.3, secao: 0.5, linha: 1.2, contador: 0.9, rota: 0.2 };
export const reduzido = () => typeof window !== 'undefined' && window.matchMedia('(prefers-reduced-motion: reduce)').matches;
export const subir = (delay = 0) => ({
    initial: { opacity: 0, y: 24 },
    whileInView: { opacity: 1, y: 0 },
    viewport: { once: true, amount: 0.15 },
    transition: { duration: DUR.secao, ease: EASE, delay },
});
export const rota = {
    initial: { opacity: 0, y: 8 },
    animate: { opacity: 1, y: 0 },
    exit: { opacity: 0 },
    transition: { duration: DUR.rota, ease: EASE },
};
