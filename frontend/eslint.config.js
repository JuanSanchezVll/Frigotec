import js from '@eslint/js'
import globals from 'globals'
import reactHooks from 'eslint-plugin-react-hooks'
import reactRefresh from 'eslint-plugin-react-refresh'
import { defineConfig, globalIgnores } from 'eslint/config'

// Regra do FSD: uma camada só importa de camadas abaixo dela.
// app > pages > widgets > features > entities > shared
const camadasAcima = (...camadas) => ({
  patterns: camadas.map((camada) => ({
    group: [`**/${camada}`, `**/${camada}/**`],
    message: `Violação do FSD: esta camada não pode importar de "${camada}".`,
  })),
})

const regraDeCamada = (pasta, proibidas) => ({
  files: [`src/${pasta}/**/*.{js,jsx}`],
  rules: { 'no-restricted-imports': ['error', camadasAcima(...proibidas)] },
})

export default defineConfig([
  globalIgnores(['dist', 'docs']),
  {
    files: ['**/*.{js,jsx}'],
    extends: [
      js.configs.recommended,
      reactHooks.configs.flat.recommended,
      reactRefresh.configs.vite,
    ],
    languageOptions: {
      globals: globals.browser,
      parserOptions: { ecmaFeatures: { jsx: true } },
    },
  },
  {
    files: ['vite.config.js'],
    languageOptions: { globals: globals.node },
  },
  {
    // Regras novas e estritas do React: o código vindo do Figma Make funciona, mas viola algumas.
    // Ficam como aviso até refatorarmos tela a tela.
    files: ['**/*.{js,jsx}'],
    rules: {
      'react-hooks/set-state-in-effect': 'warn',
      'react-hooks/refs': 'warn',
      'react-refresh/only-export-components': 'warn',
    },
  },
  regraDeCamada('shared', ['entities', 'features', 'widgets', 'pages', 'app']),
  regraDeCamada('entities', ['features', 'widgets', 'pages', 'app']),
  regraDeCamada('features', ['widgets', 'pages', 'app']),
  regraDeCamada('widgets', ['pages', 'app']),
  regraDeCamada('pages', ['app']),
])
