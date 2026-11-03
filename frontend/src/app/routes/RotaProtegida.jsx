import { Navigate, Outlet } from 'react-router-dom';
import { useApp } from '../../entities/estado';
import { rotaInicial } from '../../entities/usuario';

/**
 * Guarda de rota (camada app): só deixa entrar quem está logado e tem um dos perfis permitidos.
 * - sem login: volta para /login;
 * - perfil errado (ex.: cliente abrindo /tecnico): vai para a home do próprio perfil.
 * Isto é só experiência de uso: a segurança de verdade é do backend (token + perfil, SDD RN-USR-06).
 */
export function RotaProtegida({ perfis }) {
    const { usuario } = useApp();
    if (!usuario) return <Navigate to="/login" replace/>;
    if (!perfis.includes(usuario.perfil)) return <Navigate to={rotaInicial(usuario.perfil)} replace/>;
    return <Outlet />;
}
