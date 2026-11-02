const tons = ['bg-primaria text-fundo', 'bg-primaria-clara text-primaria-escura', 'bg-secundaria text-fundo', 'bg-fundo-brasa text-texto-brasa'];
export function Avatar({ nome, tamanho = 40, tom = 0 }) {
    const ini = nome.split(' ').filter(Boolean).slice(0, 2).map((p) => p[0]).join('').toUpperCase();
    return <span aria-hidden style={{ width: tamanho, height: tamanho, fontSize: tamanho * 0.36 }} className={`inline-flex shrink-0 items-center justify-center rounded-full font-semibold ${tons[tom % tons.length]}`}>{ini}</span>;
}
