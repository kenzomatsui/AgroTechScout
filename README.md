# AgroTechScout

**Observação de partidas transformada em dados para a equipe.**

Protótipo Android de scouting para a FIRST Tech Challenge. O código usa o nome interno **FTCScout** e organiza eventos, equipes, partidas e registros de desempenho para consulta e análise.

## O que está no projeto

- Telas de eventos, equipes e detalhes de partidas.
- Formulários de scouting com registros de autônomo, teleop e endgame.
- Campos para pontuação, penalidades, observações e cooperação.
- Persistência local com Room e telas de análise.

O modelo atual contém campos de pixels, backdrop, drone e hanging, além de comentários que identificam exemplos de pontuação. **Esses campos precisam ser ajustados à temporada usada pela equipe** antes de servir como referência em uma competição.

## Explorar o código

A implementação principal está em [`app/src/main/java/com/example/ftcscout`](app/src/main/java/com/example/ftcscout).

| Pasta | Conteúdo |
| :--- | :--- |
| `data/entities/` | Modelos de eventos, equipes, partidas e scouting. |
| `data/dao/` e `data/repository/` | Acesso aos dados locais. |
| `ui/screens/` | Telas do aplicativo. |
| `ui/viewmodels/` | Estado e lógica de apresentação. |
| `ui/navigation/` | Navegação entre telas. |

O diretório `com/example/agroscout` também está presente como parte da estrutura anterior; o manifesto atual aponta para `com.example.ftcscout`.

## Abrir no Android Studio

```sh
git clone https://github.com/kenzomatsui/AgroTechScout.git
```

Abra a pasta no Android Studio, configure **JDK 17** e **Android SDK 35**, sincronize o Gradle e selecione o módulo `app`. O Android mínimo configurado é **7.0 / API 24**.

O projeto está em desenvolvimento. A documentação descreve a implementação disponível; não há uma versão de produção validada indicada aqui.

**Stack:** Kotlin · Jetpack Compose · Material 3 · Room · ViewModel · Navigation Compose
