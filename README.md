# Plug-Get

Plug-Get is a simple Linux like package manager for minecraft plugins. It allows you to easily search, install and update plugins from Modrinth without leaving ever leaving the game or your server console. It supports both apt and pacman syntax

## Features

- Search for plugins directly from Modrinth API
  ![Search Example](https://github.com/PinkCandyDev/Plug-Get/blob/main/search.gif)
- One-command plugin installation with automatic dependency resolution
  ![Install Example](https://github.com/PinkCandyDev/Plug-Get/blob/main/install.gif)
- Update all plugins with a single command
  ![update Example](https://github.com/PinkCandyDev/Plug-Get/blob/main/update~~~~.gif)
- Remove plugins with optional dependency cleanup
- Version control (latest, rolling, beta, alpha, specific versions)
- Compatible with folia, paper, spigot

## Commands & Flags

### All Commands
List of commands curently avalible
| Command | Short | Description |
|---------|-------|-------------|
| `/pg help` | `/pg -h` | Show help menu |
| `/pg search <slug>` | `/pg -Ss <slug>` | Search for plugins |
| `/pg install <slug>` | `/pg -S <slug>` | Install a plugin |
| `/pg update` | `/pg -Syu` | Update all installed plugins |
| `/pg list` | `/pg -Qs` | List all installed plugins |
| `/pg versions <slug>` | `/pg -Vs <slug>` | Show versions of chosen plugin |
| `/pg remove <slug>` | `/pg -R <slug>` | Remove a plugin |
| `/pg autoremove <slug>` | `/pg -Rs <slug>` | Remove a plugin with its dependencies|
| `/pg reload` | `/pg -rl` | `Reload configuration file|

### Confirmation Commands

| Command | Short | Description |
|---------|-------|-------------|
| `/pg y` | `/pg -y` | Confirm pending action |
| `/pg n` | `/pg -n` | Cancel pending action |
| `/pg release` | `/pg -Alr` | Release action lock if stuck |

### Installation Flags

Use these flags with `/pg install <slug> [FLAG]`:

| Flag | Description |
|------|--------|
| `--latest` | Install latest and most stable version |
| `--rolling` | Install always newest version|
| `--beta` | Install from beta channel |
| `--alpha` | Install from alpha channel |
| `--v <version>` | Install specific version (e.g., `--v 7.2.15`) |
| `--vl <version>` | Install specific version as latest type |
| `--vr <version>` | Install specific version as rolling type |