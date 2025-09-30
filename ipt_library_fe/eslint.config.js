import eslintPluginPrettier from 'eslint-plugin-prettier';
import eslintPluginReact from 'eslint-plugin-react';
import eslintPluginReactHooks from 'eslint-plugin-react-hooks';
import configPrettier from 'eslint-config-prettier';

export default [
  {
    ignores: ['src/reportWebVitals.js', 'src/setupTests.js'],
  },
  {
    files: ['src/**/*.{js,jsx}'],
    languageOptions: {
      ecmaVersion: 'latest',
      sourceType: 'module',
      parserOptions: {
        ecmaFeatures: {
          jsx: true,
        },
      },
      globals: {
        window: true,
        document: true,
        console: true,
      },
    },
    plugins: {
      prettier: eslintPluginPrettier,
      react: eslintPluginReact,
      'react-hooks': eslintPluginReactHooks,
    },
    rules: {
      // PRETTIER RULES
      'prettier/prettier': [
        'warn',
        {
          arrowParens: 'always',
          bracketSameLine: false,
          objectWrap: 'preserve',
          bracketSpacing: true,
          semi: true,
          experimentalOperatorPosition: 'end',
          experimentalTernaries: false,
          singleQuote: true,
          jsxSingleQuote: false,
          quoteProps: 'as-needed',
          trailingComma: 'all',
          singleAttributePerLine: false,
          htmlWhitespaceSensitivity: 'css',
          vueIndentScriptAndStyle: false,
          proseWrap: 'preserve',
          insertPragma: false,
          requirePragma: false,
          tabWidth: 2,
          useTabs: false,
          embeddedLanguageFormatting: 'auto',
          printWidth: 120,
        },
      ],

      // REACT RULES
      'react/react-in-jsx-scope': 'off', // Vite không cần import React
      'react/prop-types': 'off', // nếu không dùng PropTypes

      // REACT HOOKS RULES (nên giữ)
      'react-hooks/rules-of-hooks': 'error',
      'react-hooks/exhaustive-deps': 'warn',
    },
    settings: {
      react: {
        version: 'detect',
      },
    },
  },
  configPrettier,
];
