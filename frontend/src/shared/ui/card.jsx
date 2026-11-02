export function Card({ className = '', ...r }) {
    return <div className={`rounded-lg border border-borda bg-fundo p-5 shadow-1 md:p-7 ${className}`} {...r}/>;
}
