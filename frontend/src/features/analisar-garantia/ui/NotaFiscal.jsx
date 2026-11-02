/** Nota fiscal de exemplo (documento fictício desenhado em HTML). */
export function NotaFiscal({ zoom = 1 }) {
    return (<div className="overflow-auto rounded-md border border-borda bg-superficie p-3" style={{ maxHeight: 360 }}>
      <div style={{ transform: `scale(${zoom})`, transformOrigin: 'top left', width: `${100 / zoom}%` }} className="transition-transform duration-300">
        <div className="rounded-sm bg-fundo p-5 shadow-2 texto-legenda">
          <div className="flex items-start justify-between border-b border-borda pb-3"><div><p className="text-sm font-bold">DISTRIBUIDORA EXEMPLO LTDA.</p><p className="text-texto-secundario">CNPJ 00.000.000/0001-00</p></div><div className="text-right"><p className="font-bold">NOTA FISCAL</p><p className="text-texto-secundario">Nº 004512 · 25/08/2025</p></div></div>
          <p className="mt-3 text-texto-secundario">Destinatário: Restaurante Sabor &amp; Brasa Ltda. · CNPJ 12.345.678/0001-90</p>
          <table className="mt-3 w-full text-left"><thead className="bg-superficie"><tr><th className="p-1.5">Descrição</th><th className="p-1.5">Qtd</th><th className="p-1.5 text-right">Valor</th></tr></thead>
            <tbody><tr className="border-t border-borda"><td className="p-1.5">Chapa Frigopro · N/S FP-2026-004512</td><td className="p-1.5">1</td><td className="p-1.5 text-right">R$ 8.900,00</td></tr></tbody></table>
          <p className="mt-3 text-right text-sm font-bold">Total: R$ 8.900,00</p>
        </div>
      </div>
    </div>);
}
