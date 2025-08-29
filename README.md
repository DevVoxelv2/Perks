# 🚀 NitroPerks v2.0.0

**Advanced Minecraft Perk System with GUI, Database Support & Admin Tools**

[![Minecraft Version](https://img.shields.io/badge/Minecraft-1.20--1.21.8-brightgreen.svg)](https://minecraft.net)
[![Java Version](https://img.shields.io/badge/Java-17+-orange.svg)](https://adoptium.net)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

## 📋 Overview

NitroPerks is a comprehensive perk management system for Minecraft servers, featuring an intuitive GUI, robust database integration, and powerful admin tools. Perfect for survival, economy, and custom game mode servers.

## ✨ Key Features

### 🎯 **Core Features**
- **14 Built-in Perks** - Telekinesis, Glow, No Hunger, Fly, Speed, and more
- **Categorized System** - Combat, Utility, Movement, Survival, Mining categories
- **Economy Integration** - Full Vault support for perk purchases
- **Database Persistence** - MySQL support with async operations
- **Admin Tools** - Comprehensive GUI-based administration

### 🎨 **Advanced GUI System**
- **Category-based Navigation** - Organized, intuitive menus
- **Visual Status Indicators** - Clear perk ownership and activity status
- **Price Display** - Formatted pricing with affordability indicators
- **Admin Panel** - Complete server management through GUI

### 🎭 **Effects & Feedback**
- **Visual Effects** - Particle effects for perk activation/deactivation
- **Sound System** - Audio feedback for all interactions
- **Title Messages** - Large-screen notifications for important events
- **Action Bar Updates** - Real-time status updates

### 🔧 **Version Compatibility**

#### ✅ **Fully Supported Versions**
- **Minecraft 1.20.x** - All subversions
- **Minecraft 1.21.x** - Versions 1.21.0 through 1.21.8

#### 🛠️ **Technical Requirements**
- **Java 17+** - Required for compilation and runtime
- **Spigot/Paper** - Built on Spigot API with Paper optimizations
- **Vault** - Required dependency for economy features
- **MySQL** - Optional but recommended for data persistence

#### 🔄 **Automatic Compatibility**
The plugin automatically detects your server version and:
- ✅ Uses compatible sounds, particles, and materials
- ✅ Gracefully handles API differences between versions
- ✅ Provides fallbacks for older/newer versions
- ✅ Logs compatibility information on startup

## 🚀 Installation

### Prerequisites
1. **Minecraft Server** running version 1.20 - 1.21.8
2. **Java 17+** installed on your server
3. **Vault** plugin installed
4. **Economy plugin** (EssentialsX, CMI, etc.)
5. **MySQL database** (optional but recommended)

### Quick Setup
1. Download the latest `NitroPerks-2.0.0.jar`
2. Place in your server's `plugins/` directory
3. Start/restart your server
4. Configure database settings in `config.yml`
5. Customize messages and menus in respective files
6. Restart server to apply changes

### Configuration Files
- `config.yml` - Main configuration, database, and perk prices
- `messages.yml` - All text messages, sounds, and particle settings
- `menus.yml` - GUI layouts and templates

## 🎮 Usage

### Player Commands
```
/perks              - Open main perk menu
/perkshop          - Access perk shop (redirects to main menu)
/perk              - Alias for /perks
/nitroperks        - Full plugin name command
/np                - Short alias
```

### Admin Commands
```
/perks admin                    - Open admin panel
/perks add <player> <perk>      - Give perk to player
/perks remove <player> <perk>   - Remove perk from player
/perks setprice <perk> <price>  - Set perk price
/perks reload                   - Reload configuration
```

### Permissions
```yaml
# Basic usage
nitroperks.use                  # Access to menus and owned perks

# Admin permissions
nitroperks.admin               # Full admin access
nitroperks.admin.gui          # Admin GUI access
nitroperks.admin.setprice     # Price modification
nitroperks.admin.give         # Give perks to players
nitroperks.admin.remove       # Remove perks from players
nitroperks.admin.reload       # Reload configuration

# Individual perk permissions
nitroperks.perks.*            # All perks
nitroperks.perks.telekinesis  # Specific perk access
nitroperks.perks.fly          # Specific perk access
# ... (see plugin.yml for complete list)
```

## 🔧 Available Perks

### 🥊 Combat Perks
Currently no combat-specific perks (expandable)

### 🛠️ Utility Perks
- **Telekinesis** - Auto-collect dropped items
- **Glow** - Permanent glowing effect
- **Night Vision** - See in the dark

### 🏃 Movement Perks
- **Speed** - Increased movement speed
- **Jump** - Higher jumping ability
- **Fly** - Creative-like flight in survival

### 🛡️ Survival Perks
- **No Hunger** - Prevents hunger depletion
- **Lava Resistance** - Immunity to fire/lava damage
- **No Fall** - Prevents fall damage
- **Water Breath** - Unlimited underwater breathing
- **Keep XP** - Retain experience on death
- **Keep Inventory** - Retain items on death

### ⛏️ Mining Perks
- **Haste** - Faster block breaking
- **Instant Smelt** - Auto-smelt mined ores

## 🗄️ Database Schema

### Tables Created
```sql
-- Player perk ownership and status
player_perks (
    id INT AUTO_INCREMENT PRIMARY KEY,
    player_uuid VARCHAR(36) NOT NULL,
    perk_key VARCHAR(50) NOT NULL,
    unlocked BOOLEAN DEFAULT TRUE,
    active BOOLEAN DEFAULT FALSE,
    unlock_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    level INT DEFAULT 1,
    usage_count BIGINT DEFAULT 0
)

-- Player statistics and tracking
player_stats (
    id INT AUTO_INCREMENT PRIMARY KEY,
    player_uuid VARCHAR(36) NOT NULL,
    total_perks_owned INT DEFAULT 0,
    total_money_spent DECIMAL(15,2) DEFAULT 0.00,
    join_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_seen TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)
```

## 🔧 Configuration

### Basic Config (config.yml)
```yaml
mysql:
  host: localhost
  port: 3306
  database: perks
  username: root
  password: password

prices:
  telekinesis: 1200000
  glow: 540000
  fly: 150000
  # ... etc
```

### Messages (messages.yml)
```yaml
sounds:
  enabled: true
  menu-click: 'UI_BUTTON_CLICK'
  perk-activate: 'ENTITY_PLAYER_LEVELUP'

particles:
  enabled: true
  perk-activate: 'VILLAGER_HAPPY'
  perk-deactivate: 'SMOKE_NORMAL'
```

## 🚀 Performance Features

- **Asynchronous Database Operations** - Non-blocking data persistence
- **Intelligent Caching** - Reduced database queries
- **Version Compatibility Layer** - Automatic API adaptation
- **Optimized GUI Rendering** - Efficient menu generation
- **Memory Management** - Automatic cleanup and resource management

## 🔄 Version Migration

### From v1.x to v2.0.0
The plugin will automatically:
1. Create new database tables if needed
2. Migrate existing data (if any)
3. Update configuration files
4. Preserve existing perk ownerships

### Backup Recommendation
Always backup your:
- Database before major updates
- Configuration files
- Player data

## 🐛 Troubleshooting

### Common Issues

**Plugin won't load:**
- Check Java version (requires 17+)
- Verify Vault is installed and working
- Check console for specific error messages

**Database connection failed:**
- Verify MySQL credentials in config.yml
- Ensure MySQL server is running
- Check firewall/port settings

**Perks not working:**
- Verify player has correct permissions
- Check if perk is activated (not just owned)
- Review console for error messages

**GUI not displaying correctly:**
- Update to latest server version
- Check for resource pack conflicts
- Verify client-server version compatibility

### Support
- Check console logs for detailed error information
- Verify all dependencies are installed and up-to-date
- Test with minimal plugin setup to isolate conflicts

## 📈 Roadmap

- **Perk Levels** - Upgradeable perk tiers
- **Custom Perks API** - Developer API for custom perks
- **Web Interface** - Online administration panel
- **Advanced Statistics** - Detailed usage analytics
- **Perk Combinations** - Synergy between different perks

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🤝 Contributing

Contributions are welcome! Please feel free to submit pull requests or open issues for bugs and feature requests.

---

**Made with ❤️ for the Minecraft community**