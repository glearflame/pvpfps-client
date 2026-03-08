# PVP FPS Client (Fabric 1.21.8)

Cliente custom para Minecraft Java Edition `1.21.8`, focado em FPS alto e PvP.

## Base

- Minecraft `1.21.8`
- Fabric Loader `0.18.4`
- Java `17+` (recomendado Java 21)
- Fabric API `0.136.1+1.21.8`

## Objetivo

Projeto leve estilo PvP client (Lunar/Feather-like), mas aberto, modular e com foco total em:

- desempenho (FPS, uso de RAM, menos stutter)
- HUD competitivo para PvP
- utilitarios de combate e render

## Arquitetura

```text
client/
 ├ modules/
 │   ├ combat/
 │   ├ render/
 │   ├ hud/
 │   └ misc/
 ├ gui/
 ├ config/
 ├ events/
 └ utils/
```

Pacote Java:
`src/client/java/com/pvpfps/client/...`

## Modulos implementados

### Combat
- ToggleSprint
- ToggleSneak
- CustomCrosshair
- HitColor
- DamageIndicator
- Zoom (tecla `C`)

### Render
- Fullbright
- ClearWater (equivalente via opcoes graficas)
- LowFire (modo para uso com low-fire resource pack)
- ReducedParticles
- BlockOverlay

### HUD
- FPSCounter
- CPSCounter
- PingDisplay
- Keystrokes
- ArmorHUD
- PotionEffectsHUD

### Performance
- PerformanceProfile (presets: `MAX_FPS`, `BALANCED`, `QUALITY`)

### Misc
- ProfileHotkeys (`F6` salvar perfil, `F7` carregar)

## GUI

Click GUI moderna simples:

- categorias: Combat, Render, HUD, Performance, Misc
- toggle de cada modulo
- ajuste de posicao HUD (`X/Y`) direto na GUI

Tecla padrao: `Right Shift`

## Sistema de configuracao

Arquivos em:
`config/pvpfps-client/`

- estado de modulos (on/off)
- keybinds
- settings
- posicoes de HUD
- perfis (`profiles/*.json`)

## Stack de mods de performance (externa)

O projeto inclui o script:

`./scripts/install-performance-mods.ps1`

Ele baixa automaticamente mods compativeis com `1.21.8 + fabric` (Modrinth):

- Fabric API
- Sodium
- Lithium
- FerriteCore
- Entity Culling
- More Culling
- ImmediatelyFast
- ModernFix
- C2ME
- Memory Leak Fix
- Debugify

Opcional:

- Sodium Extra
- Reese's Sodium Options
- Iris
- Zoomify
- Cull Leaves
- LambDynamicLights
- BetterF3
- Continuity

### Uso do script

```powershell
./scripts/install-performance-mods.ps1
```

Com opcionais:

```powershell
./scripts/install-performance-mods.ps1 -IncludeOptional
```

Fallback de versao (somente se voce quiser tentar versoes proximas):

```powershell
./scripts/install-performance-mods.ps1 -AllowVersionFallback
```

## Resource packs PvP

Use a pasta `resourcepacks/` para packs 16x/32x (low fire, short swords, clean GUI).

## Build

```powershell
./gradlew.bat build
```

## Executar em dev

```powershell
./gradlew.bat runClient
```

## Observacao tecnica

Otimizacoes como Sodium/Lithium/C2ME/ModernFix sao mods separados e devem rodar junto com este cliente para atingir FPS maximo em PCs fracos.
