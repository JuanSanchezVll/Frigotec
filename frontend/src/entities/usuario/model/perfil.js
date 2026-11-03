// Perfis do usuário (iguais ao enum Perfil do backend) e a tela inicial de cada um.
export const PERFIL = {
    CLIENTE: "CLIENTE",
    TECNICO: "TECNICO",
    ADMIN: "ADMIN",
}

// Quem faz parte da equipe Frigotec (usa as telas do técnico). O ADMIN faz tudo do técnico (SDD seção 3).
export const PERFIS_DA_EQUIPE = [PERFIL.TECNICO, PERFIL.ADMIN]

export const ehDaEquipe = (perfil) => PERFIS_DA_EQUIPE.includes(perfil)

export const ehAdmin = (perfil) => perfil === PERFIL.ADMIN

// Depois do login: cliente -> /home; técnico -> /tecnico; administrador -> /admin.
export function rotaInicial(perfil) {
    if (ehAdmin(perfil)) return "/admin"
    return ehDaEquipe(perfil) ? "/tecnico" : "/home"
}
