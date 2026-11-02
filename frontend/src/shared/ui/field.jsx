import { CalendarDays, Check, ChevronDown } from 'lucide-react';
import { useEffect, useId, useRef, useState } from 'react';
export function Field({ label, hint, optional, htmlFor, children }) {
    return (<div>
      <label htmlFor={htmlFor} className="mb-2 block texto-rotulo text-texto">
        {label}
        {optional && <span className="ml-1 font-normal text-texto-secundario">(opcional)</span>}
      </label>
      {children}
      {hint && <p className="mt-2 texto-pequeno text-texto-secundario">{hint}</p>}
    </div>);
}
export function TextInput({ label, hint, optional, ...rest }) {
    const id = useId();
    return (<Field label={label} hint={hint} optional={optional} htmlFor={id}>
      <input id={id} className="campo" {...rest}/>
    </Field>);
}
export function TextArea({ label, hint, ...rest }) {
    const id = useId();
    return (<Field label={label} hint={hint} htmlFor={id}>
      <textarea id={id} rows={5} className="campo min-h-[120px] resize-y" {...rest}/>
    </Field>);
}
/** Campo de digitar ou escolher. Aceita qualquer texto. */
export function Combo({ label, options, value, onChange, placeholder, hint, soLista = false }) {
    const id = useId();
    const [aberto, setAberto] = useState(false);
    const ref = useRef(null);
    useEffect(() => {
        const f = (e) => { if (!ref.current?.contains(e.target))
            setAberto(false); };
        document.addEventListener('mousedown', f);
        return () => document.removeEventListener('mousedown', f);
    }, []);
    const lista = soLista || !value ? options : options.filter((o) => o.toLowerCase().includes(value.toLowerCase()));
    return (<Field label={label} hint={hint} htmlFor={id}>
      <div ref={ref} className="relative">
        <input id={id} role="combobox" aria-expanded={aberto} aria-controls={`${id}-l`} className={`campo pr-11 ${soLista ? 'cursor-pointer caret-transparent' : ''}`} value={value} placeholder={placeholder} readOnly={soLista} autoComplete="off" onChange={(e) => { onChange(e.target.value); setAberto(true); }} onClick={() => setAberto((a) => !a)} onKeyDown={(e) => { if (e.key === 'Escape')
        setAberto(false); }}/>
        <ChevronDown aria-hidden size={20} className={`pointer-events-none absolute right-4 top-1/2 -translate-y-1/2 text-texto-secundario transition-transform ${aberto ? 'rotate-180' : ''}`}/>
        {aberto && lista.length > 0 && (<ul id={`${id}-l`} role="listbox" className="anim-pop absolute left-0 right-0 top-full z-30 mt-2 max-h-64 overflow-auto rounded-md border border-borda bg-fundo p-1 shadow-2">
            {lista.map((o) => (<li key={o} role="option" aria-selected={o === value} onMouseDown={(e) => { e.preventDefault(); onChange(o); setAberto(false); }} className={`flex min-h-11 cursor-pointer items-center justify-between rounded-sm px-3 hover:bg-primaria-clara ${o === value ? 'font-semibold text-primaria' : ''}`}>
                {o}{o === value && <Check size={18} aria-hidden/>}
              </li>))}
          </ul>)}
      </div>
    </Field>);
}
/** Campo de data com calendário. */
export function DateInput({ label, value, onChange, hint }) {
    const id = useId();
    const nativo = useRef(null);
    return (<Field label={label} hint={hint} htmlFor={id}>
      <div className="relative">
        <input id={id} className="campo pr-12" placeholder="dd/mm/aaaa" value={value} onChange={(e) => onChange(e.target.value)}/>
        <button type="button" aria-label="Abrir calendário" onClick={() => nativo.current?.showPicker?.()} className="absolute right-0 top-0 flex h-12 w-12 items-center justify-center text-texto-secundario hover:text-primaria">
          <CalendarDays size={20}/>
        </button>
        <input ref={nativo} type="date" tabIndex={-1} aria-hidden className="pointer-events-none absolute bottom-0 right-0 h-0 w-0 opacity-0" onChange={(e) => { const [a, m, d] = e.target.value.split('-'); if (a)
        onChange(`${d}/${m}/${a}`); }}/>
      </div>
    </Field>);
}
/** Campo com rótulo flutuante e ícone (login e cadastro). */
export function FloatField({ label, icon: Icon, ...rest }) {
    const id = useId();
    return (<div className="relative">
      <Icon aria-hidden size={20} className="pointer-events-none absolute left-4 top-1/2 -translate-y-1/2 text-texto-secundario"/>
      <input id={id} placeholder=" " className="campo peer h-14 pb-1 pl-12 pt-5" {...rest}/>
      <label htmlFor={id} className="pointer-events-none absolute left-12 top-1/2 origin-left -translate-y-1/2 text-texto-secundario transition-all duration-150 peer-focus:top-4 peer-focus:scale-75 peer-focus:font-semibold peer-focus:text-primaria peer-[:not(:placeholder-shown)]:top-4 peer-[:not(:placeholder-shown)]:scale-75">{label}</label>
    </div>);
}
